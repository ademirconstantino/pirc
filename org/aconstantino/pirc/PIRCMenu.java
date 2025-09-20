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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * @@author Ademir Constantino Filho <a href="mailto:ziegfried@@techie.com">ziegfried@@techie.com</a>
 * 17/09/2002 -  11:39:32 
 */
public class PIRCMenu extends JMenuBar {

	public PIRCMenu(PIRCConnectionWindow conWindow, PIRCFrame pircFrame) {
		this.conWindow = conWindow;
		this.pircFrame = pircFrame;
		init();
	}

	public void switchConn() {
		if (pircFrame.isConnected()) {
			connect.setText("Disconnect");
			connect.removeActionListener(connection);
			connect.addActionListener(disconnection);
		} else {
			connect.setText("Connect");
			connect.removeActionListener(disconnection);
			connect.addActionListener(connection);
		}
	}

	private void init() {

		connect = new JMenuItem("Conectar");

		connection = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conWindow.show();
			}
		};

		disconnection = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pircFrame.disconnect();

			}
		};

		connect.addActionListener(connection);
		options = new JMenuItem("Opções");
		exit = new JMenuItem("Sair");
		initMenuFile();
		send = new JMenuItem("Enviar");
		chat = new JMenuItem("Chat");
		dccOptions = new JMenuItem("Opções de DCC");
		initMenuDcc();
		addToMenu();
	}

	private void initMenuFile() {
		menuFile = new JMenu("Arquivo");
		menuFile.add(connect);
		menuFile.add(options);
		menuFile.add(exit);
	}

	private void initMenuDcc() {
		menuDcc = new JMenu("DCC");
		menuDcc.add(send);
		menuDcc.add(chat);
		menuDcc.add(dccOptions);
	}

	private void addToMenu() {
		this.add(menuFile);
		this.add(menuDcc);
	}

	private JMenu menuFile;
	private JMenuItem connect;
	private JMenuItem options;
	private JMenuItem exit;

	private JMenu menuDcc;
	private JMenuItem send;
	private JMenuItem chat;
	private JMenuItem dccOptions;

	private PIRCConnectionWindow conWindow;
	private PIRCFrame pircFrame;
	private ActionListener connection;
	private ActionListener disconnection;
}
