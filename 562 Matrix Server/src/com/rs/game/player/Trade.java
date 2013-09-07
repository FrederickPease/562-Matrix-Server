package com.rs.game.player;

import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Player;
import com.rs.game.player.content.ItemConstants;
import com.rs.utils.EconomyPrices;
import com.rs.utils.ItemExamines;

public class Trade {

	private Player player, target;
	private ItemsContainer<Item> items;
	private boolean tradeModified;
	private boolean accepted;

	public Trade(Player player) {
		this.player = player; //player reference
		items = new ItemsContainer<Item>(28, false);
	}

	/*
	 * called to both players
	 */
	public void openTrade(Player target) {
		synchronized (this) {
			synchronized (target.getTrade()) {
				this.target = target;
				player.getPackets().sendIComponentText(335, 15, "Trading With: "+target.getDisplayName());
				//player.getPackets().sendIComponentText(203, target.getDisplayName());
				sendInterItems();
				sendOptions();
				sendTradeModified();
				refreshFreeInventorySlots();
				refreshTradeWealth();
				refreshStageMessage(true);
				player.getInterfaceManager().sendInterface(335);
				player.getInterfaceManager().sendInventoryInterface(336);
				player.setCloseInterfacesEvent(new Runnable() {
					@Override
					public void run() {
						closeTrade(CloseTradeStage.CANCEL);
					}
				});
			}
		}
	}


	public void removeItem(final int slot, int amount) {
		synchronized (this) {
			if(!isTrading())
				return;
			synchronized (target.getTrade()) {
				Item item = items.get(slot);
				if (item == null)
					return;
				Item[] itemsBefore = items.getItemsCopy();
				int maxAmount = items.getNumberOf(item);
				if (amount < maxAmount)
					item = new Item(item.getId(), amount);
				else
					item = new Item(item.getId(), maxAmount);
				items.remove(slot, item);
				player.getInventory().addItem(item);
				refreshItems(itemsBefore);
				cancelAccepted();
				setTradeModified(true);
			}
		}
	}

	public void sendFlash(int slot) {
		player.getPackets().sendInterFlashScript(335, 33, 4, 7, slot);
		target.getPackets().sendInterFlashScript(335, 36, 4, 7, slot);
	}
	

	public void cancelAccepted() {
		boolean canceled = false;
		if(accepted) {
			accepted = false;
			canceled = true;
		}
		if(target.getTrade().accepted) {
			target.getTrade().accepted = false;
			canceled = true;
		}
		if(canceled)
			refreshBothStageMessage(canceled);
	}

	public void addItem(int slot, int amount) {
		synchronized (this) {
			if(!isTrading())
				return;
			synchronized (target.getTrade()) {
				Item item = player.getInventory().getItem(slot);
				if (item == null)
					return;
				if (!ItemConstants.isTradeable(item)) {
					player.getPackets().sendGameMessage("That item isn't tradeable.");
					return;
				}
				Item[] itemsBefore = items.getItemsCopy();
				int maxAmount = player.getInventory().getItems().getNumberOf(item);
				if (amount < maxAmount)
					item = new Item(item.getId(), amount);
				else
					item = new Item(item.getId(), maxAmount);
				items.add(item);
				player.getInventory().deleteItem(slot, item);
				refreshItems(itemsBefore);
				cancelAccepted();
			}
		}
	}

	public void refreshItems(Item[] itemsBefore) {
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			Item item = items.getItems()[index];
			if (itemsBefore[index] != item)  {
				if(itemsBefore[index] != null && (item == null || item.getId() != itemsBefore[index].getId() || item.getAmount() < itemsBefore[index].getAmount()))
					sendFlash(index);
				changedSlots[count++] = index;
			}
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
		refreshFreeInventorySlots();
		refreshTradeWealth();
	}

	public void sendOptions() {
		player.getPackets().sendInterSetItemsOptionsScript(335, 30, 90, 4, 7,
				"Remove", "Remove-5", "Remove-10", "Remove-All", "Remove-X",
				"Value");
		player.getPackets().sendIComponentSettings(335, 30, 0, 27, 1150);
		Object[] tparams3 = new Object[] { "", "", "", "", "", "", "", "",
				"Value<col=FF9040>", -1, 0, 7, 4, 90, 21954593 };
		player.getPackets().sendRunScript(695, tparams3);
		player.getPackets().sendIComponentSettings(335, 33, 0, 27, 1026);
		player.getPackets().sendInterSetItemsOptionsScript(336, 0, 93, 4, 7,
				"Offer", "Offer-5", "Offer-10", "Offer-All", "Offer-X",
				"Value<col=FF9040>", "Lend");
		// player.getPackets().sendIComponentSettings(335, 0, 0, 27, 1278);
		player.getPackets().sendIComponentSettings(335, 87, -1, -1, 1026);
		player.getPackets().sendIComponentSettings(335, 88, -1, -1, 1030);
		player.getPackets().sendIComponentSettings(335, 83, -1, -1, 1024);
		player.getPackets().sendIComponentSettings(336, 0, 0, 27, 1278);
		player.getPackets().sendHideIComponent(335, 74, false);
		player.getPackets().sendHideIComponent(335, 75, false);

	}

	public boolean isTrading() {
		return target != null;
	}

	public void setTradeModified(boolean modified) {
		if(modified == tradeModified)
			return;
		tradeModified = modified;
		sendTradeModified();
	}

	public void sendInterItems11() {
		player.getPackets().sendItems(-1, 2, 90, items);
		target.getPackets().sendItems(-1, 2, 90, items);//tar
		
	player.getPackets().sendItems(-2, 60981, 90, items);
		target.getPackets().sendItems(-2, 60981, 90, items);//tar
		
		/*player.getPackets().sendItems(90, items);
		target.getPackets().sendItems(90, items, true);*/
	}

	public void refresh11(int... slots) {
/*	player.getPackets().sendUpdateItems(90, items, slots);
		target.getPackets().sendUpdateItems(90, items.getItems(), slots);*/

		player.getPackets().sendUpdateItems(-1, 2, 90, items, slots);
		player.getPackets().sendUpdateItems(-2, 60981, 90, items, slots);

		target.getPackets().sendUpdateItems(-1, 2, 90, items.getItems(), slots);
		target.getPackets().sendUpdateItems(-2, 60981, 90, items.getItems(), slots);

	}
	
	public void sendInterItems() {
		player.getPackets().sendItems(-1, 0, 90, items);
		target.getPackets().sendItems(-2, 0, 90, items);
	}

	public void refresh(int... slots) {
		player.getPackets().sendUpdateItems(-1, 0, 90, items, slots);
		target.getPackets().sendUpdateItems(-2, 0, 90, items, slots);

	}

	public void accept(boolean firstStage) {
		synchronized (this) {
			if(!isTrading())
				return;
			synchronized (target.getTrade()) {
				if(target.getTrade().accepted) {
					if(firstStage) {
						if(nextStage())
							target.getTrade().nextStage();
					}else{
						player.setCloseInterfacesEvent(null);
						player.closeInterfaces();
						closeTrade(CloseTradeStage.DONE);
					}
					return;
				}
				accepted = true;
				refreshBothStageMessage(firstStage);
			}
		}
	}

	public void sendValue(int slot, boolean traders) {
		if(!isTrading())
			return;
		Item item = traders ? target.getTrade().items.get(slot) : items.get(slot);
		if (item == null)
			return;
		if (!ItemConstants.isTradeable(item)) {
			player.getPackets().sendGameMessage("That item isn't tradeable.");
			return;
		}
		int price = EconomyPrices.getPrice(item.getId());
		player.getPackets().sendGameMessage(item.getDefinitions().getName()+": market price is "+price+" coins.");
	}

	public void sendValue(int slot) {
		Item item = player.getInventory().getItem(slot);
		if (item == null)
			return;
		if (!ItemConstants.isTradeable(item)) {
			player.getPackets().sendGameMessage("That item isn't tradeable.");
			return;
		}
		int price = EconomyPrices.getPrice(item.getId());
		player.getPackets().sendGameMessage(item.getDefinitions().getName()+": market price is "+price+" coins.");
	}

	public void sendExamine(int slot, boolean traders) {
		if(!isTrading())
			return;
		Item item = traders ? target.getTrade().items.get(slot) : items.get(slot);
		if (item == null)
			return;
		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	public boolean nextStage() {
		if(!isTrading())
			return false;
		if(player.getInventory().getItems().getUsedSlots() + target.getTrade().items.getUsedSlots() > 28) {
			player.setCloseInterfacesEvent(null);
			player.closeInterfaces();
			closeTrade(CloseTradeStage.NO_SPACE);
			return false;
		}
		accepted = false;
		player.getInterfaceManager().sendInterface(334);
		player.getInterfaceManager().closeInventoryInterface();
		//writeOffers();
		target.getPackets().sendIComponentText(334, 34, "Are you sure you want to make this trade?");
		//player.getPackets().sendHideIComponent(334, 55, !(tradeModified || target.getTrade().tradeModified));
		refreshBothStageMessage(false);
		return true;
	}

	public void refreshBothStageMessage(boolean firstStage) {
		refreshStageMessage(firstStage);
		target.getTrade().refreshStageMessage(firstStage);
	}


	public void refreshStageMessage(boolean firstStage) {
		player.getPackets().sendIComponentText(firstStage ? 335 : 334, firstStage ? 37 : 34, getAcceptMessage(firstStage));
	}

	public String getAcceptMessage(boolean firstStage) {
		if(accepted)
			return "Waiting for other player...";
		if(target.getTrade().accepted)
			return "Other player has accepted.";
		return firstStage ? "" : "Are you sure you want to make this trade?";
	}

	public void sendTradeModified() {
		player.getPackets().sendConfig(1042, tradeModified ? 1 : 0);
		target.getPackets().sendConfig(1043, tradeModified ? 1 : 0);
	}

	public void refreshTradeWealth() {
		int wealth = getTradeWealth();
		player.getPackets().sendConfig(729, wealth);
		target.getPackets().sendConfig(697, wealth);
	}

	public void refreshFreeInventorySlots() {
		int freeSlots = player.getInventory().getFreeSlots();
		target.getPackets().sendIComponentText(335, 21, "has "+(freeSlots == 0 ? "no" : freeSlots)+" free"
				+"<br>inventory slots");
	}

	public int getTradeWealth() {
		int wealth = 0;
		for(Item item : items.getItems()) {
			if(item == null)
				continue;
			wealth += EconomyPrices.getPrice(item.getId()) * item.getAmount();
		}
		return wealth;
	}
	

	public void writeOffers() {
		//int mySize = player.getTrade().items.getSize();
		//int otherSize = target.getTrade().items.getSize();
		ItemsContainer<Item> offer = items;
		ItemsContainer<Item> other = offer;
		//	target.getPackets().sendHideIComponent(334, 41, false);
			//target.getPackets().sendHideIComponent(334, 42, true);
		//	target.getPackets().sendHideIComponent(334, 43, true);
			target.getPackets()
			 .sendIComponentText(334, 41, buildString(other));
			 player.getPackets()
			 .sendIComponentText(334, 41, buildString(offer));

	}
	
	public String buildString(ItemsContainer<Item> offer) {
		String a = "";
		if (offer.getSize() > 0) {
			for (int i = 0; i < offer.getSize(); i++) {
				if (offer.get(i) == null)
					continue;

				a = a + "<col=FF9040>"
						+ offer.get(i).getDefinitions().getName();
				if (offer.get(i).getAmount() > 1) {
					a = a + "<col=FFFFFF> x ";
					a = a + "<col=FFFFFF>"
							+ Integer.toString(offer.get(i).getAmount())
							+ "<br>";
				} else {
					a = a + "<br>";
				}
			}
		} else {
			a = "<col=FFFFFF>Absolutely nothing!";
		}
		return a;
	}

	private static enum CloseTradeStage {
		CANCEL, NO_SPACE, DONE
	}

	public void closeTrade(CloseTradeStage stage) {
		synchronized (this) {
			synchronized (target.getTrade()) {
				Player oldTarget = target;
				target = null;
				tradeModified = false;
				accepted = false;
				if(CloseTradeStage.DONE != stage) {
					player.getInventory().getItems().addAll(items);
					player.getInventory().init();
					items.clear();
				} else {
					player.getPackets().sendGameMessage("Accepted trade.");
					player.getInventory().getItems().addAll(oldTarget.getTrade().items);
					player.getInventory().init();
					oldTarget.getTrade().items.clear();
				}
				if(oldTarget.getTrade().isTrading()) {
					oldTarget.setCloseInterfacesEvent(null);
					oldTarget.closeInterfaces();
					oldTarget.getTrade().closeTrade(stage);
					if(CloseTradeStage.CANCEL == stage)
						oldTarget.getPackets().sendGameMessage("<col=ff0000>Other player declined trade!");
					else if (CloseTradeStage.NO_SPACE == stage) {
						player.getPackets().sendGameMessage("You don't have enough space in your inventory for this trade.");
						oldTarget.getPackets().sendGameMessage("Other player doesn't have enough space in their inventory for this trade.");
					}
				}
			}
		}
	}

}
