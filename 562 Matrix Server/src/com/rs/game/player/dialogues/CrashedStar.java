package com.rs.game.player.dialogues;

import com.rs.game.player.Skills;
import com.rs.Settings;

public final class CrashedStar extends Dialogue {

	@Override
	public void start() {
		sendDialogue(SEND_2_TEXT_CHAT,
				"Congratulations, you were the first to find this star!",
				"You receive "+(player.getSkills().getLevel(Skills.MINING) * 75) * Settings.XP_RATE+" Mining XP as a reward."); //Legit RuneScape message
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			end();
		}

	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}