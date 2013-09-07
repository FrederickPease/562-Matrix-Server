package com.rs;

import com.rs.game.WorldTile;

public final class Settings {

	//client/server settings
	public static final String SERVER_NAME = "VexationX";
	public static final int PORT_ID = 43594;
	public static final String CACHE_PATH = "data/cache";
	public static final int RECEIVE_DATA_LIMIT = 5000;
	public static final int PACKET_SIZE_LIMIT = 5000;
	public static final int CLIENT_REVISION = 562;
	public static final int CUSTOM_CACHE_REVISION = 1;
	//world settings
	public static final int START_PLAYER_HITPOINTS = 10;
	public static final WorldTile START_PLAYER_LOCATION = new WorldTile(3222, 3222, 0);//new WorldTile(3094, 3107, 0);
	public static final WorldTile RESPAWN_PLAYER_LOCATION = new WorldTile(3222, 3222, 0);
	public static final long MAX_PACKETS_DECODER_PING_DELAY = 30000; //30seconds
	public static final int WORLD_CYCLE_TIME = 600; //the speed of world in ms
	public static final int XP_RATE = 5; //5x
	public static final int DROP_RATE = 10;
	//mem settings
	public static final int PLAYERS_LIMIT = 2000;
	public static final int LOCAL_PLAYERS_LIMIT = 250;
	public static final int NPCS_LIMIT = Short.MAX_VALUE;
	public static final int LOCAL_NPCS_LIMIT = 250;
	public static final int MIN_FREE_MEM_ALLOWED = 30000000; //30mb
	//game constants
	public static final int [] MAP_SIZES = {104, 120, 136, 168};
	/**
	 * Music & Emote settings
	 */
	public static final int AIR_GUITAR_MUSICS_COUNT = 50;
	
	/**
	 * Launching settings
	 */
	public static boolean DEBUG;
	public static boolean HOSTED;
	public static boolean ECONOMY;
	
	/**
	 * Donator settings
	 */
	public static String[] DONATOR_ITEMS = {  };
	
	public static String[] EXTREME_DONATOR_ITEMS = {  };

	/**
	 * Item settings
	 */
	public static String[] EARNED_ITEMS = { };
	
	public static String[] REMOVING_ITEMS = { "(class",
		"sacred clay", "dominion", "sled", "party", "torva", "disc", "whip","torva", "virtus", "pernix", "ganodermic", "polypore", "spirit shield", 
		"chaotic", "celestial",  "fallen cape","Chaotic", "Chaotic rapier",
        "Chaotic longsword", "Chaotic maul", "Chaotic staff", 
        "Chaotic crossbow", "Chaotic kiteshield", "bandos", 
        "torva", "flaming skull", "max", "dragon","party", 
        "santa", "h'ween", "ganodermic","katana", "h'weehn", "spirit", "spirit shield", "staff of light"};
	
	public static String[] VOTE_REQUIRED_ITEMS = {  };

	
	private Settings() {
		
	}
}