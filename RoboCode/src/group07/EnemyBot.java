package group07;

import robocode.*;

public class EnemyBot {
	private ScannedRobotEvent e;

	private double bearing, distance, energy, heading, velocity;
	private String name;
	private int type;
	private boolean scanned = true;
	public EnemyBot() {
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
		//first time enemy is scanned gives its type 0 = leader, 1 = droid, 2 = normal
		if(scanned) {
			scanned = false;
			if(e.getEnergy()<=105) {
				type = 2;
			}
			else if(e.getEnergy()<130) {
				type = 1;
			}
			else {
				type = 0;
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
		if(name == "") {
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
	

}
