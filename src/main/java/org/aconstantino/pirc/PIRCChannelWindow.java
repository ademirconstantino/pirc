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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 * @author Ademir Constantino Filho <a href="mailto:ziegfried@techie.com">ziegfried@techie.com</a>
 * 20/09/2002 -  18:37:37 
 */
public final class PIRCChannelWindow extends ChatWindow {

	public PIRCChannelWindow(Channel channel, PIRCFrame pircframe) {
		super(channel.getName(), pircframe, true);
		this.channel = channel;
		nicks.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		nicks.setFont(FONT);
		nicks.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					openPrivate();
				}
			}
		});
		getChatPanel().add(new JScrollPane(nicks), BorderLayout.EAST);
		updateNicks();
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosed(InternalFrameEvent evt) {
				sair();
			}
		});
		setClosable(true);
		setSize(700, 400);
		setVisible(true);
	}

	@Override
	String getTarget() {
		return channel.getName();
	}

	public void updateNicks() {
		nicksModel.clear();
		for (String nick : channel.getNickList()) {
			nicksModel.addElement(nick);
		}
	}

	public void updateTopic() {
		append(
			"-- Talking in "
				+ channel.getName()
				+ " - "
				+ ZonedDateTime.now().format(
					DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
		append(channel.getName() + " Topic is \"" + channel.getTopic() + "\"");
	}

	/**
	 * Leaves the channel when the window is closed by the user
	 */
	private void sair() {
		if (pircFrame.getChannelWindow(channel.getName()) != this) {
			/* the channel was already removed (kick, part, disconnection) */
			return;
		}
		if (pircFrame.isConnected()) {
			try {
				pircFrame.getIrcSocket().println("PART " + channel.getName());
			} catch (IRCSocketException e) {
				// the reader thread notices the closed connection
			}
		}
		pircFrame.removeChannel(channel.getName());
	}

	/**
	 * Opens a private message window with the selected nickname
	 */
	private void openPrivate() {
		String selected = nicks.getSelectedValue();
		if (selected != null) {
			String nick = NickNameList.stripStatus(selected);
			if (!nick.equalsIgnoreCase(pircFrame.getMyNickName())) {
				pircFrame.getPrivateWindow(nick, true);
			}
		}
	}

	private final Channel channel;
	private final DefaultListModel<String> nicksModel = new DefaultListModel<>();
	private final JList<String> nicks = new JList<>(nicksModel);

}
