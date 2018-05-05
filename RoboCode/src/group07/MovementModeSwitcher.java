package group07;

import robocode.TeamRobot;

public class MovementModeSwitcher {
	// Anti grav mode = 0
	// Surf mode = 1
	// RNDmode(aka matthias gammla move system) = 2
	// ...
	private TeamRobot r;
	private long time = 0;
	public static int currentMode = 0;

	public MovementModeSwitcher(TeamRobot r) {
		this.r = r;
	}

	public int getCurrentMode() {
		return currentMode;
	}

	public void SurfMode() {
		time = r.getTime() + 25;
		currentMode = 1;
	}

	public void NewTurn() {
		if (time <= r.getTime()) {
			currentMode = 0;
		}
	}
	public void RNDMove() {
		time = r.getTime() + 10;
		currentMode = 2;
	}
	
	public void AGmove() {
		time = r.getTime();
		currentMode = 0;
	}
}
