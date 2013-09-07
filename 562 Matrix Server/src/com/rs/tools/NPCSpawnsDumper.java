package com.rs.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.rs.cache.Cache;
import com.rs.io.InputStream;
import com.rs.utils.Logger;
import com.rs.utils.MapContainersXteas;
import com.rs.utils.SerializableFilesManager;

public class NPCSpawnsDumper {

	private static int writtenCount;
	private static ArrayList<Integer> dumpedregions;
	@SuppressWarnings("unchecked")
	public static final void main(String[] args) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter("data/npcs/unpackedSpawnsList.txt", true));
		Logger.log("Launcher", "Initing Cache..."); 
		Cache.init();
		Logger.log("Launcher", "Initing Data File..."); 
		try {
			dumpedregions = (ArrayList<Integer>) SerializableFilesManager.loadSerializedFile(new File("dumpedregions"));
		} catch (ClassNotFoundException e) {
		}
		if(dumpedregions == null) {
			dumpedregions = new ArrayList<Integer>();
		}
		MapContainersXteas.init();
		for(int regionId = 0; regionId < 15000; regionId++) {
			if(dumpedregions.contains(regionId))
				continue;
			dumpRegionNPCs(regionId, out);
		}
		out.close();
		System.out.println("found "+writtenCount+" npc spawns on cache.");
		SerializableFilesManager.storeSerializableClass(dumpedregions, new File("dumpedregions"));
		
	}
	
	public static final void dumpRegionNPCs(int regionId, BufferedWriter writer) throws IOException {
		writer.flush();
		int regionX = (regionId >> 8) * 64;
		int regionY = (regionId & 0xff) * 64;
		int npcSpawnsContainerId = Cache.getCacheFileManagers()[5].getContainerId("n" + ((regionX >> 3) / 8)+ "_" + ((regionY >> 3) / 8));
		if(npcSpawnsContainerId == -1)
			return;
		byte[] npcSpawnsContainerData = Cache.getCacheFileManagers()[5].getFileData(npcSpawnsContainerId, 0, null);
		if(npcSpawnsContainerData == null)
			return;
		boolean added = false;
		InputStream stream = new InputStream(npcSpawnsContainerData);
		while(stream.getRemaining() > 0) {
			int hash = stream.readUnsignedShort();
			int npcId = stream.readUnsignedShort();
			/*if((NPCDefinitions.getNPCDefinitions(npcId).walkMask & 0x1) <= 0)
				continue;*/
			if(!added) {
				writer.newLine();
				writer.write("//RegionId: "+regionId+", regionX: "+regionX+", regionY: "+regionY);
				writer.flush();
				dumpedregions.add(regionId);
				added = true;
			}
			int plane = hash >> 758085070;
			int localX = (0x1f92 & hash) >> -585992921;
			int x = regionX+localX;
			int localY = 0x3f & hash;
			int y = regionY+localY;
			//System.out.println("npcId: "+npcId+", plane: "+plane+", x: "+x+", y: "+y);
			writer.newLine();
			writer.write(npcId+" - "+x+" "+y+" "+plane);
			writer.flush();
			writtenCount++;
		}
	}
	
}
