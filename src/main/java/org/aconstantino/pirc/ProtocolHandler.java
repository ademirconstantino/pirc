/**
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

import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.SwingUtilities;

/**
 * @author Ademir Constantino Filho <a href="mailto:ziegfried@techie.com">ziegfried@techie.com</a>
 * 17/09/2002 -  11:22:59
 */

public class ProtocolHandler
	extends Thread
	implements ProtocolHandlerConstants, ProtocolHandlerListener {

	public void run() {
		try {
			while ((socketData = ircsocket.getInputData()) != null) {
				final String line = socketData;
				try {
					/* the handlers update the Swing windows */
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
							try {
								parse(line);
							} catch (IRCSocketException e) {
							} catch (RuntimeException e) {
								e.printStackTrace();
							}
						}
					});
				} catch (InterruptedException e) {
					break;
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		} catch (IRCSocketException e) {
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pircMain.onDisconnected();
			}
		});
	}

	/**
	 * Parses a line sent by the server
	 * [':' prefix ' '] command [' ' params]
	 */
	private void parse(String line) throws IRCSocketException {
		command = "";
		param = "";
		prefix = "";
		isNumeric = false;

		String rest = line;
		if (rest.startsWith(":")) {
			int index = rest.indexOf(' ');
			if (index == -1) {
				return;
			}
			prefix = rest.substring(1, index);
			rest = rest.substring(index + 1).trim();
		}
		int index = rest.indexOf(' ');
		if (index == -1) {
			command = rest;
		} else {
			command = rest.substring(0, index);
			param = rest.substring(index + 1).trim();
		}
		if (command.length() == 0) {
			return;
		}

		if (Character.isDigit(command.charAt(0))) {
			try {
				numCommand = Integer.parseInt(command);
				isNumeric = true;
			} catch (NumberFormatException e) {
				isNumeric = false;
			}
		}
		/**
		 * Parse and ping response
		 */
		if (command.equalsIgnoreCase("PING")) {
			ircsocket.println("PONG " + trailing(param));
			onSendPong();
		} else if (command.equalsIgnoreCase("PRIVMSG")) {
			String to = firstToken(param);
			String msg = trailing(param);
			String from = nickOf(prefix);
			if (msg.startsWith("\u0001")) {
				onCtcp(to, msg.replaceAll("\u0001", ""), from);
			} else if (isChannel(to)) {
				onChanMsg(to, msg, from);
			} else {
				onPrivMsg(from, msg);
			}
		}
		/**
		 * parse notice auth commands
		 */
		else if (
			command.equalsIgnoreCase("NOTICE") && param.startsWith("AUTH")) {
			onUnknown(trailing(param));
		} else if (command.equalsIgnoreCase("NOTICE")) {
			onNotice(firstToken(param), trailing(param), nickOf(prefix));
		}
		/**
		 * Parse server welcome msg
		 */
		else if (isNumeric && numCommand == WELCOME) {
			registered = true;
			pircMain.setMyNickName(firstToken(param));
			onWelcome(trailing(param));
		}
		/**
		 * Parse server your host msg
		 */
		else if (isNumeric && numCommand == YOUR_HOST) {
			onYourHost(trailing(param));
		}
		/**
		 * Parse server created msg
		 */
		else if (isNumeric && numCommand == CREATED) {
			onCreated(trailing(param));
		}
		/**
		 * Parse server my info msg
		 */
		else if (isNumeric && numCommand == MY_INFO) {
			onMyInfo(trailing(param));
		}
		/**
		 * Parse server bounce msg
		 */
		else if (isNumeric && numCommand == BOUNCE) {
			onBounce(trailing(param));
		} else if (isNumeric && numCommand == ERR_NICK_NAME_IN_USE) {
			StringTokenizer st = new StringTokenizer(param);
			st.nextToken();
			onNickInUse(st.hasMoreTokens() ? st.nextToken() : "");
		} else if (command.equalsIgnoreCase("JOIN")) {
			onJoin(firstToken(trailing(param)), prefix);
		} else if (command.equalsIgnoreCase("PART")) {
			onPart(firstToken(param), prefix);
		} else if (command.equalsIgnoreCase("NICK")) {
			onNick(trailing(param), prefix);
		} else if (command.equalsIgnoreCase("QUIT")) {
			onQuit(prefix, trailing(param));
		} else if (command.equalsIgnoreCase("KICK")) {
			StringTokenizer st = new StringTokenizer(param);
			String chan = st.nextToken();
			String who = st.nextToken();
			onKick(chan, who, nickOf(prefix), trailing(param));
		} else if (command.equalsIgnoreCase("MODE")) {
			String target = firstToken(param);
			String modes = param.substring(target.length()).trim();
			onMode(target, modes, nickOf(prefix));
		} else if (command.equalsIgnoreCase("TOPIC")) {
			onTopicSet(nickOf(prefix), firstToken(param), trailing(param));
		} else if (isNumeric && numCommand == NAM_REPLY) {
			/* <me> ( "=" / "*" / "@" ) <channel> :<nicks> */
			StringTokenizer st = new StringTokenizer(param);
			st.nextToken();
			st.nextToken();
			String chan = st.nextToken();
			NickNameList nicks = (NickNameList) names.get(chan);
			if (nicks == null) {
				nicks = new NickNameList();
				names.put(chan, nicks);
			}
			st = new StringTokenizer(trailing(param));
			while (st.hasMoreTokens()) {
				nicks.addNickName(st.nextToken());
			}
		} else if (isNumeric && numCommand == END_OF_NAMES) {
			StringTokenizer st = new StringTokenizer(param);
			st.nextToken();
			String chan = st.nextToken();
			NickNameList nicks = (NickNameList) names.remove(chan);
			if (nicks != null) {
				onNamReply(nicks, chan);
			}
		} else if (isNumeric && numCommand == TOPIC) {
			StringTokenizer st = new StringTokenizer(param);
			st.nextToken();
			String chan = st.nextToken();
			onTopic(chan, trailing(param));
		} else if (isNumeric) {
			/* removes the <me> target and shows the reply */
			String msg = param.substring(firstToken(param).length()).trim();
			int colon = msg.indexOf(':');
			if (colon > -1 && (colon == 0 || msg.charAt(colon - 1) == ' ')) {
				msg = msg.substring(0, colon) + msg.substring(colon + 1);
			}
			onUnknown(msg);
		} else {
			onUnknown(line);
		}
	}

	/**
	 * Returns the trailing parameter (text after " :")
	 */
	private String trailing(String p) {
		if (p.startsWith(":")) {
			return p.substring(1);
		}
		int index = p.indexOf(" :");
		if (index > -1) {
			return p.substring(index + 2);
		}
		return p;
	}

	private String firstToken(String p) {
		StringTokenizer st = new StringTokenizer(p);
		if (!st.hasMoreTokens()) {
			return "";
		}
		String s = st.nextToken();
		return s.startsWith(":") ? s.substring(1) : s;
	}

	private String nickOf(String prefix) {
		int index = prefix.indexOf('!');
		return index > -1 ? prefix.substring(0, index) : prefix;
	}

	private String hostOf(String prefix) {
		int index = prefix.indexOf('!');
		return index > -1 ? prefix.substring(index + 1) : "";
	}

	private boolean isChannel(String name) {
		return name.length() > 0 && "#&+!".indexOf(name.charAt(0)) > -1;
	}

	private boolean isMe(String nick) {
		return nick.equalsIgnoreCase(pircMain.getMyNickName());
	}

	private String strip(String s) {
		return colorStrip.stripColor(s);
	}

	private PIRCChannelWindow getChannelWindow(String channel) {
		return pircMain.getChannelWindow(new Channel(channel));
	}

	private IRCSocket ircsocket;

	/**
	 * Returns the ircsocket.
	 * @return IRCSocket
	 */
	public IRCSocket getIrcsocket() {
		return ircsocket;
	}

	public void setPIRCConnectionWindow(PIRCConnectionWindow conWindow) {
		this.conWindow = conWindow;
	}

	public void setPIRCFrame(PIRCFrame pircMain) {
		this.pircMain = pircMain;
	}

	/**
	 * Sets the ircsocket.
	 * @param ircsocket The ircsocket to set
	 */
	public void setIrcsocket(IRCSocket ircsocket) {
		this.ircsocket = ircsocket;
	}

	public void onUnknown(String unknownString) {
		pircMain.getMainWindow().append("-\r\n" + strip(unknownString));
	}

	public void onWelcome(String welcomeStr) {
		pircMain.getMainWindow().append("-\r\n" + strip(welcomeStr));
	}

	public void onYourHost(String params) {
		pircMain.getMainWindow().append("-\r\n" + strip(params));
	}

	public void onCreated(String params) {
		pircMain.getMainWindow().append("-\r\n" + strip(params));
	}

	public void onBounce(String serverName) {
		pircMain.getMainWindow().append("-\r\n" + strip(serverName));
	}

	public void onPing() {
		pircMain.getMainWindow().append("-\r\n" + "Ping");
	}

	public void onSendPong() {
		pircMain.getMainWindow().append("-\r\n" + "Pong");
	}

	public void onMyInfo(String params) {
		pircMain.getMainWindow().append("-\r\n" + strip(params));
	}

	public void onNickInUse(String nick) {
		String alternative =
			conWindow == null ? "" : conWindow.getAlternative().trim();
		if (!registered
			&& alternative.length() > 0
			&& !alternative.equalsIgnoreCase(nick)) {
			pircMain.getMainWindow().append(
				"-\r\n" + nick + " já está em uso, tentando " + alternative);
			pircMain.setMyNickName(alternative);
			try {
				ircsocket.println("NICK " + alternative);
			} catch (IRCSocketException e) {
			}
		} else {
			pircMain.getMainWindow().append(
				"-\r\n" + nick + " já está em uso, use /nick <novo nick>");
		}
	}

	public void onJoin(String channel, String prefix) {
		String nick = nickOf(prefix);
		if (!pircMain.getChannels().getChannelExists(channel)) {
			if (isMe(nick)) {
				Channel c = new Channel();
				c.setName(channel);
				pircMain.getChannels().putChannel(c);
				pircMain.createChannelWindow(c);
			}
		} else if (!isMe(nick)) {
			String host = hostOf(prefix);
			NickNameList list =
				pircMain.getChannels().getChannel(channel).getNickList();
			if (!list.containsNickName(nick)) {
				list.addNickName(nick);
				list.organizeByStatus();
			}
			PIRCChannelWindow window = getChannelWindow(channel);
			if (window != null) {
				window.updateNicks();
				window.append(
					nick + " (" + host + ") " + " Entrou no " + channel);
			}
		}
	}

	public void onPart(String channel, String prefix) {
		String host = hostOf(prefix);
		String nick = nickOf(prefix);
		if (!pircMain.getChannels().getChannelExists(channel)) {
			return;
		}
		if (!isMe(nick)) {
			NickNameList list =
				pircMain.getChannels().getChannel(channel).getNickList();
			list.removeNickname(nick);
			list.organizeByStatus();
			PIRCChannelWindow window = getChannelWindow(channel);
			if (window != null) {
				window.updateNicks();
				window.append(
					nick + " (" + host + ") " + " saiu do " + channel);
			}
		} else {
			pircMain.removeChannel(channel);
		}
	}

	public void onNick(String newNick, String oldNick) {
		String host = hostOf(oldNick);
		oldNick = nickOf(oldNick);
		if (isMe(oldNick)) {
			pircMain.setMyNickName(newNick);
			pircMain.getMainWindow().append(
				"-\r\nVocê agora é conhecido como " + newNick);
		}
		for (int i = 0; i < pircMain.getChannelsIn().size(); i++) {
			String currChan = (String) pircMain.getChannelsIn().get(i);
			Channel c = pircMain.getChannels().getChannel(currChan);
			if (c == null || !c.getNickList().containsNickName(oldNick)) {
				continue;
			}
			c.getNickList().updateNickName(oldNick, newNick);
			PIRCChannelWindow window = getChannelWindow(currChan);
			if (window != null) {
				window.append(
					oldNick
						+ " ("
						+ host
						+ ") "
						+ " mudou o nick para "
						+ newNick);
				window.updateNicks();
			}
		}
		pircMain.renamePrivateWindow(oldNick, newNick);
	}

	public void onQuit(String prefix, String reason) {
		String host = hostOf(prefix);
		String nick = nickOf(prefix);
		for (int i = 0; i < pircMain.getChannelsIn().size(); i++) {
			String currChan = (String) pircMain.getChannelsIn().get(i);
			Channel c = pircMain.getChannels().getChannel(currChan);
			if (c == null || !c.getNickList().containsNickName(nick)) {
				continue;
			}
			c.getNickList().removeNickname(nick);
			PIRCChannelWindow window = getChannelWindow(currChan);
			if (window != null) {
				window.updateNicks();
				window.append(
					nick + " (" + host + ") " + " saiu do IRC (" + strip(reason) + ")");
			}
		}
		PrivateMessageSession session = pircMain.getPrivateWindow(nick, false);
		if (session != null) {
			session.append(nick + " saiu do IRC (" + strip(reason) + ")");
		}
	}

	public void onKick(String channel, String who, String by, String reason) {
		if (!pircMain.getChannels().getChannelExists(channel)) {
			return;
		}
		if (isMe(who)) {
			pircMain.removeChannel(channel);
			pircMain.getMainWindow().append(
				"-\r\nVocê foi kickado do "
					+ channel
					+ " por "
					+ by
					+ " ("
					+ strip(reason)
					+ ")");
		} else {
			NickNameList list =
				pircMain.getChannels().getChannel(channel).getNickList();
			list.removeNickname(who);
			PIRCChannelWindow window = getChannelWindow(channel);
			if (window != null) {
				window.updateNicks();
				window.append(
					who + " foi kickado por " + by + " (" + strip(reason) + ")");
			}
		}
	}

	public void onMode(String target, String modes, String by) {
		PIRCChannelWindow window = getChannelWindow(target);
		if (isChannel(target) && window != null) {
			window.append(by + " modificou o modo: " + modes);
			/* updates the nicknames status (@ and +) */
			try {
				ircsocket.println("NAMES " + target);
			} catch (IRCSocketException e) {
			}
		} else {
			pircMain.getMainWindow().append(
				"-\r\n" + by + " modificou o modo de " + target + ": " + modes);
		}
	}

	public void onNamReply(NickNameList nicks, String chan) {
		if (pircMain.getChannels().getChannelExists(chan)) {
			pircMain.getChannels().getChannel(chan).setNickList(nicks);
			pircMain
				.getChannels()
				.getChannel(chan)
				.getNickList()
				.organizeByStatus();
			PIRCChannelWindow window = getChannelWindow(chan);
			if (window != null) {
				window.updateNicks();
			}
		} else {
			onUnknown(chan + ": " + nicks);
		}
	}

	public void onChanMsg(String to, String msg, String from) {
		PIRCChannelWindow window = getChannelWindow(to);
		if (window != null) {
			window.append("<" + from + "> " + strip(msg));
		}
	}

	public void onPrivMsg(String from, String msg) {
		pircMain.getPrivateWindow(from, true).append(
			"<" + from + "> " + strip(msg));
	}

	public void onNotice(String to, String msg, String from) {
		PIRCChannelWindow window = isChannel(to) ? getChannelWindow(to) : null;
		if (window != null) {
			window.append("-" + from + "- " + strip(msg));
		} else {
			pircMain.getMainWindow().append("-" + from + "- " + strip(msg));
		}
	}

	public void onCtcp(String to, String msg, String from) {
		String ctcp = firstToken(msg).toUpperCase();
		String args = msg.substring(firstToken(msg).length()).trim();
		try {
			if (ctcp.equals("ACTION")) {
				String text = "* " + from + " " + strip(args);
				if (isChannel(to)) {
					PIRCChannelWindow window = getChannelWindow(to);
					if (window != null) {
						window.append(text);
					}
				} else {
					pircMain.getPrivateWindow(from, true).append(text);
				}
				return;
			} else if (ctcp.equals("VERSION")) {
				ircsocket.println(
					"NOTICE "
						+ from
						+ " :\u0001VERSION pIRC 0.7BETA - Java "
						+ System.getProperty("java.version")
						+ " - "
						+ System.getProperty("os.name")
						+ "\u0001");
			} else if (ctcp.equals("PING")) {
				ircsocket.println(
					"NOTICE " + from + " :\u0001PING " + args + "\u0001");
			}
		} catch (IRCSocketException e) {
		}
		pircMain.getMainWindow().append(
			"-\r\n[" + ctcp + "] de " + from);
	}

	public void onTopic(String channel, String param) {
		if (pircMain.getChannels().getChannelExists(channel)) {
			pircMain.getChannels().getChannel(channel).setTopic(strip(param));
			PIRCChannelWindow window = getChannelWindow(channel);
			if (window != null) {
				window.updateTopic();
			}
		}
	}

	public void onTopicSet(String by, String chan, String newTopic) {
		if (pircMain.getChannels().getChannelExists(chan)) {
			pircMain.getChannels().getChannel(chan).setTopic(strip(newTopic));
			PIRCChannelWindow window = getChannelWindow(chan);
			if (window != null) {
				window.append(
					by + " Modificou o tópico para: " + strip(newTopic));
			}
		}
	}

	private String socketData;
	private String command, param, prefix;
	private int numCommand;
	private PIRCConnectionWindow conWindow;
	private boolean isNumeric;
	private boolean registered;
	private PIRCFrame pircMain;
	private final Hashtable names = new Hashtable();
	private final ColorStrip colorStrip = new ColorStrip();
}
