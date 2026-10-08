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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Dialog to add or edit a server of the servers list
 * @author Ademir Constantino Filho <a href="mailto:ziegfried@techie.com">ziegfried@techie.com</a>
 * 01/10/2002 -  08:44:35 
 */
public final class PIRCEditServer extends JDialog {

	/**
	 * @param owner the connection window
	 * @param reader the servers list, saved when the user clicks Ok
	 * @param groups the groups shown in the group combo
	 * @param server the server to edit or null to add a new server
	 */
	public PIRCEditServer(
		Window owner,
		PIRCServersReader reader,
		List<String> groups,
		Server server) {
		super(owner, ModalityType.APPLICATION_MODAL);
		this.reader = reader;
		this.server = server;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);

		groups.forEach(group::addItem);
		group.setEditable(true);
		name.setColumns(20);
		if (server == null) {
			setTitle("Adicionar Servidor");
			port.setText("6667");
		} else {
			setTitle("Editar " + server.name());
			name.setText(server.name());
			url.setText(server.host());
			port.setText(String.valueOf(server.port()));
			group.setSelectedItem(server.group());
		}

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		addRow(panel, 0, "Nome", name);
		addRow(panel, 1, "URL", url);
		addRow(panel, 2, "Porta", port);
		addRow(panel, 3, "Grupo", group);

		JButton ok = new JButton("Ok");
		ok.addActionListener(e -> okEvent());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 4;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 0, 0, 0);
		panel.add(ok, c);
		getContentPane().add(panel, BorderLayout.CENTER);
		getRootPane().setDefaultButton(ok);
		pack();
		setLocationRelativeTo(owner);
	}

	private static void addRow(JPanel panel, int row, String label, JComponent field) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = row;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 9);
		panel.add(new JLabel(label), c);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = row;
		c.gridwidth = 5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(2, 0, 2, 0);
		panel.add(field, c);
	}

	private void okEvent() {
		Object selectedGroup = group.getEditor().getItem();
		String groupValue = selectedGroup == null ? "" : selectedGroup.toString().trim();
		String nameValue = name.getText().trim();
		String urlValue = url.getText().trim();
		if (nameValue.isEmpty() || urlValue.isEmpty() || groupValue.isEmpty()) {
			warn("Preencha todos os campos.");
			return;
		}
		int portValue;
		try {
			portValue = Integer.parseInt(port.getText().trim());
		} catch (NumberFormatException e) {
			portValue = -1;
		}
		if (portValue <= 0 || portValue > 65535) {
			warn("Porta inválida.");
			return;
		}
		Server saved = new Server(groupValue, nameValue, urlValue, portValue);
		if (server == null) {
			reader.add(saved);
		} else {
			reader.replace(server, saved);
		}
		try {
			reader.save();
		} catch (IOException e) {
			PIRCExceptionWindow.showError(this, "Não foi possível salvar", e);
		}
		savedServer = saved;
		dispose();
	}

	private void warn(String message) {
		JOptionPane.showMessageDialog(this, message, getTitle(), JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * @return the saved server or null if the user did not save it
	 */
	public Server getSavedServer() {
		return savedServer;
	}

	private final JTextField name = new JTextField();
	private final JTextField url = new JTextField();
	private final JTextField port = new JTextField();
	private final JComboBox<String> group = new JComboBox<>();
	private final PIRCServersReader reader;
	private final Server server;
	private Server savedServer;

}
