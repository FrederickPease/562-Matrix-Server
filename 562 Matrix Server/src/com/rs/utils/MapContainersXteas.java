package com.rs.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public final class MapContainersXteas {

	private final static HashMap<Integer, int[]> mapContainersXteas = new HashMap<Integer, int[]>();
	
	public static final int[] getMapContainerXteas(int regionId) {
		return mapContainersXteas.get(regionId);
	}
	public static void init() {
		if(new File("data/map/containersXteas/packed.mcx").exists())
			loadPackedXteas();
		else
			loadUnpackedXteas();
	}
	
	private static final void loadPackedXteas() {
		try {
			DataInputStream in = new DataInputStream(new FileInputStream("data/map/containersXteas/packed.mcx"));
			while (in.available() != 0) {
				int regionId = in.readShort();
				int[] xteas = new int[4];
				for (int index = 0; index < 4; index++)
					xteas[index] = in.readInt();
				mapContainersXteas.put(regionId, xteas);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static final void loadUnpackedXteas() {
		Logger.log("MapContainersXteas", "Packing map containers xteas...");
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream("data/map/containersXteas/packed.mcx"));
			File unpacked = new File("data/map/containersXteas/unpacked/");
			File[] xteasFiles = unpacked.listFiles();
			for (File region : xteasFiles) {
				String name = region.getName();
				if (!name.contains(".txt")) {
					region.delete();
					continue;
				}
				int regionId = Short.parseShort(name.replace(".txt", ""));
				if(regionId <= 0) {
					region.delete();
					continue;
				}
				BufferedReader in = new BufferedReader(new FileReader(region));
				out.writeShort(regionId);
				final int[] xteas = new int[4];
				for (int index = 0; index < 4; index++) {
					xteas[index] = Integer.parseInt(in.readLine());
					out.writeInt(xteas[index]);
				}
				mapContainersXteas.put(regionId, xteas);
				in.close();
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private MapContainersXteas() {
		
	}
	
}
