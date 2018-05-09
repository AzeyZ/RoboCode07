package group07;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

// Code and Methods heavily inspired by the surfing tutorial by Voidious
// http://www.robowiki.net/wiki/Wave_Surfing_Tutorial

/**
 * A class for out surfMovement system.
 * 
 */

public class SurfMovement {
	private static int BINS = 47;
	private static double _surfStats[] = new double[BINS]; // we'll use 47 bins
	private Point2D.Double _myLocation; // our bot's location
	private Point2D.Double _enemyLocation; // enemy bot's location
	private static MovementModeSwitcher mode;
	private RobotMovement Rmove;
	private EnemyTracker track;
	private TeamRobot r;
	private AllyTracker allyTracker;

	private ArrayList _enemyWaves;
	private ArrayList _surfDirections;
	private ArrayList _surfAbsBearings;
	private static double _oppEnergy = 100.0;

	private static Rectangle2D.Double _fieldRect; // = new java.awt.geom.Rectangle2D.Double(18, 18, 764, 564);

	private static double WALL_STICK = 160;

	/**
	 * @param MovementModeSwitcher Same instance as in the main class.
	 * @param RobotMovement Same instance as in the main class.
	 * @param EnemyTracker Same instance as in the main class.
	 * @param TeamRobot an Instance of the main class.
	 */
	
	public SurfMovement(MovementModeSwitcher mode, RobotMovement Rmove, EnemyTracker track, TeamRobot r,
			AllyTracker allyTracker) {
		this.track = track;
		this.mode = mode;
		this.Rmove = Rmove;
		_enemyWaves = new ArrayList();
		_surfDirections = new ArrayList();
		_surfAbsBearings = new ArrayList();
		this.r = r;
		this.allyTracker = allyTracker;
	}
	/**
	 * 
	 * @param ScannedRobotEvent updating our surf calculations with the new scannedRobotevent. 
	 */
	public void updateSurf(ScannedRobotEvent e) {
		
		// Creating an outer rectangle smaller than the size of the map to stop us from hitting the walls.
		_fieldRect = new java.awt.geom.Rectangle2D.Double(18, 18, r.getBattleFieldWidth() * 0.95,
				r.getBattleFieldHeight() * 0.95);
		// Saving our location
		_myLocation = new Point2D.Double(r.getX(), r.getY());
		//calculating our lateralvelocity and absolute bearing.
		double lateralVelocity = r.getVelocity() * Math.sin(e.getBearingRadians());
		double absBearing = e.getBearingRadians() + r.getHeadingRadians();
		_surfDirections.add(0, new Integer((lateralVelocity >= 0) ? 1 : -1));
		_surfAbsBearings.add(0, new Double(absBearing + Math.PI));
		// Calculating enemy bulletpower
		double bulletPower = _oppEnergy - e.getEnergy();
		// Checking if enemy has fired
		if (bulletPower < 3.01 && bulletPower > 0.09 && _surfDirections.size() > 2) {
			// Changing our current mode to surfMode
			mode.surfMode();
			// new enemy wave
			EnemyWave ew = new EnemyWave();
			ew.fireTime = r.getTime() - 1;
			ew.bulletVelocity = bulletVelocity(bulletPower);
			ew.distanceTraveled = bulletVelocity(bulletPower);
			ew.direction = ((Integer) _surfDirections.get(2)).intValue();
			ew.directAngle = ((Double) _surfAbsBearings.get(2)).doubleValue();
			ew.fireLocation = (Point2D.Double) _enemyLocation.clone(); // last tick
			// adding the new wave to the list
			_enemyWaves.add(ew);
		}
		// saving enemy energy
		_oppEnergy = e.getEnergy();

		// update after EnemyWave detection, because that needs the previous
		// enemy location as the source of the wave
		_enemyLocation = project(_myLocation, absBearing, e.getDistance());
		// updating waves with new info and starting our surfing.
		updateWaves();
		doSurfing(e);

	}
	/**
	 * Local help class for enemywaves
	 */
	class EnemyWave {
		Point2D.Double fireLocation;
		long fireTime;
		double bulletVelocity, directAngle, distanceTraveled;
		int direction;

		public EnemyWave() {
		}
	}
	
	/**
	 * 
	 * @param Current enemywave
	 * @param our current direction either 1 or -1
	 * @param instance of our main class
	 * @return danger index.
	 */
	private double checkDanger(EnemyWave surfWave, int direction, TeamRobot r) {
		int index = getFactorIndex(surfWave, predictPosition(surfWave, direction, r));

		return _surfStats[index];
	}
	/**
	 * 
	 * @param ScannedRobotEvent for currently scanned enemy.
	 */
	private void doSurfing(ScannedRobotEvent e) {

		EnemyWave surfWave = getClosestSurfableWave();

		if (surfWave == null) {
			return;
		}
		// calculating danger indexes
		double dangerLeft = checkDanger(surfWave, -1, r);
		double dangerRight = checkDanger(surfWave, 1, r);
		// calculating desired angle
		double goAngle = absoluteBearing(surfWave.fireLocation, _myLocation);
		// Changing angle to not hit walls
		if (dangerLeft < dangerRight) {
			goAngle = wallSmoothing(_myLocation, goAngle - (Math.PI / 2), -1);
		} else {
			goAngle = wallSmoothing(_myLocation, goAngle + (Math.PI / 2), 1);
		}
		
		
		/**
		 * Checking if any allies are too close to us
		 */
		ArrayList<Ally> allies = allyTracker.getAllyList();
		boolean allyDistanceCheck = true;
		for (int i = 0; i < allies.size(); i++) {
			if (!allies.get(i).getName().equals(r.getName())) {
				double dist = MathUtils.distance(r.getX(), r.getY(), allies.get(i).getX(), allies.get(i).getY());
				if (dist < 150) {
					allyDistanceCheck = false;
				}
			}
		}
		// checking if surf target is too far away
		boolean targetDistanceCheck = e.getDistance() > 800;
		// checking for rammers
		boolean antiRam = true;
		for (int i = 0; i < track.getEnemyList().size(); i++) {
			if (track.getEnemyList().get(i).getDistance() < 150) {
				antiRam = false;
			}
		}
		
		// checks if we should switch to antigrav movement instead
		if (!allyDistanceCheck || targetDistanceCheck || !antiRam) {
			mode.AGmove();
		}
		// moving in desired direction
		if (mode.getCurrentMode() == 1) {
			setBackAsFront(r, goAngle);
		}
	}
	/**
	 * 
	 * @param EnemyWave our current Instance of enemywave
	 * @param direction our direction either 1 or -1
	 * @param TeamRobot Instance of our main class
	 * @return predictedPosition
	 */
	private Point2D.Double predictPosition(EnemyWave surfWave, int direction, TeamRobot r) {
		Point2D.Double predictedPosition = (Point2D.Double) _myLocation.clone();
		double predictedVelocity = r.getVelocity();
		double predictedHeading = r.getHeadingRadians();
		double maxTurning, moveAngle, moveDir;

		int counter = 0; // number of ticks in the future
		boolean intercepted = false;

		do { // the rest of these code comments are rozu's
			moveAngle = wallSmoothing(predictedPosition,
					absoluteBearing(surfWave.fireLocation, predictedPosition) + (direction * (Math.PI / 2)), direction)
					- predictedHeading;
			moveDir = 1;

			if (Math.cos(moveAngle) < 0) {
				moveAngle += Math.PI;
				moveDir = -1;
			}

			moveAngle = Utils.normalRelativeAngle(moveAngle);

			// maxTurning 
			maxTurning = Math.PI / 720d * (40d - 3d * Math.abs(predictedVelocity));
			predictedHeading = Utils.normalRelativeAngle(predictedHeading + limit(-maxTurning, moveAngle, maxTurning));

			// if predictedVelocity and moveDir have
			// different signs you want to breack down
			// otherwise you want to accelerate
			predictedVelocity += (predictedVelocity * moveDir < 0 ? 2 * moveDir : moveDir);
			predictedVelocity = limit(-8, predictedVelocity, 8);

			// calculate the new predicted position
			predictedPosition = project(predictedPosition, predictedHeading, predictedVelocity);

			counter++;

			if (predictedPosition.distance(surfWave.fireLocation) < surfWave.distanceTraveled
					+ (counter * surfWave.bulletVelocity) + surfWave.bulletVelocity) {
				intercepted = true;
			}
		} while (!intercepted && counter < 500);

		return predictedPosition;
	}
	/**
	 * If we are hit by a bullet we redo our calculations with the new info.
	 * @param HitByBulletEvent 
	 * 
	 */
	public void onHitByBulletSurf(HitByBulletEvent e) {
		// If the _enemyWaves collection is empty, we must have missed the
		// detection of this wave somehow.
		if (!_enemyWaves.isEmpty()) {
			Point2D.Double hitBulletLocation = new Point2D.Double(e.getBullet().getX(), e.getBullet().getY());
			EnemyWave hitWave = null;

			// look through the EnemyWaves, and find one that could've hit us.
			for (int x = 0; x < _enemyWaves.size(); x++) {
				EnemyWave ew = (EnemyWave) _enemyWaves.get(x);

				if (Math.abs(ew.distanceTraveled - _myLocation.distance(ew.fireLocation)) < 50
						&& Math.abs(bulletVelocity(e.getBullet().getPower()) - ew.bulletVelocity) < 0.001) {
					hitWave = ew;
					break;
				}
			}

			if (hitWave != null) {
				logHit(hitWave, hitBulletLocation);

				// We can remove this wave now, of course.
				_enemyWaves.remove(_enemyWaves.lastIndexOf(hitWave));
			}
		}
	}

	// Given the EnemyWave that the bullet was on, and the point where we
	// were hit, update our stat array to reflect the danger in that area.
	private void logHit(EnemyWave ew, Point2D.Double targetLocation) {
		int index = getFactorIndex(ew, targetLocation);

		for (int x = 0; x < BINS; x++) {
			// for the spot bin that we were hit on, add 1;
			// for the bins next to it, add 1 / 2;
			// the next one, add 1 / 5; and so on...
			_surfStats[x] += 1.0 / (Math.pow(index - x, 2) + 1);
		}
	}
	
	/**
	 * 
	 * @param EnemyWave current enemywave
	 * @param targetLocation targets location
	 * @return SurfWave
	 */
	private static int getFactorIndex(EnemyWave ew, Point2D.Double targetLocation) {
		double offsetAngle = (absoluteBearing(ew.fireLocation, targetLocation) - ew.directAngle);
		double factor = Utils.normalRelativeAngle(offsetAngle) / maxEscapeAngle(ew.bulletVelocity) * ew.direction;

		return (int) limit(0, (factor * ((BINS - 1) / 2)) + ((BINS - 1) / 2), BINS - 1);
	}
	/**
	 * Calculates closet Surfable Wave
	 * @return surWave 
	 */
	private EnemyWave getClosestSurfableWave() {
		double closestDistance = 50000; // I juse use some very big number here
		EnemyWave surfWave = null;

		for (int x = 0; x < _enemyWaves.size(); x++) {
			EnemyWave ew = (EnemyWave) _enemyWaves.get(x);
			double distance = _myLocation.distance(ew.fireLocation) - ew.distanceTraveled;

			if (distance > ew.bulletVelocity && distance < closestDistance) {
				surfWave = ew;
				closestDistance = distance;
			}
		}

		return surfWave;
	}
	/**
	 * Updating waves
	 */
	private void updateWaves() {
		for (int x = 0; x < _enemyWaves.size(); x++) {
			EnemyWave ew = (EnemyWave) _enemyWaves.get(x);

			ew.distanceTraveled = (r.getTime() - ew.fireTime) * ew.bulletVelocity;
			if (ew.distanceTraveled > _myLocation.distance(ew.fireLocation) + 50) {
				_enemyWaves.remove(x);
				x--;
			}
		}
	}
	
	/**
	 * Calculates angle where we do not hit the wall
	 * @param botLocation
	 * @param angle
	 * @param orientation
	 * @return angle angle where we do not hit the wall.
	 */
	private double wallSmoothing(Point2D.Double botLocation, double angle, int orientation) {
		while (!_fieldRect.contains(project(botLocation, angle, WALL_STICK))) {
			angle += orientation * 0.05;
		}
		return angle;
	}
	/**
	 * Projecting our position in the angle and length given
	 * @param sourceLocation
	 * @param angle
	 * @param length
	 * @return projected Position
	 */
	private static Point2D.Double project(Point2D.Double sourceLocation, double angle, double length) {
		return new Point2D.Double(sourceLocation.x + Math.sin(angle) * length,
				sourceLocation.y + Math.cos(angle) * length);
	}
	/**
	 * Calculates absolute bearing between us and enemy location
	 * @param sourcelocation
	 * @param targetlocation
	 * @return absolute bearing 
	 */
	private static double absoluteBearing(Point2D.Double source, Point2D.Double target) {
		return Math.atan2(target.x - source.x, target.y - source.y);
	}
	/**
	 * 
	 * @param min
	 * @param value
	 * @param max
	 * @return limit
	 */
	private static double limit(double min, double value, double max) {
		return Math.max(min, Math.min(value, max));
	}
	/**
	 * calculates bullet velocity given desired power
	 * @param power
	 * @return BulletVelocity
	 */
	private static double bulletVelocity(double power) {
		return (20.0 - (3.0 * power));
	}
	/**
	 * Calculates maximum escape angle
	 * @param velocity
	 * @return maxEscapeAngle
	 */
	private static double maxEscapeAngle(double velocity) {
		return Math.asin(8.0 / velocity);
	}
	/**
	 * Moves robot in the desired angle to surf on enemy waves.
	 * @param robot Instance of our main class
	 * @param Angle
	 */
	private static void setBackAsFront(TeamRobot robot, double goAngle) {
		double angle = Utils.normalRelativeAngle(goAngle - robot.getHeadingRadians());
		if (Math.abs(angle) > (Math.PI / 2)) {
			if (angle < 0) {
				robot.setTurnRightRadians(Math.PI + angle);
			} else {
				robot.setTurnLeftRadians(Math.PI - angle);
			}
			robot.setBack(100);
		} else {
			if (angle < 0) {
				robot.setTurnLeftRadians(-1 * angle);
			} else {
				robot.setTurnRightRadians(angle);
			}
			robot.setAhead(100);
		}
	}
}