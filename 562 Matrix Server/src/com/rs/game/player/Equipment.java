package com.rs.game.player;

import java.io.Serializable;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;

public final class Equipment implements Serializable {

	private static final long serialVersionUID = -4147163237095647617L;
	
	public static final byte SLOT_HAT = 0, SLOT_CAPE = 1, SLOT_AMULET = 2,
	SLOT_WEAPON = 3, SLOT_CHEST = 4, SLOT_SHIELD = 5, SLOT_LEGS = 7,
	SLOT_HANDS = 9, SLOT_FEET = 10, SLOT_RING = 12, SLOT_ARROWS = 13;
	
	public static String[] EQUIPMENT_SLOTS = { "Hat", "Cape", "Amulet", "Weapon",
			"Chest", "Shield", "Legs", "Hands", "Feet", "Ring", "Arrows" };

	public static final int SIZE = 14;
	
	private static String[] CAPES = { "druidic cloak", "cape", "Cape",
	"Ava's accumulator" };
	private static String[] HATS = { "druidic wreath", "halo", "Red headband", "Royal",
	"crown", "sallet","Santa hat", "helm", "hood", "coif", "Coif", "partyhat",
	"Hat", "full helm (t)", "full helm (g)", "hat (t)", "hat (g)",
	"cav", "boater", "helmet", "mask", "Helm of neitiznot", "Robin hood hat" };
	private static String[] BOOTS = { "boots", "Boots", "shoes", "Shoes" };
	private static String[] GLOVES = { "gloves", "gauntlets", "Gloves",
	"vambraces", "vamb", "bracers" };
	private static String[] SHIELDS = { "kiteshield", "sq shield", "Toktz-ket",
	"books", "book", "kiteshield (t)", "kiteshield (g)",
	"kiteshield(h)", "defender", "shield" };
	private static String[] AMULETS = { "amulet", "necklace", "Amulet of",
	"scarf" };
	private static String[] ARROWS = { "arrow", "arrows", "arrow(p)",
	"arrow(+)", "arrow(s)", "bolt", "Bolt rack", "Opal bolts",
	"Dragon bolts", "bolts (e)", "bolts", "Hand cannon shot" };
	private static String[] RINGS = { "ring", "Ring" };
	private static String[] BODY = { "armour", "hauberk","Dragon chainbody", "platebody",
	"chainbody", "robetop", "leathertop", "platemail", "top", "Shirt",
	"brassard", "Robe top", "body", "platebody (t)", "platebody (g)",
	"body(g)", "body_(g)", "chestplate", "torso", "Woven top", "shirt", "Bandos chestplate",
	"Zamorak d'hide"};
	private static String[] LEGS = { "druidic robe", "cuisse", "pants",
	"platelegs", "plateskirt", "skirt", "bottoms", "chaps",
	"platelegs (t)", "platelegs (g)", "bottom", "skirt", "skirt (g)",
	"skirt (t)", "chaps (g)", "chaps (t)", "tassets", "legs",
	"trousers", "robe bottom", "Void knight robe", "Shorts"};
	private static String[] WEAPONS = {"rapier", "hatchet", "bow", "Hand cannon",
	"Inferno adze", "Silverlight", "Darklight", "wand",
	"Statius's warhammer", "anchor", "spear.", "Vesta's longsword.",
	"scimitar", "longsword", "sword", "longbow", "shortbow", "dagger",
	"mace", "halberd", "spear", "Abyssal whip", "Abyssal vine whip", "Ornate katana", "axe", "flail",
	"crossbow", "Torags hammers", "dagger(p)","dagger (p++)", "dagger(+)",
	"dagger(s)", "spear(p)", "spear(+)", "spear(s)", "spear(kp)",
	"maul", "dart", "dart(p)", "javelin", "javelin(p)", "knife",
	"knife(p)", "Longbow", "Shortbow", "Crossbow", "Toktz-xil",
	"Toktz-mej", "Tzhaar-ket", "staff", "Staff", "godsword", "c'bow",
	"Crystal bow", "Dark bow", "claws", "warhammer", "adze", "hand" };
	private static String[] FULL_BODY = { "armour", "hauberk", "top", "shirt",
	"platebody", "Ahrims robetop", "Karils leathertop", "brassard",
	"Robe top", "robetop", "platebody (t)","Rune platebody", "platebody (g)",
	"chestplate", "torso", "Morrigan's", "leather body", "chainbody",
	"Zuriel's", "robe top", "Void knight top", "Zamorak d'hide", "Monk's robe" };
	private static String[] FULL_HAT = { "sallet", "Attack hood", "med helm", "coif",
	"Dharoks helm","Initiate helm", "Coif",
	"Helm of neitiznot", "hood", "Void melee helm", "Void mage helm", "Void range helm" };
	private static String[] FULL_MASK = { "sallet", "full helm", "mask",
	"Verac's helm", "Guthan's helm", "Torag's helm", "Karil's coif",
	"full helm (t)", "full helm (g)", "mask" };
	
	private ItemsContainer<Item> items;
	
	private transient Player player;
	private transient int equipmentHpIncrease;
	
	public Equipment() {
		items = new ItemsContainer<Item>(15, false);
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void init() {
		player.getPackets().sendItems(387, 29, 94, items);
		refresh(null);
	}
	
	public void refresh(int... slots) {
		if(slots != null) {
			player.getPackets().sendUpdateItems(387, 29, 94, items, slots);
			player.getCombatDefinitions().checkAttackStyle();
		}
		player.getCombatDefinitions().refreshBonuses();
		refreshEquipmentHp(slots == null);
	}
	
	public void reset() {
		items.reset();
		init();
	}
	
	public Item getItem(int slot) {
		return items.get(slot);
	}
	
	public void refreshEquipmentHp(boolean init) {
		double hpIncrease = 0;
		for(int index = 0; index < items.getSize(); index++) {
			Item item = items.get(index);
			if (item == null)
				continue;
			int id = item.getId();
			if(index == Equipment.SLOT_HAT) {
			if(id == 20135 || id == 20137 //torva
					|| id == 20147 || id == 20149 //pernix
					|| id == 20159 || id == 20161 //virtus
					)
				hpIncrease += 66;
			}else if (index == Equipment.SLOT_CHEST) {
				if(id == 20139 || id == 20141 //torva
						|| id == 20151 || id == 20153 //pernix
						|| id == 20163 || id == 20165 //virtus
						)
					hpIncrease += 200;	
			}else if (index == Equipment.SLOT_LEGS) {
				if(id == 20143 || id == 20145 //torva
						|| id == 20155 || id == 20157 //pernix
						|| id == 20167 || id == 20169 //virtus
						)
					hpIncrease += 134;	
			}
			
		}
		if (hpIncrease != equipmentHpIncrease) {
			equipmentHpIncrease = (int) hpIncrease;
			if(!init)
				player.refreshHitPoints();
		}
	}
	
	public static boolean isFullBody(Item item) {
		String itemName = item.getDefinitions().getName();
		if(itemName == null)
			return false;
		for (int i = 0; i < FULL_BODY.length; i++)
			if (itemName.contains(FULL_BODY[i]))
				return true;
		return false;
	}
	
	public static boolean isFullHat(Item item) {
		String itemName = item.getDefinitions().getName();
		if(itemName == null)
			return false;
		for (int i = 0; i < FULL_HAT.length; i++) {
			if (itemName.contains(FULL_HAT[i])) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isFullMask(Item item) {
		String itemName = item.getDefinitions().getName();
		if(itemName == null)
			return false;
		for (int i = 0; i < FULL_MASK.length; i++)
			if (itemName.contains(FULL_MASK[i]))
				return true;
		return false;
	}
	
	public static int getItemSlot(int itemId) {
		String item = ItemDefinitions.getItemDefinitions(itemId).getName();
		if(item == null)
			return -1;
		for (int i = 0; i < CAPES.length; i++)
			if (item.contains(CAPES[i]))
				return 1;
		for (int i = 0; i < HATS.length; i++)
			if (item.contains(HATS[i]))
				return 0;
		for (int i = 0; i < BOOTS.length; i++)
			if (item.endsWith(BOOTS[i]) || item.startsWith(BOOTS[i]))
				return 10;
		for (int i = 0; i < GLOVES.length; i++)
			if (item.endsWith(GLOVES[i]) || item.startsWith(GLOVES[i]))
				return 9;
		for (int i = 0; i < SHIELDS.length; i++)
			if (item.contains(SHIELDS[i]))
				return 5;
		for (int i = 0; i < AMULETS.length; i++)
			if (item.endsWith(AMULETS[i]) || item.startsWith(AMULETS[i]))
				return 2;
		for (int i = 0; i < ARROWS.length; i++)
			if (item.endsWith(ARROWS[i]) || item.startsWith(ARROWS[i]))
				return 13;
		for (int i = 0; i < RINGS.length; i++)
			if (item.endsWith(RINGS[i]) || item.startsWith(RINGS[i]))
				return 12;
		for (int i = 0; i < BODY.length; i++)
			if (item.contains(BODY[i]))
				return 4;
		for (int i = 0; i < LEGS.length; i++)
			if (item.contains(LEGS[i]))
				return 7;
		for (int i = 0; i < WEAPONS.length; i++)
			if (item.endsWith(WEAPONS[i]) || item.startsWith(WEAPONS[i]))
				return 3;
		return -1;
}
	
	public boolean hasTwoHandedWeapon() {
		Item item = items.get(SLOT_WEAPON);
		if(item == null)
			return false;
		return isTwoHandedWeapon(item);
	}
	public static boolean isTwoHandedWeapon(Item item) {
		int itemId = item.getId();
		if (itemId == 4212)
			return true;
		else if (itemId == 4214)
			return true;
		String wepEquiped = item.getDefinitions().getName();
		if(wepEquiped == null)
			return false;
		else if (wepEquiped.endsWith("claws"))
			return true;
		else if (wepEquiped.endsWith("anchor"))
			return true;
		else if (wepEquiped.endsWith("2h sword"))
			return true;
		else if (wepEquiped.endsWith("longbow"))
			return true;
		else if (wepEquiped.equals("Seercull"))
			return true;
		else if (wepEquiped.endsWith("shortbow"))
			return true;
		else if (wepEquiped.endsWith("Longbow"))
			return true;
		else if (wepEquiped.endsWith("Shortbow"))
			return true;
		else if (wepEquiped.endsWith("bow full"))
			return true;
		else if (wepEquiped.equals("Dark bow"))
			return true;
		else if (wepEquiped.endsWith("halberd"))
			return true;
		else if (wepEquiped.equals("Granite maul"))
			return true;
		else if (wepEquiped.equals("Karils crossbow"))
			return true;
		else if (wepEquiped.equals("Torags hammers"))
			return true;
		else if (wepEquiped.equals("Veracs flail"))
			return true;
		else if (wepEquiped.equals("Dharoks greataxe"))
			return true;
		else if (wepEquiped.equals("Guthans warspear"))
			return true;
		else if (wepEquiped.equals("Tzhaar-ket-om"))
			return true;
		else if (wepEquiped.endsWith("godsword"))
			return true;
		else if (wepEquiped.equals("Saradomin sword"))
			return true;
		return false;
	}
	
	public int getWeaponRenderEmote() {
		  if (items.get(3) == null) {
		   return 1426;
		  }
		  Item item = items.get(3);
		  int id = item.getId();
		  switch(id) {
		   case -1:
		   	return 1426;
		   default:
		   	return item.getDefinitions().getRenderAnimId();
		   case 837://175's
		   case 767:
		    	return 175;
		 
		   case 839://176's
		   case 841:
		   case 13719:
		    	return 176;
		 
		   case 12845://370's
		   case 12846:
		   case 12847:
		   case 12848:
		   case 12849:
		    	return 370;
		 
		   case 1307://124's
		   case 1309:
		   case 1311:
		   case 1313:
		   case 1315:
		   case 1317:
		   case 1319:
		    	return 124;
		 
		   case 14152://158's
		   case 14154:
		   case 14156:
		   case 14158:
		   case 14160:
		   case 14102:
		   case 14110:
		    	return 158;
		 
		   case 1335://1430's
		   case 1337:
		   case 1339:
		   case 1341:
		   case 1343:
		   case 1345:
		   case 1347:
		    	return 1430;
		 
		   case 1387://1272's
		    	return 1272;
		 
		   case 1419://1271's
		    	return 1271;
		 
		   case 4037://131's
		   case 4039:
		    	return 131;
		 
		   case 4084://1119's
		    	return 1119;
		 
		   case 4151://1578's
		    	return 1578;
		 
		   case 4886://1580's
		   case 4887:
		   case 4888:
		   case 4718:
		   case 4889:
		   case 4890:
		    	return 1580;
		 
		   case 4934://372's
		   case 4935:
		   case 4936:
		   case 4937:
		   case 4938:
		   case 4734:
		    	return 372;
		 
		   case 4982://373's
		   case 4983:
		   case 4984:
		   case 4985:
		   case 4986:
		   case 4755:
		    	return 373;
		 
		   case 5614:
		    	return 191;
		 
		   case 13117://712's
		   case 13118:
		   case 13119:
		   case 13120:
		   case 13121:
		   case 13122:
		   case 13123:
		   case 13124:
		   case 13125:
		   case 13126:
		   case 13127:
		   case 13128:
		   case 13129:
		   case 13130:
		   case 13131:
		   case 13132:
		   case 13133:
		   case 13134:
		   case 13135:
		   case 13136:
		   case 13137:
		   case 13138:
		   case 13139:
		   case 13140:
		   case 13141:
		   case 13142:
		   case 13143:
		   case 13144:
		   case 13145:
		   case 13146:
		    	return 712;
		 
		   case 5704://28's
		   case 5706:
		   case 5708:
		   case 5710:
		   case 5712:
		   case 7639:
		   case 1237:
		   case 1239:
		   case 1241:
		   case 1243:
		   case 1245:
		   case 1251:
		   case 1253:
		   case 1255:
		   case 1257:
		   case 772:
		   case 1259:
		   case 7640:
		   case 7641:
		   case 7642:
		   case 7643:
		   case 7644:
		   case 7645:
		   case 7646:
		   case 7647:
		   case 3053:
		   case 3054:
		   case 3170:
		   case 3171:
		   case 3172:
		   case 1379:
		   case 1381:
		   case 1383:
		   case 1385:
		   case 3173:
		   case 3174:
		   case 3175:
		   case 3176:
		   case 3190:
		   case 14377:
		   case 14379:
		   case 14381:
		   case 14383:
		   case 14385:
		   case 3192:
		   case 3194:
		   case 3196:
		   case 3198:
		   case 3200:
		   case 3202:
		   case 7648:
		   case 5718:
		   case 5720:
		   case 5722:
		   case 5724:
		   case 5726:
		   case 1389:
		   case 1391:
		   case 1393:
		   case 1395:
		   case 1397:
		   case 1399:
		   case 1401:
		   case 1403:
		   case 1405:
		   case 1407:
		   case 1409:
		   case 5734:
		   case 5736:
		   case 5016:
		   case 6526:
		   case 6562:
		   case 6563:
		   case 3204:
		   case 13988:
		   case 13990:
		   case 14117:
		   case 13905:
		   case 13907:
		   case 13929:
		   case 13931:
		   case 13941:
		   case 13943:
		   case 14725:
		   case 13629:
		   case 10280:
		   case 10282:
		   case 10284:
		   case 13630:
		   case 13631:
		   case 13632:
		   case 13633:
		   case 13634:
		   case 13635:
		   case 13636:
		   case 13637:
		   case 13638:
		   case 13639:
		   case 13640:
		   case 13641:
		   case 13642:
		   case 13643:
		   case 13644:
		   case 13645:
		   case 13646:
		   case 13647:
		   case 13648:
		   case 1247:
		   case 1249:
		   case 1261:
		   case 1263:
		   case 5714:
		   case 5716:
		   case 5728:
		   case 5730:
		   case 13867:
		   case 13869:
		   case 6599:
		   case 6603:
		   case 4675:
		   case 4710:
		   case 6760:
		   case 6762:
		   case 6764:
		   case 6818:
		   case 6908:
		   case 6910:
		   case 6912:
		   case 6914:
		   case 4910:
		   case 4911:
		   case 4912:
		   case 4913:
		   case 10440:
		   case 10442:
		   case 10444:
		   case 4914:
		   case 4862:
		   case 4863:
		   case 4864:
		   case 4726:
		   case 4865:
		   case 4866:
		   case 4158:
		   case 4159:
		   case 4170:
		   case 4580:
		   case 9013:
		   case 9044:
		   case 9046:
		   case 9048:
		   case 11367:
		   case 11369:
		   case 11371:
		   case 11373:
		   case 11375:
		   case 11377:
		   case 11379:
		   case 11381:
		   case 11382:
		   case 11384:
		   case 11386:
		   case 11388:
		   case 11389:
		   case 11391:
		   case 11393:
		   case 11395:
		   case 11396:
		   case 11398:
		   case 11400:
		   case 11736:
		   case 11738:
		   case 11402:
		   case 11403:
		   case 11405:
		   case 11407:
		   case 11409:
		   case 11410:
		   case 11412:
		   case 11414:
		   case 11416:
		   case 11417:
		   case 11419:
		   case 9050:
		   case 10010:
		   case 9084:
		   case 9091:
		   case 9092:
		   case 9093:
		   case 4582:
		   case 7804:
		   case 7809:
		   case 4584:
		    	return 28;
		 
		   case 6082://284's
		    	return 284;
		 
		   case 6528://27's
		   case 4153:
		   case 7668:
		    	return 27;
		 
		   case 6605://1381's
		   case 1277:
		   case 1279:
		   case 1281:
		   case 1289:
		   case 1283:
		   case 1285:
		   case 1287:
		   case 11716:
		   case 13879:
		   case 13880:
		   case 13881:
		   case 13882:
		    	return 1381;
		 
		   case 7671://1386's
		   case 7673:
		    	return 1386;
		 
		   case 6607://1629's
		   case 6611:
		    	return 1629;
		 
		   case 6609://124's
		   case 6817:
		   case 7158:
		   case 7439:
		    	return 124;
		 
		   case 6773://131's
		   case 6774:
		   case 6775:
		   case 6776:
		   case 6777:
		   case 6778:
		   case 6779:
		   case 8650:
		   case 8652:
		   case 8654:
		   case 8656:
		   case 8658:
		   case 8660:
		   case 8662:
		   case 8664:
		   case 8666:
		   case 8668:
		   case 8670:
		   case 8672:
		   case 8674:
		   case 8676:
		   case 8678:
		   case 8680:
		   case 8966:
		   case 8967:
		   case 8968:
		   case 8969:
		   case 8970:
		   case 8971:
		    	return 131;
		 
		   case 7170:
		    	return 234;
		 
		   case 7421://618's
		   case 7422:
		   case 7423:
		   case 7424:
		   case 7425:
		   case 7426:
		   case 7427:
		   case 7428:
		   case 7429:
		   case 7430:
		   case 7431:
		    	return 618;
		 
		   case 7449://134's
		    	return 134;
		 
		   case 8871://822's
		    	return 822;
		 
		   case 8880://175's
		   case 9174:
		   case 9176:
		   case 9177:
		   case 9179:
		   case 9181:
		   case 9183:
		   case 10156:
		   case 13081:
		   case 11165:
		   case 11167:
		   case 9185:
		    	return 175;
		 
		   case 1303://1582's
		   case 1305:
		   case 4587:
		   case 1333:
		   case 1321://1629's
		   case 1323:
		   case 1325:
		   case 1327:
		   case 1329:
		   case 1331:
		   case 1291:
		   case 1293:
		   case 1295:
		   case 1297:
		   case 1299:
		   case 1301:
		   case 13979:
		   case 13981:
		   case 13982:
		   case 13984:
		    	return 1582;
		 
		   case 10024:
		    	return 1283;
		 
		   case 10033://234's
		   case 10034:
		    	return 234;
		 
		   case 10146://1277's
		   case 10147:
		   case 10148:
		   case 10149:
		    	return 1277;
		 
		   case 12578://294's
		   case 12579:
		   case 12580:
		    	return 294;
		 
		   case 3695://792's
		    	return 792;
		 
		   case 11259://158's
		    	return 158;
		 
		   case 11694://1579's
		   case 11696:
		   case 11698:
		   case 11700:
		   case 11730:
		    	return 1579;
		 
		   case 10150://1279's
		    	return 1279;
		 
		   case 10487://1171's
		    	return 1171;
		 
		   case 10735://1383's
		    	return 1383;
		 
		   case 10858://124's
		    	return 124;
		 
		   case 10887://985's
		    	return 985;
		 
		   case 11235://303's
		   	return 303;
		 
		   case 667://292's
		    	return 292;
		 
		   case 11998://159's
		    	return 159;
		 
		   case 12570://1157's
		    	return 1157;
		 
		   case 12844://593's
		    	return 593;
		 
		   case 13661://1096's
		    	return 1096;
		 
		   case 13666://326's
		   	return 326;
		 
		   case 13671://327's
		    	return 327;
		 
		   case 13676://328's
		    	return 328;
		 
		   case 14057://1072's
		    	return 1072;
		 
		   case 14679://1481's
		   	return 1481;
		 
		   case 14712://398's
		    	return 398;
		 
		   case 14713://182's
		    	return 182;
		 
		   case 15241://1603
		    	return 1603;
			}
		}
	
	public boolean hasShield() {
		return items.get(5) != null;
	}
	
	public int getWeaponId() {
		Item item = items.get(SLOT_WEAPON);
		if(item == null)
			return -1;
		return item.getId();
	}
	
	public int getChestId() {
		Item item = items.get(SLOT_CHEST);
		if(item == null)
			return -1;
		return item.getId();
	}
	
	public int getHatId() {
		Item item = items.get(SLOT_HAT);
		if(item == null)
			return -1;
		return item.getId();
	}
	public int getShieldId() {
		Item item = items.get(SLOT_SHIELD);
		if(item == null)
			return -1;
		return item.getId();
	}
	
	
	public int getLegsId() {
		Item item = items.get(SLOT_LEGS);
		if(item == null)
			return -1;
		return item.getId();
	}
	
	public void removeAmmo(int ammoId, int ammount) {
		if(ammount == -1) {
			items.remove(SLOT_WEAPON, new Item(ammoId, 1));
			refresh(SLOT_WEAPON);
		}else{
			items.remove(SLOT_ARROWS, new Item(ammoId, ammount));
			refresh(SLOT_ARROWS);
		}
	}
	
	public int getCapeId() {
		Item item = items.get(SLOT_CAPE);
		if(item == null)
			return -1;
		return item.getId();
	}
	
	public int getAmmoId() {
		Item item = items.get(SLOT_ARROWS);
		if(item == null)
			return -1;
		return item.getId();
	}
	
	public int getBootsId() {
		Item item = items.get(SLOT_FEET);
		if(item == null)
			return -1;
		return item.getId();
	}
	
	public ItemsContainer<Item> getItems() {
		return items;
	}
	
	public int getEquipmentHpIncrease() {
		return equipmentHpIncrease;
	}


	public int getGlovesId() {
		Item item = items.get(SLOT_HANDS);
		if (item == null)
			return -1;
		return item.getId();
	}
	public static boolean hideHair(Item item) {
		return item.getDefinitions().getEquipType() == 8;
	}

	public static boolean showBear(Item item) { 
		String name = item.getName().toLowerCase();
		return !hideHair(item)
				|| name.contains("horns")
				|| name.contains("hat")
				|| name.contains("afro")
				|| name.contains("cowl")
				|| name.contains("tattoo")
				|| name.contains("headdress")
				|| name.contains("hood")
				|| (name.contains("mask") && !name.contains("h'ween"))
				|| (name.contains("helm") && !name.contains("full"));
	}

	public void deleteItem(int itemId, int amount) {
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(new Item(itemId, amount));
		refreshItems(itemsBefore);
	}

	public void refreshItems(Item[] itemsBefore) {
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			if (itemsBefore[index] != items.getItems()[index])
				changedSlots[count++] = index;
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
	}

	public int getRingId() {
		Item item = items.get(SLOT_RING);
		if (item == null)
			return -1;
		return item.getId();
	}
	
	public boolean wearingArmour() {
		return getItem(SLOT_HAT) != null || getItem(SLOT_CAPE) != null
				|| getItem(SLOT_AMULET) != null || getItem(SLOT_WEAPON) != null
				|| getItem(SLOT_CHEST) != null || getItem(SLOT_SHIELD) != null
				|| getItem(SLOT_LEGS) != null || getItem(SLOT_HANDS) != null
				|| getItem(SLOT_FEET) != null;
	}

	public int getAmuletId() {
		Item item = items.get(SLOT_AMULET);
		if (item == null)
			return -1;
		return item.getId();
	}

	public static boolean isCape(Item item) {
		String wepEquiped = item.getDefinitions().getName().toLowerCase();
		if (wepEquiped == null)
			return false;
		else if (wepEquiped.contains("cape"))
			return true;
		else if (wepEquiped.contains("cloak"))
			return true;
		else if (wepEquiped.contains("ava"))
			return true;
		return false;
	}
}