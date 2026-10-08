/**
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
  *@author Ademir Constantino Filho
  *@since pIRC 0.1
  */

public class Identd extends Thread {

	/**
	 * @return the version for this ident server
	 */

	public float getVersion() {
		return versionInfo;
	}

	/**
	 * Return the Connection State for this sever
	 * @return <code> true </code> if connected
	 * @return <code> false </code> if not connected
	 */

	public boolean getConnectedState() {
		return connected;
	}

	/**
	 * If the data was sent to server return <code> true </code>
	 * @return <code> true </code> if your ident data was sent to server
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
	 * @param systemValue System (default java)
	 * @param userNameValue the username
	 */

	public Identd(String systemValue, String usernameValue) {

		if (systemValue == null)
			throw new NullPointerException();
		if (usernameValue == null)
			throw new NullPointerException();

		wSent = false;
		this.system = systemValue;
		this.username = usernameValue;
		userId = "USERID";
		setDaemon(true);
	}

	/**
	 * Waits for the ident request of the IRC server, answers it and closes
	 * the ident server. Use <code>start()</code> to run it in background.
	 */

	public void run() {
		try {
			serversocket = new ServerSocket(port);
			socket = serversocket.accept();
			in =
				new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			out = new PrintStream(socket.getOutputStream(), true);
			connected = true;
			sr = in.readLine();
			if (sr != null) {
				String sr2 =
					sr.trim() + " : " + userId + " : " + system + " : " + username;
				wSent = true;
				out.print(sr2 + "\r\n");
			}
		} catch (IOException e) {
			// port 113 needs special privileges on most systems
			System.err.println("Identd: " + e.getMessage());
		} finally {
			close();
		}
	}

	/**
	 * Close the identserver
	 */

	public void close() {
		try {
			connected = false;
			if (out != null) {
				out.close();
			}
			if (socket != null) {
				socket.close();
			}
			if (serversocket != null) {
				serversocket.close();
			}
		} catch (IOException e) {
		}
	}

	/**
	 * Returns the system.
	 * @return String
	 */
	public String getSystem() {
		return system;
	}

	/**
	 * Returns the userId.
	 * @return String
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Returns the username.
	 * @return String
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns the versionInfo.
	 * @return float
	 */
	public float getVersionInfo() {
		return versionInfo;
	}

	/**
	 * Sets the system.
	 * @param system The system to set
	 */
	public void setSystem(String system) {
		this.system = system;
	}

	/**
	 * Sets the userId.
	 * @param userId The userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Sets the username.
	 * @param username The username to set
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
