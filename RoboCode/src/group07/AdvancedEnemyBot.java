package group07;

import robocode.*;

public class AdvancedEnemyBot extends EnemyBot {
	private double x, y;
	
	public AdvancedEnemyBot() {
		reset();
	}

	public void reset() {
		super.reset();
		x = 0;
		y = 0;
	}
	
	public void update(ScannedRobotEvent e, Robot robot) {
		super.update(e);
		
		double absBearingDeg = (robot.getHeading() + e.getBearing());
		if (absBearingDeg < 0) { absBearingDeg += 360; }
		
		x = robot.getX() + Math.sin(Math.toRadians(absBearingDeg)) * e.getDistance();
		y = robot.getY() + Math.cos(Math.toRadians(absBearingDeg)) * e.getDistance();
	}
	
	public double getFutureX(double time) {
		return x + Math.sin(Math.toRadians(getHeading())) * getVelocity() * time;
	}
	
	public double getFutureY(double time) {
		return y + Math.cos(Math.toRadians(getHeading())) * getVelocity() * time;
	}
}
