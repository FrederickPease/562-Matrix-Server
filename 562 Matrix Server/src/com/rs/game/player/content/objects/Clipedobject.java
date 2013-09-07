package com.rs.game.player.content.objects;



public class Clipedobject {

	public static final int[] BANK_BOOTHS = new int[] { 2213, 2214, 2215, 3045, 5276, 5277, 
		6083, 6084, 10517, 10518, 11338, 11402, 11758, 12798, 12799, 12800, 12801, 14367, 14368,
		16700, 18491, 19230, 20323, 20324, 20325, 20326, 20327, 20328, 22819, 24914, 25808, 26972,
		29085, 30015, 30016, 34205, 34206, 34207, 34752, 35647, 35648, 36262, 36786, 36787 };
	
	public static final int[] COUNTERS = new int[] { 612, 617, 2791, 2792, 2793, 2876, 3800, 4138, 
		7496, 7497, 9516, 9616, 10485, 10486, 10813, 11626, 11662, 12933, 14366, 15040, 15114, 18016,
		18017, 18018, 19699, 22811, 24124, 27082, 28357, 28358, 28363, 28364, 29144, 29356, 29469,
		29559, 29560, 30212, 30923, 33128, 36935, 37169 };
	
	public static boolean BankBooth (int objectId) {
		for (int id : BANK_BOOTHS)
			if (id == objectId)
				return true;
		return false;
	}
	
	public static boolean Counter (int objectId) {
		for (int id : COUNTERS)
			if (id == objectId)
				return true;
		return false;
	}
	
}
