/**
  *  pIRC - Cliente de IRC desenvolvido em Java.
  * 
  * Copyright (C) 2002 -  Ademir Constantino Filho
  * 
  * Este programa Ã© software livre; vocÃª pode redistribuÃ­-lo e/ou
  * modificÃ¡-lo sob os termos da LicenÃ§a PÃºblica Geral GNU, conforme
  * publicada pela Free Software Foundation; tanto a versÃ£o 2 da
  * LicenÃ§a como (a seu critÃ©rio) qualquer versÃ£o mais nova.
  *
  * Este programa Ã© distribuÃ­do na expectativa de ser Ãºtil, mas SEM
  * QUALQUER GARANTIA; sem mesmo a garantia implÃ­cita de
  * COMERCIALIZAÃÃO ou de ADEQUAÃÃO A QUALQUER PROPÃSITO EM
  * PARTICULAR. Consulte a LicenÃ§a PÃºblica Geral GNU para obter mais
  * detalhes.
  *
  * VocÃª deve ter recebido uma cÃ³pia da LicenÃ§a PÃºblica Geral GNU
  * junto com este programa; se nÃ£o, escreva para a Free Software
  * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
  * 02111-1307, USA.
  *
  * VocÃª pode entrar em contato pelo endereÃ§o de email:
  * ziegfried@onda.com.br
  *
  */

package org.aconstantino.pirc;


/**
 *  Strip common mIRC colors
 *  @author     JoÃ£o Pedrosa
 */

public class ColorStrip {

	private final String author = "JoÃ£o Pedrosa";
	private final float versionInfo = (float) 0.2;
	private static final String RE_COLOR = "\u0003(\\d{1,2}(,\\d{1,2})?)?|[\u0002\u0016\u001f\u000f\u0001]";

	/**
	 * Constructor for ColorStrip.
	 */
	public ColorStrip() {
		super();
	}

	/**
	 * @return    Author of This file
	 */

	public String getAuthor() {
		return author;
	}

	/**
	 * @return    a float that contains IrcLib-Util.CorlorStrip Current Version
	 */

	public float getVersion() {
		return versionInfo;
	}

	/**
	 * Strip common mIRC colors/styles
	 * 
	 * @param  f1  Color string
	 * @return     a new String without colors
	 * @deprecated replaced with <a href="#stripColor(java.lang.String)">#stripColor(java.lang.String)</a>
	 */

	public String colorStrip(String f1) {
		return "";
	}

	/**
	 * Returns the versionInfo.
	 * @return float
	 */
	public float getVersionInfo() {
		return versionInfo;
	}

	/**
	 * Strip common mIRC colors/styles
	 *
	 * @param  s Styled string
	 * @return a new String without colors
	 */

	public String stripColor(String s) {
		return s.replaceAll(RE_COLOR, "");
	}

}
