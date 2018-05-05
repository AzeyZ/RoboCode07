package group07;

import robocode.*;

public class EnemyBot {
	public static final int LEADER_BOT = 0;
	public static final int DROID = 1;
	public static final int NORMAL_BOT = 2;

	protected Robot07 MrRobot;
	private double bearing, distance, energy, heading, velocity;
	private long tick;
	private String name;
	private int type;
	private boolean scanned = true;

	public EnemyBot(Robot07 MrRobot) {
		this.MrRobot = MrRobot;

	}

	public void update(double bearing, double distance, double energy, double heading, double velocity, long time,
			String name) {
		this.bearing = bearing;
		this.distance = distance;
		this.energy = energy;
		this.heading = heading;
		this.velocity = velocity;
		this.tick = time;
		this.name = name;
		// first time enemy is scanned gives its type 0 = leader, 1 = droid, 2 = normal
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

	public boolean none() {
		if (name.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public double getBearing() {
		return bearing;
	}

	public double getBearingRadians() {
		return Math.toRadians(bearing);
	}

	public double getDistance() {
		return distance;
	}

	public double getEnergy() {
		return energy;
	}

	public double getHeading() {
		return heading;
	}

	public double getVelocity() {
		return velocity;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public long getTick() {
		return tick;
	}

	public void setEnergy(int Energy) {
		this.energy = Energy;

	}

	public double msgDistance() {

		double absBearingDeg = (MrRobot.getHeading() + bearing);
		if (absBearingDeg < 0) {
			absBearingDeg += 360;
		}

		double x = MrRobot.getX() + Math.sin(Math.toRadians(absBearingDeg)) * distance;
		double y = MrRobot.getX() + Math.sin(Math.toRadians(absBearingDeg)) * distance;

		return MathUtils.distance(0, 0, x, y);
	}

	public double getX() {
		double absBearingDeg = (MrRobot.getHeading() + bearing);
		if (absBearingDeg < 0) {
			absBearingDeg += 360;
		}
		return MrRobot.getX() + Math.sin(Math.toRadians(absBearingDeg)) * distance;
	}

	public double getY() {
		double absBearingDeg = (MrRobot.getHeading() + bearing);
		if (absBearingDeg < 0) {
			absBearingDeg += 360;
		}
		return MrRobot.getY() + Math.cos(Math.toRadians(absBearingDeg)) * distance;
	}

}
