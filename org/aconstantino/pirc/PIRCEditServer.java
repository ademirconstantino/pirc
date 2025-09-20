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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * @@author Ademir Constantino Filho <a href="mailto:ziegfried@@techie.com">ziegfried@@techie.com</a>
 * 01/10/2002 -  08:44:35 
 */
public class PIRCEditServer extends JDialog {

	public PIRCEditServer(PIRCServersReader reader) {
		this.reader = reader;
	}

	private void init() {

		java.awt.GridBagConstraints gridBagConstraints;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Edit Server");
		setModal(true);
		setResizable(false);
		jp1.setLayout(new GridBagLayout());
		jlb.setText("Name");
		jlb.setToolTipText("name");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		jp1.add(jlb, gridBagConstraints);
		jld.setText("URL");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		jp1.add(jld, gridBagConstraints);
		jlc.setText("Port");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		jp1.add(jlc, gridBagConstraints);
		jla.setText("Group");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(0, 9, 0, 9);
		jp1.add(jla, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 5;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(2, 0, 2, 0);
		jp1.add(tField, gridBagConstraints);
		tFieldA.setMinimumSize(new Dimension(40, 20));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridwidth = 5;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(2, 0, 2, 0);
		jp1.add(tFieldA, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 5;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(2, 0, 2, 0);
		jp1.add(tFieldC, gridBagConstraints);
		jb1.setText("Ok");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		jp1.add(jb1, gridBagConstraints);
		jcA.setEditable(true);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 5;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		jp1.add(jcA, gridBagConstraints);
		getContentPane().add(jp1, BorderLayout.CENTER);
		pack();
	}
	
	public void show() {
		pack();
	}

	public String getGroupName() {
		return groupName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
		this.setTitle("Edit "+serverName);
	}

	private final JLabel jld = new JLabel();
	private final JButton jb1 = new JButton();
	private final JPanel jp1 = new JPanel();
	private final JComboBox jcA = new JComboBox();
	private final JLabel jla = new JLabel();
	private final JLabel jlb = new JLabel();
	private final JLabel jlc = new JLabel();
	private final JTextField tFieldC = new JTextField();
	private final JTextField tField = new JTextField();
	private final JTextField tFieldA = new JTextField();
	private final PIRCServersReader reader;
	private String serverName, groupName;

}
@


1.1
log
@ouxi
@
text
@@

