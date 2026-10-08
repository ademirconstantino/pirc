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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
  * This object consists of maintaining the nicknames contained in a Channel,
  * methods to organize them and to maintain them well in way structured.
  * The nicknames keep their status prefix (@ for operators, + for voice).
  *
  * @author Ademir Constantino Filho
  * @since pIRC 0.1
  * @version 0.2
  */

public final class NickNameList implements Iterable<String> {

	/**
	 * Add a nickName to the NickNameList 
	 * @param nick The NickName String to be added
	 */

	public void addNickName(String nick) {
		nicks.add(nick);
	}

	/**
	 * Replace a nickName, keeping its status
	 * @param oldNickName The 'OLD' NickName String to be removed
	 * @param newNickName The 'NEW' NickName String to be added
	 */

	public void updateNickName(String oldNickName, String newNickName) {
		int index = indexOfNickName(oldNickName);
		if (index == -1) {
			return;
		}
		String old = nicks.remove(index);
		addNickName(getStatus(old) + newNickName);
		organizeByStatus();
	}

	/**
	 * Remove a nickName from the list.
	 * @param nick Nickname String to remove
	 */

	public void removeNickname(String nick) {
		int index = indexOfNickName(nick);
		if (index > -1) {
			nicks.remove(index);
		}
	}

	/**
	 * Checks if the nickname is in the list, ignoring the status (@ or +)
	 * @param nick Nickname String
	 * @return <code> true </code> if the nickname is in the list
	 */

	public boolean containsNickName(String nick) {
		return indexOfNickName(nick) > -1;
	}

	/**
	 * Returns the index of a nickname, ignoring the status (@ or +)
	 * @param nick Nickname String
	 * @return the index or -1 if not found
	 */

	public int indexOfNickName(String nick) {
		String clean = stripStatus(nick);
		for (int i = 0; i < nicks.size(); i++) {
			if (stripStatus(nicks.get(i)).equalsIgnoreCase(clean)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Removes the status (@ or +) from the nickname
	 * @param nick Nickname String
	 * @return the nickname without status
	 */

	public static String stripStatus(String nick) {
		return nick.substring(getStatus(nick).length());
	}

	private static String getStatus(String nick) {
		if (nick.startsWith("@") || nick.startsWith("+")) {
			return nick.substring(0, 1);
		}
		return "";
	}

	private static int statusOrder(String nick) {
		return switch (getStatus(nick)) {
			case "@" -> 0;
			case "+" -> 1;
			default -> 2;
		};
	}

	/**
	 * <P> Organize all NickNames by Status, keeping the order inside each status </P>
	 * <br> 1.&nbsp;Operator; <br>&nbsp;2.Voice; <br>&nbsp;3.Normal User;
	 */

	public void organizeByStatus() {
		nicks.sort(Comparator.comparingInt(NickNameList::statusOrder));
	}

	public int size() {
		return nicks.size();
	}

	public String get(int index) {
		return nicks.get(index);
	}

	@Override
	public Iterator<String> iterator() {
		return nicks.iterator();
	}

	@Override
	public String toString() {
		return nicks.toString();
	}

	private final List<String> nicks = new ArrayList<>();
}
