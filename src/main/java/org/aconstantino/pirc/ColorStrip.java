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

import java.util.regex.Pattern;

/**
 *  Strip common mIRC colors
 *  @author     João Pedrosa
 */

public final class ColorStrip {

	/* color (^C fg,bg), bold, reverse, underline, italic, strike, reset and CTCP */
	private static final Pattern RE_COLOR =
		Pattern.compile(
			"\u0003(\\d{1,2}(,\\d{1,2})?)?|[\u0002\u0016\u001f\u001d\u001e\u000f\u0001]");

	private ColorStrip() {
	}

	/**
	 * Strip common mIRC colors/styles
	 *
	 * @param  s Styled string
	 * @return a new String without colors
	 */

	public static String stripColor(String s) {
		return RE_COLOR.matcher(s).replaceAll("");
	}

}
