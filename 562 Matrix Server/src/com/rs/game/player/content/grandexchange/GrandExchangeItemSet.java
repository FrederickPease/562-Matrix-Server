package com.rs.game.player.content.grandexchange;

import com.rs.game.player.Player;

/**
 * @author Taylor Moon
 * 
 * @version Elixrr 2 | update 3
 */
public class GrandExchangeItemSet {

	/**
	 * Adds the set to a player's inventory
	 * 
	 * @param player
	 *            The player
	 * @param set
	 *            The set to add
	 */
	public GrandExchangeItemSet(Player player, GEItemSet set) {
		if (player.getInventory().hasFreeSlots()) {
			player.getBank().addItem(set.getSetID(), 1, true);
			player.getPackets().sendGameMessage("Your set has added to your bank.");
			return;
		}
		player.getInventory().addItem(set.getSetID(), 1);
	}

	/**
	 * 
	 * @author Taylor Moon
	 * 
	 * @version Elixrr 2 | update 3
	 */
	public enum GEItemSet {

		NULL(0, new int[] {});

		/**
		 * The ID of the item set
		 */
		private int id;

		/**
		 * The ID of the containing item
		 */
		private int[] itemId;

		/**
		 * Constructs the item set
		 * 
		 * @param id
		 *            The ID of the set
		 * @param itemId
		 *            the ID of the item
		 */
		GEItemSet(int id, int[] itemId) {
			this.id = id;
		}

		/**
		 * Returns the set ID
		 * 
		 * @return The ID of this set
		 */
		public int getSetID() {
			return id;
		}

		/**
		 * Returns an array of the items inside
		 * 
		 * @return The items inside the set
		 */
		public int[] getItemsInside() {
			return itemId;
		}
	}
}