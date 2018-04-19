package group07;

import java.util.ArrayList;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.util.Utils;
public class Gun {

	private Robot07 robot;
	private TargetEnemyBot target;
	private GunControl gunControl = new GunControl();
	// WaveBullet list
	private ArrayList<WaveBullet> waves = new ArrayList<WaveBullet>();
	private int[][] stats = new int[13][31];
	private int direction = 1;


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
	
	//Wave functions
	public void Wave (EnemyTracker track) {
		
		//double absBearing = Math.PI/180*MathUtils.absoluteBearing(robot.getX(), robot.getY(), track.getTarget().getX(), track.getTarget().getY());
		double absBearing = track.getTarget().getBearingRadians() + robot.getHeadingRadians();
		// find our enemy's location:
		double ex = robot.getX() + Math.sin(absBearing) * track.getTarget().getDistance();
		double ey = robot.getY() + Math.cos(absBearing) * track.getTarget().getDistance();

		// Let's process the waves now:
		for (int i=0; i < waves.size(); i++)
		{
			WaveBullet currentWave = (WaveBullet)waves.get(i);
			if (currentWave.checkHit(ex, ey, robot.getTime()))
			{
				waves.remove(currentWave);
				i--;
			}
		}
		double power = Math.min(400 / target.getDistance(), 3);

		if (track.getTarget().getVelocity() != 0)
		{
			if (Math.sin(track.getTarget().getHeading()*Math.PI/180-absBearing)*track.getTarget().getVelocity() < 0)
				direction = -1;
			else
				direction = 1;
		}
		int[] currentStats = stats[(int)(track.getTarget().getDistance() / 100)]; 

		WaveBullet newWave = new WaveBullet(robot.getX(), robot.getY(), absBearing, power, direction, robot.getTime(), currentStats);
	
	       if (robot.setFireBullet(Math.min(400 / target.getDistance(), 3)) != null) {
               waves.add(newWave);
	       }

		int bestindex = 15;
		for (int i=0; i<31; i++) {
			if (currentStats[bestindex] < currentStats[i]) {
				bestindex = i;
			}
		}
		double guessfactor = (double)(bestindex - (stats.length - 1) / 2)/((stats.length - 1) / 2);
		double angleOffset = direction * guessfactor * newWave.maxEscapeAngle();
		double gunAdjust = Utils.normalRelativeAngle(absBearing - robot.getGunHeadingRadians() + angleOffset);
		robot.setTurnGunRightRadians(gunAdjust);
		if (robot.getGunHeat() == 0 && gunAdjust < Math.atan2(9, track.getTarget().getDistance()) && robot.setFireBullet(Math.min(400 / target.getDistance(), 3)) != null) {
			
		}
	}
}
