package org.aconstantino.pirc;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.jupiter.api.Test;

class IRCSocketTest {

	@Test
	void decodesUtf8() {
		assertEquals("ação", IRCSocket.decode("ação\r".getBytes(UTF_8)));
	}

	@Test
	void fallsBackToLatin1() {
		assertEquals("ação", IRCSocket.decode("ação".getBytes(ISO_8859_1)));
	}

	@Test
	void rejectsInvalidPort() {
		assertThrows(IllegalArgumentException.class, () -> new IRCSocket("localhost", 0));
		assertThrows(IllegalArgumentException.class, () -> new IRCSocket("localhost", 70000));
	}

	@Test
	void sendsAndReadsLines() throws Exception {
		try (ServerSocket server = new ServerSocket(0, 1, InetAddress.getLoopbackAddress())) {
			IRCSocket socket = new IRCSocket("127.0.0.1", server.getLocalPort());
			socket.connect();
			assertTrue(socket.isConnected());
			try (Socket peer = server.accept()) {
				socket.println("PRIVMSG #pirc :olá\r\nQUIT");
				BufferedReader in =
					new BufferedReader(new InputStreamReader(peer.getInputStream(), UTF_8));
				/* CR and LF inside a line can not inject a second command */
				assertEquals("PRIVMSG #pirc :olá  QUIT", in.readLine());

				OutputStream out = peer.getOutputStream();
				out.write("PING :srv\r\n".getBytes(UTF_8));
				out.write("é latin1\r\n".getBytes(ISO_8859_1));
				out.write("sem CRLF".getBytes(UTF_8));
				out.flush();
				peer.shutdownOutput();

				assertEquals("PING :srv", socket.readLine());
				assertEquals("é latin1", socket.readLine());
				assertEquals("sem CRLF", socket.readLine());
				assertNull(socket.readLine());
			}
			socket.close();
			assertFalse(socket.isConnected());
		}
	}

	@Test
	void failsWhenNotConnected() {
		IRCSocket socket = new IRCSocket("localhost", 6667);
		assertThrows(IRCSocketException.class, () -> socket.println("PING"));
		assertThrows(IRCSocketException.class, socket::readLine);
	}
}
