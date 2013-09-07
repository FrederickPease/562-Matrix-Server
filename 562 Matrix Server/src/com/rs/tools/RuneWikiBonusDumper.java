package com.rs.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;

import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.utils.Utils;

public class RuneWikiBonusDumper {

	public static final void main(String[] args) throws IOException {
		System.out.println("Starting..");
		Cache.init();
		for(int itemId = 21348; itemId < Utils.getItemDefinitionsSize(); itemId++) {
			if(ItemDefinitions.getItemDefinitions(itemId).isWearItem())
				if(dumpItem(itemId))
					System.out.println("DUMPED ITEM : "+itemId);
				else
					System.out.println("FAILED ITEM: "+itemId+", "+ItemDefinitions.getItemDefinitions(itemId).getName());
		}
	}
	
	public static boolean dumpItem(int itemId) {
		String pageName = ItemDefinitions.getItemDefinitions(itemId).getName();
		if(pageName == null || pageName.equals("null"))
			return false;
		pageName = pageName.replace("(p)", "");
		pageName = pageName.replace("(p+)", "");
		pageName = pageName.replace("(p++)", "");
		pageName = pageName.replaceAll(" ", "_");
		try {
			WebPage page = new WebPage("http://runescape.wikia.com/wiki/"+pageName);
			try {
				page.load();
			}catch (Exception e) {
				System.out.println("Invalid page: "+itemId+", "+pageName);
				return false;
			}
			
			int bonusId = 0;
			int[] bonuses = new int[18];
			for(String  line : page.getLines()) {
				
				if(bonusId == 0 || bonusId == 5) {
					String replace = "<td colspan=\"2\" width=\"30\" align=\"center\">";
					if(line.startsWith(replace)) {
						line = line.replace(replace, "").replaceAll(" ", "").replace("+", "").replace("<br/>", "");
						int bonus = Integer.valueOf(line);
						//System.out.println(bonus);
						bonuses[bonusId++] = bonus;
						continue;
					}
				}else if ((bonusId >= 1 && bonusId <= 4) || (bonusId >= 6 && bonusId <= 10)) {
					String replace = "</td><td colspan=\"2\" width=\"30\" align=\"center\">";
					if(line.startsWith(replace)) {
						line = line.replace(replace, "").replaceAll(" ", "").replace("+", "").replace("64to100", "100");
						int bonus = Integer.valueOf(line);
						//System.out.println(bonus);
						bonuses[bonusId++] = bonus;
						continue;
					}	
				}else if (bonusId == 11) {
					String replace = "<td colspan=\"4\" width=\"60\" align=\"center\">";
					if(line.startsWith(replace)) {
						line = line.replace(replace, "").replaceAll(" ", "").replace("%", "").replace(".0", "");
						int bonus = Integer.valueOf(line);
						//System.out.println(bonus);
						bonuses[bonusId++] = bonus;
						continue;
					}	
				}else if (bonusId == 12 || bonusId == 13) {
						String replace = "</td><td colspan=\"4\" width=\"60\" align=\"center\">";
						if(line.startsWith(replace)) {
							line = line.replace(replace, "").replaceAll(" ", "").replace("%", "").replace(".0", "");
							int bonus = Integer.valueOf(line);
					//		System.out.println(bonus);
							bonuses[bonusId++] = bonus;
							continue;
						}	
				}else if (bonusId == 14) {
					String replace = "<td colspan=\"3\" width=\"45\" align=\"center\">";
					if(line.startsWith(replace)) {
						line = line.replace(replace, "").replaceAll(" ", "").replace("+", "").replace(".0", "").replace(".1", "").replace(".5", "");
						int bonus = Integer.valueOf(line);
					//	System.out.println(bonus);
						bonuses[bonusId++] = bonus;
						continue;
					}	
				}else if (bonusId >= 15 && bonusId <= 17) {
					String replace = "</td><td colspan=\"3\" width=\"45\" align=\"center\">";
					if(line.startsWith(replace)) {
						line = line.replace(replace, "").replaceAll(" ", "").replace("%", "").replace("+", "").replace(".0", "").replace("52to70", "70").replace("0(4trimmed)", "4").replace("15(Slayertasksonly)", "0").replace(".5", "").replace("?", "0").replace("<i>Varies</i>", "0");
						int bonus = Integer.valueOf(line);
					//	System.out.println(bonus);
						bonuses[bonusId++] = bonus;
						if(bonusId == 18)
							break;
						continue;
					}	
				}
			}
			
			if(bonusId != 18)
				return false;
			boolean end = true;
			for(int bonus : bonuses) {
				if(bonus != 0)  {
					end = false;
					break;
				}
			}
			if(end)
				return false;
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter("bonuses/"+itemId+".txt"));
				writer.write("Attack bonus");
				writer.newLine();
				writer.flush();
				for(int index = 0; index < 5; index++) {
					writer.write(""+bonuses[index]);
					writer.newLine();
					writer.flush();
				}
				writer.write("Defence bonus");
				writer.newLine();
				writer.flush();
				for(int index = 5; index < 11; index++) {
					writer.write(""+bonuses[index]);
					writer.newLine();
					writer.flush();
				}
				writer.write("Damage absorption");
				writer.newLine();
				writer.flush();
				for(int index = 11; index < 14; index++) {
					writer.write(""+bonuses[index]);
					writer.newLine();
					writer.flush();
				}
				writer.write("Other bonuses");
				writer.newLine();
				writer.flush();
				for(int index = 14; index < 18; index++) {
					writer.write(""+bonuses[index]);
					writer.newLine();
					writer.flush();
				}
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			return dumpItem(itemId);
		}
		return false;
	}
	
}
