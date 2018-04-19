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
		reset();
	}

	public void update(double bearing, double distance, double energy, double heading, double velocity, long time, String name) {
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

	public void reset() {
		bearing = 0.0;
		distance = 0.0;
		energy = 0.0;
		heading = 0.0;
		velocity = 0.0;
		tick = 0;
		name = "";

	}

	public boolean none() {
		if (name == "") {
			return true;
		} else {
			return false;
		}
	}

	public double getBearing() {
		return bearing;
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

}
