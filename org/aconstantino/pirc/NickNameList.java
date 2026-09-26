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

import java.util.Vector;

/**
  * This object consists of maintaining the nicknames contained in a Channel,
  * methods to organize them and to maintain them well in way structured.
  *
  * @author Ademir Constantino Filho
  * @since pIRC 0.1
  * @version 0.1
  */

public class NickNameList extends Vector {

	/**
	 * Add a nickName to the NickNameList 
	 * @param nick The NickName String to be added
	 */

	public void addNickName(String nick) {
		this.addElement(nick);
	}

	/**
	 * Replace a nickName to the NickNameList 
	 * @param oldNickName The 'OLD' NickName String to be removed from vector
	 * @param newNickName The 'NEW' NickName String to be added to vector
	 */

	public void updateNickName(String oldNickName, String newNickName) {
		int index = this.indexOfNickName(oldNickName);
		if (index == -1) {
			return;
		}
		String old = (String) this.get(index);
		this.removeElementAt(index);
		this.addNickName(getStatus(old) + newNickName);
		this.organizeByStatus();
	}

	/**
	 * Remove a nickName from the NickList Vector.
	 * @param nick Nickname String to remove
	 */

	public void removeNickname(String nick) {
		int index = this.indexOfNickName(nick);
		if (index > -1) {
			this.removeElementAt(index);
		}
	}

	/**
	 * Checks if the nickname is in the list, ignoring the status (@ or +)
	 * @param nick Nickname String
	 * @return <code> true </code> if the nickname is in the list
	 */

	public boolean containsNickName(String nick) {
		return this.indexOfNickName(nick) > -1;
	}

	/**
	 * Returns the index of a nickname, ignoring the status (@ or +)
	 * @param nick Nickname String
	 * @return the index or -1 if not found
	 */

	public int indexOfNickName(String nick) {
		String clean = stripStatus(nick);
		for (int i = 0; i < this.size(); i++) {
			if (stripStatus((String) this.get(i)).equalsIgnoreCase(clean)) {
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

	/**
	 * <P> Organize all NickNames by Status </P>
	 * <br> 1.&nbsp;Operator; <br>&nbsp;2.Voice; <br>&nbsp;3.Normal User;
	 */

	public void organizeByStatus() {

		//{{{ ~ begins the organization
		/* creates temporary vectors to do the nicknames organization */
		Vector temporary, temporaryb, temporaryc, temporaryd;
		/* The Current Value */
		String curr;
		/* Ok, set an approximate value */
		temporary = new Vector(this.size());
		temporaryb = new Vector(this.size());
		temporaryc = new Vector(this.size());
		/* Parse values */
		for (int i = 0; i < (int) this.size(); i++) {
			curr = (String) this.get(i);
			if (curr.startsWith("@")) {
				temporary.addElement(curr);
			} else if (curr.startsWith("+")) {
				temporaryb.addElement(curr);
			} else {
				temporaryc.addElement(curr);
			}
		}
		/* resize vectors */
		temporary.setSize(temporary.size());
		temporaryb.setSize(temporaryb.size());
		temporaryc.setSize(temporaryc.size());
		temporaryd = new Vector(this.size());
		/* first operators then voicers and users */
		for (int i = 0; i < (int) temporary.size(); i++) {
			temporaryd.addElement((String) temporary.get(i));
		}
		for (int i = 0; i < (int) temporaryb.size(); i++) {
			temporaryd.addElement((String) temporaryb.get(i));
		}
		for (int i = 0; i < (int) temporaryc.size(); i++) {
			temporaryd.addElement((String) temporaryc.get(i));
		}
		this.removeAllElements();
		for (int i = 0; i < (int) temporaryd.size(); i++) {
			this.addElement(temporaryd.get(i));
		}
		// ~ ends the organization ;) }}}
	}
}
