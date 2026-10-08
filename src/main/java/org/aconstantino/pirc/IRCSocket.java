/*
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
  * ziegfried@onda.com.br
  *
  */

package org.aconstantino.pirc;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.util.Objects;

/**
 *
 * The irclib socket. Sends the lines in UTF-8 and reads lines in UTF-8,
 * falling back to ISO-8859-1 for lines that are not valid UTF-8.
 * @author Ziegfried Duhbe
 * @version 0.3
 */

public final class IRCSocket implements Closeable {

	/** Time to wait for the server to accept the connection (30 seconds) */
	public static final int CONNECT_TIMEOUT = 30_000;

	/**
	 * @param serverName the server address
	 * @param serverPort the server port ( 6667 most popular port )
	 */
	public IRCSocket(String serverName, int serverPort) {
		this.serverName = Objects.requireNonNull(serverName, "serverName");
		if (serverPort <= 0 || serverPort > 65535) {
			throw new IllegalArgumentException("Invalid port: " + serverPort);
		}
		this.serverPort = serverPort;
	}

	/**
	 * Connect to the IRC Server, blocks until connected
	 *
	 * @exception  IRCSocketException if the connection fails
	 */
	public void connect() throws IRCSocketException {
		if (socket != null) {
			throw new IRCSocketException("Already connected.");
		}
		Socket s = new Socket();
		try {
			s.connect(new InetSocketAddress(serverName, serverPort), CONNECT_TIMEOUT);
			in = new BufferedInputStream(s.getInputStream());
			out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), UTF_8));
			socket = s;
		} catch (IOException e) {
			try {
				s.close();
			} catch (IOException ignored) {
				// already failed
			}
			throw new IRCSocketException(
				e.getMessage() != null ? e.getMessage() : e.toString());
		}
	}

	/**
	 *  Close the IRCSocket
	 */

	@Override
	public void close() {
		Socket s = socket;
		if (s != null) {
			try {
				s.close();
			} catch (IOException e) {
				// nothing to do
			}
		}
	}

	/**
	 * Close IRCSocket sending a Quit message to IRC Server
	 *
	 * @param  quitMessage             Quit Message ( Bye! )
	 */

	public void close(String quitMessage) {
		try {
			println("QUIT :" + quitMessage);
		} catch (IRCSocketException e) {
			// closing anyway
		}
		close();
	}

	/**
	 * Reads a line sent by the server
	 *
	 * @return the line without CRLF, or null when the connection is closed
	 * @exception  IRCSocketException if the connection fails
	 */

	public String readLine() throws IRCSocketException {
		if (in == null) {
			throw new IRCSocketException("Not connected.");
		}
		try {
			lineBuffer.reset();
			int b;
			while ((b = in.read()) != -1) {
				if (b == '\n') {
					return decode(lineBuffer.toByteArray());
				}
				lineBuffer.write(b);
			}
			return lineBuffer.size() > 0 ? decode(lineBuffer.toByteArray()) : null;
		} catch (IOException e) {
			throw new IRCSocketException(e.getMessage());
		}
	}

	/**
	 * Decodes a line in UTF-8, or in ISO-8859-1 if it is not valid UTF-8
	 * @param bytes the line, with or without the CR
	 */
	static String decode(byte[] bytes) {
		int length = bytes.length;
		if (length > 0 && bytes[length - 1] == '\r') {
			length--;
		}
		try {
			return UTF_8
				.newDecoder()
				.onMalformedInput(CodingErrorAction.REPORT)
				.onUnmappableCharacter(CodingErrorAction.REPORT)
				.decode(ByteBuffer.wrap(bytes, 0, length))
				.toString();
		} catch (CharacterCodingException e) {
			return new String(bytes, 0, length, ISO_8859_1);
		}
	}

	/**
	 * Sends a line to the server, adding CRLF.
	 * CR and LF inside the line are replaced by spaces, so a line
	 * is always sent as a single IRC command.
	 *
	 * @param  line                the line to send
	 * @exception  IRCSocketException if the connection fails
	 */

	public synchronized void println(String line) throws IRCSocketException {
		if (out == null) {
			throw new IRCSocketException("Not connected.");
		}
		try {
			out.write(line.replace('\r', ' ').replace('\n', ' '));
			out.write("\r\n");
			out.flush();
		} catch (IOException e) {
			throw new IRCSocketException(e.getMessage());
		}
	}

	/**
	 * Gets the server attribute of the IRCSocket object
	 *
	 * @return    Servername value
	 */

	public String getServer() {
		return serverName;
	}

	/**
	 * Gets the port attribute of the IRCSocket object
	 *
	 * @return    Server port
	 */

	public int getPort() {
		return serverPort;
	}

	/**
	 * @return <code> true </code> if connected and not closed
	 */

	public boolean isConnected() {
		Socket s = socket;
		return s != null && !s.isClosed();
	}

	private final String serverName;
	private final int serverPort;
	private final ByteArrayOutputStream lineBuffer = new ByteArrayOutputStream(512);
	private volatile Socket socket;
	private InputStream in;
	private Writer out;

}
