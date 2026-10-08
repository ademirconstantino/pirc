/*
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * The main pIRC class
 * @author Ademir Constantino Filho.
 */

public final class PIRCFrame extends JFrame {

	public static final String VERSION = "0.7BETA";
	public static final String QUIT_MESSAGE =
		"pIRC Version " + VERSION + " Test By Ademir Constantino Filho";

	public PIRCFrame() {
		super("pIRC");
		setIconImage(
			new ImageIcon(PIRCFrame.class.getResource("images/icons/main.png"))
				.getImage());
		connection = new PIRCConnectionWindow(this);
		pircMenu = new PIRCMenu(connection, this);
		setJMenuBar(pircMenu);
		setContentPane(jdp);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				sendQuit();
			}
		});
		jdp.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
		jdp.setBackground(Color.GRAY);
		jdp.add(pircMainWindow);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(600, 400);
		setLocation(screen.width / 6, screen.height / 6);
		pircMainWindow.setSize(getSize());
		try {
			pircMainWindow.setSelected(true);
		} catch (PropertyVetoException e) {
			// the main window can not be selected, nothing to do
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException
				| InstantiationException
				| IllegalAccessException
				| UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
			PIRCFrame pircf = new PIRCFrame();
			pircf.setVisible(true);
			pircf.toFront();
		});
	}

	private void sendQuit() {
		if (connected) {
			try {
				ircSocket.println("QUIT :" + QUIT_MESSAGE);
			} catch (IRCSocketException e) {
				// exiting anyway
			}
		}
	}

	/**
	 * Sends the quit message and exits pIRC
	 */
	public void exit() {
		sendQuit();
		System.exit(0);
	}

	/**
	 * Connects to the server selected in the connection window.
	 * The connection is opened in background, the windows are updated
	 * when it is done.
	 */
	public void connect(PIRCConnectionWindow cw) {
		Server server = cw.getServer();
		if (connected || connecting || server == null) {
			return;
		}
		connecting = true;
		pircMainWindow.append(
			"-\r\nConectando a " + server.host() + " (" + server.port() + ")");
		Identd identd = new Identd("JAVA", cw.getNickname().trim());
		identd.start();
		IRCSocket socket = new IRCSocket(server.host(), server.port());
		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws IRCSocketException {
				socket.connect();
				return null;
			}

			@Override
			protected void done() {
				connecting = false;
				try {
					get();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return;
				} catch (ExecutionException e) {
					identd.close();
					pircMainWindow.append("-\r\nNão foi possível conectar");
					PIRCExceptionWindow.showError(
						PIRCFrame.this,
						"Não foi possível conectar",
						e.getCause() instanceof Exception cause ? cause : e);
					return;
				}
				ident = identd;
				ircSocket = socket;
				connected = true;
				pircMenu.switchConn(true);
				startUp(cw);
			}
		}.execute();
	}

	private void startUp(PIRCConnectionWindow cw) {
		String nick = cw.getNickname().trim();
		setMyNickName(nick);
		try {
			ircSocket.println("PASS pIRC");
			ircSocket.println("NICK " + nick);
			ircSocket.println("USER " + cw.getRealName() + " 0 * :" + cw.getEmail());
		} catch (IRCSocketException e) {
			PIRCExceptionWindow.showError(this, "Não foi possível conectar", e);
		}
		Thread reader =
			new Thread(new ProtocolHandler(this, ircSocket, cw), "pIRC-reader");
		reader.setDaemon(true);
		reader.start();
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
		if (text.isBlank()) {
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
				cmd = cmd.toUpperCase(Locale.ROOT);
				int restSpace = rest.indexOf(' ');
				if (cmd.equals("MSG") && restSpace > -1) {
					String to = rest.substring(0, restSpace);
					String msg = rest.substring(restSpace + 1);
					ircSocket.println("PRIVMSG " + to + " :" + msg);
					return "-> *" + to + "* " + msg;
				} else if (cmd.equals("ME") && target != null) {
					ircSocket.println(
						"PRIVMSG " + target + " :\u0001ACTION " + rest + "\u0001");
					return "* " + myNickName + " " + rest;
				} else {
					ircSocket.println(rest.isEmpty() ? cmd : cmd + " " + rest);
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

	public IRCSocket getIrcSocket() {
		return ircSocket;
	}

	public Channels getChannels() {
		return channels;
	}

	public void createChannelWindow(Channel channel) {
		PIRCChannelWindow window = new PIRCChannelWindow(channel, this);
		channelWindows.put(channel.getName(), window);
		showInternalFrame(window);
	}

	/**
	 * Adds a window to the desktop, in front of the other windows
	 */
	private void showInternalFrame(JInternalFrame frame) {
		jdp.add(frame);
		frame.moveToFront();
		try {
			frame.setSelected(true);
		} catch (PropertyVetoException e) {
			// the window can not be selected, it is shown anyway
		}
	}

	/**
	 * @return the window of the channel or null
	 */
	public PIRCChannelWindow getChannelWindow(String channelName) {
		return channelWindows.get(channelName);
	}

	/**
	 * Removes the channel and its window from pIRC
	 * @param channelName the channel name
	 */
	public void removeChannel(String channelName) {
		PIRCChannelWindow window = channelWindows.remove(channelName);
		channels.remove(channelName);
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
	public PrivateMessageSession getPrivateWindow(String nickname, boolean create) {
		String key = nickname.toLowerCase(Locale.ROOT);
		PrivateMessageSession session = privates.get(key);
		if (session == null && create) {
			session = new PrivateMessageSession(nickname, this);
			privates.put(key, session);
			showInternalFrame(session);
		}
		return session;
	}

	/**
	 * Removes the private message window of a nickname
	 * @param nickname the nickname
	 */
	public void removePrivateWindow(String nickname) {
		privates.remove(nickname.toLowerCase(Locale.ROOT));
	}

	/**
	 * Updates the private message window when the nickname changes
	 */
	public void renamePrivateWindow(String oldNick, String newNick) {
		PrivateMessageSession session =
			privates.remove(oldNick.toLowerCase(Locale.ROOT));
		if (session != null) {
			session.setNickname(newNick);
			privates.put(newNick.toLowerCase(Locale.ROOT), session);
			session.append(oldNick + " mudou o nick para " + newNick);
		}
	}

	public String getMyNickName() {
		return myNickName;
	}

	public void setMyNickName(String myNickName) {
		this.myNickName = myNickName;
	}

	public void disconnect() {
		if (connected) {
			IRCSocket socket = ircSocket;
			socket.close(QUIT_MESSAGE);
			onDisconnected(socket);
		}
	}

	/**
	 * Called when the connection with the server is closed
	 * @param socket the closed connection, ignored if it is not the
	 * current one (a reader thread of an old connection finishing)
	 */
	public void onDisconnected(IRCSocket socket) {
		if (!connected || socket != ircSocket) {
			return;
		}
		connected = false;
		socket.close();
		if (ident != null) {
			ident.close();
			ident = null;
		}
		List.copyOf(channelWindows.keySet()).forEach(this::removeChannel);
		List.copyOf(privates.values()).forEach(PrivateMessageSession::dispose);
		privates.clear();
		channels.clear();
		pircMenu.switchConn(false);
		pircMainWindow.append("-\r\nDesconectado");
	}

	public boolean isConnected() {
		return connected;
	}

	private final JDesktopPane jdp = new JDesktopPane();
	private final PIRCMainWindow pircMainWindow = new PIRCMainWindow(this);
	private final PIRCConnectionWindow connection;
	private final PIRCMenu pircMenu;
	private final Map<String, PIRCChannelWindow> channelWindows = new HashMap<>();
	private final Map<String, PrivateMessageSession> privates = new HashMap<>();
	private final Channels channels = new Channels();
	private boolean connected;
	private boolean connecting;
	private Identd ident;
	private IRCSocket ircSocket;
	private String myNickName;

}
