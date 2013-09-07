package com.rs.net.decoders.handlers;

import com.rs.game.item.Item;
import com.rs.game.player.Player;

public class ButtonHandler {

	public static void sendRemove(Player player, int slotId) {
		if (slotId >= 15)
			return;
		player.stopAll(false, false);
		Item item = player.getEquipment().getItem(slotId);
		if (item == null
				|| !player.getInventory().addItem(item.getId(),
						item.getAmount()))
			return;
		player.getEquipment().getItems().set(slotId, null);
		player.getEquipment().refresh(slotId);
		player.getAppearence().generateAppearenceData();
		/*if (Runecrafting.isTiara(item.getId()))
			player.getPackets().sendConfig(491, 0);*/
		if (slotId == 3)
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
		

}
}
