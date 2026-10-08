/**
  *  pIRC - IRC Client written in Java.
  * 
  * ( Copyright (C) 2002 -  Ademir Constantino Filho )
  *
  * This program is free software; you can redistribute it and/or
  * modify it under the terms of the GNU General Public License
  * as published by the Free Software Foundation; either version 2
  * of the License, or (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with this program; if not, write to the Free Software
  * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
  *
  */

package org.aconstantino.pirc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * The main pIRC class
 * @author Ademir Constantino Filho.
 */

public class PIRCFrame extends JFrame {

	public PIRCFrame() {
		super("pIRC");
		init();
	}

	private void init() {
		setIconImage(
			Toolkit.getDefaultToolkit().createImage(
				PIRCFrame.class.getResource("images/icons/main.png")));
		connection = new PIRCConnectionWindow(this);
		connection.init();
		this.setContentPane(jdp);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				sendQuit(evt);
			}
		});
		jdp.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
		jdp.add(pircMainWindow);
		jdp.setBackground(bgColor);
		pircMenu = new PIRCMenu(connection, this);
		this.setJMenuBar(pircMenu);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		connection.setLocation((int) screen.width / 6, (int) screen.height / 6);
		this.setSize(600, 400);
		this.setLocation(screen.width / 6, screen.height / 6);
		pircMainWindow.setSize(this.getSize());
		pircMainWindow.setVisible(true);
		pircMainWindow.show();

		try {
			pircMainWindow.setSelected(true);
		} catch (PropertyVetoException e) {
		}
	}

	public void sendQuit(WindowEvent evt) {
		if (connected) {
			try {
				ircSocket.writeln("QUIT :" + QUIT_MESSAGE);
			} catch (IRCSocketException e) {
				System.exit(0);
			}
		}
	}

	/**
	 * Sends the quit message and exits pIRC
	 */
	public void exit() {
		sendQuit(null);
		System.exit(0);
	}

	private PIRCMenu pircMenu;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
				UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		PIRCFrame pircf = new PIRCFrame();
		pircf.setDefaultCloseOperation(EXIT_ON_CLOSE);
		pircf.show();
		pircf.toFront();
	}

	public void connect(PIRCConnectionWindow cw) {
		String[] server = cw.getServer();
		if (!connected && server != null) {
			try {
				pircMainWindow.append(
					"-\r\nConectando a " + server[2] + " (" + server[3] + ")");
				ident = new Identd("JAVA", cw.getNickname());
				ident.start();
				ircSocket = new IRCSocket();
				ircSocket.setPort(Integer.parseInt(server[3]));
				ircSocket.setServer(server[2]);
				ircSocket.setTimeOut(30000000);
				ircSocket.connect();
				connected = true;
				pircMenu.switchConn();
				startUp(cw);
			} catch (Exception e) {
				if (ident != null) {
					ident.close();
				}
				new PIRCExceptionWindow("Não foi possível conectar", e).show();
			}
		}
	}

	private void startUp(PIRCConnectionWindow cw) {
		proHandle = new ProtocolHandler();
		proHandle.setIrcsocket(ircSocket);
		String nick = connection.getNickname().trim();
		this.setMyNickName(nick);
		String name = connection.getName();
		String email = connection.getEmail();
		try {
			getIrcSocket().println("PASS pIRC");
			getIrcSocket().println("NICK " + nick);
			getIrcSocket().println("USER " + name + " 0 * :" + email);
		} catch (IRCSocketException e) {
			new PIRCExceptionWindow("Não foi possível conectar", e).show();
		}
		proHandle.setPIRCFrame(this);
		proHandle.setPIRCConnectionWindow(connection);
		new Thread(proHandle).start();
	}

	/**
	 * Sends the text typed by the user in a window.
	 * Text starting with "/" is sent as an IRC command (/join #pirc),
	 * other text is sent as a message to the target.
	 * @param target the channel or nickname, null for the main window
	 * @param text the typed text
	 * @return the text to show in the window or null
	 */
	public String sendInput(String target, String text) {
		if (text.trim().length() == 0) {
			return null;
		}
		if (!connected) {
			return "Não conectado";
		}
		try {
			if (text.startsWith("/")) {
				String cmd = text.substring(1);
				String rest = "";
				int space = cmd.indexOf(' ');
				if (space > -1) {
					rest = cmd.substring(space + 1);
					cmd = cmd.substring(0, space);
				}
				cmd = cmd.toUpperCase();
				if (cmd.equals("MSG") && rest.indexOf(' ') > -1) {
					String to = rest.substring(0, rest.indexOf(' '));
					String msg = rest.substring(rest.indexOf(' ') + 1);
					ircSocket.println("PRIVMSG " + to + " :" + msg);
					return "-> *" + to + "* " + msg;
				} else if (cmd.equals("ME") && target != null) {
					ircSocket.println(
						"PRIVMSG " + target + " :\u0001ACTION " + rest + "\u0001");
					return "* " + myNickName + " " + rest;
				} else {
					ircSocket.println(
						rest.length() > 0 ? cmd + " " + rest : cmd);
					return null;
				}
			} else if (target != null) {
				ircSocket.println("PRIVMSG " + target + " :" + text);
				return "> " + text;
			} else {
				ircSocket.println(text);
				return null;
			}
		} catch (IRCSocketException e) {
			return e.getMessage();
		}
	}

	public PIRCMainWindow getMainWindow() {
		return pircMainWindow;
	}

	public Identd getIdent() {
		return ident;
	}

	public IRCSocket getIrcSocket() {
		return ircSocket;
	}

	public ChannelsHashTable getChannels() {
		return channels;
	}

	public int getProcSize() {
		return process.size();
	}

	public void createChannelWindow(Channel channel) {
		process.put(
			channel.getName(),
			new PIRCChannelWindow(
				(Channel) channels.getChannel(channel.getName()),
				this));
		jdp.add((PIRCChannelWindow) process.get(channel.getName()));
		jdp.setSelectedFrame(
			(PIRCChannelWindow) process.get(channel.getName()));
		if (!channelsIn.contains(channel.getName())) {
			channelsIn.add(channel.getName());
		}
	}

	public PIRCChannelWindow getChannelWindow(Channel channel) {
		return (PIRCChannelWindow) process.get(channel.getName());
	}

	/**
	 * Removes the channel and its window from pIRC
	 * @param channelName the channel name
	 */
	public void removeChannel(String channelName) {
		PIRCChannelWindow window =
			(PIRCChannelWindow) process.remove(channelName);
		channels.removeChannel(channelName);
		channelsIn.remove(channelName);
		if (window != null && !window.isClosed()) {
			window.dispose();
		}
	}

	/**
	 * Returns the private message window of a nickname
	 * @param nickname the nickname
	 * @param create create the window if it does not exist
	 * @return the private message window or null
	 */
	public PrivateMessageSession getPrivateWindow(
		String nickname,
		boolean create) {
		String key = nickname.toLowerCase();
		PrivateMessageSession session =
			(PrivateMessageSession) privates.get(key);
		if (session == null && create) {
			session = new PrivateMessageSession(nickname, this);
			privates.put(key, session);
			jdp.add(session);
			jdp.setSelectedFrame(session);
		}
		return session;
	}

	/**
	 * Removes the private message window of a nickname
	 * @param nickname the nickname
	 */
	public void removePrivateWindow(String nickname) {
		privates.remove(nickname.toLowerCase());
	}

	/**
	 * Updates the private message window when the nickname changes
	 */
	public void renamePrivateWindow(String oldNick, String newNick) {
		PrivateMessageSession session =
			(PrivateMessageSession) privates.remove(oldNick.toLowerCase());
		if (session != null) {
			session.setNickname(newNick);
			session.setTitle(newNick);
			privates.put(newNick.toLowerCase(), session);
			session.append(oldNick + " mudou o nick para " + newNick);
		}
	}

	/**
	 * Returns all opened private message windows
	 */
	public Enumeration getPrivateWindows() {
		return privates.elements();
	}

	public boolean getProcess(Channel channel) {
		boolean exists = false;
		Channel ch = (Channel) process.get(channel);
		if (ch != null) {
			exists = true;
		}
		return exists;
	}

	public Vector getChannelsIn() {
		return channelsIn;
	}

	public String getMyNickName() {
		return myNickName;
	}

	public void setMyNickName(String myNickName) {
		this.myNickName = myNickName;
	}

	public void disconnect() {
		if (connected) {
			try {
				ircSocket.close(QUIT_MESSAGE);
			} catch (IRCSocketException e) {
			}
			onDisconnected();
		}
	}

	/**
	 * Called when the connection with the server is closed
	 */
	public void onDisconnected() {
		if (!connected) {
			return;
		}
		connected = false;
		if (ident != null) {
			ident.close();
		}
		Vector names = new Vector(process.keySet());
		for (int i = 0; i < names.size(); i++) {
			removeChannel((String) names.get(i));
		}
		Vector sessions = new Vector(privates.values());
		for (int i = 0; i < sessions.size(); i++) {
			((PrivateMessageSession) sessions.get(i)).dispose();
		}
		privates.clear();
		channels.clear();
		channelsIn.clear();
		pircMenu.switchConn();
		pircMainWindow.append("-\r\nDesconectado");
	}

	public boolean isConnected() {
		return connected;
	}

	private JDesktopPane jdp = new JDesktopPane();
	private PIRCMainWindow pircMainWindow = new PIRCMainWindow(this);
	private final Color bgColor = Color.GRAY;
	private PIRCConnectionWindow connection;
	private boolean connected;
	private Identd ident;
	public boolean identOk;
	private IRCSocket ircSocket;
	private Hashtable process = new Hashtable();
	private Hashtable privates = new Hashtable();
	public static final String QUIT_MESSAGE =
		"pIRC Version 0.7BETA Test By Ademir Constantino Filho";
	private ProtocolHandler proHandle;
	private Vector channelsIn = new Vector();
	private String myNickName;
	private ChannelsHashTable channels = new ChannelsHashTable(this);

}
