package com.rs.game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.cache.Cache;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

public final class RegionBuilder {
	

	public static final int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;

	/*
	 * build here the maps you wont edit again
	 */
	public static void init() {
		//a small test, copying 100x100 area from lumby up to varrock to coords 4000-4100x 4000-4100y
		//int[] map = findEmptyMap(100,100);
		//copyAllPlanesMap(getRegion(3222), getRegion(3222), map[0], map[1], 100);
	}
	
	
	public static int getRegion(int c) {
		return c >> 3;
	}
	
	//private static boolean lastSearchPositive; //used to fast up the formula
	/*
	 * returns the offset regionx and y
	 * notice that every region is 8x8 block. so example x 2 y 3 is (16 x 24 area)
	 * the returned map is distanced from other existing/generated maps so you wont see them if you close to them
	 */
	/*public static int[] findEmptyMap(int widthRegions, int heightRegions) {
		boolean lastSearchPositive = RegionBuilder.lastSearchPositive = !RegionBuilder.lastSearchPositive;
		int regionsXDistance = ((widthRegions)/8)+1; //1map distance at least
		int regionsYDistance = ((heightRegions)/8)+1; //1map distance at least
		for(int regionIdC = 0; regionIdC < 20000; regionIdC++) {
			int regionId = lastSearchPositive ? 20000-regionIdC : regionIdC;
			int regionX = (regionId >> 8) * 64;
			int regionY = (regionId & 0xff) * 64;
			if(regionX >> 3 < 336 || regionY >> 3 < 336)
				continue;
			boolean found = true;
			for(int thisRegionX = regionX - 64;
					thisRegionX < (regionX + (regionsXDistance * 64)); thisRegionX += 64) {
				for(int thisRegionY = regionY - 64;
						thisRegionY < (regionY + (regionsYDistance * 64)); thisRegionY += 64) {
					if(thisRegionX < 0 || thisRegionY < 0)
						continue;
					if(!emptyRegion(thisRegionX,
							thisRegionY, !(thisRegionX < regionX
							|| thisRegionY < regionY
							|| thisRegionX > (regionX + ((regionsXDistance-1) * 64)))
							|| thisRegionY > (regionY + ((regionsYDistance-1) * 64)))) {
						found = false;
						break;
					}
					
				}
			}
			if(found)
				return new int[] {getRegion(regionX), getRegion(regionY)};
		}
		return null;
	}
	
	private static boolean emptyRegion(int regionX, int regionY, boolean checkValid) {
		if(regionX > 10000 || regionY > 16000)
			return !checkValid; //invalid map gfto
		int rx = getRegion(regionX) / 8;
		int ry = getRegion(regionY) / 8;
		if(Cache.getCacheFileManagers()[5].getContainerId("m" + rx+ "_" + ry) != -1)
			return false; //a real map already exists
		Region region = World.getRegions().get((rx << 8) + ry);
		return region == null || !(region instanceof DynamicRegion);
	}*/
	
	private static boolean lastSearchPositive; //used to fast up the formula
	/*
	 * returns the offset regionx and y
	 * notice that every region is 8x8 block. so example x 2 y 3 is (16 x 24 area)
	 * the returned map is distanced from other existing/generated maps so you wont see them if you close to them
	 */
	public static int[] findEmptyMap(int widthRegions, int heightRegions) {
	boolean lastSearchPositive = RegionBuilder.lastSearchPositive = !RegionBuilder.lastSearchPositive;
		lastSearchPositive = !lastSearchPositive;
		int regionsXDistance = ((widthRegions)/8)+1; //1map distance at least
		int regionsYDistance = ((heightRegions)/8)+1; //1map distance at least
		for(int regionIdC = 0; regionIdC < 20000; regionIdC++) {
			int regionId = lastSearchPositive ? 20000-regionIdC : regionIdC;
			int regionX = (regionId >> 8) * 64;
			int regionY = (regionId & 0xff) * 64;
			boolean found = true;
			for(int thisRegionX = regionX - 64;
					thisRegionX < (regionX + (regionsXDistance * 64)); thisRegionX += 64) {
				for(int thisRegionY = regionY - 64;
						thisRegionY < (regionY + (regionsYDistance * 64)); thisRegionY += 64) {
					if(thisRegionX < 0 || thisRegionY < 0)
						continue;
					if(!emptyRegion(thisRegionX,
							thisRegionY, !(thisRegionX < regionX
							|| thisRegionY < regionY
							|| thisRegionX > (regionX + ((regionsXDistance-1) * 64)))
							|| thisRegionY > (regionY + ((regionsYDistance-1) * 64)))) {
						found = false;
						break;
					}
					
				}
			}
			if(found)
				return new int[] {getRegion(regionX), getRegion(regionY)};
		}
		return null;
	}
	
	private static boolean emptyRegion(int regionX, int regionY, boolean checkValid) {
		if(regionX > 10000 || regionY > 16000)
			return !checkValid; //invalid map gfto
		int rx = getRegion(regionX) / 8;
		int ry = getRegion(regionY) / 8;
		if(Cache.getCacheFileManagers()[5].getContainerId("m" + rx+ "_" + ry) != -1) {
			return false; //a real map already exists
		}
		Region region = World.getRegions().get((rx << 8) + ry);
		return region == null || !(region instanceof DynamicRegion);
	}
	
	public static void cutRegion(int regionX, int regionY, int plane) {
		DynamicRegion toRegion = createDynamicRegion((((regionX / 8) << 8) + (regionY / 8)));
		int regionOffsetX = (regionX - ((regionX/8) * 8));
		int regionOffsetY = (regionY - ((regionY/8) * 8));
		toRegion.getRegionCoords()[plane][regionOffsetX][regionOffsetY][0] = 0;
		toRegion.getRegionCoords()[plane][regionOffsetX][regionOffsetY][1] = 0;
		toRegion.getRegionCoords()[plane][regionOffsetX][regionOffsetY][2] = 0;
		toRegion.getRegionCoords()[plane][regionOffsetX][regionOffsetY][3] = 0;
	}
	
	public static final void destroyMap(int toRegionX, int toRegionY, int widthRegions, int heightRegions) {
		for(int xOffset = 0; xOffset < widthRegions; xOffset++) {
			for(int yOffset = 0; yOffset < heightRegions; yOffset++) {
				int toThisRegionX = toRegionX+xOffset;
				int toThisRegionY = toRegionY+yOffset;
				destroyDynamicRegion(((toThisRegionX / 8) << 8) + (toThisRegionY / 8));
			}
		}
	}
	
	
	public static final void repeatMap(int toRegionX, int toRegionY, int widthRegions, int heightRegions, int rx, int ry, int plane, int rotation, int... toPlanes) {
		for(int xOffset = 0; xOffset < widthRegions; xOffset++) {
			for(int yOffset = 0; yOffset < heightRegions; yOffset++) {
				int toThisRegionX = toRegionX+xOffset;
				int toThisRegionY = toRegionY+yOffset;
				DynamicRegion toRegion = createDynamicRegion((((toThisRegionX / 8) << 8) + (toThisRegionY / 8)));
				int regionOffsetX = (toThisRegionX - ((toThisRegionX/8) * 8));
				int regionOffsetY = (toThisRegionY - ((toThisRegionY/8) * 8));
				for(int pIndex = 0; pIndex < toPlanes.length; pIndex++) {
					int toPlane = toPlanes[pIndex];
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][0] = rx;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][1] = ry;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][2] = plane;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][3] = rotation;
				}
			}
		}
	}
	
	public static final void cutMap(int toRegionX, int toRegionY, int widthRegions, int heightRegions, int... toPlanes) {
		for(int xOffset = 0; xOffset < widthRegions; xOffset++) {
			for(int yOffset = 0; yOffset < heightRegions; yOffset++) {
				int toThisRegionX = toRegionX+xOffset;
				int toThisRegionY = toRegionY+yOffset;
				DynamicRegion toRegion = createDynamicRegion((((toThisRegionX / 8) << 8) + (toThisRegionY / 8)));
				int regionOffsetX = (toThisRegionX - ((toThisRegionX/8) * 8));
				int regionOffsetY = (toThisRegionY - ((toThisRegionY/8) * 8));
				for(int pIndex = 0; pIndex < toPlanes.length; pIndex++) {
					int toPlane = toPlanes[pIndex];
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][0] = 0;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][1] = 0;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][2] = 0;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][3] = 0;
				}
			}
		}
	}
	
	/*
	 * copys a single 8x8 map tile and allows you to rotate it
	 */
	public static void copyRegion(int fromRegionX, int fromRegionY, int fromPlane, int toRegionX, int toRegionY, int toPlane, int rotation) {
		DynamicRegion toRegion = createDynamicRegion((((toRegionX / 8) << 8) + (toRegionY / 8)));
		int regionOffsetX = (toRegionX - ((toRegionX/8) * 8));
		int regionOffsetY = (toRegionY - ((toRegionY/8) * 8));
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][0] = fromRegionX;
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][1] = fromRegionY;
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][2] = fromPlane;
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][3] = rotation;
	}
	
	/*
	 * copy a exactly square of map from a place to another
	 */
	public static final void copyAllPlanesMap(int fromRegionX, int fromRegionY,
			int toRegionX, int toRegionY, int widthRegions, int heightRegions) {
		int[] planes = new int[4];
		for (int plane = 1; plane < 4; plane++)
			planes[plane] = plane;
		copyMap(fromRegionX, fromRegionY, toRegionX, toRegionY, widthRegions,
				heightRegions, planes, planes);
	}

	/*
	 * copy a exactly square of map from a place to another
	 */
	public static final void copyAllPlanesMap(int fromRegionX, int fromRegionY, int toRegionX, int toRegionY, int ratio) {
		int[] planes = new int[4];
		for(int plane = 1; plane < 4; plane++)
			planes[plane] = plane;
		copyMap(fromRegionX, fromRegionY, toRegionX, toRegionY, ratio, ratio, planes, planes);
	}
	
	/*
	 * copy a square of map from a place to another
	 */
	public static final void copyMap(int fromRegionX, int fromRegionY, int toRegionX, int toRegionY, int ratio, int[] fromPlanes, int[] toPlanes) {
		copyMap(fromRegionX, fromRegionY, toRegionX, toRegionY, ratio, ratio, fromPlanes, toPlanes);
	}
	
	/*
	 * copy a rectangle of map from a place to another
	 */
	public static final void copyMap(int fromRegionX, int fromRegionY, int toRegionX, int toRegionY, int widthRegions, int heightRegions, int[] fromPlanes, int[] toPlanes) {
		if(fromPlanes.length != toPlanes.length)
			throw new RuntimeException("PLANES LENGTH ISNT SAME OF THE NEW PLANES ORDER!");
		for(int xOffset = 0; xOffset < widthRegions; xOffset++) {
			for(int yOffset = 0; yOffset < heightRegions; yOffset++) {
				int fromThisRegionX = fromRegionX+xOffset;
				int fromThisRegionY = fromRegionY+yOffset;
				int toThisRegionX = toRegionX+xOffset;
				int toThisRegionY = toRegionY+yOffset;
				DynamicRegion toRegion = createDynamicRegion((((toThisRegionX / 8) << 8) + (toThisRegionY / 8)));
				int regionOffsetX = (toThisRegionX - ((toThisRegionX/8) * 8));
				int regionOffsetY = (toThisRegionY - ((toThisRegionY/8) * 8));
				for(int pIndex = 0; pIndex < fromPlanes.length; pIndex++) {
					int toPlane = toPlanes[pIndex];
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][0] = fromThisRegionX;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][1] = fromThisRegionY;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][2] = fromPlanes[pIndex];
				}
			}
		}
	}
	
	/*
	 * not recommended to use unless you want to make a more complex map
	 */
	public static DynamicRegion createDynamicRegion(int regionId) {
		Region region = World.getRegions().get(regionId);
		if(region != null) {
			if(region instanceof DynamicRegion) //if its already dynamic lets keep building it
				return (DynamicRegion) region;
		}
		DynamicRegion newRegion = new DynamicRegion(regionId);
		World.getRegions().put(regionId, newRegion);
		return newRegion;
	}	
	
	/*
	 * Safely destroys a dynamic region
	 */
	public static void destroyDynamicRegion(int regionId) {
		Region region = World.getRegions().get(regionId);
		if(region != null) {
			CopyOnWriteArrayList<Integer> playerIndexes = region.getPlayerIndexes();
			CopyOnWriteArrayList<Integer> npcIndexes = region.getNPCsIndexes();
			if(npcIndexes != null) {
				for(int npcIndex : npcIndexes) {
					NPC npc = World.getNPCs().get(npcIndex);
					if(npc == null)
						continue;
					if(npc.getRespawnTile().getRegionId() == regionId)
						World.removeNPC(npc);
					else
						npc.setNextWorldTile(new WorldTile(npc.getRespawnTile()));
				}
			}
			World.getRegions().remove(regionId);
			if(playerIndexes != null) {
				for(int playerIndex : playerIndexes) {
					Player player = World.getPlayers().get(playerIndex);
					if(player == null || !player.hasStarted() || player.hasFinished())
						continue;
					player.setForceNextMapLoadRefresh(true);
					player.loadMapRegions();
				}
			}
		}
	}
	
	private RegionBuilder() {
		
	}


	public static int[] findEmptyRegionBound(int widthChunks, int heightChunks) {
		int regionHash = findEmptyRegionHash(widthChunks, heightChunks);
		return new int[] {(regionHash >> 8), regionHash & 0xff};
	}
	
	public static int[] findEmptyChunkBound(int widthChunks, int heightChunks) {
		int[] map = findEmptyRegionBound(widthChunks, heightChunks);
		map[0] *= 8;
		map[1] *= 8;
		return map;
	}
	public static int getRegionHash(int chunkX, int chunkY) {
		return (chunkX << 8) + chunkY;
	}
	private static final Object ALGORITHM_LOCK = new Object();
	private static final List<Integer> EXISTING_MAPS = new ArrayList<Integer>();
	
	
	private static final int MAX_REGION_X = 127;
	private static final int MAX_REGION_Y = 255;

	public static int findEmptyRegionHash(int widthChunks, int heightChunks) {
		int regionsDistanceX = 1;
		while(widthChunks > 8) {
			regionsDistanceX += 1;
			widthChunks -= 8;
		}
		int regionsDistanceY = 1;
		while(heightChunks > 8) {
			regionsDistanceY += 1;
			heightChunks -= 8;
		}
		synchronized (ALGORITHM_LOCK) {
			for(int regionX = 1; regionX <= MAX_REGION_X - regionsDistanceX;  regionX++) { 
				skip: for(int regionY = 1; regionY <= MAX_REGION_Y - regionsDistanceY;  regionY++) { 
					int regionHash = getRegionHash(regionX, regionY); //map hash because skiping to next map up		
					for(int checkRegionX = regionX - 1; checkRegionX <= regionX + regionsDistanceX; checkRegionX++) {
						for(int checkRegionY = regionY - 1; checkRegionY <= regionY + regionsDistanceY; checkRegionY++) {
							int hash = getRegionHash(checkRegionX, checkRegionY);
							if(regionExists(hash)) 
								continue skip;
							
						}
					}
					reserveArea(regionX, regionY, regionsDistanceX, regionsDistanceY, false);
					return regionHash;
				}
			}
		}
		return -1;
		
	}
	public static void reserveArea(int fromRegionX, int fromRegionY, int width, int height, boolean remove) {
		for (int regionX = fromRegionX; regionX < fromRegionX + width;  regionX++) {
			for (int regionY = fromRegionY; regionY < fromRegionY + height;  regionY++) {
				if(remove)
						EXISTING_MAPS.remove((Integer)getRegionHash(regionX, regionY));
					else
						EXISTING_MAPS.add(getRegionHash(regionX, regionY));
			}
		}
	}
	
	
	public static boolean regionExists(int mapHash) {	
		return EXISTING_MAPS.contains(mapHash);
		
	}


	/*
	 * copys a single 8x8 map tile and allows you to rotate it
	 */
	public static void copyChunk(int fromChunkX, int fromChunkY,
			int fromPlane, int toChunkX, int toChunkY, int toPlane,
			int rotation) {
		DynamicRegion toRegion = createDynamicRegion((((toChunkX / 8) << 8) + (toChunkY / 8)));
		int regionOffsetX = (toChunkX - ((toChunkX / 8) * 8));
		int regionOffsetY = (toChunkY - ((toChunkY / 8) * 8));
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][0] = fromChunkX;
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][1] = fromChunkY;
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][2] = fromPlane;
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][3] = rotation;
		World.getRegion((((fromChunkY / 8) << 8) + (fromChunkX / 8)), true);
	}
	
}
