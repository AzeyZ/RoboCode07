package group07;

public class RobotMovement {
	private int moveDirection;
	private double velocity;
	private long time;
	private double degreeCloser;
	private AdvancedEnemyBot target;

	public RobotMovement() {

	}

	public boolean isTargetNull(AdvancedEnemyBot target) {
		if (target == null) {
			return true;
		}
		else
		{
			this.target = target;
			return false;
		}
	}

	public double doMoveRobot(int moveDirection, double velocity, long time) {
		if (target == null) {
			return 0;
		}
		degreeCloser = 0;
		this.moveDirection = moveDirection;
		this.velocity = velocity;
		this.time = time;
		

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
// setTurnRight(target.getBearing() + 90 - (degreeCloser * moveDirection));
//
//// bytar riktning om vi stannat eller det g�tt 20 ticks
// if (velocity == 0 || time % 20 == 0) {
// moveDirection *= -1;
// setAhead(100 * moveDirection);
// }