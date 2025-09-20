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

//

package org.aconstantino.pirc;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PIRCServersReader extends DefaultHandler {

	public PIRCServersReader(String fName) {
		this.fName = fName;
		init();
	}

	private void init() {
		xmlFile = new File(fName);
		spf.setValidating(true);
		try {
			parser = spf.newSAXParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		try {
			parser.parse(xmlFile, this);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startElement(String a, String b, String c, Attributes d) {
		if (c.equals("server")) {
			int urlIndex = d.getIndex("url");
			String urlValue = d.getValue(urlIndex);
			int portIndex = d.getIndex("port");
			String portValue = d.getValue(portIndex);
			int groupIndex = d.getIndex("group");
			String groupValue = d.getValue(groupIndex);
			int nameIndex = d.getIndex("name");
			String nameValue = d.getValue(nameIndex);
			h.addElement(
				new String[] { groupValue, nameValue, urlValue, portValue, c });
		}
	}

	public Vector getH() {
		return h;
	}

	public File getXmlFile() {
		return xmlFile;
	}

	public void setXmlFile(File xmlFile) {
		this.xmlFile = xmlFile;
	}

	private String fName;
	private File xmlFile;
	private SAXParserFactory spf = SAXParserFactory.newInstance();
	private SAXParser parser;
	private Vector h = new Vector();

}
@


1.1
log
@ouxi
@
text
@@

