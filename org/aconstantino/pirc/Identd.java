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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
  *Basic Identd Server for connecting to IRC network
  *@@author Ademir Constantino Filho
  *@@since pIRC 0.1
  */

public class Identd extends Thread {

	/**
	 * @@return the version for this ident server
	 */

	public float getVersion() {
		return versionInfo;
	}

	/**
	 * Return the Connection State for this sever
	 * @@return <code> true </code> if connected
	 * @@return <code> false </code> if not connected
	 */

	public boolean getConnectedState() {
		return connected;
	}

	/**
	 * If the data was sent to server return <code> true </code>
	 * @@return <code> true </code> if your ident data was sent to server
	 */

	public boolean wasSent() {
		boolean toReturn;
		if (!connected && wSent) {
			toReturn = true;
		} else {
			toReturn = false;
		}

		return toReturn;
	}

	/**
	 * The Ident Server constructor
	 * @@param systemValue System (default java)
	 * @@param userNameValue the username
	 */

	public Identd(String systemValue, String usernameValue) {

		if (systemValue == null)
			throw new NullPointerException();
		if (usernameValue == null)
			throw new NullPointerException();

		wSent = false;
		this.system = systemValue;
		this.username = usernameValue;
	}

	/**
	 * Start the ident server
	 */

	public void start() {

		userId = "USERID";
		if (system == null) {
			system = "JAVA";
		}
		if (username == null) {
			username = "noUser";
		}
		try {
			if (!connected) {
				serversocket = new ServerSocket(port);
				socket = serversocket.accept();
				in =
					new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				out = new PrintStream(socket.getOutputStream());
				new Thread(this).start();
				connected = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		if (connected) {
			try {
				sr = in.readLine();
				if (sr != null) {
					String sr2 =
						sr + " : " + userId + " : " + system + " : " + username;
					wSent = true;
					out.println(sr2);
					close();

				}
			} catch (IOException e) {
				System.err.println(e);
			}
		}
		try {
			sleep(180000);
		} catch (InterruptedException e) {
			System.err.println(e);
		}
	}

	/**
	 * Close the identserver
	 */

	public void close() {
		try {
			if (connected) {
				out.close();
				connected = false;
				socket.close();
				serversocket.close();
				this.interrupt();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the system.
	 * @@return String
	 */
	public String getSystem() {
		return system;
	}

	/**
	 * Returns the userId.
	 * @@return String
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Returns the username.
	 * @@return String
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns the versionInfo.
	 * @@return float
	 */
	public float getVersionInfo() {
		return versionInfo;
	}

	/**
	 * Sets the system.
	 * @@param system The system to set
	 */
	public void setSystem(String system) {
		this.system = system;
	}

	/**
	 * Sets the userId.
	 * @@param userId The userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Sets the username.
	 * @@param username The username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	private final int port = 113;
	private String sr;
	private boolean connected;
	private String userId, system, username;
	private ServerSocket serversocket;
	private Socket socket;
	private BufferedReader in;
	private PrintStream out;
	private boolean wSent;
	private final float versionInfo = (float) 0.3;

}
@


1.1
log
@ouxi
@
text
@@

