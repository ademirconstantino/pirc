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

import java.util.Vector;

/**
 * @@author Ademir Constantino Filho <a href="mailto:ziegfried@@techie.com">ziegfried@@techie.com</a>
 * 17/09/2002 -  11:22:03 
 */

public class Channel {

	public Channel() {
	}
	
	public Channel(String channelName) {
		this.setName(channelName);
	}

	/**
	 * Constructor for Channel.
	 */
	public Channel(String name, String modes, String topic) {
		this.name = name;
		this.modes = modes;
		this.topic = topic;
	}


	/**
	 * Returns the modes.
	 * @@return String
	 */
	public String getModes() {
		return modes;
	}

	/**
	 * Returns the name.
	 * @@return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the topic.
	 * @@return String
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * Sets the modes.
	 * @@param modes The modes to set
	 */
	public void setModes(String modes) {
		this.modes = modes;
	}

	/**
	 * Sets the name.
	 * @@param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the topic.
	 * @@param topic The topic to set
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}

	private String modes, name, topic;
	private NickNameList nickList = new NickNameList();

	/**
	 * Returns the nickList.
	 * @@return NickNameList
	 */
	public NickNameList getNickList() {
		return nickList;
	}
	

	/**
	 * Sets the nickList.
	 * @@param nickList The nickList to set
	 */
	public void setNickList(NickNameList nickList) {
		this.nickList = nickList;
	}

}
@


1.1
log
@ouxi
@
text
@@

