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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A line sent by the IRC server: [':' prefix ' '] command [' ' params]
 * The last parameter may start with ':' and then contains spaces.
 */
record IrcMessage(String prefix, String command, List<String> params) {

	IrcMessage {
		params = List.copyOf(params);
	}

	/**
	 * Parses a line sent by the server
	 * @param line the line without CRLF
	 * @return the message or null if the line has no command
	 */
	static IrcMessage parse(String line) {
		String rest = line.strip();
		String prefix = "";
		if (rest.startsWith(":")) {
			int space = rest.indexOf(' ');
			if (space == -1) {
				return null;
			}
			prefix = rest.substring(1, space);
			rest = rest.substring(space + 1).stripLeading();
		}
		List<String> params = new ArrayList<>();
		String command = null;
		while (!rest.isEmpty()) {
			if (command != null && rest.startsWith(":")) {
				params.add(rest.substring(1));
				break;
			}
			int space = rest.indexOf(' ');
			String token = space == -1 ? rest : rest.substring(0, space);
			if (command == null) {
				command = token.toUpperCase(Locale.ROOT);
			} else {
				params.add(token);
			}
			rest = space == -1 ? "" : rest.substring(space + 1).stripLeading();
		}
		return command == null ? null : new IrcMessage(prefix, command, params);
	}

	/**
	 * @return the parameter at the index or "" if it does not exist
	 */
	String param(int index) {
		return index >= 0 && index < params.size() ? params.get(index) : "";
	}

	/**
	 * @return the last parameter (usually the text after " :") or ""
	 */
	String trailing() {
		return params.isEmpty() ? "" : params.get(params.size() - 1);
	}

	/**
	 * @return the parameters from the index joined with spaces
	 */
	String paramsFrom(int index) {
		return index < params.size()
			? String.join(" ", params.subList(index, params.size()))
			: "";
	}

	boolean isNumeric() {
		return command.length() == 3 && command.chars().allMatch(Character::isDigit);
	}

	/**
	 * @return the numeric reply code or -1 if the command is not numeric
	 */
	int numeric() {
		return isNumeric() ? Integer.parseInt(command) : -1;
	}

	/**
	 * @return the nickname of the prefix (nick!user@host)
	 */
	String nick() {
		int index = prefix.indexOf('!');
		return index > -1 ? prefix.substring(0, index) : prefix;
	}

	/**
	 * @return the user@host of the prefix (nick!user@host) or ""
	 */
	String host() {
		int index = prefix.indexOf('!');
		return index > -1 ? prefix.substring(index + 1) : "";
	}
}
