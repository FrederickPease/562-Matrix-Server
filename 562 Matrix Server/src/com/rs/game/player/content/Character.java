/*package com.rs.game.player.content;

import com.rs.game.player.Player;

public class Character {



		//TODO - before hairs and before beards.
		public static final int START_UP_INTERFACE = 771;
		
		public static void open(Player player) {
			player.getFrames().displayInterface(START_UP_INTERFACE);
			player.getPackets().sendPlayerOnIComponent(START_UP_INTERFACE, 79);
			player.getPackets().sendIComponentAnimation(9805, START_UP_INTERFACE, 79);
			player.getPackets().sendConfig(START_UP_INTERFACE, 92);//back - hair (problem with them..)
			player.getPackets().sendConfig(START_UP_INTERFACE, 97);//back - beards (problem with them..)
			player.getCharacterConstants().setGender(CharacterConstants.GENDER_MALE);
			player.getCharacterConstants().toDefault();
			player.getFrames().sendConfig(1262, 1);
			player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
		}

		
		public static void handleButtons(Player player, int buttonId) {
			int index = -1;
			int color = -1;
			if(buttonId >= 307 && buttonId <= 312) {
				index = CharacterConstants.BOOT_COLOR_INDEX;
				color = CharacterConstants.BOOT_COLORS[buttonId - 307];
			}
			if(buttonId >= 151 && buttonId <= 158) {
				index = CharacterConstants.SKIN_COLOR_INDEX;
				color = CharacterConstants.SKIN_COLORS[buttonId - 151];
			}
			if(buttonId >= 100 && buttonId <= 124) {
				index = CharacterConstants.HAIR_COLOR_INDEX;
				color = CharacterConstants.HAIR_COLORS[buttonId - 100];
			}
			if(buttonId >= 189 && buttonId <= 217) {
				index = CharacterConstants.TOP_COLOR_INDEX;
				color = CharacterConstants.TOP_COLORS[buttonId - 189];
			}
			if(buttonId >= 249 && buttonId <= 276) {
				index = CharacterConstants.LEG_COLOR_INDEX;
				color = CharacterConstants.LEG_COLORS[buttonId - 249];
			}
			if(index != -1 && color != -1) {
				player.getCharacterConstants().colour[index] = color;
				player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
			}
			switch(buttonId) {
			case 357://Previous feet
				switch(player.getCharacterConstants().getGender()) {
				case CharacterConstants.GENDER_MALE:
					int nextFeet = -1;
					int feetIndex = player.getCharacterConstants().getFeetIndex();
					if (feetIndex == 0) {
						feetIndex = 1;
					} else {
						feetIndex -= 1;
					}
					nextFeet = CharacterConstants.MALE_FEET_STYLES[feetIndex];
					player.getCharacterConstants().setFeetIndex(feetIndex);
					player.getCharacterConstants().setLook(6, nextFeet);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				case CharacterConstants.GENDER_FEMALE:
					nextFeet = -1;
					feetIndex = player.getCharacterConstants().getFeetIndex();
					if (feetIndex == 0) {
						feetIndex = 1;
					} else {
						feetIndex -= 1;
					}
					nextFeet = CharacterConstants.FEMALE_FEET_STYLES[feetIndex];
					player.getCharacterConstants().setFeetIndex(feetIndex);
					player.getCharacterConstants().setLook(6, nextFeet);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				}
				break;
			case 358://Next feet
				switch(player.getCharacterConstants().getGender()) {
				case CharacterConstants.GENDER_MALE:
					int nextFeet = -1;
					int feetIndex = player.getCharacterConstants().getFeetIndex();
					if (feetIndex == 1) {
						feetIndex = 0;
					} else {
						feetIndex += 1;
					}
					nextFeet = CharacterConstants.MALE_FEET_STYLES[feetIndex];
					player.getCharacterConstants().setFeetIndex(feetIndex);
					player.getCharacterConstants().setLook(6, nextFeet);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				case CharacterConstants.GENDER_FEMALE:
					nextFeet = -1;
					feetIndex = player.getCharacterConstants().getFeetIndex();
					if (feetIndex == 1) {
						feetIndex = 0;
					} else {
						feetIndex += 1;
					}
					nextFeet = CharacterConstants.FEMALE_FEET_STYLES[feetIndex];
					player.getCharacterConstants().setFeetIndex(feetIndex);
					player.getCharacterConstants().setLook(6, nextFeet);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				}
				break;
			case 353://Previous legs
				switch(player.getCharacterConstants().getGender()) {
				case CharacterConstants.GENDER_MALE:
					int nextLegs = -1;
					int legsIndex = player.getCharacterConstants().getLegsIndex();
					if (legsIndex == 0) {
						legsIndex = CharacterConstants.MALE_LEGS_STYLES.length - 1;
					} else {
						legsIndex -= 1;
					}
					nextLegs = CharacterConstants.MALE_LEGS_STYLES[legsIndex];
					player.getCharacterConstants().setLegsIndex(legsIndex);
					player.getCharacterConstants().setLook(5, nextLegs);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				case CharacterConstants.GENDER_FEMALE:
					nextLegs = -1;
					legsIndex = player.getCharacterConstants().getLegsIndex();
					if (legsIndex == 0) {
						legsIndex = CharacterConstants.FEMALE_LEGS_STYLES.length - 1;
					} else {
						legsIndex -= 1;
					}
					nextLegs = CharacterConstants.FEMALE_LEGS_STYLES[legsIndex];
					player.getCharacterConstants().setLegsIndex(legsIndex);
					player.getCharacterConstants().setLook(5, nextLegs);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				}
				break;
			case 354://Next legs
				switch(player.getCharacterConstants().getGender()) {
				case CharacterConstants.GENDER_MALE:
					int nextLegs = -1;
					int legsIndex = player.getCharacterConstants().getLegsIndex();
					if (legsIndex == 10) {
						legsIndex = 0;
					} else {
						legsIndex += 1;
					}
					nextLegs = CharacterConstants.MALE_LEGS_STYLES[legsIndex];
					player.getCharacterConstants().setLegsIndex(legsIndex);
					player.getCharacterConstants().setLook(5, nextLegs);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				case CharacterConstants.GENDER_FEMALE:
					nextLegs = -1;
					legsIndex = player.getCharacterConstants().getLegsIndex();
					if (legsIndex == 14) {
						legsIndex = 0;
					} else {
						legsIndex += 1;
					}
					nextLegs = CharacterConstants.FEMALE_LEGS_STYLES[legsIndex];
					player.getCharacterConstants().setLegsIndex(legsIndex);
					player.getCharacterConstants().setLook(5, nextLegs);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				}
				break;
			case 349://Previous wrists
				switch(player.getCharacterConstants().getGender()) {
				case CharacterConstants.GENDER_MALE:
					int nextWrists = -1;
					int wristsIndex = player.getCharacterConstants().getWristsIndex();
					if (wristsIndex == 0) {
						wristsIndex = CharacterConstants.MALE_WRISTS_STYLES.length - 1;
					} else {
						wristsIndex -= 1;
					}
					nextWrists = CharacterConstants.MALE_WRISTS_STYLES[wristsIndex];
					player.getCharacterConstants().setWristsIndex(wristsIndex);
					player.getCharacterConstants().setLook(4, nextWrists);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				case CharacterConstants.GENDER_FEMALE:
					nextWrists = -1;
					wristsIndex = player.getCharacterConstants().getWristsIndex();
					if (wristsIndex == 0) {
						wristsIndex = CharacterConstants.FEMALE_WRISTS_STYLES.length - 1;
					} else {
						wristsIndex -= 1;
					}
					nextWrists = CharacterConstants.FEMALE_WRISTS_STYLES[wristsIndex];
					player.getCharacterConstants().setWristsIndex(wristsIndex);
					player.getCharacterConstants().setLook(4, nextWrists);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				}
				break;
			case 350://Next wrists
				switch(player.getCharacterConstants().getGender()) {
				case CharacterConstants.GENDER_MALE:
					int nextWrists = -1;
					int wristsIndex = player.getCharacterConstants().getWristsIndex();
					if (wristsIndex == 12) {
						wristsIndex = 0;
					} else {
						wristsIndex += 1;
					}
					nextWrists = CharacterConstants.MALE_WRISTS_STYLES[wristsIndex];
					player.getCharacterConstants().setWristsIndex(wristsIndex);
					player.getCharacterConstants().setLook(4, nextWrists);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				case CharacterConstants.GENDER_FEMALE:
					nextWrists = -1;
					wristsIndex = player.getCharacterConstants().getWristsIndex();
					if (wristsIndex == 12) {
						wristsIndex = 0;
					} else {
						wristsIndex += 1;
					}
					nextWrists = CharacterConstants.FEMALE_WRISTS_STYLES[wristsIndex];
					player.getCharacterConstants().setWristsIndex(wristsIndex);
					player.getCharacterConstants().setLook(4, nextWrists);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				}
				break;
			case 345://Before arms
				switch(player.getCharacterConstants().getGender()) {
				case CharacterConstants.GENDER_MALE:
					int nextArms = -1;
					int armsIndex = player.getCharacterConstants().getArmsIndex();
					if (armsIndex == 0) {
						armsIndex = CharacterConstants.MALE_ARMS_STYLES.length - 1;
					} else {
						armsIndex -= 1;
					}
					nextArms = CharacterConstants.MALE_ARMS_STYLES[armsIndex];
					player.getCharacterConstants().setArmsIndex(armsIndex);
					player.getCharacterConstants().setLook(3, nextArms);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				case CharacterConstants.GENDER_FEMALE:
					armsIndex = player.getCharacterConstants().getArmsIndex();
					nextArms = -1;
					if (armsIndex == 0) {
						armsIndex = CharacterConstants.FEMALE_ARMS_STYLES.length - 1;
					} else {
						armsIndex -= 1;
					}
					nextArms = CharacterConstants.FEMALE_ARMS_STYLES[armsIndex];
					player.getCharacterConstants().setArmsIndex(armsIndex);
					player.getCharacterConstants().setLook(3, nextArms);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				}
				break;
			case 346://Next arms
				switch(player.getCharacterConstants().getGender()) {
				case CharacterConstants.GENDER_MALE:
					int nextArms = -1;
					int armsIndex = player.getCharacterConstants().getArmsIndex();
					if (armsIndex == 11) {
						armsIndex = 0;
					} else {
						armsIndex += 1;
					}
					nextArms = CharacterConstants.MALE_ARMS_STYLES[armsIndex];
					player.getCharacterConstants().setArmsIndex(armsIndex);
					player.getCharacterConstants().setLook(3, nextArms);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				case CharacterConstants.GENDER_FEMALE:
					armsIndex = player.getCharacterConstants().getArmsIndex();
					nextArms = -1;
					if (armsIndex == 10) {
						armsIndex = 0;
					} else {
						armsIndex += 1;
					}
					nextArms = CharacterConstants.FEMALE_ARMS_STYLES[armsIndex];
					player.getCharacterConstants().setArmsIndex(armsIndex);
					player.getCharacterConstants().setLook(3, nextArms);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				}
				break;
			case 342://Next torso
				switch(player.getCharacterConstants().getGender()) {
				case CharacterConstants.GENDER_MALE:
					int nextChest = -1;
					int chestIndex = player.getCharacterConstants().getTorsoIndex();
					if (chestIndex == 13) {
						chestIndex = 0;
					} else {
						chestIndex += 1;
					}
					nextChest = CharacterConstants.MALE_TORSO_STYLES[chestIndex];
					player.getCharacterConstants().setTorsoIndex(chestIndex);
					player.getCharacterConstants().setLook(2, nextChest);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				case CharacterConstants.GENDER_FEMALE:
					chestIndex = player.getCharacterConstants().getTorsoIndex();
					if (chestIndex == 10) {
						chestIndex = 0;
					} else {
						chestIndex += 1;
					}
					nextChest = CharacterConstants.FEMALE_TORSO_STYLES[chestIndex];
					player.getCharacterConstants().setTorsoIndex(chestIndex);
					player.getCharacterConstants().setLook(2, nextChest);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				}
				break;
			case 341://Previous torso
				switch(player.getCharacterConstants().getGender()) {
				case CharacterConstants.GENDER_MALE:
					int nextChest = -1;
					int chestIndex = player.getCharacterConstants().getTorsoIndex();
					if (chestIndex == 0) {
						chestIndex = CharacterConstants.MALE_TORSO_STYLES.length - 1;
					} else {
						chestIndex -= 1;
					}
					nextChest = CharacterConstants.MALE_TORSO_STYLES[chestIndex];
					player.getCharacterConstants().setTorsoIndex(chestIndex);
					player.getCharacterConstants().setLook(2, nextChest);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				case CharacterConstants.GENDER_FEMALE:
					chestIndex = player.getCharacterConstants().getTorsoIndex();
					if (chestIndex == 0) {
						chestIndex = CharacterConstants.FEMALE_TORSO_STYLES.length - 1;
					} else {
						chestIndex -= 1;
					}
					nextChest = CharacterConstants.FEMALE_TORSO_STYLES[chestIndex];
					player.getCharacterConstants().setTorsoIndex(chestIndex);
					player.getCharacterConstants().setLook(2, nextChest);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				}
				break;
			case 93://Next hair
				switch(player.getCharacterConstants().getGender()) {
				case CharacterConstants.GENDER_MALE:
					int nextHair = -1;
					int hairIndex = player.getCharacterConstants().getHairIndex();
					if (hairIndex == 23) {
						hairIndex = 0;
					} else {
						hairIndex += 1;
					}
					nextHair = CharacterConstants.MALE_HAIR_STYLES[hairIndex];
					player.getCharacterConstants().setHairIndex(hairIndex);
					player.getCharacterConstants().setLook(0, nextHair);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				case CharacterConstants.GENDER_FEMALE:
					nextHair = -1;
					hairIndex = player.getCharacterConstants().getHairIndex();
					if (hairIndex == 33) {
						hairIndex = 0;
					} else {
						hairIndex += 1;
					}
					nextHair = CharacterConstants.FEMALE_HAIR_STYLES[hairIndex];
					player.getCharacterConstants().setHairIndex(hairIndex);
					player.getCharacterConstants().setLook(0, nextHair);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				}
				break;
			case 98://Next beard
				switch(player.getCharacterConstants().getGender()) {
				case CharacterConstants.GENDER_MALE:
					int nextBeard = -1;
					int beardIndex = player.getCharacterConstants().getBeardIndex();
					if (beardIndex == 18) {
						beardIndex = 0;
					} else {
						beardIndex += 1;
					}
					nextBeard = CharacterConstants.MALE_FACIAL_HAIR_STYLES[beardIndex];
					player.getCharacterConstants().setBeardIndex(beardIndex);
					player.getCharacterConstants().setLook(1, nextBeard);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				}
				break;
			case 92://Previous hair
				switch(player.getCharacterConstants().getGender()) {
				case Appearence.GENDER_MALE:
					int nextHair = -1;
					int hairIndex = player.getCharacterConstants().getHairIndex();
					if (hairIndex == 0) {
						hairIndex = Appearence.MALE_HAIR_STYLES.length - 1;
					} else {
						hairIndex -= 1;
					}
					nextHair = CharacterConstants.MALE_HAIR_STYLES[hairIndex];
					player.getCharacterConstants().setHairIndex(hairIndex);
					player.getCharacterConstants().setLook(0, nextHair);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				case CharacterConstants.GENDER_FEMALE:
					nextHair = -1;
					hairIndex = player.getCharacterConstants().getHairIndex();
					if (hairIndex == 0) {
						hairIndex = CharacterConstants.FEMALE_HAIR_STYLES.length - 1;
					} else {
						hairIndex -= 1;
					}
					nextHair = CharacterConstants.FEMALE_HAIR_STYLES[hairIndex];
					player.getCharacterConstants().setHairIndex(hairIndex);
					player.getCharacterConstants().setLook(0, nextHair);
					player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
					break;
				}
				break;
			case 49://male
				player.getCharacterConstants().setGender(CharacterConstants.GENDER_MALE);
				player.getCharacterConstants().toDefault();
				player.getFrames().sendConfig(1262, 1);
				player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
				break;
			case 52://female
				player.getCharacterConstants().setGender(CharacterConstants.GENDER_FEMALE);
				player.getCharacterConstants().toDefault();
				player.getFrames().sendConfig(1262, -1);
				player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
				break;
			case 40: // mouse
				player.getSettings().setMouseTwoButtons(false);
				break;
			case 37: // mouse
				player.getSettings().setMouseTwoButtons(true);
				break;
			case 16:
				//for(int i = 100; i < 300; i++)
					//player.getPackets().sendInterfaceConfig(START_UP_INTERFACE, i, false);
				break;
			case 321://Random
				player.getCharacterConstants().colour[CharacterConstants.TOP_COLOR_INDEX] = CharacterConstants.TOP_COLORS[NumberUtils.random(CharacterConstants.TOP_COLORS.length - 1)];
				player.getCharacterConstants().colour[CharacterConstants.LEG_COLOR_INDEX] = CharacterConstants.LEG_COLORS[NumberUtils.random(CharacterConstants.LEG_COLORS.length - 1)];
				player.getCharacterConstants().colour[CharacterConstants.BOOT_COLOR_INDEX] = CharacterConstants.BOOT_COLORS[NumberUtils.random(CharacterConstants.BOOT_COLORS.length - 1)];
				player.getUpdateFlags().flag(UpdateFlag.CharacterConstants);
				break;
			case 319:
				for(int i = 360; i < 365; i++)
					player.getPackets().sendConfig(START_UP_INTERFACE, i, true);
				break;
			case 362:
				player.getFrames().closeInterfaces();
				player.getFrames().sendWindowPane(548);
				break;
			}
		}
	}*/
