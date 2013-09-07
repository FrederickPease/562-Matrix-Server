package com.rs.game.minigames.soulwars;

import com.rs.game.Entity;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.game.player.content.Foods.Food;
import com.rs.game.player.content.Pots.Pot;
import com.rs.game.player.controlers.Controler;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

/**
 * 
 * @author Taylor Moon
 *
 */
public class SoulWars extends Controler {

	/** If players can join teams or not */
	private boolean teamPortalsOpen;

	/** If a game is currently in progress or not */
	protected boolean gameInProgress;

	/** Represents this soulwars engine */
	private static SoulwarsEngine engine;

	/** Represents the red team */
	private static Team redTeam;

	/** Represents the blue team */
	private static Team blueTeam;

	/** Nomads NPC id */
	public static final int NOMAD = 9427;

	/** The bandage item id */
	public static final int BANDAGE = 4049;

	/** The barricade item id */
	public static final int BARRICADE = 4053;

	/** The bones item id */
	public static final int BONES = 526;

	/**
	 * Starts this controler instance
	 */
	@Override
	public void start() {
		player.lock();
		player.setNextWorldTile(new WorldTile(1886, 3178, 0));
		player.setNextFaceWorldTile(new WorldTile(1885, 3177, 0));
		player.unlock();
	}

	/**
	 * Initiates soulwars world wide
	 */
	public static void init() {
		redTeam = Team.RED;
		blueTeam = Team.BLUE;
		engine = new SoulwarsEngine();
	}

	/**
	 * Joins a teams waiting lobby
	 */
	public void joinTeam(final Team team) {
		if (canJoinTeam() && !team.getWaiting().contains(player)
				&& team != null) {
			player.lock();
			if (player.getX() != (team == blueTeam ? 1880 : 1899)
					&& player.getY() != 3162) {
				WorldTile destTile = new WorldTile((team == blueTeam ? 1880
						: 1899), 3162, 0);
				player.setNextFaceWorldTile(destTile);
				player.addWalkSteps((team == blueTeam ? 1880 : 1899), 3162);
			}
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					player.stopAll();
					player.addWalkSteps((team == blueTeam ? 1878 : 1901), 3162);
				}

			});
			setCape(team.getCape());
			team.getWaiting().add(player);
			sendInterface(Stage.WAITING);
			player.unlock();
		}
	}
	
	/**
	 * Completely leaves the soulwars area
	 */
	public void leaveSoulwars() {
		getTeam(player).getPlaying().remove(player);
		getTeam(player).getWaiting().remove(player);
		WorldTile destTile = new WorldTile(3082, 2475, 0);
		player.setNextWorldTile(destTile);
		removeControler();
	}

	/**
	 * Leaves a waiting lobby
	 * 
	 * @param team
	 *            the team to leave
	 */
	public void leaveLobby(final Team team) {
		if (!team.getWaiting().contains(player)
				|| !team.getWaiting().contains(player))
			return;
		player.lock();
		if (player.getX() != (team == redTeam ? 1900 : 1879)
				&& player.getY() != 3162) {
			WorldTile destTile = new WorldTile((team == redTeam ? 1900 : 1879),
					3162, 0);
			player.setNextFaceWorldTile(destTile);
			player.addWalkSteps((team == redTeam ? 1900 : 1879), 3162);
		}
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.stopAll();
				player.addWalkSteps((team == blueTeam ? 1880 : 1899), 3162);
			}
		});
		setCape(null);
		team.getWaiting().remove(player);
		sendInterface(null);
		player.unlock();
	}

	/**
	 * Sets the players team cape, if the cape is null then the cape will be
	 * removed from the players back.
	 * 
	 * @param cape
	 *            - the cape to be set
	 */
	public boolean setCape(Item cape) {
		if (cape == null) {
			player.getEquipment().getItems()
					.set(Equipment.SLOT_CAPE, new Item(-1));
			player.getAppearence().generateAppearenceData();
			return false;
		}
		if (cape.getId() != 14642 || cape.getId() != 14641) {
			return false;
		}
		player.getEquipment().getItems().set(Equipment.SLOT_CAPE, cape);
		player.getEquipment().refresh(Equipment.SLOT_CAPE);
		player.getAppearence().generateAppearenceData();
		return true;
	}

	/**
	 * Sends the soulwars interface depending on it's stage
	 * 
	 * @param stage
	 *            - the interface stage
	 */
	public void sendInterface(Stage stage) {
		switch (stage) {
		case PLAYING:
			player.getInterfaceManager().sendTab(
					player.getInterfaceManager().hasRezizableScreen() ? 10 : 8,
					836);
			player.getPackets().sendIComponentText(
					836,
					9,
					""
							+ (blueTeam.getAvatarDeaths() == 0 ? "None"
									: blueTeam.getAvatarDeaths()));
			player.getPackets().sendIComponentText(836, 10, "100");
			player.getPackets().sendIComponentText(836, 11,
					"" + blueTeam.getAvatarHealth());
			player.getPackets().sendIComponentText(
					836,
					13,
					""
							+ (redTeam.getAvatarDeaths() == 0 ? "None"
									: redTeam.getAvatarDeaths()));
			player.getPackets().sendIComponentText(836, 14, "100");
			player.getPackets().sendIComponentText(836, 15,
					"" + redTeam.getAvatarHealth());
			player.getPackets().sendIComponentText(836, 27,
					"" + engine.calculateMinutesLeft());
			break;
		case WAITING:
			player.getInterfaceManager().sendTab(
					player.getInterfaceManager().hasRezizableScreen() ? 10 : 8,
					837);
			player.getPackets().sendIComponentText(837, 9,
					"" + engine.getTotalWaitTime());
			player.getPackets().sendIComponentText(837, 5,
					"" + redTeam.getWaiting().size());
			player.getPackets().sendIComponentText(837, 3,
					"" + blueTeam.getWaiting().size());
			break;
		default:
			player.getPackets().sendWindowsPane(
					player.getInterfaceManager().hasRezizableScreen() ? 746
							: 548, -1);
			break;

		}
	}

	/**
	 * Determines if a player can join a team or not
	 * 
	 * @return - true if said player can; false otherwise.
	 */
	public boolean canJoinTeam() {
		for (int i = 0; i < player.getInventory().getItems().getSize(); i++) {
			boolean isFood = player.getInventory().getItems().getItems()[i]
					.getDefinitions().containsOption("eat")
					|| player.getInventory().getItems().getItems()[i]
							.getDefinitions().containsOption("drink");
			if (player.getEquipment().getItem(Equipment.SLOT_CAPE) != null
					|| isFood) {
				sendDialogue("cantJoin", player);
				return false;
			}
		}
		if (!teamPortalsOpen)
			return false;
		return true;
	}

	/**
	 * If the next soulwars game is ready to be started
	 * 
	 * @return - true if it is, false otherwise
	 */
	public boolean readyToStart() {
		if (redTeam.getWaiting().size() < 5 || blueTeam.getWaiting().size() < 5
				|| gameInProgress)
			return false;
		return true;
	}

	/**
	 * Starts the next soulwars game
	 */
	public void startGame() {
		if (readyToStart()) {
			gameInProgress = true;
			try {
				// disabling lobby enter
				teamPortalsOpen = false;

				// getting all the teams

				for (Player allBlue : blueTeam.getWaiting()) {
					for (Player allRed : redTeam.getWaiting()) {

						// sending to spawn tile
						allBlue.setNextWorldTile(blueTeam.getSpawnBase());
						allRed.setNextWorldTile(redTeam.getSpawnBase());

						// adding the waiting list to the playing list
						blueTeam.getPlaying().addAll(blueTeam.getWaiting());
						redTeam.getPlaying().addAll(redTeam.getWaiting());
						blueTeam.getWaiting().clear();
						redTeam.getWaiting().clear();

						// enabling lobby join
						teamPortalsOpen = true;
					}
				}
			} finally {
				sendInterface(Stage.PLAYING);
				refreshConfigs();
				engine.startGameTimer();
				Dialogue.sendNPCDialogueNoContinue(player, NOMAD, 9427,
						"Now fight!");
			}
		}

	}

	/**
	 * Refreshes the configs for everyone in this game
	 */
	private void refreshConfigs() {
		player.getPackets().sendConfig(380, engine.calculateMinutesLeft());
		player.getPackets().sendConfig(145, blueTeam.getPoints());
		player.getPackets().sendConfig(155, redTeam.getPoints());

	}

	/**
	 * Ends the current game
	 */
	public void endCurrentGame() {
		if (gameInProgress = true)
			return;
		for (Player allRedTeam : redTeam.getPlaying()) {
			for (Player allBlueTeam : blueTeam.getPlaying()) {
				leaveCurrentGame(allRedTeam, false);
				leaveCurrentGame(allBlueTeam, false);
			}
		}
		gameInProgress = false;
	}

	/**
	 * Sends a player out of a current game
	 * 
	 * @param player
	 *            - the player leaving
	 * @param force
	 *            - if the player is being forced; or the game is ending
	 */
	public void leaveCurrentGame(Player player, boolean force) {
		player.lock();
		try {
			getTeam(player).getPlaying().remove(player);
			removeSupplies(-1);
			setCape(null);
			player.getAppearence().transformIntoNPC(-1);
			player.getAppearence().generateAppearenceData();
			player.reset();
			player.setNextWorldTile(getTeam(player).getEndBase());
			if (!force) {
				if (getTeam(player).getPoints() != -1)
					getTeam(player).setPoints(-1);
				if (getTeam(player).getAvatar() != null) {
					getTeam(player).removeAvatar();
				}
				assignZeals(getLastWinningTeam() == getTeam(player));
				sendDialogue("gameEnd", player);
			}
			player.setNextWorldTile(getTeam(player).getEndBase());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a specific type of dialogue depending on the stage
	 * 
	 * @param stage
	 *            - the stage of dialogue to be sent
	 * @param player
	 *            - the player for dialogue to be sent to
	 */
	public void sendDialogue(String stage, Player player) {
		switch (stage) {
		case "depart":
			Dialogue.sendNPCDialogueNoContinue(player, NOMAD, 9427, "todo");// TODO
			break;
		case "gameEnd":
			boolean won = getLastWinningTeam() == getTeam(player)
					|| getLastWinningTeam() == null;
			Dialogue.sendNPCDialogueNoContinue(
					player,
					NOMAD,
					9427,
					"The "
							+ (getLastWinningTeam() == Team.RED ? "<Col=ff0000>red</col>"
									: "<col=6655ff>blue</col>")
							+ " team was victorious! You are awarded "
							+ (won ? "10" : "1") + ""
							+ (won ? "zeals" : "zeal")
							+ "for your participation");
			break;
		case "cantJoin":
			Dialogue.sendNPCDialogueNoContinue(player, NOMAD, 9427,
					"You cant have food, potions, or be wearing a cape to enter the lobby!");
			break;
		case "cantLeave":
			Dialogue.sendNPCDialogueNoContinue(player, NOMAD, 9427,
					"You can't just leave like that, either fight or use the portal!");
			break;
		}
	}

	/**
	 * Returns the last winning team in this soulwars game
	 * 
	 * @return - the last winning team
	 */
	public Team getLastWinningTeam() {
		if (blueTeam.getPoints() == redTeam.getPoints()) {
			return null;
		}
		return blueTeam.getPoints() > redTeam.getPoints() ? blueTeam : redTeam;
	}

	/**
	 * Retruns the team of a player
	 * 
	 * @param player
	 *            - the player to get their team
	 * @return - the players team
	 */
	public Team getTeam(Player player) {
		if (!isOnTeam(player))
			return null;
		if (blueTeam.getWaiting().contains(player)
				|| blueTeam.getPlaying().contains(player)
				&& (redTeam.getWaiting().contains(player) || redTeam
						.getPlaying().contains(player)))
			return blueTeam;
		return redTeam;
	}

	/**
	 * Returns the players current stage
	 * 
	 * @param player
	 *            - the player to check
	 * @return - if the player is playing or waiting
	 */
	public Stage getStage(Player player) {
		return (getTeam(player).getWaiting().contains(player)
				&& !getTeam(player).getPlaying().contains(player) ? Stage.WAITING
				: Stage.PLAYING);
	}

	/**
	 * Checks if a specified player is on a team
	 * 
	 * @param player
	 *            - the player to check
	 * @return - if the player is on a team
	 */
	public boolean isOnTeam(Player player) {
		return redTeam.getWaiting().contains(player)
				|| redTeam.getPlaying().contains(player)
				&& (blueTeam.getWaiting().contains(player) || blueTeam
						.getPlaying().contains(player));

	}

	/**
	 * Removes this players supplies. Leave the arguments -1 if you want all the
	 * supplies including bones, bandages, and barricades to be remove from this
	 * players inventory
	 * 
	 * @param suppliesType
	 *            - the supplies to remove
	 */
	public void removeSupplies(int suppliesType) {
		for (Item item : player.getInventory().getItems().getItems()) {

			if (suppliesType == -1) {
				if (item.getId() != (BONES) || item.getId() != BARRICADE
						|| item.getId() != BANDAGE)
					continue;
				player.getInventory().deleteItem(item);
				return;
			}
			if (item.getId() != (suppliesType == BANDAGE ? BANDAGE
					: suppliesType == BARRICADE ? BARRICADE
							: suppliesType == BONES ? BONES : -1))
				continue;
			player.getInventory().deleteItem(item);
		}
	}

	/**
	 * Gives the player their zeals they earned from their last played game
	 * 
	 * @param won
	 *            if that player won
	 */
	public void assignZeals(boolean won) {
		if (!won && getLastWinningTeam() != null)
			player.zeals++;
		player.zeals += 10;

	}

	/**
	 * Checks if there is currently a game in progress
	 * 
	 * @return - if there is a game in progress
	 */
	public boolean gameInProgress() {
		return gameInProgress;
	}

	/**
	 * Processes an event once every game tick
	 */
	@Override
	public void process() {
		if (gameInProgress == false) {
			startGame();
		}
		if (player.getControlerManager().getControler() == null) {
			forceClose();
		}
	}

	/**
	 * Processes an event of a button click
	 */
	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		if (interfaceId == 387) {
			if (componentId == 9) {
				player.getPackets().sendGameMessage(
						"You can't remove your team's colors.");
				return false;
			}
		}
		return true;
	}

	/**
	 * Determines if a player can equip a specified item
	 */
	@Override
	public boolean canEquip(int slotId, int itemId) {
		if (slotId == Equipment.SLOT_CAPE) {
			player.getPackets().sendGameMessage(
					"You can't remove your team's colors.");
			return false;
		}
		return true;
	}

	/**
	 * Determines if the player can attack a specified target
	 */
	@Override
	public boolean canAttack(Entity target) {
		if (target instanceof Player && !canHit(target)) {
			player.getPackets().sendGameMessage("You can't attack your team.");
			return false;
		}
		return canHit(target);
	}

	/**
	 * Determines if a player can hit a specified target
	 */
	@Override
	public boolean canHit(Entity target) {
		if (target instanceof NPC)
			return true;
		if (((Player) target).getEquipment().getCapeId() == player
				.getEquipment().getCapeId())
			return false;
		return true;
	}

	/**
	 * Determines if a player can eat a specified food
	 */
	@Override
	public boolean canEat(Food food) {
		if (food.getId() != BANDAGE) {
			player.getPackets().sendGameMessage(
					"A magical force prevents you from eating this");
			return false;
		}
		return true;
	}

	/**
	 * Determines if a player can drink a specified potion
	 */
	@Override
	public boolean canPot(Pot pot) {
		player.getPackets().sendGameMessage(
				"A magical force prevents you from drinking this");
		return false;
	}

	/**
	 * Determines if a player can magic tele or not
	 */
	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		for (Player staff : World.getPlayers()) {
			if (staff.getRights() < 1)
				continue;
			if (toTile == staff.getLastWorldTile()) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"Oops, something went wrong!");
				leaveCurrentGame(player, true);
				return true;
			}
			sendDialogue("cantLeave", player);
			return false;
		}
		return false;
	}

	/**
	 * return can't teleport
	 */
	public boolean processItemTeleport(WorldTile toTile) {
		sendDialogue("cantLeave", player);
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		int id = object.getId();
		switch (id) {
		case 42021:
		case 42022:
			leaveCurrentGame(player, true);
			return false;
		case 42030:
			boolean leavingRed = (player.getX() >= 1900);
			if(leavingRed) {
				leaveLobby(getTeam(player));
			} else {
				joinTeam(redTeam);
			}
			return false;
		case 42029:
			boolean leavingBlue = (player.getX() <= 1879);
			if(leavingBlue) {
				leaveLobby(getTeam(player));
			} else {
				joinTeam(blueTeam);
			}
			return false;
		case 42018:
			boolean passingRed = (player.getX() <= 1958);
			if(passingRed) {
				player.setNextWorldTile(new WorldTile(1959, 3239, 0));
			} else {
				player.setNextWorldTile(new WorldTile(1958, 3239, 0));
			}
			return false;
		case 42015:
			boolean passingBlue = (player.getX() >= 1816);
			if(passingBlue) {
				player.setNextWorldTile(new WorldTile(1815, 3225, 0));
			} else {
				player.setNextWorldTile(new WorldTile(1816, 3225, 0));
			}
			return false;
		case 42220:
			leaveSoulwars();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @author Taylor Moon
	 * 
	 */
	enum Stage {
		WAITING, PLAYING
	}
}
