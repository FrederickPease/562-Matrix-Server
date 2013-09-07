package com.rs.game.player.dialogues;

import com.rs.game.Graphics;
import com.rs.game.player.Skills;

public final class LevelUp extends Dialogue {
	
	public static final int[] SKILL_ICON = {
		100000000, 400000000, 200000000, 450000000, 250000000, 500000000,
		300000000, 1100000000, 1250000000, 1300000000, 1050000000, 1200000000,
		800000000, 1000000000, 900000000, 650000000, 600000000, 700000000,
		1400000000, 1450000000, 850000000, 1500000000, 1600000000, 1650000000, 0
	};
	
	public static final int[] SKILL_LEVEL_UP_MUSIC_EFFECTS = { 37, 37, 37, 37,
		37, -1, 37, -1, 39, -1, -1, -1, -1, -1, 53, -1, -1, -1, -1, -1, -1,
		-1, -1, 300, 417 };
	
	/*public static final int[] SKILL_FLASH = {
		1, 4, 2, 64, 8, 16, 32, 32768, 131072, 2048, 16384, 65536, 1024, 8192, 4096, 256, 128,
		512, 524288, 1048576, 262144, 2097152, 4194304, 8388608, 0, 0
	};*/
	private int skill;
	@Override
	public void start() {
	    skill = (Integer) parameters[0];
	    int level = player.getSkills().getLevelForXp(skill);
		player.getTemporaryAttributtes().put("leveledUp", skill);
		player.getTemporaryAttributtes().put("leveledUp["+skill+"]", Boolean.TRUE);
		player.setNextGraphics(new Graphics(199));
		if (level == 99)
			player.setNextGraphics(new Graphics(1765));
		player.getInterfaceManager().sendChatBoxInterface(740);
		String name = Skills.SKILL_NAME[skill];
		player.getPackets().sendIComponentText(740, 0, "Congratulations, you have just advanced a"+ (name.startsWith("A") ? "n": "")+" "+ name + " level!");
		player.getPackets().sendIComponentText(740, 1, "You have now reached level " + player.getSkills().getLevel(skill) + ".");
		player.getPackets().sendGameMessage("You've just advanced a"+ (name.startsWith("A") ? "n": "")+" "+ name + " level! You have reached level " + player.getSkills().getLevel(skill) + ".");
		player.getPackets().sendConfig(1179, SKILL_ICON[skill]);
		int musicEffect = SKILL_LEVEL_UP_MUSIC_EFFECTS[skill];
		if (musicEffect != -1)
			player.getPackets().sendMusic(musicEffect);
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		end();
	}
	
	@Override
	public void finish() {
		player.getPackets().sendConfig(1179, SKILL_ICON[skill]); //removes random flash
	}
}
