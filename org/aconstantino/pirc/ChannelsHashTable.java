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
@


1.1
log
@ouxi
@
text
@@

