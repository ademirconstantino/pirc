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

public interface ProtocolHandlerListener {

	public void onUnknown(String unknownString);
	public void onWelcome(String welcomeStr);
	public void onYourHost(String params);
	public void onCreated(String params);
	public void onBounce(String serverName);
	public void onPing();
	public void onSendPong();
	public void onMyInfo(String params);
	public void onJoin(String channel, String prefix);
	public void onPart(String channel, String prefix);
	public void onNick(String newNick, String oldNick);
	public void onNamReply(NickNameList nicks, String chan);
	public void onChanMsg(String to, String msg, String from);
	public void onTopic(String channel, String param);
	public void onTopicSet(String by, String chan, String newTopic);
}
@


1.1
log
@ouxi
@
text
@@

