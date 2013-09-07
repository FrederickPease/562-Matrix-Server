package com.rs.game.player;

import java.util.concurrent.ConcurrentHashMap;

public class InterfaceManager {

	
	
	public static final int FIXED_WINDOW_ID = 548;
	public static final int RESIZABLE_WINDOW_ID = 746;
	public static final int CHAT_BOX_TAB = 12;
	public static final int FIXED_SCREEN_TAB_ID = 16;
	public static final int RESIZABLE_SCREEN_TAB_ID = 8;
	public static final int FIXED_INV_TAB_ID = 145;
	public static final int RESIZABLE_INV_TAB_ID = 26;
	private Player player;

	private final ConcurrentHashMap<Integer, int[]> openedinterfaces = new ConcurrentHashMap<Integer, int[]>();
	
	private boolean resizableScreen;
	private int windowsPane;
	
	public InterfaceManager(Player player) {
		this.player = player;
	}
	
	public void sendTab(int tabId, int interfaceId) {
		player.getPackets().sendInterface(true, resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, tabId, interfaceId);
	}
	
	public void sendChatBoxInterface(int interfaceId) {
		player.getPackets().sendInterface(true, 752, CHAT_BOX_TAB, interfaceId);
	}
	
	public void closeChatBoxInterface() {
		player.getPackets().closeInterface(CHAT_BOX_TAB);
	}
	
	public void sendInterface(int interfaceId) {
		player.getPackets().sendInterface(false, resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, resizableScreen ? RESIZABLE_SCREEN_TAB_ID : FIXED_SCREEN_TAB_ID, interfaceId);
	}
	public void sendInventoryInterface(int childId) {
		player.getPackets().sendInterface(false, resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, resizableScreen ? RESIZABLE_INV_TAB_ID : FIXED_INV_TAB_ID, childId);
	}
/*	
	public final void sendInterfaces() {
		if(player.getDisplayMode() == 2 || player.getDisplayMode() == 3) {
			resizableScreen = true;
			sendFullScreenInterfaces();
		}else{
			resizableScreen = false;
			sendFixedInterfaces();
		}
		player.getCombatDefinitions().sendUnlockAttackStylesButtons();
		player.getEmotesManager().unlockEmotesBook();
		player.getPackets().sendHideIComponent(745, 1, !player.isAtMultiArea()); //multi icon
		player.getPrayer().init(); //unlock pray options
		player.getControlerManager().sendInterfaces();
	}*/
	public final void sendInterfaces() {
		if(player.getDisplayMode() == 2 || player.getDisplayMode() == 3) {
			resizableScreen = true;
			sendFullScreenInterfaces();
		}else{
			resizableScreen = false;
			sendFixedInterfaces();
		}
		player.getPackets().sendHideIComponent(745, 1, !player.isAtMultiArea()); //multi icon
		player.getEmotesManager().unlockEmotesBook();
		player.getCombatDefinitions().sendUnlockAttackStylesButtons();
		player.getMusicsManager().unlockMusicPlayer();
		player.getPrayer().init(); //unlock pray options
		if (player.getFamiliar() != null && player.isRunning())
			player.getFamiliar().unlock();
		player.getControlerManager().sendInterfaces();
	}
	
	public void sendFullScreenInterfaces() {
		player.getPackets().sendWindowsPane(746, 0);
		sendTab(11, 745);
		sendTab(15, 751);
		sendTab(18, 752);
		sendTab(19, 754);
		sendTab(163, 748);
		sendTab(164, 749);
		sendTab(165, 750);
		sendTab(166, 747);
		player.getPackets().sendInterface(true, 752, 8, 137);
		sendTab(29, 884);
		sendTab(30, 320);
		sendTab(31, 190);
		sendTab(32, 259);//259); //  Achievement tab
		sendTab(33, 149);
		sendTab(34, 387);
		sendPrayerBook();
		sendMagicBook();
		sendTab(38, 550);
		sendTab(39, 551); //551 ignore now friendchat
		sendTab(40, 589); //589 old clan chat now new clan chat
		sendTab(41, 261);
		sendTab(42, 464);
		sendTab(43, 187);
		sendTab(44, 34);
		sendTab(47, 182);
	}
	
	public void sendPrayerBook() {
		sendTab(resizableScreen ? 35 : 156, 271);
	}
	
	
	public void sendMagicBook() {
		sendTab(resizableScreen ? 36 : 157, player.getCombatDefinitions().getSpellBook());
	}
	
	public void sendFixedInterfaces() {
		player.getPackets().sendWindowsPane(548, 0);
		sendTab(12, 745);
		sendTab(20, 751); // Chat options
		sendTab(140, 752); // Chatbox
		sendTab(137, 747);//139 
		sendTab(133, 748); // HP bar
		sendTab(134, 749); // Prayer bar
		sendTab(135, 750); // Energy bar
		player.getPackets().sendInterface(true, 752, 8, 137); // Playername on chat
		sendTab(150,  884); // Attack tab
		sendTab(151, 320); // Skill tab
		sendTab(152, 190); //  Quest tab
		sendTab(153, 259);//259); //  Achievement tab
		sendTab(154, 149); // Inventory tab
		sendTab(155, 387); // Equipment tab
		sendPrayerBook();
		sendMagicBook();
		sendTab(159, 550); // Friend tab
		sendTab(160,  551); //551 ignore now friendchat// Ignore tab
		sendTab(161, 589); //589 old clan chat now new clan chat// Clan tab
		sendTab(162, 261); // Setting tab
		sendTab(163, 464); // Emote tab
		sendTab(164, 187); // Music tab
		sendTab(165, 34); // Notes tab.
		sendTab(168, 182); // Logout tab
	}

	public boolean addInterface(int windowId, int tabId, int childId) {
		if (openedinterfaces.containsKey(tabId))
			player.getPackets().closeInterface(tabId);
		openedinterfaces.put(tabId, new int[] { childId, windowId });
		return openedinterfaces.get(tabId)[0] == childId;
	}

	public boolean containsInterface(int tabId, int childId) {
		if (childId == windowsPane)
			return true;
		if (!openedinterfaces.containsKey(tabId))
			return false;
		return openedinterfaces.get(tabId)[0] == childId;
	}

	public int getTabWindow(int tabId) {
		if (!openedinterfaces.containsKey(tabId))
			return FIXED_WINDOW_ID;
		return openedinterfaces.get(tabId)[1];
	}

	public boolean containsInterface(int childId) {
		if (childId == windowsPane)
			return true;
		for (int[] value : openedinterfaces.values())
			if (value[0] == childId)
				return true;
		return false;
	}

	public boolean containsTab(int tabId) {
		return openedinterfaces.containsKey(tabId);
	}

	public void removeAll() {
		openedinterfaces.clear();
	}
	
	public boolean containsScreenInter() {
		return containsTab(resizableScreen ? RESIZABLE_SCREEN_TAB_ID : FIXED_SCREEN_TAB_ID);
	}
	
	public void closeScreenInterface() {
		player.getPackets().closeInterface(resizableScreen ? RESIZABLE_SCREEN_TAB_ID : FIXED_SCREEN_TAB_ID);
	}
	
	public boolean containsInventoryInter() {
		return containsTab(resizableScreen ? RESIZABLE_INV_TAB_ID : FIXED_INV_TAB_ID);
	}
	public void closeInventoryInterface() {
		player.getPackets().closeInterface(resizableScreen ? RESIZABLE_INV_TAB_ID : FIXED_INV_TAB_ID);
	}
	
	
	public boolean containsChatBoxInter() {
		return containsTab(CHAT_BOX_TAB);
	}

	public boolean removeTab(int tabId) {
		return openedinterfaces.remove(tabId) != null;
	}

	public boolean removeInterface(int tabId, int childId) {
		if (!openedinterfaces.containsKey(tabId))
			return false;
		if (openedinterfaces.get(tabId)[0] != childId)
			return false;
		return openedinterfaces.remove(tabId) != null;
	}

	public boolean hasRezizableScreen() {
		return resizableScreen;
	}

	public void setWindowsPane(int windowsPane) {
		this.windowsPane = windowsPane;
	}

	public int getWindowsPane() {
		return windowsPane;
	}

	public void closeInventory() {
		player.getPackets().closeInterface(resizableScreen ? 115 : 175);
	}

	public void sendInventory() {
		sendTab(resizableScreen ? 115 : 175, Inventory.INVENTORY_INTERFACE);
	}
	public void replaceRealChatBoxInterface(int interfaceId) {
		player.getPackets().sendInterface(true, 752, 12, interfaceId);
	}

	public void setGeSearch(Object[] o) {
		player.getPackets().sendConfig1(1109, -1);
		player.getPackets().sendConfig1(1112, 0);
		player.getPackets().sendConfig1(1113, 0);
		replaceRealChatBoxInterface(389);
		player.getPackets().sendRunScript(570, o, "s");
	}

	public void closeReplacedRealChatBoxInterface() {
		player.getPackets().closeInterface(752, 12);
	}


	public void closeEquipment() {
		player.getPackets().closeInterface(resizableScreen ? 95 : 209);
	}
	public void sendEquipment() {
		sendTab(resizableScreen ? 95 : 209, 387);
	}
	/*
	 * returns lastGameTab
	 */
	public int openGameTab(int tabId) {
		player.getPackets().sendConfig(168, tabId);
		int lastTab = 4; // tabId
		// tab = tabId;
		return lastTab;
	}

	public void closePrayerBook() {
		player.getPackets().closeInterface(resizableScreen ? 117 : 210);
	}


	
	public void closeMagicBook() {
		player.getPackets().closeInterface(resizableScreen ? 118 : 211);
	}
	
	public void sendEmotes() {
		sendTab(resizableScreen ? 124 : 184, 590);
	}
	
	public void closeEmotes() {
		player.getPackets().closeInterface(resizableScreen ? 124 : 217);
	}
	
	public void closeSkills() {
		player.getPackets().closeInterface(resizableScreen ? 113 : 206);
	}
	
	public void closeCombatStyles() {
		player.getPackets().closeInterface(resizableScreen ? 111 : 204);
	}
	
	public void closeTaskSystem() {
		player.getPackets().closeInterface(resizableScreen ? 112 : 205);
	}

	public void sendOverlay(int interfaceId, boolean fullScreen) {
		sendTab(resizableScreen ? fullScreen ? 1 : 11 : 0, interfaceId);
	}

	public void closeOverlay(boolean fullScreen) {
		player.getPackets().closeInterface(resizableScreen ? fullScreen ? 1 : 11 : 0);
	}

	public void sendSettings() {
		sendSettings(261);
	}

	public void sendSettings(int interfaceId) {
		sendTab(resizableScreen ? 123 : 183, interfaceId);
	}

	

}
