package group07;

public class RobotMovement {
	private int moveDirection = 1;
	private double velocity;
	private long time;
	private double degreeCloser = 0;
	private TargetEnemyBot target;
	private Robot07 robot;

	public RobotMovement(Robot07 robot) {
		this.robot = robot;
	}
	
	// Uppdaterar
	public void update(TargetEnemyBot target) {
		this.target = target;
		this.velocity = robot.getVelocity();
		this.time = robot.getTime();
	}
	
	// Sk�ter flyttandet
	public void move() {
		if (!isTargetNull()) {
			robot.setTurnRight(rotation());
			if (hasStopped()) {
				robot.setAhead(ahead());
			}
		}
	}
	
	// Kollar om target �r null
	public boolean isTargetNull() {
		if (target == null) {
			return true;
		}
		else
		{
			return false;
		}
	}

	// Ber�knar hur vi ska roteras
	public double rotation() {
		if (target.getDistance() > 200) {
			degreeCloser = 10;
		} else {
			degreeCloser = 0;
		}
		return (target.getBearing() + 90 - (degreeCloser * moveDirection));
	}

	// Kollar om vi stannat eller det g�tt 20 ticks
	public boolean hasStopped() {
		return velocity == 0 || time % 20 == 0;
	}

	// Hur l�ngt vi ska g� i vilken riktning
	public double ahead() {
		moveDirection *= -1;
		return (100 * moveDirection);
	}
}
