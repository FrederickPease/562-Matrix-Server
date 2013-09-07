package com.rs.game.player.content;

public class CharacterConstants {

	public static final int HAIR_COLOR_INDEX = 0;
	public static final int TOP_COLOR_INDEX = 1;
	public static final int LEG_COLOR_INDEX = 2;
	public static final int BOOT_COLOR_INDEX = 3;
	public static final int SKIN_COLOR_INDEX = 4;
	
	public static final int GENDER_FEMALE = 1;
	public static final int GENDER_MALE = 0;
	
	public final static int[] SKIN_COLORS = {
		0, 1, 2, 3, 4, 5, 6, 7
	};
	public final static int[] BOOT_COLORS = {
		6, 1, 2, 3, 4, 5
	};
	
	public final static int[] HAIR_COLORS = {
		20/*burgundy*/,    19/*red*/,   10/*vermillion*/, 18/*pink*/,      4/*orange*/,
		5/*yello*/ ,       15/*peach*/, 7/*brown*/,       0/*dark brown*/, 6/*light brown*/,
		21/*mint green*/,  9/*green*/,  22/*dark green*/, 17/*dark blue*/, 8/*turquoise*/,
		16/*cyan*/,        11/*purple*/,24 /*viloet*/,    23/*indigo*/,    3/*dark grey*/,
		2/*military gray*/,1/*white*/,  14/*light gray*/, 13/*taupe*/,     12/*black*/,
	};
	
	public final static int[] TOP_COLORS = {
		24, 23, 2, 22, 12, 11, 6, 19, 4, 0, 9, 13, 25, 8, 15, 26, 21, 7, 20, 14, 10, 28, 27, 3, 5, 18, 17, 1, 16
	};
	
	public final static int[] LEG_COLORS = {
		24, 23, 3, 22, 13, 12, 7, 19, 5, 1, 10, 14, 25, 9, 0, 21, 8, 20, 15, 11, 28, 27, 4, 6, 18, 17, 2, 16
	};
	
	//Males
	public final static int[] MALE_HAIR_STYLES = {
		/*0, 1, 2, 3, 4, 5, 6, 7, 8, 91, 92, 93, 94, 95, 96, 97, 261, 262, 263, 264, 265, 266, 267, 268, 
		309, 310, 311, 312, 313, 314, 315, 316*/
		0, 1, 2, 3, 4, 5, 6, 7, 8, 91, 92, 93, 94, 95, 96, 97, 261, 262, 263, 264, 265, 266, 267, 268
	};
	
	public final static int[] MALE_FACIAL_HAIR_STYLES = {
		10, 11, 12, 13, 14, 15, 16, 17, 98, 99, 100, 101, 102, 103, 104,
		305, 306, 307, 308
	};
	
	public final static int[] MALE_TORSO_STYLES = {
		//18, 19, 20, 21, 22, 23, 24, 25, 111, 112, 113, 114, 115, 116
		//18, 19, 20, 21, 22, 23, 24, 25, 111, 111, 113, 114, 115,112, 116
		//111, 113, 114, 115,112, 116, 18, 19, 20, 21, 22, 23, 24, 25
		//113, 114, 115, 112, 116, 18, 19, 20, 21, 22, 23, 24, 25//original
		//18, 113, 114, 115, 112, 116, 18, 19, 20, 21, 22, 23, 24, 25, 111, 111
		18, 19, 20, 21, 22, 23, 24, 25, 111, 112, 113, 114, 115, 116
	};
	
	public final static int[] MALE_ARMS_STYLES = {
		26, 27, 28, 29, 30, 31, 105, 106, 107, 108, 109, 110//original
		//26, 27, 29, 30, 31, 105, 105, 108, 106, 107, 109, 110, 28
	};
	
	public final static int[] MALE_WRISTS_STYLES = {
		//33, 34, 84, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126//original
		33, 34, 84, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126
	};
	
	public final static int[] MALE_LEGS_STYLES = {
		36, 37, 38, 39, 40, 85, 86, 87, 88, 89, 90 //original
		//36, 36, 85, 37,89, 90, 40, 86, 88, 39, 38, 87
		//36, 85, 37, 89, 90, 40, 86, 88, 39, 38, 87, 36 

	};
	
	public final static int[] MALE_FEET_STYLES = {
		42, 43
	};
	
	//Females
	public final static int[] FEMALE_HAIR_STYLES = {
		/*45, 46, 47, 48, 49, 50, 51, 52, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 269, 270, 271, 272, 273, 274, 275,
		276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300,
		301, 302, 303, 304, 353, 354, 355, 356, 357, 358, 359, 360, 361, 362*/
		45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 269, 270, 271, 272, 273, 274, 275,
		276, 277, 278, 279, 280
	};
	
	public final static int[] FEMALE_TORSO_STYLES = {
		56, 57, 58, 59, 60, 153, 154, 155, 156, 157, 158
	};
	
	public final static int[] FEMALE_ARMS_STYLES = {
		61, 62, 63, 64, 65, 147, 148, 149, 150, 151, 152
	};
	
	public final static int[] FEMALE_WRISTS_STYLES = {
		67, 68, 127, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168
	};
	
	public final static int[] FEMALE_LEGS_STYLES = {
		70, 71, 72, 73, 74, 75, 76, 77, 128, 129, 130, 131, 132, 133, 134
	};
	
	public final static int[] FEMALE_FEET_STYLES = {
		79, 80
	};
	
	
}
