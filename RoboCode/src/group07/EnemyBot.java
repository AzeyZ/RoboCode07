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
	 * Energy is used to decide which type the Enemy is the first time we update the information.
	 * 
	 * @param bearing Bearing to Enemy.
	 * @param distance Distance to Enemy.
	 * @param energy The energy that the Enemy has left.
	 *            
	 * @param heading Heading of the Enemy.
	 * @param velocity Velocity of the Enemy.
	 * @param time Turn information was updated.
	 * @param name Name of the Enemy the information belongs to.
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
	 * Bearing in degrees.
	 */
	public double getBearing() {
		return bearing;
	}

	/**
	 * 
	 * @return double
	 * Bearing in radians.
	 */
	public double getBearingRadians() {
		return Math.toRadians(bearing);
	}

	/**
	 * 
	 * @return double
	 * Distance to Enemy.
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * 
	 * @return double
	 * Energy.
	 */
	public double getEnergy() {
		return energy;
	}

	/**
	 * 
	 * @return double
	 * Heading.
	 */
	public double getHeading() {
		return heading;
	}

	/**
	 * 
	 * @return double
	 * Velocity.
	 */
	public double getVelocity() {
		return velocity;
	}

	/**
	 * 
	 * @return String
	 * Name.
	 */
	public String getName() {
		if(name == null)
		{
			return "";
		}
		return name;
	}

	/**
	 * 
	 * @return int
	 * The type of the enemy.
	 */
	public int getType() {
		return type;
	}

	/**
	 * 
	 * @return long
	 * Turn information was updated.
	 */
	public long getTick() {
		return tick;
	}

	/**
	 * 
	 * @return double
	 * X for the Enemy.
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
	 * Y for the Enemy.
	 */
	public double getY() {
		double absBearingDeg = (MrRobot.getHeading() + bearing);
		if (absBearingDeg < 0) {
			absBearingDeg += 360;
		}
		return MrRobot.getY() + Math.cos(Math.toRadians(absBearingDeg)) * distance;
	}

}
