package group07;

/**
 * 
 * EnemyBot: Saving info about the enemy.
 */

public class EnemyBot {
	public static final int LEADER_BOT = 0;
	public static final int DROID = 1;
	public static final int NORMAL_BOT = 2;

	protected MrRobot MrRobot;
	private double bearing, distance, energy, heading, velocity;
	private long tick;
	private String name;
	private int type;
	private boolean scanned = true;

	/**
	 * EnemyBot: Create a new EnemyBot object.
	 * 
	 * @param MrRobot
	 */
	public EnemyBot(MrRobot MrRobot) {
		this.MrRobot = MrRobot;

	}

	/**
	 * update: Updating the info about the EnemyBot.
	 * 
	 * @param bearing
	 * @param distance
	 * @param energy
	 *            Energy is used to decide which type the enemyRobot is.
	 * @param heading
	 * @param velocity
	 * @param time
	 * @param name
	 */
	public void update(double bearing, double distance, double energy, double heading, double velocity, long time,
			String name) {
		this.bearing = bearing;
		this.distance = distance;
		this.energy = energy;
		this.heading = heading;
		this.velocity = velocity;
		this.tick = time;
		this.name = name;
		if (scanned) {
			scanned = false;
			if (energy <= 105) {
				type = NORMAL_BOT;
			} else if (energy < 130) {
				type = DROID;
			} else {
				type = LEADER_BOT;
			}
		}
	}

	/**
	 * 
	 * @return double
	 */
	public double getBearing() {
		return bearing;
	}

	/**
	 * 
	 * @return double
	 */
	public double getBearingRadians() {
		return Math.toRadians(bearing);
	}

	/**
	 * 
	 * @return double
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * 
	 * @return double
	 */
	public double getEnergy() {
		return energy;
	}

	/**
	 * 
	 * @return double
	 */
	public double getHeading() {
		return heading;
	}

	/**
	 * 
	 * @return double
	 */
	public double getVelocity() {
		return velocity;
	}

	/**
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return int
	 */
	public int getType() {
		return type;
	}

	/**
	 * 
	 * @return long
	 */
	public long getTick() {
		return tick;
	}

	/**
	 * 
	 * @param Energy
	 */
	public void setEnergy(int Energy) {
		this.energy = Energy;

	}

	/**
	 * 
	 * @return double
	 */
	public double getX() {
		double absBearingDeg = (MrRobot.getHeading() + bearing);
		if (absBearingDeg < 0) {
			absBearingDeg += 360;
		}
		return MrRobot.getX() + Math.sin(Math.toRadians(absBearingDeg)) * distance;
	}

	/**
	 * 
	 * @return double
	 */
	public double getY() {
		double absBearingDeg = (MrRobot.getHeading() + bearing);
		if (absBearingDeg < 0) {
			absBearingDeg += 360;
		}
		return MrRobot.getY() + Math.cos(Math.toRadians(absBearingDeg)) * distance;
	}

}
