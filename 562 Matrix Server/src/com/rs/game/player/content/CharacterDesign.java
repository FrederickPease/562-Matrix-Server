package com.rs.game.player.content;

import com.rs.game.player.Player;



/**
 * 
 * @author 20% Mystic Flow, 20% Emily, 60% RuneUnited
 */
public final class CharacterDesign {

	public static final int INTERFACE = 771;

	public enum Gender {
		MALE, FEMALE;
	}

	public static void refresh(Player player) {
		player.getAppearence().generateAppearenceData();
	}

	public static void handle(Player player, int button) {
		switch (button) {

		case 380: // male
			change(player, Gender.MALE);
			break;
		case 383: // female
			change(player, Gender.FEMALE);
			break;

		case 160:
			player.getAppearence().colour[2] = 1; // Khaki
			refresh(player);
			break;
		case 177:
			player.getAppearence().colour[2] = 2; // Dark Grey
			refresh(player);
			break;
		case 153:
			player.getAppearence().colour[2] = 3; // Dark Red
			refresh(player);
			break;
		case 173:
			player.getAppearence().colour[2] = 4; // Navy Blue
			refresh(player);
			break;
		case 159:
			player.getAppearence().colour[2] = 5; // Light Kyaki
			refresh(player);
			break;
		case 174:
			player.getAppearence().colour[2] = 6; // Beige
			refresh(player);
			break;
		case 157:
			player.getAppearence().colour[2] = 7; // Burnt Orange
			refresh(player);
			break;
		case 167:
			player.getAppearence().colour[2] = 8; // Dark Blue
			refresh(player);
			break;
		case 164:
			player.getAppearence().colour[2] = 9; // Sea Green
			refresh(player);
			break;
		case 161:
			player.getAppearence().colour[2] = 10; // Gold
			refresh(player);
			break;
		case 170:
			player.getAppearence().colour[2] = 11; // Mauve
			refresh(player);
			break;
		case 156:
			player.getAppearence().colour[2] = 12; // Orange
			refresh(player);
			break;
		case 155:
			player.getAppearence().colour[2] = 13; // Dark Pink
			refresh(player);
			break;
		case 162:
			player.getAppearence().colour[2] = 14; // Moss Green
			refresh(player);
			break;
		case 169:
			player.getAppearence().colour[2] = 15; // Pale Blue
			refresh(player);
			break;
		case 178:
			player.getAppearence().colour[2] = 16; // Black
			refresh(player);
			break;
		case 176:
			player.getAppearence().colour[2] = 17; // Taupe
			refresh(player);
			break;
		case 175:
			player.getAppearence().colour[2] = 18; // Light Grey
			refresh(player);
			break;
		case 158:
			player.getAppearence().colour[2] = 19; // Peach
			refresh(player);
			break;
		case 168:
			player.getAppearence().colour[2] = 20; // Light Blue
			refresh(player);
			break;
		case 166:
			player.getAppearence().colour[2] = 21; // Royal Blue
			refresh(player);
			break;
		case 154:
			player.getAppearence().colour[2] = 22; // Pink
			refresh(player);
			break;
		case 152:
			player.getAppearence().colour[2] = 23; // Red
			refresh(player);
			break;
		case 151:
			player.getAppearence().colour[2] = 24; // Burgundy
			refresh(player);
			break;
		case 163:
			player.getAppearence().colour[2] = 25; // Mint Green
			refresh(player);
			break;
		case 150:
			player.getAppearence().colour[2] = 26; // Dark Green
			refresh(player);
			break;
		case 172:
			player.getAppearence().colour[2] = 27; // Indigo
			refresh(player);
			break;
		case 171:
			player.getAppearence().colour[2] = 28; // Violet
			refresh(player);
			break;
		case 165:
			player.getAppearence().colour[2] = 29; // Forest Green
			refresh(player);
			break;
		case 217:
			player.getAppearence().colour[1] = 1; // Khaki
			refresh(player);
			break;
		case 234:
			player.getAppearence().colour[1] = 1; // Dark Grey (E)
			refresh(player);
			break;
		case 210:
			player.getAppearence().colour[1] = 2; // Dark Red (E)
			refresh(player);
			break;
		case 230:
			player.getAppearence().colour[1] = 3; // Navy Blue (E)
			refresh(player);
			break;
		case 216:
			player.getAppearence().colour[1] = 4; // Light Kyaki (E)
			refresh(player);
			break;
		case 231:
			player.getAppearence().colour[1] = 5; // Beige (E)]
			refresh(player);
			break;
		case 214:
			player.getAppearence().colour[1] = 6; // Burnt Orange (E)
			refresh(player);
			break;
		case 224:
			player.getAppearence().colour[1] = 7; // Dark Blue (E)
			refresh(player);
			break;
		case 221:
			player.getAppearence().colour[1] = 8; // Sea Green (E)
			refresh(player);
			break;
		case 218:
			player.getAppearence().colour[1] = 9; // Gold (E)
			refresh(player);
			break;
		case 228:
			player.getAppearence().colour[1] = 10; // Mauve (E)
			refresh(player);
			break;
		case 213:
			player.getAppearence().colour[1] = 11; // Orange (E)
			refresh(player);
			break;
		case 212:
			player.getAppearence().colour[1] = 12; // Dark Pink (E)
			refresh(player);
			break;
		case 219:
			player.getAppearence().colour[1] = 13; // Moss Green (E)
			refresh(player);
			break;
		case 227:
			player.getAppearence().colour[1] = 14; // Pale Blue (E)
			refresh(player);
			break;
		case 235:
			player.getAppearence().colour[1] = 16; // Black
			refresh(player);
			break;
		case 233:
			player.getAppearence().colour[1] = 17; // Taupe
			refresh(player);
			break;
		case 232:
			player.getAppearence().colour[1] = 18; // Light Grey
			refresh(player);
			break;
		case 215:
			player.getAppearence().colour[1] = 19; // Peach
			refresh(player);
			break;
		case 225:
			player.getAppearence().colour[1] = 20; // Light Blue
			refresh(player);
			break;
		case 265:
			player.getAppearence().colour[1] = 21; // Royal Blue
			refresh(player);
			break;
		case 211:
			player.getAppearence().colour[1] = 22; // Pink
			refresh(player);
			break;
		case 209:
			player.getAppearence().colour[1] = 23; // Red
			refresh(player);
			break;
		case 208:
			player.getAppearence().colour[1] = 24; // Burgundy
			refresh(player);
			break;
		case 220:
			player.getAppearence().colour[1] = 25; // Mint Green
			refresh(player);
			break;
		case 223:
			player.getAppearence().colour[1] = 26; // Dark Green
			refresh(player);
			break;
		case 229:
			player.getAppearence().colour[1] = 27; // Indigo
			refresh(player);
			break;
		case 226:
			player.getAppearence().colour[1] = 28; // Violet
			refresh(player);
			break;
		case 222:
			player.getAppearence().colour[1] = 15; // Forest Green (E)
			refresh(player);
			break;
		case 139:
			player.getAppearence().colour[3] = 1; // Dark Green
			refresh(player);
			break;
		case 140:
			player.getAppearence().colour[3] = 2; // Pale Gray
			refresh(player);
			break;
		case 141:
			player.getAppearence().colour[3] = 3; // Black
			refresh(player);
			break;
		case 142:
			player.getAppearence().colour[3] = 4; // Light Brown
			refresh(player);
			break;
		case 143:
			player.getAppearence().colour[3] = 5; // Grey
			refresh(player);
			break;
		case 138:
			player.getAppearence().colour[3] = 6; // Brown
			refresh(player);
			break;
		case 305:
			player.getAppearence().colour[4] = 1; // Fair
			refresh(player);
			break;
		case 306:
			player.getAppearence().colour[4] = 2; // Tanned
			refresh(player);
			break;
		case 307:
			player.getAppearence().colour[4] = 3; // Olive
			refresh(player);
			break;
		case 308:
			player.getAppearence().colour[4] = 4; // Light Brown
			refresh(player);
			break;
		case 309:
			player.getAppearence().colour[4] = 5; // Brown
			refresh(player);
			break;
		case 310:
			player.getAppearence().colour[4] = 6; // Dark Brown
			refresh(player);
			break;
		case 311:
			player.getAppearence().colour[4] = 7; // Very Pale
			refresh(player);
			break;
		case 304:
			player.getAppearence().colour[4] = 8; // Pale
			refresh(player);
			break;
		case 341:
			player.getAppearence().colour[0] = 1; // White
			refresh(player);
			break;
		case 340:
			player.getAppearence().colour[0] = 2; // Military Grey
			refresh(player);
			break;
		case 339:
			player.getAppearence().colour[0] = 3; // Dark Grey
			refresh(player);
			break;
		case 324:
			player.getAppearence().colour[0] = 4; // Orange
			refresh(player);
			break;
		case 325:
			player.getAppearence().colour[0] = 5; // Yellow
			refresh(player);
			break;
		case 329:
			player.getAppearence().colour[0] = 6; // Light Brown
			refresh(player);
			break;
		case 327:
			player.getAppearence().colour[0] = 7; // Brown
			refresh(player);
			break;
		case 334:
			player.getAppearence().colour[0] = 8; // Turquoise
			refresh(player);
			break;
		case 331:
			player.getAppearence().colour[0] = 9; // Green
			refresh(player);
			break;
		case 322:
			player.getAppearence().colour[0] = 10; // Vermillion
			refresh(player);
			break;
		case 336:
			player.getAppearence().colour[0] = 11; // Purple
			refresh(player);
			break;
		case 344:
			player.getAppearence().colour[0] = 12; // Black
			refresh(player);
			break;
		case 343:
			player.getAppearence().colour[0] = 13; // Taupe
			refresh(player);
			break;
		case 342:
			player.getAppearence().colour[0] = 14; // Light Grey
			refresh(player);
			break;
		case 326:
			player.getAppearence().colour[0] = 15; // Peach
			refresh(player);
			break;
		case 335:
			player.getAppearence().colour[0] = 16; // Cyan
			refresh(player);
			break;
		case 333:
			player.getAppearence().colour[0] = 17; // Dark Blue
			refresh(player);
			break;
		case 323:
			player.getAppearence().colour[0] = 18; // Pink
			refresh(player);
			break;
		case 320:
			player.getAppearence().colour[0] = 20; // Burgundy
			refresh(player);
			break;
		case 321:
			player.getAppearence().colour[0] = 19; // Red
			refresh(player);
			break;
		case 330:
			player.getAppearence().colour[0] = 21; // Mint Green
			refresh(player);
			break;
		case 332:
			player.getAppearence().colour[0] = 22; // Dark Green
			refresh(player);
			break;
		case 338:
			player.getAppearence().colour[0] = 23; // Indigo
			refresh(player);
			break;
		case 337:
			player.getAppearence().colour[0] = 24; // Violet
			refresh(player);
			break;
		case 328:
			player.getAppearence().colour[0] = 25; // Dark Brown
			refresh(player);
			break;

		/*
		 * Start Of Hair
		 */

		case 293: // Hair --
			player.getAppearence().look[0]--;
			if (player.getAppearence().getGender() == 0) { // Male
				if (player.getAppearence().getLooks()[0] < 0)
					player.getAppearence().getLooks()[0] = 268;
				if (player.getAppearence().getLooks()[0] < 246 && player.getAppearence().getLooks()[0] > 200)
					player.getAppearence().getLooks()[0] = 97;
				if (player.getAppearence().getLooks()[0] < 91)
					player.getAppearence().getLooks()[0] = 8;
			} else if (player.getAppearence().getGender() == 1) { // Female
				if (player.getAppearence().getLooks()[0] < 46)
					player.getAppearence().getLooks()[0] = 146;
				if (player.getAppearence().getLooks()[0] < 135 && player.getAppearence().getLooks()[0] > 55)
					player.getAppearence().getLooks()[0] = 55;
			}
			player.getAppearence().generateAppearenceData();
			break;
		case 294: // Hair ++
			player.getAppearence().look[0]++;
			if (player.getAppearence().getGender() == 0) { // Male
				if (player.getAppearence().getLooks()[0] > 8 && player.getAppearence().getLooks()[0] < 91)
					player.getAppearence().getLooks()[0] = 91;
				if (player.getAppearence().getLooks()[0] > 97 && player.getAppearence().getLooks()[0] < 246)
					player.getAppearence().getLooks()[0] = 246;
				if (player.getAppearence().getLooks()[0] > 268)
					player.getAppearence().getLooks()[0] = 0;
			} else if (player.getAppearence().getGender() == 1) { // Female
				if (player.getAppearence().getLooks()[0] > 55 && player.getAppearence().getLooks()[0] < 135)
					player.getAppearence().getLooks()[0] = 135;
				if (player.getAppearence().getLooks()[0] > 146 && player.getAppearence().getLooks()[0] < 246)
					player.getAppearence().getLooks()[0] = 46;
			}
			player.getAppearence().generateAppearenceData();
			break;

		/*
		 * Start Of Beard
		 */

		case 372: // Beard --
			player.getAppearence().look[1]--;
			if (player.getAppearence().getGender() == 0) { // Male
				if (player.getAppearence().getLooks()[1] < 10)
					player.getAppearence().getLooks()[1] = 17;
			} else {
			}
			player.getAppearence().generateAppearenceData();
			break;
		case 373: // Beard ++
			player.getAppearence().look[1]++;
			if (player.getAppearence().getGender() == 0) { // Male
				if (player.getAppearence().getLooks()[1] > 17)
					player.getAppearence().getLooks()[1] = 10;
			} else {
			}
			player.getAppearence().generateAppearenceData();
			break;

		/*
		 * Start Of Torso
		 */

		case 106: // Torso ++
			player.getAppearence().look[2]++;
			if (player.getAppearence().getGender() == 0) { // Male
				if (player.getAppearence().getLooks()[2] > 25 && player.getAppearence().getLooks()[2] < 111)
					player.getAppearence().getLooks()[2] = 111;
				if (player.getAppearence().getLooks()[2] > 116 && player.getAppearence().getLooks()[2] < 300)
					player.getAppearence().getLooks()[2] = 18;
			} else if (player.getAppearence().gender == 1) { // Female
				if (player.getAppearence().getLooks()[2] > 60 && player.getAppearence().getLooks()[2] < 153)
					player.getAppearence().getLooks()[2] = 153;
				if (player.getAppearence().getLooks()[2] > 158)
					player.getAppearence().getLooks()[2] = 57;
			}
			player.getAppearence().generateAppearenceData();
			break;
		case 105: // Torso --
			player.getAppearence().look[2]--;
			if (player.getAppearence().getGender() == 0) { // Male
				if (player.getAppearence().getLooks()[2] < 18 && player.getAppearence().getLooks()[2] > 1)
					player.getAppearence().getLooks()[2] = 116;
				if (player.getAppearence().getLooks()[2] < 111 && player.getAppearence().getLooks()[2] > 100)
					player.getAppearence().getLooks()[2] = 25;
			} else if (player.getAppearence().gender == 1) { // Female
				if (player.getAppearence().getLooks()[2] < 57 && player.getAppearence().getLooks()[2] > 0)
					player.getAppearence().getLooks()[2] = 158;
				if (player.getAppearence().getLooks()[2] < 153 && player.getAppearence().getLooks()[2] > 60)
					player.getAppearence().getLooks()[2] = 60;
			}
			player.getAppearence().generateAppearenceData();
			break;

		/*
		 * Start Of Arms
		 */

		case 110: // Arm ++
			player.getAppearence().look[3]++;
			if (player.getAppearence().gender == 0) { // Male
				if (player.getAppearence().getLooks()[3] > 32 && player.getAppearence().getLooks()[3] < 105)
					player.getAppearence().getLooks()[3] = 105;
				if (player.getAppearence().getLooks()[3] > 110)
					player.getAppearence().getLooks()[3] = 26;

			} else if (player.getAppearence().gender == 1) { // Female
				if (player.getAppearence().getLooks()[3] > 66 && player.getAppearence().getLooks()[3] < 147)
					player.getAppearence().getLooks()[3] = 147;
				if (player.getAppearence().getLooks()[3] > 152)
					player.getAppearence().getLooks()[3] = 61;
			}
			player.getAppearence().generateAppearenceData();
			break;
		case 109: // Arm --
			player.getAppearence().look[3]--;
			if (player.getAppearence().gender == 0) { // male
				if (player.getAppearence().getLooks()[3] < 26 && player.getAppearence().getLooks()[3] > 0)
					player.getAppearence().getLooks()[3] = 110;
				if (player.getAppearence().getLooks()[3] < 105 && player.getAppearence().getLooks()[3] > 32)
					player.getAppearence().getLooks()[3] = 32;
			} else if (player.getAppearence().gender == 1) { // Female
				if (player.getAppearence().getLooks()[3] < 61 && player.getAppearence().getLooks()[3] > 0)
					player.getAppearence().getLooks()[3] = 152;
				if (player.getAppearence().getLooks()[3] < 147 && player.getAppearence().getLooks()[3] > 66)
					player.getAppearence().getLooks()[3] = 61;
			}
			player.getAppearence().generateAppearenceData();
			break;

		/*
		 * Start Of Pants
		 */

		case 118: // Pants ++
			player.getAppearence().look[5]++;
			if (player.getAppearence().gender == 0) { // Male
				if (player.getAppearence().getLooks()[5] > 41 && player.getAppearence().getLooks()[5] < 85)
					player.getAppearence().getLooks()[5] = 85;
				if (player.getAppearence().getLooks()[5] > 90)
					player.getAppearence().getLooks()[5] = 36;

			} else if (player.getAppearence().gender == 1) { // Female
				if (player.getAppearence().getLooks()[5] > 78 && player.getAppearence().getLooks()[5] < 128)
					player.getAppearence().getLooks()[5] = 128;
				if (player.getAppearence().getLooks()[5] > 134)
					player.getAppearence().getLooks()[5] = 70;
			}
			player.getAppearence().generateAppearenceData();
			break;
		case 117: // Pants --
			player.getAppearence().look[5]--;
			if (player.getAppearence().gender == 0) { // male
				if (player.getAppearence().getLooks()[5] < 36 && player.getAppearence().getLooks()[5] > 0)
					player.getAppearence().getLooks()[5] = 90;
				if (player.getAppearence().getLooks()[5] < 85 && player.getAppearence().getLooks()[5] > 41)
					player.getAppearence().getLooks()[5] = 41;
			} else if (player.getAppearence().gender == 1) { // Female
				if (player.getAppearence().getLooks()[5] < 70 && player.getAppearence().getLooks()[5] > 0)
					player.getAppearence().getLooks()[5] = 134;
				if (player.getAppearence().getLooks()[5] < 128 && player.getAppearence().getLooks()[5] > 78)
					player.getAppearence().getLooks()[5] = 78;
			}
			player.getAppearence().generateAppearenceData();
			break;

		case 76:
			//player.cantWalk = false;
			player.getPackets().sendCloseInterface();
			player.getAppearence().generateAppearenceData();
			break;
		case 97:
		//	player.cantWalk = false;
			player.getPackets().sendCloseInterface();
			player.getAppearence().generateAppearenceData();
			break;
		case 16:
			break;
		case 18:
			player.getPackets().sendIComponentAnimation(9835, 771, 374);
			player.getPackets().sendPlayerOnIComponent(771, 374);
			break;

		}
		if (button >= 304 && button <= 311) {
			player.getAppearence().getColours()[4] = button - 304;
		}
	}

	public static void change(Player player, Gender gender) {
		if (gender == Gender.MALE) {
			player.getAppearence().male();
		} else if (gender == Gender.FEMALE) {
			player.getAppearence().female();
		}
		player.getAppearence().generateAppearenceData();
		player.getPackets().sendConfig(1262, gender == Gender.MALE ? 1 : 8);
	}

}
