/*
  *  pIRC - IRC Client written in Java.
  * 
  * ( Copyright (C) 2002 -  Ademir Constantino Filho )
  *
  * This program is free software; you can redistribute it and/or
  * modify it under the terms of the GNU General Public License
  * as published by the Free Software Foundation; either version 2
  * of the License, or (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with this program; if not, write to the Free Software
  * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
  *
  */

package org.aconstantino.pirc;

import static java.nio.charset.StandardCharsets.US_ASCII;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

/**
  *Basic Identd Server for connecting to IRC network.
  *Answers one ident request (RFC 1413) and closes.
  *@author Ademir Constantino Filho
  *@since pIRC 0.1
  */

public final class Identd extends Thread {

	/**
	 * The Ident Server constructor
	 * @param system System (default java)
	 * @param username the username
	 */

	public Identd(String system, String username) {
		super("pIRC-identd");
		this.system = Objects.requireNonNull(system, "system");
		this.username = Objects.requireNonNull(username, "username");
		setDaemon(true);
	}

	/**
	 * Waits for the ident request of the IRC server, answers it and closes
	 * the ident server. Use <code>start()</code> to run it in background.
	 */

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(PORT);
			if (closed) {
				return;
			}
			socket = serverSocket.accept();
			BufferedReader in =
				new BufferedReader(
					new InputStreamReader(socket.getInputStream(), US_ASCII));
			String request = in.readLine();
			if (request != null) {
				Writer out = new OutputStreamWriter(socket.getOutputStream(), US_ASCII);
				out.write(request.trim() + " : USERID : " + system + " : " + username + "\r\n");
				out.flush();
			}
		} catch (IOException e) {
			// port 113 needs special privileges on most systems
			if (!closed) {
				System.err.println("Identd: " + e.getMessage());
			}
		} finally {
			close();
		}
	}

	/**
	 * Close the identserver
	 */

	public void close() {
		closed = true;
		closeQuietly(socket);
		closeQuietly(serverSocket);
	}

	private static void closeQuietly(AutoCloseable c) {
		if (c != null) {
			try {
				c.close();
			} catch (Exception e) {
				// nothing to do
			}
		}
	}

	private static final int PORT = 113;
	private final String system, username;
	private volatile ServerSocket serverSocket;
	private volatile Socket socket;
	private volatile boolean closed;

}
