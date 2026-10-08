/*
  *  pIRC - Cliente de IRC desenvolvido em Java.
  *
  * Copyright (C) 2002 -  Ademir Constantino Filho
  *
  * Este programa é software livre; você pode redistribuí-lo e/ou
  * modificá-lo sob os termos da Licença Pública Geral GNU, conforme
  * publicada pela Free Software Foundation; tanto a versão 2 da
  * Licença como (a seu critério) qualquer versão mais nova.
  *
  * Este programa é distribuído na expectativa de ser útil, mas SEM
  * QUALQUER GARANTIA; sem mesmo a garantia implícita de
  * COMERCIALIZAÇÃO ou de ADEQUAÇÃO A QUALQUER PROPÓSITO EM
  * PARTICULAR. Consulte a Licença Pública Geral GNU para obter mais
  * detalhes.
  *
  * Você deve ter recebido uma cópia da Licença Pública Geral GNU
  * junto com este programa; se não, escreva para a Free Software
  * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
  * 02111-1307, USA.
  *
  * Você pode entrar em contato pelo endereço de email:
  * ziegfried@onda.com.br
  *
  */


package org.aconstantino.pirc;

import static org.aconstantino.pirc.ProtocolHandlerConstants.BOUNCE;
import static org.aconstantino.pirc.ProtocolHandlerConstants.CREATED;
import static org.aconstantino.pirc.ProtocolHandlerConstants.END_OF_NAMES;
import static org.aconstantino.pirc.ProtocolHandlerConstants.ERR_NICK_NAME_IN_USE;
import static org.aconstantino.pirc.ProtocolHandlerConstants.MY_INFO;
import static org.aconstantino.pirc.ProtocolHandlerConstants.NAM_REPLY;
import static org.aconstantino.pirc.ProtocolHandlerConstants.TOPIC;
import static org.aconstantino.pirc.ProtocolHandlerConstants.WELCOME;
import static org.aconstantino.pirc.ProtocolHandlerConstants.YOUR_HOST;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.SwingUtilities;

/**
 * Reads the lines sent by the server and updates the pIRC windows.
 * The lines are read in a background thread and handled in the Swing
 * event dispatch thread.
 * @author Ademir Constantino Filho <a href="mailto:ziegfried@techie.com">ziegfried@techie.com</a>
 * 17/09/2002 -  11:22:59
 */

public final class ProtocolHandler implements Runnable, ProtocolHandlerListener {

	public ProtocolHandler(
		PIRCFrame pircMain,
		IRCSocket ircsocket,
		PIRCConnectionWindow conWindow) {
		this.pircMain = pircMain;
		this.ircsocket = ircsocket;
		this.conWindow = conWindow;
	}

	@Override
	public void run() {
		try {
			String line;
			while ((line = ircsocket.readLine()) != null) {
				IrcMessage message = IrcMessage.parse(line);
				if (message == null) {
					continue;
				}
				String raw = line;
				/* the handlers update the Swing windows */
				SwingUtilities.invokeAndWait(() -> {
					try {
						handle(message, raw);
					} catch (RuntimeException e) {
						e.printStackTrace();
					}
				});
			}
		} catch (IRCSocketException e) {
			// connection closed
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(() -> pircMain.onDisconnected(ircsocket));
	}

	/**
	 * Handles a message sent by the server
	 * @param m the parsed message
	 * @param line the raw line, shown when the command is unknown
	 */
	private void handle(IrcMessage m, String line) {
		switch (m.command()) {
			case "PING" -> {
				send("PONG :" + m.trailing());
				onSendPong();
			}
			case "PRIVMSG" -> {
				String to = m.param(0);
				String msg = m.trailing();
				if (msg.startsWith("\u0001")) {
					onCtcp(to, msg.replace("\u0001", ""), m.nick());
				} else if (isChannel(to)) {
					onChanMsg(to, msg, m.nick());
				} else {
					onPrivMsg(m.nick(), msg);
				}
			}
			case "NOTICE" -> {
				if (m.param(0).equals("AUTH")) {
					onUnknown(m.trailing());
				} else {
					onNotice(m.param(0), m.trailing(), m.nick());
				}
			}
			case "JOIN" -> onJoin(m.param(0), m.prefix());
			case "PART" -> onPart(m.param(0), m.prefix());
			case "NICK" -> onNick(m.param(0), m.prefix());
			case "QUIT" -> onQuit(m.prefix(), m.trailing());
			case "KICK" -> onKick(m.param(0), m.param(1), m.nick(), m.param(2));
			case "MODE" -> onMode(m.param(0), m.paramsFrom(1), m.nick());
			case "TOPIC" -> onTopicSet(m.nick(), m.param(0), m.trailing());
			default -> {
				if (m.isNumeric()) {
					handleNumeric(m);
				} else {
					onUnknown(line);
				}
			}
		}
	}

	/**
	 * Handles the numeric replies, the first parameter is always my nickname
	 */
	private void handleNumeric(IrcMessage m) {
		switch (m.numeric()) {
			case WELCOME -> {
				registered = true;
				pircMain.setMyNickName(m.param(0));
				onWelcome(m.trailing());
			}
			case YOUR_HOST -> onYourHost(m.trailing());
			case CREATED -> onCreated(m.trailing());
			case MY_INFO -> onMyInfo(m.paramsFrom(1));
			case BOUNCE -> onBounce(m.paramsFrom(1));
			case ERR_NICK_NAME_IN_USE -> onNickInUse(m.param(1));
			case NAM_REPLY -> {
				/* <me> ( "=" / "*" / "@" ) <channel> :<nicks> */
				String chan = m.param(m.params().size() - 2);
				NickNameList nicks =
					names.computeIfAbsent(chan, c -> new NickNameList());
				for (String nick : m.trailing().split(" ")) {
					if (!nick.isEmpty()) {
						nicks.addNickName(nick);
					}
				}
			}
			case END_OF_NAMES -> {
				String chan = m.param(1);
				NickNameList nicks = names.remove(chan);
				if (nicks != null) {
					onNamReply(nicks, chan);
				}
			}
			case TOPIC -> onTopic(m.param(1), m.trailing());
			/* removes the <me> target and shows the reply */
			default -> onUnknown(m.paramsFrom(1));
		}
	}

	private void send(String line) {
		try {
			ircsocket.println(line);
		} catch (IRCSocketException e) {
			// the reader thread notices the closed connection
		}
	}

	private static boolean isChannel(String name) {
		return !name.isEmpty() && "#&+!".indexOf(name.charAt(0)) > -1;
	}

	private boolean isMe(String nick) {
		return nick.equalsIgnoreCase(pircMain.getMyNickName());
	}

	private static String strip(String s) {
		return ColorStrip.stripColor(s);
	}

	private static String nickOf(String prefix) {
		int index = prefix.indexOf('!');
		return index > -1 ? prefix.substring(0, index) : prefix;
	}

	private static String hostOf(String prefix) {
		int index = prefix.indexOf('!');
		return index > -1 ? prefix.substring(index + 1) : "";
	}

	private Channels channels() {
		return pircMain.getChannels();
	}

	private void status(String text) {
		pircMain.getMainWindow().append("-\r\n" + text);
	}

	@Override
	public void onUnknown(String unknownString) {
		status(strip(unknownString));
	}

	@Override
	public void onWelcome(String welcomeStr) {
		status(strip(welcomeStr));
	}

	@Override
	public void onYourHost(String params) {
		status(strip(params));
	}

	@Override
	public void onCreated(String params) {
		status(strip(params));
	}

	@Override
	public void onBounce(String serverName) {
		status(strip(serverName));
	}

	@Override
	public void onPing() {
		status("Ping");
	}

	@Override
	public void onSendPong() {
		status("Pong");
	}

	@Override
	public void onMyInfo(String params) {
		status(strip(params));
	}

	@Override
	public void onNickInUse(String nick) {
		String alternative =
			conWindow == null ? "" : conWindow.getAlternative().trim();
		if (!registered
			&& !alternative.isEmpty()
			&& !alternative.equalsIgnoreCase(nick)) {
			status(nick + " já está em uso, tentando " + alternative);
			pircMain.setMyNickName(alternative);
			send("NICK " + alternative);
		} else {
			status(nick + " já está em uso, use /nick <novo nick>");
		}
	}

	@Override
	public void onJoin(String channel, String prefix) {
		String nick = nickOf(prefix);
		Channel c = channels().get(channel);
		if (c == null) {
			if (isMe(nick)) {
				c = new Channel(channel);
				channels().put(c);
				pircMain.createChannelWindow(c);
			}
		} else if (!isMe(nick)) {
			NickNameList list = c.getNickList();
			if (!list.containsNickName(nick)) {
				list.addNickName(nick);
				list.organizeByStatus();
			}
			PIRCChannelWindow window = pircMain.getChannelWindow(channel);
			if (window != null) {
				window.updateNicks();
				window.append(
					nick + " (" + hostOf(prefix) + ")  Entrou no " + channel);
			}
		}
	}

	@Override
	public void onPart(String channel, String prefix) {
		String nick = nickOf(prefix);
		Channel c = channels().get(channel);
		if (c == null) {
			return;
		}
		if (isMe(nick)) {
			pircMain.removeChannel(channel);
			return;
		}
		NickNameList list = c.getNickList();
		list.removeNickname(nick);
		list.organizeByStatus();
		PIRCChannelWindow window = pircMain.getChannelWindow(channel);
		if (window != null) {
			window.updateNicks();
			window.append(
				nick + " (" + hostOf(prefix) + ")  saiu do " + channel);
		}
	}

	@Override
	public void onNick(String newNick, String oldNick) {
		String host = hostOf(oldNick);
		oldNick = nickOf(oldNick);
		if (isMe(oldNick)) {
			pircMain.setMyNickName(newNick);
			status("Você agora é conhecido como " + newNick);
		}
		for (Channel c : channels().all()) {
			if (!c.getNickList().containsNickName(oldNick)) {
				continue;
			}
			c.getNickList().updateNickName(oldNick, newNick);
			PIRCChannelWindow window = pircMain.getChannelWindow(c.getName());
			if (window != null) {
				window.append(
					oldNick + " (" + host + ")  mudou o nick para " + newNick);
				window.updateNicks();
			}
		}
		pircMain.renamePrivateWindow(oldNick, newNick);
	}

	@Override
	public void onQuit(String prefix, String reason) {
		String host = hostOf(prefix);
		String nick = nickOf(prefix);
		for (Channel c : channels().all()) {
			if (!c.getNickList().containsNickName(nick)) {
				continue;
			}
			c.getNickList().removeNickname(nick);
			PIRCChannelWindow window = pircMain.getChannelWindow(c.getName());
			if (window != null) {
				window.updateNicks();
				window.append(
					nick + " (" + host + ")  saiu do IRC (" + strip(reason) + ")");
			}
		}
		PrivateMessageSession session = pircMain.getPrivateWindow(nick, false);
		if (session != null) {
			session.append(nick + " saiu do IRC (" + strip(reason) + ")");
		}
	}

	@Override
	public void onKick(String channel, String who, String by, String reason) {
		Channel c = channels().get(channel);
		if (c == null) {
			return;
		}
		if (isMe(who)) {
			pircMain.removeChannel(channel);
			status(
				"Você foi kickado do "
					+ channel
					+ " por "
					+ by
					+ " ("
					+ strip(reason)
					+ ")");
		} else {
			c.getNickList().removeNickname(who);
			PIRCChannelWindow window = pircMain.getChannelWindow(channel);
			if (window != null) {
				window.updateNicks();
				window.append(
					who + " foi kickado por " + by + " (" + strip(reason) + ")");
			}
		}
	}

	@Override
	public void onMode(String target, String modes, String by) {
		PIRCChannelWindow window =
			isChannel(target) ? pircMain.getChannelWindow(target) : null;
		if (window != null) {
			window.append(by + " modificou o modo: " + modes);
			/* updates the nicknames status (@ and +) */
			send("NAMES " + target);
		} else {
			status(by + " modificou o modo de " + target + ": " + modes);
		}
	}

	@Override
	public void onNamReply(NickNameList nicks, String chan) {
		Channel c = channels().get(chan);
		if (c != null) {
			nicks.organizeByStatus();
			c.setNickList(nicks);
			PIRCChannelWindow window = pircMain.getChannelWindow(chan);
			if (window != null) {
				window.updateNicks();
			}
		} else {
			onUnknown(chan + ": " + nicks);
		}
	}

	@Override
	public void onChanMsg(String to, String msg, String from) {
		PIRCChannelWindow window = pircMain.getChannelWindow(to);
		if (window != null) {
			window.append("<" + from + "> " + strip(msg));
		}
	}

	@Override
	public void onPrivMsg(String from, String msg) {
		pircMain.getPrivateWindow(from, true).append(
			"<" + from + "> " + strip(msg));
	}

	@Override
	public void onNotice(String to, String msg, String from) {
		PIRCChannelWindow window =
			isChannel(to) ? pircMain.getChannelWindow(to) : null;
		if (window != null) {
			window.append("-" + from + "- " + strip(msg));
		} else {
			pircMain.getMainWindow().append("-" + from + "- " + strip(msg));
		}
	}

	@Override
	public void onCtcp(String to, String msg, String from) {
		int space = msg.indexOf(' ');
		String ctcp =
			(space == -1 ? msg : msg.substring(0, space)).toUpperCase(Locale.ROOT);
		String args = space == -1 ? "" : msg.substring(space + 1).trim();
		switch (ctcp) {
			case "ACTION" -> {
				String text = "* " + from + " " + strip(args);
				if (isChannel(to)) {
					PIRCChannelWindow window = pircMain.getChannelWindow(to);
					if (window != null) {
						window.append(text);
					}
				} else {
					pircMain.getPrivateWindow(from, true).append(text);
				}
				return;
			}
			case "VERSION" -> send(
				"NOTICE "
					+ from
					+ " :\u0001VERSION pIRC "
					+ PIRCFrame.VERSION
					+ " - Java "
					+ System.getProperty("java.version")
					+ " - "
					+ System.getProperty("os.name")
					+ "\u0001");
			case "PING" -> send("NOTICE " + from + " :\u0001PING " + args + "\u0001");
			default -> {
				// other CTCP requests are only shown
			}
		}
		status("[" + ctcp + "] de " + from);
	}

	@Override
	public void onTopic(String channel, String param) {
		Channel c = channels().get(channel);
		if (c != null) {
			c.setTopic(strip(param));
			PIRCChannelWindow window = pircMain.getChannelWindow(channel);
			if (window != null) {
				window.updateTopic();
			}
		}
	}

	@Override
	public void onTopicSet(String by, String chan, String newTopic) {
		Channel c = channels().get(chan);
		if (c != null) {
			c.setTopic(strip(newTopic));
			PIRCChannelWindow window = pircMain.getChannelWindow(chan);
			if (window != null) {
				window.append(
					by + " Modificou o tópico para: " + strip(newTopic));
			}
		}
	}

	private final PIRCFrame pircMain;
	private final IRCSocket ircsocket;
	private final PIRCConnectionWindow conWindow;
	private final Map<String, NickNameList> names = new HashMap<>();
	private boolean registered;
}
