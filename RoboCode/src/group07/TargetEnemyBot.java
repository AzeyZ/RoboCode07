package group07;

import robocode.*;

public class TargetEnemyBot extends EnemyBot {
	private double x, y;
	private long time;
	
	public TargetEnemyBot(Robot07 MrRobot) {
		super(MrRobot);
		reset();
	}
	

	public void reset() {
		super.reset();
		x = 0;
		y = 0;
		time = 0;
	}
	
	public void update(ScannedRobotEvent e, Robot robot) {
		super.update(e);
		
		e.getTime();
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
	
	public double getFutureDistance(Robot robot, double time) {
		double d = MathUtils.absoluteBearing(robot.getX(), robot.getY(), getFutureX(time), getFutureY(time));
		return d;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	public long getTime()
	{
		return time;
	}
	
}
