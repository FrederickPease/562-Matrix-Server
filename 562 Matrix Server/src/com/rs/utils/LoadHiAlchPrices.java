package com.rs.utils;

import java.io.BufferedReader;
import java.io.FileReader;



/**
 * Item Definition class
 * 
 * @author Graham
 * 
 */
public class LoadHiAlchPrices {

	private final int[] hiAlchPrice = new int[31000];

	public void LoadObjects() {

	}

	public LoadHiAlchPrices() {
		int amt = 0;
		String line = "", token = "", token2 = "", token2_2 = "", token3[] = new String[10];
		BufferedReader list = null;
		try {
			list = new BufferedReader(new FileReader("./data/hialchprices.cfg"));
			line = list.readLine().trim();
		} catch (Exception e) {
			throw new RuntimeException("Error loading item list.");
		}
		while (line != null) {
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot).trim();
				token2 = line.substring(spot + 1).trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("Item")) {
					amt++;
					int ItemId = Integer.parseInt(token3[0]);
					hiAlchPrice[ItemId] = Integer.parseInt(token3[1]);
				}
			} else {
				if (line.equals("[ENDOFPRICELIST]")) {
					try {
						LoadObjects();
						Logger.log("LoadHiAlchPrices", "Loaded " + amt + " High Alchemy prices.");
						list.close();
					} catch (Exception exception) {
					}
					list = null;
					return;
				}
			}
			try {
				line = list.readLine().trim();
			} catch (Exception exception1) {
				try {
					list.close();
				} catch (Exception exception) {
				}
				list = null;
				return;
			}
		}
	}

	public int getHiAlchPrice(int Id) {
		return hiAlchPrice[Id];
	}

}
