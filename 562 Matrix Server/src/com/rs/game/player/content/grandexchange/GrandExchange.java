package com.rs.game.player.content.grandexchange;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cores.CoresManager;
import com.rs.utils.EconomyPrices;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

/**
 * 
 * @author Taylor Moon
 * 
 * @version Elixrr 2 | update 3
 */
public class GrandExchange implements Serializable {

	/**
	 * The serial ID
	 */
	private static final long serialVersionUID = 2569119971687708412L;
//
	/** The player instance */
	private Player player;

	/**
	 * Opens the grand exchange interface
	 */
	public void open() {
		player.getPackets().sendConfig(1112, -1);
		player.getPackets().sendConfig(1113, -1);
		player.getPackets().sendConfig(1109, -1);
		player.getPackets().sendConfig(1110, 0);
		player.getInterfaceManager().closeInventoryInterface();
		player.getInterfaceManager().sendInterface(105);
	}

	/**
	 * Handles interface buttons
	 * 
	 * @param componentId
	 *            The component ID
	 */
	public void handleButtons(int interfaceId, int slotId, int componentId,
			int packetId) {
		player.lock(2);
		switch (interfaceId) {
		case 105:
			switch (componentId) {
			case 19:
			case 35:
			case 51:
			case 70:
			case 89:
			case 108:
				int myBox = getBoxForComponent(componentId);
				if (myBox > -1) {
					player.getTemporaryAttributtes().put("box", myBox);
					if (packetId == 67) {
						abort(myBox,
								player.getTemporaryAttributtes().get("buying").equals(
										Boolean.FALSE));
						return;
					}
					openCollectionInterface(myBox,
							(player.getTemporaryAttributtes().get("buying")
									.equals(Boolean.FALSE)));
				}
				break;
			case 31:
			case 47:
			case 63:
			case 82:
			case 101:
			case 120:
				int buyBox = getBoxForComponent(componentId);
				if (buyBox > -1) {
					displayBuyInterface(buyBox);
					player.getPackets().sendConfig(1112, buyBox);
					player.getTemporaryAttributtes().put("box", buyBox);
				}
				break;
			case 32:
			case 48:
			case 64:
			case 83:
			case 102:
			case 121:
				int sellBox = getBoxForComponent(componentId);
				if (sellBox > -1) {
					displaySellInterface(sellBox);
					player.getTemporaryAttributtes().put("box", sellBox);
					player.getPackets().sendConfig(1112, sellBox);
				}
				break;
			case 155:
			case 157:
			case 160:
			case 162:
			case 164:
			case 166:
				setAmount(componentId);
				break;
			case 186:
				boolean buying = (boolean) player.getTemporaryAttributtes().get("buying");
				if (!buying) {
					player.getTemporaryAttributtes().remove("awaitingSellConfirm");
				}
				acceptOffer(buying);
				break;
			case 190:
				int box = (Integer) player.getTemporaryAttributtes().get("box");
				if (box > -1) {
					displayBuyInterface(box);
				}
				break;
			case 128:
				boolean buying1 = (boolean) player.getTemporaryAttributtes().get("buying");
				try {
					if (!buying1
							&& player.getTemporaryAttributtes().get("awaitingSellConfirm") != null) {
						player.getInventory().addItem(
								(Item) player
										.getTemporaryAttributtes().get("awaitingSellConfirm"));
						player.getTemporaryAttributtes().remove("awaitingSellConfirm");
					}
					open();
				} catch (NullPointerException e) {

				}
				break;
			case 200:
				abort((Integer) player.getTemporaryAttributtes().get("box"), player
						.getTemporaryAttributtes().get("buying").equals(Boolean.FALSE));
				break;
			}
			break;
		case 107:
			if ((Boolean) player.getTemporaryAttributtes().get("buying").equals(Boolean.TRUE))
				return;
			sellItem(player.getInventory().getItem(slotId).getId(), player
					.getInventory().getItem(slotId).getAmount());
			break;
		}
	}

	/**
	 * Accepts a GE offer
	 * 
	 * @param buying
	 *            If the offer is a buying offer
	 */
	public void acceptOffer(final boolean buying) {
		player.getInterfaceManager().closeChatBoxInterface();
		final int box = (Integer) player.getTemporaryAttributtes().get("box");
		final int itemId = (Integer) player.getTemporaryAttributtes().get("item");
		final int amount = (Integer) player.getTemporaryAttributtes().get("amount");
		final int price = (Integer) player.getTemporaryAttributtes().get("price");
		try {
			if (player.getTemporaryAttributtes().get("notEnough") != null) {
				player.getPackets().sendGameMessage("You do not have enough of this item to sell "
						+ Utils.formatTypicalInteger((int) player
								.getTemporaryAttributtes().get("notEnough")) + " of it.");
				return;
			}
		} catch (NullPointerException e) {

		}
		if (buying) {
			if (player.getInventory().getNumerOf(995) < price * amount) {
				player.getPackets().sendGameMessage("You do not have enough gold peice"
						+ (price == 1 ? "" : "s") + " to purchase "
						+ (amount > 1 ? "these items." : "this item."));
				return;
			} else {
				player.getInventory().deleteItem(995, price * amount);
			}
			player.getPackets().setGe(player, box, 1, itemId, price, amount, 0);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					int tr = Utils.random(amount);
					player.getPackets().setGe(player, box,
							tr >= amount ? 5 : 3, itemId, price, amount,
							tr >= amount ? amount : tr);
					player.offer[box] = new Offer(player, box, price, itemId,
							amount, GrandExchangeOfferType.SELLING_PROGRESS
									.getOpcode(), GrandExchangeType.BUYING,
							Calendar.getInstance().getTime());
					player.offer[box].setAmountTransacted(tr);
					player.offer[box].accept();
					stop();
				}
			}, 4);
		} else {
			if (player.getTemporaryAttributtes().get("note") != null) {
				player.getInventory().deleteItem(
						((int) player.getTemporaryAttributtes().get("note")), amount);
			} else {
				player.getInventory().deleteItem(itemId, amount);
			}
			player.getPackets().setGe(player, box, 9, itemId, price, amount, 0);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					
					/**int t = Utils.random(amount);
					player.getPackets().setGe(player, box,
							t >= amount ? 13 : 12, itemId, price, amount, t);
					player.offer[box] = new Offer(player, box, price, itemId,
							amount, GrandExchangeOfferType.SUBMITTING_BUY_OFFER
									.getOpcode(), GrandExchangeType.SELLING,
							Calendar.getInstance().getTime());
					player.offer[box].setAmountTransacted(t);
					player.offer[box].accept();
					stop();**/
				}
			}, 4);
		}
		open();
	}

	/**
	 * Sends the collection interface
	 * 
	 * @param slot
	 *            The GE slot
	 */
	public void openCollectionInterface(final int slot, final boolean selling) {
		player.getPackets().sendConfig(1112, slot);
		player.getPackets().sendItemOnIComponent(105, 143,
				player.offer[slot].getId(), 1);
		player.getInterfaceManager().closeChatBoxInterface();
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				int priceOffset = (Utils.random(EconomyPrices
						.getPrice(player.offer[slot].getId()) / 3) * player.offer[slot]
						.getAmount());
				if (player.offer[slot].getAmountTransacted() != 0)
					player.getPackets().sendItemOnIComponent(
							105,
							206,
							selling ? 995 : player.offer[slot].getId(),
							selling ? EconomyPrices.getPrice(player.offer[slot]
									.getId()) : player.offer[slot]
									.getAmountTransacted());
				stop();
				if (!selling)
					player.getPackets().sendItemOnIComponent(105, 208, 995,
							priceOffset);
				stop();
			}
		});
	}

	/**
	 * Causes an item to abort
	 * 
	 * @param slot
	 *            The slot to abort
	 */
	public void abort(final int slot, final boolean selling) {
		player.getInterfaceManager().closeChatBoxInterface();
		if (player.offer[slot].getAmountTransacted() >= player.offer[slot]
				.getAmount())
			return;
		player.getPackets().setGe(player, slot, -3, player.offer[slot].getId(),
				player.offer[slot].getPrice(), player.offer[slot].getAmount(),
				player.offer[slot].getAmountTransacted());
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.getPackets().sendItemOnIComponent(
						105,
						206,
						selling ? 995 : player.offer[slot].getId(),
						!selling ? player.offer[slot].getAmount()
								- player.offer[slot].getAmountTransacted()
								: EconomyPrices.getPrice(player.offer[slot]
										.getId()));
				if (!selling)
					player.getPackets()
							.sendItemOnIComponent(
									105,
									208,
									995,

									EconomyPrices.getPrice(player.offer[slot]
											.getId())
											* (player.offer[slot].getAmount() - player.offer[slot]
													.getAmountTransacted()));
				player.offer[slot].needsMoneyCollected();
				player.offer[slot].abort();
			}

		});
		player.getPackets().sendGameMessage("Abort request acknowledged. Please be aware that your offer may have already been completed.");
	}

	/**
	 * Processes the GE for this player
	 */
	public void process() {
		try {
			for (int i = 0; i < player.offer.length; i++) {
				if (player.offer[i] == null
						|| player.offer[i].isAborted()
						|| !player.offer[i].wasAccepted()
						|| !player.offer[i].waitingCollection()
						|| player.offer[i].getAmountTransacted() == player.offer[i]
								.getAmount())
					continue;
				final int id = player.offer[i].getId();
				final int amount = player.offer[i].getAmount();
				final int price = player.offer[i].getPrice();
				final int transacted = player.offer[i].getAmountTransacted();
				final int box = i;
				final boolean selling = player.getTemporaryAttributtes().get("buying").equals(
						Boolean.FALSE);
				if (player.offer[i].getAmountTransacted() >= player.offer[i]
						.getAmount() || amount == 1) {
					WorldTasksManager.schedule(new WorldTask() {

						@Override
						public void run() {
							player.getPackets().setGe(player, box, 5, id,
									price, amount, amount);
							player.offer[box].needsCollected();
							return;
						}
					});
				}
				if (Utils.random(23) == 6) {
					final int t = Utils.random(amount);
					if (t < amount || transacted != amount) {
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.getPackets().setGe(player, box, 3, id,
										price, amount, transacted + t);
								player.offer[box]
										.setAmountTransacted(player.offer[box]
												.getAmountTransacted() + t);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										player.getPackets()
												.sendItemOnIComponent(
														105,
														206,
														selling ? 995
																: player.offer[box]
																		.getId(),
														selling ? EconomyPrices
																.getPrice(player.offer[box]
																		.getId())
																: player.offer[box]
																		.getAmountTransacted());
									}
								});
							}
						});
					} else if (t >= amount
							|| player.offer[i].getAmountTransacted() >= amount) {
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.getPackets().setGe(player, box, 5, id,
										price, amount, amount);
								player.offer[box].needsCollected();
							}
						});
					}
				}
			}
		} catch (Exception e) {
			/* Empty */
		}
	}

	/**
	 * Creates an offer record
	 * 
	 * @param offer
	 *            The offer to record
	 */
	/**public void createRecord(Offer offer) {
		final ArrayList<String> lines = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					"Grand Exchange/" + player.getDisplayName()));
			String line1 = reader.readLine();
			if (line1 != null) {
				lines.add(line1);
				String line = "";
				while ((line = reader.readLine()) != null)
					lines.add(line);
			}
			reader.close();
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					"Grand Exchage/" + player.getDisplayName()));
			for (String s : lines)
				writer.write(s);
			writer.write(offer.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}**/

	/**
	 * Returns the offer
	 * 
	 * @param file
	 *            The file
	 * @param date
	 *            The date
	 * @return The corresponding offer
	 */
	/**@SuppressWarnings("unused")
	public Offer getOffer(File file, Date date) {// FIXME date search
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String[] line = reader.readLine().replace("x", " ").split(" ");
			int amount = Integer.parseInt(line[1]);
			I//tem item = Item.setId.parseInt(line[2]);
			// TODO finish this lolol.
			reader.close();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}**/

	/**
	 * Sets an offer
	 * 
	 * @param box
	 *            The box
	 * @param offer
	 *            The offer to set
	 */
	public void setOffer(int box, Offer offer) {
		this.player.offer[box] = offer;
	}

	/**
	 * Sells a GE item
	 * 
	 * @param id
	 *            The item id
	 * @param amount
	 *            The item amount
	 */
	public void sellItem(int id, int amount) {
		player.getInterfaceManager().closeChatBoxInterface();
		if (!canExchange(new Item(id))) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"That item is not tradeable.");
			return;
		}
		ItemDefinitions def = ItemDefinitions.getItemDefinitions(id);
		if (def == null)
			throw new NullPointerException("Nulled item definitions: " + id);
		if (def.isNoted()) {
			player.getTemporaryAttributtes().put("note", def.getId());
			id = def.getId() - 1;
			player.getInterfaceManager().closeInventoryInterface();
			sendPrice(id);
			setItem(def.getId() - 1, EconomyPrices.getPrice(id), player
					.getInventory().getItems().getNumberOf(def.getId()));
			player.getTemporaryAttributtes().put("itemAmount", player.getInventory()
					.getItems().getNumberOf(def.getId()));
			player.getInventory().deleteItem(id, amount);
			player.getTemporaryAttributtes().put("awaitingSellConfirm", new Item(id, amount));
		} else {
			amount = player.getInventory().getItems().getNumberOf(id);
			player.getInterfaceManager().closeInventoryInterface();
			sendPrice(id);
			setItem(id, EconomyPrices.getPrice(id), player.getInventory()
					.getItems().getNumberOf(def.getId()));
			player.getInventory().deleteItem(id, amount);
			player.getTemporaryAttributtes().put("itemAmount", amount);
			/*
			 * Prevents losing the item after walking away or closing the
			 * interface somehow
			 */
			player.getTemporaryAttributtes().put("awaitingSellConfirm", new Item(id, amount));
		}
	}

	/**
	 * Buys an item
	 * 
	 * @param id
	 *            The item id
	 */
	public void buyItem(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName()
				.toLowerCase();
		if (!canExchange(new Item(id)) || name.contains("partyhat")
				|| name.contains("disk of") || name.equalsIgnoreCase("pumpkin")
				|| name.contains("santa hat") || name.contains("easter egg")
				|| name.contains("h'ween")
				|| name.contains("christmas cracker")) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"That item is not tradeable.");
			return;
		}
		ItemDefinitions def = ItemDefinitions.getItemDefinitions(id);
		if (def == null) {
			return;
		}
		player.getInterfaceManager().closeInventoryInterface();
		sendPrice(id);
		setItem(id, EconomyPrices.getPrice(id), 1);
		final int box = (Integer)player.getTemporaryAttributtes().get("box");
		final int price = (Integer) player.getTemporaryAttributtes().get("price");
		player.offer[box] = new Offer(player, box, price, id, 1,
				GrandExchangeOfferType.SELLING_PROGRESS.getOpcode(),
				GrandExchangeType.BUYING, Calendar.getInstance().getTime());
	}

	/**
	 * Sends the items
	 * 
	 * @param items
	 *            The items to send
	 * @param box
	 *            The box
	 */
	public void sendItems(Item[] items, int box) {
		int key = getComponentForBox(box);
		player.getPackets().sendItems(-1, 0, key, items);
	}

	/**
	 * If a player can exchange a certain item
	 * 
	 * @param item
	 *            The item to exchange
	 * @return If the item is exchangable
	 */
	public boolean canExchange(Item item) {
		if (item.getDefinitions().containsOption("Check-charges")
				|| item.getDefinitions().containsOption("Check-state"))
			return false;
		if (item.getDefinitions().containsOption("Bind")
				|| item.getDefinitions().containsOption("Destroy")
				|| item.getDefinitions().containsOption("Change-colour")
				|| item.getDefinitions().containsOption("Customize"))
			return false;
		switch (item.getId()) {
		case 995:
			return false;
		default:
			return true;
		}
	}

	/**
	 * Returns the offer of a corresponding box
	 * 
	 * @param box
	 *            The box
	 * @return The offer
	 */
	public Offer getOffer(int box) {
		try {
			return player.offer[box];
		} catch (NullPointerException e) {

		}
		return null;
	}

	/**
	 * Returns the offers
	 * 
	 * @return The offers
	 */
	public Offer[] getOffers() {
		return player.offer;
	}

	/**
	 * Displays the default buy interface
	 * 
	 * @param box
	 *            The offer box
	 */
	public void displayBuyInterface(int box) {
		player.getTemporaryAttributtes().put("buying", Boolean.TRUE);
		Object[] o = new Object[] { "Grand Exchange Item Search" };
		player.getPackets().sendConfig(1113, 0);
		player.getPackets().setGeSearch(o);
	}

	/**
	 * Displays the default sell interface
	 * 
	 * @param player
	 *            The player
	 * @param offerBox
	 *            The offer box
	 */
	public void displaySellInterface(int offerBox) {
		reset();
		player.getTemporaryAttributtes().put("buying", Boolean.FALSE);
		player.getPackets().sendConfig(1113, 1);
		player.getInterfaceManager().sendInventoryInterface(107);
		player.getPackets().sendItems(93, player.getInventory().getItems());
		player.getPackets().sendUnlockIComponentOptionSlots(107, 0, 0, 27, 0);
		player.getPackets().sendInterSetItemsOptionsScript(107, 0, 93, 4, 7,
				"Offer");
		player.getInterfaceManager().closeChatBoxInterface();
		player.getPackets().sendHideIComponent(105, 196, true);
	}

	/**
	 * Sets the item constants
	 * 
	 * @param id
	 *            The ID
	 * @param price
	 *            The price
	 * @param amount
	 *            The amount
	 */
	public void setItem(int id, int price, int amount) {
		player.getPackets().sendConfig(1109, id);
		player.getPackets().sendConfig(1110, amount);
		player.getPackets().sendConfig(1111, price);
		player.getTemporaryAttributtes().put("item", id);
		player.getTemporaryAttributtes().put("price", price);
		player.getTemporaryAttributtes().put("amount", amount);
	}

	/**
	 * Sends the price settings for an item
	 * 
	 * @param id
	 *            The item id
	 */
	public void sendPrice(int id) {
		player.getPackets().sendConfig(1111, EconomyPrices.getPrice(id));
		player.getPackets().sendConfig(1115, EconomyPrices.getPrice(id));
		player.getPackets().sendConfig(1114, EconomyPrices.getPrice(id));
		player.getPackets().sendConfig(1116, EconomyPrices.getPrice(id));
	}

	/**
	 * Resets the GE boxes
	 */
	public void reset() {
		player.getPackets().sendConfig(1109, -1);
		player.getPackets().sendConfig(1110, 0);
		player.getPackets().sendConfig(1111, 0);
		player.getPackets().sendConfig(1112, -1);
		player.getPackets().sendConfig(1113, 0);
		player.getTemporaryAttributtes().remove("box");
		player.getTemporaryAttributtes().remove("item");
		player.getTemporaryAttributtes().remove("price");
		player.getTemporaryAttributtes().remove("amount");
	}

	/**
	 * Sets the item amount
	 * 
	 * @param button
	 *            The button that's being clicked
	 */
	public void setAmount(int button) {
		int amount = (Integer) player.getTemporaryAttributtes().get("amount");
		int itemId = (Integer) player.getTemporaryAttributtes().get("item");
		ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
		boolean buying = (Boolean) player.getTemporaryAttributtes().get("buying");
		switch (button) {
		case 155:
			if (amount > 1) {
				amount--;
			} else {
				amount = 1;
			}
			break;
		case 157:
			if (amount < Integer.MAX_VALUE) {
				amount++;
			} else {
				amount = 1;
			}
			break;
		case 160:
			if (buying) {
				if (player.getInventory().getNumerOf(itemId) <= amount
						&& !buying) {
					return;
				}
				amount += 1;
			} else {
				amount = 1;
			}
			break;
		case 162:
			if (buying) {
				amount += 10;
			} else {
				if (player.getInventory().getNumerOf(itemId) > 10) {
					amount = (int) player.getTemporaryAttributtes().get("amount");
				} else {
					amount = 10;
				}
			}
			break;
		case 164:
			if (buying) {
				amount += 100;
			} else {
				if (defs.isNoted()) {
					if (player.getInventory().getItems()
							.getNumberOf(defs.getCertId()) <= 100) {
						amount = (int) player.getTemporaryAttributtes().get("amount");
					} else {
						amount = 100;
					}
				} else if (player.getInventory().getItems().getNumberOf(itemId) <= 100) {
					amount = (int) player.getTemporaryAttributtes().get("amount");
				} else {
					amount = 100;
				}
			}
			break;
		case 166:
			if (buying) {
				amount += 1000;
			} else {
				amount = (int) player.getTemporaryAttributtes().get("amount");
			}
			break;
		}
		player.getTemporaryAttributtes().put("amount", amount);
		player.getPackets().sendConfig(1110, amount);
	}

	/**
	 * Returns the button corresponding to a box
	 * 
	 * @param box
	 *            The box
	 * @return The corresponding button ID
	 */
	public int getComponentForBox(int box) {
		switch (box) {
		case 0:
			return 523;
		case 1:
			return 524;
		case 2:
			return 525;
		case 3:
			return 526;
		case 4:
			return 527;
		case 5:
			return 528;
		}
		return -1;
	}

	/**
	 * Returns the box corresponding to the button
	 * 
	 * @param button
	 *            The button being clicked
	 * @return The box
	 */
	public int getBoxForComponent(int button) {
		switch (button) {
		case 19:
		case 31:
		case 32:
			return 0;
		case 35:
		case 48:
		case 47:
			return 1;
		case 51:
		case 63:
		case 64:
			return 2;
		case 70:
		case 82:
		case 83:
			return 3;
		case 89:
		case 101:
		case 102:
			return 4;
		case 108:
		case 120:
		case 121:
			return 5;
		}
		return -1;
	}

	/**
	 * Sets the player
	 * 
	 * @param player
	 *            The player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
}