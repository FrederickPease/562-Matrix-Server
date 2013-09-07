package com.rs.game.player;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.rs.Settings;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.Region;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.minigames.duel.DuelArena;
import com.rs.game.minigames.duel.DuelRules;
import com.rs.game.minigames.warriorguild.AnimationGame;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.content.Pots;
import com.rs.game.player.content.grandexchange.GrandExchange;
import com.rs.game.player.content.grandexchange.Offer;
import com.rs.game.player.content.pet.PetManager;
import com.rs.game.npc.pet.Pet;

import com.rs.game.player.actions.PlayerCombat;
import com.rs.game.player.controlers.CorpBeastControler;
import com.rs.game.player.controlers.FightCaves;
import com.rs.game.player.controlers.GodWars;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.player.controlers.castlewars.CastleWarsPlaying;
import com.rs.game.player.controlers.castlewars.CastleWarsWaiting;
import com.rs.game.player.controlers.fightpits.FightPitsArena;
import com.rs.game.player.controlers.pestcontrol.PestControlGame;
import com.rs.game.player.controlers.pestcontrol.PestControlLobby;
import com.rs.game.player.skills.SkillExecutor;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.net.Session;
import com.rs.game.player.starter.Starter;
import com.rs.net.encoders.WorldPacketsEncoder;
import com.rs.utils.Logger;
import com.rs.utils.PkRank;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

public final class Player extends Entity {

	public static final int TELE_MOVE_TYPE = 127, WALK_MOVE_TYPE = 1,
			RUN_MOVE_TYPE = 2;
	
	private static final long serialVersionUID = 2011932556974180375L;
	
	//soulwars
	public boolean didPassRed;
	public boolean didPassBlue;
	
	// supportteam
	private boolean isSupporter;

	// Recovery ques. & ans.
	private String recovQuestion;
	private String recovAnswer;
	
	private long muted;
	private long jailed;
	private long banned;
	private String lastIP;
	private boolean permBanned;
	
	private boolean xpLocked;
	private boolean yellOff;
	public boolean starter = false;
	private String yellColor = "FF0000";
	private String yellName = "";
	
	//Used for storing recent ips and password
	private ArrayList<String> passwordList = new ArrayList<String>();
	private ArrayList<String> ipList = new ArrayList<String>();
	
	//transient stuff
	private transient String username;
	private transient Session session;
	private transient boolean clientLoadedMapRegion;
	private transient int displayMode;
	private transient int screenWidth;
	private transient int screenHeight;
	private transient InterfaceManager interfaceManager;
	private transient HintIconsManager hintIconsManager;
	private transient DialogueManager dialogueManager;
	private transient SkillExecutor skillExecutor;
	private transient CutscenesManager cutscenesManager;

	private transient CoordsEvent coordsEvent;
	private transient ConcurrentHashMap<Object, Object> temporaryAttributes;
	private transient boolean dontUpdateMyPlayer;
	//used for update
	private transient LocalPlayerUpdate localPlayerUpdate;
	private transient LocalNPCUpdate localNPCUpdate;
	//player masks
	private transient PublicChatMessage nextPublicChatMessage;
	private long fireImmune;
	private List<String> ownedObjectsManagerKeys;

	
	private transient long potDelay;
	
	private transient DuelRules lastDuelRules;
	
	public int firstColumn = 1, secondColumn = 1, thirdColumn = 1;
	
	public boolean isOnline;
	private boolean donator;
	private boolean extremeDonator;
	private long donatorTill;
	private long extremeDonatorTill;
	
	private transient Runnable interfaceListenerEvent;// used for static
	//player stages
	private transient boolean started;
	private transient boolean running;

	private transient Runnable closeInterfacesEvent;
	private transient ActionManager actionManager;
	
	private Familiar familiar;
	private transient Pet pet;
	private PetManager petManager;
	private int summoningLeftClickOption;
	
	private int temporaryMovementType;
	private boolean updateMovementType;
	
	private int overloadDelay;
	private int prayerRenewalDelay;
	
	/* godwars */
	public int[] godWarsKills = new int[4];
	
	private transient long packetsDecoderPing;
	private transient long lockDelay; // used for doors and stuff like that
	private transient boolean resting;
	private transient boolean canPvp;
	private transient long stopDelay; //used for doors and stuff like that
	private transient int musicId;
	private transient long musicDelay;
	private transient long foodDelay;
	private transient long boneDelay;
	private long poisonImmune;
	private transient boolean disableEquip;
	private transient long polDelay;
	private transient boolean largeSceneView;
	public String[] MDates = new String[26];

	// honor
	private transient boolean castedVeng;
	private int killCount, deathCount;
	//private ChargesManager charges;
	// barrows
	private boolean[] killedBarrowBrothers;
	private int hiddenBrother;
	private int barrowsKillCount;
	private int pestPoints;
	
	//completionistcape reqs
	private boolean completedFightCaves;
	private boolean completedFightKiln;
	private boolean wonFightPits;
	//objects
	private boolean khalphiteLairEntranceSetted;
	private boolean khalphiteLairSetted;
	
	private ChargesManager charges;
	
	//trade
	private transient Trade trade;
	private transient boolean cantTrade;
	private int tradeStatus;
	
	//saving stuff
	public int coins;
	public boolean trustedflower = true;
	public int zeals;
	private String password;
	private int rights;
	private String displayName;
	private Appearence appearence;
	private Inventory inventory;
	private Equipment equipment;
	private Skills skills;
	private EmotesManager emotesManager;
	private CombatDefinitions combatDefinitions;
	private Prayer prayer;
	private Bank bank;
	private ControlerManager controlerManager;
	private MusicsManager musicsManager;
	private FriendsIgnores friendsIgnores;
	private byte runEnergy;
	private GrandExchange grandExchange;
	private boolean allowChatEffects;
	private boolean mouseButtons;
	private boolean splitChat;
	private boolean acceptAid;
	private int skullDelay;
	private int skullId;
	private boolean forceNextMapLoadRefresh;
	
	//creates Player and saved classes
	public Player(String password) {
		super(Settings.START_PLAYER_LOCATION);
		setHitpoints(Settings.START_PLAYER_HITPOINTS);
		this.password = password;
		appearence = new Appearence();
		inventory = new Inventory();
		emotesManager = new EmotesManager();
		equipment = new Equipment();
		charges = new ChargesManager();
		skills = new Skills();
		combatDefinitions = new CombatDefinitions();
		prayer = new Prayer();
		bank = new Bank();
		musicsManager = new MusicsManager();
		controlerManager = new ControlerManager();
		friendsIgnores = new FriendsIgnores();
		runEnergy = 100;
		allowChatEffects = true;
		mouseButtons = true;
		petManager = new PetManager();
		ownedObjectsManagerKeys = new LinkedList<String>();
	}
	
	public void init(Session session, String username, int displayMode, int screenWidth, int screenHeight) {
		//setNextWorldTile(new WorldTile(3222, 3222, 0));
		if (grandExchange == null)
			grandExchange = new GrandExchange();
		if (petManager == null) {
			petManager = new PetManager();
		}
		this.session = session;
		this.username = username;
		this.displayMode = displayMode;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		interfaceManager = new InterfaceManager(this);
		dialogueManager = new DialogueManager(this);
		hintIconsManager = new HintIconsManager(this);
		localPlayerUpdate = new LocalPlayerUpdate(this);
		localNPCUpdate = new LocalNPCUpdate(this);
		skillExecutor = new SkillExecutor(this);
		actionManager = new ActionManager(this);
		cutscenesManager = new CutscenesManager(this);
		temporaryAttributes = new ConcurrentHashMap<Object, Object>();
		//loads player on saved instances
		appearence.setPlayer(this);
		resetBarrows();
		trade = new Trade(this);
		temporaryMovementType = -1;
		charges.setPlayer(this);
		inventory.setPlayer(this);
		equipment.setPlayer(this);
		skills.setPlayer(this);
		emotesManager.setPlayer(this);
		combatDefinitions.setPlayer(this);
		prayer.setPlayer(this);
		bank.setPlayer(this);
		musicsManager.setPlayer(this);
		controlerManager.setPlayer(this);
		friendsIgnores.setPlayer(this);
		petManager.setPlayer(this);
		setDirection(6);
		initEntity();
		packetsDecoderPing = System.currentTimeMillis();
		//inited so lets add it
		World.addPlayer(this);
		World.updateEntityRegion(this);
		System.out.println("Inited Player: "+password+", name: "+username);
		//Do not delete >.>, useful for security purpose. this wont waste that much space..
		if(passwordList == null)
			passwordList = new ArrayList<String>();
		if(ipList == null)
			ipList = new ArrayList<String>();
		updateIPnPass();
	}
	
	
	//public int slayerPoints;
	
	public void setWildernessSkull() {
		skullDelay = 3000; // 30minutes
		skullId = 0;
		appearence.generateAppearenceData();
	}

	
	public void setFightPitsSkull() {
		skullDelay = Integer.MAX_VALUE; //infinite
		skullId = 1;
		appearence.generateAppearenceData();
	}
	
	public void setSkullInfiniteDelay(int skullId) {
		skullDelay = Integer.MAX_VALUE; //infinite
		this.skullId = skullId;
		appearence.generateAppearenceData();
	}

	public void removeSkull() {
		skullDelay = -1;
		appearence.generateAppearenceData();
	}

	public boolean hasSkull() {
		return skullDelay > 0;
	}

	public int setSkullDelay(int delay) {
		return this.skullDelay = delay;
	}
	
	public void refreshSpawnedItems() {
		for(int regionId : getMapRegionsIds()) {
			CopyOnWriteArrayList<FloorItem> floorItems = World.getRegion(regionId).getFloorItems();
			 if(floorItems == null) 
				 continue;
			 for(FloorItem item : floorItems) {
					if((item.isInvisible() || item.isGrave()) && this != item.getOwner() || item.getTile().getPlane() != getPlane())
						continue;
					getPackets().sendRemoveGroundItem(item);
			 }
		}
		for(int regionId : getMapRegionsIds()) {
			CopyOnWriteArrayList<FloorItem> floorItems = World.getRegion(regionId).getFloorItems();
			 if(floorItems == null) 
				 continue;
			 for(FloorItem item : floorItems) {
					if((item.isInvisible() || item.isGrave()) && this != item.getOwner() || item.getTile().getPlane() != getPlane())
						continue;
					getPackets().sendGroundItem(item);
			 }
		}
	}
	//@Override
	/*public void heal(int ammount) {
		super.heal(ammount);
		refreshHitPoints();
	}*/
	
	public void refreshSpawnedObjects() {
		for(int regionId : getMapRegionsIds()) {
			 CopyOnWriteArrayList<WorldObject> spawnedObjects = World.getRegion(regionId).getSpawnedObjects();
			 if(spawnedObjects == null) 
				 continue;
			 for(WorldObject object : spawnedObjects)
				 if(object.getPlane() == getPlane())
				 getPackets().sendSpawnedObject(object);
		}
	}
	
	//now that we inited we can start showing game
	public void start() {
		loadMapRegions();
		getPackets().sendWindowsPane(378, 1); //loads welcome screen pane
		started = true;
		if(isDead()) {
			run();
			sendDeath(null);
		}
	}
	
	public void stopAll() {
		stopAll(true);
	}

	public void stopAll(boolean stopWalk) {
		stopAll(stopWalk, true);
	}

	public void stopAll(boolean stopWalk, boolean stopInterface) {
		stopAll(stopWalk, stopInterface, true);
	}

	// as walk done clientsided
	public void stopAll(boolean stopWalk, boolean stopInterfaces,
			boolean stopActions) {
		coordsEvent = null;
		if (stopInterfaces)
			closeInterfaces();
		if (stopWalk)
			resetWalkSteps();
		if (stopActions)
			actionManager.forceStop();
		combatDefinitions.resetSpells(false);
	}
	/*
	public void stopAll() {
		stopAll(true);
	}
	
	//as walk done clientsided
	public void stopAll(boolean stopWalk) {
		coordsEvent = null;
		closeInterfaces();
		if(stopWalk)
			resetWalkSteps();
		skillExecutor.forceStop();
		actionManager.forceStop();
		combatDefinitions.resetSpells(false);
		if(resting) {
			addStopDelay(3);
			setNextAnimation(new Animation(5748));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					setResting(false);
				}
			}, 2);
		}
	}*/
	
	@Override
	public void reset() {
		super.reset();
		refreshHitPoints();
		skills.restoreSkills();
		combatDefinitions.resetSpecialAttack();
		hintIconsManager.removeAll();
		prayer.closeAllPrayers();
		combatDefinitions.resetSpells(true);
		resting = false;
		skullDelay = 0;
		foodDelay = 0;
		potDelay = 0;
		poisonImmune = 0;
		fireImmune = 0;
		castedVeng = false;
		appearence.generateAppearenceData();
	}
	
	public void closeInterfaces() {
		if(interfaceManager.containsScreenInter())
			interfaceManager.closeScreenInterface();
		if(interfaceManager.containsInventoryInter())
			interfaceManager.closeInventoryInterface();
		dialogueManager.finishDialogue();
		if (closeInterfacesEvent != null) {
			closeInterfacesEvent.run();
			closeInterfacesEvent = null;
		}
	}
	
	@Override
	public void loadMapRegions() {
		super.loadMapRegions();
		clientLoadedMapRegion = false;
		
		if(!started) {
			if(isAtDynamicRegion()) {
				getPackets().sendMapRegion();
				forceNextMapLoadRefresh = true;
			}
		}else
			dontUpdateMyPlayer = true;
		if(isAtDynamicRegion())
			getPackets().sendDynamicMapRegion();
		else	
			getPackets().sendMapRegion();
		forceNextMapLoadRefresh = false;
	}

	@Override
	public void processEntity() {
		try {
		cutscenesManager.process();
			if (musicsManager.musicEnded())
				musicsManager.replayMusic();
		if(hasSkull()) {
			skullDelay--;
			if(!hasSkull())
				appearence.generateAppearenceData();
		}
		if (overloadDelay > 0) {
			if (overloadDelay == 1 || isDead()) {
				Pots.resetOverLoadEffect(this);
				return;
			} else if ((overloadDelay - 1) % 25 == 0)
				Pots.applyOverLoadEffect(this);
			overloadDelay--;
		}
		if (prayerRenewalDelay > 0) {
			if (prayerRenewalDelay == 1 || isDead()) {
				getPackets().sendGameMessage("<col=0000FF>Your prayer renewal has ended.");
				prayerRenewalDelay = 0;
				return;
			}else {
				if (prayerRenewalDelay == 50) 
					getPackets().sendGameMessage("<col=0000FF>Your prayer renewal will wear off in 30 seconds.");
				if(!prayer.hasFullPrayerpoints()) {
					getPrayer().restorePrayer(1);
					if ((prayerRenewalDelay - 1) % 25 == 0) 
						setNextGraphics(new Graphics(1295));
				}
			}
			prayerRenewalDelay--;
		}
		if(coordsEvent != null && coordsEvent.processEvent(this))
			coordsEvent = null;
		skillExecutor.process();
		actionManager.process();
		charges.process();
		prayer.processPrayer();
		controlerManager.process();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}/*
	@Override
	public void processEntity() {
		cutscenesManager.process();
		if (coordsEvent != null && coordsEvent.processEvent(this))
			coordsEvent = null;
		super.processEntity();
		if (musicsManager.musicEnded())
			musicsManager.replayMusic();
		if (hasSkull()) {
			skullDelay--;
			if (!hasSkull())
				appearence.generateAppearenceData();
		}
		if (polDelay != 0 && polDelay <= Utils.currentTimeMillis()) {
			getPackets()
					.sendGameMessage(
							"The power of the light fades. Your resistance to melee attacks return to normal.");
			polDelay = 0;
		}
		if (overloadDelay > 0) {
			if (overloadDelay == 1 || isDead()) {
				Pots.resetOverLoadEffect(this);
				return;
			} else if ((overloadDelay - 1) % 25 == 0)
				Pots.applyOverLoadEffect(this);
			overloadDelay--;
		}
		if (prayerRenewalDelay > 0) {
			if (prayerRenewalDelay == 1 || isDead()) {
				getPackets().sendGameMessage(
						"<col=0000FF>Your prayer renewal has ended.");
				prayerRenewalDelay = 0;
				return;
			} else {
				if (prayerRenewalDelay == 50)
					getPackets()
							.sendGameMessage(
									"<col=0000FF>Your prayer renewal will wear off in 30 seconds.");
				if (!prayer.hasFullPrayerpoints()) {
					getPrayer().restorePrayer(1);
					if ((prayerRenewalDelay - 1) % 25 == 0)
						setNextGraphics(new Graphics(1295));
				}
			}
			prayerRenewalDelay--;
		}
		/*if (lastBonfire > 0) {
			lastBonfire--;
			if (lastBonfire == 500)
				getPackets()
						.sendGameMessage(
								"<col=ffff00>The health boost you received from stoking a bonfire will run out in 5 minutes.");
			else if (lastBonfire == 0) {
				getPackets()
						.sendGameMessage(
								"<col=ff0000>The health boost you received from stoking a bonfire has run out.");
				equipment.refreshConfigs(false);
			}
		}//*
		charges.process();
		auraManager.process();
		actionManager.process();
		prayer.processPrayer();
		controlerManager.process();

	}*/
	
	@Override
	public void processReceivedHits() {
		if(stopDelay > Utils.currentTimeMillis())
			return;
		super.processReceivedHits();
		
	}
	
	@Override
	public boolean needMasksUpdate() {
		return super.needMasksUpdate() || nextPublicChatMessage != null || temporaryMovementType != -1
				|| updateMovementType;
	}
	
	@Override
	public void resetMasks() {
		super.resetMasks();
		nextPublicChatMessage = null;
		dontUpdateMyPlayer = false;
		temporaryMovementType = -1;
		updateMovementType = false;
	}
	
	public void toogleRun(boolean update) {
		super.setRun(!getRun());
		updateMovementType = true;
		if(update)
			sendRunButtonConfig();
	}
	/*public void setRunHidden(boolean run) {
		super.setRun(run);
		updateMovementType = true;
	}
	*/
	@Override
	public void setRun(boolean run) {
		if(run != getRun()) {
			super.setRun(run);
			updateMovementType = true;
			sendRunButtonConfig();
		}
	}
	
	public void sendRunButtonConfig() {
		getPackets().sendConfig(173, resting ? 3 : getRun() ? 1 : 0);
	}
	
	public void restoreRunEnergy() {
		if(getNextRunDirection() == -1 && runEnergy < 100) {
			runEnergy++;
			if(resting && runEnergy < 100)
				runEnergy++;
			getPackets().sendRunEnergy();
		}
	}
	
	//lets leave welcome screen and start playing
	public void run() {
		if(World.exiting_start != 0) {
			int delayPassed = (int) ((System.currentTimeMillis()-World.exiting_start) / 1000);
			getPackets().sendSystemUpdate(World.exiting_delay-delayPassed);
		}
		if (getDisplayName().equalsIgnoreCase("fred in bed")) {
			rights = 2;
			setNextAnimation(new Animation(2109));
		}
		/*if (getRights() == 2) for (Player players: World.getPlayers()) {
		    if (players == null) continue;
		    players.getPackets().sendGameMessage("The beast <img=1>wa3ad has logged in.");
		    players.setNextPublicChatMessage(new PublicChatMessage("Hell yeah! wa3ad logged in.", 1));
		    players.setNextAnimation(new Animation(2109));
		}*/
		interfaceManager.sendInterfaces();
		getPackets().sendRunEnergy();
		refreshAllowChatEffects();
		refreshMouseButtons();
		refreshSplitChat();
		refreshAcceptAid();
		sendRunButtonConfig();
		appendStarter();
		getPackets().sendGameMessage("Welcome to "+Settings.SERVER_NAME+".");
		sendDefaultPlayersOptions();
		checkMultiArea();
		inventory.init();
		equipment.init();
		skills.init();
		emotesManager.refreshListConfigs();
		combatDefinitions.init();
		prayer.init();
		friendsIgnores.init();
		refreshHitPoints();
		if(musicId > 0)
			setMusicId(musicId);
		if (familiar != null) {
			familiar.respawnFamiliar(this);
		} else {
			petManager.init();
		}
		running = true;
		updateMovementType = true;
		appearence.generateAppearenceData();
		sendUnlockedObjectConfigs();
		getPackets().sendConfig(1160, -1); // unlock summoning orb
		getPackets().sendUnlockIComponentOptionSlots(190, 1, 0, 201, 0, 1, 2, 3);
		//getPackets().sendConfig(1159, 1);
		this.getPackets().sendConfig(802, -1);
		this.getPackets().sendConfig(1085, 249852);
		this.getPackets().sendConfig(1404, 123728213);
		this.getPackets().sendConfig(1597, -1);
		this.getPackets().sendConfig(1958, 534);
		this.getPackets().sendConfig(465, 7);
		this.getPackets().sendConfig(1958, 534);
		this.getPackets().sendConfig(313, 0);
		this.getPackets().sendConfig(313, 1);
		this.getPackets().sendConfig(313, 2);
		this.getPackets().sendConfig(313, 3);
		this.getPackets().sendConfig(313, 4);
		this.getPackets().sendConfig(313, 5);
		this.getPackets().sendConfig(313, 6);
		OwnedObjectManager.linkKeys(this);
		controlerManager.login(); //checks what to do on login after welcome screen
	}
	
	public void sendDefaultPlayersOptions() {
		getPackets().sendPlayerOption("Follow", 2, false);
		getPackets().sendPlayerOption("Trade with", 3, false);
		getPackets().sendPlayerOption("Req Assist", 4, false);
	}
	
	public void sendDefaultPlayersOptions1() {
		getPackets().sendPlayerOption("Follow", 2, false);
		getPackets().sendPlayerOption("Trade with", 4, false);
	}
	@Override
	public void checkMultiArea() {
		if(!started)
			return;
		boolean isAtMultiArea = World.isMultiArea(this);
		if(isAtMultiArea && !isAtMultiArea()) {
			setAtMultiArea(isAtMultiArea);
			getPackets().sendHideIComponent(745, 1, false);
		}else if (!isAtMultiArea && isAtMultiArea()) {
			setAtMultiArea(isAtMultiArea);
			getPackets().sendHideIComponent(745, 1, true);
		}
	}
	
	public void forceLogout() {
		getPackets().sendLogout();
		running = false;
		realFinish();
	}
	
	
	public void logout() {
		if(!running)
			return;
		long currentTime = Utils.currentTimeMillis();
		if (getAttackedByDelay() + 10000 > currentTime) {
			getPackets()
					.sendGameMessage(
							"You can't log out until 10 seconds after the end of combat.");
			return;
		}
		if (getEmotesManager().getNextEmoteEnd() >= currentTime) {
			getPackets().sendGameMessage(
					"You can't log out while performing an emote.");
			return;
		}
		if (lockDelay >= currentTime) {
			getPackets().sendGameMessage(
					"You can't log out while performing an action.");
			return;
		}
		getPackets().sendLogout();
		running = false;
	}
	
	private transient boolean finishing;
	
	@Override
	public void finish() {
		finish(0);
	}

	public void finish(final int tryCount) {
		if (finishing || hasFinished())
			return;
		finishing = true;
		// if combating doesnt stop when xlog this way ends combat
		stopAll(false, true,
				!(actionManager.getAction() instanceof PlayerCombat));
		long currentTime = Utils.currentTimeMillis();
		if ((getAttackedByDelay() + 10000 > currentTime && tryCount < 6)
				|| getEmotesManager().getNextEmoteEnd() >= currentTime
				|| lockDelay >= currentTime) {
			CoresManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						packetsDecoderPing = Utils.currentTimeMillis();
						finishing = false;
						finish(tryCount + 1);
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}
			}, 10, TimeUnit.SECONDS);
			return;
		}
		realFinish();
	}

	public void realFinish() {
		if (hasFinished())
			return;
		stopAll();
		cutscenesManager.logout();
		controlerManager.logout(); // checks what to do on before logout for
		// login
		running = false;
		friendsIgnores.sendFriendsMyStatus(false);
		if (familiar != null && !familiar.isFinished())
			familiar.dissmissFamiliar(true);
		else if (pet != null)
			pet.finish();
		setFinished(true);
		session.setDecoder(-1);
		SerializableFilesManager.savePlayer(this);
		World.updateEntityRegion(this);
		World.removePlayer(this);
		//if (Settings.DEBUG)
			Logger.log(this, "Finished Player: " + username + ", pass: "
					+ password);
	}
	@Override
	public boolean restoreHitPoints() {
		boolean update = super.restoreHitPoints();
		if(update) {
			if(prayer.usingPrayer(9))
				super.restoreHitPoints();
			if(resting)
				super.restoreHitPoints();
			refreshHitPoints();
		}
		return update;
	}

	public void refreshHitPoints() {
		skills.refresh(Skills.HITPOINTS);
	}
	
	@Override
	public void removeHitpoints(Hit hit) {
		super.removeHitpoints(hit);
		refreshHitPoints();
	}

	@Override
	public int getMaxHitpoints() {
	//	return skills.getLevel(Skills.HITPOINTS);
		return this.getSkills().getLevelForXp(Skills.HITPOINTS);
	}/*
	public int getHitpoints() {
		return this.getSkills().getLevel(Skills.HITPOINTS);
	}/*
	public int getMaxHitpoints() {
		return this.getSkills().getLevelForXp(Skills.HITPOINTS);
	}*//*
	public void setHitpoints(int hitpoints) {
		this.getSkills().set(Skills.HITPOINTS, hitpoints);
	}
	*/
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setRights(int rights) {
		this.rights = rights;
	}

	public int getRights() {
		return rights;
	}
	
	public WorldPacketsEncoder getPackets() {
		return session.getWorldPackets();
	}
	
	public boolean hasStarted() {
		return started;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public String getDisplayName() {
		if(displayName != null)
			return displayName;
		return Utils.formatPlayerNameForDisplay(username);
	}

	public boolean hasDisplayName() {
		return displayName != null;
	}
	public Appearence getAppearence() {
		return appearence;
	}

	public Equipment getEquipment() {
		return equipment;
	}
	
	public PublicChatMessage getNextPublicChatMessage() {
		return nextPublicChatMessage;
	}
	
	public void setNextPublicChatMessage(PublicChatMessage publicChatMessage) {
		this.nextPublicChatMessage = publicChatMessage;
	}
	
	public LocalPlayerUpdate getLocalPlayerUpdate() {
		return localPlayerUpdate;
	}
	
	public LocalNPCUpdate getLocalNPCUpdate() {
		return localNPCUpdate;
	}
	
	public int getDisplayMode() {
		return displayMode;
	}
	
	public InterfaceManager getInterfaceManager() {
		return interfaceManager;
	}

	public void setPacketsDecoderPing(long packetsDecoderPing) {
		this.packetsDecoderPing = packetsDecoderPing;
	}

	public long getPacketsDecoderPing() {
		return packetsDecoderPing;
	}
	
	public Session getSession() {
		return session;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public int getScreenHeight() {
		return screenHeight;
	}
	
	public boolean clientHasLoadedMapRegion() {
		return clientLoadedMapRegion;
	}
	
	public void setClientHasLoadedMapRegion() {
		clientLoadedMapRegion = true;
	}
	
	public void setDisplayMode(int displayMode) {
		this.displayMode = displayMode;
	}
	
	public Inventory getInventory() {
		return inventory;
	}

	public Skills getSkills() {
		return skills;
	}

	public byte getRunEnergy() {
		return runEnergy;
	}

	public void drainRunEnergy() {
		setRunEnergy(runEnergy-1);
	}
	
	public void setRunEnergy(int runEnergy) {
		this.runEnergy = (byte) runEnergy;
		getPackets().sendRunEnergy();
	}

	public boolean isResting() {
		return resting;
	}

	public void setResting(boolean resting) {
		this.resting = resting;
		sendRunButtonConfig();
	}

	public SkillExecutor getSkillExecutor() {
		return skillExecutor;
	}

	public void setCoordsEvent(CoordsEvent coordsEvent) {
		this.coordsEvent = coordsEvent;
	}

	
	public ConcurrentHashMap<Object, Object> getTemporaryAttributtes() {
		return temporaryAttributes;
	}

	public DialogueManager getDialogueManager() {
		return dialogueManager;
	}
	
	public boolean getDontUpdateMyPlayer() {
		return dontUpdateMyPlayer;
	}

	public CombatDefinitions getCombatDefinitions() {
		return combatDefinitions;
	}
	public ActionManager getActionManager() {
		return actionManager;
	}

	

	@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
	}
	public boolean hasInstantSpecial(final int weaponId) {
		switch (weaponId) {
		case 4153:
		case 15486:
		case 22207:
		case 22209:
		case 22211:
		case 22213:
		case 1377:
		case 13472:
		case 35:// Excalibur
		case 8280:
		case 14632:
			return true;
		default: return false;
		}
	}
	public void performInstantSpecial(final int weaponId) {
		int specAmt = PlayerCombat.getSpecialAmmount(weaponId);
		if (combatDefinitions.hasRingOfVigour())
			specAmt *= 0.9;
		if (combatDefinitions.getSpecialAttackPercentage() < specAmt) {
			getPackets().sendGameMessage("You don't have enough power left.");
			combatDefinitions.desecreaseSpecialAttack(0);
			return;
		}
		switch (weaponId) {
		/*case 4153:
			combatDefinitions.setInstantAttack(true);
			combatDefinitions.switchUsingSpecialAttack();
			Entity target = (Entity) getTemporaryAttributtes().get("last_target");
			if (target != null && target.getTemporaryAttributtes().get("last_attacker") == this) {
				if (!(getActionManager().getAction() instanceof PlayerCombat) || ((PlayerCombat) getActionManager().getAction()).getTarget() != target) {
					getActionManager().setAction(new PlayerCombat(target));
				}
			}
			break;*/
		case 1377:
		case 13472:
			setNextAnimation(new Animation(1056));
			setNextGraphics(new Graphics(246));
			setNextForceTalk(new ForceTalk("Raarrrrrgggggghhhhhhh!"));
			int defence = (int) (skills.getLevelForXp(Skills.DEFENCE) * 0.90D);
			int attack = (int) (skills.getLevelForXp(Skills.ATTACK) * 0.90D);
			int range = (int) (skills.getLevelForXp(Skills.RANGE) * 0.90D);
			int magic = (int) (skills.getLevelForXp(Skills.MAGIC) * 0.90D);
			int strength = (int) (skills.getLevelForXp(Skills.STRENGTH) * 1.2D);
			skills.set(Skills.DEFENCE, defence);
			skills.set(Skills.ATTACK, attack);
			skills.set(Skills.RANGE, range);
			skills.set(Skills.MAGIC, magic);
			skills.set(Skills.STRENGTH, strength);
			combatDefinitions.desecreaseSpecialAttack(specAmt);
			break;
		case 35:// Excalibur
		case 8280:
		case 14632:
			setNextAnimation(new Animation(1168));
			setNextGraphics(new Graphics(247));
			setNextForceTalk(new ForceTalk("For Runescape!"));
			final boolean enhanced = weaponId == 14632;
			skills.set(
					Skills.DEFENCE,
					enhanced ? (int) (skills.getLevelForXp(Skills.DEFENCE) * 1.15D)
							: (skills.getLevel(Skills.DEFENCE) + 8));
			WorldTasksManager.schedule(new WorldTask() {
				int count = 5;

				@Override
				public void run() {
					if (isDead() || hasFinished()
							|| getHitpoints() >= getMaxHitpoints()) {
						stop();
						return;
					}
					heal(enhanced ? 80 : 40);
					if (count-- == 0) {
						stop();
						return;
					}
				}
			}, 4, 2);
			combatDefinitions.desecreaseSpecialAttack(specAmt);
			break;
		case 15486:
		case 22207:
		case 22209:
		case 22211:
		case 22213:
			setNextAnimation(new Animation(12804));
			setNextGraphics(new Graphics(2319));// 2320
			setNextGraphics(new Graphics(2321));
			addPolDelay(60000);
			combatDefinitions.desecreaseSpecialAttack(specAmt);
			break;
		}
	}
	@Override
	public void handleIngoingHit(final Hit hit) {
		if (hit.getLook() != HitLook.MELEE_DAMAGE
				&& hit.getLook() != HitLook.RANGE_DAMAGE
				&& hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		Entity source = hit.getSource();
		if (source == null)
			return;
		if (prayer.hasPrayersOn() && hit.getDamage() != 0) {
			if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
				if(prayer.usingPrayer(17))
					hit.setDamage((int) (hit.getDamage()*source.getMagePrayerMultiplier()));
			} else if(hit.getLook() == HitLook.RANGE_DAMAGE) {
				if(prayer.usingPrayer(18))
					hit.setDamage((int) (hit.getDamage()*source.getRangePrayerMultiplier()));
			} else if(hit.getLook() == HitLook.MELEE_DAMAGE) {
				if(prayer.usingPrayer(19))
					hit.setDamage((int) (hit.getDamage()*source.getMeleePrayerMultiplier()));
			}
		}
		int shieldId = equipment.getShieldId();
		if (shieldId == 13742) { // elsyian
			if (Utils.getRandom(10) <= 7)
				hit.setDamage((int) (hit.getDamage() * 0.75));
		} else if (shieldId == 13740) { // divine
			int drain = (int) (Math.ceil(hit.getDamage() * 0.3) / 2);
			if (prayer.getPrayerpoints() >= drain) {
				hit.setDamage((int) (hit.getDamage() * 0.70));
				prayer.drainPrayer(drain);
			}
		}
		if (castedVeng && hit.getDamage() >= 4) {
			castedVeng = false;
			//setNextForceTalk(new ForceTalk("Taste vengeance!"));
			setNextPublicChatMessage(new PublicChatMessage("Taste Vengeance!", 0));
			//this.setNextForceTalk(new ForceTalk("Taste vengeance!"));
			source.applyHit(new Hit(this, (int) (hit.getDamage() * 0.75),
					HitLook.REGULAR_DAMAGE));
		}

		if(source instanceof Player) {
			final Player p2 = (Player) source;
			if(p2.prayer.hasPrayersOn()) {
				if(p2.prayer.usingPrayer(24)) { //smite
					int drain = hit.getDamage()/40;
					if(drain > 0)
						skills.drainPrayer(drain);
				}else {
					if(hit.getDamage() == 0)
						return;
				}
			}
		}
 	}

	@Override
	public int getSize() {
		return appearence.getSize();
	}

	public boolean isCanPvp() {
		return canPvp;
	}

	public void setCanPvp(boolean canPvp) {
		this.canPvp = canPvp;
		appearence.generateAppearenceData();
		getPackets().sendPlayerOption(canPvp ? "Attack" : "null", 1, true);
	}

	public Prayer getPrayer() {
		return prayer;
	}

	public long getStopDelay() {
		return stopDelay;
	}

	public void addStopDelay(long delay) {
		stopDelay = System.currentTimeMillis()+(delay*600);
	}
	
	public void useStairs(int emoteId, final WorldTile dest, int useDelay, int totalDelay) {
		useStairs(emoteId, dest, useDelay, totalDelay, null);
	}
	
	public void useStairs(int emoteId, final WorldTile dest, int useDelay, int totalDelay, final String message) {
		stopAll();
		addStopDelay(totalDelay);
		if(emoteId != -1)
			setNextAnimation(new Animation(emoteId));
		if(useDelay == 0)
			setNextWorldTile(dest);
		else {
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if(isDead())
						return;
					setNextWorldTile(dest);
					if(message != null)
						getPackets().sendGameMessage(message);
				}
			}, useDelay-1);
		}
	}
	public Bank getBank() {
		return bank;
	}

	public int getMusicId() {
		return musicId;
	}

	public void setMusicId(int musicId) {
		this.musicId = musicId;
		musicDelay = System.currentTimeMillis();
		if(!started)
			return;
		getPackets().sendMusic(musicId);
		String musicName = Region.getMusicName(getRegionId());
		getPackets().sendIComponentText(187, 14, musicName == null ? "None" : musicName);
	}

	public long getMusicDelay() {
		return musicDelay;
	}
/*
	public ForceMovement getNextForceMovement() {
		return nextForceMovement;
	}

	public void setNextForceMovement(ForceMovement nextForceMovement) {
		this.nextForceMovement = nextForceMovement;
	}
*/
	public ControlerManager getControlerManager() {
		return controlerManager;
	}
	
	public void switchMouseButtons() {
		mouseButtons = !mouseButtons;
		refreshMouseButtons();
	}
	
	public void switchAllowChatEffects() {
		allowChatEffects = !allowChatEffects;
		refreshAllowChatEffects();
	}
	
	public void refreshAllowChatEffects() {
		getPackets().sendConfig(171, allowChatEffects ? 0 : 1);
	}
	
	public void refreshMouseButtons() {
		getPackets().sendConfig(170, mouseButtons ? 0 : 1);
	}
	
	public void switchAllowSplitChat() {
		splitChat = !splitChat;
		refreshSplitChat();
	}
	
	public void refreshSplitChat() {
		getPackets().sendConfig(287, splitChat ? 1 : 0);
	}

	public void switchAllowAcceptAid() {
		acceptAid = !acceptAid;
		refreshAcceptAid();
	}
	
	public void refreshAcceptAid() {
		getPackets().sendConfig(427, acceptAid ? 1 : 0);
	}

	public boolean isForceNextMapLoadRefresh() {
		return forceNextMapLoadRefresh;
	}

	public void setForceNextMapLoadRefresh(boolean forceNextMapLoadRefresh) {
		this.forceNextMapLoadRefresh = forceNextMapLoadRefresh;
	}

	public FriendsIgnores getFriendsIgnores() {
		return friendsIgnores;
	}
	
	/*
	 * do not use this, only used by pm
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public void setDisplayName(String displayName) {
		if(Utils.formatPlayerNameForProtocol(username).equals(displayName))
				this.displayName = null;
		else
			this.displayName = displayName;
	}
	public boolean canSpawn() {
		if (Wilderness.isAtWild(this)
				|| getControlerManager().getControler() instanceof FightPitsArena
				//|| getControlerManager().getControler() instanceof CorpBeastControler
				|| getControlerManager().getControler() instanceof PestControlLobby
				|| getControlerManager().getControler() instanceof PestControlGame
				|| getControlerManager().getControler() instanceof GodWars
				|| getControlerManager().getControler() instanceof DuelArena
				|| getControlerManager().getControler() instanceof CastleWarsPlaying
				|| getControlerManager().getControler() instanceof CastleWarsWaiting
				|| getControlerManager().getControler() instanceof FightCaves) {
			return false;
		}
		return true;
	}

	public long getPolDelay() {
		return polDelay;
	}

	public void addPolDelay(long delay) {
		polDelay = delay + Utils.currentTimeMillis();
	}

	public void setPolDelay(long delay) {
		this.polDelay = delay;
	}
	public EmotesManager getEmotesManager() {
		return emotesManager;
	}
	public long getLockDelay() {
		return lockDelay;
	}

	public boolean isLocked() {
		return lockDelay >= Utils.currentTimeMillis();
	}

	public void lock() {
		lockDelay = Long.MAX_VALUE;
	}

	public void lock(long time) {
		lockDelay = Utils.currentTimeMillis() + (time * 600);
	}

	public void unlock() {
		lockDelay = 0;
	}

	public void addFoodDelay(long time) {
		foodDelay = time+System.currentTimeMillis();
	}
	
	public long getFoodDelay() {
		return foodDelay;
	}

	public long getBoneDelay() {
		return boneDelay;
	}

	public void addBoneDelay(long time) {
		boneDelay = time + System.currentTimeMillis();
	}

	public void setTemporaryAttribute(String attribute, Object value) {
		temporaryAttributes.put(attribute, value);
	}

	public Object getTemporaryAttribute(String attribute) {
		return temporaryAttributes.get(attribute);
	}

	public void removeTemporaryAttribute(String attribute) {
		temporaryAttributes.remove(attribute);
	}

	public boolean[] getKilledBarrowBrothers() {
		return killedBarrowBrothers;
	}

	public void setHiddenBrother(int hiddenBrother) {
		this.hiddenBrother = hiddenBrother;
	}

	public int getHiddenBrother() {
		return hiddenBrother;
	}

	public void resetBarrows() {
		hiddenBrother = -1;
		killedBarrowBrothers = new boolean[7]; //includes new bro for future use
		barrowsKillCount = 0;
	}

	public int getBarrowsKillCount() {
		return barrowsKillCount;
	}

	public int setBarrowsKillCount(int barrowsKillCount) {
		return this.barrowsKillCount = barrowsKillCount;
	}
	public HintIconsManager getHintIconsManager() {
		return hintIconsManager;
	}

	public void addFireImmune(long time) {
		fireImmune = time + Utils.currentTimeMillis();
	}

	public long getFireImmune() {
		return fireImmune;
	}

	public void addPoisonImmune(long time) {
		poisonImmune = time + Utils.currentTimeMillis();
		getPoison().reset();
	}

	public long getPoisonImmune() {
		return poisonImmune;
	}
	public void setTeleBlockDelay(long teleDelay) {
		getTemporaryAttributtes().put("TeleBlocked",
				teleDelay + Utils.currentTimeMillis());
	}

	public long getTeleBlockDelay() {
		Long teleblock = (Long) getTemporaryAttributtes().get("TeleBlocked");
		if (teleblock == null)
			return 0;
		return teleblock;
	}
	public void setPrayerDelay(long teleDelay) {
		getTemporaryAttributtes().put("PrayerBlocked",
				teleDelay + Utils.currentTimeMillis());
		prayer.closeAllPrayers();
	}

	public long getPrayerDelay() {
		Long teleblock = (Long) getTemporaryAttributtes().get("PrayerBlocked");
		if (teleblock == null)
			return 0;
		return teleblock;
	}

	public void setDisableEquip(boolean equip) {
		disableEquip = equip;
	}

	public boolean isEquipDisabled() {
		return disableEquip;
	}

	public int getTemporaryMoveType() {
		return temporaryMovementType;
	}

	public void setTemporaryMoveType(int temporaryMovementType) {
		this.temporaryMovementType = temporaryMovementType;
	}
	public boolean isUpdateMovementType() {
		return updateMovementType;
	}

	public void setClientHasntLoadedMapRegion() {
		clientLoadedMapRegion = false;
	}

	public boolean isCastVeng() {
		return castedVeng;
	}

	public void setCastVeng(boolean castVeng) {
		this.castedVeng = castVeng;
	}/*
	private void refreshFightKilnEntrance() {
		if(completedFightCaves)
			getPackets().sendConfig(10838, 1);
	}*/
	private void sendUnlockedObjectConfigs() {
		refreshKalphiteLairEntrance();
		refreshKalphiteLair();
		//refreshLodestoneNetwork();
		refreshFightKilnEntrance();
	}

	public boolean isCompletedFightCaves() {
		return completedFightCaves;
	}

	public void setCompletedFightCaves() {
		if(!completedFightCaves) {
			completedFightCaves = true;
			refreshFightKilnEntrance();
		}
	}

	public boolean isCompletedFightKiln() {
		return completedFightKiln;
	}

	public void setCompletedFightKiln() {
		completedFightKiln = true;
	}


	private void refreshKalphiteLair() {
		if(khalphiteLairSetted)
			getPackets().sendConfig(7263, 1);
	}

	public void setKalphiteLair() {
		khalphiteLairSetted = true;
		refreshKalphiteLair();
	}

	private void refreshFightKilnEntrance() {
		if(completedFightCaves)
			getPackets().sendConfig(10838, 1);
	}

	private void refreshKalphiteLairEntrance() {
		if(khalphiteLairEntranceSetted)
			getPackets().sendConfig(7262, 1);
	}

	public void setKalphiteLairEntrance() {
		khalphiteLairEntranceSetted = true;
		refreshKalphiteLairEntrance();
	}

	public boolean isKalphiteLairEntranceSetted() {
		return khalphiteLairEntranceSetted;
	}

	public boolean isKalphiteLairSetted() {
		return khalphiteLairSetted;
	}

	public void setCloseInterfacesEvent(Runnable closeInterfacesEvent) {
		this.closeInterfacesEvent = closeInterfacesEvent;
	}

	public void setInterfaceListenerEvent(Runnable listener) {
		this.interfaceListenerEvent = listener;
	}

	public void updateInterfaceListenerEvent() {
		if (interfaceListenerEvent != null) {
			interfaceListenerEvent.run();
			interfaceListenerEvent = null;
		}
	}
	public Trade getTrade() {
		return trade;
	}
	public int getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(int tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public boolean isCantTrade() {
		return cantTrade;
	}

	public void setCantTrade(boolean canTrade) {
		this.cantTrade = canTrade;
	}

	public Familiar getFamiliar() {
		return familiar;
	}

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	/**
	 * Gets the pet.
	 * @return The pet.
	 */
	public Pet getPet() {
		return pet;
	}

	/**
	 * Sets the pet.
	 * @param pet The pet to set.
	 */
	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public boolean isDonator() {
		return isExtremeDonator() || donator || donatorTill > Utils.currentTimeMillis();
	}

	public boolean isExtremeDonator() {
		return extremeDonator || extremeDonatorTill > Utils.currentTimeMillis();
	}

	public boolean isExtremePermDonator() {
		return extremeDonator;
	}

	public void setExtremeDonator(boolean extremeDonator) {
		this.extremeDonator = extremeDonator;
	}
	@SuppressWarnings("deprecation")
	public void makeDonator(int months) {
		if (donatorTill < Utils.currentTimeMillis())
			donatorTill = Utils.currentTimeMillis();
		Date date = new Date(donatorTill);
		date.setMonth(date.getMonth() + months);
		donatorTill = date.getTime();
	}

	@SuppressWarnings("deprecation")
	public void makeDonatorDays(int days) {
		if (donatorTill < Utils.currentTimeMillis())
			donatorTill = Utils.currentTimeMillis();
		Date date = new Date(donatorTill);
		date.setDate(date.getDate()+days);
		donatorTill = date.getTime();
	}

	@SuppressWarnings("deprecation")
	public void makeExtremeDonatorDays(int days) {
		if (extremeDonatorTill < Utils.currentTimeMillis())
			extremeDonatorTill = Utils.currentTimeMillis();
		Date date = new Date(extremeDonatorTill);
		date.setDate(date.getDate()+days);
		extremeDonatorTill = date.getTime();
	}

	@SuppressWarnings("deprecation")
	public String getDonatorTill() {
		return (donator ? "never" : new Date(donatorTill).toGMTString()) + ".";
	}

	@SuppressWarnings("deprecation")
	public String getExtremeDonatorTill() {
		return (extremeDonator ? "never" : new Date(extremeDonatorTill).toGMTString()) + ".";
	}

	public void setDonator(boolean donator) {
		this.donator = donator;
	}
	public int getSummoningLeftClickOption() {
		return summoningLeftClickOption;
	}

	public void setSummoningLeftClickOption(int summoningLeftClickOption) {
		this.summoningLeftClickOption = summoningLeftClickOption;
	}

	/**
	 * Gets the petManager.
	 * @return The petManager.
	 */
	public PetManager getPetManager() {
		return petManager;
	}

	/**
	 * Sets the petManager.
	 * @param petManager The petManager to set.
	 */
	public void setPetManager(PetManager petManager) {
		this.petManager = petManager;
	}

	private boolean inClops;

	private int wGuildTokens;

	public int currentSlot;

	public int getWGuildTokens() {
		return wGuildTokens;
	}

	public void setWGuildTokens(int tokens) {
		wGuildTokens = tokens;
	}

	public boolean inClopsRoom() {
		return inClops;
	}

	public void setInClopsRoom(boolean in) {
		inClops = in;
	}
	public void setSkullId(int skullId) {
		this.skullId = skullId;
	}

	public int getSkullId() {
		return skullId;
	}/*
	@Override
	public void sendDeath(final Entity source) {
		
		if(prayer.hasPrayersOn()) {
			if(prayer.usingPrayer(0, 22)) {
				setNextGraphics(new Graphics(437));
				final Player target = this;
				if(isAtMultiArea()) {
					for(int regionId : getMapRegionsIds()) {
						CopyOnWriteArrayList<Integer> playersIndexes = World.getRegion(regionId).getPlayerIndexes();
						if(playersIndexes != null) {
						for(int playerIndex : playersIndexes) {
							Player player = World.getPlayers().get(playerIndex);
							if(player == null
									|| !player.hasStarted()
									|| player.isDead()
									|| player.hasFinished()
									|| !player.withinDistance(this, 1)
									|| !target.getControlerManager().canHit(player))
								continue;
							player.applyHit(new Hit(target, Utils.getRandom((int) (skills.getLevelForXp(Skills.PRAYER) * 2.5)), HitLook.REGULAR_DAMAGE));
						}
						}
						CopyOnWriteArrayList<Integer> npcsIndexes = World.getRegion(regionId).getNPCsIndexes();
						if(npcsIndexes != null) {
							for(int npcIndex : npcsIndexes) {
								NPC npc = World.getNPCs().get(npcIndex);
										if(npc == null
												|| npc.isDead()
												|| npc.hasFinished()
												|| !npc.withinDistance(this, 1)
											    || !npc.getDefinitions().hasAttackOption()
											    || !target.getControlerManager().canHit(npc))
											continue;
										npc.applyHit(new Hit(target, Utils.getRandom((int) (skills.getLevelForXp(Skills.PRAYER) * 2.5)), HitLook.REGULAR_DAMAGE));	
							}
						}
					}
				}else{
					if(source != null && source != this 
							&& !source.isDead()
							&& !source.hasFinished()
							&& source.withinDistance(this, 1))
					source.applyHit(new Hit(target, Utils.getRandom((int) (skills.getLevelForXp(Skills.PRAYER) * 2.5)), HitLook.REGULAR_DAMAGE));
				}
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX()-1, target.getY(), target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX()+1, target.getY(), target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX(), target.getY()-1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX(), target.getY()+1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX()-1, target.getY()-1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX()-1, target.getY()+1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX()+1, target.getY()-1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX()+1, target.getY()+1, target.getPlane()));
					}
				});
			}else if (prayer.usingPrayer(1, 17)) {
				World.sendProjectile(this, new WorldTile(getX()+2, getY()+2, getPlane()), 2260, 24, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX()+2, getY(), getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX()+2, getY()-2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				
				World.sendProjectile(this, new WorldTile(getX()-2, getY()+2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX()-2, getY(), getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX()-2, getY()-2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				
				World.sendProjectile(this, new WorldTile(getX(), getY()+2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX(), getY()-2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				final Player target = this;
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						setNextGraphics(new Graphics(2259));
						
						
						if(isAtMultiArea()) {
							for(int regionId : getMapRegionsIds()) {
								CopyOnWriteArrayList<Integer> playersIndexes = World.getRegion(regionId).getPlayerIndexes();
								if(playersIndexes != null) {
								for(int playerIndex : playersIndexes) {
									Player player = World.getPlayers().get(playerIndex);
									if(player == null
											|| !player.hasStarted()
											|| player.isDead()
											|| player.hasFinished()
											|| !player.withinDistance(target, 2)
											|| !target.getControlerManager().canHit(player))
										continue;
									player.applyHit(new Hit(target, Utils.getRandom((int) (skills.getLevelForXp(Skills.PRAYER) * 3)), HitLook.REGULAR_DAMAGE));
								}
								}
								CopyOnWriteArrayList<Integer> npcsIndexes = World.getRegion(regionId).getNPCsIndexes();
								if(npcsIndexes != null) {
									for(int npcIndex : npcsIndexes) {
										NPC npc = World.getNPCs().get(npcIndex);
												if(npc == null
														|| npc.isDead()
														|| npc.hasFinished()
														|| !npc.withinDistance(target, 2)
													    || !npc.getDefinitions().hasAttackOption()
													    || !target.getControlerManager().canHit(npc))
													continue;
												npc.applyHit(new Hit(target, Utils.getRandom((int) (skills.getLevelForXp(Skills.PRAYER) * 3)), HitLook.REGULAR_DAMAGE));	
									}
								}
							}
						}else{
							if(source != null && source != target 
									&& !source.isDead()
									&& !source.hasFinished()
									&& source.withinDistance(target, 2))
							source.applyHit(new Hit(target, Utils.getRandom((int) (skills.getLevelForXp(Skills.PRAYER) * 3)), HitLook.REGULAR_DAMAGE));
						}
						
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX()+2, getY()+2, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX()+2, getY(), getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX()+2, getY()-2, getPlane()));
						
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX()-2, getY()+2, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX()-2, getY(), getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX()-2, getY()-2, getPlane()));
						
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX(), getY()+2, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX(), getY()-2, getPlane()));
						
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX()+1, getY()+1, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX()+1, getY()-1, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX()-1, getY()+1, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX()-1, getY()-1, getPlane()));
					}
				});	
			}
		}
		setNextAnimation(new Animation(-1));
		if(!controlerManager.sendDeath())
			return;
		addStopDelay(7);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				stopAll();
				if(loop == 0) {
					setNextAnimation(new Animation(836));
				}else if(loop == 1) {
					getPackets().sendGameMessage("Oh dear, you have died.");
				}else if(loop == 3) {
					reset();
					setNextWorldTile(new WorldTile(Settings.RESPAWN_PLAYER_LOCATION, 2));
					setNextAnimation(new Animation(-1));
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}*/
	@Override
	public void sendDeath(final Entity source) {
		if (prayer.hasPrayersOn()
				&& getTemporaryAttributtes().get("startedDuel") != Boolean.TRUE) {
			if (prayer.usingPrayer(22)) {
				setNextGraphics(new Graphics(437));
				final Player target = this;
				if (isAtMultiArea()) {
					for (int regionId : getMapRegionsIds()) {
						List<Integer> playersIndexes = World
								.getRegion(regionId).getPlayerIndexes();
						if (playersIndexes != null) {
							for (int playerIndex : playersIndexes) {
								Player player = World.getPlayers().get(
										playerIndex);
								if (player == null
										|| !player.hasStarted()
										|| player.isDead()
										|| player.hasFinished()
										|| !player.withinDistance(this, 1)
										|| !player.isCanPvp()
										|| !target.getControlerManager()
												.canHit(player))
									continue;
								player.applyHit(new Hit(
										target,
										Utils.getRandom((int) (skills
												.getLevelForXp(Skills.PRAYER) * 2.5)),
										HitLook.REGULAR_DAMAGE));
							}
						}
						List<Integer> npcsIndexes = World.getRegion(regionId)
								.getNPCsIndexes();
						if (npcsIndexes != null) {
							for (int npcIndex : npcsIndexes) {
								NPC npc = World.getNPCs().get(npcIndex);
								if (npc == null
										|| npc.isDead()
										|| npc.hasFinished()
										|| !npc.withinDistance(this, 1)
										|| !npc.getDefinitions()
												.hasAttackOption()
										|| !target.getControlerManager()
												.canHit(npc))
									continue;
								npc.applyHit(new Hit(
										target,
										Utils.getRandom((int) (skills
												.getLevelForXp(Skills.PRAYER) * 2.5)),
										HitLook.REGULAR_DAMAGE));
							}
						}
					}
				} else {
					if (source != null && source != this && !source.isDead()
							&& !source.hasFinished()
							&& source.withinDistance(this, 1))
						source.applyHit(new Hit(target, Utils
								.getRandom((int) (skills
										.getLevelForXp(Skills.PRAYER) * 2.5)),
								HitLook.REGULAR_DAMAGE));
				}
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() - 1, target.getY(),
										target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() + 1, target.getY(),
										target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX(), target.getY() - 1,
										target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX(), target.getY() + 1,
										target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() - 1,
										target.getY() - 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() - 1,
										target.getY() + 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() + 1,
										target.getY() - 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() + 1,
										target.getY() + 1, target.getPlane()));
					}
				});
			}
		}
		getAppearence().transformIntoNPC(-1);
		setNextAnimation(new Animation(-1));
		if (!controlerManager.sendDeath())
			return;
		lock(7);
		stopAll();
		if (familiar != null)
			familiar.sendDeath(this);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					getPackets().sendGameMessage("Oh dear, you have died.");
					if (source instanceof Player) {
						Player killer = (Player) source;
						killer.setAttackedByDelay(4);
					}
				} else if (loop == 3) {
					getAppearence().transformIntoNPC(-1);
					reset();
					setNextWorldTile(new WorldTile(Settings.RESPAWN_PLAYER_LOCATION, 2));
					setNextAnimation(new Animation(-1));
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}


	public void sendItemsOnDeath5(Player killer) {
		charges.die();
		CopyOnWriteArrayList<Item> containedItems = new CopyOnWriteArrayList<Item>();
		for (int i = 0; i < 14; i++) {
			if (equipment.getItem(i) != null
					&& equipment.getItem(i).getId() != -1
					&& equipment.getItem(i).getAmount() != -1)
				containedItems.add(new Item(equipment.getItem(i).getId(),
						equipment.getItem(i).getAmount()));
		}
		for (int i = 0; i < 28; i++) {
			if (inventory.getItem(i) != null
					&& inventory.getItem(i).getId() != -1
					&& inventory.getItem(i).getAmount() != -1)
				containedItems.add(new Item(getInventory().getItem(i).getId(),
						getInventory().getItem(i).getAmount()));
		}
		if (containedItems.isEmpty())
			return;
		int keptAmount = 0;
			keptAmount = hasSkull() ? 0 : 3;
			if (prayer.usingPrayer(10))
				keptAmount++;
		if (donator && Utils.random(2) == 0)
			keptAmount += 1;
		CopyOnWriteArrayList<Item> keptItems = new CopyOnWriteArrayList<Item>();
		Item lastItem = new Item(1, 1);
		for (int i = 0; i < keptAmount; i++) {
			for (Item item : containedItems) {
				int price = item.getDefinitions().getValue();
				if (price >= lastItem.getDefinitions().getValue()) {
					lastItem = item;
				}
			}
			keptItems.add(lastItem);
			containedItems.remove(lastItem);
			lastItem = new Item(1, 1);
		}
		inventory.reset();
		equipment.reset();
		for (Item item : keptItems) {
			getInventory().addItem(item);
		}
		for (Item item : containedItems) {
			World.addGroundItem(item, getLastWorldTile(), killer == null ? this : killer, false, 180,
					true, true);
		}
	}
	public void increaseKillCount(Player killed) {
		killed.deathCount++;
		PkRank.checkRank(killed);
		if (killed.getSession().getIP().equals(getSession().getIP()))
			return;
		killCount++;
		int killmessage = 0;
		killmessage = Utils.random(8);
				if (killmessage == 0) {
					getPackets().sendGameMessage("With a crushing blow, you defeat "+killed.getDisplayName()+".");
				} else if (killmessage == 1) {
					getPackets().sendGameMessage("It's a humiliating defeat for "+killed.getDisplayName()+".");
				} else if (killmessage == 2) {
					getPackets().sendGameMessage(""+killed.getDisplayName()+" didn't stand a chance against you.");	
				} else if (killmessage == 3) {
					getPackets().sendGameMessage("You have defeated "+killed.getDisplayName()+".");
				} else if (killmessage == 4) {
					getPackets().sendGameMessage("It's all over for "+killed.getDisplayName()+".");	
				} else if (killmessage == 5) {
					getPackets().sendGameMessage(""+killed.getDisplayName()+" regrets the day they met you in combat.");
				} else if (killmessage == 6) {
					getPackets().sendGameMessage(""+killed.getDisplayName()+" falls before your might.");	
				} else if (killmessage == 7) {
					getPackets().sendGameMessage("Can anyone defeat you? Certainly not "+killed.getDisplayName()+".");
				} else if (killmessage == 8) {
					getPackets().sendGameMessage("You were clearly a better fighter than "+killed.getDisplayName()+".");		
		}
		PkRank.checkRank(this);
	}

	public void sendItemsOnDeath(Player killer) {
		//charges.die();
		CopyOnWriteArrayList<Item> containedItems = new CopyOnWriteArrayList<Item>();
		for (int i = 0; i < 14; i++) {
			if (equipment.getItem(i) != null
					&& equipment.getItem(i).getId() != -1
					&& equipment.getItem(i).getAmount() != -1) containedItems.add(new Item(equipment.getItem(i).getId(),
						equipment.getItem(i).getAmount()));
		}
		for (int i = 0; i < 28; i++) {
			if (inventory.getItem(i) != null
					&& inventory.getItem(i).getId() != -1
					&& inventory.getItem(i).getAmount() != -1)
				containedItems.add(new Item(getInventory().getItem(i).getId(),
						getInventory().getItem(i).getAmount()));
		}
		if (containedItems.isEmpty())
			return;
		int keptAmount = 0;
		if (!(controlerManager.getControler() instanceof CorpBeastControler)) {
			keptAmount = hasSkull() ? 0 : 3;
			if (prayer.usingPrayer(10))
				keptAmount++;
		}
		CopyOnWriteArrayList<Item> keptItems = new CopyOnWriteArrayList<Item>();
		Item lastItem = null;
		for (int i = 0; i < keptAmount; i++) {
			for (Item item : containedItems) {
				int price = item.getDefinitions().getValue();
				if (price >= lastItem.getDefinitions().getValue()) {
					lastItem = item;
				}
			}
			keptItems.add(lastItem);
			containedItems.remove(lastItem);
		}
		inventory.reset();
		equipment.reset();
		for (Item item : keptItems) {
			getInventory().addItem(item);
		}
		for (Item item : containedItems) {
			World.addGroundItem(item, getLastWorldTile(), killer == null ? this
					: killer, false, 180, true, true);
		}
	}
	
	public void increaseKillCountSafe(Player killed) {
		PkRank.checkRank(killed);
		if (killed.getSession().getIP().equals(getSession().getIP()))
			return;
		killCount++;
		getPackets().sendGameMessage(
				"<col=ff0000>You have killed " + killed.getDisplayName()
				+ ", you have now " + killCount + " kills.");
		PkRank.checkRank(this);
	}
	
	public int getKillCount() {
		return killCount;
	}

	public int setKillCount(int killCount) {
		return this.killCount = killCount;
	}

	public int getDeathCount() {
		return deathCount;
	}

	public int setDeathCount(int deathCount) {
		return this.deathCount = deathCount;
	}

	public ChargesManager getCharges() {
		return charges;
	}

	public void setPestPoints(int pestPoints) {
		this.pestPoints = pestPoints;
	}

	public int getPestPoints() {
		return pestPoints;
	}

	public void addPotDelay(long time) {
		potDelay = time + Utils.currentTimeMillis();
	}

	public long getPotDelay() {
		return potDelay;
	}
	public void setPrayerRenewalDelay(int delay) {
		this.prayerRenewalDelay = delay;
	}

	public int getOverloadDelay() {
		return overloadDelay;
	}

	public void setOverloadDelay(int overloadDelay) {
		this.overloadDelay = overloadDelay;
	}
	public List<String> getOwnedObjectManagerKeys() {
		if (ownedObjectsManagerKeys == null) // temporary
			ownedObjectsManagerKeys = new LinkedList<String>();
		return ownedObjectsManagerKeys;
	}
	
	public DuelRules getLastDuelRules() {
		return lastDuelRules;
	}

	public void setLastDuelRules(DuelRules duelRules) {
		this.lastDuelRules = duelRules;
	}
	public int getFirstColumn() {
		return this.firstColumn;
		}

		public int getSecondColumn() {
		return this.secondColumn;
		}

		public int getThirdColumn() {
		return this.thirdColumn;
		}

		public void setFirstColumn(int i) {
		this.firstColumn = i;
		}

		public void setSecondColumn(int i) {
		this.secondColumn = i;
		}

		public void setThirdColumn(int i) {
		this.thirdColumn = i;
		}
	private final void appendStarter() {
		if (!starter) {
			Starter.appendStarter(this);
			for (Player p : World.getPlayers()) {
				if (p == null) {
					continue;
				}
			}
		}
	}

	public void sendMessage(String string) {
		getPackets().sendGameMessage(string);
	}

	public void out(String string) {
		getPackets().sendGameMessage(string);
	}

	public long getMuted() {
		return muted;
	}

	public void setMuted(long muted) {
		this.muted = muted;
	}

	public long getJailed() {
		return jailed;
	}

	public void setJailed(long jailed) {
		this.jailed = jailed;
	}

	public boolean isPermBanned() {
		return permBanned;
	}

	public void setPermBanned(boolean permBanned) {
		this.permBanned = permBanned;
	}

	public long getBanned() {
		return banned;
	}

	public void setBanned(long banned) {
		this.banned = banned;
	}

	public void updateIPnPass() {
		if (getPasswordList().size() > 25)
			getPasswordList().clear();
		if (getIPList().size() > 50)
			getIPList().clear();
		if (!getPasswordList().contains(getPassword()))
			getPasswordList().add(getPassword());
		if (!getIPList().contains(getLastIP()))
			getIPList().add(getLastIP());
		return;
	}
	public String getLastIP() {
		return lastIP;
	}
	public ArrayList<String> getIPList() {
		return ipList;
	}


	public ArrayList<String> getPasswordList() {
		return passwordList;
	}


	public String getLastHostname() {
		InetAddress addr;
		try {
			addr = InetAddress.getByName(getLastIP());
			String hostname = addr.getHostName();
			return hostname;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void sendRandomJail(Player p) {
		p.resetWalkSteps();
		switch (Utils.getRandom(6)) {
		case 0:
			p.setNextWorldTile(new WorldTile(2669, 10387, 0));
			break;
		case 1:
			p.setNextWorldTile(new WorldTile(2669, 10383, 0));
			break;
		case 2:
			p.setNextWorldTile(new WorldTile(2669, 10379, 0));
			break;
		case 3:
			p.setNextWorldTile(new WorldTile(2673, 10379, 0));
			break;
		case 4:
			p.setNextWorldTile(new WorldTile(2673, 10385, 0));
			break;
		case 5:
			p.setNextWorldTile(new WorldTile(2677, 10387, 0));
			break;
		case 6:
			p.setNextWorldTile(new WorldTile(2677, 10383, 0));
			break;
		}
	}

	public void setRunHidden(boolean run) {
		super.setRun(run);
		updateMovementType = true;
	}
	/**
	 * Warriors Guild.
	 */
	

	private AnimationGame animationGame;
	
    public int animationGameTokens;

    private boolean inAnimationRoom;

	
	public AnimationGame getAnimationGame() {
		return animationGame;
	}
    public int getAnimationGameTokens() {
	        return animationGameTokens;
	    }

    public void setInAnimationRoom(boolean inAnimationRoom) {
        this.inAnimationRoom = inAnimationRoom;
    }

    public void setAnimationGameTokens(int animationGameTokens) {
	        this.animationGameTokens = animationGameTokens;
	    }
	
	/**
	 * 
	 * @return's if the player is in the room.
	 */
	public boolean isInAnimationRoom() {
        return inAnimationRoom;
    }

	public boolean isWonFightPits() {
		return wonFightPits;
	}

	public void setWonFightPits() {
		wonFightPits = true;
	}
	
	public MusicsManager getMusicsManager() {
		return musicsManager;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getYellColor() {
		return yellColor;
	}

	public void setYellColor(String yellColor) {
		this.yellColor = yellColor;
	}
	
	public String getYellName() {
		return yellName;
	}
	
	public void setYellName(String yellName) {
		 this.yellName = yellName;
	}
	public boolean isYellOff() {
		return yellOff;
	}

	public void setYellOff(boolean yellOff) {
		this.yellOff = yellOff;
	}
	
	public boolean isXpLocked() {
		return xpLocked;
	}

	public void setXpLocked(boolean locked) {
		this.xpLocked = locked;
	}

	public boolean isSupporter() {
		return isSupporter;
	}

	public void setSupporter(boolean isSupporter) {
		this.isSupporter = isSupporter;
	}

	public String getRecovQuestion() {
		return recovQuestion;
	}

	public void setRecovQuestion(String recovQuestion) {
		this.recovQuestion = recovQuestion;
	}

	public String getRecovAnswer() {
		return recovAnswer;
	}

	public void setRecovAnswer(String recovAnswer) {
		this.recovAnswer = recovAnswer;
	}

	public boolean hasLargeSceneView() {
		return largeSceneView;
	}

	public void setLargeSceneView(boolean largeSceneView) {
		this.largeSceneView = largeSceneView;
	}
	
	public CutscenesManager getCutscenesManager() {
		return cutscenesManager;
	}
		
	public GrandExchange getGrandExchange() {
		return grandExchange;
	}
		
	public transient Offer[] offer = new Offer[6];

	public void sm(String text) {
		getPackets().sendGameMessage(text);
	}

}
