package group07;

import robocode.*;
import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Robot07 extends robocode.TeamRobot {
	/**
	 * run: Robot's default behavior
	 */
	private ArrayList<EnemyBot> enemies = new ArrayList<EnemyBot>();
	private ArrayList<Ally> allies = new ArrayList<Ally>();
	private AdvancedEnemyBot target;
	double angleTurret;
	//g�r upp varje efter varje tick d� scan ej fokuserad
	private int lastScan = 0;
	//bytas till -1 om man vill �ka bakl�nges
	private int moveDirection = 1;

	public void run() {
		// Initialization of the robot should be put here
		setColors(Color.red, Color.blue, Color.red); // body,gun,radar
		// adding allies
		String[] teamm8 = getTeammates();
		if (teamm8 != null) {
			for (int i = 0; i < teamm8.length; i++) {
				allies.add(new Ally(teamm8[i]));
			}
		}

		//ser till att alla delar kan rotera individuellt
		setAdjustRadarForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustGunForRobotTurn(true);

		setTurnRadarRight(360);
		// Robot main loop
		while (true) {
			//flyttar vapnet
			doMoveGun();
			//flyttar roboten
			doMoveRobot();
			// Kollar om scannern har tappat fokus
			lastScan++;
			if (lastScan % 5 == 0) {
				target.reset();
				setTurnRadarRight(360);
			}

			//beh�vs f�r att alla set commands ska k�ra
			execute();
		}
	}
	
	//Flyttar vapnet om man har en target
	private void doMoveGun() {
		if(!target.none()) {
			setTurnGunRight(getHeading() + target.getBearing() - getGunHeading());
			doShootGun();
		}
	}
	
	//Skjuter med en viss kraft
	private void doShootGun() {
		if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 5 && target.getDistance() < 500)
		{
			setFire(Math.min(400 / target.getDistance(), 3));
		}		
	}


	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		target = new AdvancedEnemyBot(e,this);
		radarFollowTarget();
		// check if scannedRobot already exists, else adds it.
		// under progress
		for (int i = 0; i < enemies.size(); i++) {
			if (!e.equals(enemies.get(i))) {
				enemies.add(new EnemyBot(e));
				// notifies Allies(Robot07) of new Enemy
				ArrayList<Serializable> msg = new ArrayList<Serializable>();
				msg.add("1");
				msg.add((Serializable) enemies.get(i));
				try {
					sendMessage("Robot07", msg);
				} catch (IOException error) {
					// TODO Auto-generated catch block
				}
				break;
			}
		}

	}

	//F�ljer radarn p� target
	public void radarFollowTarget()
	{
		double d = getHeading() - getRadarHeading() + target.getBearing();
		setTurnRadarRight(d);	
		lastScan = 0;
	}

	/**
	 * onMessageReceived: What to do when you receive a message
	 */
	public void onMessageReceived(MessageEvent e) {
		@SuppressWarnings("unchecked")
		ArrayList<Serializable> msg = (ArrayList<Serializable>) e.getMessage();
		// Check if Message was from same type
		boolean m_Same = e.getSender().contains("Robot07");

		//checks is message was of type 1(EnemyBot)
		if(m_Same && (int)msg.get(1) == 1) {
			//TODO update local enemies list
		}
		else if (m_Same && (int) msg.get(1) == 2) {
			// remove dead robot see onDeath()
		} else {
			
		}

	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
	}

	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {

	}

	public void onDeath(RobotDeathEvent event) {
		ArrayList<Serializable> msg = new ArrayList<Serializable>();
		msg.add("2");
		msg.add((Serializable)this); //Vet inte om detta funkar
		try {
			sendMessage("Robot07", msg);
		} catch (IOException error) {
			// TODO Auto-generated catch block
		}
	}
	
	public void onRobotDeath(RobotDeathEvent e) {
		if (e.getName().equals(target.getName())) {
			target.reset();
		}
	}

	private void doMoveRobot() {
		double degreeCloser = 0;
		if (target.getDistance() > 200) {
			degreeCloser = 10;
		} else {
			degreeCloser = 0;
		}
		setTurnRight(target.getBearing() + 90 - (degreeCloser * moveDirection));

		// bytar riktning om vi stannat eller det g�tt 20 ticks
		if (getVelocity() == 0 || getTime() % 20 == 0) {
			moveDirection *= -1;
			setAhead(100 * moveDirection);
		}
	}
}
