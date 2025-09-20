head	1.2;
access;
symbols;
locks; strict;
comment	@# @;
expand	@o@;


1.2
date	2004.11.13.20.36.01;	author ziegfried;	state dead;
branches;
next	1.1;

1.1
date	2002.12.11.05.49.16;	author ziegfried;	state Exp;
branches;
next	;


desc
@@


1.2
log
@*** empty log message ***
@
text
@/**
  *  pIRC - Cliente de IRC desenvolvido em Java.
  * 
  * Copyright (C) 2002 -  Ademir Constantino Filho
  * 
  * Este programa � software livre; voc� pode redistribu�-lo e/ou
  * modific�-lo sob os termos da Licen�a P�blica Geral GNU, conforme
  * publicada pela Free Software Foundation; tanto a vers�o 2 da
  * Licen�a como (a seu crit�rio) qualquer vers�o mais nova.
  *
  * Este programa � distribu�do na expectativa de ser �til, mas SEM
  * QUALQUER GARANTIA; sem mesmo a garantia impl�cita de
  * COMERCIALIZA��O ou de ADEQUA��O A QUALQUER PROP�SITO EM
  * PARTICULAR. Consulte a Licen�a P�blica Geral GNU para obter mais
  * detalhes.
  *
  * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral GNU
  * junto com este programa; se n�o, escreva para a Free Software
  * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
  * 02111-1307, USA.
  *
  * Voc� pode entrar em contato pelo endere�o de email:
  * ziegfried@@onda.com.br
  *
  */

package org.aconstantino.pirc;


/**
 *  Strip common mIRC colors
 *  @@author     Jo�o Pedrosa
 */

public class ColorStrip {

	private final String author = "Jo�o Pedrosa";
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
@


1.1
log
@ouxi
@
text
@@

