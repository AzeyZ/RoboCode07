package group07;

import java.awt.geom.Point2D;
import java.util.ArrayList;

// method antigrav move is heavily inspired by Alisdair Owens from IBM
// https://www.ibm.com/developerworks/library/j-antigrav/index.html

public class RobotMovement {
	private int moveDirection = 1;
	private double velocity;
	private long time;
	private double degreeCloser = 0;
	private TargetEnemyBot target;
	private Robot07 robot;
	private ArrayList<GravPoint> gravpoints = new ArrayList<GravPoint>();
	private MovementModeSwitcher mode;
	private EnemyTracker enemyTracker;
	private static int warning;
	private static double lastX;
	private static double lastY;

	public RobotMovement(Robot07 robot, MovementModeSwitcher mode, EnemyTracker enemyTracker) {
		this.robot = robot;
		this.mode = mode;
		this.enemyTracker = enemyTracker;
	}

	// Uppdaterar
	public void update(TargetEnemyBot target) {
		this.target = target;
		this.velocity = robot.getVelocity();
		this.time = robot.getTime();
	}

	// Sköter flyttandet
	public void move() {
		if (!isTargetNull()) {
			robot.setTurnRight(rotation());
			if (hasStopped()) {
				robot.setAhead(ahead());
			}
		}
	}

	// Kollar om target är null
	public boolean isTargetNull() {
		if (target == null) {
			return true;
		} else {
			return false;
		}
	}

	// Beräknar hur vi ska roteras
	public double rotation() {
		if (target.getDistance() > 50) {
			degreeCloser = 30;
		} else {
			degreeCloser = 0;
		}
		return (target.getBearing() + 90 - (degreeCloser * moveDirection));
	}

	// Kollar om vi stannat eller det gått 20 ticks
	public boolean hasStopped() {
		return velocity == 0 || time % 20 == 0;
	}

	// Hur långt vi ska gå i vilken riktning
	public double ahead() {
		moveDirection *= -1;
		return (100 * moveDirection);
	}

	void antiGravMove(EnemyTracker track) {
//		System.out.println("grav engaged");
		// Recommend using force = strength/Math.pow(distance,1.5) for calculating the
		// force of the intermediate points on your bot.
		// https://www.ibm.com/developerworks/library/j-antigrav/index.html
		double xforce = 0;
		double yforce = 0;
		double force;
		double ang;
		GravPoint p;
		gravpoints.clear();

		if ((MathUtils.distance(robot.getX(), robot.getY(), lastX, lastY)) < 15) {
			warning++;
			if (warning > 7) {
				move();
				mode.RNDMove();
			}
		} else {
			warning = 0;

			for (int i = 0; i < enemyTracker.getLivingEnemies().size(); i++) {
				if (enemyTracker.getLivingEnemies().get(i).getDistance() < 200) {
					gravpoints.add(new GravPoint(enemyTracker.getLivingEnemies().get(i).getX(),
							enemyTracker.getLivingEnemies().get(i).getY(), -1000));

				}
				else if (enemyTracker.getLivingEnemies().get(i).getDistance() < 100) {
					gravpoints.add(new GravPoint(enemyTracker.getLivingEnemies().get(i).getX(),
							enemyTracker.getLivingEnemies().get(i).getY(), -5000));
				}

				if (track.getTarget().getDistance() > 400) {
					gravpoints.add(new GravPoint(track.getTarget().getX(), track.getTarget().getY(), 3000));
				} else if (track.getTarget().getDistance() < 150) {
					gravpoints.add(new GravPoint(track.getTarget().getX(), track.getTarget().getY(), 800));
				} else {
					gravpoints.add(new GravPoint(track.getTarget().getX(), track.getTarget().getY(), 1500));
				}

			}

			for (int i = 0; i < robot.getAllies().size(); i++) {
				if (!robot.getAllies().get(i).getName().equals(robot.getName())) {
					gravpoints.add(
							new GravPoint(robot.getAllies().get(i).getX(), robot.getAllies().get(i).getY(), -1000));
				}

			}

			for (int i = 0; i < gravpoints.size(); i++) {
				p = (GravPoint) gravpoints.get(i);
				// Calculate the total force from this point on us
				force = p.power / Math.pow(getRange(robot.getX(), robot.getY(), p.x, p.y), 2);
				// Find the bearing from the point to us
				// ang = Math.toRadians(MathUtils.normalizeBearing(Math.toDegrees(Math.PI/2 -
				// Math.atan2(robot.getY() - p.y, robot.getX() - p.x))));
				ang = normaliseBearing(Math.PI / 2 - Math.atan2(robot.getY() - p.y, robot.getX() - p.x));
				// Add the components of this force to the total force in their
				// respective directions
				xforce += Math.sin(ang) * force;
				yforce += Math.cos(ang) * force;
			}

			/**
			 * The following four lines add wall avoidance. They will only affect us if the
			 * bot is close to the walls due to the force from the walls decreasing at a
			 * power 3.
			 **/

			xforce += 5000
					/ Math.pow(getRange(robot.getX(), robot.getY(), robot.getBattleFieldWidth(), robot.getY()), 3);
			xforce -= 5000 / Math.pow(getRange(robot.getX(), robot.getY(), 0, robot.getY()), 3);
			yforce += 5000
					/ Math.pow(getRange(robot.getX(), robot.getY(), robot.getX(), robot.getBattleFieldHeight()), 3);
			yforce -= 5000 / Math.pow(getRange(robot.getX(), robot.getY(), robot.getX(), 0), 3);

			// Move in the direction of our resolved force.
			goTo(robot.getX() - xforce, robot.getY() - yforce);
		}
	}

	/** Move in the direction of an x and y coordinate **/
	void goTo(double x, double y) {
		double dist = 20;
		double angle = MathUtils.absoluteBearing(robot.getX(), robot.getY(), x, y);
		double r = turnTo(angle);
		robot.setAhead(dist * r);
		lastX = robot.getX();
		lastY = robot.getY();
	}

	/**
	 * Turns the shortest angle possible to come to a heading, then returns the
	 * direction the bot needs to move in.
	 **/
	int turnTo(double angle) {
		double ang;
		int dir;
		ang = MathUtils.normalizeBearing(robot.getHeading() - angle);
		if (ang > 90) {
			ang -= 180;
			dir = -1;
		} else if (ang < -90) {
			ang += 180;
			dir = -1;
		} else {
			dir = 1;
		}
		robot.setTurnLeft(ang);
		return dir;
	}

	// Returns the distance between two points**/
	double getRange(double x1, double y1, double x2, double y2) {
		double x = x2 - x1;
		double y = y2 - y1;
		double range = Math.sqrt(x * x + y * y);
		return range;
	}

	// if a bearing is not within the -pi to pi range, alters it to provide the
	// shortest angle
	double normaliseBearing(double ang) {
		if (ang > Math.PI)
			ang -= 2 * Math.PI;
		if (ang < -Math.PI)
			ang += 2 * Math.PI;
		return ang;
	}

	class GravPoint {
		public double x, y, power;

		public GravPoint(double pX, double pY, double pPower) {
			x = pX;
			y = pY;
			power = pPower;
		}
	}
}
