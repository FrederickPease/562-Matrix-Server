package com.rs.game.minigames.soulwars;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;

/**
 * Represents the engine that controlles soulwars
 * 
 * @author Taylor Moon
 *
 */
public class SoulwarsEngine extends SoulWars {

	/**
	 * This engines attributes
	 */
	private int currentGameTime;
	private int totalWaitTime = 900;

	/**
	 * The amount of time it takes for a soulwars game to end
	 */
	public static final int SOULWARS_GAME_TIME = 900;// in seconds 15 minutes

	/**
	 * Represents the time remaining in the current game
	 * 
	 * @return - the current game time
	 */
	public int getCurrentGameTime() {
		return currentGameTime;
	}

	/**
	 * Returns the total waiting time for the players in the lobby at this time.
	 * If there is no game then the time is 0.
	 * 
	 * @return - this lobbys waiting time
	 */
	public int getTotalWaitTime() {
		return totalWaitTime;
	}

	/**
	 * calculates the number of minutes in a specified amount of seconds
	 * 
	 * @return - the number of minutes in seconds
	 */
	public int calculateMinutesLeft() {
		String string = "" + (totalWaitTime / 60) + "".replaceAll(".", "");
		return Integer.parseInt(string);
	}

	/**
	 * Instances a soulwars boss engine
	 * 
	 * @return - SoulwarsGameEngine
	 */
	public GameTimer startGameTimer() {
		GameTimer timer = new GameTimer();
		if (timer.isRunning)
			return null;
		CoresManager.slowExecutor.scheduleWithFixedDelay(timer, 60000, 60000,
				TimeUnit.MILLISECONDS);
		return timer;
	}

	/**
	 * Reoresents the boss timer in this engine
	 * @author Taylor
	 *
	 */
	class GameTimer extends TimerTask {

		public boolean isRunning;

		@Override
		public void run() {
			currentGameTime++;
			totalWaitTime--;
			if (currentGameTime == SOULWARS_GAME_TIME) {
				endCurrentGame();
				cancel();
			}
		}
	}
}
