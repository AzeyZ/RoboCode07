package group07;
import robocode.*;
import java.awt.Color;
import java.io.IOException;
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
		setColors(Color.red,Color.blue,Color.red); // body,gun,radar
		//adding allies
		String[] teamm8 = getTeammates();
		for(int i = 0; i<teamm8.length;i++) {
			allies.add(new Ally(teamm8[i]));
		}
		
		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
			turnGunRight(360);
			ahead(100);
			back(100);
			
//			try {
//				sendMessage("Robot07","hejsan");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		}
	}
	
	
	
	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		//check if scannedRobot already exists, else adds it.
		for(int i = 0; i<enemies.size(); i++) {
			if(!e.equals(enemies.get(i))) {
				enemies.add(e);
			}
		}

	}
	/**
	 * onMessageReceived: What to do when you receive a message
	 */
	public void onMessageReceived(MessageEvent event) {
		//Check if Message was from same type
		boolean m_Same = event.getSender() == "Robot07";
		if(m_Same) {
			//follows our com protocol
		}
		else {
			//doesn't
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
}