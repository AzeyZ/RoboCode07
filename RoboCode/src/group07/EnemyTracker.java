package group07;

import java.util.ArrayList;

import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

public class EnemyTracker {
	private ArrayList<EnemyBot> enemies = new ArrayList<EnemyBot>();
	private TargetEnemyBot target;
	private TargetPrioritizer targetPrio = new TargetPrioritizer();
	private Robot07 robot;

	public EnemyTracker(Robot07 robot) {
		this.robot = robot;
		target = new TargetEnemyBot(robot);
	}

	// Update enemy list
	public void update(double bearing, double distance, double energy, double heading, double velocity, long time,
			String name) {
		if ((isNewEnemy(name) != null)) {
			for (int k = 0; k < enemies.size(); k++) {
				if (name.equals(enemies.get(k).getName()) && enemies.get(k).getTick() < time) {
					enemies.get(k).update(bearing, distance, energy, heading, velocity, time, name);
				}
			}
		} else {
			addEnemy(bearing, distance, energy, heading, velocity, time, name);
		}
	}

	public void msgUpdate(double enemyX, double enemyY, double energy, double heading, double velocity, long time,
			String name) {
		double bearing = calBearing(enemyX, enemyY);
		double distance = MathUtils.distance(robot.getX(), robot.getY(), enemyX, enemyY);
//		System.out.println(name);
		if(bearing < -180 || bearing > 180) {
//		System.out.println("Bearing:" + bearing + "---------------------");
		}
		if (bearing != -1) {
			if ((isNewEnemy(name) != null)) {
				for (int k = 0; k < enemies.size(); k++) {
					if (name.equals(enemies.get(k).getName()) && enemies.get(k).getTick() < time) {
						enemies.get(k).update(bearing, distance, energy, heading, velocity, time, name);
					}
				}
			} else {
				addEnemy(bearing, distance, energy, heading, velocity, time, name);
			}
		}
	}

	// Enemies
	public void addEnemy(double bearing, double distance, double energy, double heading, double velocity, long time,
			String name) {
		EnemyBot bot = new EnemyBot(robot);
		bot.update(bearing, distance, energy, heading, velocity, time, name);
		enemies.add(bot);
	}

	// Check if exist (if exist return enemy)
	public EnemyBot isNewEnemy(String name) {
		for (EnemyBot k : enemies) {
			if (name.equals(k.getName())) {
				return k;
			}
		}
		return null;
	}

	// Updates dead robot
	public void robotDeath(RobotDeathEvent e) {
		for (int k = 0; k < enemies.size(); k++) {
			if (e.getName().equals(enemies.get(k).getName())) {
				enemies.get(k).setEnergy(0);
			}
		}
		enemies = targetPrio.sortList(enemies, robot.getTime());
	}

	// Target prio
	public void updateTarget() {
		if (!enemies.isEmpty()) {
			enemies = targetPrio.sortList(enemies, robot.getTime());
			if (allEnemiesScanned()) {
				target.update(enemies.get(0).getBearing(), enemies.get(0).getDistance(), enemies.get(0).getEnergy(),
						enemies.get(0).getHeading(), enemies.get(0).getVelocity(), enemies.get(0).getTick(),
						enemies.get(0).getName());
			}
		}
	}

	public boolean allEnemiesScanned() {
		return enemies.size() > robot.getOthers() - robot.getAllies().size();
	}

	public ArrayList<EnemyBot> getLivingEnemies() {
		ArrayList<EnemyBot> temp = new ArrayList<>();
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getEnergy() > 0) {
				temp.add(enemies.get(i));
			}
		}
		return temp;
	}

	private double calBearing(double enemyX, double enemyY) {
		//-180 <= getBearing() < 180

		double angle = Math.toDegrees(Math.atan2(enemyX - robot.getX(), enemyY - robot.getY()));
		double heading = robot.getHeading();
		if(heading >= 180)
		{
			heading -= 360;
		}
		double bearing = angle - heading;
		if(bearing >= 180)
		{
			bearing -=360;
		}
		return bearing;

//		double yDiff = Math.abs(enemyY - robot.getY());
//		double xDiff = Math.abs(enemyX - robot.getX());
//
//		if (yDiff == 0) {
//			if (enemyX > robot.getX()) {
//				return Math.abs(90 - robot.getHeading());
//			}
//			if (enemyX < robot.getX()) {
//				return Math.abs(270 - robot.getHeading());
//			}
//		}
//
//		if (xDiff == 0) {
//			if (enemyY > robot.getY()) {
//				return robot.getHeading();
//			}
//			if (enemyY < robot.getY()) {
//				return Math.abs(180 - robot.getHeading());
//			}
//		}
//
//		if (enemyX < robot.getX() && enemyY < robot.getY()) { // 3
//			return Math.toDegrees(Math.abs(180 + Math.atan(xDiff / yDiff) - robot.getHeading()));
//		}
//		if (enemyX > robot.getX() && enemyY > robot.getY()) { // 2
//			return Math.toDegrees(Math.abs(Math.atan(xDiff / yDiff) - robot.getHeading()));
//		}
//		if (enemyX < robot.getX() && enemyY > robot.getY()) { // 1
//			return Math.toDegrees(Math.abs(270 + Math.atan(yDiff / xDiff) - robot.getHeading()));
//		}
//		if (enemyX > robot.getX() && enemyY < robot.getY()) { // 4
//			return Math.toDegrees(Math.abs(90 + Math.atan(yDiff / xDiff) - robot.getHeading()));
//		}
//		return -1;
	}

	// Getters
	public ArrayList<EnemyBot> getEnemies() {
		return enemies;
	}

	public TargetEnemyBot getTarget() {
		return target;
	}
}
