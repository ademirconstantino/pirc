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

// file status: unterminated

package org.aconstantino.pirc;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

/**
 * @@author Ademir Constantino Filho <a href="mailto:ziegfried@@techie.com">ziegfried@@techie.com</a>
 * 17/09/2002 -  14:33:27 
 */
public class PIRCConnectionWindow extends JDialog {

	public PIRCConnectionWindow(PIRCFrame pircFrame) {
		this.pircFrame = pircFrame;
	}

	public void init() {
		getContentPane().setLayout(new BorderLayout());
		setSize(456, 320);
		initServers();
		//setModal(true);
		setTitle("Conexão");
		northCenter.setLayout(new BorderLayout());
		connect.setSize(150, 40);
		connect.setSize(150, 40);
		southPanel.setLayout(new BorderLayout());
		conPanel.setLayout(new FlowLayout());
		conPanel.add(connect);
		conPanel.add(cancel);
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connectionEvent(e);
			}
		});
		mainPanel.setLayout(defaultLayout);
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		initServerPanel();

		mainPanel.add(
			labela,
			new GridBagConstraints(
				0,
				1,
				1,
				1,
				0.0,
				0.0,
				GridBagConstraints.CENTER,
				GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0),
				0,
				0));
		mainPanel.add(
			name,
			new GridBagConstraints(
				1,
				1,
				1,
				1,
				0.0,
				0.0,
				GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0),
				300,
				0));
		mainPanel.add(
			labelb,
			new GridBagConstraints(
				0,
				2,
				1,
				1,
				0.0,
				0.0,
				GridBagConstraints.CENTER,
				GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0),
				0,
				0));
		mainPanel.add(
			email,
			new GridBagConstraints(
				1,
				2,
				1,
				1,
				0.0,
				0.0,
				GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0),
				300,
				0));
		mainPanel.add(
			labelc,
			new GridBagConstraints(
				0,
				3,
				1,
				1,
				0.0,
				0.0,
				GridBagConstraints.CENTER,
				GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0),
				0,
				0));
		mainPanel.add(
			nickname,
			new GridBagConstraints(
				1,
				3,
				1,
				1,
				0.0,
				0.0,
				GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0),
				300,
				0));
		mainPanel.add(
			labeld,
			new GridBagConstraints(
				0,
				4,
				1,
				1,
				0.0,
				0.0,
				GridBagConstraints.CENTER,
				GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0),
				0,
				0));

		mainPanel.add(
			alternative,
			new GridBagConstraints(
				1,
				4,
				1,
				1,
				0.0,
				0.0,
				GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0),
				300,
				0));

		mainPanel.add(
			sb_Panel,
			new GridBagConstraints(
				0,
				5,
				0,
				0,
				0.0,
				0.0,
				GridBagConstraints.CENTER,
				GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0),
				0,
				0));

		topImagePanel.setLayout(new BorderLayout());
		topImagePanel.add(new JLabel(topImage), BorderLayout.NORTH);
		topImagePanel.add(new JSeparator());
		northCenter.add(mainPanel, BorderLayout.CENTER);
		getContentPane().add(topImagePanel, BorderLayout.NORTH);
		getContentPane().add(northCenter, BorderLayout.CENTER);
		southPanel.add(conPanel, BorderLayout.CENTER);
		southPanel.add(new JSeparator(), BorderLayout.NORTH);
		getContentPane().add(southPanel, BorderLayout.SOUTH);

	}

	public void initServerPanel() {
		GridBagConstraints gridBagConstraints;

		sb_Panel.setLayout(new GridBagLayout());
		sb_Panel.setBorder(new TitledBorder("Servidor"));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		sb_Panel.add(sl_Group, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(0, 0, 2, 0);
		sb_Panel.add(jc_Group, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		sb_Panel.add(sl_Server, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(1, 0, 0, 0);
		sb_Panel.add(jc_Server, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(0, 0, 0, 3);
		sb_Panel.add(sb_Edit, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(0, 0, 0, 3);
		sb_Panel.add(sb_Add, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		sb_Panel.add(sb_Del, gridBagConstraints);
		getContentPane().add(sb_Panel, BorderLayout.CENTER);

	}

	public void initServers() {
		g = new Groups(reader.getH());

		jc_Group.addItem("all");
		for (int i = 0; i < g.getGroups().size(); i++) {
			try {
				String s = (String) g.getGroups().get(i);
				jc_Group.addItem(s);
			} catch (NullPointerException e) {
			}
		}
		for (int i = 0; i < g.getServerValues().size(); i++) {
			if (jc_Group.getSelectedIndex() == 0) {
				String[] x = (String[]) g.getServerValues().get(i);
				jc_Server.addItem(x[1]);
			} else {
				break;
			}
		}

		jc_Group.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = (String) jc_Group.getSelectedItem();
				if (jc_Group.getSelectedIndex() != 0) {
					if (g.getGroup(s)) {
						jc_Server.removeAllItems();
						for (int i = 0; i < g.getServerValues().size(); i++) {
							if (jc_Group.getSelectedIndex() > 0) {
								String[] x =
									(String[]) g.getServerValues().get(i);
								if (x[0].equals(s)) {
									jc_Server.addItem(x[1]);
								}
							} else {
								break;
							}
						}
					}
				} else {
					jc_Server.removeAllItems();
					for (int i = 0; i < g.getServerValues().size(); i++) {
						String[] x = (String[]) g.getServerValues().get(i);
						jc_Server.addItem(x[1]);
					}
				}
			}
		});
	}

	public String getAlternative() {
		return alternative.getText();
	}

	public String getEmail() {
		return email.getText();
	}

	public String getName() {
		return name.getText();
	}

	public String getNickname() {
		return nickname.getText();
	}

	public String getServer() {
		return ""; //server.getText();
	}

	private void connectionEvent(ActionEvent e) {
		pircFrame.connect(this);
		dispose();
	}

	private final JLabel labela = new JLabel("Name: "),
		labelb = new JLabel("Email: "),
		labelc = new JLabel("NickName: "),
		labeld = new JLabel("Alternativo: ");
	private final JTextField name = new JTextField("pIRC"),
		email = new JTextField("seunome@@seuprovedor.com"),
		nickname = new JTextField("usuario_pIRC"),
		alternative = new JTextField("usu_pIRC");
	private final JPanel serverPanel = new JPanel(),
		mainPanel = new JPanel(),
		conPanel = new JPanel(),
		southPanel = new JPanel(),
		northCenter = new JPanel();
	private final JButton connect = new JButton("Conectar");
	private final PIRCFrame pircFrame;
	private final JButton cancel = new JButton("Cancelar");
	private final GridBagLayout defaultLayout = new GridBagLayout();
	private final GridBagConstraints defaultLayoutConstraints =
		new GridBagConstraints();
	private final JPanel topImagePanel = new JPanel();
	private final ImageIcon topImage =
		new ImageIcon("org/aconstantino/pirc/images/connection_top.gif");
	private final JLabel sl_Group = new JLabel("Grupo");
	private final JLabel sl_Server = new JLabel("Servidor");
	private final JButton sb_Edit = new JButton("Editar");
	private final JButton sb_Add = new JButton("Adicionar");
	private final JButton sb_Del = new JButton("Deletar");
	private final JPanel sb_Panel = new JPanel();
	private final JComboBox jc_Server = new JComboBox();
	private final JComboBox jc_Group = new JComboBox();
	private PIRCServersReader reader = new PIRCServersReader("cfg/servers.xml");
	private Groups g;

}

class Groups {

	public Groups(Vector v) {
		this.vs = v;
		init();
	}

	private void init() {
		for (int i = 0; i < vs.size(); i++) {
			String[] element = (String[]) vs.get(i);
			addServerValue(element);
			if (!getGroup(element[0])) {
				groups.add(element[0]);
			}
		}
	}

	public boolean getGroup(String groupName) {
		return groups.contains(groupName);
	}

	public Vector getGroups() {
		return groups;
	}

	public Vector getServerValues() {
		return serverValues;
	}

	public void addServerValue(String[] element) {
		serverValues.add(element);
	}

	private Vector groups = new Vector();
	private String[] a;
	private Vector vs;
	private Vector serverValues = new Vector();

}
