package com.rs.tools;

import java.io.IOException;
import java.net.MalformedURLException;

import com.rs.cache.Cache;
import com.rs.cache.loaders.NPCDefinitions;

public class RuneWikiNPCDefinitionsDumper {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Cache.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dumpNPC(8);
	}
	
	public static boolean dumpNPC(int npcId) {
		NPCDefinitions defs = NPCDefinitions.getNPCDefinitions(npcId);
		if(!defs.hasAttackOption())
			return false;
		String pageName = defs.name;
		if(pageName == null || pageName.equals("null"))
			return false;
		try {
			WebPage page = new WebPage("http://runescape.wikia.com/wiki/"+pageName);
			try {
				page.load();
			}catch (Exception e) {
				System.out.println("Invalid page: "+npcId+", "+pageName);
				return false;
			}
			for(String  line : page.getLines()) {
				System.out.println(line);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
