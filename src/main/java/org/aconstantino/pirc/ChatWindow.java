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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * A window with the messages and a field to type text.
 * The typed text is sent to the target of the window.
 */
abstract class ChatWindow extends JInternalFrame {

	static final Font FONT = new Font("Verdana", Font.BOLD, 12);

	private static final DateTimeFormatter TIME_STAMP =
		DateTimeFormatter.ofPattern("'['HH:mm:ss']'");

	/**
	 * @param title the window title
	 * @param pircFrame the pIRC main frame
	 * @param timeStamped <code> true </code> to show the time of each line
	 */
	@SuppressWarnings("this-escape")
	ChatWindow(String title, PIRCFrame pircFrame, boolean timeStamped) {
		super(title);
		this.pircFrame = pircFrame;
		this.timeStamped = timeStamped;
		tMain.setEditable(false);
		tMain.setForeground(Color.GRAY);
		tMain.setBackground(Color.BLACK);
		tMain.setFont(FONT);
		tField.setBackground(Color.GRAY);
		tField.setFont(FONT);
		tField.addActionListener(e -> {
			String echo = pircFrame.sendInput(getTarget(), tField.getText());
			if (echo != null) {
				append(echo);
			}
			tField.setText("");
		});
		tScroll.setAutoscrolls(true);
		tPanel.add(tScroll, BorderLayout.CENTER);
		tPanel.add(tField, BorderLayout.SOUTH);
		getContentPane().add(tPanel);
		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
	}

	/**
	 * @return the channel or nickname the typed text is sent to,
	 * null to send the text to the server
	 */
	abstract String getTarget();

	public void append(String str) {
		String line = timeStamped ? getTimeStamp() + "  " + str : str;
		tMain.append(line + "\r\n");
		tMain.setCaretPosition(tMain.getDocument().getLength());
	}

	public static String getTimeStamp() {
		return LocalTime.now().format(TIME_STAMP);
	}

	/**
	 * @return the panel with the messages and the text field
	 */
	JPanel getChatPanel() {
		return tPanel;
	}

	final PIRCFrame pircFrame;
	private final boolean timeStamped;
	private final JTextArea tMain = new JTextArea();
	private final JScrollPane tScroll = new JScrollPane(tMain);
	private final JTextField tField = new JTextField();
	private final JPanel tPanel = new JPanel(new BorderLayout());

}
