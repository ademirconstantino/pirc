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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The channels pIRC has joined, by name
 * @author Ademir Constantino Filho <a href="mailto:ziegfried@techie.com">ziegfried@techie.com</a>
 * 20/09/2002 -  12:34:24
 */
public final class Channels {

	public void put(Channel channel) {
		channels.put(channel.getName(), channel);
	}

	public boolean contains(String channelName) {
		return channels.containsKey(channelName);
	}

	/**
	 * @return the channel or null if pIRC is not in the channel
	 */
	public Channel get(String channelName) {
		return channels.get(channelName);
	}

	public void remove(String channelName) {
		channels.remove(channelName);
	}

	public void clear() {
		channels.clear();
	}

	/**
	 * @return a copy of the channels, in the order they were joined
	 */
	public Collection<Channel> all() {
		return List.copyOf(channels.values());
	}

	private final Map<String, Channel> channels = new LinkedHashMap<>();

}
