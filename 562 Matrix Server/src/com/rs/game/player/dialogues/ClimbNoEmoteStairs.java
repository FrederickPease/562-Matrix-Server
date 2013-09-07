package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;

public class ClimbNoEmoteStairs extends Dialogue {
	
	private WorldTile upTile;
	private WorldTile downTile;
	@Override
	public void start() {
		upTile = (WorldTile) parameters[0];
		downTile = (WorldTile) parameters[1];
		sendDialogue(SEND_3_LARGE_OPTIONS, "What would you like to do?", "Go up the stairs.", "Go down the stairs.", "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(interfaceId == SEND_3_LARGE_OPTIONS && componentId == 2)
			player.useStairs(-1, upTile, 0, 1);
		else if(interfaceId == SEND_3_LARGE_OPTIONS && componentId == 3)
			player.useStairs(-1, downTile, 0, 1);
		end();
	}

	@Override
	public void finish() {
		
	}

}
