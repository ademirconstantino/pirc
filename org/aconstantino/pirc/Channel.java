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
