package com.rs.game;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.cache.Cache;
import com.rs.cache.loaders.InterfaceScript;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.item.FloorItem;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.utils.Logger;
import com.rs.utils.MapContainersXteas;
import com.rs.utils.NPCSpawns;
import com.rs.utils.ObjectSpawns;

public class Region {
	
	private int regionId;
	private RegionMap clipedOnlyMap;
	private RegionMap map;
	private CopyOnWriteArrayList<Integer> playersIndexes;
	private CopyOnWriteArrayList<Integer> npcsIndexes;
	private CopyOnWriteArrayList<WorldObject> spawnedObjects;
	private CopyOnWriteArrayList<FloorItem> floorItems;
	private List<WorldObject> removedObjects;
	private WorldObject[][][][] objects;
	private int loadMapStage;
	private boolean loadedNPCSpawns;
	private boolean loadedObjectSpawns;
	private int musicId;

	  public static final String getMusicName(int regionId) {
	    	switch(regionId)  {
	    	/*
	    	 * tutorial island
	    	 */
	    	case 12336:
	    		return "Newbie Melody";
	    	/*
	    	 * darkmeyer
	    	 * 
	    	 */
	    	case 14644:
	    		return "Darkmeyer";
	    	/*
	    	 * kalaboss
	    	 * 
	    	 */
	    	case 13626:
	    	case 13627:
	    	case 13882:
	    		return "Born to Do this";
		/*
		 * Lumbridge, falador and region.
		 */
	    	case 12850:
	    	case 12849:
	    		return "Dream";
	    	case 12851:
	    		return "Autumn Voyage";
	    	case 12338:
	    	case 12339:
	    		return "Start";
	    	case 12593:
	    		return "Book of Spells";
	    	case 12083:
	    		return "Competition";
	    	case 12082:
	    		return "Sea Shanty";
	    	case 12081:
		case 11573:
		case 11574:
		case 11575:
		case 11317:
	    		return "Attention";
	    	case 11825:
	    		return "Arrival";
	    	case 11823:
	    		return "Mudskipper Melody";
	    	case 11824:
	    		return "Sea Shanty2";
	    	case 11826:
	    		return "Fanfare";
	    	case 11570:
	    		return "Wandar";
	    	case 11827:
	    		return "Splendour";
	    	case 11828:
	    		return "Long Way Home";
	    	case 11829:
	    		return "Workshop";
	    	case 12341:
	    		return "Barbarianims";
	    	case 12853:
	    	case 12854:
	    		return "Greatness";
	    	case 12852:
	    		return "Expanse";
	    	case 12597:
	    		return "Bounty Hunter Level 1";
	    	case 12594:
	    		return "Doorways";
	    	case 12855:
	    		return "Crystal Sword";
	    	case 12856:
	    		return "Dark";
	    	case 12599:
		case 11320:
	    		return "Bounty Hunter Level 3";
	    	case 12598:
	    		return "Doorways";
	    	case 12342:
	    	case 12343:
	    		return "Spirit";
	    	case 12600:
	    		return "Scape Wild";
		/*
		 * Camelot and region.
		 */
	    	case 11062:
		case 10806:
		case 10805:
	    		return "Camelot";
		case 10550:
			return "Talking Forest";
		case 10549:
			return "Lasting";
		case 10548:
			return "Wonderous";
		case 10547:
			return "Baroque";
		case 10291:
		case 10292:
		case 10293:
		
			return "Knightly";
			
		/*
		 * Mobilies Armies.
		 */
		case 9516:
			return "Command Centre";
	    	default:
	    		return null;
	    	}
	    }
	  
		public static final String getMusicName3(int regionId) {
			switch (regionId) {
			case 13152: //crucible
				return "Steady";	
			case 13151: //crucible
				return "Hunted";	
			case 12895: //crucible
				return "Target";	
			case 12896: //crucible
				return "I Can See You";	
			case 11575: //burthope
				return "Spiritual";
			case 18512:
			case 18511:
			case 19024:
				return "Tzhaar City III";
			case 18255: //fight pits
				return "Tzhaar Supremacy III";
			case 14948:
				return "Dominion Lobby III";
			default:
				return null;
			}
		}

		public static final String getMusicName2(int regionId) {
			switch (regionId) {
			case 13152: //crucible
				return "I Can See You";	
			case 13151: //crucible
				return "You Will Know Me";	
			case 12895: //crucible
				return "Steady";	
			case 12896: //crucible
				return "Hunted";	
			case 12853:
				return "Cellar Song";
			case 11573: //taverley
				return "Taverley Lament";
				
			case 11575: //burthope
				return "Taverley Adventure";
				/*
				 * kalaboss
				 */
			case 13626:
			case 13627:
			case 13882:
			case 13881:
				return "Daemonheim Fremenniks";
			case 18512:
			case 18511:
			case 19024:
				return "Tzhaar City II";
			case 18255: //fight pits
				return "Tzhaar Supremacy II";
			case 14948:
				return "Dominion Lobby II";
			default:
				return null;
			}
		}

		public static final String getMusicName1(int regionId) {
			switch (regionId) {
			case 15967://Runespan
				return "Runespan";
			case 15711://Runespan
				return "Runearia";
			case 15710://Runespan
				return "Runebreath";
			case 13152: //crucible
				return "Hunted";	
			case 13151: //crucible
				return "Target";	
			case 12895: //crucible
				return "I Can See You";	
			case 12896: //crucible
				return "You Will Know Me";	
			case 12597:
				return "Spirit";
			case 13109:
				return "Medieval";
			case 13110:
				return "Honkytonky Parade";
			case 10658:
				return "Espionage";
			case 13899: //water altar
				return "Zealot";
			case 10039:
				return "Legion";
			case 11319: //warriors guild
				return "Warriors' Guild";
			case 11575: //burthope
				return "Spiritual";
			case 11573: //taverley
				return "Taverley Ambience";
			case 7473:
				return "The Waiting Game";
			case 18512:
			case 18511:
			case 19024:
				return "Tzhaar City I";
			case 18255: //fight pits
				return "Tzhaar Supremacy I";
			case 14672:
			case 14671:
			case 14415:
			case 14416:
				return "Living Rock";
			case 11157: //Brimhaven Agility Arena
				return "Aztec";
			case 15446:
			case 15957:
			case 15958:
				return "Dead and Buried";
			case 12848:
				return "Arabian3";
			case 12954:
			case 12442:
			case 12441:
				return "Scape Cave";
			case 12185:
			case 11929:
				return "Dwarf Theme";
			case 12184:
				return "Workshop";
			case 6992:
			case 6993: //mole lair
				return "The Mad Mole";
			case 9776: // castle wars
				return "Melodrama";
			case 10029:
			case 10285:
				return "Jungle Hunt";
			case 14231: // barrows under
				return "Dangerous Way";
			case 12856: // chaos temple
				return "Faithless";
			case 13104:
			case 12847: // arround desert camp
			case 13359:
			case 13102:
				return "Desert Voyage";
			case 13103:
				return "Lonesome";
			case 12589: // granite mine
				return "The Desert";
			case 13407: //crucible entrance
			case 13360: // dominion tower outside
				return "";
			case 14948:
				return "Dominion Lobby I";
			case 11836: // lava maze near kbd entrance
				return "Attack3";
			case 12091: // lava maze west
				return "Wilderness2";
			case 12092: // lava maze north
				return "Wild Side";
			case 9781:
				return "Gnome Village";
			case 11339: // air altar
				return "Serene";
			case 11083: // mind altar
				return "Miracle Dance";
			case 10827: // water altar
				return "Zealot";
			case 10571: // earth altar
				return "Down to Earth";
			case 10315: // fire altar
				return "Quest";
			case 8523: // cosmic altar
				return "Stratosphere";
			case 9035: // chaos altar
				return "Complication";
			case 8779: // death altar
				return "La Mort";
			case 10059: // body altar
				return "Heart and Mind";
			case 9803: // law altar
				return "Righteousness";
			case 9547: // nature altar
				return "Understanding";
			case 9804: // blood altar
				return "Bloodbath";
			case 13107:
				return "Arabian2";
			case 13105:
				return "Al Kharid";
			case 12342:
				return "Forever";
			case 10806:
				return "Overture";
			case 10899:
				return "Karamja Jam";
			case 13623:
				return "The Terrible Tower";
			case 12374:
				return "The Route of All Evil";
			case 9802:
				return "Undead Dungeon";
			case 10809: // east rellekka
				return "Borderland";
			case 10553: // Rellekka
				return "Rellekka";
			case 10552: // south
				return "Saga";
			case 10296: // south west
				return "Lullaby";
			case 10828: // south east
				return "Legend";
			case 9275:
				return "Volcanic Vikings";
			case 11061:
			case 11317:
				return "Fishing";
			case 9551:
				return "TzHaar!";
			case 12345:
				return "Eruption";
			case 12089:
				return "Dark";
			case 12446:
			case 12445:
				return "Wilderness";
			case 12343:
				return "Dangerous";
			case 14131:
				return "Dance of the Undead";
			case 11844:
			case 11588:
				return "The Vacant Abyss";
			case 13363: // duel arena hospital
				return "Shine";
			case 13362: // duel arena
				return "Duel Arena";
			case 12082: // port sarim
				return "Sea Shanty2";
			case 12081: // port sarim south
				return "Tomorrow";
			case 11602:
				return "Strength of Saradomin";
			case 12590:
				return "Bandit Camp";
			case 10329:
				return "The Sound of Guthix";
			case 9033:
				return "Attack5";
				// godwars
			case 11603:
				return "Zamorak Zoo";
			case 11346:
				return "Armadyl Alliance";
			case 11347:
				return "Armageddon";
			case 13114:
				return "Wilderness";
				// black kngihts fortess
			case 12086:
				return "Knightmare";
				// tzaar
			case 9552:
				return "Fire and Brimstone";
				// kq
			case 13972:
				return "Insect Queen";
				// clan wars free for all:
			case 11094:
				return "Clan Wars";
				/*
				 * tutorial island
				 */
			case 12336:
				return "Newbie Melody";
				/*
				 * darkmeyer
				 */
			case 14644:
				return "Darkmeyer";
				/*
				 * kalaboss
				 */
			case 13626:
			case 13627:
			case 13882:
			case 13881:
				return "Daemonheim Entrance";
				/*
				 * Lumbridge, falador and region.
				 */
			case 11574: // heroes guild
				return "Splendour";
			case 12851:
				return "Autumn Voyage";
			case 12338: // draynor and market
				return "Unknown Land";
			case 12339: // draynor up
				return "Start";
			case 12340: // draynor mansion
				return "Spooky";
			case 12850: // lumbry castle
				return "Harmony";
			case 12849: // east lumbridge swamp
				return "Yesteryear";
			case 12593: // at Lumbridge Swamp.
				return "Book of Spells";
			case 12594: // on the path between Lumbridge and Draynor.
				return "Dream";
			case 12595: // at the Lumbridge windmill area.
				return "Flute Salad";
			case 12854: // at Varrock Palace.
				return "Adventure";
			case 12853: // at varrock center
				return "Garden";
			case 12852: // varock mages
				return "Expanse";
			case 13108:
				return "Still Night";
			case 12083:
				return "Wander";
			case 11828:
				return "Fanfare";
			case 11829:
				return "Scape Soft";
			case 11577:
				return "Mad Eadgar";
			case 10293: // at the Fishing Guild.
				return "Mellow";
			case 11824:
				return "Mudskipper Melody";
			case 11570:
				return "Wandar";
			case 12341:
				return "Barbarianims";
			case 12855:
				return "Crystal Sword";
			case 12344:
				return "Dark";
			case 12599:
				return "Doorways";
			case 12598:
				return "The Trade Parade";
			case 11318:
				return "Ice Melody";
			case 12600:
				return "Scape Wild";
			case 10032: // west yannile:
				return "Big Chords";
			case 10288: // east yanille
				return "Magic Dance";
			case 11826: // Rimmington
				return "Long Way Home";
			case 11825: // rimmigton coast
				return "Attention";
			case 11827: // north rimmigton
				return "Nightfall";
				/*
				 * Camelot and region.
				 */
			case 11062:
			case 10805:
				return "Camelot";
			case 10550:
				return "Talking Forest";
			case 10549:
				return "Lasting";
			case 10548:
				return "Wonderous";
			case 10547:
				return "Baroque";
			case 10291:
			case 10292:
				return "Knightly";
			case 11571: // crafting guild
				return "Miles Away";
			case 11595: // ess mine
				return "Rune Essence";
			case 10294:
				return "Theme";
			case 12349:
				return "Mage Arena";
			case 13365: // digsite
				return "Venture";
			case 13364: // exams center
				return "Medieval";
			case 13878: // canifis
				return "Village";
			case 13877: // canafis south
				return "Waterlogged";
				/*
				 * Mobilies Armies.
				 */
			case 9516:
				return "Command Centre";
			case 12596: // champions guild
				return "Greatness";
			case 10804: // legends guild
				return "Trinity";
			case 11601:
				return "Zaros Zeitgeist"; // zaros godwars
			default:
				return null;
			}
		}

	   
   private static final int getMusicId(int regionId) {
   	String musicName = getMusicName(regionId);
   	if(musicName == null)
   		return -1;
   	int musicIndex = (int) InterfaceScript.getInterfaceScript(1345).getKeyForValue(musicName);
   	return InterfaceScript.getInterfaceScript(1351).getIntValue(musicIndex);
   }


	   /*
    private static final int getMusicId(int regionId) {
    	String musicName = getMusicName(regionId);
    	if(musicName == null)
    		return -1;
    	if(musicName.equals("Born to Do this"))
    		return 803; 
    	return Cache.getCacheFileManagers()[6].getContainerId(getMusicName(regionId));
    }*/
    
	public Region(int regionId) {
		this.regionId = regionId;
		musicId = getMusicId(regionId);
		
		//indexes null by default cuz we dont want them on mem for regions that players cant go in
		checkLoadMap();
	}
	
	public RegionMap forceGetRegionMapClipedOnly() {
		if (clipedOnlyMap == null)
			clipedOnlyMap = new RegionMap(regionId, true);
		return clipedOnlyMap;
	}

	public RegionMap forceGetRegionMap() {
		if (map == null)
			map = new RegionMap(regionId, false);
		return map;
	}
	public void removeMapFromMemory() {
		if(loadMapStage == 2 && (playersIndexes == null || playersIndexes.isEmpty()) && (npcsIndexes == null || npcsIndexes.isEmpty())) {
			objects = null;
			map = null;
			loadMapStage = 0;
		}
	}
	
	public void addMapObject(WorldObject object, int x, int y) {
		if (map == null)
			map = new RegionMap(regionId, false);
		if (clipedOnlyMap == null)
			clipedOnlyMap = new RegionMap(regionId, true);
		int plane = object.getPlane();
		int type = object.getType();
		int rotation = object.getRotation();
		if (x < 0 || y < 0 || x >= map.getMasks()[plane].length
				|| y >= map.getMasks()[plane][x].length)
			return;
		ObjectDefinitions objectDefinition = ObjectDefinitions
				.getObjectDefinitions(object.getId()); // load here

		if (type == 22 ? objectDefinition.getClipType() != 0 : objectDefinition
				.getClipType() == 0)
			return;	
		if (type >= 0 && type <= 3) {
			map.addWall(plane, x, y, type, rotation,
					objectDefinition.isProjectileCliped(), true);
			if (objectDefinition.isProjectileCliped())
				clipedOnlyMap.addWall(plane, x, y, type, rotation,
						objectDefinition.isProjectileCliped(), true);
		} else if (type >= 9 && type <= 21) {
			int sizeX;
			int sizeY;
			if (rotation != 1 && rotation != 3) {
				sizeX = objectDefinition.getSizeX();
				sizeY = objectDefinition.getSizeY();
			} else {
				sizeX = objectDefinition.getSizeY();
				sizeY = objectDefinition.getSizeX();
			}
			map.addObject(plane, x, y, sizeX, sizeY,
					objectDefinition.isProjectileCliped(), true);
			if (objectDefinition.isProjectileCliped())
				clipedOnlyMap.addObject(plane, x, y, sizeX, sizeY,
						objectDefinition.isProjectileCliped(), true);
		} else if (type == 22) {
			// map.addFloor(plane, x, y);
		}
	}
	public void removeMapObject(WorldObject object, int x, int y) {
		if (map == null)
			map = new RegionMap(regionId, false);
		if (clipedOnlyMap == null)
			clipedOnlyMap = new RegionMap(regionId, true);
		int plane = object.getPlane();
		int type = object.getType();
		int rotation = object.getRotation();
		if (x < 0 || y < 0 || x >= map.getMasks()[plane].length
				|| y >= map.getMasks()[plane][x].length)
			return;
		ObjectDefinitions objectDefinition = ObjectDefinitions
				.getObjectDefinitions(object.getId()); // load here
		if (type == 22 ? objectDefinition.getClipType() != 0 : objectDefinition
				.getClipType() == 0)
			return;
		if (type >= 0 && type <= 3) {
			map.removeWall(plane, x, y, type, rotation,
					objectDefinition.isProjectileCliped(), true);
			if (objectDefinition.isProjectileCliped())
				clipedOnlyMap.removeWall(plane, x, y, type, rotation,
						objectDefinition.isProjectileCliped(), true);
		} else if (type >= 9 && type <= 21) {
			int sizeX;
			int sizeY;
			if (rotation != 1 && rotation != 3) {
				sizeX = objectDefinition.getSizeX();
				sizeY = objectDefinition.getSizeY();
			} else {
				sizeX = objectDefinition.getSizeY();
				sizeY = objectDefinition.getSizeX();
			}
			map.removeObject(plane, x, y, sizeX, sizeY,
					objectDefinition.isProjectileCliped(), true);
			if (objectDefinition.isProjectileCliped())
				clipedOnlyMap.removeObject(plane, x, y, sizeX, sizeY,
						objectDefinition.isProjectileCliped(), true);
		} else if (type == 22) {
			// map.removeFloor(plane, x, y);
		}
	}

	//override by static region to empty
	public void checkLoadMap() {
		if(loadMapStage == 0) {
			loadMapStage = 1;
			//lets use slow executor, if we take 1-3sec to load objects who cares? what maters are the players on the loaded regions lul
			CoresManager.slowExecutor.submit(new Runnable() {
				@Override
				public void run() {
					loadRegionMap();
					loadMapStage = 2;
					if(!loadedObjectSpawns) {
						loadObjectSpawns();
						loadedObjectSpawns = true;
					}
					if(!loadedNPCSpawns) {
						loadNPCSpawns();
						loadedNPCSpawns = true;
					}
				}
			});
		}
	}
	
	private void loadNPCSpawns() {
		NPCSpawns.loadNPCSpawns(regionId);
	}
	
	private void loadObjectSpawns() {
		ObjectSpawns.loadObjectSpawns(regionId);
	}
	
	public int getRegionId() {
		return regionId;
	}
	
	private void loadRegionMap() {
		int regionX = (regionId >> 8) * 64;
		int regionY = (regionId & 0xff) * 64;
		int landContainerId = Cache.getCacheFileManagers()[5].getContainerId("l"
				+ ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8));
		byte[] landContainerData = landContainerId == -1 ? null : Cache.getCacheFileManagers()[5].getFileData(landContainerId, 0, MapContainersXteas.getMapContainerXteas(regionId));
		int mapContainerId = Cache.getCacheFileManagers()[5].getContainerId("m"
				+ ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8));
		byte[] mapContainerData = mapContainerId == -1 ? null : Cache.getCacheFileManagers()[5].getFileData(mapContainerId, 0);
		byte[][][] mapSettings = mapContainerData == null ? null
				: new byte[4][64][64];
		if (mapContainerData != null) {
			InputStream mapStream = new InputStream(mapContainerData);
			for (int plane = 0; plane < 4; plane++) {
				for (int x = 0; x < 64; x++) {
					for (int y = 0; y < 64; y++) {
						while (true) {
							int value = mapStream.readUnsignedByte();
							if (value == 0) {
								break;
							} else if (value == 1) {
								mapStream.readByte();
								break;
							} else if (value <= 49) {
								mapStream.readByte();

							} else if (value <= 81) {
								mapSettings[plane][x][y] = (byte) (value - 49);
							}
						}
					}
				}
			}
				if (regionId != 11844) { // that region floor is wrong shouldnt be
					// cliped
for (int plane = 0; plane < 4; plane++) {
for (int x = 0; x < 64; x++) {
	for (int y = 0; y < 64; y++) {
		if ((mapSettings[plane][x][y] & 0x1) == 1
				&& (mapSettings[1][x][y] & 2) != 2)
			forceGetRegionMap().clipTile(plane, x, y);
	}
}
}
}
}
if (landContainerData != null) {
InputStream landStream = new InputStream(landContainerData);
int objectId = -1;
int incr;
while ((incr = landStream.readSmart2()) != 0) {
objectId += incr;
int location = 0;
int incr2;
while ((incr2 = landStream.readUnsignedSmart()) != 0) {
location += incr2 - 1;
int localX = (location >> 6 & 0x3f);
int localY = (location & 0x3f);
int plane = location >> 12;
int objectData = landStream.readUnsignedByte();
int type = objectData >> 2;
int rotation = objectData & 0x3;
if (localX < 0 || localX >= 64 || localY < 0
		|| localY >= 64)
	continue;
int objectPlane = plane;
if (mapSettings != null
		&& (mapSettings[1][localX][localY] & 2) == 2) 
	objectPlane--;
if (objectPlane < 0 || objectPlane >= 4 || plane < 0
		|| plane >= 4)
	continue;
addObject(new WorldObject(objectId, type, rotation, localX + regionX, localY + regionY, objectPlane), 
		objectPlane, localX, localY);
}
}
}
		if(landContainerData == null && landContainerId != -1 && MapContainersXteas.getMapContainerXteas(regionId) != null) {
			//player.setNextWorldTile(new WorldTile(3222, 3222, 0));
			Logger.log(this, "Missing xteas for region "+regionId+".");
		}
	}
	

	public void addObject(WorldObject object, int plane, int localX, int localY) {
		if(World.restrictedTiles != null){
			for(WorldTile restrictedTile : World.restrictedTiles){
				if(restrictedTile != null){
					int restX = restrictedTile.getX(), restY = restrictedTile.getY();
					int restPlane = restrictedTile.getPlane();
							
					if(object.getX() == restX && object.getY() == restY && object.getPlane() == restPlane){
						World.spawnObject(new WorldObject(-1, 10, 2, object.getX(), object.getY(), object.getPlane()), false);
						return;
					}
				}
			}
		}
		addMapObject(object, localX, localY);
		if(objects == null)
			objects = new WorldObject[4][64][64][];
		WorldObject[] tileObjects = objects[plane][localX][localY];
		if(tileObjects == null)
			objects[plane][localX][localY] = new WorldObject[] {object};
		else {
			WorldObject[] newTileObjects = new WorldObject[tileObjects.length+1];
			newTileObjects[tileObjects.length] = object;
			System.arraycopy(tileObjects, 0, newTileObjects, 0, tileObjects.length);
			objects[plane][localX][localY] = newTileObjects;
		}
	}
	
	public CopyOnWriteArrayList<Integer> getPlayerIndexes() {
		return playersIndexes;
	}
	
	public CopyOnWriteArrayList<Integer> getNPCsIndexes() {
		return npcsIndexes;
	}
	
	public void addPlayerIndex(int index) {
		//creates list if doesnt exist
		if(playersIndexes == null)
			playersIndexes = new CopyOnWriteArrayList<Integer>();
			playersIndexes.add(index);
	}
	
	public void addNPCIndex(int index) {
		//creates list if doesnt exist
		if(npcsIndexes == null)
			npcsIndexes = new CopyOnWriteArrayList<Integer>();
			npcsIndexes.add(index);
	}
	
	public void removePlayerIndex(Integer index) {
			if(playersIndexes == null) //removed region example cons or dung
				return;
			playersIndexes.remove(index);
	}
	
	public void removeNPCIndex(Integer index) {
			if(npcsIndexes == null) //removed region example cons or dung
				return;
			npcsIndexes.remove(index);
	}
	/*
	public WorldObject getObject(int plane, int x, int y) {
		WorldObject[] objects = getObjects(plane, x,y);
		if(objects == null)
			return null;
		return objects[0];
	}
	
	//override by static region to get objects from needed
	public WorldObject[] getObjects(int plane, int x, int y) {
		checkLoadMap();
		//if objects just loaded  now will return null, anyway after they load will return correct so np
		if(objects == null)
			return null;
		return objects[plane][x][y];
	}*/
	
	public WorldObject getObject(int id, WorldTile tile) {
		int absX = (regionId >> 8) * 64;
		int absY = (regionId & 0xff) * 64;
		int localX = tile.getX()-absX;
		int localY = tile.getY()-absY;
		if(localX < 0 || localY < 0 || localX >= 64 || localY >= 64)
			return null;
		WorldObject spawnedObject = getSpawnedObject(tile);
		if(spawnedObject != null)
			return spawnedObject;
		WorldObject[] mapObjects = getObjects(tile.getPlane(), localX, localY);
		if(mapObjects == null)
			return null;
		for(WorldObject object : mapObjects)
			if(object.getId() == id)
				return object;
		return null;
	}
	
	public WorldObject getSpawnedObject(WorldTile tile) {
		if(spawnedObjects == null)
			return null;
		for(WorldObject object : spawnedObjects)
			if(object.getX() == tile.getX() && object.getY() == tile.getY() && object.getPlane() == tile.getPlane())
				return object;
		return null;
	}
	
	public void addObject(WorldObject object) {
		if(spawnedObjects == null)
			spawnedObjects = new CopyOnWriteArrayList<WorldObject>();
		spawnedObjects.add(object);
	}
	
	public void removeObject(WorldObject object) {
		if(spawnedObjects == null)
			return;
		spawnedObjects.remove(object);
	}
	
	public CopyOnWriteArrayList<WorldObject> getSpawnedObjects() {
		return spawnedObjects;
	}
	
	public WorldObject getRealObject(WorldObject spawnObject) {
		int absX = (regionId >> 8) * 64;
		int absY = (regionId & 0xff) * 64;
		int localX = spawnObject.getX()-absX;
		int localY = spawnObject.getY()-absY;
		WorldObject[] mapObjects = getObjects(spawnObject.getPlane(), localX, localY);
		if(mapObjects == null)
			return null;
		for(WorldObject object : mapObjects)
			if(object.getType() == spawnObject.getType())
				return object;
		return null;
	}
	
	public boolean containsObject(int id, WorldTile tile) {
		int absX = (regionId >> 8) * 64;
		int absY = (regionId & 0xff) * 64;
		int localX = tile.getX()-absX;
		int localY = tile.getY()-absY;
		if(localX < 0 || localY < 0 || localX >= 64 || localY >= 64)
			return false;
		WorldObject spawnedObject = getSpawnedObject(tile);
		if(spawnedObject != null)
			return spawnedObject.getId() == id;
		WorldObject[] mapObjects = getObjects(tile.getPlane(), localX, localY);
		if(mapObjects == null)
			return false;
		for(WorldObject object : mapObjects)
			if(object.getId() == id)
				return true;
		return false;
	}	
	
	public CopyOnWriteArrayList<FloorItem> forceGetFloorItems() {
		if(floorItems == null)
			floorItems = new CopyOnWriteArrayList<FloorItem>();
		return floorItems;
	}
	public CopyOnWriteArrayList<FloorItem> getFloorItems() {
		return floorItems;
	}
	
	public FloorItem getGroundItem(int id, WorldTile tile, Player player) {
		if(floorItems == null)
			return null;
		for(FloorItem item : floorItems) {
			if((item.isInvisible() || item.isGrave()) && player != item.getOwner())
				continue;
			if(item.getId() == id && tile.getX() == item.getTile().getX() && tile.getY() == item.getTile().getY() && tile.getPlane() == item.getTile().getPlane())
				return item;
		}
		return null;
	}

	public int getMusicId() {
		return musicId;
	}
	// setMask
/*
	public int getRotation(int plane, int localX, int localY) {
		return 0;
	}
	// overrided by static region to get mask from needed region
	public int getMaskClipedOnly(int plane, int localX, int localY) {
		if (clipedOnlyMap == null || getLoadMapStage() != 2)
			return -1; // cliped tile
		return clipedOnlyMap.getMasks()[plane][localX][localY];
	}*/
	public int getLoadMapStage() {
		return loadMapStage;
	}

	public void setLoadMapStage(int loadMapStage) {
		this.loadMapStage = loadMapStage;
	}
	/*
	// overrided by static region to get mask from needed region
	public int getMask(int plane, int localX, int localY) {
		if (map == null || getLoadMapStage() != 2)
			return -1; // cliped tile

		if (localX >= 64 || localY >= 64 || localX < 0 || localY < 0) {
			WorldTile tile = new WorldTile(map.getRegionX() + localX,
					map.getRegionY() + localY, plane);
			int regionId = tile.getRegionId();
			int newRegionX = (regionId >> 8) * 64;
			int newRegionY = (regionId & 0xff) * 64;
			return World.getRegion(tile.getRegionId()).getMask(plane,
					tile.getX() - newRegionX, tile.getY() - newRegionY);
		}

		return map.getMasks()[plane][localX][localY];
	}

	public void setMask(int plane, int localX, int localY, int mask) {
		if (map == null || getLoadMapStage() != 2)
			return; // cliped tile

		if (localX >= 64 || localY >= 64 || localX < 0 || localY < 0) {
			WorldTile tile = new WorldTile(map.getRegionX() + localX,
					map.getRegionY() + localY, plane);
			int regionId = tile.getRegionId();
			int newRegionX = (regionId >> 8) * 64;
			int newRegionY = (regionId & 0xff) * 64;
			World.getRegion(tile.getRegionId()).setMask(plane,
					tile.getX() - newRegionX, tile.getY() - newRegionY, mask);
			return;
		}

		map.setMask(plane, localX, localY, mask);
	}*/
	// overrided by static region to get mask from needed region
	public int getMask(int plane, int localX, int localY) {
		if (map == null/* || getLoadMapStage() != 2*/)
			return 2097152; // cliped tile

		if (localX >= 64 || localY >= 64 || localX < 0 || localY < 0) {
			WorldTile tile = new WorldTile(map.getRegionX() + localX,
					map.getRegionY() + localY, plane);
			int regionId = tile.getRegionId();
			int newRegionX = (regionId >> 8) * 64;
			int newRegionY = (regionId & 0xff) * 64;
			return World.getRegion(tile.getRegionId()).getMask(plane,
					tile.getX() - newRegionX, tile.getY() - newRegionY);
		}

		return map.getMasks()[plane][localX][localY];
	}

	public void setMask(int plane, int localX, int localY, int mask) {
		if (map == null || getLoadMapStage() != 2)
			return ; // cliped tile

		if (localX >= 64 || localY >= 64 || localX < 0 || localY < 0) {
			WorldTile tile = new WorldTile(map.getRegionX() + localX,
					map.getRegionY() + localY, plane);
			int regionId = tile.getRegionId();
			int newRegionX = (regionId >> 8) * 64;
			int newRegionY = (regionId & 0xff) * 64;
			World.getRegion(tile.getRegionId()).setMask(plane,
					tile.getX() - newRegionX, tile.getY() - newRegionY, mask);
			return;
		}

		map.setMask(plane, localX, localY, mask);
	}

	// setMask

	public int getRotation(int plane, int localX, int localY) {
		return 0;
	}

	// overrided by static region to get mask from needed region
	public int getMaskClipedOnly(int plane, int localX, int localY) {
		if (clipedOnlyMap == null/* || getLoadMapStage() != 2*/)
			return 2097152; // cliped tile//-1
		return clipedOnlyMap.getMasks()[plane][localX][localY];
	}


	public void addRemovedObject(WorldObject object) {
		if (removedObjects == null)
			removedObjects = new CopyOnWriteArrayList<WorldObject>();
		removedObjects.add(object);
	}

	public void removeRemovedObject(WorldObject object) {
		if (removedObjects == null)
			return;
		removedObjects.remove(object);
	}
	public WorldObject getRemovedObject(WorldTile tile) {
		if (removedObjects == null)
			return null;
		for (WorldObject object : removedObjects)
			if (object.getX() == tile.getX() && object.getY() == tile.getY()
					&& object.getPlane() == tile.getPlane())
				return object;
		return null;
	}
	public List<WorldObject> getRemovedObjects() {
		return removedObjects;
	}


	public WorldObject getObject(int plane, int x, int y) {
		WorldObject[] objects = getObjects(plane, x, y);
		if (objects == null)
			return null;
		return objects[0];
	}

	public WorldObject getObject(int plane, int x, int y, int type) {
		WorldObject[] objects = getObjects(plane, x, y);
		if (objects == null)
			return null;
		for (WorldObject object : objects)
			if (object.getType() == type)
				return object;
		return null;
	}

	// override by static region to get objects from needed
	public WorldObject[] getObjects(int plane, int x, int y) {
		checkLoadMap();
		// if objects just loaded now will return null, anyway after they load
		// will return correct so np
		if (objects == null)
			return null;
		return objects[plane][x][y];
	}
	
}
