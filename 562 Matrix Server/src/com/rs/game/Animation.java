package com.rs.game;

public final class Animation {

	private int id, speed;
	private int[] ids;
	
	public Animation(int id) {
		this.id = id;
		
	}
	public Animation(int id, int speed) {
		this.id = id;
		this.speed = speed;
	}
	
	public int getId() {
		return id;
	}
	
	public int getSpeed() {
		return speed;
	}
	public int[] getIds() {
		return ids;
	}

}
