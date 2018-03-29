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
	public void update(ScannedRobotEvent e) {
		if ((isNewEnemy(e) != null)) {
			for (int k = 0; k < enemies.size(); k++) {
				if (e.getName().equals(enemies.get(k).getName())) {
					enemies.get(k).update(e);
				}
			}
		} else {
			addEnemy(e);
		}
	}

	// Enemies
	public void addEnemy(ScannedRobotEvent e) {
		EnemyBot bot = new EnemyBot(robot);
		bot.update(e);
		enemies.add(bot);
	}

	// Check if exist (if exist return enemy)
	public EnemyBot isNewEnemy(ScannedRobotEvent e) {
		for (EnemyBot k : enemies) {
			if (e.getName().equals(k.getName())) {
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
		enemies = targetPrio.sortList(enemies);
	}

	// Target prio
	public void updateTarget() {
		if (!enemies.isEmpty()) {
			enemies = targetPrio.sortList(enemies);
			if (allEnemiesScanned()) {
				target.update(enemies.get(0).getEvent(), robot);
			}
		}
	}

	public boolean allEnemiesScanned() {
		return enemies.size() >= robot.getOthers() - robot.getAllies().size();
	}

	// Getters
	public ArrayList<EnemyBot> getEnemies() {
		return enemies;
	}

	public TargetEnemyBot getTarget() {
		return target;
	}
}
