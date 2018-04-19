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
	private int[] currentStats;
	private WaveBullet newWave;
	private double absBearing = 0;
	int direction = 1;


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
	public void Wave (EnemyTracker track , TeamRobot r) {
		// Enemy absolute bearing, you can use your one if you already declare it.
		double absBearing = r.getHeadingRadians() + track.getTarget().getBearing()*Math.PI/180;

		// find our enemy's location:
		double ex = r.getX() + Math.sin(absBearing) * track.getTarget().getDistance();
		double ey = r.getY() + Math.cos(absBearing) * track.getTarget().getDistance();

		// Let's process the waves now:
		for (int i=0; i < waves.size(); i++)
		{
			WaveBullet currentWave = (WaveBullet)waves.get(i);
			if (currentWave.checkHit(ex, ey, r.getTime()))
			{
				waves.remove(currentWave);
				i--;
			}
		}
		double power = Math.min(400 / target.getDistance(), 3);

		// don't try to figure out the direction they're moving 
		// they're not moving, just use the direction we had before
		if (track.getTarget().getVelocity() != 0)
		{
			if (Math.sin(track.getTarget().getHeading()*Math.PI/180-absBearing)*track.getTarget().getVelocity() < 0)
				direction = -1;
			else
				direction = 1;
		}
		currentStats = stats[(int)(track.getTarget().getDistance() / 100)]; 

		newWave = new WaveBullet(r.getX(), r.getY(), absBearing, power,direction, r.getTime(), currentStats);
	}

	public void fireWave(EnemyTracker track, TeamRobot r) {
		
	       if (r.setFireBullet() != null) {
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
		double gunAdjust = Utils.normalRelativeAngle(absBearing - r.getGunHeadingRadians() + angleOffset);
		r.setTurnGunRightRadians(gunAdjust);
		if (r.getGunHeat() == 0 && gunAdjust < Math.atan2(9, track.getTarget().getDistance()) && r.setFireBullet() != null) {
			
		}
	}
}

