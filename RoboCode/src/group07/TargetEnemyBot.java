package group07;

import robocode.*;

public class TargetEnemyBot extends EnemyBot {
	private double x, y;
	
	public TargetEnemyBot() {
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
		return MathUtils.getFutureLinearX(x, getHeading(), getVelocity(), time);
	}
	
	public double getFutureY(double time) {
		return MathUtils.getFutureLinearY(y, getHeading(), getVelocity(), time);
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
}
