/**
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
