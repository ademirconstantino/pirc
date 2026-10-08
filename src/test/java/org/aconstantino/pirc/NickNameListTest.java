package org.aconstantino.pirc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class NickNameListTest {

	private static List<String> toList(NickNameList list) {
		List<String> result = new ArrayList<>();
		list.forEach(result::add);
		return result;
	}

	private static NickNameList of(String... nicks) {
		NickNameList list = new NickNameList();
		for (String nick : nicks) {
			list.addNickName(nick);
		}
		return list;
	}

	@Test
	void organizesOperatorsThenVoicesThenUsersKeepingOrder() {
		NickNameList list = of("zeca", "+voz", "@op2", "ana", "@op1", "+voz2");
		list.organizeByStatus();
		assertEquals(List.of("@op2", "@op1", "+voz", "+voz2", "zeca", "ana"), toList(list));
	}

	@Test
	void findsNicknamesIgnoringStatusAndCase() {
		NickNameList list = of("@Op", "+Voz", "user");
		assertTrue(list.containsNickName("op"));
		assertTrue(list.containsNickName("@VOZ"));
		assertEquals(2, list.indexOfNickName("USER"));
		assertFalse(list.containsNickName("other"));
	}

	@Test
	void updateKeepsStatus() {
		NickNameList list = of("@old", "user");
		list.updateNickName("old", "new");
		assertEquals(List.of("@new", "user"), toList(list));
	}

	@Test
	void updateOfUnknownNicknameDoesNothing() {
		NickNameList list = of("user");
		list.updateNickName("other", "new");
		assertEquals(List.of("user"), toList(list));
	}

	@Test
	void removesIgnoringStatus() {
		NickNameList list = of("@op", "user");
		list.removeNickname("op");
		assertEquals(List.of("user"), toList(list));
		assertEquals(1, list.size());
		assertEquals("user", list.get(0));
	}

	@Test
	void stripsStatus() {
		assertEquals("op", NickNameList.stripStatus("@op"));
		assertEquals("voz", NickNameList.stripStatus("+voz"));
		assertEquals("user", NickNameList.stripStatus("user"));
	}
}
