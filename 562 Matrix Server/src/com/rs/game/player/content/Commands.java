package com.rs.game.player.content;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Summoning;
import com.rs.game.player.actions.Summoning.Pouches;
import com.rs.game.player.content.pet.Pets;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Encrypt;
import com.rs.utils.IPBanL;
import com.rs.utils.PkRank;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;

public final class Commands {
	
//			String computerName = InetAddress.getByName(player.getSession().getIP()).getHostName();
	
	public static void sendYell(Player player, String message,
			boolean isStaffYell) {
		if (player.getMuted() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"You temporary muted. Recheck in 48 hours.");
			return;
		}
		for (Player players : World.getPlayers()) {
			players.getPackets().sendGameMessage(player.getDisplayName() + ": " + message);
		}
	}
	
	public static boolean processCommand(Player player, String command, boolean console,  boolean clientCommand) {
		if(command.length() == 0)
			return false;
		String[] cmd = command.toLowerCase().split(" ");
		if(player.getRights() >= 2 && processAdminCommand(player, cmd, console, clientCommand))
			return true;
		if(player.getRights() >= 1 && processModCommand(player, cmd, console, clientCommand))
			return true;
		if (Settings.ECONOMY) {
			player.getPackets().sendGameMessage(
					"Commands are set to off");
			return true;
		}
		return processNormalCommand(player, cmd, console, clientCommand);
	}
	
	
	/*
	 * extra parameters if you want to check them
	 */
	public static boolean processAdminCommand(final Player player, String[] cmd, boolean console, boolean clientCommand) {
		if(clientCommand) {
			//unused atm
		}else{
			String name;
			Player target;
			
			switch (cmd[0]) {
			case "search":
				int lol = Utils.random(1, 10);
				String color = "";
				if (lol == 1)
					color = "B7FF70";
				if (lol == 2)
					color = "FF0000";
				if (lol == 3)
					color = "A889FF";
				if (lol == 4)
					color = "FF9502";
				if (lol == 5)
					color = "00692F";
				if (lol == 6)
					color = "FFFF71";
				if (lol == 7)
					color = "FF00DC";
				if (lol == 8)
					color = "00FF00";
				if (lol == 9)
					color = "00BFFF";
				if (lol == 10)
					color = "7A7A7A";
			    if (cmd.length < 2) {
			        player.getPackets().sendGameMessage(
			                "Example: ::search whip");
			        return true;
			    } else {
			        String command = "";
			        int loop = 0;
			        for (String s : cmd) {
			            if (loop == 0) {
			                loop++;
			                continue;
			            }
			            if (loop > 1) {
			                command = command + " " + s;
			            } else {
			                command = command + s;
			            }
			            loop++;
			        }
			        player.getPackets().sendPanelBoxMessage(
			        		"<col=ff0000>--------------------------------------------");
			        for (int i = 0; i < Utils.getItemDefinitionsSize(); i++)
			        	if (ItemDefinitions.getItemDefinitions(i) != null) {
			        		ItemDefinitions defs = ItemDefinitions.getItemDefinitions(i);
			        		if (defs.getName().toLowerCase().contains(command.toLowerCase())) {
			        			if (new Item(i).getDefinitions().isNoted())
			        				player.getPackets().sendPanelBoxMessage("<col="+color+">"+new Item (i).getName()
			        						+"<col=ff0000> (noted)</col> - <col=ffff00>"+i);
//			        			else if (new Item(i).getDefinitions().isLended())
//			        				player.getPackets().sendPanelBoxMessage("<col="+color+">"+new Item (i).getName()
//			        						+"<col=ff0000> (lent)</col> - <col=ffff00>"+i);
			        			else
			        				player.getPackets().sendPanelBoxMessage("<col="+color+">"+new Item (i).getName()
			        						+"</col> - <col=ffff00>"+i);
			        		}
			        	}
			    }
				return true;
			case "update":
				int delay = 60;
				if (cmd.length >= 2) {
					try {
						delay = Integer.valueOf(cmd[1]);
					} catch (NumberFormatException e) {
						player.getPackets().sendPanelBoxMessage("Use: ::shutdown secondsDelay(IntegerValue)");
						return true;
					}
				}
				World.safeShutdown(false, delay);
				return true;

			case "npc":
				World.spawnNPC(Integer.valueOf(cmd[1]), new WorldTile(player), -1, true);
				return true;

			case "hairc":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::hairc Id");
					return true;
				}
				try {
					player.getAppearence().setHairColor(Integer.valueOf(cmd[1]));
					player.getAppearence().generateAppearenceData();
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::hairc Id");
					return true;
				}
				return true;

			case "hairs":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::hairs Id");
					return true;
				}
				try {
					player.getAppearence().setHairStyle(Integer.valueOf(cmd[1]));
					player.getAppearence().generateAppearenceData();
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::hairs Id");
					return true;
				}
				return true;

			case "beardc":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::beardc Id");
					return true;
				}
				try {
					player.getAppearence().setFacialHair(Integer.valueOf(cmd[1]));
					player.getAppearence().generateAppearenceData();
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::beardc Id");
					return true;
				}
				return true;
				
			case "beards":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::beards Id");
					return true;
				}
				try {
					player.getAppearence().setBeardStyle(Integer.valueOf(cmd[1]));
					player.getAppearence().generateAppearenceData();
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::beards Id");
					return true;
				}
				return true;

			case "topc":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::topc Id");
					return true;
				}
				try {
					player.getAppearence().setTopColor(Integer.valueOf(cmd[1]));
					player.getAppearence().generateAppearenceData();
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::topc Id");
					return true;
				}
				return true;
				
			case "tops":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::tops Id");
					return true;
				}
				try {
					player.getAppearence().setTopStyle(Integer.valueOf(cmd[1]));
					player.getAppearence().generateAppearenceData();
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::tops Id");
					return true;
				}
				return true;

			case "legsc":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::legsc Id");
					return true;
				}
				try {
					player.getAppearence().setLegsColor(Integer.valueOf(cmd[1]));
					player.getAppearence().generateAppearenceData();
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::legsc Id");
					return true;
				}
				return true;
				
			case "legss":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::legs Id");
					return true;
				}
				try {
					player.getAppearence()
							.setLegsStyle(Integer.valueOf(cmd[1]));
					player.getAppearence().generateAppearenceData();
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::legss Id");
					return true;
				}
				return true;
				
			case "summon":
				Summoning.infusePouches(player);
				return true;

			case "pouch":
				Summoning.spawnFamiliar(player, Pouches.PACK_YAK);
				return true;

			case "work":
				// player.getPackets().sendConfig(168, 8);// tab id
				player.getInterfaceManager().sendInventoryInterface(662);
				return true;

			case "bosses":
				player.getDialogueManager().startDialogue("TeleportBosses");
				return true;
			case "bork":
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3114, 5528, 0));
				return true;

			case "karamja":
				player.getDialogueManager().startDialogue("KaramjaTrip",
						Utils.getRandom(1) == 0 ? 11701 : (Utils.getRandom(1) == 0 ? 11702 : 11703));
				return true;

			case "minigames":
				player.getDialogueManager().startDialogue("TeleportMinigame");
				return true;

			case "getip":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.sm("Couldn't find player " + name + ".");
				} else
					player.sm(target.getDisplayName() + "'s IP is "+ target.getSession().getIP() + ".");
				return true;

			case "spawnplayer":
				Player other = new Player("troll");
				other.init(player.getSession(), "fagit", 0, 0, 0);
				other.setNextWorldTile(player);
				return true;

			case "setlevel":
				if (cmd.length < 3) {
					player.sm("Usage ::setlevel skillId level");
					return true;
				}
				try {
					int skill = Integer.parseInt(cmd[1]);
					int level = Integer.parseInt(cmd[2]);
					player.getSkills().set(skill, level);
					player.getSkills().setXp(skill, Skills.getXPForLevel(level));
					player.getAppearence().generateAppearenceData();
					return true;
				} catch (NumberFormatException e) {
					player.sm("Usage ::setlevel skillId level");
				}
				return true;
				
			case "shop":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::shop Id");
					return true;
				}
				try {
					ShopsHandler.openShop(player, Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::shop Id");
					return true;
				}
				return true;

			case "mute":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setMuted(Utils.currentTimeMillis() + (1 * 60 * 60 * 1000));
					target.sm("You've been muted for 1 hour by " + player.getDisplayName() + ".");
					player.sm("You have muted " + target.getDisplayName() + " for 1 hour.");
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if (!SerializableFilesManager.containsPlayer(name)) {
						player.sm("Account name " + Utils.formatPlayerNameForDisplay(name) + " doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					target.setMuted(Utils.currentTimeMillis() + (1 * 60 * 60 * 1000));
					player.sm("You have muted " + target.getDisplayName() + " for 1 hour.");
					SerializableFilesManager.savePlayer(target);
				}
				return true;

			case "unmuteall":
				for (Player targets : World.getPlayers()) {
					if (player == null)
						continue;
					targets.setMuted(0);
				}
				return true;

			case "unpermban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				File acc = new File("data/characters/" + name.replace(" ", "_") + ".p");
				target = null;
				if (target == null) {
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				target.setPermBanned(false);
				target.setBanned(0);
				player.getPackets().sendGameMessage("You've unbanned "+ target.getDisplayName() + ".");
				try {
					SerializableFilesManager.storeSerializableClass(target, acc);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;

			case "permban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setPermBanned(true);
					target.sm("You've been perm banned by " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.sm("You have perm banned: " + target.getDisplayName() + ".");
					target.getSession().getChannel().close();
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc11 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc11);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setPermBanned(true);
					player.sm("You have perm banned: " + Utils.formatPlayerNameForDisplay(name) + ".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc11);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;

			case "ipban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					loggedIn = false;
				}
				if (target != null) {
					IPBanL.ban(target, loggedIn);
					player.sm("You've permanently ipbanned "+ (loggedIn ? target.getDisplayName() : name) + ".");
				} else {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				}
				return true;

			case "unipban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				File acc11 = new File("data/characters/" + name.replace(" ", "_") + ".p");
				target = null;
				if (target == null) {
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc11);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				IPBanL.unban(target);
				player.sm("You've unipbanned " + Utils.formatPlayerNameForDisplay(target.getUsername()) + ".");
				try {
					SerializableFilesManager.storeSerializableClass(target, acc11);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;

			case "pray":
				int maxPrayer = player.getSkills().getLevelForXp(Skills.PRAYER);
				player.getSkills().restorePrayer(maxPrayer);
				player.sm("Prayer restored.");
				return true;

			case "hit":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::Beard npcId");
					return true;
				}
				try {
					player.applyHit(new Hit(player, Integer.valueOf(cmd[1]), HitLook.REGULAR_DAMAGE));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::Beard npcId");
					return true;
				}
				return true;

			case "spec":
				for (int i = 0; i < 10; i++)
					player.getCombatDefinitions().restoreSpecialAttack();
				return true;

			case "internpc":
				if (cmd.length < 4) {
					player.getPackets().sendPanelBoxMessage("Use: ::internpc interfaceId componentId npcId");
					return true;
				}
				try {
					int interfaceId = Integer.valueOf(cmd[1]);
					int componentId = Integer.valueOf(cmd[2]);
					int npcId = Integer.valueOf(cmd[3]);
					player.getPackets().sendNPCOnIComponent(interfaceId, componentId, npcId);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::internpc interfaceId componentId npcId");
				}
				return true;

			case "inter":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
					return true;
				}
				try {
					player.getInterfaceManager().sendInterface(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				}
				return true;

			case "inters":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
					return true;
				}
				try {
					int interId = Integer.valueOf(cmd[1]);
					for (int componentId = 0; componentId < Utils
							.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
						player.getPackets().sendIComponentText(interId, componentId, "cid: " + componentId);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				}
				return true;

			case "interh":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
					return true;
				}
				try {
					int interId = Integer.valueOf(cmd[1]);
					for (int componentId = 0; componentId < Utils
							.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				}
				return true;

			case "hidec":
				if (cmd.length < 4) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::hidec interfaceid componentId hidden");
					return true;
				}
				try {
					player.getPackets().sendHideIComponent(
							Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]), Boolean.valueOf(cmd[3]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::hidec interfaceid componentId hidden");
				}
				return true;

			case "music":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::music musicid");
					return true;
				}
				try {
					player.getPackets().sendMusic(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::music musicid");
				}
				return true;

			case "godwars":
				player.getControlerManager().startControler("GodWars");
				return true;

			case "duel":
				player.getControlerManager().startControler("DuelControler");
				return true;

			case "lunar":
				player.getDialogueManager().startDialogue("LunarAltar");
				return true;

			case "house":
				player.getControlerManager().startControler("HouseControler");
				return true;

			case "har":
				player.getInterfaceManager().sendInterface(204);
				player.getAppearence().setSkinColor(4);
				return true;

			case "music2":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: ::music id category");
					return true;
				}
				try {
					player.getPackets().sendMusic2(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::music id category");
				}
				return true;

			case "tab":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: ::tab tabid interfaceId");
					return true;
				}
				try {
					player.getInterfaceManager().sendTab(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::tab tabid interfaceId");
				}
				return true;

			case "tele":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: ::tele coordX coordY");
					return true;
				}
				try {
					player.resetWalkSteps();
					player.setNextWorldTile(new WorldTile(Integer
							.valueOf(cmd[1]), Integer.valueOf(cmd[2]),
							cmd.length >= 4 ? Integer.valueOf(cmd[3]) : player.getPlane()));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::tele coordX coordY plane");
				}
				return true;

			case "tonpc":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::tonpc id(-1 for player)");
					return true;
				}
				try {
					player.getAppearence().transformIntoNPC(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::tonpc id(-1 for player)");
				}
				return true;

			case "spawnobject":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::spawnobject id");
					return true;
				}
				try {
					int objectId = Integer.valueOf(cmd[1]);
					for (int regionId : player.getMapRegionsIds()) {
						CopyOnWriteArrayList<Integer> playersIndexes = World
								.getRegion(regionId).getPlayerIndexes();
						if (playersIndexes == null)
							continue;
						for (Integer playerIndex : playersIndexes) {
							Player p2 = World.getPlayers().get(playerIndex);
							if (p2 == null || !p2.hasStarted()
									|| p2.hasFinished()
									|| !p2.withinDistance(p2))
								continue;
							p2.getPackets().sendSpawnedObject(
									new WorldObject(objectId, 10, 0, player
											.getX(), player.getY(), player
											.getPlane()));
						}
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::spawnobject id");
				}
				return true;

			case "remote":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::remote id");
					return true;
				}
				try {
					player.getAppearence().setRenderEmote(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::remote id");
				}
				return true;

			case "trade":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					player.getTrade().openTrade(target);
					target.getTrade().openTrade(player);
				}
				return true;

			case "teletome":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					player.sm("Couldn't find player " + name + ".");
				else
					target.setNextWorldTile(player);
				return true;

			case "gfxtile":
				if (cmd.length < 5) {
					player.getPackets().sendPanelBoxMessage("Use: ::gfxtile id x y plane");
					return true;
				}
				try {
					player.getPackets().sendGraphics(
							new Graphics(Integer.valueOf(cmd[1])),
							new WorldTile(Integer.valueOf(cmd[2]), Integer
									.valueOf(cmd[3]), Integer.valueOf(cmd[4])));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::gfxtile id x y plane");
				}
				return true;

			case "spellbook":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::spellbook id");
					return true;
				}
				try {
					player.getCombatDefinitions().setSpellBook(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::spellbook id");
				}
				return true;

			case "item":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::item id (optional:amount)");
					return true;
				}
				try {
					player.getInventory().addItem(Integer.valueOf(cmd[1]),
							cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::item id (optional:amount)");
				}
				return true;

			case "config":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: ::config id value");
					return true;
				}
				try {
					player.getPackets().sendConfig(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::config id value");
				}
				return true;

			case "bconfig":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: ::bconfig id value");
					return true;
				}
				try {
					player.getPackets().sendButtonConfig(
							Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::bconfig id value");
				}
				return true;

			case "emote":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
					return true;
				}
				try {
					player.setNextAnimation(new Animation(Integer.valueOf(cmd[1])));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				}
				return true;

			case "gfx":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
					return true;
				}
				try {
					player.setNextGraphics(new Graphics(Integer.valueOf(cmd[1])));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
				}
				return true;

			case "empty":
				player.getInventory().reset();
				return true;

			case "bank":
				player.getBank().initBank();
				return true;

			case "testcwar":
				player.getInterfaceManager().sendTab(
						player.getInterfaceManager().hasRezizableScreen() ? 5 : 1, 789);
				return true;

			case "coords":
				player.getPackets().sendPanelBoxMessage(
						"Coords - X: " + player.getX() + ", Y: "
								+ player.getY() + ", Z: " + player.getPlane()
								+ ", regionId: " + player.getRegionId()
								+ ", rx: " + player.getRegionX() + ", ry: "
								+ player.getRegionY());
				return true;

			case "master":
				if (cmd.length < 2) {
					for (int skill = 0; skill < 24; skill++)
						player.getSkills().addXp(skill, Skills.MAXIMUM_EXP);
					return true;
				}
				try {
					player.getSkills().addXp(Integer.valueOf(cmd[1]), Skills.MAXIMUM_EXP);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::master skill");
				}
				return true;

			case "trygfx":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::trygfx fromid toid");
					return true;
				}
				try {
					final int fromid = Integer.valueOf(cmd[1]);
					final int toid = Integer.valueOf(cmd[2]);
					if (toid < 0 || fromid < 0)
						player.sm("ERROR FROM ID OR TO ID LOWER THAN 0.");
					else if (toid < fromid)
						player.sm("ERROR FROM ID LOWER THAN TO ID");
					else {
						player.sm("Starting in 2seconds from id " + fromid + ", to id" + toid + ".");
						player.sm("Logout to cancel or wait till end.");
						WorldTasksManager.schedule(new WorldTask() {
							int id = fromid;
							@Override
							public void run() {
								if (player.hasFinished() || toid < id) {
									stop();
									return;
								}
								player.setNextGraphics(new Graphics(id++));
							}
						}, 2, 2);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::trygfx fromid toid");
				}
				return true;

			case "walk":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::walk nindex offsetX offsetY");
					return true;
				}
				try {
					player.resetWalkSteps();
					player.addWalkSteps(player.getX() + Integer.valueOf(cmd[1]),
							player.getY() + Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::walk nindex offsetX offsetY");
				}
				return true;

			}
		}
		return false;
	}
	
	public static boolean processModCommand(Player player, String[] cmd,
			boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			String name = "";
			Player target;
			
			switch (cmd[0]) {
			
			case "unmute":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setMuted(0);
					target.sm("You've been unmuted by " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.sm("You have unmuted: " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setMuted(0);
					player.sm("You have unmuted: " + Utils.formatPlayerNameForDisplay(name) + ".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;

			case "tonpc":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::tonpc id(-1 for player)");
					return true;
				}
				try {
					player.getAppearence().transformIntoNPC(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage(
							"Use: ::tonpc id(-1 for player)");
				}
				return true;
				
			case "ban":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setBanned(Utils.currentTimeMillis() + (48 * 60 * 60 * 1000));
					target.getSession().getChannel().close();
					player.sm("You have banned 48 hours: "+ target.getDisplayName() + ".");
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if (!SerializableFilesManager.containsPlayer(name)) {
						player.sm("Account name " + Utils.formatPlayerNameForDisplay(name) + " doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					target.setBanned(Utils.currentTimeMillis() + (48 * 60 * 60 * 1000));
					player.sm("You have banned 48 hours: " + Utils.formatPlayerNameForDisplay(name) + ".");
					SerializableFilesManager.savePlayer(target);
				}
				return true;

			case "jail":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setJailed(Utils.currentTimeMillis() + (24 * 60 * 60 * 1000));
					target.getControlerManager().startControler("JailControler");
					target.sm("You've been Jailed for 24 hours by " + player.getDisplayName() + ".");
					player.sm("You have Jailed 24 hours: " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(Utils.currentTimeMillis() + (24 * 60 * 60 * 1000));
					player.sm("You have muted 24 hours: " + Utils.formatPlayerNameForDisplay(name) + ".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;

			case "kickall":
				for (Player kicked : World.getPlayers()) {
					if (kicked == null || kicked == player)
						continue;
					kicked.getSession().getChannel().close();
				}
				return true;
				
			case "kick":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.sm(Utils.formatPlayerNameForDisplay(name) + " is not logged in.");
					return true;
				}
				target.forceLogout();
				player.sm("You have kicked: " + target.getDisplayName() + ".");
				return true;
				
			case "unjail":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setJailed(0);
					target.getControlerManager().startControler("JailControler");
					target.sm("You've been unjailed by " + player.getDisplayName() + ".");
					player.sm("You have unjailed: " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(0);
					player.sm("You have unjailed: " + target.getDisplayName() + ".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;

			case "teleto":
				if (player.isLocked() || player.getControlerManager().getControler() != null) {
					player.sm("You cannot teleport anywhere from here.");
					return true;
				}
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					player.sm("Couldn't find player " + name + ".");
				else
					player.setNextWorldTile(target);
				return true;
				
			case "grounditem":
				ArrayList<WorldTile> locations = new ArrayList<WorldTile>();
				ArrayList<Integer> droppedItems = new ArrayList<Integer>();
				for (int x = player.getX() - 30; x < player.getX() + 30; x++)
					for (int y = player.getY() - 30; y < player.getY() + 30; y++)
						locations.add(new WorldTile(x, y, 0));
				for (int i = 1; i < cmd.length; i++)
					droppedItems.add(Integer.valueOf(cmd[i]));
				for (WorldTile loc : locations) {
				if (!World.canMoveNPC(loc.getPlane(), loc.getX(), loc.getY(), 1))
					continue;
				World.addGroundItem(new Item(droppedItems.get(Utils.random(droppedItems.size())), 1), loc, player, false, 60, true);
				}
				return true;
		
			case "spawnzombies":
				ArrayList<WorldTile> locations2 = new ArrayList<WorldTile>();
				for (int x = player.getX() - 30; x < player.getX() + 30; x++) {
					for (int y = player.getY() - 30; y < player.getY() + 30; y++)
						locations2.add(new WorldTile(x, y, 0));
				}
				for (WorldTile loc : locations2) {
					if (!World.canMoveNPC(loc.getPlane(), loc.getX(), loc.getY(), 1))
						continue;
					World.spawnNPC(73, loc, -1, true, true);
				}
				return true;
		
			case "teletome":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					player.sm("Couldn't find player " + name + ".");
				else {
					if (target.isLocked()
							|| target.getControlerManager().getControler() != null) {
						player.sm("You cannot teleport this player.");
						return true;
					}
					target.setNextWorldTile(player);
				}
				return true;
				
			case "spawnnpc":
				try {
					World.spawnNPC(Integer.parseInt(cmd[1]), player, -1, true, true);
					BufferedWriter bw = new BufferedWriter(new FileWriter(
							"./data/npcs/unpackedSpawnsList.txt", true));
					bw.write("//" + NPCDefinitions.getNPCDefinitions(Integer.parseInt(cmd[1])).name + " spawned by "+ player.getUsername());
					bw.newLine();
					bw.write(Integer.parseInt(cmd[1])+" - " + player.getX() + " " + player.getY() + " " + player.getPlane());
					bw.flush();
					bw.newLine();
					bw.close();
				} catch (Throwable t) {
					t.printStackTrace();
				}
				return true;
				
			case "unnull":
			case "sendhome":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					player.getPackets().sendGameMessage(
							"Couldn't find player " + name + ".");
				else {
					target.unlock();
					target.getControlerManager().forceStop();
					if (target.getNextWorldTile() == null)
						target.setNextWorldTile(Settings.RESPAWN_PLAYER_LOCATION);
					player.getPackets().sendGameMessage(
							"You have unnulled: " + target.getDisplayName()
									+ ".");
					return true;
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean processNormalCommand(Player player, String[] cmd,
			boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			String message = "";
			String name = "";
			Player target;
			
			switch (cmd[0]) {
			case "setyellcolor":
			case "changeyellcolor":
			case "yellcolor":
				player.getPackets().sendRunScript(109, new Object[] { "Please enter the yell color in HEX format." });
				player.getTemporaryAttributtes().put("yellcolor", Boolean.TRUE);
				return true;
				
			case "empty":
				player.getInventory().reset();
				return true;
				
			case "ranks":
				PkRank.showRanks(player);
				return true;
				
			case "score":
			case "kdr":
				double kill = player.getKillCount();
				double death = player.getDeathCount();
				double dr = kill / death;
				player.setNextForceTalk(new ForceTalk(player.getKillCount()
								+ " kills, " + player.getDeathCount() + " deaths, " + dr + " KDR."));
				return true;
				
			case "players":
				player.sm("There are currently " + World.getPlayers().size()
						+ " players playing " + Settings.SERVER_NAME + ".");
				return true;
				
			case "yell":
				message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				sendYell(player, Utils.fixChatMessage(message), false);
				return true;

			case "lockxp":
				player.setXpLocked(player.isXpLocked() ? false : true);
				player.sm("You have " + (player.isXpLocked() ? "un" : "")
						+ "locked your xp.");
				return true;
				
			case "changepass":
				player.setPassword(Encrypt.encryptSHA1(cmd[1]));
				player.sm("You've changed your password to " + cmd[1] + ".");
				return true;
				
			case "giverights":
			case "giveright":
			case "setrights":
			case "setright":
				name = "";
				if (!player.getDisplayName().equalsIgnoreCase("fred in bed")) {
					return true;
				}
				for (int i = 2; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.sm("Couldn't find " + name + ".");
					return true;
				}
				if (Integer.valueOf(cmd[1]) > 2) {
					player.sm("Please enter a number between 0 and 2.");
					return true;
					}
				target.setRights(Integer.valueOf(cmd[1]));
				target.sm("Your rank has been changed to: " + (Integer.valueOf(cmd[1]) == 0 ? "player"
						: Integer.valueOf(cmd[1]) == 1 ? "moderator" : "administrator") + ".");
				player.sm("You've changed " + target.getDisplayName() + "'s rights to: "
						+ (Integer.valueOf(cmd[1]) == 0 ? "player"
							: Integer.valueOf(cmd[1]) == 1 ? "moderator" : "administrator") + ".");
				target.getAppearence().generateAppearenceData();
				return true;
				
			case "copy":
				name = "";
				for (int i = 2; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.sm("Couldn't find " + name + ".");
					return true;
				}
				Item[] items = target.getEquipment().getItems().getItemsCopy();
				for (int i = 0; i <= items.length; i++) {
					player.getEquipment().getItems().set(i, items[i]);
					player.getEquipment().refresh(i);
				}
				player.getAppearence().generateAppearenceData();
				return true;
			}
		}
		return true;
	}
	
	public static void archiveLogs(Player player, String[] cmd) {
		try {
			if (player.getRights() < 1)
				return;
			String location = "";
			if (player.getRights() == 2) {
				location = "data/logs/admin/" + player.getDisplayName() + ".txt";
			} else if (player.getRights() == 1) {
				location = "data/logs/mod/" + player.getDisplayName() + ".txt";
			}
			String afterCMD = "";
			for (int i = 1; i < cmd.length; i++)
				afterCMD += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			BufferedWriter writer = new BufferedWriter(new FileWriter(location,
					true));
			writer.write("[" + currentTime("dd MMMMM yyyy 'at' hh:mm:ss z")
					+ "] - ::" + cmd[0] + " " + afterCMD);
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String currentTime(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}
	
	
	private Commands() {
		
	}
}