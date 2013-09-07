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

import com.rs.game.WorldTile;

public final class MapAreas {

	private final static HashMap<Integer, int[]> mapAreas = new HashMap<Integer, int[]>();
	
	public static final void init() {
		if(new File("data/map/packedMapAreas.ma").exists())
			loadPackedMapAreas();
		else
			loadUnpackedMapAreas();
	}
	
	public static final boolean isAtArea(String areaName, WorldTile tile) {
		return isAtArea(Utils.getNameHash(areaName), tile);
	}
	
	public static final boolean isAtArea(int areaNameHash, WorldTile tile) {
		int[] coordsList = mapAreas.get(areaNameHash);
		if(coordsList == null)
			return false;
		int index = 0;
		while(index < coordsList.length) {
			if(tile.getPlane() == coordsList[index]
					&& tile.getX() >= coordsList[index+1]
					&& tile.getX() <= coordsList[index+2]  
					&& tile.getY() >= coordsList[index+3]
					&& tile.getY() <= coordsList[index+4])
				return true;
			index += 5;
		}
		return false;
	}

	private static void loadUnpackedMapAreas() {
		Logger.log("MapAreas", "Packing map areas...");
		try {
			BufferedReader in = new BufferedReader(new FileReader("data/map/unpackedMapAreas.txt"));
			DataOutputStream out = new DataOutputStream(new FileOutputStream("data/map/packedMapAreas.ma"));
			while(true) {
				String line = in.readLine();
				if(line == null)
					break;
				if(line.startsWith("//"))
					continue;
				String[] splitedLine = line.split(" - ", 2);
				String areaName = splitedLine[0];
				String[] splitedCoords = splitedLine[1].split(" ");
				int[] coordsList = new int[splitedCoords.length];
				if(coordsList.length < 5)
					throw new RuntimeException("Invalid list for area line: "+line);
				for(int i = 0; i < coordsList.length; i++)
					coordsList[i] = Integer.parseInt(splitedCoords[i]);
				int areaNameHash = Utils.getNameHash(areaName);
				if(mapAreas.containsKey(areaNameHash))
					continue;
				out.writeInt(areaNameHash);
				out.writeByte(coordsList.length);
				for(int i = 0; i < coordsList.length; i++)
					out.writeShort(coordsList[i]);
				mapAreas.put(areaNameHash, coordsList);
			}
			in.close();
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void loadPackedMapAreas() {
		try {
			DataInputStream in = new DataInputStream(new FileInputStream("data/map/packedMapAreas.ma"));
			while (in.available() != 0) {
				int areaNameHash = in.readInt();
				int[] coordsList = new int[in.readUnsignedByte()];
				for(int i = 0; i < coordsList.length; i++)
					coordsList[i] = in.readUnsignedShort();
				mapAreas.put(areaNameHash, coordsList);
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private MapAreas() {
		
	}
}
