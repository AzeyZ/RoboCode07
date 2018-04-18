package group07;

import robocode.*;

public class EnemyBot {
	public static final int LEADER_BOT = 0;
	public static final int DROID = 1;
	public static final int NORMAL_BOT = 2;
	private ScannedRobotEvent e;
	private Robot07 MrRobot;
	private double bearing, distance, energy, heading, velocity;
	private long tick;
	private String name;
	private int type;
	private boolean scanned = true;

	public EnemyBot(Robot07 MrRobot) {
		this.MrRobot = MrRobot;
		reset();
	}

	public void update(ScannedRobotEvent e) {
		this.e = e;
		bearing = e.getBearing();
		distance = e.getDistance();
		energy = e.getEnergy();
		heading = e.getHeading();
		velocity = e.getVelocity();
		name = e.getName();
		tick = MrRobot.getTime();
		// first time enemy is scanned gives its type 0 = leader, 1 = droid, 2 = normal
		if (scanned) {
			scanned = false;
			if (e.getEnergy() <= 105) {
				type = NORMAL_BOT;
			} else if (e.getEnergy() < 130) {
				type = DROID;
			} else {
				type = LEADER_BOT;
			}
		}
	}

	public void reset() {
		e = null;
		bearing = 0.0;
		distance = 0.0;
		energy = 0.0;
		heading = 0.0;
		velocity = 0.0;
		name = "";

	}

	public boolean none() {
		if (name == "") {
			return true;
		} else {
			return false;
		}
	}

	public ScannedRobotEvent getEvent() {
		return e;
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
