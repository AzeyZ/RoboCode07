package group07;

/**
 * Radar: Controlling the radar.
 *
 */
public class Radar {
	private MrRobot robot;
	private EnemyBot target;
	private int radarDirection;
	private int wiggle;
	private int lastScan;
	private double turn;

	/**
	 * 
	 * @param robot
	 *            Instance of main class.
	 */
	public Radar(MrRobot robot) {
		this.robot = robot;
		radarDirection = 1;
		wiggle = 15;
		lastScan = 0;
	}

	/**
	 * update: updating radarTarget.
	 * 
	 * @param target
	 *            EnemyBot object for the new radarTarget.
	 */
	public void update(EnemyBot target) {
		this.target = target;
	}

	/**
	 * scan: Handles keeping track of the radarTarget. Finding the radarTarget.
	 */
	public void scan() {
		turn = MathUtils.normalizeBearing(robot.getHeading() - robot.getRadarHeading() + target.getBearing());
		double changeDirection = 1;
		if (turn >= 0) {
			changeDirection = 1;
		} else {
			changeDirection = -1;
		}
		// Lost focus then rotate radar
		lastScan++;
		if (lastScan % 5 == 0) {
			robot.setTurnRadarRight(360 * changeDirection);
		}
		// Focus the existing target
		if (robot.getTime() - target.getTick() < 5) {
			scanTarget();
		}
	}

	/**
	 * scanTarget: Handles the radar if we got our radarTarget in the scanner.
	 */
	public void scanTarget() {
		turn += wiggle * radarDirection;
		robot.setTurnRadarRight(MathUtils.normalizeBearing(turn));
		radarDirection *= -1;
		lastScan = 0;
	}

	/**
	 * 
	 * @return True if we have focus. False if we lost focus.
	 */
	public boolean gotFocus() {
		return lastScan <= 4;
	}
}
