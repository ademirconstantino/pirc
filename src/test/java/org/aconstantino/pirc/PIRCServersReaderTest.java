package org.aconstantino.pirc;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PIRCServersReaderTest {

	@TempDir
	Path dir;

	private Path write(String content) throws Exception {
		Path file = dir.resolve("servers.xml");
		Files.writeString(file, content, UTF_8);
		return file;
	}

	@Test
	void readsServersAndGroups() throws Exception {
		Path file = write(
			"<?xml version='1.0'?>\n<servers>\n"
				+ "<server url=\"irc.a.org\" port=\"6667\" group=\"brasnet\" name=\"A\"/>\n"
				+ "<server url=\"irc.b.org\" port=\"7000\" group=\"libera\" name=\"B\"/>\n"
				+ "<server url=\"irc.c.org\" port=\"6668\" group=\"brasnet\" name=\"C\"/>\n"
				+ "</servers>\n");
		PIRCServersReader reader = new PIRCServersReader(file);
		assertEquals(
			List.of(
				new Server("brasnet", "A", "irc.a.org", 6667),
				new Server("libera", "B", "irc.b.org", 7000),
				new Server("brasnet", "C", "irc.c.org", 6668)),
			reader.getServers());
		assertEquals(List.of("brasnet", "libera"), reader.getGroups());
	}

	@Test
	void ignoresServersWithInvalidPort() throws Exception {
		Path file = write(
			"<servers><server url=\"x\" port=\"abc\" group=\"g\" name=\"bad\"/>"
				+ "<server url=\"y\" port=\"6667\" group=\"g\" name=\"good\"/></servers>");
		assertEquals(
			List.of(new Server("g", "good", "y", 6667)),
			new PIRCServersReader(file).getServers());
	}

	@Test
	void savesAndReadsBack() throws Exception {
		Path file = write("<servers></servers>");
		PIRCServersReader reader = new PIRCServersReader(file);
		Server old = new Server("g", "old", "irc.old.org", 6667);
		reader.add(old);
		reader.add(new Server("rede & cia", "São \"Paulo\" <SP>", "irc.sp.org", 6697));
		reader.replace(old, new Server("g", "new", "irc.new.org", 6668));
		reader.save();

		assertEquals(reader.getServers(), new PIRCServersReader(file).getServers());
		assertEquals("new", reader.getServers().get(0).name());

		reader.remove(reader.getServers().get(0));
		reader.save();
		assertEquals(1, new PIRCServersReader(file).getServers().size());
	}

	@Test
	void doesNotLoadExternalEntities() throws Exception {
		Path secret = dir.resolve("secret.txt");
		Files.writeString(secret, "segredo");
		Path file = write(
			"<?xml version='1.0'?><!DOCTYPE servers [<!ENTITY xxe SYSTEM \""
				+ secret.toUri()
				+ "\">]><servers><server url=\"&xxe;\" port=\"6667\" group=\"g\" name=\"n\"/></servers>");
		assertTrue(new PIRCServersReader(file).getServers().isEmpty());
	}

	@Test
	void missingFileGivesEmptyList() {
		assertTrue(new PIRCServersReader(dir.resolve("none.xml")).getServers().isEmpty());
	}
}
