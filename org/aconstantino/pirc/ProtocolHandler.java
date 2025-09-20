head	1.2;
access;
symbols;
locks; strict;
comment	@# @;
expand	@o@;


1.2
date	2004.11.13.20.36.01;	author ziegfried;	state dead;
branches;
next	1.1;

1.1
date	2002.12.11.05.49.16;	author ziegfried;	state Exp;
branches;
next	;


desc
@@


1.2
log
@*** empty log message ***
@
text
@/**
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
  * ziegfried@@onda.com.br
  *
  */

package org.aconstantino.pirc;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * @@author Ademir Constantino Filho <a href="mailto:ziegfried@@techie.com">ziegfried@@techie.com</a>
 * 17/09/2002 -  11:22:59 
 */

public class ProtocolHandler
	extends Thread
	implements ProtocolHandlerConstants {

	public void run() {
		try {
			while ((socketData = ircsocket.getInputData()) != null) {
				st = new StringTokenizer(socketData, " \r\n");
				token = st.countTokens();
				if (socketData.startsWith(":") && token >= 3) {
					String temp = st.nextToken();
					int index = temp.indexOf(":");
					if (index != -1)
						prefix = temp.substring(index + 1);
					temp = st.nextToken();
					command = temp;
					temp = st.nextToken("\n");
					param = temp;
				} else if (!socketData.startsWith(":") && token >= 2) {
					String temp = st.nextToken();
					command = temp;
					param = st.nextToken("\n");
				}
				if (command != null)
					command.trim();
				if (param != null)
					param.trim();
				if (prefix != null)
					prefix.trim();
				 //param = new ColorStrip().stripColor(param);
				//prefix = new ColorStrip().stripColor(prefix);				
				pircMain.getMainWindow().append(param);
				
				char xtemp[] = command.substring(0, 1).toCharArray();
				if (!Character.isLetter(xtemp[0])) {
					try {
						numCommand = Integer.parseInt(command);
						isNumeric = true;
					} catch (Exception e) {
						isNumeric = false;
					}
				}
				/**
				 * Parse and ping response
				 */
				if (command.equalsIgnoreCase("PING")) {
					if (param.indexOf(':') > -1) {
						int temp = param.indexOf(':');
						param = param.substring(temp + 1, param.length());
					}
					ircsocket.println("PONG " + param);
					onSendPong();
				} else if (command.equalsIgnoreCase("PRIVMSG")) {
					StringTokenizer st = new StringTokenizer(param);
					String to = st.nextToken();
					int msgPoint = param.indexOf(':');
					String msg = param.substring(msgPoint, param.length());
					msg = msg.substring(1, msg.length());
					String from =
						prefix.substring(
							prefix.indexOf(':') + 1,
							prefix.indexOf('!'));
					if (to.indexOf('#') > -1) {
						onChanMsg(to, msg, from);
					} else {
						if(to.equals(pircMain.getMyNickName())) {
						}
					}
					command = "";
					param = "";
					prefix = "";
				}
				/**
				 * parse notice auth commands 
				 */
				else if (
					!isNumeric
						&& command.equalsIgnoreCase("NOTICE")
						&& param.startsWith("AUTH")) {
					if (param.indexOf(':') > -1) {
						int temp = param.indexOf(':');
						param = param.substring(temp + 1, param.length());
					}
					onUnknown(param);
					command = "";
					param = "";
					prefix = "";
				}
				/**
				 * Parse server welcome msg
				 */
				else if (isNumeric && numCommand == WELCOME) {
					if (param.indexOf(':') > -1) {
						int temp = param.indexOf(':');
						param = param.substring(temp + 1, param.length());
					}
					onWelcome(param);
					command = "";
					param = "";
					prefix = "";
				}
				/**
				 * Parse server your host msg
				 */
				else if (isNumeric && numCommand == YOUR_HOST) {
					if (param.indexOf(':') > -1) {
						int temp = param.indexOf(':');
						param = param.substring(temp + 1, param.length());
					}
					onYourHost(param);
					command = "";
					param = "";
					prefix = "";
				}
				/**
				 * Parse server created msg
				 */
				else if (isNumeric && numCommand == CREATED) {
					if (param.indexOf(':') > -1) {
						int temp = param.indexOf(':');
						param = param.substring(temp + 1, param.length());
					}
					onCreated(param);
					command = "";
					param = "";
					prefix = "";
				}
				/**
				 * Parse server my info msg
				 */
				else if (isNumeric && numCommand == MY_INFO) {
					if (param.indexOf(':') > -1) {
						int temp = param.indexOf(':');
						param = param.substring(temp + 1, param.length());
					}
					onMyInfo(param);
					command = "";
					param = "";
					prefix = "";
				}
				/**
				 * Parse server bounce msg
				 */

				else if (isNumeric && numCommand == BOUNCE) {
					int portBounce = 1;
					if (param.indexOf(':') > -1) {
						int temp = param.indexOf(':');
						param = param.substring(temp + 1, param.length());
						StringTokenizer stemp = new StringTokenizer(param);
					}
					onBounce(param);
					command = "";
					param = "";
					prefix = "";
				} else if (command.equalsIgnoreCase("JOIN")) {
					if (param.indexOf(':') > -1) {
						int temp = param.indexOf(':');
						param = param.substring(temp + 1, param.length());
						StringTokenizer stemp = new StringTokenizer(param);
					}
					onJoin(param, prefix);
					command = "";
					param = "";
					prefix = "";
				} else if (command.equalsIgnoreCase("PART")) {
					param = param.substring(1, param.length());
					onPart(param, prefix);
					command = "";
					param = "";
					prefix = "";
				} else if (command.equalsIgnoreCase("NICK")) {
					param = param.substring(2, param.length());
					onNick(param, prefix);
				} else if (command.equalsIgnoreCase("TOPIC")) {
					param.trim();
					String chan =
						param.substring(
							param.indexOf('#'),
							param.indexOf(':') - 1);
					if (param.indexOf(':') > -1) {
						int temp = param.indexOf(':');
						param = param.substring(temp + 1, param.length());
					}

					onTopicSet(prefix, chan, param);
					command = "";
					param = "";
					prefix = "";
				} else if (isNumeric && numCommand == NAM_REPLY) {

					String chan = "";
					StringTokenizer st = new StringTokenizer(param);
					st.nextToken();
					st.nextToken();
					chan = st.nextToken();
					NickNameList nicks = new NickNameList();
					String currNick;
					while (true) {
						try {
							currNick = st.nextToken();
						} catch (NoSuchElementException e) {
							break;
						}
						if (currNick.indexOf(':') > -1) {
							currNick =
								currNick.substring(
									currNick.indexOf(':') + 1,
									currNick.length());
						}
						if (currNick != null) {
							nicks.addNickName(currNick);
						} else {
							break;
						}
					}

					onNamReply(nicks, chan);
					command = "";
					param = "";
					prefix = "";

				} else if (isNumeric && numCommand == TOPIC) {
					StringTokenizer st = new StringTokenizer(param);
					st.nextToken();
					String chan = st.nextToken();
					if (param.indexOf(':') > -1) {
						int temp = param.indexOf(':');
						param = param.substring(temp + 1, param.length());
					}

					onTopic(chan, param);
					command = "";
					param = "";
					prefix = "";
				}

				command = "";
				param = "";
				prefix = "";

				try {
					this.sleep(5);
				} catch (InterruptedException e) {

				}
			}

		} catch (IRCSocketException e) {
			e.printStackTrace();
		}
	}

	private IRCSocket ircsocket;

	/**
	 * Returns the ircsocket.
	 * @@return IRCSocket
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
	 * @@param ircsocket The ircsocket to set
	 */
	public void setIrcsocket(IRCSocket ircsocket) {
		this.ircsocket = ircsocket;
	}

	public void onUnknown(String unknownString) {
		pircMain.getMainWindow().append("-\r\n" + unknownString);
	}

	public void onWelcome(String welcomeStr) {
		pircMain.getMainWindow().append("-\r\n" + welcomeStr);
	}

	public void onYourHost(String params) {
		pircMain.getMainWindow().append("-\r\n" + params);
	}

	public void onCreated(String params) {
		pircMain.getMainWindow().append("-\r\n" + params);
	}

	public void onBounce(String serverName) {
	}

	public void onPing() {
		pircMain.getMainWindow().append("-\r\n" + "Ping");
	}

	public void onSendPong() {
		pircMain.getMainWindow().append("-\r\n" + "Pong");
	}

	public void onMyInfo(String params) {
		pircMain.getMainWindow().append("-\r\n" + params);
	}

	public void onJoin(String channel, String prefix) {
		if (!pircMain.getChannels().getChannelExists(channel)) {
			Channel c = new Channel();
			c.setName(channel);
			pircMain.getChannelsIn().add(channel);
			pircMain.getChannels().putChannel(c);
			pircMain.createChannelWindow(c);
		} else {
			String host =
				prefix.substring(prefix.indexOf('!') + 1, prefix.length());
			prefix = prefix.substring(0, prefix.indexOf('!'));
			pircMain
				.getChannels()
				.getChannel(channel)
				.getNickList()
				.addNickName(
				new String(prefix));
			pircMain
				.getChannels()
				.getChannel(channel)
				.getNickList()
				.organizeByStatus();
			pircMain.getChannelWindow(new Channel(channel)).updateNicks();
			pircMain.getChannelWindow(new Channel(channel)).append(
				prefix + " (" + host + ") " + " Entrou no " + channel);
		}
	}

	public void onPart(String channel, String prefix) {
		String host =
			prefix.substring(prefix.indexOf('!') + 1, prefix.length());
		prefix = prefix.substring(0, prefix.indexOf('!'));
		prefix.trim();
		if (!prefix.equals(pircMain.getMyNickName())) {
			pircMain.getChannels().getChannel(channel).getNickList().remove(
				prefix);
			pircMain
				.getChannels()
				.getChannel(channel)
				.getNickList()
				.organizeByStatus();
			pircMain.getChannelWindow(new Channel(channel)).updateNicks();
			pircMain.getChannelWindow(new Channel(channel)).append(
				prefix + " (" + host + ") " + " saiu do " + channel);
		} else {
			pircMain.getChannelsIn().remove(channel);
		}
	}

	public void onNick(String newNick, String oldNick) {
		String host =
			prefix.substring(oldNick.indexOf('!') + 1, oldNick.length());
		oldNick = oldNick.substring(0, oldNick.indexOf('!'));
		String currChan = "";
		for (int i = 0; i < pircMain.getChannels().size(); i++) {
			try {
				currChan = (String) pircMain.getChannelsIn().get(i);
				if (oldNick.equals(pircMain.getMyNickName())) {
					pircMain.setMyNickName(newNick);
				}
				pircMain
					.getChannels()
					.getChannel(currChan)
					.getNickList()
					.updateNickName(
					oldNick,
					newNick);
				pircMain.getChannelWindow(new Channel(currChan)).append(
					oldNick
						+ " ("
						+ host
						+ ") "
						+ " mudou o nick para "
						+ newNick);
				pircMain.getChannelWindow(new Channel(currChan)).updateNicks();
			} catch (NullPointerException e) {
				break;
			}
		}
	}

	public void onNamReply(NickNameList nicks, String chan) {
		chan.trim();
		if (pircMain.getChannels().getChannelExists(chan)) {
			pircMain.getChannels().getChannel(chan).setNickList(nicks);
			pircMain
				.getChannels()
				.getChannel(chan)
				.getNickList()
				.organizeByStatus();
			pircMain.getChannelWindow(new Channel(chan)).updateNicks();
		}
	}

	public void onChanMsg(String to, String msg, String from) {
		to.trim();
		pircMain.getChannelWindow(new Channel(to)).append(
			"<" + from + "> " + msg);
	}

	public void onTopic(String channel, String param) {
		channel.trim();
		pircMain.getChannels().getChannel(channel).setTopic(param);
		pircMain.getChannelWindow(new Channel(channel)).updateTopic();
	}

	public void onTopicSet(String by, String chan, String newTopic) {
		pircMain.getChannels().getChannel(chan).setTopic(newTopic);
		pircMain.getChannelWindow(new Channel(chan)).append(
			by + " Modificou o tópico para: " + newTopic);
	}

	private String socketData;
	private String command, param, prefix;
	private int token;
	private int numCommand;
	private String nick;
	private PIRCConnectionWindow conWindow;
	private boolean isNumeric;
	private PIRCFrame pircMain;
	private StringTokenizer st;
}
@


1.1
log
@ouxi
@
text
@@

