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
  * Este programa � software livre; voc� pode redistribu�-lo e/ou
  * modific�-lo sob os termos da Licen�a P�blica Geral GNU, conforme
  * publicada pela Free Software Foundation; tanto a vers�o 2 da
  * Licen�a como (a seu crit�rio) qualquer vers�o mais nova.
  *
  * Este programa � distribu�do na expectativa de ser �til, mas SEM
  * QUALQUER GARANTIA; sem mesmo a garantia impl�cita de
  * COMERCIALIZA��O ou de ADEQUA��O A QUALQUER PROP�SITO EM
  * PARTICULAR. Consulte a Licen�a P�blica Geral GNU para obter mais
  * detalhes.
  *
  * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral GNU
  * junto com este programa; se n�o, escreva para a Free Software
  * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
  * 02111-1307, USA.
  *
  * Voc� pode entrar em contato pelo endere�o de email:
  * ziegfried@@onda.com.br
  *
  */

package org.aconstantino.pirc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @@author Ademir Constantino Filho <a href="mailto:ziegfried@@techie.com">ziegfried@@techie.com</a>
 * 28/09/2002 -  12:21:09 
 */
public class PrivateMessageSession extends JInternalFrame {

	public PrivateMessageSession(String nickname, PIRCFrame pircframe) {
		super(nickname);
		this.pircFrame = pircframe;
		this.nickname = nickname;
		init();
	}

	private void init() {

		tPanel.setLayout(new BorderLayout());
		tPanel.add(tScroll, BorderLayout.CENTER);
		tPanel.add(tField, BorderLayout.SOUTH);
		this.getContentPane().add(tPanel);
		tScroll.setAutoscrolls(true);
		setVisible(true);
		tMain.setEditable(false);
		tMain.setForeground(Color.GRAY);
		tMain.setBackground(Color.BLACK);
		tMain.setFont(new Font("Verdana", 1, 12));
		tField.setBackground(Color.GRAY);
		tMain.setBackground(Color.BLACK);
		tField.setFont(new Font("Verdana", 1, 12));
		tField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent evt) {
				if (evt.getKeyChar() == evt.VK_ENTER) {
					try {
						pircFrame.getIrcSocket().write(
							"PRIVMSG "
								+ nickname
								+ " :"
								+ tField.getText()
								+ "\r\n");
						append("> " + tField.getText());
					} catch (IRCSocketException e) {
					}
					tField.setText("");
				}
			}
		});

		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
		setSize(400, 400);
		show();
	}

	public void append(String str) {
		tMain.append(getTimeStamp() + "  " + str + "\r\n");
	}

	public String getTimeStamp() {
		String ho = "[h:mm:ss]";
		Date d = new Date();
		String h = "";
		SimpleDateFormat formatter = new SimpleDateFormat(ho);
		h = formatter.format(d);

		return h;
	}

	/**
	 * Returns the nickname.
	 * @@return String
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Sets the nickname.
	 * @@param nickname The nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	private String nickname;
	private final JTextArea tMain = new JTextArea();
	private final JScrollPane tScroll = new JScrollPane(tMain);
	private final JTextField tField = new JTextField();
	private final JPanel tPanel = new JPanel();
	private PIRCFrame pircFrame;

}
@


1.1
log
@ouxi
@
text
@@

