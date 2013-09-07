package com.rs.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.utils.Utils.EntityDirection;

public class NPCSpawning {

	/**
	 * Contains the custom npc spawning
	 */

	public static void spawnNPCS() {
		//Faladors bankers
		World.spawnNPC(494, new WorldTile(2945, 3366, 0), -1, false, "NORTH");
		World.spawnNPC(494, new WorldTile(2946, 3366, 0), -1, false, "NORTH");
		World.spawnNPC(494, new WorldTile(2947, 3366, 0), -1, false, "NORTH");
		World.spawnNPC(494, new WorldTile(2948, 3366, 0), -1, false, "NORTH");
		World.spawnNPC(494, new WorldTile(2949, 3366, 0), -1, false, "NORTH");
        //Varrocks banker
		World.spawnNPC(44, new WorldTile(3191, 3435, 0), -1, false, "WEST");
		World.spawnNPC(45, new WorldTile(3191, 3437, 0), -1, false, "WEST");
		World.spawnNPC(44, new WorldTile(3191, 3439, 0), -1, false, "WEST");
		World.spawnNPC(45, new WorldTile(3191, 3441, 0), -1, false, "WEST");
		World.spawnNPC(44, new WorldTile(3191, 3443, 0), -1, false, "WEST");
		
		World.spawnNPC(44, new WorldTile(3180, 3436, 0), -1, false, "EAST");
		World.spawnNPC(45, new WorldTile(3180, 3438, 0), -1, false, "EAST");
		World.spawnNPC(44, new WorldTile(3180, 3440, 0), -1, false, "EAST");
		World.spawnNPC(45, new WorldTile(3180, 3442, 0), -1, false, "EAST");
		World.spawnNPC(44, new WorldTile(3180, 3444, 0), -1, false, "EAST");
		
		//Lambridge guards
		World.spawnNPC(926, new WorldTile(3269, 3229, 0), -1, false, "EAST");
		World.spawnNPC(926, new WorldTile(3268, 3226, 0), -1, false, "EAST");
		World.spawnNPC(925, new WorldTile(3267, 3226, 0), -1, false, "WEST");
		World.spawnNPC(925, new WorldTile(3266, 3229, 0), -1, false, "WEST");
		
		 //summoning area NPC
		World.spawnNPC(4907, new WorldTile(3208, 3222, 2), -1, false, "south");
		World.spawnNPC(8083, new WorldTile(2954, 3370, 0), -1, false, "NORTH");
        World.spawnNPC(6970, new WorldTile(2207, 5345, 0), -1, true, true);//Pikkupstix (summoning shops) 
	}

	/**
	 * The NPC classes.
	 */
	private static final Map<Integer, Class<?>> CUSTOM_NPCS = new HashMap<Integer, Class<?>>();

	public static void npcSpawn() {
		int size = 0;
		boolean ignore = false;
		try {
			for (String string : FileUtilities
					.readFile("data/npcs/npcspawns.txt")) {
				if (string.startsWith("//") || string.equals("")) {
					continue;
				}
				if (string.contains("/*")) {
					ignore = true;
					continue;
				}
				if (ignore) {
					if (string.contains("*/")) {
						ignore = false;
					}
					continue;
				}
				String[] spawn = string.split(" ");
				@SuppressWarnings("unused")
				int id = Integer.parseInt(spawn[0]), x = Integer
						.parseInt(spawn[1]), y = Integer.parseInt(spawn[2]), z = Integer
						.parseInt(spawn[3]), faceDir = Integer
						.parseInt(spawn[4]);
				NPC npc = null;
				Class<?> npcHandler = CUSTOM_NPCS.get(id);
				if (npcHandler == null) {
					npc = new NPC(id, new WorldTile(x, y, z), -1, true, false);
				} else {
					npc = (NPC) npcHandler.getConstructor(int.class)
							.newInstance(id);
				}
				if (npc != null) {
					WorldTile spawnLoc = new WorldTile(x, y, z);
					npc.setLocation(spawnLoc);
					World.spawnNPC(npc.getId(), spawnLoc, -1, true, false);
					size++;
				}
			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		}
		System.err.println("Loaded " + size + " custom npc spawns!");
	}

}