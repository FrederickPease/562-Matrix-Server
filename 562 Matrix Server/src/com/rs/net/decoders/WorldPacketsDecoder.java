package com.rs.net.decoders;

import java.util.HashMap;

import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.minigames.CastleWars;
import com.rs.game.minigames.FightPits;
import com.rs.game.minigames.creations.StealingCreation;
import com.rs.game.minigames.duel.DuelControler;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.EmotesManager;
import com.rs.game.player.Equipment;
import com.rs.game.ForceMovement;
import com.rs.game.player.CombatDefinitions;
import com.rs.game.player.Inventory;
import com.rs.game.player.OwnedObjectManager;
import com.rs.game.player.Player;
import com.rs.game.player.PublicChatMessage;
import com.rs.game.player.QuickChatMessage;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.FightPitsViewingOrb;
import com.rs.game.player.actions.BoxAction;
import com.rs.game.player.actions.BoxAction.HunterEquipment;
import com.rs.game.player.actions.BoxAction.HunterNPC;
import com.rs.game.player.actions.Summoning;
import com.rs.game.player.content.agility.BarbarianOutpostAgility;
import com.rs.game.player.content.agility.GnomeAgility;
import com.rs.game.player.content.agility.WildernessAgility;
import com.rs.game.player.content.objects.Clipedobject;
import com.rs.game.player.actions.PlayerCombat;
import com.rs.game.player.actions.PlayerFollow;
import com.rs.game.player.content.Burying.Bone;
import com.rs.game.player.content.CharacterDesign;
import com.rs.game.player.content.Commands;
import com.rs.game.player.content.Hunter;
import com.rs.game.player.content.Magic;
import com.rs.game.player.content.Pots;
import com.rs.game.player.content.ShootingStars;
import com.rs.game.player.content.SkillsDialogue;
import com.rs.game.player.content.ToyHorsey;
//import com.rs.game.player.content.Runecrafting;
import com.rs.game.player.content.Shop;
import com.rs.game.player.skills.Woodcutting;
import com.rs.game.player.skills.Woodcutting.TreeDefinitions;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.io.InputStream;
import com.rs.net.Session;
import com.rs.net.decoders.handlers.InventoryOptionsHandler;
import com.rs.net.decoders.handlers.NPCHandler;
//import com.rs.net.decoders.handlers.ObjectHandler;
import com.rs.utils.Logger;
import com.rs.utils.Utils;
import com.rs.utils.huffman.Huffman;
import com.rs.game.player.content.Foods;
import com.rs.game.player.controlers.Barrows;
import com.rs.game.player.controlers.FightCaves;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.player.actions.mining.EssenceMining;
import com.rs.game.player.actions.mining.EssenceMining.EssenceDefinitions;
import com.rs.game.player.actions.mining.Mining;
import com.rs.game.player.actions.mining.Mining.RockDefinitions;

public final class WorldPacketsDecoder extends Decoder {

	private static final byte[] PACKET_SIZES = new byte[256];
	
	private final static int EXAMINE_ITEM_PACKET = 124;
	private final static int EXAMINE_NPC_PACKET = 49;
	private final static int WALKING_PACKET = 119;
	private final static int MINI_WALKING_PACKET = 163;
	private final static int WEIRD_WALKING_PACKET = 160;
	private final static int AFK_PACKET = 244;
	public final static int ACTION_BUTTON1_PACKET = 216;
	public final static int ACTION_BUTTON2_PACKET = 205;
	private final static int WEAR_ITEM_PACKET = 229;
	public final static int ACTION_BUTTON3_PACKET = 3;
	public final static int ACTION_BUTTON4_PACKET = 19;
	public final static int ACTION_BUTTON5_PACKET = 193;
	public final static int ACTION_BUTTON6_PACKET = 76;
	public final static int ACTION_BUTTON7_PACKET = 173;
	private final static int ACTION_BUTTON8_PACKET = 89;
	private final static int ACTION_BUTTON9_PACKET = 221;
	private final static int PLAYER_TRADE_OPTION_PACKET = 141;
	private final static int PLAYER_OPTION4_PACKET = 114;
	private final static int MOVE_CAMERA_PACKET = 235;
	private final static int CLICK_PACKET = 87;
	private final static int CLOSE_INTERFACE_PACKET = 91;
	private final static int COMMANDS_PACKET = 171;
	private final static int IN_OUT_SCREEN_PACKET = 4;
	private final static int INTER_PACKET_COUNT_CHECKER_PACKET = 5;
	private final static int SWITCH_DETAIL = 155;
	private final static int DONE_LOADING_REGION = 116;
	private final static int PING_PACKET = 255;
	private final static int SCREEN_PACKET = 170;
	private final static int PUBLIC_CHAT_PACKET = 182;
	private final static int QUICK_CHAT_PACKET = 68;
	private final static int ADD_FRIEND_PACKET = 226;
	private final static int REMOVE_FRIEND_PACKET = 92;
	private final static int SEND_FRIEND_MESSAGE_PACKET = 123;
	private final static int OBJECT_CLICK1_PACKET = 45;
	private final static int OBJECT_CLICK2_PACKET = 190;
	private final static int OBJECT_CLICK3_PACKET = 26;
	private final static int NPC_CLICK1_PACKET = 217;
	private final static int NPC_CLICK2_PACKET = 254;
	private final static int NPC_CLICK3_PACKET = 38;
	private final static int ATTACK_NPC = 207;//207
	private final static int ATTACK_PLAYER = 152;
	private final static int PLAYER_OPTION_2_PACKET = 140;
	private final static int ITEM_DROP_PACKET = 248;
	private final static int ITEM_ON_ITEM_PACKET = 117;
	private final static int ITEM_SELECT_PACKET = 66;
	private final static int ITEM_ON_OBJECT_PACKET = 202;
	private final static int ITEM_OPERATE_PACKET = 29;//189
	private final static int ITEM_ON_NPC_PACKET = 136;//189
	private final static int ITEM_TAKE_PACKET = 194;
	private final static int DIALOGUE_CONTINUE_PACKET = 147;
	private final static int ENTER_INTEGER_PACKET = 206;
	private final static int SWITCH_INTERFACE_ITEM_PACKET = 253;
	private final static int ITEM_OPTION1_PACKET = 234;
	private final static int INTERFACE_ON_PLAYER = 79;
	private final static int INTERFACE_ON_NPC = 84;
	static {
		loadPacketSizes();
	}
	
	public static void loadPacketSizes() {
		for(int id = 0; id < 256; id++)
			PACKET_SIZES[id] = -4;
		PACKET_SIZES[EXAMINE_ITEM_PACKET] = 2;
		PACKET_SIZES[EXAMINE_NPC_PACKET] = 2;
		PACKET_SIZES[AFK_PACKET] = 0;
		PACKET_SIZES[ACTION_BUTTON1_PACKET] = 6;
		PACKET_SIZES[WEIRD_WALKING_PACKET] = 4;
		PACKET_SIZES[ACTION_BUTTON2_PACKET] = 4;
		PACKET_SIZES[WEAR_ITEM_PACKET] = 8;
		PACKET_SIZES[ACTION_BUTTON3_PACKET] = 6;
		PACKET_SIZES[ACTION_BUTTON4_PACKET] = 6;
		PACKET_SIZES[ACTION_BUTTON5_PACKET] = 6;
		PACKET_SIZES[ACTION_BUTTON6_PACKET] = 6;
		PACKET_SIZES[ACTION_BUTTON7_PACKET] = 6;
		PACKET_SIZES[ACTION_BUTTON8_PACKET] = 6;
		PACKET_SIZES[ACTION_BUTTON9_PACKET] = 6;
		PACKET_SIZES[CLICK_PACKET] = 6;
		PACKET_SIZES[CLOSE_INTERFACE_PACKET] = 0;
		PACKET_SIZES[COMMANDS_PACKET] = -1;
		PACKET_SIZES[IN_OUT_SCREEN_PACKET] = 1;
		PACKET_SIZES[INTER_PACKET_COUNT_CHECKER_PACKET] = 2;
		PACKET_SIZES[PING_PACKET] = 0;
		PACKET_SIZES[MINI_WALKING_PACKET] = -1;
		PACKET_SIZES[WALKING_PACKET] = -1;
		PACKET_SIZES[SCREEN_PACKET] = 6;
		PACKET_SIZES[SWITCH_DETAIL] = 4;
		PACKET_SIZES[DONE_LOADING_REGION] = 0;
		PACKET_SIZES[PUBLIC_CHAT_PACKET] = -1;
		PACKET_SIZES[QUICK_CHAT_PACKET] = -1;
		PACKET_SIZES[ADD_FRIEND_PACKET] = -1;
		PACKET_SIZES[REMOVE_FRIEND_PACKET] = -1;
		PACKET_SIZES[SEND_FRIEND_MESSAGE_PACKET] = -1;
		PACKET_SIZES[OBJECT_CLICK1_PACKET] = 7;
		PACKET_SIZES[OBJECT_CLICK2_PACKET] = 7;
		PACKET_SIZES[OBJECT_CLICK3_PACKET] = 6;
		PACKET_SIZES[ITEM_DROP_PACKET] = 8;
		PACKET_SIZES[ITEM_ON_ITEM_PACKET] = 16;
		PACKET_SIZES[ITEM_SELECT_PACKET] = 8;
		PACKET_SIZES[ITEM_OPERATE_PACKET] = 8;
		PACKET_SIZES[ITEM_ON_NPC_PACKET] = 11;
		PACKET_SIZES[ITEM_ON_OBJECT_PACKET] = 16;//16
		PACKET_SIZES[ITEM_TAKE_PACKET] = 7;
		PACKET_SIZES[DIALOGUE_CONTINUE_PACKET] = 6;
		PACKET_SIZES[MOVE_CAMERA_PACKET] = 4;
		PACKET_SIZES[ENTER_INTEGER_PACKET] = 4;
		PACKET_SIZES[PLAYER_TRADE_OPTION_PACKET] = 3;
		PACKET_SIZES[PLAYER_OPTION4_PACKET] = 3;
		PACKET_SIZES[PLAYER_OPTION_2_PACKET] = 3;
		PACKET_SIZES[SWITCH_INTERFACE_ITEM_PACKET] = 9;
		PACKET_SIZES[ATTACK_NPC] = 3;
		PACKET_SIZES[ATTACK_PLAYER] = 3;
		PACKET_SIZES[NPC_CLICK1_PACKET] = 3;
		PACKET_SIZES[NPC_CLICK2_PACKET] = 3;
		PACKET_SIZES[NPC_CLICK3_PACKET] = 3;
		PACKET_SIZES[ITEM_OPTION1_PACKET] = 8;
		PACKET_SIZES[INTERFACE_ON_PLAYER] = 9;
		PACKET_SIZES[INTERFACE_ON_NPC] = 9;
	}
	
	private Player player;
	private boolean clicked;
	public WorldPacketsDecoder(Session session, Player player) {
		super(session);
		this.player = player;
	}

	@Override
	public void decode(InputStream stream) {
		while (stream.getRemaining() > 0 && session.getChannel().isConnected() && !player.hasFinished()) {
			int packetId = stream.readUnsignedByte();
			if(packetId >= PACKET_SIZES.length) {
				System.out.println("PacketId " +packetId+ " has fake packet id.");
				break;
			}
			int length = PACKET_SIZES[packetId];
			if (length == -1)
				length = stream.readUnsignedByte();
			else if (length == -2)
				length = stream.readUnsignedShort();
			else if (length == -3)
				length = stream.readInt();
			else if (length == -4) {
				length = stream.getRemaining();
				System.out.println("Invalid size for PacketId "+packetId+". Size guessed to be "+length);
			}
			if(length > stream.getRemaining()) {
				length = stream.getRemaining();
				System.out.println("PacketId " +packetId+ " has fake size. - expected size " +length);
				//break;
				
			}
		//	System.out.println("PacketId " +packetId+ " has . - expected size " +length);
			int startOffset = stream.getOffset();
			processPackets(packetId, stream, length);
			stream.setOffset(startOffset + length);
		}
	}
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
				if(player.getX() == 3005 && player.getY() == 3376
						|| player.getX() == 2999 && player.getY() == 3375
						|| player.getX() == 2996 && player.getY() == 3377
						|| player.getX() == 2989 && player.getY() == 3378
						|| player.getX() == 2987 && player.getY() == 3387
						|| player.getX() == 2984 && player.getY() == 3387) {
					//mole
					player.setNextWorldTile(new WorldTile(1752, 5137, 0));
					player.getPackets().sendGameMessage("You seem to have dropped down into a network of mole tunnels.");
					return;
				}
				player.getPackets().sendGameMessage("You find nothing.");
			}
			
		});
	}

	public void processPackets(final int packetId, InputStream stream, int length) {
		player.setPacketsDecoderPing(System.currentTimeMillis());
		if(packetId == PING_PACKET) {
			//kk we ping :)
		}else if(packetId == AFK_PACKET) {
			player.getSession().getChannel().close();
		}else if(packetId == CLOSE_INTERFACE_PACKET) {
			if(!player.isRunning()) {
				player.run();
				return;
			}
			player.stopAll();
		}else if(packetId == MOVE_CAMERA_PACKET) {
			//not using it atm
			stream.readShort();
			stream.readShortLE();
		}else if(packetId == INTER_PACKET_COUNT_CHECKER_PACKET) {
			if(stream.readUnsignedShort() != player.getPackets().getInterPacketsCount()) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				//may miss
			//	player.getPackets().sendGameMessage("Invalid interface packet count.");
			}
		}else if(packetId == IN_OUT_SCREEN_PACKET) {
			//not using this check because not 100% efficient
			@SuppressWarnings("unused")
			boolean inScreen = stream.readByte() == 1;
		}else if(packetId == SCREEN_PACKET) {
			int displayMode = stream.readUnsignedByte();
			player.setScreenWidth(stream.readUnsignedShort());
			player.setScreenHeight(stream.readUnsignedShort());
			@SuppressWarnings("unused")
			boolean switchScreenMode = stream.readUnsignedByte() == 1;
			if(!player.hasStarted() || player.hasFinished() || displayMode == player.getDisplayMode() || !player.getInterfaceManager().containsInterface(742))
				return;
			player.setDisplayMode(displayMode);
			player.getInterfaceManager().removeAll();
			player.getInterfaceManager().sendInterfaces();
			player.getInterfaceManager().sendInterface(742);
		}else if(packetId == CLICK_PACKET) {
			int mouseHash = stream.readShortLE();
			int mouseButton =  mouseHash >> 15;
			int time = mouseHash - (mouseButton << 15); //time
			int positionHash = stream.readIntV2();
			int y = positionHash >> 16; //y;
			int x = positionHash - (y << 16); //x
			//mass click or stupid autoclicker, lets stop lagg
			if(time <= 1 || x < 0 || x > player.getScreenWidth() || y < 0 || y > player.getScreenHeight()) {
			//	player.getSession().getChannel().close();
				clicked = false;
				return;
			}
			clicked = true;
		}else if (packetId == DIALOGUE_CONTINUE_PACKET) {
			@SuppressWarnings("unused")
			int junk = stream.readShortLE();
			int interfaceHash = stream.readIntV1();
			int interfaceId = interfaceHash >> 16;
			if(Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			if(!player.isRunning() || !player.getInterfaceManager().containsInterface(interfaceId))
				return;
			int componentId = interfaceHash - (interfaceId << 16);
			player.getDialogueManager().continueDialogue(interfaceId, componentId);
		}else if(packetId == ACTION_BUTTON1_PACKET || packetId == ACTION_BUTTON2_PACKET
				|| packetId == ACTION_BUTTON3_PACKET
				|| packetId == ACTION_BUTTON4_PACKET || packetId == ACTION_BUTTON5_PACKET
				|| packetId == ACTION_BUTTON6_PACKET || packetId == ACTION_BUTTON7_PACKET
				|| packetId == ACTION_BUTTON8_PACKET || packetId == ACTION_BUTTON9_PACKET) {
			if(!clicked) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			clicked = false;
			int interfaceId = stream.readUnsignedShort();
			if(Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			if(player.isDead() || !player.getInterfaceManager().containsInterface(interfaceId))
				return;
			int componentId = stream.readUnsignedShort();
			if(componentId == 65535)
				componentId = -1;
			if(componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			int slotId = packetId != ACTION_BUTTON2_PACKET ? stream.readUnsignedShort() : -1;
			//int buttonId = stream.readShort() & 0xFFFF;
			int buttonId2 = -1;
			if (!player.getControlerManager().processButtonClick(interfaceId,
					componentId, slotId, packetId))
				return;
			if (stream.getLength() >= 6) {
				buttonId2 = stream.readShort() & 0xFFFF;
			}
			if (buttonId2 == 65535) {
				buttonId2 = 0;
			}
			if(slotId == 65535)
				slotId = -1;
			if (interfaceId == 182) {
				if(player.getInterfaceManager().containsInventoryInter())
					return;
				if(componentId == 6) 
					if(!player.hasFinished())
						player.logout();
			} else if ((interfaceId == 590 && componentId == 8) || interfaceId == 464) {
				player.getEmotesManager().useBookEmote(interfaceId == 464 ? componentId : EmotesManager.getId(slotId, packetId));
		/*	}else if(interfaceId == 192) {
				if(componentId == 7)
					player.getCombatDefinitions().switchShowCombatSpells();
				else if(componentId == 9)
					player.getCombatDefinitions().switchShowTeleportSkillSpells();
				else if(componentId == 11)
					player.getCombatDefinitions().switchShowMiscallaneousSpells();
				else if(componentId == 13)
					player.getCombatDefinitions().switchShowSkillSpells();
				else if (componentId >= 15 & componentId <= 17)
					player.getCombatDefinitions().setSortSpellBook(componentId-15);
				else
					Magic.processNormalSpell(player, componentId, packetId);*/
			}else if (interfaceId == 187) {
				if (componentId == 1) {
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getMusicsManager().playAnotherMusic(slotId / 2);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						player.getMusicsManager().sendHint(slotId / 2);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getMusicsManager().addToPlayList(slotId / 2);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getMusicsManager().removeFromPlayList(slotId / 2);
				} else if (componentId == 4)
					player.getMusicsManager().addPlayingMusicToPlayList();
				else if (componentId == 10)
					player.getMusicsManager().switchPlayListOn();
				else if (componentId == 11)
					player.getMusicsManager().clearPlayList();
				else if (componentId == 13)
					player.getMusicsManager().switchShuffleOn();
			} else if (interfaceId == 192) {
				if (componentId == 2)
					player.getCombatDefinitions().switchDefensiveCasting();
				else if (componentId == 7)
					player.getCombatDefinitions().switchShowCombatSpells();
				else if (componentId == 9)
					player.getCombatDefinitions().switchShowTeleportSkillSpells();
				else if (componentId == 11)
					player.getCombatDefinitions().switchShowMiscallaneousSpells();
				else if (componentId == 13)
					player.getCombatDefinitions().switchShowSkillSpells();
				else if (componentId >= 15 & componentId <= 17)
					player.getCombatDefinitions()
					.setSortSpellBook(componentId - 15);
				else
					Magic.processNormalSpell(player, componentId, packetId);
			/*} else if (interfaceId == 430) {
				if (componentId == 5)
					player.getCombatDefinitions().switchShowCombatSpells();
				else if (componentId == 7)
					player.getCombatDefinitions().switchShowTeleportSkillSpells();
				else if (componentId == 9)
					player.getCombatDefinitions().switchShowMiscallaneousSpells();
				else if (componentId >= 11 & componentId <= 13)
					player.getCombatDefinitions()
					.setSortSpellBook(componentId - 11);
				else if (componentId == 20)
					player.getCombatDefinitions().switchDefensiveCasting();
				else
					Magic.processLunarSpell(player, componentId, packetId);*/
			} else if (interfaceId == 430) {
				if (componentId == 5)
					player.getCombatDefinitions().switchShowCombatSpells();
				else if (componentId == 7)
					player.getCombatDefinitions().switchShowTeleportSkillSpells();
				else if (componentId == 9)
					player.getCombatDefinitions().switchShowMiscallaneousSpells();
				else if (componentId >= 9 & componentId <= 11)
					player.getCombatDefinitions()
					.setSortSpellBook(componentId - 9);
				else if (componentId == 20)
					player.getCombatDefinitions().switchDefensiveCasting();
				else
					Magic.processLunarSpell(player, componentId, packetId);
		/*	}else if(interfaceId == 193) {
				if(componentId == 5)
					player.getCombatDefinitions().switchShowCombatSpells();
				else if(componentId == 7)
					player.getCombatDefinitions().switchShowTeleportSkillSpells();
				else if(componentId == 9)
					player.getCombatDefinitions().switchShowMiscallaneousSpells();
				else if (componentId >= 11 && componentId <= 13)
					player.getCombatDefinitions().setSortSpellBook(componentId-1);*/
			/*} else if (interfaceId == 193) {
				if (componentId == 5)
					player.getCombatDefinitions().switchShowCombatSpells();
				else if (componentId == 7)
					player.getCombatDefinitions().switchShowTeleportSkillSpells();
				else if (componentId >= 11 && componentId <= 13)
					player.getCombatDefinitions().setSortSpellBook(componentId - 9);
				else if (componentId == 18)
					player.getCombatDefinitions().switchDefensiveCasting();
				else
					Magic.processAncientSpell(player, componentId, packetId);*/
			} else if (interfaceId == 193) {
				if (componentId == 5)
					player.getCombatDefinitions().switchShowCombatSpells();
				else if (componentId == 7)
					player.getCombatDefinitions().switchShowTeleportSkillSpells();
				else if (componentId >= 11 && componentId <= 13)
					player.getCombatDefinitions().setSortSpellBook(componentId - 1);
				else if (componentId == 18)
					player.getCombatDefinitions().switchDefensiveCasting();
				else
					Magic.processAncientSpell(player, componentId, packetId);
			}else if(interfaceId == 261) {
				if(player.getInterfaceManager().containsInventoryInter())
					return;
				if(componentId == 3) {
					player.toogleRun(true);
				}else if(componentId == 16) {
					if(player.getInterfaceManager().containsScreenInter()) {
						player.getPackets().sendGameMessage("Please close the interface you have open before setting your graphic options.");
						return;
					}
					player.stopAll();
					player.getInterfaceManager().sendInterface(742);
				}else if (componentId == 4)
					player.switchAllowChatEffects();
				else if (componentId == 5)
					player.switchAllowSplitChat();
				else if (componentId == 6)
					player.switchMouseButtons();
				else if(componentId == 18) {
					if(player.getInterfaceManager().containsScreenInter()) {
						player.getPackets().sendGameMessage("Please close the interface you have open before setting your audio options.");
						return;
					}
					player.stopAll();
					player.getInterfaceManager().sendInterface(743);
				}
				
			}else if(interfaceId == 271) {
				if(componentId == 7 || componentId == 6)
					player.getPrayer().switchPrayer(slotId);
				else if (componentId == 8 && player.getPrayer().isUsingQuickPrayer())
					player.getPrayer().switchSettingQuickPrayer();
			}else if(interfaceId == 320) {
				player.stopAll();
				boolean lvlup = false;
				int skillMenu = -1;
				switch(componentId) {
			case 125: //Attack
				skillMenu = 1;
				if(player.getTemporaryAttributtes().get("leveledUp[0]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 1);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 10);
				}
		        break;
		    case 126: //Strength
				skillMenu = 2;
				if(player.getTemporaryAttributtes().get("leveledUp[2]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 2);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 20);
				}
		        break;
		    case 127: //Defence
				skillMenu = 5;
				if(player.getTemporaryAttributtes().get("leveledUp[1]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 5);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 40);
				}
		        break;
		    case 128: //Ranged
				skillMenu = 3;
				if(player.getTemporaryAttributtes().get("leveledUp[4]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 3);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 30);
				}
		        break;
		    case 129: //Prayer
				if(player.getTemporaryAttributtes().get("leveledUp[5]") != Boolean.TRUE) {
					skillMenu = 7;
					player.getPackets().sendConfig(965, 7);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 60);
				}
		        break;
		    case 130: //Magic
				if(player.getTemporaryAttributtes().get("leveledUp[6]") != Boolean.TRUE) {
					skillMenu = 4;
					player.getPackets().sendConfig(965, 4);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 33);
				}
		        break;
		    case 131: //Runecrafting
				if(player.getTemporaryAttributtes().get("leveledUp[20]") != Boolean.TRUE) {
					skillMenu = 12;
					player.getPackets().sendConfig(965, 12);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 100);
				}
		        break;
		    case 132: //Construction
				skillMenu = 22;
				if(player.getTemporaryAttributtes().get("leveledUp[21]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 22);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 698);
				}
		        break;
		    case 133: //Hitpoints
				skillMenu = 6;
				if(player.getTemporaryAttributtes().get("leveledUp[3]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 6);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 50);
				}
		        break;
		    case 134: //Agility
				skillMenu = 8;
				if(player.getTemporaryAttributtes().get("leveledUp[16]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 8);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 65);
				}
		        break;
		    case 135: //Herblore
				skillMenu = 9;
				if(player.getTemporaryAttributtes().get("leveledUp[15]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 9);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 75);
				}
		        break;
		    case 136: //Thieving
				skillMenu = 10;
				if(player.getTemporaryAttributtes().get("leveledUp[17]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 10);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 80);
				}
		        break;
		    case 137: //Crafting
				skillMenu = 11;
				if(player.getTemporaryAttributtes().get("leveledUp[12]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 11);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 90);
				}
		        break;
		    case 138: //Fletching
				skillMenu = 19;
				if(player.getTemporaryAttributtes().get("leveledUp[9]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 19);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 665);
				}
		        break;
		    case 139: //Slayer
				skillMenu = 20;
				if(player.getTemporaryAttributtes().get("leveledUp[18]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 20);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 673);
				}
		        break;
		    case 140: //Hunter
				skillMenu = 23;
				if(player.getTemporaryAttributtes().get("leveledUp[22]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 23);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 689);
				}
		        break;
		    case 141: //Mining
				skillMenu = 13;
				if(player.getTemporaryAttributtes().get("leveledUp[14]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 13);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 110);
				}
		        break;
		    case 142: //Smithing
				skillMenu = 14;
				if(player.getTemporaryAttributtes().get("leveledUp[13]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 14);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 115);
				}
		        break;
		    case 143: //Fishing
				skillMenu = 15;
				if(player.getTemporaryAttributtes().get("leveledUp[10]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 15);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 120);
				}
		        break;
		    case 144: //Cooking
				skillMenu = 16;
				if(player.getTemporaryAttributtes().get("leveledUp[7]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 16);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 641);
				}
		        break;
		    case 145: //Firemaking
				skillMenu = 17;
				if(player.getTemporaryAttributtes().get("leveledUp[11]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 17);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 649);
				}
		        break;
		    case 146: //Woodcutting
				skillMenu = 18;
				if(player.getTemporaryAttributtes().get("leveledUp[8]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 18);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 660);
				}
		        break;
		    case 147: //Farming
				skillMenu = 21;
				if(player.getTemporaryAttributtes().get("leveledUp[19]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 21);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 681);
				}
		        break;
		    case 148: //Summoning
				skillMenu = 24;
				if(player.getTemporaryAttributtes().get("leveledUp[23]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 24);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 705);
				}
		        break;
			}
			player.getInterfaceManager().sendInterface(lvlup ? 741 : 499);
			for(int skill = 0; skill < 25; skill++)
				player.getTemporaryAttributtes().remove("leveledUp["+skill+"]");
			if(skillMenu != -1)
				player.getTemporaryAttributtes().put("skillMenu", skillMenu);
			player.getPackets().sendConfig(1179, 0); //stops flash
			}else if(interfaceId == 378) {
				if(componentId == 152 && !player.isRunning()) //click here to play
					player.run();
			}else if(interfaceId == 499) {
				int skillMenu = -1;
				if(player.getTemporaryAttributtes().get("skillMenu") != null)
					skillMenu = (Integer) player.getTemporaryAttributtes().get("skillMenu");
				switch(componentId) {
				case 10:
					player.getPackets().sendConfig(965, skillMenu);
					break;
				case 11:
					player.getPackets().sendConfig(965, 1024 + skillMenu);
					break;
				case 12:
					player.getPackets().sendConfig(965, 2048 + skillMenu);
					break;
				case 13:
					player.getPackets().sendConfig(965, 3072 + skillMenu);
					break;
				case 14:
					player.getPackets().sendConfig(965, 4096 + skillMenu);
					break;
				case 15:
					player.getPackets().sendConfig(965, 5120 + skillMenu);
					break;
				case 16:
					player.getPackets().sendConfig(965, 6144 + skillMenu);
					break;
				case 17:
					player.getPackets().sendConfig(965, 7168 + skillMenu);
					break;
				case 18:
					player.getPackets().sendConfig(965, 8192 + skillMenu);
					break;
				case 19:
					player.getPackets().sendConfig(965, 9216 + skillMenu);
					break;
				case 20:
					player.getPackets().sendConfig(965, 10240 + skillMenu);
					break;
				case 21:
					player.getPackets().sendConfig(965, 11264 + skillMenu);
					break;
				case 22:
					player.getPackets().sendConfig(965, 12288 + skillMenu);
					break;
				case 23:
					player.getPackets().sendConfig(965, 13312 + skillMenu);
					break;
				case 29: //close inter
					player.stopAll();
					break;
				}
		/*	} else if (interfaceId == 449) {
				if (componentId == 1) {
					Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
					if (shop == null)
						return;
					shop.sendInventory(player);
				} else if (componentId == 21) {
					Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
					if (shop == null)
						return;
					Integer slot = (Integer) player.getTemporaryAttributtes().get(
							"ShopSelectedSlot");
					if (slot == null)
						return;
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						shop.buy(player, slot, 1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						shop.buy(player, slot, 5);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						shop.buy(player, slot, 10);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						shop.buy(player, slot, 50);

				}
			} else if (interfaceId == 620) {
				if (componentId == 25) {
					Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
					if (shop == null)
						return;
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						shop.sendInfo(player, slotId);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						shop.buy(player, slotId, 1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						shop.buy(player, slotId, 5);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						shop.buy(player, slotId, 10);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
						shop.buy(player, slotId, 50);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
						shop.buy(player, slotId, 500);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
						shop.sendExamine(player, slotId);
				}
			} else if (interfaceId == 621) {
				if (componentId == 0) {

					if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
						player.getInventory().sendExamine(slotId);
					else {
						Shop shop = (Shop) player.getTemporaryAttributtes().get(
								"Shop");
						if (shop == null)
							return;
						if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
							shop.sendValue(player, slotId);
						else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
							shop.sell(player, slotId, 1);
						else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
							shop.sell(player, slotId, 5);
						else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
							shop.sell(player, slotId, 10);
						else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
							shop.sell(player, slotId, 50);
					}
					if (player.getTemporaryAttributtes().get("RingNPC") == Boolean.TRUE) {
				player.unlock();
				player.getAppearence().transformIntoNPC(-1);
				player.getTemporaryAttributtes().remove("RingNPC");
			}
				}*/
			} else if (interfaceId == 375) {
					player.getInterfaceManager().closeInventoryInterface();
					player.unlock();
					player.getAppearence().transformIntoNPC(-1);
		                        player.getTemporaryAttributtes().remove("RingNPC");
					return;
			}else if (interfaceId == 374) {
					if (componentId >= 11 && componentId <= 15)
						player.setNextWorldTile(new WorldTile(
								FightPitsViewingOrb.ORB_TELEPORTS[componentId - 5]));
					else if (componentId == 5)
						player.stopAll();
			} else if (interfaceId == 449) {
				if (componentId == 1) {
					Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
					if (shop == null)
						return;
					shop.sendInventory(player);
				} else if (componentId == 21) {
					Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
					if (shop == null)
						return;
					Integer slot = (Integer) player.getTemporaryAttributtes().get(
							"ShopSelectedSlot");
					if (slot == null)
						return;
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						shop.buy(player, slot, 1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						shop.buy(player, slot, 5);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						shop.buy(player, slot, 10);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						shop.buy(player, slot, 50);

				}
			} else if (interfaceId == 620) {
				if (componentId == 25) {//2518
					Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
					if (shop == null)
						return;
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						shop.sendInfo(player, slotId);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						shop.buy(player, slotId, 1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
						shop.buy(player, slotId, 5);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
						shop.buy(player, slotId, 10);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET)
						shop.buy(player, slotId, 50);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
						shop.buy(player, slotId, 500);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						shop.sendExamine(player, slotId);
				}
			} else if (interfaceId == 621) {
				if (componentId == 0) {

					if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getInventory().sendExamine(slotId);
					else {
						Shop shop = (Shop) player.getTemporaryAttributtes().get(
								"Shop");
						if (shop == null)
							return;
						if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
							shop.sendValue(player, slotId);
						else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
							shop.sell(player, slotId, 1);
						else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
							shop.sell(player, slotId, 5);
						else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
							shop.sell(player, slotId, 10);
						else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET)
							shop.sell(player, slotId, 50);
					}
				}
			}else if (interfaceId == 771) {
				CharacterDesign.handle(player, componentId);
			}else if (interfaceId == 382) {
				if(componentId == 18) {
					player.stopAll();
					final WorldObject ditch = (WorldObject) player.getTemporaryAttributtes().get("wildernessditch");
					if(ditch == null)
						return;
					player.addStopDelay(4);
					player.setNextAnimation(new Animation(6132));
					final WorldTile toTile = new WorldTile(player.getX(), ditch.getY()+2, ditch.getPlane());
					player.setNextForceMovement(new ForceMovement(new WorldTile(player), 1, toTile, 2, 0));
					final ObjectDefinitions objectDef = ditch.getDefinitions();
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							player.setNextWorldTile(toTile, false);
							player.setNextFaceWorldTile(new WorldTile(ditch.getCoordFaceX(objectDef.getSizeX(), objectDef.getSizeY(), ditch.getRotation())
									, ditch.getCoordFaceY(objectDef.getSizeX(),objectDef.getSizeY(), ditch.getRotation())
									, ditch.getPlane()));
							player.getControlerManager().startControler("Wilderness");
						}
					}, 2);
				}
			}else if(interfaceId == 387) {
				System.out.println("Component: "+componentId+"");
				if(componentId == 53) {
					player.stopAll();
					player.getInterfaceManager().sendInterface(102);
				}else if(componentId == 56) {
					player.stopAll();
					player.getInterfaceManager().sendInterface(667);
					player.getInterfaceManager().sendInventoryInterface(670);
					player.getPackets().sendIComponentText(667,36, "Stab: " + player.getCombatDefinitions().getBonuses()[0]);
					player.getPackets().sendIComponentText(667,37, "Slash: " + player.getCombatDefinitions().getBonuses()[1]);
					player.getPackets().sendIComponentText(667,38, "Crush: " + player.getCombatDefinitions().getBonuses()[2]);
					player.getPackets().sendIComponentText(667,39, "Magic: " + player.getCombatDefinitions().getBonuses()[3]);
					player.getPackets().sendIComponentText(667,40, "Range: " + player.getCombatDefinitions().getBonuses()[4]);
					player.getPackets().sendIComponentText(667,41, "Stab: " + player.getCombatDefinitions().getBonuses()[5]);
					player.getPackets().sendIComponentText(667,42, "Slash: " + player.getCombatDefinitions().getBonuses()[6]);
					player.getPackets().sendIComponentText(667,43, "Crush: " + player.getCombatDefinitions().getBonuses()[7]);
					player.getPackets().sendIComponentText(667,44, "Magic: " + player.getCombatDefinitions().getBonuses()[8]);
					player.getPackets().sendIComponentText(667,45, "Range: " + player.getCombatDefinitions().getBonuses()[9]);
					player.getPackets().sendIComponentText(667,46, "Summoning: " + player.getCombatDefinitions().getBonuses()[10]);
					player.getPackets().sendIComponentText(667,48, "Strength: " + player.getCombatDefinitions().getBonuses()[14]);
					player.getPackets().sendIComponentText(667,49, "Ranged Str: " + player.getCombatDefinitions().getBonuses()[15]);
					player.getPackets().sendIComponentText(667,50, "Prayer: " + player.getCombatDefinitions().getBonuses()[16]);
					player.getPackets().sendIComponentText(667,51, "Magic Damage: " + player.getCombatDefinitions().getBonuses()[17]+"%");
				}else if(componentId == 63) {
					if(player.getInterfaceManager().containsScreenInter()) {
						player.getPackets().sendGameMessage("Please finish what you're doing before opening the price checker.");
						return;
					}
					player.stopAll();
					player.getInterfaceManager().sendInterface(206);
					player.getInterfaceManager().sendInventoryInterface(207);
				}
				}else if(interfaceId == 742) {
				if(componentId == 46) //close
					player.stopAll();
			}else if(interfaceId == 743) {
				if(componentId == 20) //close
					player.stopAll();
			}else if(interfaceId == 741) {
				if(componentId == 9) //close
					player.stopAll();
			}else if(interfaceId == 749) {
				if(componentId == 1) {
					if(packetId == 216) //activate
						player.getPrayer().switchQuickPrayers();
					else if(packetId == 19) //switch
						player.getPrayer().switchSettingQuickPrayer();
				}
			}else if (interfaceId == 750) {
				if(componentId == 1) {
					if(player.isResting()) {
						player.toogleRun(true);
						player.stopAll();
						return;
					}
					if(packetId == 216)
						player.toogleRun(true);
					else if (packetId == 19) {
						player.stopAll();
						player.setResting(true);
						player.setNextAnimation(new Animation(5713));
					}
				}
			} else if (interfaceId == 105
					|| interfaceId == 107
					|| interfaceId == 110) {
		//player.getGrandExchange().handleButtons(interfaceId, slotId, componentId, packetId);				
			} else if (interfaceId == 334) {
				if(componentId == 21)
					player.closeInterfaces();
				else if (componentId == 20)
					player.getTrade().accept(false);
			} else if (interfaceId == 335) {
				if(componentId == 16)//18
				//	if(packetId == 216) 
					player.getTrade().accept(true);
				else if(componentId == 18)//20 
					player.closeInterfaces();
				else if(componentId == 30) {
					if(packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getTrade().removeItem(slotId, 1);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getTrade().removeItem(slotId, 5);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
						player.getTrade().removeItem(slotId, 10);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
						player.getTrade().removeItem(slotId, Integer.MAX_VALUE);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET) {
						player.getTemporaryAttributtes().put("trade_item_X_Slot", slotId);
						player.getTemporaryAttributtes().put("trade_isRemove", Boolean.TRUE);
						player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
					}else if(packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getTrade().sendValue(slotId, false);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getTrade().sendExamine(slotId, false);
				}else if(componentId == 33) {
					if(packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getTrade().sendValue(slotId, true);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getTrade().sendExamine(slotId, true);
				}
			} else if (interfaceId == 336) {
				if(componentId == 0) {
					if(packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getTrade().addItem(slotId, 1);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getTrade().addItem(slotId, 5);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
						player.getTrade().addItem(slotId, 10);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
						player.getTrade().addItem(slotId, Integer.MAX_VALUE);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET) {
						player.getTemporaryAttributtes().put("trade_item_X_Slot", slotId);
						player.getTemporaryAttributtes().remove("trade_isRemove");
						player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
					}else if(packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
						player.getTrade().sendValue(slotId);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getInventory().sendExamine(slotId);
				}
			/*} else if (interfaceId == 751) {
	                if (componentId == 20) {
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						player.setTradeStatus(0);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.setTradeStatus(1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.setTradeStatus(2);
				} /*else if (componentId == 17) {
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						player.setAssistStatus(0);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.setAssistStatus(1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.setAssistStatus(2);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
						//ASSIST XP Earned/Time
					}*/
		/*	}else	if (interfaceId == 548 || interfaceId == 746) {
					if ((interfaceId == 548 && componentId == 148)
							|| (interfaceId == 746 && componentId == 199)) {
						if (player.getInterfaceManager().containsScreenInter()
								|| player.getInterfaceManager()
								.containsInventoryInter()) {
							// TODO cant open sound
							player.getPackets()
							.sendGameMessage(
									"Please finish what you're doing before opening the world map.");
							return;
						}
						// world map open
						player.getPackets().sendWindowsPane(755, 0);
						int posHash = player.getX() << 14 | player.getY();
						player.getPackets().sendConfig(622, posHash); // map open
						// center
						// pos
						player.getPackets().sendConfig(674, posHash); // player
						// position
					}*/
			/*	if (!player.getControlerManager().processButtonClick(interfaceId,
						componentId, slotId, packetId))
					return;*/
		/*	} else if (interfaceId == 107) {
				if (player.getGrandExchange().canExchange(new Item(buttonId2))
						&& ItemDefinitions.getItemDefinitions(buttonId2) != null) {
					player.getGrandExchange().sellItem(player, buttonId2,
							player.getInventory().getNumerOf(buttonId2));
				}*/
			} else if (interfaceId == 762) {
				if (componentId == 14)
					player.getBank().switchInsertItems();
				else if (componentId == 18)
					player.getBank().switchWithdrawNotes();
				else if (componentId == 20)
					player.getBank().depositAllInventory(true);
				else if (componentId == 22)
					player.getBank().depositAllEquipment(true);
			    else if (componentId >= 33 && componentId <= 49) {
					int tabId = 9 - ((componentId - 33) / 2);//9 - ((componentId - 33) / 2
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getBank().setCurrentTab(tabId);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getBank().collapse(tabId);
			    } else if (componentId == 81) {
			    	if(packetId == 216) 
						player.getBank().withdrawItem(slotId, 1);
					else if (packetId == 19)
						player.getBank().withdrawItem(slotId, 5);
					else if (packetId == 193)
						player.getBank().withdrawItem(slotId, 10);
					else if (packetId == 76)
						player.getBank().withdrawLastAmount(slotId);
					else if (packetId == 173) {
						player.getTemporaryAttributtes().put("bank_item_X_Slot", slotId);
						player.getTemporaryAttributtes().put("bank_isWithdraw", Boolean.TRUE);
						player.getPackets().sendRunScript(108, new Object[]{"Enter Amount:"});
					}
					else if (packetId == 89)
						player.getBank().withdrawItem(slotId, Integer.MAX_VALUE);
					else if (packetId == 221)
						player.getBank().withdrawItemButOne(slotId);
				}
			} else if (interfaceId == 640) {
				if (componentId == 18 || componentId == 22) {
					player.getTemporaryAttributtes().put("WillDuelFriendly", true);
					player.getPackets().sendConfig(283, 67108864);
				} else if (componentId == 19 || componentId == 21) {
					player.getTemporaryAttributtes().put("WillDuelFriendly", false);
					player.getPackets().sendConfig(283, 134217728);
				} else if (componentId == 20) {
					DuelControler.challenge(player);
				}
			} else if (interfaceId == 665) {
				if (player.getFamiliar() == null
						|| player.getFamiliar().getBob() == null)
					return;
				if (componentId == 0) {
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getFamiliar().getBob().addItem(slotId, 1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						player.getFamiliar().getBob().addItem(slotId, 5);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getFamiliar().getBob().addItem(slotId, 10);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getFamiliar().getBob()
						.addItem(slotId, Integer.MAX_VALUE);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
						player.getTemporaryAttributtes().put("bob_item_X_Slot",
								slotId);
						player.getTemporaryAttributtes().remove("bob_isRemove");
						player.getPackets().sendRunScript(108,
								new Object[] { "Enter Amount:" });
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
						player.getInventory().sendExamine(slotId);
				}
			} else if (interfaceId == 672) {
				if (componentId == 16) {
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						Summoning.sendCreatePouch(player, slotId, 1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						Summoning.sendCreatePouch(player, slotId, 5);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						Summoning.sendCreatePouch(player, slotId, 10);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						Summoning.sendCreatePouch(player, slotId,
								Integer.MAX_VALUE);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
						Summoning.sendCreatePouch(player, slotId, 28);// x
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET) {
						player.getPackets().sendGameMessage(
								"You currently need "
										+ ItemDefinitions.getItemDefinitions(
												slotId)
												.getCreateItemRequirements());
					}
				}
			} else if (interfaceId == 671) {
				if (player.getFamiliar() == null
						|| player.getFamiliar().getBob() == null)
					return;
				if (componentId == 27) {
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getFamiliar().getBob().removeItem(slotId, 1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						player.getFamiliar().getBob().removeItem(slotId, 5);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getFamiliar().getBob().removeItem(slotId, 10);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getFamiliar().getBob()
						.removeItem(slotId, Integer.MAX_VALUE);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
						player.getTemporaryAttributtes().put("bob_item_X_Slot",
								slotId);
						player.getTemporaryAttributtes().put("bob_isRemove",
								Boolean.TRUE);
						player.getPackets().sendRunScript(108,
								new Object[] { "Enter Amount:" });
					}
				} else if (componentId == 29)
					player.getFamiliar().takeBob();
			} else if (interfaceId == 916) {
				SkillsDialogue.handleSetQuantityButtons(player, componentId);
			} else if (interfaceId == 1165) {
				//if (componentId == 22)
					//Summoning.closeDreadnipInterface(player);
			} else if (interfaceId == 880) {
				if (componentId >= 7 && componentId <= 19)
					Familiar.setLeftclickOption(player, (componentId - 7) / 2);
				else if (componentId == 21)
					Familiar.confirmLeftOption(player);
				else if (componentId == 25)
					Familiar.setLeftclickOption(player, 7);
			} else if (interfaceId == 662) {
				if (player.getFamiliar() == null) {
					if (player.getPet() == null) {
						return;
					}
					if (componentId == 49) 
						player.getPet().call();
					else if (componentId == 51) 
						player.getDialogueManager().startDialogue("DismissD");
					return;
				}
				if (componentId == 49)
					player.getFamiliar().call();
				else if (componentId == 51)
					player.getDialogueManager().startDialogue("DismissD");
				else if (componentId == 62)
					player.getFamiliar().takeBob();
				else if (componentId == 69)
					player.getFamiliar().renewFamiliar();
				else if (componentId >= 68 && componentId <= 195) {
					if (player.getFamiliar().getSpecialAttack() == SpecialAttack.CLICK)
						player.getFamiliar().setSpecial(true);
					if (player.getFamiliar().hasSpecialOn())
						player.getFamiliar().submitSpecial(player);
				}
			} else if (interfaceId == 747) {
				if (componentId == 7) {
					Familiar.selectLeftOption(player);
				} else if (player.getPet() != null) {
					if (componentId == 11 || componentId == 20) {
						player.getPet().call();
					} else if (componentId == 12 || componentId == 21) {
						player.getDialogueManager().startDialogue("DismissD");
					} else if (componentId == 10 || componentId == 19) {
						player.getPet().sendFollowerDetails();
					}
				} else if (player.getFamiliar() != null) {
					if (componentId == 11 || componentId == 20)
						player.getFamiliar().call();
					else if (componentId == 12 || componentId == 21)
						player.getDialogueManager().startDialogue("DismissD");
					else if (componentId == 13 || componentId == 22)
						player.getFamiliar().takeBob();
					else if (componentId == 14 || componentId == 23)
						player.getFamiliar().renewFamiliar();
					else if (componentId == 19 || componentId == 10)
						player.getFamiliar().sendFollowerDetails();
					else if (componentId == 18) {
						if (player.getFamiliar().getSpecialAttack() == SpecialAttack.CLICK)
							player.getFamiliar().setSpecial(true);
						if (player.getFamiliar().hasSpecialOn())
							player.getFamiliar().submitSpecial(player);
					}
				}
				
			}else if(interfaceId == 763) {
				if(componentId == 0) {
					if(packetId == 216) 
						player.getBank().depositItem(slotId, 1, true);
					else if(packetId == 19) 
						player.getBank().depositItem(slotId, 5, true);
					else if(packetId == 193) 
						player.getBank().depositItem(slotId, 193, true);
					else if (packetId == 76)
						player.getBank().depositLastAmount(slotId);
					else if (packetId == 173) {
						player.getTemporaryAttributtes().put("bank_item_X_Slot", slotId);
						player.getTemporaryAttributtes().remove("bank_isWithdraw");
						player.getPackets().sendRunScript(108, new Object[]{"Enter Amount:"});
					}else if (packetId == 89)
						player.getBank().depositItem(slotId, Integer.MAX_VALUE, true);
				}
			}else if (interfaceId == 884) {
				if(componentId == 4)
					player.getCombatDefinitions().switchUsingSpecialAttack();
				else if (componentId >= 11 && componentId <= 14)
					player.getCombatDefinitions().setAttackStyle(componentId-11);
				else if (componentId == 15)
					player.getCombatDefinitions().switchAutoRelatie();
			}
			System.out.println("InterfaceId "+interfaceId+", componentId "+componentId+", slotId "+slotId+", PacketId: "+packetId);
		}else if (packetId == ENTER_INTEGER_PACKET) {
			if(!player.isRunning() || player.isDead())
				return;
			int value = stream.readInt();
			if(player.getInterfaceManager().containsInterface(762)
					&& player.getInterfaceManager().containsInterface(763)) {
				if(value < 0)
					return;
				Integer bank_item_X_Slot = (Integer) player.getTemporaryAttributtes().get("bank_item_X_Slot");
				if(bank_item_X_Slot == null)
					return;
				if(player.getTemporaryAttributtes().containsKey("bank_isWithdraw"))
					player.getBank().withdrawItem(bank_item_X_Slot, value);
				else
					player.getBank().depositItem(bank_item_X_Slot, value, true);
				player.getTemporaryAttributtes().remove("bank_item_X_Slot");
			} else if (player.getInterfaceManager().containsInterface(335)
					&& player.getInterfaceManager().containsInterface(336)) {
				if (value < 0)
					return;
				Integer trade_item_X_Slot = (Integer) player
						.getTemporaryAttributtes().remove("trade_item_X_Slot");
				if (trade_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("trade_isRemove") != null)
					player.getTrade().removeItem(trade_item_X_Slot, value);
				else
					player.getTrade().addItem(trade_item_X_Slot, value);
			} else if (player.getInterfaceManager().containsInterface(671)
					&& player.getInterfaceManager().containsInterface(665)) {
				if (player.getFamiliar() == null
						|| player.getFamiliar().getBob() == null)
					return;
				if (value < 0)
					return;
				Integer bob_item_X_Slot = (Integer) player
						.getTemporaryAttributtes().remove("bob_item_X_Slot");
				if (bob_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("bob_isRemove") != null)
					player.getFamiliar().getBob()
					.removeItem(bob_item_X_Slot, value);
				else
					player.getFamiliar().getBob()
					.addItem(bob_item_X_Slot, value);
			} else if (player.getTemporaryAttributtes().get("kilnX") != null) {
				int index = (Integer) player.getTemporaryAttributtes().get("scIndex");
				int componentId = (Integer) player.getTemporaryAttributtes().get("scComponentId");
				int itemId = (Integer) player.getTemporaryAttributtes().get("scItemId");
				player.getTemporaryAttributtes().remove("kilnX");
				if (StealingCreation.proccessKilnItems(player, componentId, index, itemId, value))
					return;
			}
		} else if (packetId == PLAYER_OPTION4_PACKET) {
			@SuppressWarnings("unused")
			boolean unknown = stream.readByteC() == 1;
			int playerIndex = stream.readShort();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			player.stopAll(false);
			if(player.isCantTrade()) {
				player.getPackets().sendGameMessage("You are busy.");
				return;
			}
			if (p2.getInterfaceManager().containsScreenInter() || p2.isCantTrade()) {
				player.getPackets().sendGameMessage("The other player is busy.");
				return;
			}
			if (!p2.withinDistance(player, 14)) {
				player.getPackets().sendGameMessage(
						"Unable to find target "+p2.getDisplayName());
				return;
			}

			if (p2.getTemporaryAttributtes().get("TradeTarget") == player) {
				p2.getTemporaryAttributtes().remove("TradeTarget");
				player.getTrade().openTrade(p2);
				p2.getTrade().openTrade(player);
				return;
			}
			player.faceEntity(p2);
			player.getTemporaryAttributtes().put("TradeTarget", p2);
			player.getPackets().sendGameMessage("Sending " + p2.getDisplayName() + " a request...");
			p2.getPackets().sendTradeRequestMessage(player);
		} else if (packetId == PLAYER_TRADE_OPTION_PACKET) {
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			int playerIndex = stream.readShort();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			player.stopAll(false);
			if(player.isCantTrade()) {
				player.getPackets().sendGameMessage("You are busy.");
				return;
			}
			if (p2.getInterfaceManager().containsScreenInter() || p2.isCantTrade()) {
				player.getPackets().sendGameMessage("The other player is busy.");
				return;
			}
			if (!p2.withinDistance(player, 14)) {
				player.getPackets().sendGameMessage(
						"Unable to find target "+p2.getDisplayName());
				return;
			}

			if (p2.getTemporaryAttributtes().get("TradeTarget") == player) {
				p2.getTemporaryAttributtes().remove("TradeTarget");
				player.getTrade().openTrade(p2);
				p2.getTrade().openTrade(player);
				return;
			}
			player.getTemporaryAttributtes().put("TradeTarget", p2);
			player.getPackets().sendGameMessage("Sending " + p2.getDisplayName() + " a request...");
			p2.getPackets().sendTradeRequestMessage(player);
		}else if (packetId == ITEM_OPTION1_PACKET) {
			if(!clicked) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			clicked = false;
			int interfaceId = stream.readUnsignedShort();
			if(player.isDead() || Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			if(player.getStopDelay() > System.currentTimeMillis())
				return;
			if(!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			int componentId = stream.readUnsignedShort();
			if(componentId == 65535)
				componentId = -1;
			if(componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			int itemId = stream.readUnsignedShort128();
			int slotId = stream.readUnsignedShortLE();
			if(interfaceId == 387 && componentId == 29) {
				if(slotId >= 14 || player.getInterfaceManager().containsInventoryInter())
					return;	
				Item item = player.getEquipment().getItem(slotId);
				if(item == null || item.getId() != itemId || !player.getInventory().addItem(item.getId(), item.getAmount()))
					return;
				player.getEquipment().getItems().set(slotId, null);
				player.getEquipment().refresh(slotId);
				player.getAppearence().generateAppearenceData();
			}
		}else if(packetId == WEAR_ITEM_PACKET) {
			if(!clicked) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			clicked = false;
			int interfaceId = stream.readUnsignedShort();
			
			if(player.isDead() || Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			if(player.getStopDelay() > System.currentTimeMillis())
				return;
			if(!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			int componentId = stream.readUnsignedShort();
			if(componentId == 65535)
				componentId = -1;
			if(componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			int itemId = stream.readUnsignedShortLE128();
			int slotId = stream.readUnsignedShortLE128();
			if(interfaceId == 149 && componentId == 0) {
				if(slotId >= 28 || player.getInterfaceManager().containsInventoryInter())
					return;
				Item item = player.getInventory().getItem(slotId);
				if(item == null || item.getId() != itemId || item.getDefinitions().isNoted() || !item.getDefinitions().isWearItem())
					return;
				int targetSlot = Equipment.getItemSlot(itemId);
				if(targetSlot == -1)
					return;
				player.stopAll();
				boolean isTwoHandedWeapon = targetSlot == 3 && Equipment.isTwoHandedWeapon(item);
				if(isTwoHandedWeapon && !player.getInventory().hasFreeSlots() && player.getEquipment().hasShield()) {
					player.getPackets().sendGameMessage("Not enough free space in your inventory.");
					return;
				}
				HashMap<Integer, Integer> requiriments = item.getDefinitions().getWearingSkillRequiriments();
				boolean hasRequiriments = true;
				if(requiriments != null) {
					for(int skillId : requiriments.keySet()) {
						if(skillId > 24 || skillId < 0)
							continue;
						int level = requiriments.get(skillId);
						if(level < 0 || level > 120)
							continue;
						if(player.getSkills().getLevelForXp(skillId) < level) {
							if(hasRequiriments)
								player.getPackets().sendGameMessage("You are not high enough level to use this item.");
							hasRequiriments = false;
							String name = Skills.SKILL_NAME[skillId].toLowerCase();
							player.getPackets().sendGameMessage("You need to have a"+ (name.startsWith("a") ? "n": "")+" "+name+" level of "+level+".");
						}
						
					}
				}
				if(!hasRequiriments)
					return;
				player.getInventory().deleteItem(slotId, item);
				if (targetSlot == 3) {
					if (isTwoHandedWeapon && player.getEquipment().getItem(5) != null) {
						if (!player.getInventory().addItem(
								player.getEquipment().getItem(5).getId(),
								player.getEquipment().getItem(5).getAmount())) {
							player.getInventory().addItem(itemId, item.getAmount());
							return;
						}
						player.getEquipment().getItems().set(5, null);
					}
				} else if (targetSlot == 5) {
					if (player.getEquipment().getItem(3) != null
							&& Equipment.isTwoHandedWeapon(player.getEquipment().getItem(3))) {
						if (!player.getInventory().addItem(
								player.getEquipment().getItem(3).getId(),
								player.getEquipment().getItem(3).getAmount())) {
							player.getInventory().addItem(itemId, item.getAmount());
							return;
						}
						player.getEquipment().getItems().set(3, null);
					}

				}
				if (player.getEquipment().getItem(targetSlot) != null
						&& (itemId != player.getEquipment().getItem(targetSlot)
								.getId() || !item.getDefinitions()
								.isStackable())) {
					player.getInventory().addItem(
							player.getEquipment().getItem(targetSlot)
									.getId(),
									player.getEquipment().getItem(targetSlot).getAmount());
					player.getEquipment().getItems().set(targetSlot, null);
				}
				int oldAmt = 0;
				if (player.getEquipment().getItem(targetSlot) != null) {
					oldAmt = player.getEquipment().getItem(targetSlot).getAmount();
				}
				Item item2 = new Item(itemId, oldAmt + item.getAmount());
				player.getEquipment().getItems().set(targetSlot, item2);
				player.getEquipment().refresh(targetSlot, targetSlot == 3 ? 5 : targetSlot == 3 ? 0 : 3);
				player.getAppearence().generateAppearenceData();
				player.getPackets().sendSound(2248, 0);
				player.getCombatDefinitions().desecreaseSpecialAttack(0);
			}
			if (itemId == 7927 || itemId == 6583 || itemId == 22560) {
				/*if (!player.getControlerManager().canTransformIntoNPC()) {
					player.sendMessage("You cannot do that right now.");
					return;
				}*/
				int npcId = -1;
				player.stopAll();
				player.lock();
				player.getInterfaceManager().sendInventoryInterface(375);
				player.getTemporaryAttributtes().put("RingNPC", Boolean.TRUE);
				switch (itemId) {
				case 7927:
					int[] randomEggs = { 3689, 3690, 3691, 3692, 3693, 3664 };
					npcId = Utils.random(randomEggs);
					break;
				case 6583:
				case 22560:
					npcId = 2626;
					break;
				}
				player.getAppearence().transformIntoNPC(npcId);
				return;
			}
		}else if (packetId == EXAMINE_NPC_PACKET) {
			int npcId = stream.readUnsignedShortLE();
			player.sm(npcId + "");
		}else if (packetId == EXAMINE_ITEM_PACKET) {
			int junk2 = stream.readUnsignedShortLE();
			int interfaceHash = stream.readIntV2();
			int interfaceId = interfaceHash;
			int componentId = interfaceHash - (interfaceId);
			int itemId = stream.readUnsignedShort();
			int slotId = stream.readUnsignedShortLE128();
			player.sm(itemId + " " + slotId + " " + junk2 + " " + interfaceHash + " " + interfaceId + " " + componentId);
		}else if (packetId == ITEM_OPERATE_PACKET) {
			InventoryOptionsHandler.itemsummon(player, stream);
		}else if (packetId == ITEM_ON_NPC_PACKET) {
			InventoryOptionsHandler.itemOnNpc(player, stream);
		}else if (packetId == ITEM_SELECT_PACKET) {
			if(!clicked) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			clicked = false;
			int interfaceId = stream.readUnsignedShort();
			if(player.isDead() || Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			if(player.getStopDelay() > System.currentTimeMillis())
				return;
			if(!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			int componentId = stream.readUnsignedShort();
			if(componentId == 65535)
				componentId = -1;
			if(componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			int itemId = stream.readUnsignedShort();
			int slotId = stream.readUnsignedShort128();
			if(interfaceId == 149 && componentId == 0) {
				if(slotId >= 28 || player.getInterfaceManager().containsInventoryInter())
					return;
				Item item = player.getInventory().getItem(slotId);
				if(item == null || item.getId() != itemId)
					return;
				player.stopAll();
			if (itemId == HunterEquipment.BOX.getId()) // almost done
				player.getActionManager().setAction(new BoxAction(HunterEquipment.BOX));
			else if (itemId == HunterEquipment.BRID_SNARE.getId())
				player.getActionManager().setAction(
						new BoxAction(HunterEquipment.BRID_SNARE));
				if(Foods.eat(player, item, slotId))
					return;
				if (Pots.pot(player, item, slotId))
					return;
				if(itemId == 2520){
					ToyHorsey.useHorsey(player);
					return;
				}
				if (itemId == 405) {
					final Item items = getRandom();
					final ItemDefinitions defs = ItemDefinitions
							.getItemDefinitions(items.getId());
					player.getPackets().sendGameMessage("You slowly open the casket...");
					WorldTasksManager.schedule(new WorldTask() {

						@Override
						public void run() {
							player.getInventory().deleteItem(405, 1);
							player.getInventory().addItem(items);
							player.getPackets().sendGameMessage("...And receive "
									+ (items.getId() == 995 ? "some"
											: items.getAmount() == 1 ? (defs.getName()
													.toLowerCase().startsWith("u") ? "an"
													: "a")
													: "some") + " " + defs.getName()
									+ ".");
							this.stop();
						}
					}, 0, 1);
					return;
				}
				Bone bone = Bone.forId(itemId);
				if (bone != null) {
					Bone.bury(player, slotId);
					return;
				} else if (itemId == 952) // spade
					dig(player);
				return;	
			}
		} else if (packetId == ITEM_ON_ITEM_PACKET) {
			InventoryOptionsHandler.handleItemOnItem(player, stream);
		} else if (packetId == ITEM_ON_OBJECT_PACKET) {
			InventoryOptionsHandler.handleItemObject(player, stream);
		} else if (packetId == ITEM_DROP_PACKET) {
			if(!clicked) {
				return;
			}
			clicked = false;
			int interfaceId = stream.readUnsignedShort();
			
			if(player.isDead() || Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				return;
			}
			if(player.getStopDelay() > System.currentTimeMillis())
				return;
			if(!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			int componentId = stream.readUnsignedShort();
			if(componentId == 65535)
				componentId = -1;
			if(componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
				return;
			}
			int slotId = stream.readUnsignedShort128();
			int itemId = stream.readUnsignedShortLE();
			if(interfaceId == 149 && componentId == 0) {
				if(slotId >= 28 || player.getInterfaceManager().containsInventoryInter())
					return;
				Item item = player.getInventory().getItem(slotId);
				if(item == null || item.getId() != itemId)
					return;
				if(item.getDefinitions().isDestroyItem()) {
					player.getDialogueManager().startDialogue("DestroyItemOption", slotId, item);
					return;
				}
				player.getInventory().deleteItem(slotId, item);
				World.addGroundItem(item, new WorldTile(player), player, false, 180, true);
			}
		}else if(packetId == ITEM_TAKE_PACKET) {
			if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			long currentTime =  System.currentTimeMillis();
			if(player.getStopDelay() > currentTime || player.getFreezeDelay() >= currentTime)
				return;
			int y = stream.readShortLE128();
			final int id = stream.readShort128();
			int x = stream.readShortLE128();
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			final int regionId = tile.getRegionId();
			if (!player.getMapRegionsIds().contains(regionId))
				return;
			final FloorItem item = World.getRegion(regionId).getGroundItem(id,
					tile, player);
			if (item == null)
				return;
			player.stopAll(false);
			//if(forceRun)
			//	player.setRun(forceRun);
			player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
				@Override
				public void run() {
					final FloorItem item = World.getRegion(regionId)
							.getGroundItem(id, tile, player);
					if (item == null)
						return;
			/*		if (player.getRights() > 0 || player.isSupporter()) 
						player.getPackets().sendGameMessage("This item was dropped by [Username] "+item.getOwner().getUsername()+ " [DiplayName] "+item.getOwner().getDisplayName());
			*/		player.setNextFaceWorldTile(tile);
					player.addWalkSteps(tile.getX(), tile.getY(), 1);
					World.removeGroundItem(player, item);
				}
			}, 1, 1));
		}else if (packetId == SWITCH_INTERFACE_ITEM_PACKET) {
			if(!clicked) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			clicked = false;
			int interfaceId = stream.readUnsignedShort();
			if(Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			if(!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			int componentId = stream.readUnsignedShort();
			if(componentId == 65535)
				componentId = -1;
			if(componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			@SuppressWarnings("unused")
			int unknownByte = stream.readByteC();
			int fromInterfaceHash = stream.readShort();
			int toInterfaceHash = stream.readShort();
			int toSlot = stream.readUnsignedShort128();
			int fromSlot = stream.readUnsignedShort128();
			
			int toInterfaceId = toInterfaceHash >> 16;
			int toComponentId = toInterfaceHash - (toInterfaceId << 16);
			int fromInterfaceId = fromInterfaceHash >> 16;
			int fromComponentId = fromInterfaceHash - (fromInterfaceId << 16);
			if (Utils.getInterfaceDefinitionsSize() <= fromInterfaceId
					|| Utils.getInterfaceDefinitionsSize() <= toInterfaceId)
				return;
			if (!player.getInterfaceManager()
					.containsInterface(fromInterfaceId)
					|| !player.getInterfaceManager().containsInterface(
							toInterfaceId))
				return;
			if (fromComponentId != -1
					&& Utils.getInterfaceDefinitionsComponentsSize(fromInterfaceId) <= fromComponentId)
				return;
			if (toComponentId != -1
					&& Utils.getInterfaceDefinitionsComponentsSize(toInterfaceId) <= toComponentId)
				return;
			
			if(interfaceId == 149 && componentId == 0) {
				if(toSlot >= player.getInventory().getItemsContainerSize() || fromSlot  >= player.getInventory().getItemsContainerSize()) {
					//hack, or server error or client error
					//player.getSession().getChannel().close();
					return;
				}
				
				player.getInventory().switchItem(fromSlot, toSlot);
			} else if (fromInterfaceId == 762 && toInterfaceId == 762) {
				player.getBank().switchItem(fromSlot, toSlot, fromComponentId,
						toComponentId);
			}
				System.out.println("Switch item " + fromInterfaceId + ", "
						+ fromSlot + ", " + toSlot);
		}else if(packetId == SWITCH_DETAIL) {
			int hash = stream.readInt();
			if(hash != 1057001181) {
				//hack, or server error or client error
				player.getSession().getChannel().close();
				return;
			}
			//done loading region when switch detail or mapregion
		}else if (packetId == DONE_LOADING_REGION) {
			if(!player.clientHasLoadedMapRegion()) {
				//load objects and items here
				player.setClientHasLoadedMapRegion();
			}
			player.refreshSpawnedObjects();
			player.refreshSpawnedItems();
		}else if (packetId == WALKING_PACKET || packetId == MINI_WALKING_PACKET) {
			if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			long currentTime =  System.currentTimeMillis();
			if(player.getStopDelay() > currentTime)
				return;
			if(player.getFreezeDelay() >= currentTime) {
				player.getPackets().sendGameMessage("A magical force stops you from moving.");
				return;
			}
			if(packetId == MINI_WALKING_PACKET)
				length -= 18;
			int coordY = stream.readUnsignedShortLE();
			stream.read128Byte();
			int coordX = stream.readUnsignedShort();
			int steps = (length - 5) / 2;
			if(steps > 25)
				steps = 25;
			player.stopAll();
			if(player.isResting())
				return;
			if(player.addWalkSteps(coordX, coordY))
				for(int step = 0; step < steps; step++)
					if(!player.addWalkSteps(coordX+stream.readByte(), coordY+stream.read128Byte()))
						break;
		}else if (packetId == WEIRD_WALKING_PACKET) {
			/*if(!player.hasStarted() || player.hasFinished())
				return;
			int coordX = stream.readShort();
			int coordY = stream.readShort();
			player.resetWalkSteps();
			player.addWalkSteps(coordX, coordY);*/
		}else if (packetId == PLAYER_OPTION_2_PACKET) {
			if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
		//	@SuppressWarnings("unused")
			//boolean unknown = stream.read128Byte() == 1;
			int playerIndex = stream.readShortLE();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerFollow(p2));
		}else if (packetId == ATTACK_PLAYER) {
			if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			@SuppressWarnings("unused")
			boolean unknown = stream.read128Byte() == 1;
			int playerIndex = stream.readUnsignedShort();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis()
					|| !player.getControlerManager().canPlayerOption1(p2))
				return;
			if (!player.isCanPvp())
				return;
			if (!player.getControlerManager().canAttack(p2))
				return;

			if (!player.isCanPvp() || !p2.isCanPvp()) {
				player.getPackets()
				.sendGameMessage(
						"You can only attack players in a player-vs-player area.");
				return;
			}
			if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
				if (player.getAttackedBy() != p2
						&& player.getAttackedByDelay() > Utils
						.currentTimeMillis()) {
					player.getPackets().sendGameMessage(
							"You are already in combat.");
					return;
				}
				if (p2.getAttackedBy() != player
						&& p2.getAttackedByDelay() > Utils.currentTimeMillis()) {
					if (p2.getAttackedBy() instanceof NPC) {
						p2.setAttackedBy(player); // changes enemy to player,
						// player has priority over
						// npc on single areas
					} else {
						player.getPackets().sendGameMessage(
								"That player is already in combat.");
						return;
					}
				}
			}
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerCombat(p2));
		}else if (packetId == ATTACK_NPC) {
			if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			if(player.getStopDelay() > System.currentTimeMillis())
				return;
			int npcIndex = stream.readUnsignedShortLE();
			@SuppressWarnings("unused")
			boolean unknown = stream.read128Byte() == 1;
			NPC npc = World.getNPCs().get(npcIndex);
			if(npc == null || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()) || !npc.getDefinitions().hasAttackOption())
				return;
			if (!player.getControlerManager().canAttack(npc)) {
				return;
			}
			if (npc instanceof Familiar) {
				Familiar familiar = (Familiar) npc;
				if (familiar == player.getFamiliar()) {
					player.getPackets().sendGameMessage(
							"You can't attack your own familiar.");
					return;
				}
				if (!familiar.canAttack(player)) {
					player.getPackets().sendGameMessage(
							"You can't attack this npc.");
					return;
				}
			} else if (!npc.isForceMultiAttacked()) {
				if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
					if (player.getAttackedBy() != npc
							&& player.getAttackedByDelay() > Utils
							.currentTimeMillis()) {
						player.getPackets().sendGameMessage(
								"You are already in combat.");
						return;
					}
					if (npc.getAttackedBy() != player
							&& npc.getAttackedByDelay() > Utils
							.currentTimeMillis()) {
						player.getPackets().sendGameMessage(
								"This npc is already in combat.");
						return;
					}
				}
			}
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerCombat(npc));
			System.out.println(npc.getId());
		}else if (packetId == INTERFACE_ON_PLAYER) {
			if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			if(player.getStopDelay() > System.currentTimeMillis())
				return;
			@SuppressWarnings("unused")
			boolean unknown = stream.readByteC() == 1;
			@SuppressWarnings("unused")
			int junk2 = stream.readUnsignedShortLE();
			int interfaceHash = stream.readIntV2();
			int interfaceId = interfaceHash >> 16;
			int componentId = interfaceHash - (interfaceId << 16);
			if(Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if(!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if(componentId == 65535)
				componentId = -1;
			if(componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
				return;
			int playerIndex = stream.readUnsignedShort128();
			Player p2 = World.getPlayers().get(playerIndex);
			if(p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			player.stopAll(false);
			switch (interfaceId) {
			case 662:
			case 747:
				if (player.getFamiliar() == null)
					return;
				player.resetWalkSteps();
				if ((interfaceId == 747 && componentId == 15)
						|| (interfaceId == 662 && componentId == 65)
						|| (interfaceId == 662 && componentId == 74)
						|| interfaceId == 747 && componentId == 18) {
					if ((interfaceId == 662 && componentId == 74
							|| interfaceId == 747 && componentId == 24 || interfaceId == 747
							&& componentId == 18)) {
						if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY)
							return;
					}
					if (!player.isCanPvp() || !p2.isCanPvp()) {
						player.getPackets()
						.sendGameMessage(
								"You can only attack players in a player-vs-player area.");
						return;
					}
					if (!player.getFamiliar().canAttack(p2)) {
						player.getPackets()
						.sendGameMessage(
								"You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(
								interfaceId == 662 && componentId == 74
								|| interfaceId == 747
								&& componentId == 18);
						player.getFamiliar().setTarget(p2);
					}
				}
				break;
			case 193:
				switch (componentId) {
				case 28:
				case 32:
				case 24:
				case 20:
				case 30:
				case 34:
				case 26:
				case 22:
				case 29:
				case 33:
				case 25:
				case 21:
				case 31:
				case 35:
				case 27:
				case 23:
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(p2
								.getCoordFaceX(p2.getSize()), p2
								.getCoordFaceY(p2.getSize()), p2.getPlane()));
						if (!player.getControlerManager().canAttack(p2))
							return;
						if (!player.isCanPvp() || !p2.isCanPvp()) {
							player.getPackets()
							.sendGameMessage(
									"You can only attack players in a player-vs-player area.");
							return;
						}
						if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
							if (player.getAttackedBy() != p2
									&& player.getAttackedByDelay() > Utils
									.currentTimeMillis()) {
								player.getPackets()
								.sendGameMessage(
										"That "
												+ (player
														.getAttackedBy() instanceof Player ? "player"
																: "npc")
																+ " is already in combat.");
								return;
							}
							if (p2.getAttackedBy() != player
									&& p2.getAttackedByDelay() > Utils
									.currentTimeMillis()) {
								if (p2.getAttackedBy() instanceof NPC) {
									p2.setAttackedBy(player); // changes enemy
									// to player,
									// player has
									// priority over
									// npc on single
									// areas
								} else {
									player.getPackets()
									.sendGameMessage(
											"That player is already in combat.");
									return;
								}
							}
						}
						player.getActionManager()
						.setAction(new PlayerCombat(p2));
					}
					break;
				}
			case Inventory.INVENTORY_INTERFACE:// Item on player
			/*	if (!player.getControlerManager()
						.processItemOnPlayer(p2, junk2))
					return;*/
				InventoryOptionsHandler.handleItemOnPlayer(player, p2, junk2);
				break;
			case 192:
				switch (componentId) {
				case 25: // air strike
				case 28: // water strike
				case 30: // earth strike
				case 32: // fire strike
				case 34: // air bolt
				case 39: // water bolt
				case 42: // earth bolt
				case 45: // fire bolt
				case 49: // air blast
				case 52: // water blast
				case 58: // earth blast
				case 63: // fire blast
				case 70: // air wave
				case 73: // water wave
				case 77: // earth wave
				case 80: // fire wave
				case 86: // teleblock
				case 84: // air surge
				case 87: // water surge
				case 89: // earth surge
				case 91: // fire surge
				case 99: // storm of armadyl
				case 36: // bind
				case 66: // Sara Strike
				case 67: // Guthix Claws
				case 68: // Flame of Zammy
				case 55: // snare
				case 81: // entangle
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(p2
								.getCoordFaceX(p2.getSize()), p2
								.getCoordFaceY(p2.getSize()), p2.getPlane()));
						if (!player.getControlerManager().canAttack(p2))
							return;
						if (!player.isCanPvp() || !p2.isCanPvp()) {
							player.getPackets()
							.sendGameMessage(
									"You can only attack players in a player-vs-player area.");
							return;
						}
						if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
							if (player.getAttackedBy() != p2
									&& player.getAttackedByDelay() > Utils
									.currentTimeMillis()) {
								player.getPackets()
								.sendGameMessage(
										"That "
												+ (player
														.getAttackedBy() instanceof Player ? "player"
																: "npc")
																+ " is already in combat.");
								return;
							}
							if (p2.getAttackedBy() != player
									&& p2.getAttackedByDelay() > Utils
									.currentTimeMillis()) {
								if (p2.getAttackedBy() instanceof NPC) {
									p2.setAttackedBy(player); // changes enemy
									// to player,
									// player has
									// priority over
									// npc on single
									// areas
								} else {
									player.getPackets()
									.sendGameMessage(
											"That player is already in combat.");
									return;
								}
							}
						}
						player.getActionManager()
						.setAction(new PlayerCombat(p2));
					}
					break;
				}
				break;
			}
		}else if (packetId == INTERFACE_ON_NPC) {
			if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			if(player.getStopDelay() > System.currentTimeMillis())
				return;
			@SuppressWarnings("unused")
			int junk2 = stream.readUnsignedShortLE();
			int npcIndex = stream.readUnsignedShort();
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte128() == 1;
			int interfaceHash = stream.readIntV2();
			int interfaceId = interfaceHash >> 16;
			int componentId = interfaceHash - (interfaceId << 16);
			if(Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if(!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if(componentId == 65535)
				componentId = -1;
			if(componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
				return;
			NPC npc = World.getNPCs().get(npcIndex);
			if(npc == null || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()))
				return;
			if(!npc.getDefinitions().hasAttackOption()) {
				player.getPackets().sendGameMessage("You can't attack this npc.");
				return;
			}
			player.stopAll(false);
			switch (interfaceId) {
		/*	case Inventory.INVENTORY_INTERFACE:
				Item item = player.getInventory().getItem(interfaceSlot);
				if (item == null || !player.getControlerManager().processItemOnNPC(npc, item))
					return;
				InventoryOptionsHandler.handleItemOnNPC(player, npc, item);
				break;*/
			case 662:
			case 747:
				if (player.getFamiliar() == null)
					return;
				player.resetWalkSteps();
				if ((interfaceId == 747 && componentId == 15)
						|| (interfaceId == 662 && componentId == 65)
						|| (interfaceId == 662 && componentId == 74)
						|| interfaceId == 747 && componentId == 18
						|| interfaceId == 747 && componentId == 24) {
					if ((interfaceId == 662 && componentId == 74 || interfaceId == 747
							&& componentId == 18)) {
						if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY)
							return;
					}
					if(npc instanceof Familiar) {
						Familiar familiar = (Familiar) npc;
						if (familiar == player.getFamiliar()) {
							player.getPackets().sendGameMessage("You can't attack your own familiar.");
							return;
						}
						if (!player.getFamiliar().canAttack(familiar.getOwner())) {
							player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
							return;
						}
					}
					if (!player.getFamiliar().canAttack(npc)) {
						player.getPackets()
						.sendGameMessage(
								"You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(
								interfaceId == 662 && componentId == 74
								|| interfaceId == 747
								&& componentId == 18);
						player.getFamiliar().setTarget(npc);
					}
				}
				break;
			case 193:
				switch (componentId) {
				case 28:
				case 32:
				case 24:
				case 20:
				case 30:
				case 34:
				case 26:
				case 22:
				case 29:
				case 33:
				case 25:
				case 21:
				case 31:
				case 35:
				case 27:
				case 23:
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(npc
								.getCoordFaceX(npc.getSize()), npc
								.getCoordFaceY(npc.getSize()), npc.getPlane()));
						if (!player.getControlerManager().canAttack(npc))
							return;
						if (npc instanceof Familiar) {
							Familiar familiar = (Familiar) npc;
							if (familiar == player.getFamiliar()) {
								player.getPackets().sendGameMessage(
										"You can't attack your own familiar.");
								return;
							}
							if (!familiar.canAttack(player)) {
								player.getPackets().sendGameMessage(
										"You can't attack this npc.");
								return;
							}
						} else if (!npc.isForceMultiAttacked()) {
							if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
								if (player.getAttackedBy() != npc
										&& player.getAttackedByDelay() > Utils
										.currentTimeMillis()) {
									player.getPackets().sendGameMessage(
											"You are already in combat.");
									return;
								}
								if (npc.getAttackedBy() != player
										&& npc.getAttackedByDelay() > Utils
										.currentTimeMillis()) {
									player.getPackets().sendGameMessage(
											"This npc is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setAction(
								new PlayerCombat(npc));
					}
					break;
				}
			case 192:
				switch (componentId) {
				case 25: // air strike
				case 28: // water strike
				case 30: // earth strike
				case 32: // fire strike
				case 34: // air bolt
				case 39: // water bolt
				case 42: // earth bolt
				case 45: // fire bolt
				case 49: // air blast
				case 52: // water blast
				case 58: // earth blast
				case 63: // fire blast
				case 70: // air wave
				case 73: // water wave
				case 77: // earth wave
				case 80: // fire wave
				case 84: // air surge
				case 87: // water surge
				case 89: // earth surge
				case 66: // Sara Strike
				case 67: // Guthix Claws
				case 68: // Flame of Zammy
				case 93:
				case 91: // fire surge
				case 36: // bind
				case 55: // snare
				case 81: // entangle
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(npc
								.getCoordFaceX(npc.getSize()), npc
								.getCoordFaceY(npc.getSize()), npc.getPlane()));
						if (!player.getControlerManager().canAttack(npc))
							return;
						if (npc instanceof Familiar) {
							Familiar familiar = (Familiar) npc;
							if (familiar == player.getFamiliar()) {
								player.getPackets().sendGameMessage(
										"You can't attack your own familiar.");
								return;
							}
							if (!familiar.canAttack(player)) {
								player.getPackets().sendGameMessage(
										"You can't attack this npc.");
								return;
							}
						} else if (!npc.isForceMultiAttacked()) {
							if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
								if (player.getAttackedBy() != npc
										&& player.getAttackedByDelay() > Utils
										.currentTimeMillis()) {
									player.getPackets().sendGameMessage(
											"You are already in combat.");
									return;
								}
								if (npc.getAttackedBy() != player
										&& npc.getAttackedByDelay() > Utils
										.currentTimeMillis()) {
									player.getPackets().sendGameMessage(
											"This npc is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setAction(
								new PlayerCombat(npc));
					}
					break;
				}
				break;
			}
		}else if (packetId == NPC_CLICK1_PACKET) {
			NPCHandler.handleOption1(player, stream);
		}else if (packetId == NPC_CLICK2_PACKET) {
			NPCHandler.handleOption2(player, stream);
		}else if(packetId == OBJECT_CLICK1_PACKET) {
			if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			long currentTime =  System.currentTimeMillis();
			if(player.getStopDelay() > currentTime || player.getFreezeDelay() >= currentTime)
				return;
			final int  x = stream.readUnsignedShort();
			@SuppressWarnings("unused")
			int junk = stream.readUnsignedByte128();
			final int id = stream.readUnsignedShortLE();
			final int  y = stream.readUnsignedShortLE();
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			int regionId = tile.getRegionId();
			if(!player.getMapRegionsIds().contains(regionId))
				return;
			WorldObject mapObject = World.getRegion(regionId).getObject(id, tile);
			if(mapObject == null)
				return;
			final WorldObject object = !player.isAtDynamicRegion() ? mapObject : new WorldObject(id, mapObject.getType(), mapObject.getRotation(), x, y, player.getPlane());
			player.stopAll(false);
			final ObjectDefinitions objectDef = object.getDefinitions();
			player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
				@Override
				public void run() {
					player.setNextFaceWorldTile(new WorldTile(object.getCoordFaceX(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation())
							, object.getCoordFaceY(objectDef.getSizeX(),objectDef.getSizeY(), object.getRotation())
							, object.getPlane()));
					if(!player.getControlerManager().processObjectClick1(object))
						return;
					if (CastleWars.handleObjects(player, id))
						return;
					if (object.getId() == 19205)
						Hunter.createLoggedObject(player, object, true);
					HunterNPC hunterNpc = HunterNPC.forObjectId(id);
					if (hunterNpc != null) {
						if (OwnedObjectManager.removeObject(player, object)) {
							player.setNextAnimation(hunterNpc.getEquipment().getPickUpAnimation());
								player.getInventory().getItems().addAll(hunterNpc.getItems());
							player.getInventory().addItem(hunterNpc.getEquipment().getId(), 1);
							player.getSkills().addXp(Skills.HUNTER, hunterNpc.getXp());
						} else {
							player.getPackets().sendGameMessage("This isn't your trap.");
						}
					} else if (id == HunterEquipment.BOX.getObjectId() || id == 19192) {
						if (OwnedObjectManager.removeObject(player, object)) {
							player.setNextAnimation(new Animation(5208));
							player.getInventory().addItem(HunterEquipment.BOX.getId(), 1);
						} else
							player.getPackets().sendGameMessage("This isn't your trap.");
					} else if (id == HunterEquipment.BRID_SNARE.getObjectId() || id == 19174) {
						if (OwnedObjectManager.removeObject(player, object)) {
							player.setNextAnimation(new Animation(5207));
							player.getInventory().addItem(HunterEquipment.BRID_SNARE.getId(), 1);
						} else
							player.getPackets().sendGameMessage("This isn't your trap.");
					}
				/*	else if (object.getDefinitions().name
							.equalsIgnoreCase("door"))
						//if (object.getType() == 0)
						//&& objectDef.containsOption(1, "Open"))
							handleDoor(player, object);*/
					if((id >= 1276 && id <= 1278)
						|| id == 1280
						|| (id >= 1282 && id <= 1289) //dead trees
						|| id == 1291
							|| id == 3033
							|| id == 3034
							|| id == 3036
							|| id == 24168
							|| id == 38784
							|| id == 38785
							|| id == 38786
							|| id == 38787
							|| id == 38788
							|| id == 47598) //normal trees
						player.getSkillExecutor().setSkill(new Woodcutting(object, TreeDefinitions.Normal_Tree));
					else if (id == 1281
							|| id == 3037
							|| id == 38731) //oak trees
						player.getSkillExecutor().setSkill(new Woodcutting(object, TreeDefinitions.Oak_Tree));
					else if (id ==2210
							||id == 2372
							|| id == 38616
							|| id == 139
							|| id == 142) //willow trees
						player.getSkillExecutor().setSkill(new Woodcutting(object, TreeDefinitions.Willow_Tree));
					else if (id == 1307) //maple trees
						player.getSkillExecutor().setSkill(new Woodcutting(object, TreeDefinitions.Maple_Tree));
					else if (id == 1309
							|| id == 38787
							|| id == 38755
							|| id == 5551
							|| id == 5553
							|| id == 5552) //yew trees
						player.getSkillExecutor().setSkill(new Woodcutting(object, TreeDefinitions.Yew_Tree));
				
					else if (id == 1804 && object.getX() == 3115 && object.getY() == 3450) {
						if(!player.getInventory().containsItem(275, 1)) {
							player.getPackets().sendGameMessage("you need a Key to enter.");
						} else{
							WorldObject openedDoor = new WorldObject(object.getId(),
									object.getType(), object.getRotation() - 1,
									object.getX() , object.getY(), object.getPlane());
							if (World.removeTemporaryObject(object, 1200, false)) {
								World.spawnTemporaryObject(openedDoor, 1200, false);
								player.lock(2);
								player.stopAll();
								player.addWalkSteps(
										 3115, player.getY() >= object.getY() ? object.getY() - 1
													: object.getY() , -1, false);
							player.getPackets().sendGameMessage("You pass through the door.");
							}
					}
					}
					else if (id == 36846
							|| id == 24389
							|| id == 24375
							|| id == 24376
							|| id == 24377
							|| id == 24378
							|| id == 24379
							|| id == 24381
							|| id == 24382
							|| id == 24383
							|| id == 24384
							|| id == 21505
							|| id == 21506
							|| id == 21507
							|| id == 21508
							|| id == 25825
							|| id == 25826
							|| id == 25827
							|| id == 25828
							|| id == 26916
							|| id == 26917
							|| id == 28407
							|| id == 28408
							|| id == 28409
							|| id == 28410
							|| id == 32952
							|| id == 32953
							|| id == 32954
							|| id == 32955
							|| id == 34042
							|| id == 34043
							|| id == 34044
							|| id == 34045
							|| id == 34046
							|| id == 34288
							|| id == 34289
							|| id == 34290
							|| id == 34291
							|| id == 35991
							|| id == 35992
							|| id == 36021
							|| id == 36022
							|| id == 36315
							|| id == 36316
							|| id == 36317
							|| id == 36318
							|| id == 36319
							|| id == 36320
							|| id == 36339
							|| id == 36340
							|| id == 36341
							|| id == 36342
							|| id == 36343
							|| id == 36344
							|| id == 36355
							|| id == 36356
							|| id == 36357
							|| id == 36358
							|| id == 36359
							|| id == 36360
							|| id == 36844
							|| id == 36845
							|| id == 36847
							|| id == 36848
							|| id == 36851
							|| id == 36852
							|| id == 36853
							|| id == 36856
							|| id == 36857
							|| id == 36858
							|| id == 36859
							|| id == 36862
							|| id == 36863
							|| id == 36864
							|| id == 36865
							|| id == 4636
							|| id == 4637
							|| id == 4638
							|| id == 4639
							|| id == 4640
							|| id == 4465
							|| id == 4466
							|| id == 4467
							|| id == 4468
							|| id == 4490
							|| id == 4491
							|| id == 4492
							|| id == 3333
							|| id == 3334
							|| id == 3335
							|| id == 3336
							|| id == 1530
							|| id == 1531
							|| id == 1532
							|| id == 1533
							|| id == 1534
							|| id == 1542
							|| id == 1543
							|| id == 1544
							|| id == 1545
							|| id == 1536
							|| id == 1537
							|| id == 92
							|| id == 93
							|| id == 99
							|| id == 3
							|| id == 4
							|| id == 22
							|| id == 81
							|| id == 82	
							|| id == 56324)
						handleDoor(player, object);
					else if (id == 37//gate
							|| id == 4311//gate
							|| id == 4312//gate
							|| id == 4313//gate
							|| id == 6451//gate
							|| id == 6452//gate
							|| id == 7049//gate
							|| id == 7050//gate
							|| id == 7051//gate
							|| id == 7052//gate
							|| id == 8810//gate
							|| id == 8811//gate
							|| id == 8812//gate
							|| id == 8813//gate
							|| id == 9140//gate
							|| id == 9141//gate
							|| id == 9142//gate
							|| id == 10055//gate
							|| id == 10056//gate
							|| id == 10172//gate
							|| id == 11332//gate
							|| id == 11333//gate
							|| id == 12816//gate
							|| id == 12817//gate
							|| id == 12818//gate
							|| id == 12986//gate
							|| id == 12987//gate
							|| id == 12988//gate
							|| id == 12989//gate
							|| id == 14233//gate
							|| id == 14234//gate
							|| id == 14235//gate
							|| id == 14236//gate
							|| id == 14237//gate
							|| id == 14238//gate
							|| id == 14239//gate
							|| id == 14240//gate
							|| id == 14241//gate
							|| id == 14242//gate
							|| id == 14243//gate
							|| id == 14244//gate
							|| id == 14245//gate
							|| id == 14246//gate
							|| id == 14247//gate
							|| id == 14248//gate
							|| id == 15510//gate
							|| id == 15511//gate
							|| id == 15512//gate
							|| id == 15513//gate
							|| id == 15514//gate
							|| id == 15515//gate
							|| id == 15516//gate
							|| id == 15517//gate
							|| id == 15604//gate
							|| id == 15605//gate
							|| id == 20045//gate
							|| id == 20046//gate
							|| id == 20049//gate
							|| id == 20050//gate
							|| id == 20341//gate
							|| id == 20391//gate
							|| id == 21172//gate
							|| id == 21403//gate
							|| id == 21404//gate
							|| id == 21405//gate
							|| id == 21406//gate
							|| id == 21600//gate
							|| id == 21601//gate
							|| id == 21672//gate
							|| id == 21673//gate
							|| id == 21674//gate
							|| id == 21675//gate
							|| id == 21687//gate	
							|| id == 21709//gate
							|| id == 21731//gate
							|| id == 21753//gate
							|| id == 22778//gate
							|| id == 22779//gate
							|| id == 23216//gate
							|| id == 23217//gate
							|| id == 23274//gate
							|| id == 23917//gate
							|| id == 23918//gate
							|| id == 23919//gate
							|| id == 24369//gate
							|| id == 24370//gate
							|| id == 24373//gate
							|| id == 24374//gate
							|| id == 24536//gate
							|| id == 24560//gate
							|| id == 24561//gate
							|| id == 24564//gate
							|| id == 26042//gate
							|| id == 26081//gate
							|| id == 26082//gate
							|| id == 26130//gate
							|| id == 26131//gate
							|| id == 26132//gate
							|| id == 26133//gate
							|| id == 27065//gate
							|| id == 27066//gate
							|| id == 27560//gate
							|| id == 27845//gate
							|| id == 27846//gate
							|| id == 27847//gate
							|| id == 27848//gate
							|| id == 27851//gate
							|| id == 27852//gate
							|| id == 27853//gate
							|| id == 27854//gate
							|| id == 28514//gate
							|| id == 28518//gate
							|| id == 28690//gate
							|| id == 28691//gate
							|| id == 28692//gate
							|| id == 28693//gate
							|| id == 30116//gate
							|| id == 30117//gate
							|| id == 30118//gate
							|| id == 30119//gate
							|| id == 30120//gate
							|| id == 30121//gate
							|| id == 30122//gate
							|| id == 30123//gate
							|| id == 30124//gate
							|| id == 30125//gate
							|| id == 30126//gate
							|| id == 30127//gate
							|| id == 30128//gate
							|| id == 30129//gate
							|| id == 30130//gate
							|| id == 30131//gate
							|| id == 33850//gate
							|| id == 34738//gate
							|| id == 34777//gate
							|| id == 34778//gate
							|| id == 34779//gate
							|| id == 34780//gate
							//|| id == 35549//gate
							|| id == 35550//gate
					//		|| id == 35551//gate
							|| id == 35552//gate
							|| id == 36912//gate
							|| id == 36913//gate
							|| id == 36914//gate
							|| id == 36915//gate
							|| id == 36916//gate
							|| id == 36917//gate
							|| id == 36918//gate
							|| id == 36919//gate
							|| id == 37351//gate
							|| id == 37352//gate
							|| id == 37353//gate
							|| id == 37354//gate
							|| id == 4139//gate
							|| id == 4140//gate
							|| id == 3725//gate
							|| id == 3726//gate
							|| id == 3727//gate
							|| id == 3728//gate
							|| id == 3506//gate
							|| id == 3507//gate
							|| id == 3444//gate
							|| id == 3445//gate
							|| id == 3197//gate
							|| id == 3198//gate
							|| id == 3015//gate
							|| id == 3016//gate
							|| id == 3020//gate
							|| id == 3021//gate
							|| id == 3022//gate
							|| id == 3023//gate
							|| id == 2882//gate
							|| id == 2883//gate
							|| id == 2865//gate
							|| id == 2866//gate
							|| id == 2814//gate
							|| id == 2815//gate
							|| id == 2685//gate
							|| id == 2686//gate
							|| id == 2687//gate
							|| id == 2688//gate
							|| id == 2673//gate
							|| id == 2674//gate
							|| id == 2623//gate
							|| id == 2552//gate
							|| id == 2553//gate
							|| id == 2432//gate
							|| id == 2433//gate
							|| id == 2438//gate
							|| id == 2439//gate
							|| id == 2391//gate
							|| id == 2392//gate
							|| id == 2394//gate
							|| id == 2307//gate
							|| id == 2308//gate
							|| id == 2154//gate
							|| id == 2155//gate
							|| id == 2115//gate
							|| id == 2116//gate
							|| id == 2058//gate
							|| id == 2060//gate
							|| id == 2050//gate
							|| id == 2051//gate
							|| id == 2041//gate
							|| id == 2039//gate
							|| id == 1596//gate
							|| id == 1597//gate
							|| id == 1598//gate
							|| id == 1599//gate
							|| id == 1589//gate
							|| id == 1590//gate
							|| id == 1556//gate
							|| id == 1557//gate
							|| id == 1558//gate
							|| id == 1559//gate
							|| id == 1560//gate
							|| id == 1561//gate
							|| id == 1551//gate
							|| id == 1552//gate
							|| id == 1553//gate
							|| id == 883//gate
							|| id == 190//gate
							|| id == 166//gate
							|| id == 167//gate
							|| id == 94//gate
							|| id == 95//gate
							|| id == 89//gate
							|| id == 90//gate
							|| id == 52//gate
							|| id == 53//gate
							|| id == 47//gate
							|| id == 48//gate
							|| id == 49//gate
							|| id == 50//gate
							|| id == 38//gate
							|| id == 39)//gate
						handleGate(player, object);
					//mining start
					else if (id == 31080//copper rocks
							|| id == 14856
							|| id == 14857
							|| id == 14858
							|| id == 11960
							|| id == 11961
							|| id == 11962
							|| id == 11936
							|| id == 11937
							|| id == 11938
							|| id == 14906
							|| id == 14907
							|| id == 11960
							|| id == 31081
							|| id == 31082
							|| id == 2090
							|| id == 2091
							|| id == 9708)
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Copper_Ore));
					else if (id == 31077//tin rocks
							|| id == 11934
							|| id == 11935
							|| id == 14902
							|| id == 14903
							|| id == 31078
							|| id == 2094
							|| id == 2095
							|| id == 9714
							|| id == 11957
							|| id == 11958
							|| id == 11959
							|| id == 11933)
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Tin_Ore));
					else if (id == 31071//iron rocks
							|| id == 31072
							|| id == 31073
							|| id == 2093
							|| id == 2092
							|| id == 9717
							|| id == 11954
							|| id == 11955
							|| id == 11956
							|| id == 14900
							|| id == 14901
							|| id == 14913
							|| id == 14914
							|| id == 11948
							|| id == 11949
							|| id == 11950)
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Iron_Ore));
					else if (id == 31068//coal rocks
							|| id == 31069
							|| id == 31070
							|| id == 2096
							|| id == 2097
							|| id == 14850
							|| id == 14851
							|| id == 14852
							|| id == 11963
							|| id == 11964
							|| id == 11965
							|| id == 11930
							|| id == 11931
							|| id == 11932)
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Coal_Ore));
					else if (id == 31065//Gold rocks
							|| id == 31066
							|| id == 9720
							|| id == 2098
							|| id == 2099
							|| id == 11951
							|| id == 11952
							|| id == 11953)
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Gold_Ore));
					else if (id == 31086//mithril rocks
							|| id == 31088
							|| id == 2103
							|| id == 2102
							|| id == 14853
							|| id == 11945
							|| id == 11946
							|| id == 11947
							|| id == 11942
							|| id == 11943
							|| id == 11944
							|| id == 14854)
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Mithril_Ore));
					else if (id == 31083//adamant rocks
							|| id == 31085
							|| id == 2104
							|| id == 2105
							|| id == 11939
							|| id == 11940
							|| id == 11941
							|| id == 14862
							|| id == 14863
							|| id == 14864)
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Adamant_Ore));
					else if (id == 14859//rune rocks
							|| id == 4860
							|| id == 2106
							|| id == 2107
							|| id == 14860
							|| id == 14861)
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Runite_Ore));
					else if (id == 2108//clay rocks
							|| id == 2109
							|| id == 14904
							|| id == 14905)
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Clay_Ore));
					else if (id == 11948//silver rocks
							|| id == 11948
							|| id == 11949
							|| id == 11950
							|| id == 2100
							|| id == 2101)
						player.getActionManager().setAction(
								new Mining(object, RockDefinitions.Silver_Ore));
					else if (object.getDefinitions().name
							.equalsIgnoreCase("door"))
						/*if (object.getType() == 0
						&& objectDef.containsOption(1, "Open"))*/
							handleDoor(player, object);
					else if (id == 65365)
						WildernessAgility.GateWalk(player, object);
					else if (id == 65734)
						WildernessAgility.climbCliff(player, object);
					else if (id == 65362)
						WildernessAgility.enterObstaclePipe(player, object.getX(), object.getY());
					else if (id == 64696)
						WildernessAgility.swingOnRopeSwing(player, object);
					else if (id == 64698)
						WildernessAgility.walkLog(player);
					else if (id == 64699)
						WildernessAgility.crossSteppingPalletes(player, object);
					else if (id == 38668)
						if (ShootingStars.EARLY_BIRD_XP == false) {
							player.getSkills().addXp(Skills.MINING, player.getSkills().getLevel(Skills.MINING) * 75);
							ShootingStars.EARLY_BIRD_XP = true;
							player.getDialogueManager().startDialogue("CrashedStar");
							} else
						player.getActionManager().setAction(new Mining(object, RockDefinitions.S1_Star));
					else if (id == 38667)
						if (ShootingStars.EARLY_BIRD_XP == false) {
							player.getSkills().addXp(Skills.MINING, player.getSkills().getLevel(Skills.MINING) * 75);
							ShootingStars.EARLY_BIRD_XP = true;
							player.getDialogueManager().startDialogue("CrashedStar");
							} else
						player.getActionManager().setAction(new Mining(object, RockDefinitions.S2_Star));
					else if (id == 38666)
						if (ShootingStars.EARLY_BIRD_XP == false) {
							player.getSkills().addXp(Skills.MINING, player.getSkills().getLevel(Skills.MINING) * 75);
							ShootingStars.EARLY_BIRD_XP = true;
							player.getDialogueManager().startDialogue("CrashedStar");
							} else
						player.getActionManager().setAction(new Mining(object, RockDefinitions.S3_Star));
					else if (id == 38665)
						if (ShootingStars.EARLY_BIRD_XP == false) {
							player.getSkills().addXp(Skills.MINING, player.getSkills().getLevel(Skills.MINING) * 75);
							ShootingStars.EARLY_BIRD_XP = true;
							player.getDialogueManager().startDialogue("CrashedStar");
							} else
						player.getActionManager().setAction(new Mining(object, RockDefinitions.S4_Star));
					else if (id == 38664)
						if (ShootingStars.EARLY_BIRD_XP == false) {
							player.getSkills().addXp(Skills.MINING, player.getSkills().getLevel(Skills.MINING) * 75);
							ShootingStars.EARLY_BIRD_XP = true;
							player.getDialogueManager().startDialogue("CrashedStar");
							} else
						player.getActionManager().setAction(new Mining(object, RockDefinitions.S5_Star));
					else if (id == 38663)
						if (ShootingStars.EARLY_BIRD_XP == false) {
							player.getSkills().addXp(Skills.MINING, player.getSkills().getLevel(Skills.MINING) * 75);
							ShootingStars.EARLY_BIRD_XP = true;
							player.getDialogueManager().startDialogue("CrashedStar");
							} else
						player.getActionManager().setAction(new Mining(object, RockDefinitions.S6_Star));
					else if (id == 38662)
						if (ShootingStars.EARLY_BIRD_XP == false) {
							player.getSkills().addXp(Skills.MINING, player.getSkills().getLevel(Skills.MINING) * 75);
							ShootingStars.EARLY_BIRD_XP = true;
							player.getDialogueManager().startDialogue("CrashedStar");
							} else
						player.getActionManager().setAction(new Mining(object, RockDefinitions.S7_Star));
					else if (id == 38661)
						if (ShootingStars.EARLY_BIRD_XP == false) {
							player.getSkills().addXp(Skills.MINING, player.getSkills().getLevel(Skills.MINING) * 75);
							ShootingStars.EARLY_BIRD_XP = true;
							player.getDialogueManager().startDialogue("CrashedStar");
							} else
						player.getActionManager().setAction(new Mining(object, RockDefinitions.S8_Star));
					else if (id == 38660)
						if (ShootingStars.EARLY_BIRD_XP == false) {
							player.getSkills().addXp(Skills.MINING, player.getSkills().getLevel(Skills.MINING) * 75);
							ShootingStars.EARLY_BIRD_XP = true;
							player.getDialogueManager().startDialogue("CrashedStar");
							} else
						player.getActionManager().setAction(new Mining(object, RockDefinitions.S9_Star));
					else if (id == 46318
							|| id == 46320
							|| id == 46322
							|| id == 56324)
						player.getSkillExecutor().setSkill(new Woodcutting(object, TreeDefinitions.Ivy));
					else if (id == 1306) //magic trees
						player.getSkillExecutor().setSkill(new Woodcutting(object, TreeDefinitions.Magic_Tree));
					else if (id == 5906) {
						if (player.getSkills().getLevel(Skills.AGILITY) < 42) {
							player.getPackets().sendGameMessage("You need an agility level of 42 to use this obstacle.");
							return;
						}
						player.lock();
						WorldTasksManager.schedule(new WorldTask() {
							int count = 0;

							@Override
							public void run() {
								if(count == 0) {
									player.setNextAnimation(new Animation(2594));
									WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -2 : +2), object.getY(), 0);
									player.setNextForceMovement(new ForceMovement(tile, 4, Utils.getMoveDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
								}else if (count == 2) {
									WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -2 : +2), object.getY(), 0);
									player.setNextWorldTile(tile);
								}else if (count == 5) {
									player.setNextAnimation(new Animation(2590));
									WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -5 : +5), object.getY(), 0);
									player.setNextForceMovement(new ForceMovement(tile, 4, Utils.getMoveDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
								}else if (count == 7) {
									WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -5 : +5), object.getY(), 0);
									player.setNextWorldTile(tile);
								}else if (count == 10) {
									player.setNextAnimation(new Animation(2595));
									WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -6 : +6), object.getY(), 0);
									player.setNextForceMovement(new ForceMovement(tile, 4, Utils.getMoveDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
								}else if (count == 12) {						 
									WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -6 : +6), object.getY(), 0);
									player.setNextWorldTile(tile);
								}else if (count == 14) {
									stop();
									player.unlock();
								}
								count++;
							}

						}, 0, 0);
					}
					
					else if (id == 9369)
						FightPits.enterLobby(player, false);
				 else if (id == 29602 || id == 6483
						 || id == 28716//Obelisk
						 || id == 28719//Obelisk
						 || id == 28722//Obelisk
						 || id == 28725//Obelisk
						 || id == 28728//Obelisk
						 || id == 28731//Obelisk
						 || id == 28734//Obelisk
						 || id == 6484//Obelisk
						 || id == 6486//Obelisk
						 || id == 6487//Obelisk
						 || id == 6489//Obelisk
						 || id == 6490//Obelisk
						 || id == 6492//Obelisk
						 || id == 6493//Obelisk
						 || id == 14825//Obelisk
						 || id == 14826//Obelisk
		 	     		 || id == 14827//Obelisk								
		 	     		 || id == 14828//Obelisk
						 || id == 14829//Obelisk
						 || id == 14830//Obelisk
						 || id == 14831//Obelisk
				       	 || id == 16482//Obelisk
						/* && object.getY() > 3527*/)
					player.getControlerManager().startControler("ObeliskControler", object);
				 else if (id == 9356) 
					FightCaves.enterFightCaves(player);
				 else if (id == 36972){ //altar
						final int maxPrayer = player.getSkills()
						.getLevelForXp(Skills.PRAYER);
				if (player.getPrayer().getPrayerpoints() < maxPrayer) {
					player.lock(5);
					player.getPackets().sendGameMessage(
							"You pray to the gods...", true);

					player.setNextAnimation(new Animation(645));
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							player.getSkills().restorePrayer(
									maxPrayer);
							player.getPackets()
							.sendGameMessage(
									"...and recharged your prayer.",
									true);
						}
					}, 2);
				} else 
					player.getPackets().sendGameMessage(
							"You already have full prayer.");
				}
				 else if (id == 1987) 
					player.setNextWorldTile(new WorldTile(2512, 3478, 0));
				 else if (id == 2020) 
					player.setNextWorldTile(new WorldTile(2511, 3463, 0));
				 else if (id == 37247) 
					player.setNextWorldTile(new WorldTile(2575, 9875, 0));
				 else if (id == 10283) 
					 GnomeAgility.swimriver(player);
				//strongHold of safety
				 else if (id == 29603)
				 player.setNextWorldTile(new WorldTile(3082, 4229, 0));
				 else if (id == 29602)
				 player.setNextWorldTile(new WorldTile(3074, 3456, 0));
				 else if (id == 29589)
				 player.setNextWorldTile(new WorldTile(3083, 3452, 0));
				 else if (id == 29592)
				 player.setNextWorldTile(new WorldTile(3086, 4247, 0));
				 else if (id == 29623)
				 player.setNextWorldTile(new WorldTile(3080, 4235, 0));
				 else if (id == 29660)
					 player.setNextWorldTile(new WorldTile(3149, 4251, 2));
					 else if (id == 29659)
					 player.setNextWorldTile(new WorldTile(3146, 4249, 1));
					 else if (id == 29656)
					 player.setNextWorldTile(new WorldTile(3149, 4244, 2));
					 else if (id == 29655)
					 player.setNextWorldTile(new WorldTile(3146, 4246, 1));
					 else if (id == 29664)
					 player.setNextWorldTile(new WorldTile(3157, 4244, 2));
					 else if (id == 29663)
					 player.setNextWorldTile(new WorldTile(3160, 4246, 1));
					 else if (id == 29668)
					 player.setNextWorldTile(new WorldTile(3157, 4251, 2));
					 else if (id == 29667)
					 player.setNextWorldTile(new WorldTile(3160, 4249, 1));
					 else if (id == 29672)
					 player.setNextWorldTile(new WorldTile(3171, 4271, 3));
					 else if (id == 29671)
					 player.setNextWorldTile(new WorldTile(3174, 4273, 2));
					 else if (id == 29728)
					 player.setNextWorldTile(new WorldTile(3159, 4279, 3));
					 else if (id == 29729)
                     player.setNextWorldTile(new WorldTile(3077, 3462, 0));

					//start of varrock dungeon
					else if (id == 29355 && object.getX() == 3230 && object.getY() == 9904) //varrock dungeon climb to bear
						player.useStairs(828, new WorldTile(3229, 3503, 0), 1, 2);
					else if (id == 24264)
						player.useStairs(833, new WorldTile(3229, 9904, 0), 1, 2);
					else if (id == 24366)
						player.useStairs(828, new WorldTile(3237, 3459, 0), 1, 2);
					else if (id == 882 && object.getX() == 3237 && object.getY() == 3458) 
						player.useStairs(833, new WorldTile(3237, 9858, 0), 1, 2);
					else if (id == 29355 && object.getX() == 3097 && object.getY() == 9867) //edge dungeon climb
						player.useStairs(828, new WorldTile(3096, 3468, 0), 1, 2);
					else if (id == 26934)
						player.useStairs(833, new WorldTile(3097, 9868, 0), 1, 2);
					else if (id == 29355 && object.getX() == 3088 && object.getY() == 9971)
						player.useStairs(828, new WorldTile(3087, 3571, 0), 1, 2);
					else if (id == 65453)
						player.useStairs(833, new WorldTile(3089, 9971, 0), 1, 2);
					else if (id == 12389 && object.getX() == 3116 && object.getY() == 3452)
						player.useStairs(833, new WorldTile(3117, 9852, 0), 1, 2);
					else if (id == 29355 && object.getX() == 3116 && object.getY() == 9852)
						player.useStairs(833, new WorldTile(3115, 3452, 0), 1, 2);
				 else if (id == 42611) {// Magic Portal
					player.getDialogueManager().startDialogue("MagicPortal");
				} else if (object.getDefinitions().name.equalsIgnoreCase("Obelisk") && object.getY() > 3525) {
					//Who the fuck removed the controler class and the code from SONIC!!!!!!!!!!
					//That was an hour of collecting coords :fp: Now ima kill myself.
				}else if (Wilderness.isDitch(id)) {// wild ditch
					//	  player.getDialogueManager().startDialogue(
							//	  "WildernessDitch", object);
							player.getTemporaryAttributtes().put("wildernessditch", object);
							player.getInterfaceManager().sendInterface(382);
				/*}else if (id == 23271) {// wild ditch
					player.getTemporaryAttributtes().put("wildernessditch", object);
					player.getInterfaceManager().sendInterface(382);*/
				} else if (id == 28779) {
                    player.getDialogueManager().startDialogue("BorkEnter");
				}else if (id == 27254) {// Edgeville portal
					player.getPackets().sendGameMessage(
							"You enter the portal...");
					player.useStairs(10584, new WorldTile(3087, 3488, 0), 2, 3,
							"..and are transported to Edgeville.");
					player.addWalkSteps(1598, 4506, -1, false);
				} else if (id == 12202) {// mole entrance
					if(!player.getInventory().containsItem(952, 1)) {
						player.getPackets().sendGameMessage("You need a spade to dig this.");
						return;
					}
					if(player.getX() != object.getX() || player.getY() != object.getY()) {
						player.lock();
						player.addWalkSteps(object.getX(), object.getY());
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								InventoryOptionsHandler.dig(player);
							}

						}, 1);
					}else
						InventoryOptionsHandler.dig(player);
				} else if (id == 12230 && object.getX() == 1752 && object.getY() == 5136) {// mole exit 
					player.setNextWorldTile(new WorldTile(2986, 3316, 0));
				}
				else if (id == 38811 || id == 37929) {// corp beast
					if (object.getX() == 2971 && object.getY() == 4382)
						player.getInterfaceManager().sendInterface(650);
					
					else if (object.getX() == 2918 && object.getY() == 4382) 
						player.stopAll();
						player.setNextWorldTile(new WorldTile(
								player.getX() == 2921 ? 2917 : 2921, player
										.getY(), player.getPlane()));
		                   /*
	                     * Clan Wars Free For all (safe) portals
	                     * 
	                     * Game Made by Kiyomi
	                     */
	                } else if (id == 38698) {
	                	if (player.getSkills().getCombatLevel() < 30) {
	                		player.sendMessage("You need a combat level of at least 30 to enter this portal.");
	                		}else {
	                		player.setNextWorldTile(new WorldTile(2815, 5511, 0));
	                		player.sendMessage("You can fight other players here, but your items are SAFE here.");
	                		player.getControlerManager().startControler("FFA");
	                		}
	                } else if (id == 38700) {
	                	if (player.getSkills().getCombatLevel() < 30) {
	                		player.sendMessage("You need a combat level of at least 30 to leave the game.");
	                		}else {
	                		player.setNextWorldTile(new WorldTile(3270, 3687, 0));
	                		player.sendMessage("You have left the Clan Wars Free-For-All (Safe).");
	                		player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 5 : 1, 789);
	                		}
	                } else if (id == 42219) {
	    					player.getControlerManager().startControler("Soulwars");
				} else if(id == 48803 && player.isKalphiteLairSetted()) {
					player.setNextWorldTile(new WorldTile(3508, 9494, 0));
				} else if(id == 48802 && player.isKalphiteLairEntranceSetted()) {
					player.setNextWorldTile(new WorldTile(3483, 9510, 2));
				} else if(id == 3829) {
					if(object.getX() == 3483 && object.getY() == 9510) {
						player.useStairs(828, new WorldTile(3226, 3108, 0), 1, 2);
					}
				} else if(id == 3832) {
					if(object.getX() == 3508 && object.getY() == 9494) {
						player.useStairs(828, new WorldTile(3509, 9496, 2), 1, 2);
					}
				} else if (id == 9369) {
					player.getControlerManager().startControler("FightPits");
				}/* else if (id == 54019 || id == 54020 || id == 55301)
					PkRank.showRanks(player);*/
				else if (id == 1817 && object.getX() == 2273
						&& object.getY() == 4680) { // kbd lever
					Magic.pushLeverTeleport(player, new WorldTile(3067, 10254,
							0));
				} else if (id == 14315) {
					player.getControlerManager().startControler("PestControlLobby", 1);
				} else if (id == 1816 && object.getX() == 3067
						&& object.getY() == 10252) { // kbd out lever
					Magic.pushLeverTeleport(player,
							new WorldTile(2273, 4681, 0));
				} else if (id == 9369) {
					player.getControlerManager().startControler("FightPits");
				} else if (id == 32015 && object.getX() == 3069
						&& object.getY() == 10256) { // kbd stairs
					player.useStairs(828, new WorldTile(3017, 3848, 0), 1, 2);
					player.getControlerManager().startControler("Wilderness");
				} else if (id == 1765 && object.getX() == 3017
						&& object.getY() == 3849) { // kbd out stairs
					player.stopAll();
					player.setNextWorldTile(new WorldTile(3069, 10255, 0));
					player.getControlerManager().forceStop();
				} else if (id == 14315) {
					player.getControlerManager().startControler("PestControlLobby", 1);
				} else if (id == 5959) {
					Magic.pushLeverTeleport(player,
							new WorldTile(2539, 4712, 0));
				} else if (id == 5960) {
					Magic.pushLeverTeleport(player,
							new WorldTile(3089, 3957, 0));
				} else if (id == 1814) {
					Magic.pushLeverTeleport(player,
							new WorldTile(3155, 3923, 0));
				} else if (id == 1815) {
					Magic.pushLeverTeleport(player,
							new WorldTile(2561, 3311, 0));
				}
					
					//
				else if (id == 6226  && object.getX() == 3019 && object.getY() == 9740) 
					player.useStairs(828, new WorldTile(3019, 3341, 0), 1, 2);
				else if (id == 6226  && object.getX() == 3019 && object.getY() == 9738) 
					player.useStairs(828, new WorldTile(3019, 3337, 0), 1, 2);
				else if (id == 6226  && object.getX() == 3018 && object.getY() == 9739) 
					player.useStairs(828, new WorldTile(3017, 3339, 0), 1, 2);
				else if (id == 6226  && object.getX() == 3020 && object.getY() == 9739) 
					player.useStairs(828, new WorldTile(3021, 3339, 0), 1, 2);
					else if (id == 2295)
						GnomeAgility.walkGnomeLog(player);
					else if (id == 2285)
						GnomeAgility.climbGnomeObstacleNet(player);
					else if (id == 35970)
						GnomeAgility.climbUpGnomeTreeBranch(player);
					else if (id == 2312)
						GnomeAgility.walkGnomeRope(player);
					else if (id == 4059)
						GnomeAgility.walkBackGnomeRope(player);
					else if (id == 2314)
						GnomeAgility.climbDownGnomeTreeBranch(player);
					else if (id == 2286)
						GnomeAgility.climbGnomeObstacleNet2(player);
					else if (id == 43543 || id == 43544)
						GnomeAgility.enterGnomePipe(player, object.getX(), object.getY());
					//BarbarianOutpostAgility start
				else if (id == 20210) 
					BarbarianOutpostAgility.enterObstaclePipe(player, object);
				else if (id == 43526)
					BarbarianOutpostAgility.swingOnRopeSwing(player, object);
				else if (id == 43595 && x == 2550 && y == 3546)
					BarbarianOutpostAgility.walkAcrossLogBalance(player, object);
				else if (id == 20211 && x == 2538 && y == 3545)
					BarbarianOutpostAgility.climbObstacleNet(player, object);
				else if (id == 2302 && x == 2535 && y == 3547)
					BarbarianOutpostAgility.walkAcrossBalancingLedge(player, object);
				else if (id == 1948)
					BarbarianOutpostAgility.climbOverCrumblingWall(player, object);
				else if (id == 43533)
					BarbarianOutpostAgility.runUpWall(player, object);
				else if (id == 43597) 
					BarbarianOutpostAgility.climbUpWall(player, object);
				else if (id == 43587)
					BarbarianOutpostAgility.fireSpringDevice(player, object);
				else if (id == 43527)
					BarbarianOutpostAgility.crossBalanceBeam(player, object);
				else if (id == 43531)
					BarbarianOutpostAgility.jumpOverGap(player, object);
				else if (id == 43532)
					BarbarianOutpostAgility.slideDownRoof(player, object);
				else if (id == 2491)
					player.getActionManager()
					.setAction(
							new EssenceMining(
									object,
									player.getSkills().getLevel(
											Skills.MINING) < 30 ? EssenceDefinitions.Rune_Essence
													: EssenceDefinitions.Pure_Essence));
					//start falador mininig
				else if (id == 30942 && object.getX() == 3019 && object.getY() == 3450) 
					player.useStairs(828, new WorldTile(3020, 9850, 0), 1, 2);
				else if (id == 6226 && object.getX() == 3019 && object.getY() == 9850) 
					player.useStairs(833, new WorldTile(3018, 3450, 0), 1, 2);
				else if (id == 31002 ) 
					player.useStairs(833, new WorldTile(2998, 3452, 0), 1, 2);
				else if (id == 31012) 
					player.useStairs(828, new WorldTile(2996, 9845, 0), 1, 2);
				else if (id == 30943 && object.getX() == 3059 && object.getY() == 9776) 
					player.useStairs(-1, new WorldTile(3061, 3376, 0), 0, 1);
				else if (id == 30944 && object.getX() == 3059 && object.getY() == 3376) 
					player.useStairs(-1, new WorldTile(3058, 9776, 0), 0, 1);
				else if (id == 2112 && object.getX() == 3046 && object.getY() == 9756) {
					if(player.getSkills().getLevelForXp(Skills.MINING) < 60) {
					//	player.getDialogueManager().startDialogue("SimpleNPCMessage", MiningGuildDwarf.getClosestDwarfID(player),"Sorry, but you need level 60 Mining to go in there.");
						return;
					}
					WorldObject openedDoor = new WorldObject(object.getId(),
							object.getType(), object.getRotation() - 1,
							object.getX() , object.getY() + 1, object.getPlane());
					if (World.removeTemporaryObject(object, 1200, false)) {
						World.spawnTemporaryObject(openedDoor, 1200, false);
						player.lock(2);
						player.stopAll();
						player.addWalkSteps(
								3046, player.getY() > object.getY() ? object.getY()
										: object.getY() + 1 , -1, false);
					}
				}else if (id == 2113) {
					if(player.getSkills().getLevelForXp(Skills.MINING) < 60) {
						//player.getDialogueManager().startDialogue("SimpleNPCMessage", MiningGuildDwarf.getClosestDwarfID(player),"Sorry, but you need level 60 Mining to go in there.");
						return;
					}
					player.useStairs(-1, new WorldTile(3021, 9739, 0), 0, 1);
				}else if (id == 6226  && object.getX() == 3019 && object.getY() == 9740) 
					player.useStairs(828, new WorldTile(3019, 3341, 0), 1, 2);
				else if (id == 6226  && object.getX() == 3019 && object.getY() == 9738) 
					player.useStairs(828, new WorldTile(3019, 3337, 0), 1, 2);
				else if (id == 6226  && object.getX() == 3018 && object.getY() == 9739) 
					player.useStairs(828, new WorldTile(3017, 3339, 0), 1, 2);
				else if (id == 6226  && object.getX() == 3020 && object.getY() == 9739) 
					player.useStairs(828, new WorldTile(3021, 3339, 0), 1, 2);
				else if (id == 45076)
					player.getActionManager().setAction(new Mining(object, RockDefinitions.LRC_Gold_Ore));
				else if (id == 26425 && object.getX() == 2863 && object.getY() == 5354) {
					WorldObject openedDoor = new WorldObject(object.getId(),
							object.getType(), object.getRotation() - 1,
							object.getX() , object.getY(), object.getPlane());
					if (World.removeTemporaryObject(object, 1200, false)) {
						World.spawnTemporaryObject(openedDoor, 1200, false);
						player.lock(2);
						player.stopAll();
						player.addWalkSteps(
								 player.getX() >= object.getX() ? object.getX() - 1
											: object.getX(), 5354 , -1, false);
						player.getPackets().sendGameMessage("You pass through the gate.");
						}
				}
				else if (id == 5999)
					player.getActionManager().setAction(new Mining(object, RockDefinitions.LRC_Coal_Ore));
					//champion guild
					else if (id == 24357 && object.getX() == 3188 && object.getY() == 3355) 
						player.useStairs(-1, new WorldTile(3189, 3354, 1), 0, 1);
					else if (id == 24359 && object.getX() == 3188 && object.getY() == 3355) 
						player.useStairs(-1, new WorldTile(3189, 3358, 0), 0, 1);
					else if (id == 1805 && object.getX() == 3191 && object.getY() == 3363) {
						WorldObject openedDoor = new WorldObject(object.getId(),
								object.getType(), object.getRotation() - 1,
								object.getX() , object.getY(), object.getPlane());
						if (World.removeTemporaryObject(object, 1200, false)) {
							World.spawnTemporaryObject(openedDoor, 1200, false);
							player.lock(2);
							player.stopAll();
							player.addWalkSteps(
									3191, player.getY() >= object.getY() ? object.getY() - 1
											: object.getY() , -1, false);
							if(player.getY() >= object.getY())
								player.getDialogueManager().startDialogue("SimpleNPCMessage", 198, "Greetings bolt adventurer. Welcome to the guild of", "Champions.");
						}
					}
				else if (object.getDefinitions().name
							.equalsIgnoreCase("Door"))
						if (object.getType() == 0
						&& objectDef.containsOption(1, "Open"))
							handleDoor(player, object);

					else if (id == 2350 && (object.getX() == 3352 && object.getY() == 3417 && object.getPlane() == 0))
						player.useStairs(832, new WorldTile(3177, 5731, 0), 1, 2);
					else if (id == 2353 && (object.getX() == 3177 && object.getY() == 5730 && object.getPlane() == 0))
						player.useStairs(828, new WorldTile(3353, 3416, 0), 1, 2);
					else if (id == 47120) { //zaros altar
						
						//recharge if needed
						if(player.getSkills().getLevel(Skills.PRAYER) < player.getSkills().getLevelForXp(Skills.PRAYER)) {
							player.addStopDelay(12);
							player.setNextAnimation(new Animation(12563));
							player.getSkills().set(Skills.PRAYER, (int) (player.getSkills().getLevelForXp(Skills.PRAYER)*1.15));
						}
						player.getDialogueManager().startDialogue("ZarosAltar");
					}else if (id == 36776 && (object.getX() == 3204 && object.getY() == 3229 && object.getPlane() == 0)
							|| id == 36778 && (object.getX() == 3204 && object.getY() == 3229 && object.getPlane() == 2))
						player.useStairs(-1, new WorldTile(3205, 3228, 1), 0, 1);
					else if (id == 36773 && (object.getX() == 3204 && object.getY() == 3207 && object.getPlane() == 0)
							|| id == 36775 && (object.getX() == 3204 && object.getY() == 3207 && object.getPlane() == 2))
						player.useStairs(-1, new WorldTile(3205, 3209, 1), 0, 1);
					else if (id == 36777 && (object.getX() == 3204 && object.getY() == 3229 && object.getPlane() == 1))
						player.getDialogueManager().startDialogue("ClimbNoEmoteStairs", new WorldTile(3205, 3228, 2), new WorldTile(3205, 3228, 0));
					else if (id == 36774 && (object.getX() == 3204 && object.getY() == 3207 && object.getPlane() == 1))
						player.getDialogueManager().startDialogue("ClimbNoEmoteStairs", new WorldTile(3205, 3209, 2), new WorldTile(3205, 3209, 0));
					else if (id == 36786)
						player.getDialogueManager().startDialogue("Banker", 4907); 
					else if (id == 42425 && object.getX() == 3220 && object.getY() == 3222) { //zaros portal
						player.useStairs(10256, new WorldTile(3353, 3416, 0), 4, 5, "And you find yourself into a digsite.");
						player.addWalkSteps(3222, 3223, -1, false);
						player.getPackets().sendGameMessage("You examine portal and it aborves you...");
					}else if (id == 46500 && object.getX() == 3351 && object.getY() == 3415) { //zaros portal
						player.useStairs(-1, new WorldTile(Settings.RESPAWN_PLAYER_LOCATION.getX(), Settings.RESPAWN_PLAYER_LOCATION.getY(), Settings.RESPAWN_PLAYER_LOCATION.getPlane()), 2, 3, "You found your way back to home.");
						player.addWalkSteps(3351, 3415, -1, false);
				/*	}else if (id == 23271) {//wild ditch
						player.getTemporaryAttributtes().put("wildernessditch", object);
						player.getInterfaceManager().sendInterface(382);*/
					}
					else if (id == 67053)
						player.useStairs(-1, new WorldTile(3120, 3519, 0), 0, 1);
					else {
						switch (objectDef.name.toLowerCase()) {
						case "trapdoor":
						case "manhole":
							if (objectDef.containsOption(0, "Open")) {
								WorldObject openedHole = new WorldObject(object.getId()+1,
										object.getType(), object.getRotation(), object.getX(),
										object.getY(), object.getPlane());
								//if (World.removeTemporaryObject(object, 60000, true)) {
								player.faceObject(openedHole);
								World.spawnTemporaryObject(openedHole, 60000, true);
								//}
							}
							break;
						case "closed chest":
							if (objectDef.containsOption(0, "Open")) {
								player.setNextAnimation(new Animation(536));
								player.lock(2);
								WorldObject openedChest = new WorldObject(object.getId()+1,
										object.getType(), object.getRotation(), object.getX(),
										object.getY(), object.getPlane());
								//if (World.removeTemporaryObject(object, 60000, true)) {
								player.faceObject(openedChest);
								World.spawnTemporaryObject(openedChest, 60000, true);
								//}
							}
							break;
						case "open chest":
							if (objectDef.containsOption(0, "Search")) 
								player.getPackets().sendGameMessage("You search the chest but find nothing.");
							break;
						case "spiderweb":
							if(object.getRotation() == 2) {
								player.lock(2);
								if (Utils.getRandom(1) == 0) {
									player.addWalkSteps(player.getX(), player.getY() < y ? object.getY()+2 : object.getY() - 1, -1, false);
									player.getPackets().sendGameMessage("You squeeze though the web.");
								} else
									player.getPackets().sendGameMessage(
											"You fail to squeeze though the web; perhaps you should try again.");
							}
							break;
						case "web":
							if (objectDef.containsOption(0, "Slash")) {
								player.setNextAnimation(new Animation(PlayerCombat
										.getWeaponAttackEmote(player.getEquipment()
												.getWeaponId(), player
												.getCombatDefinitions()
												.getAttackStyle())));
								slashWeb(player, object);
							}
							break;
						/*case "anvil":
							if (objectDef.containsOption(0, "Smith")) {
								ForgingBar bar = ForgingBar.getBar(player);
								if (bar != null)
									ForgingInterface.sendSmithingInterface(player, bar);
								else
									player.getPackets().sendGameMessage("You have no bars which you have smithing level to use."); 
							}
							break;*/
						case "tin ore rocks":
							player.getActionManager().setAction(
									new Mining(object, RockDefinitions.Tin_Ore));
							break;
						case "gold ore rocks":
							player.getActionManager().setAction(
									new Mining(object, RockDefinitions.Gold_Ore));
							break;
						case "iron ore rocks":
							player.getActionManager().setAction(
									new Mining(object, RockDefinitions.Iron_Ore));
							break;
						case "silver ore rocks":
							player.getActionManager().setAction(
									new Mining(object, RockDefinitions.Silver_Ore));
							break;
						case "coal rocks":
							player.getActionManager().setAction(
									new Mining(object, RockDefinitions.Coal_Ore));
							break;
						case "clay rocks":
							player.getActionManager().setAction(
									new Mining(object, RockDefinitions.Clay_Ore));
							break;
						case "copper ore rocks":
							player.getActionManager().setAction(
									new Mining(object, RockDefinitions.Copper_Ore));
							break;
						case "adamantite ore rocks":
							player.getActionManager().setAction(
									new Mining(object, RockDefinitions.Adamant_Ore));
							break;
						case "runite ore rocks":
							player.getActionManager().setAction(
									new Mining(object, RockDefinitions.Runite_Ore));
							break;
						case "granite rocks":
							player.getActionManager().setAction(
									new Mining(object, RockDefinitions.Granite_Ore));
							break;
						case "sandstone rocks":
							player.getActionManager().setAction(
									new Mining(object, RockDefinitions.Sandstone_Ore));
							break;
						case "mithril ore rocks":
							player.getActionManager().setAction(new Mining(object, RockDefinitions.Mithril_Ore));
							break;
						case "bank deposit box":
							if (objectDef.containsOption(0, "Deposit"))
								//player.getBank().initDepositBox();
							break;
						case "bank":
						case "bank chest":
						case "bank booth":
						case "counter":
							if (objectDef.containsOption(0, "Bank") || objectDef.containsOption(0, "Use"))
								player.getBank().initBank();
							break;
							// Woodcutting start
						/*case "tree":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.NORMAL));
							break;
						case "evergreen":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.EVERGREEN));
							break;
						case "dead tree":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.DEAD));
							break;
						case "oak":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager()
								.setAction(
										new Woodcutting(object,
												TreeDefinitions.OAK));
							break;
						case "willow":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.WILLOW));
							break;
						case "maple tree":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.MAPLE));
							break;
						case "ivy":
							if (objectDef.containsOption(0, "Chop"))
								player.getActionManager()
								.setAction(
										new Woodcutting(object,
												TreeDefinitions.IVY));
							break;
						case "yew":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager()
								.setAction(
										new Woodcutting(object,
												TreeDefinitions.YEW));
							break;
						case "magic tree":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.MAGIC));
							break;
						case "cursed magic tree":
							if (objectDef.containsOption(0, "Chop down"))
								player.getActionManager().setAction(
										new Woodcutting(object,
												TreeDefinitions.CURSED_MAGIC));
							break;*/
							// Woodcutting end
						case "gate":
						case "large door":
						case "metal door":
							if (object.getType() == 0
							&& objectDef.containsOption(0, "Open"))
								if(!handleGate(player, object))
									handleDoor(player, object);
							break;
						case "door":
							if (object.getType() == 0
							&& (objectDef.containsOption(0, "Open") || objectDef
									.containsOption(0, "Unlock")))
								handleDoor(player, object);
							break;
						case "ladder":
							handleLadder(player, object, 1);
							break;
						case "staircase":
							handleStaircases(player, object, 1);
							break;
						case "small obelisk":
							if (objectDef.containsOption(0, "Renew-points")) {
								int summonLevel = player.getSkills().getLevelForXp(Skills.SUMMONING);
								if(player.getSkills().getLevel(Skills.SUMMONING) < summonLevel) {
									player.lock(3);
									player.setNextAnimation(new Animation(8502));
									player.getSkills().set(Skills.SUMMONING, summonLevel);
									player.getPackets().sendGameMessage(
											"You have recharged your Summoning points.", true);
								}else
									player.getPackets().sendGameMessage("You already have full Summoning points.");
							}
							break;
						case "altar":
							if (objectDef.containsOption(0, "Pray") || objectDef.containsOption(0, "Pray-at")) {
								final int maxPrayer = player.getSkills()
										.getLevelForXp(Skills.PRAYER);
								if (player.getPrayer().getPrayerpoints() < maxPrayer) {
									player.lock(5);
									player.getPackets().sendGameMessage(
											"You pray to the gods...", true);
									player.setNextAnimation(new Animation(645));
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											player.getSkills().restorePrayer(
													maxPrayer);
											player.getPackets()
											.sendGameMessage(
													"...and recharged your prayer.",
													true);
										}
									}, 2);
								} else 
									player.getPackets().sendGameMessage(
											"You already have full prayer.");
								if (id == 6552)
									player.getDialogueManager().startDialogue(
											"AncientAltar");
							}
							break;
						default:
							player.getPackets().sendGameMessage(
									"Nothing interesting happens.");
							break;
						}
					}
					else
						player.getPackets().sendGameMessage("Nothing interesting happens.");
					System.out.println("cliked 1 at object id : "+id+", "+object.getX()+", "+object.getY()+", "+object.getPlane());
				}
				
			},objectDef.getSizeX(), Wilderness.isDitch(id) ? 4 : objectDef
					.getSizeY(), object.getRotation())); 
			//objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
		}else if (packetId == OBJECT_CLICK2_PACKET) {
			if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			long currentTime =  System.currentTimeMillis();
			if(player.getStopDelay() > currentTime || player.getFreezeDelay() >= currentTime)
				return;
			@SuppressWarnings("unused")
			int junk = stream.readUnsigned128Byte();
			int y = stream.readUnsignedShort128();
			final int id = stream.readUnsignedShort();
			int x = stream.readUnsignedShort();
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			int regionId = tile.getRegionId();
			if(!player.getMapRegionsIds().contains(regionId))
				return;
			WorldObject mapObject = World.getRegion(regionId).getObject(id, tile);
			if(mapObject == null)
				return;
			final WorldObject object = !player.isAtDynamicRegion() ? mapObject : new WorldObject(id, mapObject.getType(), mapObject.getRotation(), x, y, player.getPlane());
			player.stopAll(false);
			final ObjectDefinitions objectDef = object.getDefinitions();
			player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
				@Override
				public void run() {
					player.setNextFaceWorldTile(new WorldTile(object.getCoordFaceX(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation())
							, object.getCoordFaceY(objectDef.getSizeX(),objectDef.getSizeY(), object.getRotation())
							, object.getPlane()));
					if(!player.getControlerManager().processObjectClick2(object))
						return;
					else if (object.getDefinitions().name
							.equalsIgnoreCase("furnace"))
						player.getDialogueManager().startDialogue("SmeltingD",
								object);
					else if (id == 11666)
						player.getDialogueManager().startDialogue("SmeltingD",
								object);
					else if (id == 35551 && object.getX() == 3268 && object.getY() == 3228) {
						if(!player.getInventory().containsItem(995, 10)) {
							player.getPackets().sendGameMessage("you need to pay 10gp.");
						}else{
						WorldObject openedDoor = new WorldObject(object.getId(),
								object.getType(), object.getRotation() - 1,
								object.getX() , object.getY(), object.getPlane());
						if (World.removeTemporaryObject(object, 1200, false)) {
							World.spawnTemporaryObject(openedDoor, 1200, false);
							player.lock(2);
							player.stopAll();
							player.addWalkSteps(
									 player.getX() >= object.getX() ? object.getX() - 1
												: object.getX(), 3228 , -1, false);
							/*player.addWalkSteps(
									3268, player.getY() >= object.getY() ? object.getY() - 1
											: object.getY() , -1, false);*/
							player.getInventory().deleteItem(995, 10);
							}
					}
					}
					else if (id == 35549 && object.getX() == 3268 && object.getY() == 3227) {
						if(!player.getInventory().containsItem(995, 10)) {
							player.getPackets().sendGameMessage("you need to pay 10gp.");
						}else{
						WorldObject openedDoor = new WorldObject(object.getId(),
								object.getType(), object.getRotation() - 1,
								object.getX() , object.getY(), object.getPlane());
						if (World.removeTemporaryObject(object, 1200, false)) {
							World.spawnTemporaryObject(openedDoor, 1200, false);
							player.lock(2);
							player.stopAll();
							player.addWalkSteps(
									 player.getX() >= object.getX() ? object.getX() - 1
												: object.getX(), 3227 , -1, false);
							/*player.addWalkSteps(
									3268, player.getY() >= object.getY() ? object.getY() - 1
											: object.getY() , -1, false);*/
							player.getInventory().deleteItem(995, 10);
							}
					}
					}
					else if (id == 36786)
						player.getBank().initBank();
					else if (Clipedobject.BankBooth(id))
						player.getBank().initBank();
					else if (object.getDefinitions().name
							.equalsIgnoreCase("Door"))
						if (object.getType() == 0
						&& objectDef.containsOption(2, "Open"))
							handleDoor(player, object);
					else if (id == 36777 && (object.getX() == 3204 && object.getY() == 3229 && object.getPlane() == 1))
						player.useStairs(-1, new WorldTile(3205, 3228, 2), 0, 1);
					else if (id == 36774 && (object.getX() == 3204 && object.getY() == 3207 && object.getPlane() == 1))
						player.useStairs(-1, new WorldTile(3205, 3209, 2), 0, 1);
					else
						player.getPackets().sendGameMessage("Nothing interesting happens.");
					System.out.println("cliked 2 at object id : "+id+", "+object.getX()+", "+object.getY()+", "+object.getPlane());
				}
			}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
		}else if (packetId == OBJECT_CLICK3_PACKET) {
			if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			long currentTime =  System.currentTimeMillis();
			if(player.getStopDelay() > currentTime || player.getFreezeDelay() >= currentTime)
				return;
			int x = stream.readUnsignedShort();
			final int id = stream.readUnsignedShortLE128();
			int y = stream.readUnsignedShortLE128();
			@SuppressWarnings("unused")
			int junk = stream.readUnsignedByte();
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			int regionId = tile.getRegionId();
			if(!player.getMapRegionsIds().contains(regionId))
				return;
			WorldObject mapObject = World.getRegion(regionId).getObject(id, tile);
			if(mapObject == null)
				return;
			final WorldObject object = !player.isAtDynamicRegion() ? mapObject : new WorldObject(id, mapObject.getType(), mapObject.getRotation(), x, y, player.getPlane());
			player.stopAll(false);
			final ObjectDefinitions objectDef = object.getDefinitions();
			player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
				@Override
				public void run() {
					player.setNextFaceWorldTile(new WorldTile(object.getCoordFaceX(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation())
							, object.getCoordFaceY(objectDef.getSizeX(),objectDef.getSizeY(), object.getRotation())
							, object.getPlane()));
					if(!player.getControlerManager().processObjectClick3(object))
						return;
					player.setNextFaceWorldTile(tile);
					 if (id == 36777 && (object.getX() == 3204 && object.getY() == 3229 && object.getPlane() == 1))
							player.useStairs(-1, new WorldTile(3205, 3228, 0), 0, 1);
						else if (object.getDefinitions().name
								.equalsIgnoreCase("Door"))
							if (object.getType() == 0
							&& objectDef.containsOption(3, "Open"))
								handleDoor(player, object);
					else if (id == 36774 && (object.getX() == 3204 && object.getY() == 3207 && object.getPlane() == 1))
							player.useStairs(-1, new WorldTile(3205, 3209, 0), 0, 1);
					else
						player.getPackets().sendGameMessage("Nothing interesting happens.");
					System.out.println("cliked 2 at object id : "+id+", "+object.getX()+", "+object.getY()+", "+object.getPlane());
				}
			}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
		}else if(packetId == ADD_FRIEND_PACKET) {
			if(!player.hasStarted() || player.hasFinished())
				return;
			player.getFriendsIgnores().addFriend(stream.readString());
		}else if(packetId == REMOVE_FRIEND_PACKET) {
			if(!player.hasStarted() || player.hasFinished())
				return;
			player.getFriendsIgnores().removeFriend(stream.readString());
		}else if(packetId == SEND_FRIEND_MESSAGE_PACKET) {
			if(!player.hasStarted() || player.hasFinished())
				return;
			if (player.getMuted() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage(
						"You temporary muted. Recheck in 30 minutes.");
				return;
			}
			String username = stream.readString();
			Player p2 = World.getPlayerByDisplayName(username);
			if(p2 == null)
				return;
			player.getFriendsIgnores().sendMessage(p2, Huffman.readEncryptedMessage(76, stream));
		}else if(packetId == PUBLIC_CHAT_PACKET) {
			if(!player.hasStarted() || player.hasFinished())
				return;
			int effects = stream.readUnsignedShort();
			String message = Huffman.readEncryptedMessage(76, stream);
			if(message.startsWith("::"))
				//if command exists and processed wont send message as public message
				if(Commands.processCommand(player, message.replace("::", ""), false, false))
					return;
			if (player.getMuted() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage(
						"You temporary muted. Recheck in 48 hours.");
				return;
			}
			player.setNextPublicChatMessage(new PublicChatMessage(message, effects));
		}else if (packetId == QUICK_CHAT_PACKET) {
			//just tells you which client script created packet
			@SuppressWarnings("unused")
			boolean secondClientScript = stream.readByte() == 1;//script 5059 or 5061
			int fileId = stream.readUnsignedShort();
			byte[] data = null;
			if(length > 3) {
				data = new byte[length-3];
				stream.readBytes(data);
			}
			if(fileId == 1)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.AGILITY)};
			else if (fileId == 8)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.ATTACK)};
			else if (fileId == 13)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.CONSTRUCTION)};
			else if (fileId == 16)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.COOKING)};
			else if (fileId == 23)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.CRAFTING)};
			else if (fileId == 30)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.DEFENCE)};
			else if (fileId == 34)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.FARMING)};
			else if (fileId == 41)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.FIREMAKING)};
			else if (fileId == 47)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.FISHING)};
			else if (fileId == 55)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.FLETCHING)};
			else if (fileId == 62)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.HERBLORE)};
			else if (fileId == 70)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.HITPOINTS)};
			else if (fileId == 74)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.HUNTER)};
			else if (fileId == 135)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.MAGIC)};
			else if (fileId == 127)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.MINING)};
			else if (fileId == 120)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.PRAYER)};
			else if (fileId == 116)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.RANGE)};
			else if (fileId == 111)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.RUNECRAFTING)};
			else if (fileId == 103)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.SLAYER)};
			else if (fileId == 96)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.SMITHING)};
			else if (fileId == 92)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.STRENGTH)};
			else if (fileId == 85)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.SUMMONING)};
			else if (fileId == 79)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.THIEVING)};
			else if (fileId == 142)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.WOODCUTTING)};
			player.setNextPublicChatMessage(new QuickChatMessage(fileId, data));
		}else if(packetId == COMMANDS_PACKET) {
			if(!player.isRunning())
				return;
			boolean clientCommand = stream.readUnsignedByte() == 1;
			String command = stream.readString();
			Commands.processCommand(player, command, true, clientCommand);
		}else{
			Logger.log(this, "Missing packet "+packetId+", expected size: "+length+", actual size: "+PACKET_SIZES[packetId]);
		}
	}

	public static boolean handleDoor(Player player, WorldObject object, long timer) {
		if (World.isSpawnedObject(object))
			return false;
		WorldObject openedDoor = new WorldObject(object.getId(),
				object.getType(), object.getRotation() + 1, object.getX(),
				object.getY(), object.getPlane());
		if (object.getRotation() == 0)
			openedDoor.moveLocation(-1, 0, 0);
		else if (object.getRotation() == 1)
			openedDoor.moveLocation(0, 1, 0);
		else if (object.getRotation() == 2)
			openedDoor.moveLocation(1, 0, 0);
		else if (object.getRotation() == 3)
			openedDoor.moveLocation(0, -1, 0);
		if (World.removeTemporaryObject(object, timer, true)) {
			player.faceObject(openedDoor);
			World.spawnTemporaryObject(openedDoor, timer, true);
			return true;
		}
		return false;
	}

	private static boolean handleDoor(Player player, WorldObject object) {
		return handleDoor(player, object, 60000);//60000
	}
	private static boolean handleLadder(Player player, WorldObject object,
			int optionId) {
		String option = object.getDefinitions().getOption(optionId);
		if (option.equalsIgnoreCase("Climb-up")) {
			if (player.getPlane() == 3)
				return false;
			player.useStairs(828, new WorldTile(player.getX(), player.getY(),
					player.getPlane() + 1), 1, 2);
		} else if (option.equalsIgnoreCase("Climb-down")) {
			if (player.getPlane() == 0)
				return false;
			player.useStairs(828, new WorldTile(player.getX(), player.getY(),
					player.getPlane() - 1), 1, 2);
		} else if (option.equalsIgnoreCase("Climb")) {
			if (player.getPlane() == 3 || player.getPlane() == 0)
				return false;
			player.getDialogueManager().startDialogue(
					"ClimbEmoteStairs",
					new WorldTile(player.getX(), player.getY(), player
							.getPlane() + 1),
							new WorldTile(player.getX(), player.getY(), player
									.getPlane() - 1), "Climb up the ladder.",
									"Climb down the ladder.", 828);
		} else
			return false;
		return true;
	}
	private static void slashWeb(Player player, WorldObject object) {

		if (Utils.getRandom(1) == 0) {
			World.spawnTemporaryObject(new WorldObject(object.getId() + 1,
					object.getType(), object.getRotation(), object.getX(),
					object.getY(), object.getPlane()), 60000, true);
			player.getPackets().sendGameMessage("You slash through the web!");
		} else
			player.getPackets().sendGameMessage(
					"You fail to cut through the web.");
	}


	private static boolean handleGate(Player player, WorldObject object) {
		if (World.isSpawnedObject(object))
			return false;
		if (object.getRotation() == 0) {

			boolean south = true;
			WorldObject otherDoor = World.getObject(new WorldTile(
					object.getX(), object.getY() + 1, object.getPlane()),
					object.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object
							.getDefinitions().name)) {
				otherDoor = World.getObject(
						new WorldTile(object.getX(), object.getY() - 1, object
								.getPlane()), object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name
						.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				south = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (south) {
				openedDoor1.moveLocation(-1, 0, 0);
				openedDoor1.setRotation(3);
				openedDoor2.moveLocation(-1, 0, 0);
			} else {
				openedDoor1.moveLocation(-1, 0, 0);
				openedDoor2.moveLocation(-1, 0, 0);
				openedDoor2.setRotation(3);
			}

			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		} else if (object.getRotation() == 2) {

			boolean south = true;
			WorldObject otherDoor = World.getObject(new WorldTile(
					object.getX(), object.getY() + 1, object.getPlane()),
					object.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object
							.getDefinitions().name)) {
				otherDoor = World.getObject(
						new WorldTile(object.getX(), object.getY() - 1, object
								.getPlane()), object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name
						.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				south = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (south) {
				openedDoor1.moveLocation(1, 0, 0);
				openedDoor2.setRotation(1);
				openedDoor2.moveLocation(1, 0, 0);
			} else {
				openedDoor1.moveLocation(1, 0, 0);
				openedDoor1.setRotation(1);
				openedDoor2.moveLocation(1, 0, 0);
			}
			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		} else if (object.getRotation() == 3) {

			boolean right = true;
			WorldObject otherDoor = World.getObject(new WorldTile(
					object.getX() - 1, object.getY(), object.getPlane()),
					object.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object
							.getDefinitions().name)) {
				otherDoor = World.getObject(new WorldTile(object.getX() + 1,
						object.getY(), object.getPlane()), object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name
						.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				right = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (right) {
				openedDoor1.moveLocation(0, -1, 0);
				openedDoor2.setRotation(0);
				openedDoor1.setRotation(2);
				openedDoor2.moveLocation(0, -1, 0);
			} else {
				openedDoor1.moveLocation(0, -1, 0);
				openedDoor1.setRotation(0);
				openedDoor2.setRotation(2);
				openedDoor2.moveLocation(0, -1, 0);
			}
			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		} else if (object.getRotation() == 1) {

			boolean right = true;
			WorldObject otherDoor = World.getObject(new WorldTile(
					object.getX() - 1, object.getY(), object.getPlane()),
					object.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object
							.getDefinitions().name)) {
				otherDoor = World.getObject(new WorldTile(object.getX() + 1,
						object.getY(), object.getPlane()), object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name
						.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				right = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (right) {
				openedDoor1.moveLocation(0, 1, 0);
				openedDoor1.setRotation(0);
				openedDoor2.moveLocation(0, 1, 0);
			} else {
				openedDoor1.moveLocation(0, 1, 0);
				openedDoor2.setRotation(0);
				openedDoor2.moveLocation(0, 1, 0);
			}
			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		}
		return false;
	}
	private static boolean handleStaircases(Player player, WorldObject object,
			int optionId) {
		String option = object.getDefinitions().getOption(optionId);
		if (option.equalsIgnoreCase("Climb-up")) {
			if (player.getPlane() == 3)
				return false;
			player.useStairs(-1, new WorldTile(player.getX(), player.getY(),
					player.getPlane() + 1), 0, 1);
		} else if (option.equalsIgnoreCase("Climb-down")) {
			if (player.getPlane() == 0)
				return false;
			player.useStairs(-1, new WorldTile(player.getX(), player.getY(),
					player.getPlane() - 1), 0, 1);
		} else if (option.equalsIgnoreCase("Climb")) {
			if (player.getPlane() == 3 || player.getPlane() == 0)
				return false;
			player.getDialogueManager().startDialogue(
					"ClimbNoEmoteStairs",
					new WorldTile(player.getX(), player.getY(), player
							.getPlane() + 1),
							new WorldTile(player.getX(), player.getY(), player
									.getPlane() - 1), "Go up the stairs.",
					"Go down the stairs.");
		} else
			return false;
		return false;
	}
	
	public Player getPlayer() {
		return player;
	}

	public static void sendRemove(Player player, int slotId) {
		if (slotId >= 15)
			return;
		player.stopAll(false);
		Item item = player.getEquipment().getItem(slotId);
		if (item == null
				|| !player.getInventory().addItem(item.getId(),
						item.getAmount()))
			return;
		player.getEquipment().getItems().set(slotId, null);
		player.getEquipment().refresh(slotId);
		player.getAppearence().generateAppearenceData();
	/*	if (Runecrafting.isTiara(item.getId()))
			player.getPackets().sendConfig(491, 0);*/
		if (slotId == 3)
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
	}
	public static void refreshEquipBonuses(Player player) {
		player.getPackets().sendIComponentText(667, 28,
				"Stab: +" + player.getCombatDefinitions().getBonuses()[0]);
		player.getPackets().sendIComponentText(667, 29,
				"Slash: +" + player.getCombatDefinitions().getBonuses()[1]);
		player.getPackets().sendIComponentText(667, 30,
				"Crush: +" + player.getCombatDefinitions().getBonuses()[2]);
		player.getPackets().sendIComponentText(667, 31,
				"Magic: +" + player.getCombatDefinitions().getBonuses()[3]);
		player.getPackets().sendIComponentText(667, 32,
				"Range: +" + player.getCombatDefinitions().getBonuses()[4]);
		player.getPackets().sendIComponentText(667, 33,
				"Stab: +" + player.getCombatDefinitions().getBonuses()[5]);
		player.getPackets().sendIComponentText(667, 34,
				"Slash: +" + player.getCombatDefinitions().getBonuses()[6]);
		player.getPackets().sendIComponentText(667, 35,
				"Crush: +" + player.getCombatDefinitions().getBonuses()[7]);
		player.getPackets().sendIComponentText(667, 36,
				"Magic: +" + player.getCombatDefinitions().getBonuses()[8]);
		player.getPackets().sendIComponentText(667, 37,
				"Range: +" + player.getCombatDefinitions().getBonuses()[9]);
		player.getPackets().sendIComponentText(667, 38,
				"Summoning: +" + player.getCombatDefinitions().getBonuses()[10]);
		player.getPackets().sendIComponentText(667, 39, 
				"Absorb Melee: +" + player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_MELEE_BONUS] + "%");
		player.getPackets().sendIComponentText(667, 40,
				"Absorb Magic: +" + player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_MAGE_BONUS] + "%");
		player.getPackets().sendIComponentText(667, 41,
				"Absorb Ranged: +" + player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_RANGE_BONUS]+ "%");
		player.getPackets().sendIComponentText(667, 42,
				"Strength: " + player.getCombatDefinitions().getBonuses()[14]);
		player.getPackets().sendIComponentText(667, 43,
				"Ranged Str: " + player.getCombatDefinitions().getBonuses()[15]);
		player.getPackets().sendIComponentText(667, 44,
				"Prayer: +" + player.getCombatDefinitions().getBonuses()[16]);
		player.getPackets().sendIComponentText(667,45,"Magic Damage: +" + player.getCombatDefinitions().getBonuses()[17] + "%");
	}
	static Item[] COMMON = { new Item(995, 3000), new Item(995, 1000),
		new Item(995, 459), new Item(995, 354), new Item(995, 1500),
		new Item(995, 100), new Item(995, 1), new Item(995, 900),
		new Item(995, 2445), new Item(995, 2350), new Item(1454),
		new Item(1623) };
static Item[] UNCOMMON = { new Item(1621), new Item(1619), new Item(1452) };
static Item[] RARE = { new Item(1617), new Item(985), new Item(987),
		new Item(1462) };

private static Item getCommon() {
	return COMMON[(int) (Math.random() * COMMON.length)];
}

private static Item getUnCommon() {
	return UNCOMMON[(int) (Math.random() * UNCOMMON.length)];
}

private static Item getRare() {
	return RARE[(int) (Math.random() * RARE.length)];
}

private static Item getRandom() {
	int common = Utils.random(5, 7);
	int uncommon = Utils.random(5, 50);
	int rare = Utils.random(0, 100);
	if (common == 5 || common == 7) {
		return getCommon();
	} else if (common != 5 && common != 7 && uncommon == 5
			|| uncommon == 50) {
		return getUnCommon();
	} else if (common != 5 && common != 7 && uncommon != 50
			&& uncommon != 5 && rare == 0 || rare == 100) {
		return getRare();
	} else {
		return getCommon();
	}
}
}
