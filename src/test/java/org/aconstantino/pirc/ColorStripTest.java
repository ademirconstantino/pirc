package org.aconstantino.pirc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ColorStripTest {

	@Test
	void stripsColors() {
		assertEquals("vermelho e azul", ColorStrip.stripColor("\u000304vermelho\u0003 e \u00032,12azul"));
	}

	@Test
	void stripsStyles() {
		assertEquals(
			"negrito sublinhado itálico reverso normal",
			ColorStrip.stripColor(
				"\u0002negrito\u0002 \u001fsublinhado\u001f \u001ditálico\u001d \u0016reverso\u000f normal"));
	}

	@Test
	void keepsPlainText() {
		assertEquals("olá, 123", ColorStrip.stripColor("olá, 123"));
	}
}
