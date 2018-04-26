package group07;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import robocode.AdvancedRobot;
import robocode.Condition;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

public class Gun {

	private Robot07 robot;
	private TargetEnemyBot target;
	private GunControl gunControl = new GunControl();
	private int[][] stats = new int[13][31];
	private int direction = 1;
	private static double lastEnemyVelocity;
	private static double lateralDirection;

	public Gun(Robot07 robot) {
		this.robot = robot;
	}

	public void update(TargetEnemyBot target) {
		this.target = target;
	}

	// Aim straight at the target
	public void aim() {
		// kraften beroende på hur långt ifrån vi är
		double firePower = Math.min(400 / target.getDistance(), 3);
		// hastigheten på skottet
		double bulletSpeed = 20 - firePower * 3;
		// avstånd = hastighet * tid
		long time = (long) (target.getDistance() / bulletSpeed);

		// calculate gun turn to predicted x,y location
		double futureX = target.getFutureX(time);
		double futureY = target.getFutureY(time);

		double absDeg = MathUtils.absoluteBearing(robot.getX(), robot.getY(), futureX, futureY);
		// turn the gun to the predicted x,y location
		robot.setTurnGunRight(MathUtils.normalizeBearing(absDeg - robot.getGunHeading()));
	}

	// Fires with a certain firepower once we have rotated the gun
	public void fire() {
		if (gunControl.takeShot(robot, target)) {
			robot.setFire(Math.min(400 / target.getDistance(), 3));
		}
	}

	// Wave functions
	public void Wave(EnemyTracker track) {

		target = track.getTarget();
		if(target == null) return;
		
		double enemyAbsoluteBearing = robot.getHeadingRadians() + target.getBearingRadians();
		double enemyDistance = target.getDistance();
		double enemyVelocity = target.getVelocity();
		double power = Math.min(400 / enemyDistance, 3);
		
		if (enemyVelocity != 0) {
			lateralDirection = MathUtils.sign(enemyVelocity * Math.sin(MathUtils.toRadians(track.getTarget().getHeading()) - enemyAbsoluteBearing));
		}
		GFTWave wave = new GFTWave(robot);
		wave.gunLocation = new Point2D.Double(robot.getX(), robot.getY());
		GFTWave.targetLocation = MathUtils.project(wave.gunLocation, enemyAbsoluteBearing, target.getDistance());
		//wave.setLocation(MathUtils.project(wave.gunLocation, absBearing, target.getDistance()));
		wave.lateralDirection = lateralDirection;
		wave.bulletPower = power;
		wave.setSegmentations(enemyDistance, enemyVelocity, lastEnemyVelocity);
		lastEnemyVelocity = target.getVelocity();
		wave.bearing = enemyAbsoluteBearing;
		robot.setTurnGunRightRadians(
				Utils.normalRelativeAngle(enemyAbsoluteBearing - robot.getGunHeadingRadians() + wave.mostVisitedBearingOffset()));
		robot.setFire(wave.bulletPower);

		if (robot.getEnergy() >= power) {
			robot.addCustomEvent(wave); // ????
		}
		// robot.setTurnRadarRightRadians(Utils.normalRelativeAngle(enemyAbsoluteBearing - robot.getRadarHeadingRadians()) * 2);
	}

}

class GFTWave extends Condition {
	static Point2D targetLocation;
	double bulletPower;
	Point2D gunLocation;
	double bearing;
	double lateralDirection;
	private static final double MAX_DISTANCE = 400.0D;
	private static final int DISTANCE_INDEXES = 5;
	private static final int VELOCITY_INDEXES = 5;
	private static final int BINS = 25;
	private static final int MIDDLE_BIN = (BINS - 1) / 2;
	private static final double MAX_ESCAPE_ANGLE = 0.7D;
	private static final double BIN_WIDTH = MAX_ESCAPE_ANGLE / (double)MIDDLE_BIN;
	private static int[][][][] statBuffers = new int[DISTANCE_INDEXES][VELOCITY_INDEXES][VELOCITY_INDEXES][BINS];
	private int[] buffer;
	private AdvancedRobot robot;
	private double distanceTraveled;

	GFTWave(TeamRobot _robot) {
		this.robot = _robot;
	}

	public boolean test() {
		advance();
		if (hasArrived()) {
			this.buffer[currentBin()] += 1;
			this.robot.removeCustomEvent(this);
		}
		return false;
	}

	public void setLocation(Point2D loc) {
		targetLocation = loc;
	}

	double mostVisitedBearingOffset() {
		return (lateralDirection * BIN_WIDTH) * (mostVisitedBin() - MIDDLE_BIN);
	}

	void setSegmentations(double distance, double velocity, double lastVelocity) {
		int distanceIndex = Math.min(4, (int)(distance / 180.0D));
		int velocityIndex = (int) Math.abs(velocity / 2);
		int lastVelocityIndex = (int) Math.abs(lastVelocity / 2);
		buffer = statBuffers[distanceIndex][velocityIndex][lastVelocityIndex];
	}

	private void advance() {
		this.distanceTraveled += MathUtils.bulletVelocity(this.bulletPower);
	}

	private boolean hasArrived() {
		return this.distanceTraveled > this.gunLocation.distance(targetLocation) - 18.0D;
	}

	private int currentBin() {
		int bin = (int) Math.round(
				Utils.normalRelativeAngle(MathUtils.absoluteBearing(this.gunLocation, targetLocation) - this.bearing)
						/ (this.lateralDirection * BIN_WIDTH) + MIDDLE_BIN);

		return MathUtils.minMax(bin, 0, BINS - 1);
	}

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
