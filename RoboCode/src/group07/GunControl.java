package group07;

/**
 * 
 * Our class to control the gun from firing when it's not good to shoot.
 *
 */

public class GunControl {
	private MrRobot robot;
	private EnemyBot target;
	private AllyTracker ally;

	/**
	 * 
	 * @param ally
	 *            Instance of our allyTracker
	 */
	public GunControl(AllyTracker ally) {
		this.ally = ally;
	}

	/**
	 * 
	 * @param robot
	 *            Instance of our main class
	 * @param target
	 *            Instance of our enemybot
	 * @return takeShot checks if it's good to shoot, true = shoot, false = do not
	 *         shoot.
	 */
	public boolean takeShot(MrRobot robot, EnemyBot target) {
		this.robot = robot;
		this.target = target;
		return (distance() && target() && lowEnergy() && friendlyFire());
	}

	/**
	 * Calculates if target is within 900 pixles and checks if we can turn the gun
	 * towards them in the given time.
	 * 
	 * @return boolean to takeShot method
	 */
	private boolean distance() {
		return robot.getGunHeat() == 0 && Math.abs(robot.getGunTurnRemaining()) < 2 && target.getDistance() < 900;
	}

	/**
	 * Calculates if an ally have scanned the enemy within 4 ticks.
	 * 
	 * @return boolean to takeShot method
	 */
	private boolean target() {
		return robot.getRadar().gotFocus();

	}

	/**
	 * Checks so we have over 1 energy.
	 * 
	 * @return boolean to takeShot method
	 */
	public boolean lowEnergy() {
		return robot.getEnergy() > 1;
	}

	/**
	 * Checks if an ally is in front of us.
	 * 
	 * @return boolean to takeShot method
	 */
	private boolean friendlyFire() {
		if (target.getName().toLowerCase().contains("rut") || target.getName().toLowerCase().contains("rain")) {
			return true;
		}
		for (Ally k : ally.getAllyListWithoutOurself()) {
			double distance = MathUtils.distance(robot.getX(), robot.getY(), k.getX(), k.getY());
			double triDistance = Math.min(200, target.getDistance());
			double angle = Math.toDegrees(Math.atan2(k.getX() - robot.getX(), k.getY() - robot.getY()));
			double heading = robot.getGunHeading();
			if (heading >= 180) {
				heading -= 360;
			}
			double bearing = angle - heading;
			if (bearing >= 180) {
				bearing -= 360;
			}
			if ((bearing > -20 && bearing < 20) && (distance < triDistance)) {
				return false;
			}
		}
		return true;
	}

}
