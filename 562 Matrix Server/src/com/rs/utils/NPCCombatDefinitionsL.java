package com.rs.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.npc.combat.NPCCombatDefinitions;

public final class NPCCombatDefinitionsL {

	private final static HashMap<Integer, NPCCombatDefinitions> npcCombatDefinitions = new HashMap<Integer, NPCCombatDefinitions>();
	private final static NPCCombatDefinitions DEFAULT_DEFINITION = new NPCCombatDefinitions(
			50, 6184, 6183, 6182, 5, 1, 60, 16, NPCCombatDefinitions.MELEE, -1, -1,
			NPCCombatDefinitions.PASSIVE);
	private static final String PACKED_PATH = "data/npcs/packedCombatDefinitions.ncd";

	public static void init() {
		if (new File(PACKED_PATH).exists())
			loadPackedNPCCombatDefinitions();
		else
			loadUnpackedNPCCombatDefinitions();
	}

	public static NPCCombatDefinitions getNPCCombatDefinitions(int npcId) {
		NPCCombatDefinitions def = npcCombatDefinitions.get(npcId);
		if (def == null || NPCDefinitions.getNPCDefinitions(npcId) == null)
			return DEFAULT_DEFINITION;
		return def;
	}

	private static void loadUnpackedNPCCombatDefinitions() {
		int count = 0;
		Logger.log("NPCCombatDefinitionsL", "Packing npc combat definitions...");
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					PACKED_PATH));
			BufferedReader in = new BufferedReader(new FileReader(
					"data/npcs/unpackedCombatDefinitionsList.txt"));
			while (true) {
				String line = in.readLine();
				count++;
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				String[] splitedLine = line.split(" - ", 2);
				if (splitedLine.length != 2)
					throw new RuntimeException(
							"Invalid NPC Combat Definitions line: " + count
									+ ", " + line);
				int npcId = Integer.parseInt(splitedLine[0]);
				String[] splitedLine2 = splitedLine[1].split(" ", 12);
				if (splitedLine2.length != 12)
					throw new RuntimeException(
							"Invalid NPC Combat Definitions line: " + count
									+ ", " + line);
				int hitpoints = Integer.parseInt(splitedLine2[0]);
				int attackAnim = Integer.parseInt(splitedLine2[1]);
				int defenceAnim = Integer.parseInt(splitedLine2[2]);
				int deathAnim = Integer.parseInt(splitedLine2[3]);
				int attackDelay = Integer.parseInt(splitedLine2[4]);
				int deathDelay = Integer.parseInt(splitedLine2[5]);
				int respawnDelay = Integer.parseInt(splitedLine2[6]);
				int maxHit = Integer.parseInt(splitedLine2[7]);
				int attackStyle;
				if (splitedLine2[8].equalsIgnoreCase("MELEE"))
					attackStyle = NPCCombatDefinitions.MELEE;
				else if (splitedLine2[8].equalsIgnoreCase("RANGE"))
					attackStyle = NPCCombatDefinitions.RANGE;
				else if (splitedLine2[8].equalsIgnoreCase("MAGE"))
					attackStyle = NPCCombatDefinitions.MAGE;
				else if (splitedLine2[8].equalsIgnoreCase("SPECIAL"))
					attackStyle = NPCCombatDefinitions.SPECIAL;
				else if (splitedLine2[8].equalsIgnoreCase("SPECIAL2"))
					attackStyle = NPCCombatDefinitions.SPECIAL2;
				else
					throw new RuntimeException(
							"Invalid NPC Combat Definitions line: " + line);
				int attackGfx = Integer.parseInt(splitedLine2[9]);
				int attackProjectile = Integer.parseInt(splitedLine2[10]);
				int agressivenessType;
				if (splitedLine2[11].equalsIgnoreCase("PASSIVE"))
					agressivenessType = NPCCombatDefinitions.PASSIVE;
				else if (splitedLine2[11].equalsIgnoreCase("AGRESSIVE"))
					agressivenessType = NPCCombatDefinitions.AGRESSIVE;
				else
					throw new RuntimeException(
							"Invalid NPC Combat Definitions line: " + line);
				out.writeShort(npcId);
				out.writeShort(hitpoints);
				out.writeShort(attackAnim);
				out.writeShort(defenceAnim);
				out.writeShort(deathAnim);
				out.writeByte(attackDelay);
				out.writeByte(deathDelay);
				out.writeInt(respawnDelay);
				out.writeShort(maxHit);
				out.writeByte(attackStyle);
				out.writeShort(attackGfx);
				out.writeShort(attackProjectile);
				out.writeByte(agressivenessType);
				npcCombatDefinitions.put(npcId, 
						new NPCCombatDefinitions(hitpoints,
								attackAnim, defenceAnim, deathAnim,
								attackDelay, deathDelay, respawnDelay,
								maxHit, attackStyle, attackGfx, attackProjectile,
								agressivenessType));
			}
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void loadPackedNPCCombatDefinitions() {
		try {
			DataInputStream in = new DataInputStream(new FileInputStream("data/npcs/packedCombatDefinitions.ncd"));
			while (in.available() != 0) {
				int npcId = in.readUnsignedShort();
				int hitpoints = in.readUnsignedShort();
				int attackAnim = in.readUnsignedShort();
				int defenceAnim = in.readUnsignedShort();
				int deathAnim = in.readUnsignedShort();
				int attackDelay = in.readUnsignedByte();
				int deathDelay = in.readUnsignedByte();
				int respawnDelay = in.readInt();
				int maxHit = in.readUnsignedShort();
				int attackStyle = in.readUnsignedByte();
				int attackGfx = in.readUnsignedShort();
				int attackProjectile = in.readUnsignedShort();
				int agressivenessType = in.readUnsignedByte();
				npcCombatDefinitions.put(npcId, 
						new NPCCombatDefinitions(hitpoints,
								attackAnim, defenceAnim, deathAnim,
								attackDelay, deathDelay, respawnDelay,
								maxHit, attackStyle, attackGfx, attackProjectile,
								agressivenessType));
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private NPCCombatDefinitionsL() {
		
	}
	
}

