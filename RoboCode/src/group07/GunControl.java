package group07;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

public class GunControl {
	private Robot07 robot;
	private TargetEnemyBot target;

	public GunControl() {

	}

	public boolean takeShot(Robot07 robot, TargetEnemyBot target) {
		this.robot = robot;
		this.target = target;
		return (distance() && target());
	}

	private boolean distance() {
		return robot.getGunHeat() == 0 && Math.abs(robot.getGunTurnRemaining()) < 2 && target.getDistance() < 500;
	}

	private boolean target() {
		return robot.getRadar().gotFocus();

	}
	
	private boolean friendlyFire() {
		//Arc2D.Double arc = new Arc2D.Double(arg0, arg1, arg2, arg3, arg4, arg5, arg6)
	}

}
