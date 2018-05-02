package group07;

import robocode.TeamRobot;

public class MovementModeSwitcher {
	// Normal mode = 0
	// Surf mode = 1
	// Anti Ram mode = 2
	// ...
	private TeamRobot r;
	private long time = 0;
	private int currentMode = 0;

	public MovementModeSwitcher(TeamRobot r) {
		this.r = r;
	}

	public int getCurrentMode() {
		return currentMode;
	}

	public void setCurrentMode(int currentMode) {
		this.currentMode = currentMode;
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
}
