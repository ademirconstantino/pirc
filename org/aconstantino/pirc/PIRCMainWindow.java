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
date	2002.12.11.05.49.15;	author ziegfried;	state Exp;
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
  * ziegfried@@techie.com
  *
  */

package org.aconstantino.pirc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @@author Ademir Constantino Filho <a href="mailto:ziegfried@@techie.com">ziegfried@@techie.com</a>
 * 17/09/2002 -  12:03:22 
 */
public class PIRCMainWindow extends JInternalFrame {

	public PIRCMainWindow(PIRCFrame pircFrame) {
		super("Main Window");
		this.pircFrame = pircFrame;
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
						pircFrame.getIrcSocket().println(tField.getText());
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

	public void append(String s) {
		tMain.append(s + "\r\n");
	}

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

