package group07;

public class Radar {
	private Robot07 robot;
	private EnemyBot target;
	private int radarDirection;
	private int wiggle;
	private int lastScan;
	private double turn;

	public Radar(Robot07 robot) {
		this.robot = robot;
		radarDirection = 1;
		wiggle = 15;
		lastScan = 0;
	}

	// Gives the radar a target
	public void update(EnemyBot target) {
		this.target = target;
	}

	
	public void scan() {
		turn = MathUtils.normalizeBearing(robot.getHeading() - robot.getRadarHeading() + target.getBearing());
		double changeDirection = 1;
		if(turn >= 0 ) {
			changeDirection = 1;
		} else {
			changeDirection = -1;
		}
		// Lost focus then rotate radar
		lastScan++;
		if (lastScan % 5 == 0) {
			// target.reset();
			robot.setTurnRadarRight(360 * changeDirection);
		}
		// Focus the existing target
		if (robot.getTime() - target.getTick() < 5) {
			scanTarget();
		}
	}

	
	// Scan the target
	public void scanTarget() {
		turn += wiggle * radarDirection;
		robot.setTurnRadarRight(MathUtils.normalizeBearing(turn));
		radarDirection *= -1;
		lastScan = 0;
	}

	public boolean gotFocus() {
		return lastScan <= 4;
	}
}
