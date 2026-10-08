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

import java.util.Objects;

/**
 * An IRC server of the servers list (cfg/servers.xml)
 * @param group the network, used to group the servers
 * @param name the name shown to the user
 * @param host the server address
 * @param port the server port
 */
public record Server(String group, String name, String host, int port) {

	public Server {
		Objects.requireNonNull(group, "group");
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(host, "host");
		if (port <= 0 || port > 65535) {
			throw new IllegalArgumentException("Invalid port: " + port);
		}
	}
}
