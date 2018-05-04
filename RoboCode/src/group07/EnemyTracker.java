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
		enemies = targetPrio.sortList(enemies);
	}

	// Target prio
	public void updateTarget() {
		if (!enemies.isEmpty()) {
			enemies = targetPrio.sortList(enemies);
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
	
	public ArrayList<EnemyBot> getLivingEnemies(){
		ArrayList <EnemyBot> temp = new ArrayList<>();
		for(int i = 0; i < enemies.size(); i++)
		{
			if(enemies.get(i).getEnergy() > 0)
			{
				temp.add(enemies.get(i));
			}
		}
		return temp;
	}

	// Getters
	public ArrayList<EnemyBot> getEnemies() {
		return enemies;
	}

	public TargetEnemyBot getTarget() {
		return target;
	}
}
