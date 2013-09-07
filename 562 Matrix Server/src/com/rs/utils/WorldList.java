package com.rs.utils;

//import com.rs.io.InputStream;
import com.rs.io.OutputStream;


public class WorldList {
	
	public static WorldList worldList = null;
	
	private static final int LOCATIONS_AMOUNT = 1;
	
	private static final int[] LOCATION_IDS = new int[]{152};
	private static final String[] LOCATION_HOST_NAMES = new String[]{"Doomer <127.0.0.1>"};
	
	private static final int[] WORLD_ARRAY = new int[]{0};
	private static final int[] WORLD_TYPES = new int[]{1 | 8 | 16};
	private static final String[] WORLD_ACTIVITIES = new String[]{"Doomer World List"}; 
	
	private static final int LISTED_WORLDS_AMOUNT = 1;
	private static final int START_LISTING_AT = 1;
	
	public static final int PLAYER_COUNT_LENGTH = calculateLength(false);
	public static final int FULL_LENGTH = calculateLength(true);
	
	public WorldList() {
	}
	
	public static WorldList getWorldList() {
		if (worldList == null)
			worldList = new WorldList();
		return worldList;
	}
	
	public static int calculateLength(boolean writeWorldInformation) {
		int length = 2;
		if (writeWorldInformation) {
			length += 1;
			for (int i = 0; i < LOCATIONS_AMOUNT; i++) {
				length += 1;
				if (LOCATION_IDS[i] > 128)
					length += 1;
				length += LOCATION_HOST_NAMES[i].getBytes().length + 2;
			}
			length += 3;
			for (int i = 0; i < LISTED_WORLDS_AMOUNT; i++) {
				length += 6;
				length += WORLD_ACTIVITIES[i].getBytes().length + 2;
				length += 11;
			}
			length += 4;
		}
		for (int i = 0; i < LISTED_WORLDS_AMOUNT; i++) {
			length += 3;
		}
		return length;
	}
	
	public static void writeWorldListData(OutputStream stream, boolean writeWorldInformation) {
		stream.writeShort(0);
		stream.writeShort(writeWorldInformation ? FULL_LENGTH : PLAYER_COUNT_LENGTH);
		stream.writeShort(1);
		stream.writeShort(writeWorldInformation ? 1 : 0);
		if (writeWorldInformation) {
			stream.writeSmart(LOCATIONS_AMOUNT); 
			for (int i = 0; i < LOCATIONS_AMOUNT; i++) { 
				stream.writeSmart(LOCATION_IDS[i]);
				stream.writeString(LOCATION_HOST_NAMES[i]);
			}
			stream.writeSmart(START_LISTING_AT); 
			stream.writeSmart(LISTED_WORLDS_AMOUNT);
			stream.writeSmart(LISTED_WORLDS_AMOUNT); 
			for (int i = 0; i < LISTED_WORLDS_AMOUNT; i++) {
				stream.writeSmart(i);
				stream.writeShort(WORLD_ARRAY[i]);
				stream.writeInt(WORLD_TYPES[i]);
				stream.writeString(WORLD_ACTIVITIES[i]);
				stream.writeString("127.0.0.1");
			}
			stream.writeInt(-626474014);
		}
		for (int i = 0; i < LISTED_WORLDS_AMOUNT; i++) {
			stream.writePSmarts(i);
			stream.writeShort(93);
		}
	//	stream.write().removeLogin().close();
	}
	
}