package com.rs.game.player.skills;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public final class Woodcutting extends Skill {

	public static enum TreeDefinitions {
		Normal_Tree(1, 25, 1511, 20, 4, 1341, 8, 0),
		Oak_Tree(15, 37.5, 1521, 30, 4, 5554, 15, 15),
		Willow_Tree(30,  67.5, 1519, 60, 4, 7399, 51, 15),
		Maple_Tree(45,  100, 1517, 83, 16, 7400, 72, 10),
		Yew_Tree(60,  175, 1515, 120, 17, 7402, 94, 10),
		Ivy(68,  332.5, -1, 120, 17, 46319, 58, 10),
		Magic_Tree(75,  250, 1513, 150, 21, 7401, 121, 10);
		private int level;
		private double xp;
		private int logsId;
		private int logBaseTime;
		private int logRandomTime;
		private int stumpId;
		private int respawnDelay;
		private int randomLifeProbability;
		
		private TreeDefinitions(int level, double xp, int logsId, int logBaseTime, int logRandomTime, int stumpId, int respawnDelay, int randomLifeProbability) {
			this.level = level;
			this.xp = xp;
			this.logsId = logsId;
			this.logBaseTime = logBaseTime;
			this.logRandomTime = logRandomTime;
			this.stumpId = stumpId;
			this.respawnDelay = respawnDelay;
			this.randomLifeProbability = randomLifeProbability;
		}
		
		public int getLevel() {
			return level;
		}
		public double getXp() {
			return xp;
		}

		public int getLogsId() {
			return logsId;
		}

		public int getLogBaseTime() {
			return logBaseTime;
		}

		public int getLogRandomTime() {
			return logRandomTime;
		}

		public int getStumpId() {
			return stumpId;
		}

		public int getRespawnDelay() {
			return respawnDelay;
		}

		public int getRandomLifeProbability() {
			return randomLifeProbability;
		}
	}
	
	private WorldObject tree;
	private TreeDefinitions definitions;
	
	private int emoteId;
	private int axeTime;
	
	public Woodcutting(WorldObject tree, TreeDefinitions definitions) {
		this.tree = tree;
		this.definitions = definitions;
	}
	
	@Override
	public boolean start(Player player) {
		if(!checkAll(player))
			return false;
		player.getPackets().sendGameMessage("You swing your hatchet at the "+(TreeDefinitions.Ivy == definitions ? "ivy" : "tree")+".");
		//setWoodcuttingTime(player);
		setSkillDelay(player, getWoodcuttingDelay(player));
		return true;
	}

	private int getWoodcuttingDelay(Player player) {
		int wcTimer = definitions.getLogBaseTime() - player.getSkills().getLevel(8) - Utils.getRandom(axeTime);
		if(wcTimer < 1+definitions.getLogRandomTime())
			wcTimer = 1 + Utils.getRandom(definitions.getLogRandomTime());
		return wcTimer;
	}
	private boolean checkAll(Player player) {
		if(!hasAxe(player)) {
			player.getPackets().sendGameMessage("You need a hatchet to chop down this tree.");
			return false;
		}
		if(!setAxe(player)) {
			player.getPackets().sendGameMessage("You dont have the required level to use that axe.");
			return false;
		}
		if(!hasWoodcuttingLevel(player))
			return false;
		if (!player.getInventory().hasFreeSlots()){
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			return false;
		}
		return true;
	}
	private boolean hasWoodcuttingLevel(Player player) {
		if(definitions.getLevel() > player.getSkills().getLevel(8)) {
			player.getPackets().sendGameMessage( "You need a woodcutting level of " + definitions.getLevel() + " to chop down this tree.");
			return false;
		}
		return true;
	}
	private boolean setAxe(Player player) {
		int level = player.getSkills().getLevel(8);
		int weaponId = player.getEquipment().getWeaponId();
		if(weaponId != -1) {
		switch(weaponId) {
		case 6739: //dragon axe
			if(level >= 61) {
				emoteId = 2846; 
				axeTime = 13;
				return true;
			}
		break;
		case 1359: //rune axe
			if(level >= 41) {
				emoteId = 867;
				axeTime = 10;
				return true;
			}
		break;
		case 1357: //adam axe
			if(level >= 31) {
				emoteId = 869;
				axeTime = 7;
				return true;
			}
		break;
		case 1355: //mit axe
			if(level >= 21) {
				emoteId = 871;
				axeTime = 5;
				return true;
			}
		break;
		case 1361: //black axe
			if(level >= 11) {
				emoteId = 873;
				axeTime = 4;
				return true;
			}
		break;
		case 1353: //steel axe
			if(level >= 6) {
				emoteId = 875;
				axeTime = 3;
				return true;
			}
		break;
		case 1349: //iron axe
			emoteId = 877;
			axeTime = 2;
		return true;
		case 1351: //bronze axe
			emoteId = 879;
			axeTime = 1;
		return true;
		case 13661: //Inferno adze
			if(level >= 61) {
				emoteId = 10251;
				axeTime = 13;
				return true;
			}
		break;
		}
		}
		if(player.getInventory().containsOneItem(6739)) {
			if(level >= 61) {
				emoteId = 2846;
				axeTime = 13;
				return true;
			}
		}
		if(player.getInventory().containsOneItem(1359)) {
			if(level >= 41) {
				emoteId = 867;
				axeTime = 10;
				return true;
			}
		}
		if(player.getInventory().containsOneItem(1357)) {
			if(level >= 31) {
				emoteId = 869;
				axeTime = 7;
				return true;
			}
		}
		if(player.getInventory().containsOneItem(1355)) {
			if(level >= 21) {
				emoteId = 871;
				axeTime = 5;
				return true;
			}
		}
		if(player.getInventory().containsOneItem(1361)) {
			if(level >= 11) {
				emoteId = 873;
				axeTime = 4;
				return true;
			}
		}
		if(player.getInventory().containsOneItem(1353)) {
			if(level >= 6) {
				emoteId = 875;
				axeTime = 3;
				return true;
			}
		}
		if(player.getInventory().containsOneItem(1349)) {
			emoteId = 877;
			axeTime = 2;
			return true;
		}
		if(player.getInventory().containsOneItem(1351)) {
			emoteId = 879;
			axeTime = 1;
			return true;
		}
		if(player.getInventory().containsOneItem(13661)) {
			if(level >= 61) {
				emoteId = 10251;
				axeTime = 13;
				return true;
			}
		}
		return false;
		
	}
	
	private boolean hasAxe(Player player) {
		if(player.getInventory().containsOneItem(1351, 1349, 1353, 1355, 1357, 1361, 1359, 6739, 13661))
			return true;
		int weaponId = player.getEquipment().getWeaponId();
		if(weaponId == -1)
			return false;
		switch(weaponId) {
		case 1351://Bronze Axe
		case 1349://Iron Axe
		case 1353://Steel Axe
		case 1361://Black Axe
		case 1355://Mithril Axe
		case 1357://Adamant Axe
		case 1359://Rune Axe
		case 6739://Dragon Axe
		case 13661: //Inferno adze
		return true;
		default:
			return false;
		}
		
	}
	


	@Override
	public boolean process(Player player) {
		player.setNextAnimation(new Animation(emoteId));
		return checkTree(player);
	}
	
	@Override
	public int processWithDelay(Player player) {
		addLog(player);
		if(Utils.getRandom(definitions.getRandomLifeProbability()) == 0) {
			World.spawnTemporaryObject(new WorldObject(definitions.getStumpId(), tree.getType(), tree.getRotation(), tree.getX(), tree.getY(), tree.getPlane()), definitions.respawnDelay*600);
			player.setNextAnimation(new Animation(-1));
			return -1;
		}
		if (!player.getInventory().hasFreeSlots()){
			player.setNextAnimation(new Animation(-1));
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			return -1;
		}
		return getWoodcuttingDelay(player);
	}
	
	private void addLog(Player player) {
		double xpBoost = 1.0;
		if(player.getEquipment().getChestId() == 10939)
			xpBoost += 0.8;
		if(player.getEquipment().getLegsId() == 10940)
			xpBoost += 0.6;
		if(player.getEquipment().getHatId() == 10941)
			xpBoost += 0.4;
		if(player.getEquipment().getBootsId() == 10933)
			xpBoost += 0.2;
		player.getSkills().addXp(8, definitions.getXp()*xpBoost);
		player.getInventory().addItem(definitions.getLogsId(), 1);
		if(definitions == TreeDefinitions.Ivy) {
			player.getPackets().sendGameMessage("You succesfully cut an ivy vine.", true);
			//todo gfx
		}else{
			String logName = ItemDefinitions.getItemDefinitions(definitions.getLogsId()).getName().toLowerCase();
			player.getPackets().sendGameMessage("You get some "+logName+ ".", true);
			//todo infernal adze
		}
	}

	
	private boolean checkTree(Player player) {
		return World.getRegion(tree.getRegionId()).containsObject(tree.getId(), tree);
	}

	@Override
	public void stop(Player player) {
		setSkillDelay(player, 3); //prevents from wc,att,wc,att without delay XD
	}

}
