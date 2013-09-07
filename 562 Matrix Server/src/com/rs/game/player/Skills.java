package com.rs.game.player;

import java.io.Serializable;
import java.util.Date;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.utils.Utils;

public final class Skills implements Serializable {

	private static final long serialVersionUID = -7086829989489745985L;
	
	public static final double MAXIMUM_EXP = 200000000;
	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2, HITPOINTS = 3, RANGE = 4, PRAYER = 5,
			MAGIC = 6, COOKING = 7, WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11,
			CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15, AGILITY = 16, THIEVING = 17, SLAYER = 18,
			FARMING = 19, RUNECRAFTING = 20, CONSTRUCTION = 21, HUNTER = 22, SUMMONING = 23;
	
	public static final String[] SKILL_NAME = { "Attack", "Defence",
		"Strength", "Hitpoints", "Range", "Prayer", "Magic", "Cooking",
		"Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
		"Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer",
		"Farming", "Runecrafting", "Construction", "Hunter", "Summoning" };
	
	public short level[];
	public double xp[];
	//public int level[] = new int[SKILL_COUNT];
	//public double xp[] = new double[SKILL_COUNT];
	public static final int SKILL_COUNT = 24;
	
	private transient Player player;
	
	
	public Skills() {
		level = new short[24];
		xp = new double[24];
		for(int i = 0; i < level.length; i++) {
			level[i] = 1;
			xp[i] = 0;
		}
		level[3] = 10;
		xp[3] = 1184;
	}
	
/*	public Skills() {
		for(int i = 0; i < SKILL_COUNT; i++) {
			level[i] = 1;
			xp[i] = 0;
		}
		level[3] = 10;
		xp[3] = 1184;
	}*/
	public void restoreSkills() {
		for(int skill = 0; skill < level.length; skill++) {
			level[skill] = (short) getLevelForXp(skill);
			refresh(skill);
		}
	}
	
	public void drainPrayer(int amount) {
		int level = getLevel(PRAYER);
		set(PRAYER, level - amount > 0 ? level - amount : 0);
	}
	
	
	public void restorePrayer(int amount) {
		int level = getLevel(PRAYER);
		int realLevel = getLevelForXp(PRAYER);
		set(PRAYER, level + amount >= realLevel ? realLevel : level + amount);
		//player.getPackets().sendSkillLevels();
		player.getAppearence().generateAppearenceData();
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public int getLevel(int skill) {
		return level[skill];
	}
	
	public double getXp(int skill) {
		return xp[skill];
	}
	
	public int getCombatLevel() {
		int attack = getLevelForXp(0);
		int defence = getLevelForXp(1);
		int strength = getLevelForXp(2);
		int hp = getLevelForXp(3);
		int prayer = getLevelForXp(5);
		int ranged = getLevelForXp(4);
		int magic = getLevelForXp(6);
		int combatLevel = 3;
		combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.25) + 1; 
		double melee = (attack + strength) * 0.325; 
		double ranger = Math.floor(ranged * 1.5) * 0.325; 
		double mage = Math.floor(magic * 1.5) * 0.325; 
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		return combatLevel;
	}
	
	public void set(int skill, int newLevel) {
		level[skill] = (short) newLevel;
		refresh(skill);
	}
	
	public int getCombatLevelWithSummoning() {
		return getCombatLevel()+getSummoningCombatLevel();
	}
	
	public int getSummoningCombatLevel() {
		return getLevelForXp(Skills.SUMMONING) / 8;
	}
	
    public static int getXPForLevel(int level) {
        int points = 0;
        int output = 0;
        for (int lvl = 1; lvl <= level; lvl++) {
            points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
            if (lvl >= level) {
                return output;
            }
            output = (int) Math.floor(points / 4);
       }
       return 0;
    }
    

	/*public int getLevelForXp(int skill) {
	/*	Player player = this.player;
		if (player.RequestAssist().IsAssisting == true) {
			if (player.RequestAssist().AssistedBy != null) {
				Player AssistedBy = player.RequestAssist().AssistedBy;
				if (AssistedBy.RequestAssist().assistSkills[skill] == true)
					player = AssistedBy;
			}
		}*/
		/*
		double exp = player.getSkills().xp[skill];
		int points = 0;
		int output = 0;
		for(int lvl = 1; lvl < 100; lvl++) {
			points += Math.floor((double)lvl + 300.0 * Math.pow(2.0, (double)lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if((output - 1) >= exp) {
				return lvl;
			}
		}
		return 99;
	}*/
	public int getLevelForXp(int skill) {
		double exp = xp[skill];
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= 100; lvl++) {
			points += Math.floor(lvl + 300.0
					* Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return 99;
	}
	public void init() {
		for(int skill = 0; skill < level.length; skill++)
			refresh(skill);
	}
    
	public void refresh(int skill) {
		player.getPackets().sendSkillLevel(skill);
	}
	
	public void addXp(int skill, double exp) {
		exp *= Settings.XP_RATE;
		int oldLevel = getLevelForXp(skill);
		xp[skill] += exp;
		if(xp[skill] > MAXIMUM_EXP) {
			xp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForXp(skill);
		int levelDiff = newLevel - oldLevel;
		if(newLevel > oldLevel) {
			level[skill] += levelDiff;
			player.getDialogueManager().startDialogue("LevelUp", skill);
			if(skill == SUMMONING || (skill >= ATTACK && skill <= MAGIC))
				player.getAppearence().generateAppearenceData();
		}
		if(player.MDates[skill] == null) {
			if(xp[skill] == 200000000){
				World.sendWorldMessage("<col=800000><img=5> News: "+
			            Utils.formatPlayerNameForDisplay(player.getUsername())
			              + "has just reached 200M XP in " + level[skill] + "!", false);
				player.MDates[skill] = getDate();
			}
		}
		refresh(skill);
	}
	
	public String getDate() {
		Date date = new Date();
		String time =  date.getHours() + ":" + (date.getMinutes() < 10 ? "0" : "") + "" + date.getMinutes();
		String totalDate = date.getDate() + "/" + (date.getMonth() + 1) + "/" + (date.getYear() + 1900) + " " + time;
		return totalDate;
	}


	public int drainLevel(int skill, int drain) {
		int drainLeft = drain - level[skill];
		if (drainLeft < 0) {
			drainLeft = 0;
		}
		level[skill] -= drain;
		if (level[skill] < 0) {
			level[skill] = 0;
		}
		refresh(skill);
		return drainLeft;
	}

	public void drainSummoning(int amt) {
		int level = getLevel(Skills.SUMMONING);
		if (level == 0)
			return;
		set(Skills.SUMMONING, amt > level ? 0 : level - amt);
	}

	public void setXp(int skill, double exp) {
		xp[skill] = exp;
		refresh(skill);
	}

}
