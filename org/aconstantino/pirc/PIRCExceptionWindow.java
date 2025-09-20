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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
 * @@author Ademir Constantino Filho <a href="mailto:ziegfried@@techie.com">ziegfried@@techie.com</a>
 * 19/09/2002 -  14:10:30 
 */
public class PIRCExceptionWindow extends JDialog {

	public PIRCExceptionWindow(String title, Exception e) {
		this.title = title;
		this.e = e;
		init();
	}

	private void init() {
		setTitle(title);
		setModal(true);
		setResizable(false);
		setTitle(title);
		setSize(300, 200);
		topPanel.setLayout(new BorderLayout());
		topPanel.add(new JSeparator());
		topPanel.add(new JLabel(topImage), BorderLayout.NORTH);
		getContentPane().add(topPanel, BorderLayout.NORTH);
		detailsPanel.setLayout(new BorderLayout());
		eDetails.setText(e.getMessage());
		detailsPanel.add(eDetails, BorderLayout.CENTER);
		getContentPane().add(detailsPanel, BorderLayout.CENTER);
		closePanel.setLayout(new BorderLayout());
		closePanel.add(new JSeparator(), BorderLayout.NORTH);
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		closePanel.add(close, BorderLayout.SOUTH);
		getContentPane().add(closePanel, BorderLayout.SOUTH);
	}

	private void close() {
		this.removeAll();
		dispose();
	}

	private final String title;
	private final Exception e;
	private final JTextArea eDetails = new JTextArea();
	private final JPanel detailsPanel = new JPanel(),
		topPanel = new JPanel(),
		closePanel = new JPanel();
	private final JButton close = new JButton("Close");
	private final ImageIcon topImage =
		new ImageIcon("org/aconstantino/pirc/images/exception_top.gif");

}
@


1.1
log
@ouxi
@
text
@@

