package group07;

import robocode.*;

public class TargetEnemyBot extends EnemyBot {
	private double x, y;
	private long time;

	public TargetEnemyBot(Robot07 MrRobot) {
		super(MrRobot);
	}

	public void update(double bearing, double distance, double energy, double heading, double velocity, long time,
			String name) {
		super.update(bearing, distance, energy, heading, velocity, time, name);

		double absBearingDeg = (MrRobot.getHeading() + bearing);
		if (absBearingDeg < 0) {
			absBearingDeg += 360;
		}

		x = MrRobot.getX() + Math.sin(Math.toRadians(absBearingDeg)) * distance;
		y = MrRobot.getY() + Math.cos(Math.toRadians(absBearingDeg)) * distance;
	}

//	public double getFutureX(double time) {
//		return MathUtils.getFutureLinearX(x, getHeading(), getVelocity(), time);
//	}
//
//	public double getFutureY(double time) {
//		return MathUtils.getFutureLinearY(y, getHeading(), getVelocity(), time);
//	}

//	public double getFutureDistance(Robot robot, double time) {
//		double d = MathUtils.absoluteBearing(robot.getX(), robot.getY(), getFutureX(time), getFutureY(time));
//		return d;
//	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public long getTime() {
		return time;
	}

}
