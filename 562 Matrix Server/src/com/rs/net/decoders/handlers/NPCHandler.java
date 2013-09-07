package com.rs.net.decoders.handlers;

import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Fishing;
import com.rs.game.player.actions.Fishing.FishingSpots;
import com.rs.game.player.actions.thieving.PickPocketAction;
import com.rs.game.player.actions.thieving.PickPocketableNPC;
import com.rs.io.InputStream;
import com.rs.utils.ShopsHandler;

public class NPCHandler {

	//TODO option1
	public static void handleOption1(final Player player, InputStream stream) {
		@SuppressWarnings("unused")
		boolean unknown = stream.read128Byte() == 1;
		int npcIndex = stream.readUnsignedShort128();
		final NPC npc = World.getNPCs().get(npcIndex);
		player.stopAll(false);
		if(npc.getDefinitions().name.toLowerCase().contains("banker")) {
			if(player.withinDistance(npc, 2)) {
				npc.setNextFaceWorldTile(new WorldTile(player.getCoordFaceX(player.getSize()),
						player.getCoordFaceY(player.getSize()), player.getPlane()));
				player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize()),
						npc.getCoordFaceY(npc.getSize()), npc.getPlane()));
				player.getDialogueManager().startDialogue("Banker", npc.getId());
			}
			return;
		}
		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.setNextFaceWorldTile(new WorldTile(player.getCoordFaceX(player.getSize()),
						player.getCoordFaceY(player.getSize()), player.getPlane()));
				player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize()),
						npc.getCoordFaceY(npc.getSize()), npc.getPlane()));
				FishingSpots spot = FishingSpots.forId(npc.getId() | 1 << 24);
				if (spot != null) {
					player.getActionManager().setAction(new Fishing(spot, npc));
					return;
				}
				if (npc.getId() == 705)
					player.getDialogueManager().startDialogue("MeleeTutor", npc.getId());
				if (npc.getId() == 4906)
					player.getDialogueManager().startDialogue("WoodcuttingMaster", npc.getId());
				if (npc.getId() == 925)
					player.getDialogueManager().startDialogue("LumbyGaurds", npc.getId());
				if (npc.getId() == 926)
					player.getDialogueManager().startDialogue("LumbyGaurds", npc.getId());
				if(npc.getId() == 600) 
					player.getDialogueManager().startDialogue("test", npc.getId(), true);
				if (npc.getId() == 8273)
					player.getDialogueManager().startDialogue("Turael", npc.getId());
				if (npc.getId() == 8274)
                    player.getDialogueManager().startDialogue("Maz", npc.getId());
				if (npc.getId() == 9085)
					player.getDialogueManager().startDialogue("Kuradel", npc.getId());
				if (npc.getId() == 8275)
					player.getDialogueManager().startDialogue("Duradel", npc.getId());
				if (npc.getId() == 1598)
                    player.getDialogueManager().startDialogue("Chaeldar", npc.getId());
				if (npc.getId() == 4295)
					player.getDialogueManager().startDialogue("Velio", npc.getId());
				if (npc.getId() == 4296)
					player.getDialogueManager().startDialogue("Varnis", npc.getId());
				if (npc.getId() == 382 || npc.getId() == 3294 || npc.getId() == 4316)
					player.getDialogueManager().startDialogue("MiningGuildDwarf", npc.getId(), false);
				if (npc.getId() == 3295)
					player.getDialogueManager().startDialogue("MiningGuildDwarf", npc.getId(), true);
				else
					player.getPackets().sendGameMessage("Nothing interesting happens.");
				System.out.println(player.getDisplayName() + " clicked option1 on npc: "+npc.getId()+", "+npc.getX()+", "+npc.getY()+", "+npc.getPlane());
			}
		},  npc.getSize()));
	}
	
	//TODO option2
	public static void handleOption2(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort128();
		@SuppressWarnings("unused")
		boolean unknown = stream.readByte() == 1;
		final NPC npc = World.getNPCs().get(npcIndex);
		player.stopAll(false);
		if(npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")) {
			if(player.withinDistance(npc, 2)) {
				npc.setNextFaceWorldTile(new WorldTile(player.getCoordFaceX(player.getSize())
						, player.getCoordFaceY(player.getSize())
						, player.getPlane()));
				player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize())
						, npc.getCoordFaceY(npc.getSize())
						, npc.getPlane()));
				player.getBank().initBank();
			}
			return;
		}
		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.setNextFaceWorldTile(new WorldTile(player.getCoordFaceX(player.getSize())
						, player.getCoordFaceY(player.getSize())
						, player.getPlane()));
				player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize())
						, npc.getCoordFaceY(npc.getSize())
						, npc.getPlane()));
				FishingSpots spot = FishingSpots.forId(npc.getId() | (2 << 24));
				if (spot != null) {
					player.getActionManager().setAction(new Fishing(spot, npc));
					return;
				}
				PickPocketableNPC pocket = PickPocketableNPC.get(npc.getId());
				if (pocket != null) {
					player.getActionManager().setAction(
							new PickPocketAction(npc, pocket));
					return;
				}
				if (npc instanceof Familiar) {
					if (npc.getDefinitions().hasOption("store")) {
						if (player.getFamiliar() != npc) {
							player.getPackets().sendGameMessage(
									"That isn't your familiar.");
							return;
						}
						player.getFamiliar().store();
					} else if (npc.getDefinitions().hasOption("cure")) {
						if (player.getFamiliar() != npc) {
							player.getPackets().sendGameMessage(
									"That isn't your familiar.");
							return;
						}
						if (!player.getPoison().isPoisoned()) {
							player.getPackets().sendGameMessage(
									"Your arent poisoned or diseased.");
							return;
						} else {
							player.getFamiliar().drainSpecial(2);
							player.addPoisonImmune(120);
						}
					}
					return;
				}
				npc.faceEntity(player);
				if (!player.getControlerManager().processNPCClick2(npc))
					return;
				else if (npc.getId() == 548)
					ShopsHandler.openShop(player, 2);
				else if (npc.getId() == 546)
					ShopsHandler.openShop(player, 3);
				else if (npc.getId() == 523)
					ShopsHandler.openShop(player, 4);
				else if (npc.getId() == 553)
					ShopsHandler.openShop(player, 6);
				else if (npc.getId() == 550)
					ShopsHandler.openShop(player, 7);
				else if (npc.getId() == 549)
					ShopsHandler.openShop(player, 8);
				else if (npc.getId() == 576)
					ShopsHandler.openShop(player, 10);
				else if (npc.getId() == 545)
					ShopsHandler.openShop(player, 13);
				else if (npc.getId() == 1658)
					ShopsHandler.openShop(player, 14);
				else if (npc.getId() == 589)
					ShopsHandler.openShop(player, 28);
				else if (npc.getId() == 590)
					ShopsHandler.openShop(player, 29);
				else if (npc.getId() == 2721)
					ShopsHandler.openShop(player, 30);
				else if (npc.getId() == 2720)
					ShopsHandler.openShop(player, 31);
				else if (npc.getId() == 2719)
					ShopsHandler.openShop(player, 32);
				else if (npc.getId() == 559)
					ShopsHandler.openShop(player, 33);
				else if (npc.getId() == 2718)
					ShopsHandler.openShop(player, 34);
				else if (npc.getId() == 540)
					ShopsHandler.openShop(player, 35);
				else if (npc.getId() == 541)
					ShopsHandler.openShop(player, 36);
				else if (npc.getId() == 542)
					ShopsHandler.openShop(player, 37);
				else if (npc.getId() == 544)
					ShopsHandler.openShop(player, 38);
				else if (npc.getId() == 1783)
					ShopsHandler.openShop(player, 39);
				else if (npc.getId() == 5555)//needname
					ShopsHandler.openShop(player, 40);
				else if (npc.getId() == 5555)//needname
					ShopsHandler.openShop(player, 41);
				else if (npc.getId() == 1917)//needname
					ShopsHandler.openShop(player, 42);
				else if (npc.getId() == 538)
					ShopsHandler.openShop(player, 43);
				else if (npc.getId() == 580)
					ShopsHandler.openShop(player, 44);
				else if (npc.getId() == 577)
					ShopsHandler.openShop(player, 45);
				else if (npc.getId() == 584)
					ShopsHandler.openShop(player, 46);
				else if (npc.getId() == 581)
					ShopsHandler.openShop(player, 47);
				else if (npc.getId() == 5555)//needname
					ShopsHandler.openShop(player, 48);
				if (npc.getId() == 8461)
					player.getDialogueManager().startDialogue("Turael", npc.getId());
				if (npc.getId() == 14057)
					player.getDialogueManager().startDialogue("Velio", npc.getId());
				if (npc.getId() == 14078)
					player.getDialogueManager().startDialogue("Varnis", npc.getId());
				else
				player.getPackets().sendGameMessage("Nothing interesting happens.");
				System.out.println("cliked 2 at npc id : "+npc.getId()+", "+npc.getX()+", "+npc.getY()+", "+npc.getPlane());
			}
		},  npc.getSize()));	
	}
	
	//TODO option 3
	public void handleOption3(final Player player, InputStream stream) {
		
	}
}