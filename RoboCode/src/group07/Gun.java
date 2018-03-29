package group07;

public class Gun {

	private Robot07 robot;
	private TargetEnemyBot target;
	private GunControl gunControl = new GunControl();
	
	public Gun(Robot07 robot) {
		this.robot = robot;
	}
	
	public void update(TargetEnemyBot target) {
		this.target = target;
	}
	
	// Aim straight at the target
	public void aim() {
		 //kraften beroende på hur långt ifrån vi är
		 double firePower = Math.min(400 / target.getDistance(), 3);
		 // hastigheten på skottet
		 double bulletSpeed = 20 - firePower * 3;
		 // avstånd = hastighet * tid
		 long time = (long)(target.getDistance() / bulletSpeed);
		 
		 
		 
		// calculate gun turn to predicted x,y location
		double futureX = target.getFutureX(time);
		double futureY = target.getFutureY(time);
		
		double absDeg = MathUtils.absoluteBearing(robot.getX(), robot.getY(), futureX, futureY);
		// turn the gun to the predicted x,y location
		robot.setTurnGunRight(MathUtils.normalizeBearing(absDeg - robot.getGunHeading()));
	}
	
	// Fires with a certain firepower once we have rotated the gun
	public void fire() {
		if(gunControl.takeShot(robot, target)) {
			robot.setFire(Math.min(400 / target.getDistance(), 3));
		}
	}
}
