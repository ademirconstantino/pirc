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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * The irclib socket										
 * @@author Ziegfried Duhbe
 * @@version 0.2b
 */

public class IRCSocket {

	/**
	 * Sets the timeOut attribute of the IRCSocket object
	 * The time out for the socket, default value is 30000 ( 30 seconds )
	 * @@param  setTimeOutSocket  The new timeOut value
	 * @@see #setTimeOut
	 * @@see #setServer
	 * @@see #setPort
	 */

	public void setTimeOut(int setTimeOutSocket) {
		if (setTimeOutSocket != 0) {
			this.timeOut = setTimeOutSocket;
			timeOutConfigured = true;
		} else {
			throw new NullPointerException();
		}
		this.checkConfig();
	}

	/**
	 * Sets the serverPort attribute of the IRCSocket object
	 * ( 6667 most popular port )
	 * @@param  newPortValue Server Port
	 * @@see #setTimeOut
	 * @@see #setServer
	 */

	public void setPort(int newPortValue) {
		if (newPortValue != 0) {
			this.serverPort = newPortValue;
			serverPortConfigured = true;
		} else {
			throw new NullPointerException();
		}
		this.checkConfig();
	}

	/**
	 * Sets the serverName attribute of the IRCSocket object
	 * @@param  newServerValue Server addr String
	 * @@see #setTimeOut
	 * @@see #setPort
	 */

	public void setServer(String newServerValue) {
		if (newServerValue != null) {
			this.serverName = newServerValue;
			serverNameConfigured = true;
		} else {
			throw new NullPointerException();
		}
		this.checkConfig();
	}

	/**
	 * Constructor for the IRCSocket object
	 *
	 * @@param  setServerName  Constructor for the IRCSocket with serverName only
	 */

	public IRCSocket(String setServerName) {
		if (setServerName != null) {
			this.serverName = setServerName;
			serverNameConfigured = true;
		} else {
			throw new NullPointerException();
		}
	}

	/**
	 * Constructor for the IRCSocket object
	 *
	 * @@param  setServerName  setServerName to connect
	 * @@param  setServerPort  setServerPort to connect ( 6667 most popular port )
	 * @@param  setTimeOut     setTimeOut value, the socket will be closed if servers does not respond in timeOut value
	 */

	public IRCSocket(String setServerName, int setServerPort, int setTimeOut) {

		if (!isConfigured) {
			if (setServerName == null) {
				throw new NullPointerException();
			}
			if (setServerName != null) {
				this.serverName = setServerName;
				serverNameConfigured = true;
			}
			if (serverPort == 0) {
				throw new NullPointerException();
			}
			if (setServerPort != 0) {
				this.serverPort = setServerPort;
				serverPortConfigured = true;
			}
			if (setTimeOut != 0) {
				this.timeOut = setTimeOut;
				timeOutConfigured = true;
			} else {
				throw new NullPointerException();
			}
			this.checkConfig();
		}
	}

	public IRCSocket() {
	}

	/**
	 * Constructor for the IRCSocket object
	 *
	 * @@param  setServerName  setServerName to connect
	 * @@param  setServerPort  setServerPort to connect ( 6667 most popular port )
	 */

	public IRCSocket(String setServerName, int setServerPort) {
		if (!isConfigured) {
			if (setServerName == null) {
				throw new NullPointerException();
			}
			if (setServerName != null) {
				this.serverName = setServerName;
				serverNameConfigured = true;
			}
			if (serverPort == 0) {
				throw new NullPointerException();
			}
			if (setServerPort != 0) {
				this.serverPort = setServerPort;
				serverPortConfigured = true;
			}
			this.checkConfig();
		}
	}

	/**
	 * Connect to the IRC Server 
	 *
	 * @@exception  IRCSocketException
	 */
	public void connect() throws IRCSocketException {
		if (isCon) {
			throw new IRCSocketException(" Already connected.");
		}
		if (timeOut < 0) {
			throw new IRCSocketException(" TimeOut can´t be negative value.");
		}
		if (isClosed) {
			throw new IRCSocketException(" Socket is closed.");
		}
		if (!isConfigured) {
			throw new IRCSocketException(" IRCSocket not configured.");
		}
		if (isConfigured && !isClosed && timeOut > 0) {
			connect(1);
		}
	}

	private void connect(int value) throws IRCSocketException {
		if (value == 0) {
			close();
		}
		try {
			this.socket = new Socket(serverName, serverPort);
			this.socket.setSoTimeout(timeOut);
		} catch (IOException e) {
			throw new IRCSocketException(e.getMessage());
		}

		try {
			this.setUpSocketStream();
		} catch (IOException e) {
			throw new IRCSocketException(e.getMessage());
		}
	}

	/**
	 *  Close the IRCSocket
	 *
	 * @@exception  IRCSocketException  Description of the Exception
	 */

	public void close() throws IRCSocketException {
		try {
			socket.close();
			isCon = false;
			isClosed = true;
		} catch (IOException e) {
			throw new IRCSocketException(e.getMessage());
		}
	}

	/**
	 * Close IRCSocket sending a Quit message to IRC Server
	 *
	 * @@param  quitMessage             Quit Message ( Bye! )
	 * @@exception  IRCSocketException 
	 */

	public void close(String quitMessage) throws IRCSocketException {
		try {
			this.writeln("QUIT :" + quitMessage);
			socket.close();
			isCon = false;
			isClosed = true;
		} catch (IOException e) {
			throw new IRCSocketException(e.getMessage());
		}
	}

	private void checkConfig() {
		if (serverNameConfigured
			&& serverPortConfigured
			&& timeOutConfigured
			&& !isCon) {
			isConfigured = true;
		} else {
			isConfigured = false;
		}
	}

	private void setUpSocketStream() throws IOException {
		sockReader =
			new BufferedReader(new InputStreamReader(socket.getInputStream()));
		sockWriter = new PrintStream(socket.getOutputStream(), true);
	}

	/**
	 * Gets the inputData attribute of the IRCSocket object
	 *
	 * @@return                         The inputData value
	 * @@exception  IRCSocketException  Description of the Exception
	 */

	public String getInputData() throws IRCSocketException {
		try {
			return sockReader.readLine();
		} catch (IOException e) {
			throw new IRCSocketException(e.getMessage());
		}
	}

	/**
	 * Write String to server
	 *
	 * @@param  strWrite                String to send ( msg ? )
	 * @@exception  IRCSocketException
	 */
	public void write(String strWrite) throws IRCSocketException {
		sockWriter.print(strWrite);
	}

	/**
	 * Write String to server with CRLF (´\r´ ( carriage returns ) & ´\n´ ( line feed ))
	 *
	 * @@param  strWrite                Description of the Parameter
	 * @@exception  IRCSocketException  Description of the Exception
	 */

	public void writeln(String strWrite) throws IRCSocketException {
		sockWriter.print(strWrite + "\r\n");
	}

	/**
	 * Print String to server with CRLF (´\r´ ( carriage returns ) & ´\n´ ( line feed ))
	 *
	 * @@param  strPrint            	Str to print
	 * @@exception  IRCSocketException
	 */

	public void print(String strPrint) throws IRCSocketException {
		write(strPrint);
	}

	/**
	 * Print String to server with CRLF (´\r´ ( carriage returns ) & ´\n´ ( line feed ))
	 *
	 * @@param  strPrint                Str to print
	 * @@exception  IRCSocketException
	 */

	public void println(String strPrint) throws IRCSocketException {
		writeln(strPrint);
	}

	/**
	 * Gets the server attribute of the IRCSocket object
	 *
	 * @@return    Servername value
	 */

	public String getServer() {
		return serverName;
	}

	/**
	 * Gets the port attribute of the IRCSocket object
	 *
	 * @@return    Server port
	 */

	public int getPort() {
		return serverPort;
	}

	/**
	 * Gets the iRCSocketState attribute of the IRCSocket object
	 *
	 * @@return    The ircsocket state
	 */

	public String getIRCSocketState() {
		return (
			new String(
				"Connected to server: "
					+ serverName
					+ "\n On Port: "
					+ serverPort));
	}

	/**
	 * Gets the IRCSocket currtent state
	 * if returns <code> true </code> it´s connected
	 * if returns <code> false </code> it´s not connected
	 * @@return    The connected <code> boolean </code> value
	 */

	public boolean isConnected() {
		return isCon;
	}

	/**
	 * Gets the IRCSocket configured value
	 * if returns <code> true </code> the ircsocket it´s configured
	 * if returns <code> false </code> the ircsocket it´s not configured
	 * @@return    true if is configured false if not configured
	 */

	public boolean isConfiguredState() {
		return isConfigured;
	}

	/**
	 * Returns the isClosed.
	 * @@return boolean
	 */
	public boolean isClosed() {
		return isClosed;
	}

	/**
	 * Returns the isCon.
	 * @@return boolean
	 */
	public boolean isCon() {
		return isCon;
	}

	/**
	 * Returns the isConfigured.
	 * @@return boolean
	 */
	public boolean isConfigured() {
		return isConfigured;
	}

	/**
	 * Returns the serverName.
	 * @@return String
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * Returns the serverNameConfigured.
	 * @@return boolean
	 */
	public boolean isServerNameConfigured() {
		return serverNameConfigured;
	}

	/**
	 * Returns the serverPort.
	 * @@return int
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * Returns the serverPortConfigured.
	 * @@return boolean
	 */
	public boolean isServerPortConfigured() {
		return serverPortConfigured;
	}

	/**
	 * Returns the socket.
	 * @@return Socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * Returns the socketConfigured.
	 * @@return boolean
	 */
	public boolean isSocketConfigured() {
		return socketConfigured;
	}

	/**
	 * Returns the sockReader.
	 * @@return BufferedReader
	 */
	public BufferedReader getSockReader() {
		return sockReader;
	}

	/**
	 * Returns the sockWriter.
	 * @@return PrintWriter
	 */
	public PrintStream getSockWriter() {
		return sockWriter;
	}

	/**
	 * Returns the timeOut.
	 * @@return int
	 */
	public int getTimeOut() {
		return timeOut;
	}

	/**
	 * Returns the timeOutConfigured.
	 * @@return boolean
	 */
	public boolean isTimeOutConfigured() {
		return timeOutConfigured;
	}

	private boolean isCon = false;
	private boolean isClosed = false;
	private BufferedReader sockReader;
	private PrintStream sockWriter;
	private boolean socketConfigured,
		serverNameConfigured,
		serverPortConfigured,
		timeOutConfigured;
	private Socket socket;
	private String serverName;
	private int serverPort;
	private boolean isConfigured = false;
	private int timeOut = 30000;

}@


1.1
log
@ouxi
@
text
@@

