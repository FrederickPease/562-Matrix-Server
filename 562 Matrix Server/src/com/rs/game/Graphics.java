package com.rs.game;

public final class Graphics {

	private int id, height, speed;
	
	public Graphics(int id) {
		this.id = id;
		
	}
	public Graphics(int id, int speed, int height) {
		this.id = id;
		this.speed = speed;
		this.height = height;
	}
	
	public int getId() {
		return id;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getHeight() {
		return height;
	}
}
