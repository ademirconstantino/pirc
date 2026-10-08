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

import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 * @author Ademir Constantino Filho <a href="mailto:ziegfried@techie.com">ziegfried@techie.com</a>
 * 28/09/2002 -  12:21:09 
 */
public final class PrivateMessageSession extends ChatWindow {

	public PrivateMessageSession(String nickname, PIRCFrame pircframe) {
		super(nickname, pircframe, true);
		this.nickname = nickname;
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosed(InternalFrameEvent evt) {
				pircFrame.removePrivateWindow(getNickname());
			}
		});
		setClosable(true);
		setSize(400, 400);
		setVisible(true);
	}

	@Override
	String getTarget() {
		return nickname;
	}

	/**
	 * Returns the nickname.
	 * @return String
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Sets the nickname, when the user changes it.
	 * @param nickname The nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
		setTitle(nickname);
	}

	private String nickname;

}
