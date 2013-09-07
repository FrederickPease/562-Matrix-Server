package com.rs.game.player.skills;

import com.rs.game.player.Player;

public abstract class Skill {

	public abstract boolean start(Player player);
	
	public abstract boolean process(Player player);
	
	public abstract int processWithDelay(Player player);
	
	public abstract void stop(Player player);
	
	protected final void setSkillDelay(Player player, int delay) {
		player.getSkillExecutor().setSkillDelay(delay);
	}
}
