package com.rs.game.player.content;

import java.util.concurrent.TimeUnit;

import java.util.TimerTask;
import java.util.Date;
import com.rs.cores.CoresManager;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.utils.Utils;

public final class ShootingStars {

public static boolean CRASHEDSTAR = false;
public static boolean EARLY_BIRD_XP = false;

public WorldTile getWorldTile(int mapX, int mapY) {
	return new WorldTile(mapX, mapY, 0);
}

private static final WorldTile[] LOCATION =  { //Credit to Joshreynolds123 of Rune-server for getting these spots.
	new WorldTile(2941, 3279, 0),//crafting guild mining area
	new WorldTile(3027, 3350, 0),//behind falador east bank
	new WorldTile(2974, 3237, 0),//rimmington mining site
	new WorldTile(2925, 3339, 0),//falador mining site
	new WorldTile(2736, 3223, 0),//karamja north-western mining site(horseshoe mine)
	new WorldTile(2742, 3144, 0),//brimhaven mining site
	new WorldTile(2822, 3239, 0),//south crandor mining site
	new WorldTile(2846, 3037, 0),//karamja mine central
	new WorldTile(2825, 2997, 0),//shilo village northwestern mine
	new WorldTile(2727, 3683, 0),//keldagrim entrance
	new WorldTile(2393, 3814, 0),//jatizso mine
	new WorldTile(2138, 3939, 0),//miscellania
	new WorldTile(2378, 3833, 0),//central fremennik isles mining site (runite mine) //more to do
	new WorldTile(2705, 3334, 0),//legends guild mining site
	new WorldTile(2605, 3229, 0),//monastery
	new WorldTile(2588, 3479, 0),//coal truck coal mines
	new WorldTile(2605, 3088, 0),//yanille bank
	new WorldTile(2628, 3134, 0),//fight arena south
	new WorldTile(3285, 3182, 0),//alkharid warriors palace
	new WorldTile(3297, 3297, 0),//alkharid scorpion mine
	new WorldTile(3340, 3268, 0),//duel arena
	new WorldTile(3457, 3139, 0),//ruins of uzer mining site
	new WorldTile(3319, 2871, 0),//vultures small mine
	new WorldTile(3431, 2885, 0),//nardah bank area
	new WorldTile(3170, 2912, 0),//sandstone and granite quarry
	new WorldTile(3293, 3353, 0),//south east varrock mining site
	new WorldTile(3234, 3157, 0),//lumbridge swamp mining site
	new WorldTile(3170, 3364, 0),//south-west varrock mining site (champions guild)
	new WorldTile(3258, 3409, 0),//arburys rune-shop/behind varrock small bank
	new WorldTile(3502, 3218, 0),//burgh de rott
	new WorldTile(3508, 3486, 0),//canifis bank
	new WorldTile(3690, 2970, 0),//mos le'harmless bank
	new WorldTile(2540, 3439, 0),//tree gnome stronghold
	new WorldTile(2329, 3163, 0),//lleyta
	new WorldTile(2336, 3634, 0),//piscatoris mining colony
	new WorldTile(3109, 3566, 0),//low level wilderness mining site
	new WorldTile(3024, 3599, 0),//medium level wilderness mining site
	new WorldTile(3190, 3706, 0),//graveyard of shadows - wilderness
	new WorldTile(3037, 3799, 0),//level 46 south lava maze wilderness
	new WorldTile(3061, 3888, 0),//lava maze north mining site
	new WorldTile(3048, 3949, 0),//pirates hideout lvl 61 wilderness
	new WorldTile(3086, 3964, 0)//magic arena wilderness
	};
	
private static final int[] STAR_SIZE = {
		38660, 
		38661, 
		38662, 
		38663,
		38664, 
		38665, 
		38666, 
		38667,
		38668
};

private static enum CrashedStar { //The stars wouldn't spawn using the normal method, so I took this concept from the Living Rock Caverns.
	STAR(new WorldObject(STAR_SIZE[Utils.random(STAR_SIZE.length)], 10, 0, LOCATION[Utils.random(LOCATION.length)]));
	
	private CrashedStar(WorldObject star) {
		this.star = star;
	}
	
	private WorldObject star;
	
}

private ShootingStars() {
	
}

private static void respawnStar(final CrashedStar star) {
	World.spawnObject(star.star, false);
	CoresManager.slowExecutor.schedule(new Runnable() {

		@Override
		public void run() {
			removeStar(star);
		}
	}, 30, TimeUnit.MINUTES);
}

private static void removeStar(final CrashedStar star) {
	World.destroySpawnedObject(star.star, false);
	CoresManager.slowExecutor.schedule(new Runnable() {
		@Override
		public void run() {
			respawnStar(star);
		}
		
	}, 90, TimeUnit.MINUTES);
}

public static void init() {
	for(CrashedStar star : CrashedStar.values())
		respawnStar(star);
}
	
public static void ShootingStar() {
	
				CoresManager.fastExecutor1.schedule(new TimerTask() {
					int timer = 7200;
				
				@Override
				public void run() {
					if (timer <= 600) {
					if (Utils.getRandom(200) == 1) {
					if (CRASHEDSTAR == false) {
						World.sendWorldMessage("<img=5><col=ff6600>News: A shooting star has crashed!", false);
						CRASHEDSTAR = true;
						init();
							}
						}
					}
					if (timer == 1) {
					timer = 7200;
					CRASHEDSTAR = false;
					EARLY_BIRD_XP = false;
					}
					if (timer > 0) {
						timer--;
					}
				}
			}, 0L, 1000L);
		}
	}