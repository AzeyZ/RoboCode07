

import java.awt.Color;

import group07.AdvancedEnemyBot;
import robocode.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

public class polyBot extends AdvancedRobot {

	private AdvancedEnemyBot target = new AdvancedEnemyBot();
	private Radar radar = new Radar();
	private Gun gun = new Gun();
	private Tank tank = new Tank();

	private int moveDirection = 1;

	public void run() {

		RobotPart[] parts = new RobotPart[3];
		parts[0] = radar;
		parts[1] = gun;
		parts[2] = tank;

		// initialize each part
		for (int i = 0; i < parts.length; i++) {
			// behold, the magic of polymorphism
			parts[i].init();
		}

		// iterate through each part, moving them as we go
		for (int i = 0; true; i = (i + 1) % parts.length) {
			// polymorphism galore!
			parts[i].move();
			execute();
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		if (radar.shouldScan(e))
			target.update(e, this);
	}

	public void onRobotDeath(RobotDeathEvent e) {
		if (radar.wasTracking(e))
			target.reset();
	}

	double normalizeBearing(double angle) {
		while (angle > 180)
			angle -= 360;
		while (angle < -180)
			angle += 360;
		return angle;
	}

	// computes the absolute bearing between two points
	double absoluteBearing(double x1, double y1, double x2, double y2) {
		double xo = x2 - x1;
		double yo = y2 - y1;
		double hyp = Math.sqrt(Math.pow(xo, 2) + Math.pow(yo, 2));
		double arcSin = Math.toDegrees(Math.asin(xo / hyp));
		double bearing = 0;

		if (xo > 0 && yo > 0) { // both pos: lower-Left
			bearing = arcSin;
		} else if (xo < 0 && yo > 0) { // x neg, y pos: lower-right
			bearing = 360 + arcSin; // arcsin is negative here, actuall 360 - ang
		} else if (xo > 0 && yo < 0) { // x pos, y neg: upper-left
			bearing = 180 - arcSin;
		} else if (xo < 0 && yo < 0) { // both neg: upper-right
			bearing = 180 - arcSin; // arcsin is negative here, actually 180 + ang
		}

		return bearing;
	}
	
	//-------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------

	public interface RobotPart {
		public void init();

		public void move();
	}

	public class Radar implements RobotPart {

		private int radarDirection;

		public void init() {
			setAdjustRadarForGunTurn(true);
			radarDirection = 1;
		}

		public void move() {
			double turn = getHeading() - getRadarHeading() + target.getBearing();
			turn += 30 * radarDirection;
			setTurnRadarRight(normalizeBearing(turn));
			radarDirection *= -1;
		}

		boolean shouldScan(ScannedRobotEvent e) {
			// track if we have no target, the one we found is significantly
			// closer, or we scanned the one we've been tracking.
			return (target.none() || e.getDistance() < target.getDistance() - 70 || e.getName().equals(target.getName()));
		}

		boolean wasTracking(RobotDeathEvent e) {
			return (e.getName().equals(target.getName()));
		}
	}

	public class Gun implements RobotPart {
		public void init() {
			setAdjustGunForRobotTurn(true);
		}

		public void move() {
			setTurnGunRight(getHeading() + target.getBearing() - getGunHeading());
		}
	}

	public class Tank implements RobotPart {
		public void init() {
			setColors(Color.black, Color.black, Color.black);
		}

		public void move() {
			double degreeCloser = 0;
			if (target.getDistance() > 200) {
				degreeCloser = 10;
			} else {
				degreeCloser = 0;
			}
			setTurnRight(normalizeBearing(target.getBearing() + 90 - (degreeCloser * moveDirection)));

			// bytar riktning om vi stannat eller det gått 20 ticks
			if (getVelocity() == 0 || getTime() % 20 == 0) {
				moveDirection *= -1;
				setAhead(100 * moveDirection);
			}
		}
	}
}
