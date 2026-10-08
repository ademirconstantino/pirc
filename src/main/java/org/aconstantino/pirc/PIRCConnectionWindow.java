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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * @author Ademir Constantino Filho <a href="mailto:ziegfried@techie.com">ziegfried@techie.com</a>
 * 17/09/2002 -  14:33:27 
 */
public final class PIRCConnectionWindow extends JDialog {

	private static final String ALL_GROUPS = "all";

	public PIRCConnectionWindow(PIRCFrame pircFrame) {
		super(pircFrame, "Conexão");
		this.pircFrame = pircFrame;
		setSize(456, 335);

		JButton connect = new JButton("Conectar");
		JButton cancel = new JButton("Cancelar");
		connect.addActionListener(e -> connectionEvent());
		cancel.addActionListener(e -> dispose());
		getRootPane().setDefaultButton(connect);

		JPanel mainPanel = new JPanel(new GridBagLayout());
		addRow(mainPanel, 1, "Name: ", name);
		addRow(mainPanel, 2, "Email: ", email);
		addRow(mainPanel, 3, "NickName: ", nickname);
		addRow(mainPanel, 4, "Alternativo: ", alternative);
		mainPanel.add(
			createServerPanel(),
			new GridBagConstraints(
				0, 5, GridBagConstraints.REMAINDER, GridBagConstraints.REMAINDER,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		JPanel topImagePanel = new JPanel(new BorderLayout());
		topImagePanel.add(
			new JLabel(
				new ImageIcon(
					PIRCConnectionWindow.class.getResource("images/connection_top.gif"))),
			BorderLayout.NORTH);
		topImagePanel.add(new JSeparator());

		JPanel conPanel = new JPanel(new FlowLayout());
		conPanel.add(connect);
		conPanel.add(cancel);
		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.add(new JSeparator(), BorderLayout.NORTH);
		southPanel.add(conPanel, BorderLayout.CENTER);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(topImagePanel, BorderLayout.NORTH);
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		getContentPane().add(southPanel, BorderLayout.SOUTH);

		reloadServers(null);
	}

	private static void addRow(JPanel panel, int row, String label, JTextField field) {
		panel.add(
			new JLabel(label),
			new GridBagConstraints(
				0, row, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		panel.add(
			field,
			new GridBagConstraints(
				1, row, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 300, 0));
	}

	private JPanel createServerPanel() {
		JButton edit = new JButton("Editar");
		JButton add = new JButton("Adicionar");
		JButton delete = new JButton("Deletar");
		groupCombo.addActionListener(e -> {
			if (!reloading) {
				fillServers();
			}
		});
		add.addActionListener(e -> editServer(null));
		edit.addActionListener(e -> {
			if (getServer() != null) {
				editServer(getServer());
			}
		});
		delete.addActionListener(e -> deleteServer());

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new TitledBorder("Servidor"));
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("Grupo"), c);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 2, 0);
		panel.add(groupCombo, c);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		panel.add(new JLabel("Servidor"), c);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 0, 0, 0);
		panel.add(serverCombo, c);
		JButton[] buttons = { edit, add, delete };
		for (int i = 0; i < buttons.length; i++) {
			c = new GridBagConstraints();
			c.gridx = i;
			c.gridy = 2;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(0, 0, 0, i < buttons.length - 1 ? 3 : 0);
			panel.add(buttons[i], c);
		}
		return panel;
	}

	/**
	 * Reloads the groups and servers combos
	 * @param selected the server to be selected, can be null
	 */
	private void reloadServers(Server selected) {
		reloading = true;
		Object group = selected != null ? selected.group() : groupCombo.getSelectedItem();
		List<String> groups = reader.getGroups();
		groupCombo.removeAllItems();
		groupCombo.addItem(ALL_GROUPS);
		groups.forEach(groupCombo::addItem);
		if (group != null && groups.contains(group)) {
			groupCombo.setSelectedItem(group);
		}
		reloading = false;
		fillServers();
		if (selected != null && servers.contains(selected)) {
			serverCombo.setSelectedIndex(servers.indexOf(selected));
		}
	}

	/**
	 * Fills the servers combo with the servers of the selected group
	 */
	private void fillServers() {
		Object group = groupCombo.getSelectedItem();
		boolean all = groupCombo.getSelectedIndex() <= 0;
		serverCombo.removeAllItems();
		servers.clear();
		for (Server server : reader.getServers()) {
			if (all || server.group().equals(group)) {
				servers.add(server);
				serverCombo.addItem(server.name());
			}
		}
	}

	private void editServer(Server server) {
		PIRCEditServer edit =
			new PIRCEditServer(this, reader, reader.getGroups(), server);
		edit.setVisible(true);
		if (edit.getSavedServer() != null) {
			reloadServers(edit.getSavedServer());
		}
	}

	private void deleteServer() {
		Server server = getServer();
		if (server == null) {
			return;
		}
		int option =
			JOptionPane.showConfirmDialog(
				this,
				"Deletar o servidor " + server.name() + "?",
				"Deletar",
				JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			reader.remove(server);
			try {
				reader.save();
			} catch (IOException e) {
				PIRCExceptionWindow.showError(this, "Não foi possível salvar", e);
			}
			reloadServers(null);
		}
	}

	public String getAlternative() {
		return alternative.getText();
	}

	public String getEmail() {
		return email.getText();
	}

	/**
	 * @return the real name typed by the user
	 */
	public String getRealName() {
		return name.getText();
	}

	public String getNickname() {
		return nickname.getText();
	}

	/**
	 * Returns the selected server
	 * @return the server or null if no server is selected
	 */
	public Server getServer() {
		int index = serverCombo.getSelectedIndex();
		if (index < 0 || index >= servers.size()) {
			return null;
		}
		return servers.get(index);
	}

	private void connectionEvent() {
		if (getServer() == null) {
			warn("Selecione um servidor.");
			return;
		}
		if (getNickname().isBlank()) {
			warn("Informe o NickName.");
			return;
		}
		dispose();
		pircFrame.connect(this);
	}

	private void warn(String message) {
		JOptionPane.showMessageDialog(this, message, getTitle(), JOptionPane.WARNING_MESSAGE);
	}

	private final JTextField name = new JTextField("pIRC");
	private final JTextField email = new JTextField("seunome@seuprovedor.com");
	private final JTextField nickname = new JTextField("usuario_pIRC");
	private final JTextField alternative = new JTextField("usu_pIRC");
	private final JComboBox<String> serverCombo = new JComboBox<>();
	private final JComboBox<String> groupCombo = new JComboBox<>();
	private final PIRCServersReader reader =
		new PIRCServersReader(Path.of("cfg", "servers.xml"));
	private final List<Server> servers = new ArrayList<>();
	private final PIRCFrame pircFrame;
	private boolean reloading;

}
