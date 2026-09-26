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
  * ziegfried@onda.com.br
  *
  */

package org.aconstantino.pirc;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * @author Ademir Constantino Filho <a href="mailto:ziegfried@techie.com">ziegfried@techie.com</a>
 * 01/10/2002 -  08:44:35 
 */
public class PIRCEditServer extends JDialog {

	public PIRCEditServer(Dialog owner, PIRCServersReader reader) {
		super(owner);
		this.reader = reader;
		init();
	}

	private void init() {

		java.awt.GridBagConstraints gridBagConstraints;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Adicionar Servidor");
		setModal(true);
		setResizable(false);
		jp1.setLayout(new GridBagLayout());
		jp1.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		jlb.setText("Nome");
		jlb.setToolTipText("name");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		jp1.add(jlb, gridBagConstraints);
		jld.setText("URL");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		jp1.add(jld, gridBagConstraints);
		jlc.setText("Porta");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		jp1.add(jlc, gridBagConstraints);
		jla.setText("Grupo");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(0, 0, 0, 9);
		jp1.add(jla, gridBagConstraints);
		tFieldA.setColumns(20);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 5;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(2, 0, 2, 0);
		jp1.add(tFieldA, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 5;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(2, 0, 2, 0);
		jp1.add(tField, gridBagConstraints);
		tFieldC.setText("6667");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 5;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(2, 0, 2, 0);
		jp1.add(tFieldC, gridBagConstraints);
		jcA.setEditable(true);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 5;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		jp1.add(jcA, gridBagConstraints);
		jb1.setText("Ok");
		jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okEvent();
			}
		});
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(10, 0, 0, 0);
		jp1.add(jb1, gridBagConstraints);
		getContentPane().add(jp1, BorderLayout.CENTER);
		getRootPane().setDefaultButton(jb1);
		pack();
	}

	/**
	 * Sets the server to be edited
	 * @param server { group, name, url, port } or null to add a new server
	 */
	public void setServer(String[] server) {
		this.server = server;
		if (server != null) {
			setServerName(server[1]);
			setGroupName(server[0]);
			tFieldA.setText(server[1]);
			tField.setText(server[2]);
			tFieldC.setText(server[3]);
		}
	}

	/**
	 * Sets the groups shown in the group combo
	 */
	public void setGroups(Vector groups) {
		jcA.removeAllItems();
		for (int i = 0; i < groups.size(); i++) {
			jcA.addItem(groups.get(i));
		}
		if (groupName != null) {
			jcA.setSelectedItem(groupName);
		}
	}

	private void okEvent() {
		String name = tFieldA.getText().trim();
		String url = tField.getText().trim();
		String port = tFieldC.getText().trim();
		Object group = jcA.getSelectedItem();
		String groupValue = group == null ? "" : group.toString().trim();
		if (name.length() == 0
			|| url.length() == 0
			|| groupValue.length() == 0) {
			JOptionPane.showMessageDialog(
				this,
				"Preencha todos os campos.",
				getTitle(),
				JOptionPane.WARNING_MESSAGE);
			return;
		}
		try {
			if (Integer.parseInt(port) <= 0) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(
				this,
				"Porta inválida.",
				getTitle(),
				JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (server == null) {
			server = new String[] { groupValue, name, url, port, "server" };
			reader.getH().add(server);
		} else {
			server[0] = groupValue;
			server[1] = name;
			server[2] = url;
			server[3] = port;
		}
		try {
			reader.save();
		} catch (IOException e) {
			new PIRCExceptionWindow("Não foi possível salvar", e).show();
		}
		saved = true;
		dispose();
	}

	/**
	 * @return <code> true </code> if the server was saved
	 */
	public boolean isSaved() {
		return saved;
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
		this.setTitle("Editar " + serverName);
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
	private String[] server;
	private boolean saved;

}
