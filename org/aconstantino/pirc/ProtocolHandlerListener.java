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
