package com.rs.game.player.controlers;

import com.rs.game.Animation;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class FFA extends Controler {


	@Override
	public void start() {
		player.setCanPvp(true);
		sendInterfaces();
	}
	
	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendTab(
				player.getInterfaceManager().hasRezizableScreen() ? 5 : 1,
				789);
	}

	@Override
	public boolean logout() {
		return false; // so doesnt remove script
	}

	@Override
	public boolean login() {
		sendInterfaces();
		return false; // so doesnt remove script
	}

	@Override
	public boolean sendDeath() {
		player.addStopDelay(7);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage("Oh dear, you have died.");
				} else if (loop == 3) {
					player.getEquipment().init();
					player.getInventory().init();
					player.reset();
					player.setCanPvp(false);
					teleportPlayer(player);
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					player.getPackets().sendMusic(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}


	@Override
	public void forceClose() {
		player.setCanPvp(false);
	}

	@Override
	public void moved() {
			if (!isInClanSafe(player)) {
				player.setCanPvp(false);
			} else 
				player.setCanPvp(true);
	}
	
	private void teleportPlayer(Player player) {
			player.setNextWorldTile(new WorldTile(3000, 9676, 0));
	}

	public static boolean isInClanSafe(Player player) {
		return player.getX() >= 2756 && player.getY() >= 5512
				&& player.getX() <= 2878 && player.getY() <= 5630;
	}
}