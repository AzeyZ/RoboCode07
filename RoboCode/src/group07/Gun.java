package group07;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import robocode.AdvancedRobot;
import robocode.Condition;
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
	private static double lastEnemyVelocity;
	private static double lateralDirection;


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
		
		lateralDirection = 1;
		lastEnemyVelocity = 0;
		//double absBearing = Math.PI/180*MathUtils.absoluteBearing(robot.getX(), robot.getY(), track.getTarget().getX(), track.getTarget().getY());
		double absBearing = track.getTarget().getBearingRadians() + robot.getHeadingRadians();
		double power = Math.min(400 / target.getDistance(), 3);
		/*
		
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
		*/
		GFTWave wave = new GFTWave(robot);
		wave.gunLocation = new Point2D.Double(robot.getX(), robot.getY());
		GFTWave.targetLocation = MathUtils.project(wave.gunLocation, absBearing, target.getDistance());
		wave.lateralDirection = lateralDirection;
		wave.bulletPower = power;
		wave.setSegmentations(target.getDistance(), target.getVelocity(), lastEnemyVelocity);
		lastEnemyVelocity = target.getVelocity();
		wave.bearing = absBearing;
		robot.setTurnGunRightRadians(Utils.normalRelativeAngle(absBearing - robot.getGunHeadingRadians() + wave.mostVisitedBearingOffset()));
		robot.setFire(wave.bulletPower);
		/*
		if (robot.getEnergy() >= power) {
			robot.addCustomEvent(wave); //????
		}
		*/
		robot.setTurnRadarRightRadians(Utils.normalRelativeAngle(absBearing - robot.getRadarHeadingRadians()) * 2);
	}
	
	
	class GFTWave
	extends Condition
	{
		static Point2D targetLocation;
		double bulletPower;
		Point2D gunLocation;
		double bearing;
		double lateralDirection;
		private static final double MAX_DISTANCE = 900.0D;
		private static final int DISTANCE_INDEXES = 5;
		private static final int VELOCITY_INDEXES = 5;
		private static final int BINS = 25;
		private static final int MIDDLE_BIN = 12;
		private static final double MAX_ESCAPE_ANGLE = 0.7D;
		private static final double BIN_WIDTH = 0.05833333333333333D;
		private static int[][][][] statBuffers = new int[5][5][5][25];
		private int[] buffer;
		private AdvancedRobot robot;
		private double distanceTraveled;

		GFTWave(TeamRobot _robot)
		{
			this.robot = _robot;
		}

		public boolean test()
		{
			advance();
			if (hasArrived())
			{
				this.buffer[currentBin()] += 1;
				this.robot.removeCustomEvent(this);
			}
			return false;
		}

		double mostVisitedBearingOffset()
		{
			return this.lateralDirection * 0.05833333333333333D * (mostVisitedBin() - 12);
		}

		void setSegmentations(double distance, double velocity, double lastVelocity)
		{
			int distanceIndex = Math.min(4, (int)(distance / 180.0D));
			int velocityIndex = (int)Math.abs(velocity / 2.0D);
			int lastVelocityIndex = (int)Math.abs(lastVelocity / 2.0D);
			this.buffer = statBuffers[distanceIndex][velocityIndex][lastVelocityIndex];
		}

		private void advance()
		{
			this.distanceTraveled += MathUtils.bulletVelocity(this.bulletPower);
		}

		private boolean hasArrived()
		{
			return this.distanceTraveled > this.gunLocation.distance(targetLocation) - 18.0D;
		}

		private int currentBin()
		{
			int bin = (int)Math.round(Utils.normalRelativeAngle(MathUtils.absoluteBearing(this.gunLocation, targetLocation) - this.bearing) / (this.lateralDirection * 0.05833333333333333D) + 12.0D);

			return MathUtils.minMax(bin, 0, 24);
		}

		private int mostVisitedBin()
		{
			int mostVisited = 12;
			for (int i = 0; i < 25; i++) {
				if (this.buffer[i] > this.buffer[mostVisited]) {
					mostVisited = i;
				}
			}
			return mostVisited;
		}
	}
}

