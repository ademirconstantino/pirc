package org.aconstantino.pirc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class IrcMessageTest {

	@Test
	void parsesPrefixCommandAndTrailing() {
		IrcMessage m = IrcMessage.parse(":nick!user@host.com PRIVMSG #pirc :olá mundo :)");
		assertEquals("nick!user@host.com", m.prefix());
		assertEquals("nick", m.nick());
		assertEquals("user@host.com", m.host());
		assertEquals("PRIVMSG", m.command());
		assertEquals(List.of("#pirc", "olá mundo :)"), m.params());
		assertEquals("olá mundo :)", m.trailing());
	}

	@Test
	void parsesWithoutPrefix() {
		IrcMessage m = IrcMessage.parse("PING :irc.brasnet.org");
		assertEquals("", m.prefix());
		assertEquals("PING", m.command());
		assertEquals("irc.brasnet.org", m.trailing());
	}

	@Test
	void commandIsUpperCase() {
		assertEquals("PING", IrcMessage.parse("ping :x").command());
	}

	@Test
	void serverPrefixHasNoHost() {
		IrcMessage m = IrcMessage.parse(":irc.server.org NOTICE * :*** Looking up your hostname");
		assertEquals("irc.server.org", m.nick());
		assertEquals("", m.host());
	}

	@Test
	void parsesNumericReplies() {
		IrcMessage m = IrcMessage.parse(":srv 353 me = #pirc :@op +voice user");
		assertTrue(m.isNumeric());
		assertEquals(ProtocolHandlerConstants.NAM_REPLY, m.numeric());
		assertEquals("#pirc", m.param(2));
		assertEquals("@op +voice user", m.trailing());
		assertEquals("= #pirc @op +voice user", m.paramsFrom(1));

		IrcMessage welcome = IrcMessage.parse(":srv 001 me :Welcome");
		assertEquals(ProtocolHandlerConstants.WELCOME, welcome.numeric());
	}

	@Test
	void commandsAreNotNumeric() {
		IrcMessage m = IrcMessage.parse("JOIN #pirc");
		assertFalse(m.isNumeric());
		assertEquals(-1, m.numeric());
	}

	@Test
	void joinWithOrWithoutColon() {
		assertEquals("#pirc", IrcMessage.parse(":a!b@c JOIN :#pirc").param(0));
		assertEquals("#pirc", IrcMessage.parse(":a!b@c JOIN #pirc").param(0));
	}

	@Test
	void missingParamsAreEmpty() {
		IrcMessage m = IrcMessage.parse(":a!b@c QUIT");
		assertEquals("", m.trailing());
		assertEquals("", m.param(3));
		assertEquals("", m.param(-1));
		assertEquals("", m.paramsFrom(1));
	}

	@Test
	void kickWithoutReason() {
		IrcMessage m = IrcMessage.parse(":op!b@c KICK #pirc victim");
		assertEquals("victim", m.param(1));
		assertEquals("", m.param(2));
	}

	@Test
	void emptyTrailingIsKept() {
		IrcMessage m = IrcMessage.parse(":srv TOPIC #pirc :");
		assertEquals(List.of("#pirc", ""), m.params());
	}

	@Test
	void collapsesRepeatedSpaces() {
		IrcMessage m = IrcMessage.parse(":srv  MODE  #pirc  +o  nick");
		assertEquals("MODE", m.command());
		assertEquals("+o nick", m.paramsFrom(1));
	}

	@Test
	void invalidLinesReturnNull() {
		assertNull(IrcMessage.parse(""));
		assertNull(IrcMessage.parse("   "));
		assertNull(IrcMessage.parse(":prefixonly"));
	}
}
