package group07;

public class Gun {

	private Robot07 robot;
	private EnemyBot target;
	
	public Gun(Robot07 robot) {
		this.robot = robot;
	}
	
	public void update(EnemyBot target) {
		this.target = target;
	}
	
	// Aim straight at the target
	public void aim() {
		robot.setTurnGunRight(robot.normalizeBearing(robot.getHeading() + target.getBearing() - robot.getGunHeading()));
	}
	
	// Fires with a certain firepower once we have rotated the gun
	public void fire() {
		if (robot.getGunHeat() == 0 && Math.abs(robot.getGunTurnRemaining()) < 5 && target.getDistance() < 500) {
			robot.setFire(Math.min(400 / target.getDistance(), 3));
		}
	}
}
