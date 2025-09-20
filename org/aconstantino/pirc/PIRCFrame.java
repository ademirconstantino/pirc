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
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * The main pIRC class
 * @@author Ademir Constantino Filho.
 */

public class PIRCFrame extends JFrame {

	public PIRCFrame() {
		super("pIRC");
		init();
	}

	private void init() {
		setIconImage(
			Toolkit.getDefaultToolkit().createImage(
				"org/aconstantino/pirc/images/icons/main.png"));
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
				ircSocket.writeln(
					"QUIT :pIRC Version 0.7BETA Test By Ademir Constantino Filho");
			} catch (IRCSocketException e) {
				System.exit(0);
			}
		}
	}

	private PIRCMenu pircMenu;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
				"com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		PIRCFrame pircf = new PIRCFrame();
		pircf.setDefaultCloseOperation(EXIT_ON_CLOSE);
		pircf.show();
		pircf.toFront();
	}

	public void connect(PIRCConnectionWindow cw) {
	//	String server = cw.getServer();
		String server = "localhost";
		if (!connected) {
			try {
				ircSocket = new IRCSocket();
				ircSocket.setPort(6667);
				ircSocket.setServer(server);
				ircSocket.setTimeOut(30000000);
				ircSocket.connect();
				startUp(cw);
				ident = new Identd("ziegfried", "java");
				connected = true;
				pircMenu.switchConn();
				ident.start();
			} catch (Exception e) {
				new PIRCExceptionWindow("Não foi possível conectar", e).show();
			}
		}
	}

	private void startUp(PIRCConnectionWindow cw) {
		proHandle = new ProtocolHandler();
		proHandle.setIrcsocket(ircSocket);
		String nick = connection.getNickname();
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
		channelsIn.add(channel.getName());
	}

	public PIRCChannelWindow getChannelWindow(Channel channel) {
		return (PIRCChannelWindow) process.get(channel.getName());
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
	private ProtocolHandler proHandle;
	private Vector channelsIn = new Vector();
	private String myNickName;
	private ChannelsHashTable channels = new ChannelsHashTable(this);

}
@


1.1
log
@ouxi
@
text
@@

