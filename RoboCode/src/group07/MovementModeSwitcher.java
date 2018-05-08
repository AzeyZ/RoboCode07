package group07;

import robocode.TeamRobot;
/**
 * 
 * Our class for switchning between our movement systems
 * 
 */
public class MovementModeSwitcher {
	// Anti grav mode = 0
	// Surf mode = 1
	// RNDmode(aka matthias gammla move system) = 2
	// ...
	private TeamRobot r;
	private long time = 0;
	private int currentMode = 0;
	/**
	 * 
	 * @param TeamRobot Instance of the main class.
	 */
	public MovementModeSwitcher(TeamRobot r) {
		this.r = r;
	}
	/**
	 * returns which mode we are in currently.
	 * Anti grav mode = 0
	 * Surf mode = 1
	 * RND mode = 2
	 * @return currentMode
	 */
	public int getCurrentMode() {
		return currentMode;
	}
	/**
	 * Switchies to surfMode for at least 25 ticks.
	 */
	public void surfMode() {
		time = r.getTime() + 25;
		currentMode = 1;
	}
	/**
	 * if no other mode wants priority we switch to antigravity
	 */
	public void newTurn() {
		if (time <= r.getTime()) {
			currentMode = 0;
		}
	}
	/**
	 * Switchies to RND mode for at least 25 ticks.
	 */
	public void rndMove() {
		time = r.getTime() + 10;
		currentMode = 2;
	}
	/**
	 * Switchies to anti gravity mode. 
	 */
	public void AGmove() {
		time = r.getTime();
		currentMode = 0;
	}
}
