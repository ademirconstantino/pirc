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

import java.util.Hashtable;

/**
 * @@author Ademir Constantino Filho <a href="mailto:ziegfried@@techie.com">ziegfried@@techie.com</a>
 * 20/09/2002 -  12:34:24 
 */
public class ChannelsHashTable extends Hashtable {

	public ChannelsHashTable(PIRCFrame pirc) {
		this.pirc = pirc;
	}

	public void putChannel(Channel channel) {
		String name = channel.getName();
		put(name, channel);
	}

	public boolean getChannelExists(String channelName) {
		Channel ch = (Channel) get(channelName);
		boolean exists = false;
		if (ch != null) {
			exists = true;
		}
		return exists;
	}

	public Channel getChannel(String channelName) {
		return (Channel) get(channelName);
	}

	public void removeChannel(String channelName) {
		remove(channelName);
	}

	private PIRCFrame pirc;

}
