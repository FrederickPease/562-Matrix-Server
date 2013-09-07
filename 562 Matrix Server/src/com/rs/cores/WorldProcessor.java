package com.rs.cores;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTasksManager;

public final class WorldProcessor extends PriorityProcessor {

	public WorldProcessor(int priority) {
		super(priority);
	}
	
	@Override
	public long process() {
		long currentTime = System.currentTimeMillis();
		WorldTasksManager.processTasks();
		for(Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished())
				continue;
			if(currentTime - player.getPacketsDecoderPing() > Settings.MAX_PACKETS_DECODER_PING_DELAY) {
				player.getSession().getChannel().close();
				continue;
			}
			player.processEntity();
			player.processMovement();
			player.processReceivedHits();
		}
		for(NPC npc : World.getNPCs()) {
			if (npc == null || npc.hasFinished())
				continue;
			npc.processEntity();
		}
		/*
		 * for (NPC npc : World.getNPCs())
			npc.processEntity();
		for(int i = 0; i < NPCConstants.COW.length; i++) {
			if(Utils.random(23) == 4 && i > Utils.random(1, NPCConstants.COW_MINIMUM) && i <Utils.random(30, COW_MAXIMUM)) {
				NPCConstants.COW[i].submitForceChat(new ForceChat("Moo"));
			}
		}
		 */
		for(Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished())
				continue;
			player.getPackets().sendLocalPlayersUpdate();
			player.getPackets().sendLocalNPCsUpdate();
		}
		for(Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished())
				continue;
			player.resetMasks();
		}
		for(NPC npc : World.getNPCs()) {
			if (npc == null || npc.hasFinished())
				continue;
			npc.resetMasks();
		}
		return Settings.WORLD_CYCLE_TIME + currentTime - System.currentTimeMillis();
	}
	
}
