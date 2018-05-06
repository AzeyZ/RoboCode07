package group07;

import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class GunControl {
	private MrRobot robot;
	private EnemyBot target;

	public GunControl() {

	}

	public boolean takeShot(MrRobot robot, EnemyBot target) {
		this.robot = robot;
		this.target = target;
		return (distance() && target() && lowEnergy());
	}

	private boolean distance() {
		return robot.getGunHeat() == 0 && Math.abs(robot.getGunTurnRemaining()) < 2 && target.getDistance() < 600;
	}

	private boolean target() {
		return robot.getRadar().gotFocus();

	}
	
	public boolean lowEnergy() {
		return robot.getEnergy() > 1;
	}

//	private boolean friendlyFire() {
//		Arc2D.Double arc = new Arc2D.Double(robot.getX(), robot.getY(), target.getDistance(), target.getDistance(),
//				robot.getGunHeading()+160, 40, Arc2D.PIE);
//		
//		//ARCEN BLIR INTE RIKTIGT SOM JAG T�NKER N�GOT �R FEL
//		
//		
//		for (int i = 0; i < robot.getAllies().size(); i++) {
//			if (arc.contains(new Point2D.Double(robot.getAllies().get(i).getX(), robot.getAllies().get(i).getY()))
//					&& (!robot.getAllies().get(i).getName().equals(robot.getName()))) {
//				return false;
//			}
//		}
//		return true;
//	}

}
