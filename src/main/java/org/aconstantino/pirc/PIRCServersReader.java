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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Reads and saves the servers list:
 * &lt;server url="..." port="6667" group="..." name="..."/&gt;
 */
public final class PIRCServersReader {

	/**
	 * Reads the servers of the xml file. If the file can not be read
	 * the list starts empty and the error is printed.
	 * @param xmlFile the servers file
	 */
	public PIRCServersReader(Path xmlFile) {
		this.xmlFile = xmlFile;
		try (InputStream in = Files.newInputStream(xmlFile)) {
			newParserFactory().newSAXParser().parse(in, new DefaultHandler() {
				@Override
				public void startElement(
					String uri,
					String localName,
					String qName,
					Attributes attributes) {
					if (qName.equals("server")) {
						addServer(attributes);
					}
				}
			});
		} catch (IOException | SAXException | ParserConfigurationException e) {
			System.err.println("Could not read " + xmlFile + ": " + e.getMessage());
		}
	}

	private static SAXParserFactory newParserFactory()
		throws ParserConfigurationException, SAXException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setValidating(false);
		/* the file is local, but there is no reason to load external entities */
		spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		spf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		return spf;
	}

	private void addServer(Attributes attributes) {
		String port = attributes.getValue("port");
		try {
			servers.add(
				new Server(
					value(attributes, "group"),
					value(attributes, "name"),
					value(attributes, "url"),
					Integer.parseInt(port == null ? "" : port.trim())));
		} catch (IllegalArgumentException e) {
			System.err.println(
				"Ignoring server " + attributes.getValue("name") + ": " + e.getMessage());
		}
	}

	private static String value(Attributes attributes, String name) {
		String value = attributes.getValue(name);
		return value == null ? "" : value;
	}

	/**
	 * @return the servers, in the file order
	 */
	public List<Server> getServers() {
		return List.copyOf(servers);
	}

	/**
	 * @return the groups of the servers, without repetition, in the file order
	 */
	public List<String> getGroups() {
		return servers.stream().map(Server::group).distinct().toList();
	}

	public void add(Server server) {
		servers.add(server);
	}

	/**
	 * Replaces a server, keeping its position in the list
	 */
	public void replace(Server oldServer, Server newServer) {
		int index = servers.indexOf(oldServer);
		if (index == -1) {
			servers.add(newServer);
		} else {
			servers.set(index, newServer);
		}
	}

	public void remove(Server server) {
		servers.remove(server);
	}

	/**
	 * Saves the servers (group, name, url, port) to the xml file
	 */
	public void save() throws IOException {
		try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(xmlFile, UTF_8))) {
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<servers>");
			for (Server s : servers) {
				out.println(
					"<server url=\""
						+ escape(s.host())
						+ "\" port=\""
						+ s.port()
						+ "\" group=\""
						+ escape(s.group())
						+ "\" name=\""
						+ escape(s.name())
						+ "\"/>");
			}
			out.println("</servers>");
			if (out.checkError()) {
				throw new IOException("Error writing " + xmlFile);
			}
		}
	}

	private static String escape(String s) {
		return s
			.replace("&", "&amp;")
			.replace("\"", "&quot;")
			.replace("<", "&lt;")
			.replace(">", "&gt;");
	}

	public Path getXmlFile() {
		return xmlFile;
	}

	private final Path xmlFile;
	private final List<Server> servers = new ArrayList<>();

}
