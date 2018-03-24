package group07;

import robocode.*;

public class RobotMovement {
	private int moveDirection;
	private double velocity;
	private long time;
	private double degreeCloser;
	private TargetEnemyBot target;
	private TeamRobot robot07;

	public RobotMovement() {

	}
	
	public void doMoveRobot(TargetEnemyBot target, int moveDirection, TeamRobot robot) {
		this.degreeCloser = 0;
		this.moveDirection = moveDirection;
		this.velocity = robot.getVelocity();
		this.time = robot.getTime();
		this.robot07 = robot;
		if (!(isTargetNull(target))) {
			robot07.setTurnRight(doMoveRobot());
			if (hasStopped()) {
				robot07.ahead(setAhead());
			}
		}
	}
	public boolean isTargetNull(TargetEnemyBot target) {
		if (target == null) {
			return true;
		}
		else
		{
			this.target = target;
			return false;
		}
	}

	public double doMoveRobot() {
		
		

		if (target.getDistance() > 200) {
			degreeCloser = 10;
		} else {
			degreeCloser = 0;
		}
		return (target.getBearing() + 90 - (degreeCloser * moveDirection));
	}

	public boolean hasStopped() {
		return velocity == 0 || time % 20 == 0;
	}

	public double setAhead() {
		moveDirection *= -1;
		return (100 * moveDirection);
	}
}
