package com.rs.net.decoders.handlers;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.npc.pet.Pet;
import com.rs.game.player.Equipment;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Firemaking;
import com.rs.game.player.actions.Summoning;
import com.rs.game.player.actions.Summoning.Pouches;

import com.rs.game.player.controlers.Barrows;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.game.player.actions.Cooking;
import com.rs.game.player.actions.Cooking.Cookables;

import com.rs.io.InputStream;
import com.rs.utils.Utils;

public class InventoryOptionsHandler {
	
	public static void handleItemOnItem(final Player player, InputStream stream) {
		final int interfaceIndex = stream.readShort();
		final int itemUsed = stream.readShortLE128() & 0xFFFF;
		final int usedWith = stream.readShortLE() & 0xFFFF;
		if (interfaceIndex != 149) {
			return;
		}
		Item usedWith1 = player.getInventory().getItem(usedWith);
		Item itemUsed1 = player.getInventory().getItem(itemUsed);
		if (Firemaking.isFiremaking(player, itemUsed1, usedWith1))
			return;
		if (UseWith(usedWith, itemUsed, usedWith, itemUsed)) {
			if (Firemaking.isFiremaking(player, itemUsed1, usedWith1))
				return;
		}
		if (UseWith(13736, 13746, usedWith, itemUsed)) {
			player.getInventory().deleteItem(13736, 1);
			player.getInventory().deleteItem(13746, 1);
			player.getInventory().addItem(13738, 1);
		}
		if (UseWith(13736, 13748, usedWith, itemUsed)) {
			player.getInventory().deleteItem(13736, 1);
			player.getInventory().deleteItem(13748, 1);
			player.getInventory().addItem(13740, 1);
		}
		if (UseWith(13736, 13750, usedWith, itemUsed)) {
			player.getInventory().deleteItem(13736, 1);
			player.getInventory().deleteItem(13750, 1);
			player.getInventory().addItem(13742, 1);
		}
		if (UseWith(13736, 13752, usedWith, itemUsed)) {
			player.getInventory().deleteItem(13736, 1);
			player.getInventory().deleteItem(13752, 1);
			player.getInventory().addItem(13744, 1);
		}
		System.out.println(player.getDisplayName() + " used item 1: "
				+ itemUsed + " (" + itemUsed1.getName() + ") on item 2: "
				+ usedWith + " (" + usedWith1.getName() + ")");
	}
	
	public static void handleItemObject(final Player player, InputStream stream) {
		final int y = stream.readShortLE() & 0xFFFF;
		final int objectId = stream.readShortLE() & 0xFFFF;
		final int interfaceId = stream.readShort() & 0xFFFF;
		final int itemId = stream.readShort128() & 0xFFFF;
		final int itemSlot = stream.readShort128() & 0xFFFF;
		final int x = stream.readShortLE() & 0xFFFF;
		final int slot = player.getInventory().lookupSlot(itemId);
		if (interfaceId != 149) {// Player is not in invy screen
			return;
		}
		if (!player.getInventory().containsOneItem(itemId)) {
			return;
		}
		final Item item = player.getInventory().getItem(itemSlot);
		if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
				|| player.isDead())
			return;
		long currentTime = Utils.currentTimeMillis();
		if (player.getLockDelay() >= currentTime
				|| player.getEmotesManager().getNextEmoteEnd() >= currentTime)
			return;
		final WorldTile tile = new WorldTile(x, y, player.getPlane());
		int regionId = tile.getRegionId();
		if (!player.getMapRegionsIds().contains(regionId))
			return;
		WorldObject mapObject = World.getRegion(regionId).getObject(objectId,
				tile);
		if (mapObject == null || mapObject.getId() != objectId)
			return;
		final WorldObject object = !player.isAtDynamicRegion() ? mapObject
				: new WorldObject(objectId, mapObject.getType(),

				mapObject.getRotation(), x, y, player.getPlane());
		// final int item = player.getInventory().lookupSlot(itemId);
		if (player.isDead()
				|| Utils.getInterfaceDefinitionsSize() <= interfaceId)
			return;
		if (player.getLockDelay() > Utils.currentTimeMillis())
			return;
		if (!player.getInterfaceManager().containsInterface(interfaceId))
			return;
		/*
		 * if (item == null || item.getId() != itemId) return;
		 */
		player.stopAll(false); // false
		final ObjectDefinitions objectDef = object.getDefinitions();
		// ObjectHandler.handleItemOnObject(player, object, interfaceId, item);
		if (UseWith(954, 3828, itemId, objectId)) { // warriror
			if (player.isKalphiteLairSetted())
				return;
			player.getInventory().deleteItem(954, 1);
			player.setKalphiteLair();
		} else if (UseWith(954, 3827, itemId, objectId)) {
			if (player.isKalphiteLairEntranceSetted())
				return;
			player.getInventory().deleteItem(954, 1);
			player.setKalphiteLairEntrance();
		} else if (objectDef.name.toLowerCase().contains("range")
				|| objectDef.name.toLowerCase().contains("stove")
				|| objectDef.name.toLowerCase().contains("cooking range")
				|| objectId == 2728) {
			Cookables cook = Cooking.isCookingSkill(item);
			if (cook != null) {
				player.getDialogueManager().startDialogue("CookingD", cook,
						object);
			}
		} else
			player.getPackets().sendGameMessage("Nothing interesting happens.");
		System.out.println("Item on object: " + object.getId());
		System.out.println("worked?.");
	}

	public static void handleItemOnPlayer(final Player player,
			final Player usedOn, final int itemId) {
		player.setCoordsEvent(new CoordsEvent(usedOn, new Runnable() {
			public void run() {
				player.faceEntity(usedOn);
				if (usedOn.getInterfaceManager().containsScreenInter()) {
					player.getPackets().sendGameMessage(
							usedOn.getDisplayName() + " is busy.");
					return;
				}
				/*
				 * if (!usedOn.hasAcceptAid()) {
				 * player.getPackets().sendGameMessage(usedOn.getDisplayName() +
				 * " doesn't want to accept your items."); return; }
				 */
				switch (itemId) {
				case 962:// Christmas cracker
					if (player.getInventory().getFreeSlots() < 3
							|| usedOn.getInventory().getFreeSlots() < 3) {
						player.getPackets()
								.sendGameMessage(
										(player.getInventory().getFreeSlots() < 3 ? "You do"
												: "The other player does")
												+ " not have enough inventory space to open this cracker.");
						return;
					}
					player.getDialogueManager().startDialogue(
							"ChristmasCrackerD", usedOn, itemId);
					break;
				default:
					player.getPackets().sendGameMessage(
							"Nothing interesting happens.");
					break;
				}
			}
		}, usedOn.getSize()));
	}

	public static void handleItemOption6(Player player, int slotId, int itemId,
			Item item) {
		long time = Utils.currentTimeMillis();
		if (player.getLockDelay() >= time
				|| player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		player.stopAll(false);
		Pouches pouches = Pouches.forId(itemId);
		if (pouches != null)
			Summoning.spawnFamiliar(player, pouches);
		/*
		 * else if (itemId == 1438) Runecrafting.locate(player, 3127, 3405);
		 * else if (itemId == 1440) Runecrafting.locate(player, 3306, 3474);
		 * else if (itemId == 1442) Runecrafting.locate(player, 3313, 3255);
		 * else if (itemId == 1444) Runecrafting.locate(player, 3185, 3165);
		 * else if (itemId == 1446) Runecrafting.locate(player, 3053, 3445);
		 * else if (itemId == 1448) Runecrafting.locate(player, 2982, 3514);
		 */
		else if (itemId <= 1712 && itemId >= 1706 || itemId >= 10354
				&& itemId <= 10362)
			player.getDialogueManager().startDialogue("Transportation",
					"Edgeville", new WorldTile(3087, 3496, 0), "Karamja",
					new WorldTile(2918, 3176, 0), "Draynor Village",
					new WorldTile(3105, 3251, 0), "Al Kharid",
					new WorldTile(3293, 3163, 0), itemId);
		else if (itemId == 1704 || itemId == 10352)
			player.getPackets()
					.sendGameMessage(
							"The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
		else if (itemId >= 3853 && itemId <= 3867)
			player.getDialogueManager().startDialogue("Transportation",
					"Burthrope Games Room", new WorldTile(2880, 3559, 0),
					"Barbarian Outpost", new WorldTile(2519, 3571, 0),
					"Gamers' Grotto", new WorldTile(2970, 9679, 0),
					"Corporeal Beast", new WorldTile(2886, 4377, 0), itemId);
	}

	public static void itemOperate(final Player player, InputStream stream) {
		// try {
		int interfaceSet = stream.readInt();
		int slotId = stream.readShort();
		int interfaceId = interfaceSet >> 16;
		int itemId = stream.readShortLE128();
		// int itemId = id;
		if (interfaceId == 387) {// 387
			if (slotId < 0 || slotId >= Equipment.SIZE
					|| player.getEquipment().getItems().get(slotId) == null) {
				return;
			}
			if (player.getEquipment().getItems().get(slotId).getId() != itemId) {
				return;
			}
			Item item = player.getInventory().getItem(itemId);
			if (item == null || item.getId() != itemId)
				return;
			Pouches pouches = Pouches.forId(itemId);
			if (pouches != null) {
				Summoning.spawnFamiliar(player, pouches);
			} else if (itemId <= 1712 && itemId >= 1706 || itemId >= 10354
					&& itemId <= 10362) {
				player.getDialogueManager().startDialogue("Transportation",
						"Edgeville", new WorldTile(3087, 3496, 0), "Karamja",
						new WorldTile(2918, 3176, 0), "Draynor Village",
						new WorldTile(3105, 3251, 0), "Al Kharid",
						new WorldTile(3293, 3163, 0), itemId);
			} else if (itemId == 1704 || itemId == 10352) {
				player.getPackets()
						.sendGameMessage(
								"The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
			} else if (itemId >= 3853 && itemId <= 3867) {
				player.getDialogueManager()
						.startDialogue("Transportation",
								"Burthrope Games Room",
								new WorldTile(2880, 3559, 0),
								"Barbarian Outpost",
								new WorldTile(2519, 3571, 0), "Gamers' Grotto",
								new WorldTile(2970, 9679, 0),
								"Corporeal Beast",
								new WorldTile(2886, 4377, 0), itemId);
			}
		} else
			player.getDialogueManager().startDialogue("Transportation",
					"Edgeville", new WorldTile(3087, 3496, 0), "Karamja",
					new WorldTile(2918, 3176, 0), "Draynor Village",
					new WorldTile(3105, 3251, 0), "Al Kharid",
					new WorldTile(3293, 3163, 0), itemId);
		// player.getPackets().sendGameMessage("Nothing interesting happens.");
	}

	@SuppressWarnings("unused")
	public static void itemsummon(final Player player, InputStream stream) {
		final int interfaceId = stream.readShort();
		final int junk = stream.readShort();
		final int id = stream.readShort();
		final int slot = stream.readShort128();
		int itemId = id;
		if (!player.getInventory().containsOneItem(id)) {
			return;
		}
		if (interfaceId == 149) {
			Pouches pouches = Pouches.forId(itemId);
			if (pouches != null)
				Summoning.spawnFamiliar(player, pouches);
			if (itemId <= 1712 && itemId >= 1706 || itemId >= 10354
					&& itemId <= 10362)
				player.getDialogueManager().startDialogue("Transportation",
						"Edgeville", new WorldTile(3087, 3496, 0), "Karamja",
						new WorldTile(2918, 3176, 0), "Draynor Village",
						new WorldTile(3105, 3251, 0), "Al Kharid",
						new WorldTile(3293, 3163, 0), itemId);
			if (itemId == 1704 || itemId == 10352)
				player.getPackets()
						.sendGameMessage(
								"The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
			if (itemId >= 3853 && itemId <= 3867)
				player.getDialogueManager()
						.startDialogue("Transportation",
								"Burthrope Games Room",
								new WorldTile(2880, 3559, 0),
								"Barbarian Outpost",
								new WorldTile(2519, 3571, 0), "Gamers' Grotto",
								new WorldTile(2970, 9679, 0),
								"Corporeal Beast",
								new WorldTile(2886, 4377, 0), itemId);

			switch (id) {
			}
		} else {
			System.out.println("Unhandled item, interface: " + interfaceId
					+ ".");
		}
	}

	private static boolean UseWith(int Item1, int Item2, int itemUsed,
			int usedWith) {
		if (itemUsed == Item1 && usedWith == Item2 || itemUsed == Item2
				&& usedWith == Item1) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unused")
	public static void itemOnNpc(final Player player, InputStream stream) {
		try {
			int slot = stream.readShort() & 0xFFFF;
			int junk = stream.readIntV1() & 0xFFFF;
			int junk2 = stream.readByte128() & 0xFFFF;
			int itemId = stream.readShortLE128() & 0xFFFF;
			int npcId = stream.readShortLE() & 0xFFFF;

			if (npcId < 0) {
				return;
			}
			NPC npc = World.getNPCs().get(npcId);
			if (npc == null) {
				return;
			}
			if (slot < 0) {
				return;
			}
			handleItemOnNPC(player, npc, player.getInventory().get(slot));
		} catch (Exception e) {
		}
	}

	/*
	 * returns the other
	 */

	public static void dig(final Player player) {
		player.resetWalkSteps();
		player.setNextAnimation(new Animation(830));
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.unlock();
				if (Barrows.digIntoGrave(player))
					return;
				if (player.getX() == 3005 && player.getY() == 3376
						|| player.getX() == 2999 && player.getY() == 3375
						|| player.getX() == 2996 && player.getY() == 3377
						|| player.getX() == 2989 && player.getY() == 3378
						|| player.getX() == 2987 && player.getY() == 3387
						|| player.getX() == 2984 && player.getY() == 3387) {
					// mole
					player.setNextWorldTile(new WorldTile(1752, 5137, 0));
					player.getPackets()
							.sendGameMessage(
									"You seem to have dropped down into a network of mole tunnels.");
					return;
				}
				player.getPackets().sendGameMessage("You find nothing.");
			}

		});
	}

	public static void handleItemOnNPC(final Player player, final NPC npc,
			final Item item) {
		if (item == null) {
			return;
		}
		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				if (!player.getInventory().containsItem(item.getId(),
						item.getAmount())) {
					return;
				}
				if (npc instanceof Pet) {
					player.faceEntity(npc);
					player.getPetManager().eat(item.getId(), (Pet) npc);
					return;
				}
				if (item.getId() == 962)
					player.sm("lol");
			}
		}, npc.getSize()));
	}

}
