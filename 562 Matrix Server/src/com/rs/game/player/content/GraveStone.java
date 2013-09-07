package com.rs.game.player.content;

import java.util.TimerTask;

import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

/**
 * @author Taylor Moon
 */
public class GraveStone {

	/** The gravestone npc */
	private int gravestone;

	/** The time at which the gravestone has left */
	private int time;

	/** The drops under the gravestone */
	private Item[] drops;

	/** The gravestone position */
	private WorldTile pos;

	/** The gravestone npc */
	private NPC gs;

	/** The player associated with this gravestone */
	private Player player;

	/**
	 * Called when a player dies
	 * 
	 * @param pos
	 *            the position of this gravestone
	 * @param drop
	 *            if the player should drop
	 * @param player
	 *            the player dying
	 */
	public void deploy(WorldTile pos, boolean drop, int gravestoneNpcType, Player player,
			Item... drops) {
		this.player = player;
		this.gravestone = gravestoneNpcType;
		gs = new NPC(gravestone, pos, 0, false);
		GravestoneTimer timer = new GravestoneTimer();
		gs.setCantInteract(false);
		gs.setLocked(true);
		World.addNPC(gs);
		player.getHintIconsManager().addHintIcon(gs, 0, -1, false);
		timer.run();
		this.drops = drops;
		if (drop) {
			for(Item items : drops)
				 World.addGroundItem(items, pos, player, true, time, true);
		}
	}

	/**
	 * Causes this gravestone to collapse
	 */
	public void collapse() {
		for (Item items : drops)
			World.removeGroundItem(player, new FloorItem(items, pos, player,
					true, false));
		gs.sendDeath(null);
		World.removeNPC(gs);
		gs = null;
		player.getHintIconsManager().removeAll();
	}

	/**
	 * 
	 * @author Taylor Moon
	 * 
	 * @since Dec 15, 2012
	 */
	class GravestoneTimer extends TimerTask {

		@Override
		public void run() {
			time--;
			refreshStatus();
		}

		/**
		 * Refreshes the interface and the interface configs to the
		 * corresponding gravestone time
		 */
		private void refreshStatus() {
			if (time == 0) {
				collapse();
				cancel();
				player.out("You were late to your gravestone and it collapsed.");
				return;
			} else {
				// TODO configs and interface
			}
		}

	}

}
