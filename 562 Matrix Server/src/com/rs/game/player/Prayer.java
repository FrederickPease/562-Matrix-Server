package com.rs.game.player;

import java.io.Serializable;

public class Prayer implements Serializable {

	private static final long serialVersionUID = -2082861520556582824L;
	
	private final static int[][] prayerLvls = {
		// normal prayer book
				{ 1, 4, 7, 8, 9, 10, 13, 16, 19, 22, 25, 26, 27, 28, 31, 34, 35,
						37, 40, 43, 44, 45, 46, 49, 52, 60, 70 /*,65, 77*/},
				// ancient prayer book
				{ 50, 50, 52, 54, 56, 59, 62, 65, 68, 71, 74, 76, 78, 80, 82,
						84, 86, 89, 92, 95 } };
	
	private final static int[][] closePrayers = { // normal prayer book
		{ 0, 5, 13 }, // Skin prayers 0
		{ 1, 6, 14 }, // Strength prayers 1
		{ 2, 7, 15 }, // Attack prayers 2
		{ 3, 11, 20 }, // Range prayers 3
		{ 4, 12, 21, 28 }, // Magic prayers 4
		{ 8, 9, 26 }, // Restore prayers 5
		{ 10 }, // Protect item prayers 6
		{ 17, 18, 19 }, // Protect prayers 7
		{ 16 }, // Other protect prayers 8
		{ 22, 23, 24 }, // Other special prayers 9
		{ 25, 26 } // Other prayers 10
		};
	
	private final static int[] prayerSlotValues = { 1, 2, 4, 262144, 524288, 8,
		16, 32, 64, 128, 256, 1048576, 2097152, 512, 1024, 2048, 16777216,
		4096, 8192, 16384, 4194304, 8388608, 32768, 65536, 131072,
		33554432, /*134217728,*/ 67108864 /*268435456*/ };
	
	private transient Player player;
	private transient boolean[] onPrayers;
	private transient boolean usingQuickPrayer;
	private transient int onPrayersCount;
	private int prayerpoints;
	private boolean[] quickPrayers;
	private transient int drainDelay;
	
	public double getMageMultiplier() {
		if(onPrayersCount == 0)
			return 1.0;
		double value = 1.0;
		if (usingPrayer(4))
			value += 0.05;
		else if (usingPrayer(12))
			value += 0.10;
		else if (usingPrayer(21))
			value += 0.15;
		return value;
	}
	
	public double getRangeMultiplier() {
		if(onPrayersCount == 0)
			return 1.0;
		double value = 1.0;
		if (usingPrayer(3))
			value += 0.05;
		else if (usingPrayer(11))
			value += 0.10;
		else if (usingPrayer(20))
			value += 0.15;
		return value;
	}
	
	public double getAttackMultiplier() {
		if(onPrayersCount == 0)
			return 1.0;
		double value = 1.0;
		if (usingPrayer(2))
			value += 0.05;
		else if (usingPrayer(7))
			value += 0.10;
		else if (usingPrayer(15))
			value += 0.15;
		else if (usingPrayer(25))
			value += 0.15;
		else if (usingPrayer(26))
			value += 0.20;
		return value;
	}
	
	public double getStrengthMultiplier() {
		if(onPrayersCount == 0)
			return 1.0;
		double value = 1.0;
		if (usingPrayer(1))
			value += 0.05;
		else if (usingPrayer(6))
			value += 0.10;
		else if (usingPrayer(14))
			value += 0.15;
		else if (usingPrayer(25))
			value += 0.18;
		else if (usingPrayer(26))
			value += 0.23;
		return value;
	}
	
	public double getDefenceMultiplier() {
		if(onPrayersCount == 0)
			return 1.0;
		double value = 1.0;
		if (usingPrayer(0))
			value += 0.05;
		else if (usingPrayer(5))
			value += 0.10;
		else if (usingPrayer(13))
			value += 0.15;
		else if (usingPrayer(25))
			value += 0.20;
		else if (usingPrayer(26))
			value += 0.25;
		return value;
	}
	
	public int getPrayerHeadIcon() {
		if(onPrayersCount == 0)
			return -1;
		int value = -1;
		if (usingPrayer(16))
			value += 8;
		if (usingPrayer(17))
			value += 3;
		else if (usingPrayer(18))
			value += 2;
		else if (usingPrayer(19))
			value += 1;
		else if (usingPrayer(22))
			value += 4;
		else if (usingPrayer(23))
			value += 6;
		else if (usingPrayer(24))
			value += 5;
		return value;
	}
	
	public void switchSettingQuickPrayer() {
		usingQuickPrayer = !usingQuickPrayer;
		player.getPackets().sendButtonConfig(181, usingQuickPrayer ? 1 : 0);//activates quick choose
	    player.getPackets().sendUnlockIComponentOptionSlots(271, usingQuickPrayer ? 7 : 6, 0, 26, 0);	
		if (usingQuickPrayer) //switchs tab to prayer
			player.getPackets().sendButtonConfig(168, 6);
	}
	
	public void switchQuickPrayers() {
		if (!checkPrayer())
			return;
		if (hasPrayersOn())
			closeAllPrayers();
		else {
			boolean hasOn = false;
			int index = 0;
			for (boolean prayer : quickPrayers) {
				if (prayer) {
					if(usePrayer(index))
						hasOn = true;
				}
				index++;
			}
			if(hasOn)  {
				player.getPackets().sendButtonConfig(182, 1);
				recalculatePrayer();
			}
		}
	}
	
	private void closePrayers(int[]... prayers) {
		for (int[] prayer : prayers)
			for (int prayerId : prayer)
				if (usingQuickPrayer)
					quickPrayers[prayerId] = false;
				else {
					if(onPrayers[prayerId])
						onPrayersCount--;
					onPrayers[prayerId] = false;
				}
	}
	
	public void switchPrayer(int prayerId) {
		if (!usingQuickPrayer)
			if (!checkPrayer())
				return;
		usePrayer(prayerId);
		recalculatePrayer();
	}
	
	private boolean usePrayer(int prayerId) {
		if (prayerId < 0 || prayerId >= prayerLvls[0].length)
			return false;
		if (player.getSkills().getLevelForXp(5) < prayerLvls[0][prayerId]) {
			player.getPackets().sendGameMessage("You need a prayer level of at least " + prayerLvls[0][prayerId]+ " to use this prayer.");
			return false;
		}
		if (!usingQuickPrayer) {
			if (onPrayers[prayerId]) {
				onPrayers[prayerId] = false;
				onPrayersCount--;
				player.getAppearence().generateAppearenceData();
				return true;
			}
		} else {
			if (quickPrayers[prayerId]) {
				quickPrayers[prayerId] = false;
				return true;
			}
		}
		boolean needAppearenceGenerate = false;
		switch (prayerId) {
		case 0:
		case 5:
		case 13:
			closePrayers(closePrayers[0],
					closePrayers[10]);
			break;
		case 1:
		case 6:
		case 14:
			closePrayers(closePrayers[1],
					closePrayers[3],
					closePrayers[4],
					closePrayers[10]);
			break;
		case 2:
		case 7:
		case 15:
			closePrayers(closePrayers[2],
					closePrayers[3],
					closePrayers[4],
					closePrayers[10]);
			break;
		case 3:
		case 11:
		case 20:
			closePrayers(closePrayers[1],
					closePrayers[2],
					closePrayers[3],
					closePrayers[10]);
			break;
		case 4:
		case 12:
		case 21:
			closePrayers(closePrayers[1],
					closePrayers[2],
					closePrayers[4],
					closePrayers[10]);
			break;
		case 8:
		case 9:
		case 27:
			closePrayers(closePrayers[5]);
			break;
		case 10:
			closePrayers(closePrayers[6]);
			break;
		case 17:
		case 18:
		case 19:
			closePrayers(closePrayers[7],
					closePrayers[9]);
			needAppearenceGenerate = true;
			break;
		case 16:
			closePrayers(closePrayers[8],
					closePrayers[9]);
			needAppearenceGenerate = true;
			break;
		case 22:
		case 23:
		case 24:
			closePrayers(closePrayers[7],
					closePrayers[8],
					closePrayers[9]);
			needAppearenceGenerate = true;
			break;
		case 25:
		case 26:
			closePrayers(closePrayers[0],
					closePrayers[1],
					closePrayers[2],
					closePrayers[3],
					closePrayers[4],
					closePrayers[10]);
			break;
		case 28:
			closePrayers(closePrayers[0],
					closePrayers[1],
					closePrayers[2],
					closePrayers[4],
					closePrayers[10]);
			break;
		default:
			return false;
		}
		if (!usingQuickPrayer) {
			onPrayers[prayerId] = true;
			onPrayersCount++;
			if(needAppearenceGenerate)
				player.getAppearence().generateAppearenceData();
		} else
			quickPrayers[prayerId] = true;
		return true;
	}

	
	public void processPrayer() {
		if(!hasPrayersOn())
			return;
		if(drainDelay <= 0) {
			player.getSkills().drainPrayer(1);
			drainDelay = 5;
		}
		drainDelay--;
		
		if(!checkPrayer())
			closeAllPrayers();
	}
	public int getOnPrayersCount() {
		return onPrayersCount;
	}
	
	public void closeAllPrayers() {
		onPrayers = new boolean[29];
		onPrayersCount = 0;
		player.getPackets().sendButtonConfig(182, 0);
		player.getPackets().sendConfig(/*ancientcurses ? 1582 : */1395, 0);
		player.getAppearence().generateAppearenceData();
	}
	
	public boolean hasPrayersOn() {
		/*for (boolean prayer : onPrayers[0])
			if (prayer)
				return true;
		return false;*/
		return onPrayersCount > 0;
	}
	
	private boolean checkPrayer() {
		if (player.getSkills().getLevel(5) <= 0) {
			player.getPackets().sendGameMessage("Please recharge your prayer at the Lumbridge Church.");
			return false;
		}
		return true;
	}
	
	private void recalculatePrayer() {
		int value = 0;
		int index = 0;
		for (boolean prayer : (!usingQuickPrayer ? onPrayers : quickPrayers)) {
			if (prayer)
				value += /*ancientcurses ? Math.pow(2, index) : */prayerSlotValues[index];
			index++;
		}
		player.getPackets().sendConfig(/*ancientcurses ? (usingQuickPrayer ? 1584 : 1582) : */(usingQuickPrayer ? 1397 : 1395), value);
	}
	
	public void init() {
		player.getPackets().sendButtonConfig(181, usingQuickPrayer ? 1 : 0);
		player.getPackets().sendConfig(1086, 0);
		player.getPackets().sendConfig(1584, 0);
		player.getPackets().sendUnlockIComponentOptionSlots(271, usingQuickPrayer ? 7 : 6, 0, 26, 0);
	}
	
//	public void setPrayerBook(boolean ancientcurses) {
//		closeAllPrayers();
//		this.ancientcurses = ancientcurses;
//		player.getInterfaceManager().sendPrayerBook();
//		init();
//	}
	
	public Prayer() {
		quickPrayers = new boolean[29];
	}
	
	public void setPlayer(Player player) {
		this.player = player;
		onPrayers = new boolean[29];
		drainDelay = 5;
	}
	
	public boolean usingPrayer(int prayerId) {
		return onPrayers[prayerId];
	}
	
	public boolean isUsingQuickPrayer() {
		return usingQuickPrayer;
	}

	public int getPrayerpoints() {
		return prayerpoints;
	}

	public void setPrayerpoints(int prayerpoints) {
		this.prayerpoints = prayerpoints;
	}

	public void refreshPrayerPoints() {
		player.getPackets().sendConfig(2382, prayerpoints);
	}

	public void drainPrayerOnHalf() {
		if (prayerpoints > 0) {
			prayerpoints = prayerpoints / 2;
			refreshPrayerPoints();
		}
	}
	
	
	public boolean hasFullPrayerpoints() {
		return getPrayerpoints() >= player.getSkills().getLevelForXp(Skills.PRAYER);
	}

	/*public void drainPrayer(int amount) {
		if ((prayerpoints - amount) >= 0)
			prayerpoints -= amount;
		else
			prayerpoints = 0;
		refreshPrayerPoints();
	}*/

	public void restorePrayer(int amount) {
		int maxPrayer = player.getSkills().getLevelForXp(Skills.PRAYER);
		if ((prayerpoints + amount) <= maxPrayer)
			prayerpoints += amount;
		else
			prayerpoints = maxPrayer;
		refreshPrayerPoints();
	}

	public void reset() {
		closeAllPrayers();
		prayerpoints = player.getSkills().getLevelForXp(Skills.PRAYER);
		refreshPrayerPoints();
	}
	
	private static final double[] prayerData = { 1.2, 1.2, 1.2, 1.2, 1.2, 1.2,
		0.6, 0.6, 0.6, 3.6, 1.8, 0.6, 0.6, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3,
		0.3, 0.3, 0.3, 1.2, 0.6, 0.18, 0.24, 0.15, 0.2, 0.18 };

	public void drainPrayer(int amount) {
		double drainRate = 0;
		for(int i = 0; i < prayerData.length; i++) {
			if (onPrayers[i]) {
				drainRate += prayerData[i];
			}
		}
		if (drainRate != 0) 
			drainRate = drainRate * (1 + 0.035);
		if (prayerpoints - drainRate < 0) 
			prayerpoints = 0;
		else
			prayerpoints -= drainRate;
		refreshPrayerPoints();
	}
	
}
