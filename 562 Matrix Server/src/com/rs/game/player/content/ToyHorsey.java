package com.rs.game.player.content;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.player.Player;
import com.rs.game.player.PublicChatMessage;
import com.rs.utils.Utils;

/**
 * Slappin' dat marcupial.
 * @author Taylor Moon<Axter>
 *
 */
public class ToyHorsey {
	
	
	public static String[] chats = {//Weird ikr?
			"Come on Dobbin, we can win the race!", 
			"Hi-ho, Silver and away!",
			"Neaahhhyyy! " };
	public static Animation anims[] = { 
			new Animation(918),
			new Animation(919), 
			new Animation(920),
			new Animation(921) };
	/**
	 * uses the horsey.
	 * @Param player
	 */
	public static void useHorsey(Player player) {
		player.lock();
		int random = Utils.random(3);
		int i = Utils.random(3);
		player.setNextPublicChatMessage(new PublicChatMessage(chats[random], 0));
		player.setNextAnimation(anims[i]);
		player.unlock();
	}
	
	

}