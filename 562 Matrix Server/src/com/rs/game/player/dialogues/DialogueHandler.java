package com.rs.game.player.dialogues;

import java.util.HashMap;

public final class DialogueHandler {

	private static final HashMap<Object, Class<Dialogue>> handledDialogues = new HashMap<Object, Class<Dialogue>>();
	
	@SuppressWarnings("unchecked")
	public static final void init() {
		try {
			Class<Dialogue> value1 = (Class<Dialogue>) Class.forName(LevelUp.class.getCanonicalName());
			handledDialogues.put("LevelUp", value1);
			Class<Dialogue> value3 = (Class<Dialogue>) Class.forName(ClimbNoEmoteStairs.class.getCanonicalName());
			handledDialogues.put("ClimbNoEmoteStairs", value3);
			Class<Dialogue> value4 = (Class<Dialogue>) Class.forName(Banker.class.getCanonicalName());
			handledDialogues.put("Banker", value4);
			Class<Dialogue> value5 = (Class<Dialogue>) Class.forName(DestroyItemOption.class.getCanonicalName());
			handledDialogues.put("DestroyItemOption", value5);
			Class<Dialogue> value8 = (Class<Dialogue>) Class.forName(test.class.getCanonicalName());
			handledDialogues.put("test", value8);
			Class<Dialogue> value9 = (Class<Dialogue>) Class
					.forName(BarrowsD.class.getCanonicalName());
			handledDialogues.put("BarrowsD", value9);
			Class<Dialogue> value10 = (Class<Dialogue>) Class
					.forName(CookingD.class.getCanonicalName());
			handledDialogues.put("CookingD", value10);
			Class<Dialogue> value11 = (Class<Dialogue>) Class
					.forName(SmeltingD.class.getCanonicalName());
			handledDialogues.put("SmeltingD", value11);
			Class<Dialogue> value12 = (Class<Dialogue>) Class
					.forName(Transportation.class.getCanonicalName());
			handledDialogues.put("Transportation", value12);
			Class<Dialogue> value13 = (Class<Dialogue>) Class
					.forName(WildernessDitch.class.getCanonicalName());
			handledDialogues.put("WildernessDitch", value13);
			Class<Dialogue> value14 = (Class<Dialogue>) Class
					.forName(LunarAltar.class.getCanonicalName());
			handledDialogues.put("LunarAltar", value14);
			Class<Dialogue> value15 = (Class<Dialogue>) Class
					.forName(AncientAltar.class.getCanonicalName());
			handledDialogues.put("AncientAltar", value15);
			handledDialogues.put("LanderD", (Class<Dialogue>) Class.forName(LanderDialouge.class.getCanonicalName()));
			//handledDialogues.put("MiningGuildDwarf", (Class<Dialogue>) Class.forName(MiningGuildDwarf.class.getCanonicalName()));
			Class<Dialogue> value16 = (Class<Dialogue>) Class
					.forName(TeleportMinigame.class.getCanonicalName());
			handledDialogues.put("TeleportMinigame", value16);
			Class<Dialogue> value17 = (Class<Dialogue>) Class
					.forName(TeleportBosses.class.getCanonicalName());
			handledDialogues.put("TeleportBosses", value17);
			Class<Dialogue> value18 = (Class<Dialogue>) Class
					.forName(TeleportTraining.class.getCanonicalName());
			handledDialogues.put("TeleportTraining", value18);
			Class<Dialogue> value20 = (Class<Dialogue>) Class
					.forName(BorkEnter.class.getCanonicalName());
			handledDialogues.put("BorkEnter", value20);
            Class<Dialogue> value21 = (Class<Dialogue>) Class
					.forName(Varnis.class.getCanonicalName());
			handledDialogues.put("Varnis", value21);
			Class<Dialogue> value22 = (Class<Dialogue>) Class
					.forName(Veliaf.class.getCanonicalName());
			handledDialogues.put("Veliaf", value22);
			Class<Dialogue> value23 = (Class<Dialogue>) Class
					.forName(SetSkills.class.getCanonicalName());
			handledDialogues.put("SetSkills", value23);
			Class<Dialogue> value24 = (Class<Dialogue>) Class
					.forName(Pool.class.getCanonicalName());
			handledDialogues.put("Pool", value24);
			Class<Dialogue> value25 = (Class<Dialogue>) Class
					.forName(DismissD.class.getCanonicalName());
			handledDialogues.put("DismissD", value25);
			Class<Dialogue> value27 = (Class<Dialogue>) Class
					.forName(NewHomeGuide.class.getCanonicalName());
			handledDialogues.put("NewHomeGuide", value27);
			Class<Dialogue> value28 = (Class<Dialogue>) Class
					.forName(Talk.class.getCanonicalName());
			handledDialogues.put("Talk", value28);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static final void reload() {
		handledDialogues.clear();
		init();
	}
	
	public static final Dialogue getDialogue(Object key) {
		Class<Dialogue> classD = handledDialogues.get(key);
		if(classD == null)
			return null;
		try {
			return classD.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private DialogueHandler() {
		
	}
}
