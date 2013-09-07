package com.rs.game.player.dialogues;

import com.rs.game.minigames.pest.PestControl;

public class LanderDialouge extends Dialogue {

	@Override
	public void start() {
		sendDialogue(SEND_1_TEXT_CHAT, "", "Are you sure you would like to leave the lander?");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendDialogue(SEND_2_OPTIONS, "Yes, get me out of here!", "No, I want to stay.");
		} else if (stage == 0) {
			if (componentId == 1) {
				player.setNextWorldTile(PestControl.OUTSIDE_AREA);
				player.getControlerManager().forceStop();
			}
			end();
		}
	}

	@Override
	public void finish() {

	}

}
