package com.rs.game.player.skills;

import com.rs.game.player.Player;

public final class SkillExecutor {

	private Player player;
	private Skill skill;
	private int skillDelay;
	
	public SkillExecutor(Player player) {
		this.player = player;
	}
	
	public void process() {
		if(skill != null) {
			if(player.isDead()) {
				forceStop();
			}else if(!skill.process(player)) {
				forceStop();
			}
		}
		if(skillDelay > 0) {
			skillDelay--;
			return;
		}
		if(skill == null)
			return;
		int delay = skill.processWithDelay(player);
		if(delay == -1) {
			forceStop();
			return;
		}
		skillDelay += delay;
	}
	
	public boolean setSkill(Skill skill) {
		forceStop();
		if(!skill.start(player))
			return false;
		this.skill = skill;
		return true;
	}
	
	public void forceStop() {
		if(skill == null)
			return;
		skill.stop(player);
		skill = null;
	}

	public int getSkillDelay() {
		return skillDelay;
	}

	public void setSkillDelay(int skillDelay) {
		this.skillDelay = skillDelay;
	}
	
	public boolean hasSkillWorking() {
		return skill != null;
	}
}
