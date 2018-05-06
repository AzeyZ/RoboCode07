package group07;

import java.util.ArrayList;

import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

public class EnemyTracker {
	private ArrayList<EnemyBot> enemies = new ArrayList<EnemyBot>();
	private EnemyBot target;
	private TargetPrioritizer targetPrio = new TargetPrioritizer();
	private MrRobot robot;

	public EnemyTracker(MrRobot robot) {
		this.robot = robot;
		target = new EnemyBot(robot);
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
		if(bearing < -180 || bearing > 180) {
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
//				enemies.remove(enemies.get(k));
			}
		}
		enemies = targetPrio.sortList(enemies, robot.getTime());
		
	}

	// Target prio
	public void updateTarget() {
		if (!enemies.isEmpty()) {
			enemies = targetPrio.sortList(enemies, robot.getTime());
			if (allEnemiesScanned() && robot.getCloseEnemies().isEmpty()) {
				target = enemies.get(0);
			}
			else if(!robot.getCloseEnemies().isEmpty())
			{
				target = robot.getCloseEnemies().get(0);
				robot.enemyNearby();
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
	}

	// Getters
	public ArrayList<EnemyBot> getEnemies() {
		return enemies;
	}

	public EnemyBot getTarget() {
		return target;
	}
}
