package com.rs.game.minigames.soulwars;

import java.util.ArrayList;

import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

public enum Team {

	BLUE(new Item(14642), new ArrayList<Player>(), new ArrayList<Player>(),
			new WorldTile(1886, 3171, 0), new WorldTile(1820, 3225, 0),
			new WorldTile(1880, 3162, 0), -1, -1, new WorldTile(1, 1, 1/** TODO */
			), null, 0/** TODO */
	),

	RED(new Item(14641), new ArrayList<Player>(), new ArrayList<Player>(),
			new WorldTile(1894, 3171, 0), new WorldTile(1954, 3239, 0),
			new WorldTile(1899, 3162, 0), -1, -1, new WorldTile(1, 1, 1/** TODO */
			), null, 0/** TODO */
	);

	private Item cape;
	private ArrayList<Player> playing;
	private ArrayList<Player> waiting;
	private WorldTile grave;
	private WorldTile spawnBase;
	private WorldTile endBase;
	private int points;
	private int avatarHealth;
	private WorldTile avatarSpawn;
	private NPC avatar;
	private int avatarDeaths;

	Team(Item cape, ArrayList<Player> playing, ArrayList<Player> waiting,
			WorldTile grave, WorldTile spawnBase, WorldTile endBase,
			int points, int avatarHealth, WorldTile avatarSpawn, NPC avatar, int avatarDeaths) {
		this.cape = cape;
		this.playing = playing;
		this.waiting = waiting;
		this.grave = grave;
		this.spawnBase = spawnBase;
		this.endBase = endBase;
		this.points = points;
		this.avatarHealth = avatarHealth;
		this.avatarSpawn = avatarSpawn;
		this.avatar = avatar;
	}

	public Item getCape() {
		return cape;
	}

	public ArrayList<Player> getWaiting() {
		return waiting;
	}

	public ArrayList<Player> getPlaying() {
		return playing;
	}

	public WorldTile getGraveSpawn() {
		return grave;
	}

	public WorldTile getSpawnBase() {
		return spawnBase;
	}

	public WorldTile getEndBase() {
		return endBase;
	}

	public int getPoints() {
		return points;
	}

	public int setPoints(int points) {
		this.points = points;
		return points;
	}

	public int addPoints(int add) {
		points += add;
		return points;
	}

	public int getAvatarHealth() {
		return avatarHealth;
	}

	public WorldTile getAvatarSpawn() {
		return avatarSpawn;
	}

	public NPC getAvatar() {
		return avatar;
	}

	public void removeAvatar() {
		avatar.setNextWorldTile(new WorldTile(-1, -1, 1));
		avatar.sendDeath(new Player("null"));
		avatar = null;
	}

	public int setAvatarHealth(int health) {
		avatarHealth = health;
		return avatarHealth;
	}

	public int addAvatarHealth(int health) {
		avatarHealth += health;
		return avatarHealth;
	}
	
	public int getAvatarDeaths() {
		return avatarDeaths;
	}
	
	public void setAvatarDeaths(int d) {
		avatarDeaths = d;
	}
	
	public void addAvatarDeaths(int plus) {
		avatarDeaths += plus;
	}

}
