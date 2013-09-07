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

import com.rs.game.World;
import com.rs.game.WorldTile;

public final class ApeatollNPCS {

	public static final void init() {
		if(!new File("data/spawns/packedapeSpawns").exists())
			packNPCS();
	}
	
	
	private static final void packNPCS() {
		Logger.log("ApeatollNPCS", "Packing npc spawns...");
		if(!new File("data/spawns/packedapeSpawns").mkdir())
			throw new RuntimeException("Couldn't create packedSpawns directory.");
		try {
			BufferedReader in = new BufferedReader(new FileReader("data/spawns/apeatoll.txt"));
			while(true) {
				String line = in.readLine();
				if(line == null)
					break;
				if(line.startsWith("//"))
					continue;
				String[] splitedLine = line.split(" - ", 2);
				if(splitedLine.length != 2)
					throw new RuntimeException("Invalid NPC Spawn line: "+line);
				int npcId = Integer.parseInt(splitedLine[0]);
				String[] splitedLine2 = splitedLine[1].split(" ", 5);
				if(splitedLine2.length != 3 && splitedLine2.length != 5)
					throw new RuntimeException("Invalid NPC Spawn line: "+line);
				WorldTile tile = new WorldTile(Integer.parseInt(splitedLine2[0]), Integer.parseInt(splitedLine2[1]), Integer.parseInt(splitedLine2[2]));
				int mapAreaNameHash = -1;
				boolean canBeAttackFromOutOfArea = true;
				if(splitedLine2.length == 5) {
					mapAreaNameHash = Utils.getNameHash(splitedLine2[3]);
					canBeAttackFromOutOfArea = Boolean.parseBoolean(splitedLine2[4]);
				}
				addNPCSpawn(npcId, tile.getRegionId(), tile, mapAreaNameHash, canBeAttackFromOutOfArea);
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static final void loadNPCSpawns(int regionId) {
		File file = new File("data/spawns/packedapeSpawns/"+ regionId +".ns");
		if(!file.exists())
			return;
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			while (in.available() != 0) {
				int npcId = in.readUnsignedShort();
				int plane = in.readUnsignedByte();
				int x = in.readUnsignedShort();
				int y = in.readUnsignedShort();
				boolean hashExtraInformation = in.readBoolean();
				int mapAreaNameHash = -1;
				boolean canBeAttackFromOutOfArea = true;
				if(hashExtraInformation) {
					mapAreaNameHash = in.readInt();
					canBeAttackFromOutOfArea = in.readBoolean();
				}
				World.spawnNPC(npcId, new WorldTile(x,y,plane), mapAreaNameHash, canBeAttackFromOutOfArea);
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static final void addNPCSpawn(int npcId, int regionId, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream("data/spawns/packedapeSpawns/"+ regionId +".ns", true));
			out.writeShort(npcId);
			out.writeByte(tile.getPlane());
			out.writeShort(tile.getX());
			out.writeShort(tile.getY());
			out.writeBoolean(mapAreaNameHash != -1);
			if(mapAreaNameHash != -1) {
				out.writeInt(mapAreaNameHash);
				out.writeBoolean(canBeAttackFromOutOfArea);
			}
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private ApeatollNPCS() {
	}
}