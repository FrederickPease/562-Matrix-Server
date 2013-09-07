package com.rs.net.decoders.handlers;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Firemaking;
import com.rs.io.InputStream;

public class Buttonhandler2 {

	@SuppressWarnings("unused")
	public static void itemOnItem(final Player player, final InputStream stream) {
		final int interfaceIndex = stream.readShort();
		stream.readShort();
		int fromSlot = stream.readShort();
		int toSlot = stream.readShort();
		int itemUsed = stream.readShortLE128() & 0xFFFF;
		stream.readShort();
		int toInterfaceIndex = stream.readShortLE();
		int usedWith = stream.readShortLE() & 0xFFFF;
		if (interfaceIndex != 149 && toInterfaceIndex != 149) {
			return;
		}
		Item usedWith1 = player.getInventory().getItem(usedWith);
			Item itemUsed1 = player.getInventory().getItem(itemUsed);
			
			if (Firemaking.isFiremaking(player, itemUsed1, usedWith1))
				return;
			else
				//System.out.println("ITEM 1: " +itemUsed1+ " ITEM 2: " +usedWith1);
			System.out.println("ITEM 1 kes 2e5tek: " +itemUsed1+ " ITEM 2: " +usedWith1);

	}
}
