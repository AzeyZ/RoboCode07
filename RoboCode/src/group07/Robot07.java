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
	private ArrayList<ScannedRobotEvent> enemies = new ArrayList<ScannedRobotEvent>();
	private ArrayList<Ally> allies = new ArrayList<Ally>();
	double angleTurret;

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


		// Robot main loop
		while (true) {
			// Replace the next 4 lines with any behavior you would like

			turnRadarRight(360);
			ahead(100);
			back(100);

		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {

		// test behavior
		stop();
		setTurnGunRight(getRadarHeading() - getGunHeading());
		fire(1);
		turnRight(e.getBearing() - 90);
		setTurnRadarRight(getHeading() - getRadarHeading() + e.getBearing());

		// check if scannedRobot already exists, else adds it.
		for (int i = 0; i < enemies.size(); i++) {
			if (!e.equals(enemies.get(i))) {
				enemies.add(e);
				// notifies Allies(Robot07) of new Enemy
				ArrayList<Serializable> msg = new ArrayList<Serializable>();
				msg.add("1");
				msg.add(e);
				try {
					sendMessage("Robot07", msg);
				} catch (IOException error) {
					// TODO Auto-generated catch block
				}

			}
		}

	}

	/**
	 * onMessageReceived: What to do when you receive a message
	 */
	public void onMessageReceived(MessageEvent e) {
		@SuppressWarnings("unchecked")
		ArrayList<Serializable> msg = (ArrayList<Serializable>) e.getMessage();
		// Check if Message was from same type
		boolean m_Same = e.getSender().contains("Robot07");

		//checks is message was of type 1(scannedRobotEvent)
		if(m_Same && (int)msg.get(1) == 1) {
			//creates local copy of ScannedRobot
			ScannedRobotEvent e2 = (ScannedRobotEvent)msg.get(2);
			//Updates enemies list
			for(int i = 0; i<enemies.size(); i++) {
				if(e2.equals(enemies.get(i))) {
					if(e2.getTime() > enemies.get(i).getTime()) {

						enemies.remove(i);
						enemies.add(e2);
						break;
					}
				}
			}
			if (!enemies.contains(e2)) {
				enemies.add(e2);
			}

		} else if (m_Same && (int) msg.get(1) == 2) {
			// remove dead robot see onDeath()
		} else {

		}

	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}

	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like

		back(20);
	}

	public void onDeath(RobotDeathEvent event) {
		ArrayList<Serializable> msg = new ArrayList<Serializable>();
		msg.add("2");
		// msg.add();
		try {
			sendMessage("Robot07", msg);
		} catch (IOException error) {
			// TODO Auto-generated catch block
		}
	}

}
