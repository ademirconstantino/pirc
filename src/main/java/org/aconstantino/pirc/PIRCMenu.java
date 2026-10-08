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

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * @author Ademir Constantino Filho <a href="mailto:ziegfried@techie.com">ziegfried@techie.com</a>
 * 17/09/2002 -  11:39:32 
 */
public final class PIRCMenu extends JMenuBar {

	public PIRCMenu(PIRCConnectionWindow conWindow, PIRCFrame pircFrame) {
		connect.addActionListener(e -> {
			if (pircFrame.isConnected()) {
				pircFrame.disconnect();
			} else {
				conWindow.setLocationRelativeTo(pircFrame);
				conWindow.setVisible(true);
			}
		});
		exit.addActionListener(e -> pircFrame.exit());
		/* not implemented yet */
		options.setEnabled(false);
		send.setEnabled(false);
		chat.setEnabled(false);
		dccOptions.setEnabled(false);

		JMenu menuFile = new JMenu("Arquivo");
		menuFile.add(connect);
		menuFile.add(options);
		menuFile.add(exit);
		JMenu menuDcc = new JMenu("DCC");
		menuDcc.add(send);
		menuDcc.add(chat);
		menuDcc.add(dccOptions);
		add(menuFile);
		add(menuDcc);
	}

	/**
	 * Updates the connect item after a connection or disconnection
	 * @param connected the connection state
	 */
	public void switchConn(boolean connected) {
		connect.setText(connected ? "Desconectar" : "Conectar");
	}

	private final JMenuItem connect = new JMenuItem("Conectar");
	private final JMenuItem options = new JMenuItem("Opções");
	private final JMenuItem exit = new JMenuItem("Sair");
	private final JMenuItem send = new JMenuItem("Enviar");
	private final JMenuItem chat = new JMenuItem("Chat");
	private final JMenuItem dccOptions = new JMenuItem("Opções de DCC");
}
