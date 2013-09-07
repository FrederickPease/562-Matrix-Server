package com.rs.game;

import com.rs.utils.Logger;

public class DynamicRegion extends Region {

	//int dynamicregion squares amt
	//Region[] array with the region squares
	//int[][] squaresBounds;
	
	private int[][][][] regionCoords;
	
	public DynamicRegion(int regionId) {
		super(regionId);
		//plane,x,y,(real x, real y,or real plane coord, or rotation)
		regionCoords = new int[4][8][8][4];
	}

	@Override
	public void checkLoadMap() {
		
	}
	
	/*
	 * gets the real tile objects from real region
	 */
	@Override
	public WorldObject[] getObjects(int plane, int localX, int localY) {
		int xoffset = localX/8;
		int yoffset = localY/8;
		int rotation = regionCoords[plane][xoffset][yoffset][3];
		if(rotation != 0)
			return null;
		int realRegionX = regionCoords[plane][xoffset][yoffset][0]; 
		int realRegionY = regionCoords[plane][xoffset][yoffset][1];
		if(realRegionX == 0|| realRegionY == 0)
			return null;
		int realRegionId = (((realRegionX / 8) << 8) + (realRegionY / 8));
		Region region = World.getRegion(realRegionId);
		if(region instanceof DynamicRegion) {
			Logger.log(this, "YOU CANT MAKE A REAL MAP AREA INTO A DYNAMIC REGION!, IT MAY DEADLOCK!");
			return null; //no information so that data not loaded
		}
		int coordX = ((realRegionId >> 8) * 64) + localX;
		int coordY = ((realRegionId & 0xff) * 64) + localY;
		int regionX = coordX >> 3;
		int regionY = coordY >> 3;
		int regionOffsetX = (regionX - ((regionX/8) * 8));
		int regionOffsetY = (regionY - ((regionY/8) * 8));
		int realRegionOffsetX = (realRegionX - ((realRegionX/8) * 8));
		int realRegionOffsetY = (realRegionY - ((realRegionY/8) * 8));
		int realLocalX = (realRegionOffsetX * 8) + (localX - (regionOffsetX*8));
		int realLocalY = (realRegionOffsetY * 8) + (localY - (regionOffsetY*8));
		return region.getObjects(plane, realLocalX, realLocalY);
	}
	/*
	 * gets clip data from the original region part
	 */
	
	@Override
	public int getMask(int plane, int localX, int localY) {
		int xoffset = localX/8;
		int yoffset = localY/8;
		int rotation = regionCoords[plane][xoffset][yoffset][3];
		if(rotation != 0)
			return 0;
		int realRegionX = regionCoords[plane][xoffset][yoffset][0]; 
		int realRegionY = regionCoords[plane][xoffset][yoffset][1];
		if(realRegionX == 0|| realRegionY == 0)
			return -1;
		int realRegionId = (((realRegionX / 8) << 8) + (realRegionY / 8));
		Region region = World.getRegion(realRegionId);
		if(region instanceof DynamicRegion) {
			Logger.log(this, "YOU CANT MAKE A REAL MAP AREA INTO A DYNAMIC REGION!, IT MAY DEADLOCK!");
			return -1; //cliped, no information so that data not loaded
		}
		int coordX = ((realRegionId >> 8) * 64) + localX;
		int coordY = ((realRegionId & 0xff) * 64) + localY;
		int regionX = coordX >> 3;
		int regionY = coordY >> 3;
		int regionOffsetX = (regionX - ((regionX/8) * 8));
		int regionOffsetY = (regionY - ((regionY/8) * 8));
		int realRegionOffsetX = (realRegionX - ((realRegionX/8) * 8));
		int realRegionOffsetY = (realRegionY - ((realRegionY/8) * 8));
		int realLocalX = (realRegionOffsetX * 8) + (localX - (regionOffsetX*8));
		int realLocalY = (realRegionOffsetY * 8) + (localY - (regionOffsetY*8));
		return region.getMask(regionCoords[plane][xoffset][yoffset][2], realLocalX, realLocalY);
	}

	public int[][][][] getRegionCoords() {
		return regionCoords;
	}

	public void setRegionCoords(int[][][][] regionCoords) {
		this.regionCoords = regionCoords;
	}
}
