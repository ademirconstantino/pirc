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

interface ProtocolHandlerConstants {

	static final int WELCOME = 001;
	static final int YOUR_HOST = 002;
	static final int CREATED = 003;
	static final int MY_INFO = 004;
	static final int BOUNCE = 005;
	static final int NONE = 300;
	static final int AWAY = 301;
	static final int USERHOST = 302;
	static final int ISON = 303;	
	static final int UNAWAY = 305;
	static final int NOW_AWAY = 306;
	static final int WHOIS_USER = 311;	
	static final int WHOIS_SERVER = 312;	
	static final int WHOIS_OPERATOR = 313;
	static final int WHOW_AS_USER = 314;	
	static final int WHOIS_IDLE = 317;
	static final int END_OF_WHOIS = 318;
	static final int WHOIS_CHANNELS = 319;
	static final int END_OF_WHOWAS = 369;
	static final int LIST_START = 321;
	static final int LIST = 322;	
	static final int LIST_END = 323;	
	static final int UNIQ_OP_IS = 324;	
	static final int CHANNEL_MODE_IS = 324;
	static final int NO_TOPIC = 331;
	static final int TOPIC = 332;
	static final int INVITING = 341;
	static final int SUMMONING = 342;
	static final int INVITE_LIST = 346;
	static final int END_OF_INVITE_LIST = 347;
	static final int EXCEPT_LIST = 348;
	static final int END_OF_EXCEPT_LIST = 349;
	static final int VERSION = 351;
	static final int WHO_REPLY = 352;
	static final int END_OF_WHO = 315;
	static final int NAM_REPLY = 353;
	static final int END_OF_NAMES = 366;
	static final int LINKS = 364;
	static final int END_OF_LINKS = 365;
	static final int BAN_LIST = 367;
	static final int END_OF_BANLIST = 368;
	static final int INFO = 371;
	static final int END_OF_INFO = 374;
	static final int MOTD_START = 375;
	static final int MOTD = 372;
	static final int END_OF_MOTD = 376;
	static final int YOURE_OPER = 381;
	static final int REHASHING = 382;
	static final int YOURE_SERVICE = 383;
	static final int TIME = 391;
	static final int USERS_START = 392;	
	static final int USERS = 393;	
	static final int END_OF_USERS = 394;	
	static final int NO_USERS = 395;
	static final int TRACE_LINK = 200;
	static final int TRACE_CONNECTING = 201;
	static final int TRACE_HAND_SHAKE = 202;
	static final int TRACE_UNKNOWN = 203;
	static final int TRACE_OPERATOR = 204;	
	static final int TRACE_USER = 205;
	static final int TRACE_SERVER = 206;
	static final int TRACE_SERVICE = 207;
	static final int TRACE_NEW_TYPE = 208;
	static final int TRACE_CLASS = 209;	
	static final int STATS_COMMANDS = 212;
	static final int ENDOF_STATS = 219;
	static final int STATS_UPTIME = 242;
	static final int STATS_OLINE = 243;
	static final int UMODE_IS = 221;
	static final int SERVLIST = 234;
	static final int SERVLIST_END = 235;
	static final int LUSER_CLIENT = 251;
	static final int LUSER_OP = 252;
	static final int LUSER_UNKNOWN = 253;	
	static final int LUSER_CHANNELS = 254;
	static final int LUSER_ME = 255;
	static final int ADMIN_ME = 256;	
	static final int ADMIN_LOC1 = 257;	
	static final int ADMIN_LOC2 = 258;	
	static final int ADMIN_EMAIL = 259;
	static final int TRY_AGAIN = 263;	
	static final int ERR_NOSUCH_NICK = 401;	
	static final int ERR_NOSUCH_SERVER = 402;
	static final int ERR_NOSUCH_CHANNEL = 403;	
	static final int ERR_CANNOT_SEND_TO_CHAN = 404;
	static final int ERR_TOO_MANY_CHANNELS = 405;
	static final int ERR_WAS_NO_SUCH_NICK = 406;
	static final int ERR_TOO_MANY_TARGETS = 407;
	static final int ERR_NO_SUCH_SERVICE = 408;
	static final int ERR_NO_ORIGIN = 409;
	static final int ERR_NO_RECIPIENT = 411;
	static final int ERR_NO_TEXT_TO_SEND = 412;
	static final int ERR_NO_TOP_LEVEL = 413;
	static final int ERR_WILD_TOP_LEVEL = 414;
	static final int ERR_BADMASK = 415;
	static final int ERR_UNKNOWN_COMMAND = 421;
	static final int ERR_NO_MOTD = 422;
	static final int ERR_NO_ADMIN_INFO = 423;
	static final int ERR_FILE_ERROR = 424;
	static final int ERR_NO_NICK_NAME_GIVEN = 431;
	static final int ERR_ERRONEUS_NICKNAME = 432;
	static final int ERR_NICK_NAME_IN_USE = 433;
	static final int ERR_NICK_COLLISION = 436;
	static final int ERR_UNAVAILRESOURCE = 437;
	static final int ERR_USER_NOT_IN_CHANNEL = 441;
	static final int ERR_NOT_ON_CHANNEL = 442;
	static final int ERR_USER_ON_CHANNEL = 443;
	static final int ERR_NO_LOGIN = 444;
	static final int ERR_SUMMON_DISABLED = 445;	
	static final int ERR_USERS_DISABLED = 446;	
	static final int ERR_NOT_REGISTERED = 451;	
	static final int ERR_NEED_MORE_PARAMS = 461;	
	static final int ERR_ALREADY_REGISTRED = 462;	
	static final int ERR_NO_PERM_FOR_HOST = 463;	
	static final int ERR_PASSWD_MISMATCH = 464;
	static final int ERR_YOURE_BANNED_CREEP = 465; // 466	
	static final int ERR_YOUWILLBEBANNED = 466; // 465	
	static final int ERR_KEY_SET = 467;	
	static final int ERR_CHANNEL_IS_FULL = 471;
	static final int ERR_UNKNOWN_MODE = 472;	
	static final int ERR_INVITE_ONLY_CHAN = 473;	
	static final int ERR_BANNED_FROM_CHAN = 474;	
	static final int ERR_BAD_CHANNEL_KEY = 475;	
	static final int ERR_BADCHANMASK = 476;	
	static final int ERR_NOCHANMODES = 477;	
	static final int ERR_BANLISTFULL = 478;
	static final int ERR_NO_PRIVILEGES = 481;	
	static final int ERR_CHAN_OPRIVS_NEEDED = 482;	
	static final int ERR_CANT_KILL_SERVER = 483;	
	static final int ERR_RESTRICTED = 484;	
	static final int ERR_UNIQOPPRIVSNEEDED = 485;	
	static final int ERR_NO_OPER_HOST = 491;
	static final int ERR_UMODE_UNKNOWN_FLAG = 501;
	static final int ERR_USERS_DONT_MATCH = 502;
}
