package group07;
import robocode.*;
import java.awt.Color;
import java.io.IOException;

public class Robot07 extends robocode.TeamRobot {
	/**
	 * run: Robot's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here
		setColors(Color.red,Color.blue,Color.red); // body,gun,radar
		//String[] teamm8 = getTeammates();
		
		
		
		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		 

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
	
	public void onScannedBullet(Bullet e)
	{
		
	}
	
	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		fire(1);
	}
	/**
	 * onMessageReceived: What to do when you receive a message
	 */
	public void onMessageReceived(MessageEvent event) {
		//Check if Message was from same type
		boolean same = isTeammate("Robot07");
		if(same) {
			
			
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