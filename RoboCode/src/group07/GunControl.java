package group07;

import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class GunControl {
	private MrRobot robot;
	private EnemyBot target;
	private AllyTracker ally;

	public GunControl(AllyTracker ally) {
		this.ally = ally;
	}

	public boolean takeShot(MrRobot robot, EnemyBot target) {
		this.robot = robot;
		this.target = target;
		return (distance() && target() && lowEnergy() && friendlyFire());
	}

	private boolean distance() {
		return robot.getGunHeat() == 0 && Math.abs(robot.getGunTurnRemaining()) < 2 && target.getDistance() < 900;
	}

	private boolean target() {
		return robot.getRadar().gotFocus();

	}

	public boolean lowEnergy() {
		return robot.getEnergy() > 1;
	}

	private boolean friendlyFire() {
		if(target.getName().toLowerCase().contains("rut") || target.getName().toLowerCase().contains("rain"))
		{
			return true;
		}
		for (Ally k : ally.getAllyListWithoutOurself()) {
			double distance = MathUtils.distance(robot.getX(), robot.getY(), k.getX(), k.getY());
			double triDistance = Math.min(200, target.getDistance());
			double angle = Math.toDegrees(Math.atan2(k.getX() - robot.getX(), k.getY() - robot.getY()));
			double heading = robot.getGunHeading();
			if (heading >= 180) {
				heading -= 360;
			}
			double bearing = angle - heading;
			if (bearing >= 180) {
				bearing -= 360;
			}
			if ((bearing > -20 && bearing < 20) && (distance <  triDistance)) {
				return false;
			}
		}
		return true;
	}

}
