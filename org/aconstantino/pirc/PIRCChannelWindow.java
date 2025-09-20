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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @@author Ademir Constantino Filho <a href="mailto:ziegfried@@techie.com">ziegfried@@techie.com</a>
 * 20/09/2002 -  18:37:37 
 */
public class PIRCChannelWindow
	extends JInternalFrame
	implements ListSelectionListener {

	public PIRCChannelWindow(Channel channel, PIRCFrame pircframe) {
		super(channel.getName());
		this.pircFrame = pircframe;
		this.channel = channel;
		init();
	}

	public void updateNicks() {
		nicksModel.removeAllElements();
		for (int i = 0; i < channel.getNickList().size(); i++) {
			nicksModel.addElement(channel.getNickList().get(i));
		}
	}

	public void updateTopic() {
		append(
			"-- Talking in "
				+ channel.getName()
				+ " - "
				+ (new String(new Date().toString())));
		append(channel.getName() + " Topic is \"" + channel.getTopic() + "\"");
	}

	public void init() {
		nicks.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		nickPanel.setLayout(new BorderLayout());
		nickPanel.add(nickScroll);
		for (int i = 0; i < channel.getNickList().size(); i++) {
			nicksModel.addElement(channel.getNickList().get(i));
		}
		tScroll.setAutoscrolls(true);
		tPanel.setLayout(new BorderLayout());
		tPanel.add(tScroll, BorderLayout.CENTER);
		tPanel.add(tField, BorderLayout.SOUTH);
		tPanel.add(nickPanel, BorderLayout.EAST);
		this.getContentPane().add(tPanel);
		tMain.setEditable(false);
		tMain.setForeground(Color.GRAY);
		tMain.setBackground(Color.BLACK);
		this.addInternalFrameListener(new InternalFrameListener() {
			public void internalFrameOpened(InternalFrameEvent evt) {
			}
			public void internalFrameClosing(InternalFrameEvent evt) {
			}
			public void internalFrameClosed(InternalFrameEvent evt) {
				sair(evt);
			}
			public void internalFrameIconified(InternalFrameEvent evt) {
			}
			public void internalFrameDeiconified(InternalFrameEvent evt) {
			}
			public void internalFrameActivated(InternalFrameEvent evt) {
			}
			public void internalFrameDeactivated(InternalFrameEvent evt) {
			}
		});
		nicks.setFont(new Font("Verdana", 1, 12));
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
								+ channel.getName()
								+ " :"
								+ tField.getText()
								+ "\r\n");
					append("> "+tField.getText());
					} catch (IRCSocketException e) {
					}
					tField.setText("");
				}
			}
		});

		setResizable(true);
		setVisible(true);
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setSize(700, 400);
		show();
	}

	public void sair(InternalFrameEvent evt) {
		try {
			pircFrame.getIrcSocket().println("PART " + channel.getName());
			pircFrame.getChannels().removeChannel(channel.getName());
		} catch (IRCSocketException e) {
		}
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

	public void valueChanged(ListSelectionEvent e) {
	}

	private final JTextArea tMain = new JTextArea();
	private final JScrollPane tScroll = new JScrollPane(tMain);
	private final JTextField tField = new JTextField();
	private final JPanel tPanel = new JPanel();
	private final JPanel nickPanel = new JPanel();
	private final DefaultListModel nicksModel = new DefaultListModel();
	private final JList nicks = new JList(nicksModel);
	private JScrollPane nickScroll = new JScrollPane(nicks);
	private PIRCFrame pircFrame;
	private Channel channel;

}
@


1.1
log
@ouxi
@
text
@@

