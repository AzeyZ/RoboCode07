package group07;

import java.util.ArrayList;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
public class Gun {

	private Robot07 robot;
	private TargetEnemyBot target;
	private GunControl gunControl = new GunControl();
	// WaveBullet list
	ArrayList<WaveBullet> waves = new ArrayList<WaveBullet>();
	static int[] stats = new int[31]; 
	// 31 is the number of unique GuessFactors we're using
	// Note: this must be odd number so we can get
	// GuessFactor 0 at middle.
	int direction = 1;


	public Gun(Robot07 robot) {
		this.robot = robot;
	}

	public void update(TargetEnemyBot target) {
		this.target = target;
	}

	// Aim straight at the target
	public void aim() {
		//kraften beroende p� hur l�ngt ifr�n vi �r
		double firePower = Math.min(400 / target.getDistance(), 3);
		// hastigheten p� skottet
		double bulletSpeed = 20 - firePower * 3;
		// avst�nd = hastighet * tid
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
		int[] currentStats = stats; // This seems silly, but I'm using it to
		// show something else later
		WaveBullet newWave = new WaveBullet(r.getX(), r.getY(), absBearing, power,
				direction, r.getTime(), currentStats);
	}
}

