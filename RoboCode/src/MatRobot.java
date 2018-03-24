

import group07.AdvancedEnemyBot;
import robocode.*;


// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

public class MatRobot extends AdvancedRobot
{
	//doesn't work anymore since changes in AdvancedEnemyBot constructor
	private AdvancedEnemyBot enemy = new AdvancedEnemyBot();
	private int lastScan = 0;
	
	private int moveDirection = 1;
	public void run() {

		setAdjustRadarForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustGunForRobotTurn(true);
		enemy.reset();

		setTurnRadarRight(360);
		while(true) {
			doMoveGun();
			doMoveRobot();
			
			//Kollar om scannern har tappat fokus
			lastScan++;
			if(lastScan % 5 == 0) {
				enemy.reset();
				setTurnRadarRight(360);
			}
			
			
			//Kör köade kommandon
			execute();
		}
	}
	
	private void doMoveRobot() {
		double degreeCloser = 0;
		if(enemy.getDistance() > 200)
		{
			degreeCloser = 10;
		} else {
			degreeCloser = 0;
		}
		setTurnRight(normalizeBearing(enemy.getBearing() + 90 - (degreeCloser * moveDirection)));

		// bytar riktning om vi stannat eller det gått 20 ticks
		if (getVelocity() == 0 || getTime() % 20 == 0)
		{
			moveDirection *= -1;
			setAhead(100 * moveDirection);
		}
	}
	
	private void doMoveGun() {
		if(!enemy.none()) {
			 //kraften beroende på hur långt ifrån vi är
			 double firePower = Math.min(500 / enemy.getDistance(), 3);
			 // hastigheten på skottet
			 double bulletSpeed = 20 - firePower * 3;
			 // avstånd = hastighet * tid
			 long time = (long)(enemy.getDistance() / bulletSpeed);
			 
			 
			 
			// calculate gun turn to predicted x,y location
			double futureX = enemy.getFutureX(time);
			double futureY = enemy.getFutureY(time);
			
			double absDeg = absoluteBearing(getX(), getY(), futureX, futureY);
			// turn the gun to the predicted x,y location
			setTurnGunRight(normalizeBearing(absDeg - getGunHeading()));
	
			doShootGun();
		}
	}
	
	private void doShootGun() {
		if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 5 && enemy.getDistance() < 500)
		{
			setFire(Math.min(400 / enemy.getDistance(), 3));
		}		
	}


	public void onScannedRobot(ScannedRobotEvent e) {
		if (enemy.none() || e.getDistance() < enemy.getDistance() - 100 || e.getName().equals(enemy.getName())) {
				enemy.update(e, this);
			}
		followRadar();
	}

	public void followRadar()
	{
		double d = getHeading() - getRadarHeading() + enemy.getBearing();
		setTurnRadarRight(d);	
		lastScan = 0;
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
	
	double normalizeBearing(double angle) {
		while (angle >  180) angle -= 360;
		while (angle < -180) angle += 360;
		return angle;
	}
	
	// computes the absolute bearing between two points
	double absoluteBearing(double x1, double y1, double x2, double y2) {
		double xo = x2-x1;
		double yo = y2-y1;
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
}
