package com.rs.game;

import java.io.IOException;

import com.rs.cache.Cache;
import com.rs.cache.loaders.NPCDefinitions;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Cache.init();
		System.out.println(NPCDefinitions.getNPCDefinitions(7398).walkMask & 0x2);
		/*MapContainersXteas.init();
		CoresManager.init();
		for(int regionId = 0; regionId < 20000; regionId++) {
			Region region = World.getRegion(regionId);
		}
		System.out.println("done");*/
		
	}

}
