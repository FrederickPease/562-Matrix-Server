package com.rs.game.player.dialogues;



public class collectItems extends Dialogue {

	/**
	 * Collect the item in Grand Exchange dialogue
	 */

	@Override
	public void start() {
	//	sendOptionsDialogue("Collect your items", "Nothing...")
				;
	}

	@Override
	public void run(int interfaceId, int componentId) {
	/**	if (componentId == OPTION_1) {
			Offer offer = (Offer) parameters[0]; 
			if(offer.id.equals(player.getUsername()))  {
				if(player.getInventory().getFreeSlots() > 0) {
					// 15 - 7 = 8
					int amount = offer.amount - offer.currentAmount;
					int cAmount = offer.currentAmount;
					boolean canTake = amount == offer.removedAmount;
					//player.getPackets().sendGameMessage("amount="+amount+", canTake="+canTake);
					int itemId = amount > 1 && !ItemDefinitions.getItemDefinitions(offer.item).isStackable() ? (offer.item + 1) : offer.item;
					int itemId2 = cAmount > 1 && !ItemDefinitions.getItemDefinitions(offer.item).isStackable() ? (offer.item + 1) : offer.item;
					System.out.println("Offer completed:"+offer.completed+", Offer aborted:"+offer.aborted+"");
					if(offer.aborted) {
						if(offer.type == 1) {
							if(!canTake) {
								if(!(player.getInventory().getFreeSlots() > 1)) {
									player.getPackets().sendGameMessage("You don't enough inventory space to collect the items.");
									end();
									return;
								}
								player.getInventory().addItem(itemId, amount);	
							}
							player.getInventory().addItem(995, offer.price * cAmount);
							player.getInventory().refresh();
						} else if(offer.type == 0) {
							if(!canTake) {
								if(!(player.getInventory().getFreeSlots() > 1)) {
									player.getPackets().sendGameMessage("You don't enough inventory space to collect the items.");
									end();
									return;
								}
								player.getInventory().addItem(995, offer.price * amount);	
							}
							player.getInventory().addItem(itemId2, cAmount);
							player.getInventory().refresh();		
						}
						GrandExchange.offerList.remove(offer);
						player.getPackets().resetGE(offer.slot);
						end();
						return;
					}
					if(!canTake) {
						if(offer.type == 1) {
							player.getInventory().addItem(itemId, amount);
							player.getInventory().refresh();
							offer.removedAmount = amount;
							if(offer.completed) {
								GrandExchange.offerList.remove(offer);
								player.getPackets().resetGE(offer.slot);
								System.out.println("Offer completed:"+offer.completed);
							}
						} else if(offer.type == 0) {
							player.getInventory().addItem(995, offer.price * amount);
							player.getInventory().refresh();	
							offer.removedAmount = amount;
							if(offer.completed) {
								GrandExchange.offerList.remove(offer);
								player.getPackets().resetGE(offer.slot);
								System.out.println("Offer completed:"+offer.completed);
							}
						}
					}
					end();
				} else {
					player.getPackets().sendGameMessage("You don't enough inventory space to collect the items.");
					end();
				}
			} 
			if (componentId == OPTION_2)
			end();
		}*/
	}

	@Override
	public void finish() {
	}

}
