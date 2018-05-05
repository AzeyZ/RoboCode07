package group07;

import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class GunControl {
	private Robot07 robot;
	private TargetEnemyBot target;

	public GunControl() {

	}

	public boolean takeShot(Robot07 robot, TargetEnemyBot target) {
		this.robot = robot;
		this.target = target;
		return (distance() && target() && friendlyFire());
	}

	private boolean distance() {
		return robot.getGunHeat() == 0 && Math.abs(robot.getGunTurnRemaining()) < 2 && target.getDistance() < 800;
	}

	private boolean target() {
		return robot.getRadar().gotFocus();

	}

	private boolean friendlyFire() {
		Arc2D.Double arc = new Arc2D.Double(robot.getX(), robot.getY(), target.getDistance(), target.getDistance(),
				robot.getGunHeading()+160, 40, Arc2D.PIE);
		
		//ARCEN BLIR INTE RIKTIGT SOM JAG TÄNKER NÅGOT ÄR FEL
		
		
		for (int i = 0; i < robot.getAllies().size(); i++) {
			if (arc.contains(new Point2D.Double(robot.getAllies().get(i).getX(), robot.getAllies().get(i).getY()))
					&& (!robot.getAllies().get(i).getName().equals(robot.getName()))) {
				System.out.println("A MATE IS IN MY SIGHT FORSENT"  + "\n"+ "*******************************");
				return false;
			}
		}
		return true;
	}

}
