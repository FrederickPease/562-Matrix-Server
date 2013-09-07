package com.rs.game.player.controlers;

import java.util.HashMap;

import com.rs.game.minigames.BrimhavenAgility;
import com.rs.game.minigames.creations.StealingCreationGame;
import com.rs.game.minigames.creations.StealingCreationLobby;
import com.rs.game.minigames.duel.DuelArena;
import com.rs.game.minigames.duel.DuelControler;
import com.rs.game.minigames.soulwars.SoulWars;
import com.rs.game.player.controlers.castlewars.CastleWarsPlaying;
import com.rs.game.player.controlers.castlewars.CastleWarsWaiting;
import com.rs.game.player.controlers.events.DeathEvent;
import com.rs.game.player.controlers.fightpits.FightPitsArena;
import com.rs.game.player.controlers.fightpits.FightPitsLobby;
import com.rs.game.player.controlers.pestcontrol.PestControlGame;
import com.rs.game.player.controlers.pestcontrol.PestControlLobby;


public class ControlerHandler {

	private static final HashMap<Object, Class<Controler>> handledControlers = new HashMap<Object, Class<Controler>>();

	@SuppressWarnings("unchecked")
	public static final void init() {
		try {
			Class<Controler> value1 = (Class<Controler>) Class.forName(Wilderness.class.getCanonicalName());
			handledControlers.put("Wilderness", value1);
			Class<Controler> value4 = (Class<Controler>) Class.forName(GodWars.class.getCanonicalName());
			handledControlers.put("GodWars", value4);
			handledControlers.put("Barrows", (Class<Controler>) Class.forName(Barrows.class.getCanonicalName()));
			Class<Controler> value5 = (Class<Controler>) Class
					.forName(CastleWarsPlaying.class.getCanonicalName());
			handledControlers.put("CastleWarsPlaying", value5);
			Class<Controler> value6 = (Class<Controler>) Class
					.forName(CastleWarsWaiting.class.getCanonicalName());
			handledControlers.put("CastleWarsWaiting", value6);
			handledControlers.put("BrimhavenAgility", (Class<Controler>) Class.forName(BrimhavenAgility.class.getCanonicalName()));
			handledControlers.put("DeathEvent", (Class<Controler>) Class.forName(DeathEvent.class.getCanonicalName()));
			handledControlers.put("HouseControler", (Class<Controler>) Class.forName(HouseControler.class.getCanonicalName()));
			handledControlers.put("PestControlGame", (Class<Controler>) Class.forName(PestControlGame.class.getCanonicalName()));
			handledControlers.put("FightCavesControler", (Class<Controler>) Class.forName(FightCaves.class.getCanonicalName()));
			handledControlers.put("PestControlLobby", (Class<Controler>) Class.forName(PestControlLobby.class.getCanonicalName()));
			handledControlers.put("Soulwars", (Class<Controler>) Class.forName(SoulWars.class.getCanonicalName()));
			Class<Controler> value7 = (Class<Controler>) Class
					.forName(DuelArena.class.getCanonicalName());
			handledControlers.put("DuelArena", value7);
			Class<Controler> value8 = (Class<Controler>) Class
					.forName(DuelControler.class.getCanonicalName());
			handledControlers.put("DuelControler", value8);
		//	handledControlers.put("Barrows", (Class<Controler>) Class.forName(Barrows.class.getCanonicalName()));
			Class<Controler> value9 = (Class<Controler>) Class
					.forName(ObeliskControler.class.getCanonicalName());
			handledControlers.put("ObeliskControler", value9);
			Class<Controler> value10 = (Class<Controler>) Class
					.forName(ObeliskControler.class.getCanonicalName());
			handledControlers.put("JailControler", value10);
			Class<Controler> value11 = (Class<Controler>) Class.forName(FFA.class.getCanonicalName());
			handledControlers.put("SafeFree", value11);
			Class<Controler> value12 = (Class<Controler>) Class
					.forName(NewHomeControler.class.getCanonicalName());
			handledControlers.put("NewHomeControler", value12);
			handledControlers.put("FightPitsLobby", (Class<Controler>) Class.forName(FightPitsLobby.class.getCanonicalName()));
			handledControlers.put("FightPitsArena", (Class<Controler>) Class.forName(FightPitsArena.class.getCanonicalName()));
			handledControlers.put("StealingCreationsGame", (Class<Controler>) Class.forName(StealingCreationGame.class.getCanonicalName()));
			handledControlers.put("StealingCreationsLobby", (Class<Controler>) Class.forName(StealingCreationLobby.class.getCanonicalName()));
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static final void reload() {
		handledControlers.clear();
		init();
	}
	
	public static final Controler getControler(Object key) {
		Class<Controler> classC = handledControlers.get(key);
		if(classC == null)
			return null;
		try {
			return classC.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
