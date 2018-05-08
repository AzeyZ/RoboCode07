package group07;

import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import robocode.Condition;
import robocode.TeamRobot;
import robocode.util.Utils;

// Code and Methods heavily inspired by the Guess Factor Targeting Tutorial by Kawigi
// http://www.robowiki.net/wiki/GuessFactor_Targeting_Tutorial

/**
 * 
 * Class for shooting and aiming.
 * 
 */
public class Gun {

	private MrRobot robot;
	private EnemyBot target;
	public static double lastEnemyVelocity;
	public static double lateralDirection;

	private static EnemyBot lastTarget = null;

	private GunControl gunControl;

	/**
	 * 
	 * @param robotInstance our main class
	 * @param ally Instance of AllyTracker
	 * 
	 */
	public Gun(MrRobot robot, AllyTracker ally) {
		this.robot = robot;
		gunControl = new GunControl(ally);
	}

	/**
	 * AntiGravity movement calculations class
	 * @param track Instance of our enemyTrack
	 */
	public void Wave(EnemyTracker track) {
		//new wave
		GFTWave wave = new GFTWave(robot);
		target = track.getTarget();
		if (target == null)
			return;
		// calculating some doubles
		double enemyAbsoluteBearing = robot.getHeadingRadians() + target.getBearingRadians();
		if(enemyAbsoluteBearing >= 2*Math.PI) {
			enemyAbsoluteBearing -= 2*Math.PI; }
		double enemyDistance = target.getDistance();
		double enemyVelocity = target.getVelocity();
		double power = Math.min(600 / enemyDistance, 3);

		if (enemyVelocity != 0) {
			lateralDirection = MathUtils.sign(enemyVelocity
					* Math.sin(MathUtils.toRadians(track.getTarget().getHeading()) - enemyAbsoluteBearing));
		}
		wave.gunLocation = new Point2D.Double(robot.getX(), robot.getY());
		GFTWave.targetLocation = MathUtils.project(wave.gunLocation, enemyAbsoluteBearing, target.getDistance());
		wave.lateralDirection = lateralDirection;
		wave.bulletPower = power;
		wave.setSegmentations(enemyDistance, enemyVelocity, lastEnemyVelocity);
		lastEnemyVelocity = target.getVelocity();
		wave.bearing = enemyAbsoluteBearing;
		robot.setTurnGunRightRadians(Utils.normalRelativeAngle(
				enemyAbsoluteBearing - robot.getGunHeadingRadians() + wave.mostVisitedBearingOffset()));
		if(gunControl.takeShot(robot, target))
		{
			robot.setFire(wave.bulletPower);			
		}
		if (robot.getEnergy() >= power) {
			robot.addCustomEvent(wave); // ????
		}
		lastTarget = target;
	}

}
/**
 * Guess factor waves class
 *
 */
class GFTWave extends Condition {
	static Point2D targetLocation;
	double bulletPower;
	Point2D gunLocation;
	double bearing;
	double lateralDirection;
	private static final int DISTANCE_INDEXES = 5;
	private static final int VELOCITY_INDEXES = 5;
	private static final int BINS = 25;
	private static final int MIDDLE_BIN = (BINS - 1) / 2;
	private static final double MAX_ESCAPE_ANGLE = 0.65D;
	private static final double BIN_WIDTH = MAX_ESCAPE_ANGLE / (double) MIDDLE_BIN;
	private static int[][][][] statBuffers = new int[DISTANCE_INDEXES][VELOCITY_INDEXES][VELOCITY_INDEXES][BINS];
	private int[] buffer;
	private AdvancedRobot robot;
	private double distanceTraveled;

	GFTWave(TeamRobot _robot) {
		this.robot = _robot;
	}
	/**
	 * Checks if wave has arrived at enemy
	 */
	public boolean test() {
		advance();
		if (hasArrived()) {
			this.buffer[currentBin()] += 1;
			this.robot.removeCustomEvent(this);
		}
		return false;
	}
	/**
	 * Calculates the most Visited Bearing Offset
	 * @return mostVisitedBearingOffset
	 */
	double mostVisitedBearingOffset() {
		return (lateralDirection * BIN_WIDTH) * (mostVisitedBin() - MIDDLE_BIN);
	}
	/**
	 * Updates our statBuffers
	 * @param distance
	 * @param velocity
	 * @param lastVelocity
	 */
	void setSegmentations(double distance, double velocity, double lastVelocity) {
		int distanceIndex = Math.min(4, (int) (distance / 180.0D));
		int velocityIndex = (int) Math.abs(velocity / 2);
		int lastVelocityIndex = (int) Math.abs(lastVelocity / 2);
		buffer = statBuffers[distanceIndex][velocityIndex][lastVelocityIndex];
	}
	/**
	 * advance the wave forward
	 */
	private void advance() {
		this.distanceTraveled += MathUtils.bulletVelocity(this.bulletPower);
	}
	/**
	 *  Checks if wave has arrived
	 * @return
	 */
	private boolean hasArrived() {
		return this.distanceTraveled > this.gunLocation.distance(targetLocation) - 18.0D;
	}

	private int currentBin() {
		int bin = (int) Math.round(
				Utils.normalRelativeAngle(MathUtils.absoluteBearing(this.gunLocation, targetLocation) - this.bearing)
				/ (this.lateralDirection * BIN_WIDTH) + MIDDLE_BIN);

		return MathUtils.minMax(bin, 0, BINS - 1);
	}
	/**
	 * Returns mostvisited bin
	 * @return
	 */
	private int mostVisitedBin() {
		int mostVisited = MIDDLE_BIN;
		for (int i = 0; i < BINS; i++) {
			if (this.buffer[i] > this.buffer[mostVisited]) {
				mostVisited = i;
			}
		}
		return mostVisited;
	}
}
