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
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

/**
 * @author Ademir Constantino Filho <a href="mailto:ziegfried@techie.com">ziegfried@techie.com</a>
 * 19/09/2002 -  14:10:30 
 */
public final class PIRCExceptionWindow extends JDialog {

	/**
	 * Shows the error in a modal dialog
	 * @param owner the window the dialog is shown over, can be null
	 * @param title the dialog title
	 * @param e the error
	 */
	public static void showError(Window owner, String title, Exception e) {
		new PIRCExceptionWindow(owner, title, e).setVisible(true);
	}

	private PIRCExceptionWindow(Window owner, String title, Exception e) {
		super(owner, title, ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(300, 200);

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(new JSeparator());
		topPanel.add(
			new JLabel(
				new ImageIcon(
					PIRCExceptionWindow.class.getResource("images/exception_top.gif"))),
			BorderLayout.NORTH);
		getContentPane().add(topPanel, BorderLayout.NORTH);

		JTextArea details =
			new JTextArea(e.getMessage() != null ? e.getMessage() : e.toString());
		details.setEditable(false);
		details.setLineWrap(true);
		details.setWrapStyleWord(true);
		getContentPane().add(details, BorderLayout.CENTER);

		JButton close = new JButton("Fechar");
		close.addActionListener(evt -> dispose());
		JPanel closePanel = new JPanel(new BorderLayout());
		closePanel.add(new JSeparator(), BorderLayout.NORTH);
		closePanel.add(close, BorderLayout.SOUTH);
		getContentPane().add(closePanel, BorderLayout.SOUTH);
		getRootPane().setDefaultButton(close);
		setLocationRelativeTo(owner);
	}

}
