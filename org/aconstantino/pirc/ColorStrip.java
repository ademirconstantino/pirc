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


/**
 *  Strip common mIRC colors
 *  @@author     João Pedrosa
 */

public class ColorStrip {

	private final String author = "João Pedrosa";
	private final float versionInfo = (float) 0.2;
	private static final String RE_COLOR = "(+(\\d{1,2})?(,\\d{1,2})?)?()?()?()?()?()?";;

	/**
	 * Constructor for ColorStrip.
	 */
	public ColorStrip() {
		super();
	}

	/**
	 * @@return    Author of This file
	 */

	public String getAuthor() {
		return author;
	}

	/**
	 * @@return    a float that contains IrcLib-Util.CorlorStrip Current Version
	 */

	public float getVersion() {
		return versionInfo;
	}

	/**
	 * Strip common mIRC colors/styles
	 * 
	 * @@param  f1  Color string
	 * @@return     a new String without colors
	 * @@deprecated replaced with <a href="#stripColor(java.lang.String)">#stripColor(java.lang.String)</a>
	 */

	public String colorStrip(String f1) {
		return "";
	}

	/**
	 * Returns the versionInfo.
	 * @@return float
	 */
	public float getVersionInfo() {
		return versionInfo;
	}

	/**
	 * Strip common mIRC colors/styles
	 *
	 * @@param  s Styled string
	 * @@return a new String without colors
	 */

	public String stripColor(String s) {
		return s.replaceAll(RE_COLOR, "");
	}

}
