package group07;

import java.util.ArrayList;

import robocode.RobotDeathEvent;

public class RadarControl {
	EnemyBot radarTarget;
	Robot07 mrRobot;
	AllyTracker allyTracker;
	EnemyTracker enemyTracker;
	ArrayList<Ally> MrRobotList = new ArrayList<>();
	ArrayList<EnemyBot> enemies = new ArrayList<>();
	int myPlaceInList;
	ArrayList<AllyWithTarget> targetTracking = new ArrayList<>();

	public RadarControl(AllyTracker allyTracker, EnemyTracker enemyTracker, Robot07 mrRobot) {
		this.allyTracker = allyTracker;
		this.enemyTracker = enemyTracker;
		this.mrRobot = mrRobot;
		MrRobotList.addAll(allyTracker.getMrRobots());
		enemies.addAll(enemyTracker.getEnemies());
		myPlaceInList = allyTracker.getPlaceInList();

	}

	public void startOfGame() {

		if (myPlaceInList == 0) {
			if (!enemies.isEmpty()) {
				radarTarget = enemies.get(0);
				// enemies.remove(enemies.get(0));
				targetTracking.add(new AllyWithTarget(MrRobotList.get(0), radarTarget));
			} else {
				radarTarget = enemyTracker.getTarget();
				targetTracking.add(new AllyWithTarget(MrRobotList.get(0), radarTarget));
			}
			mrRobot.sendMessage(4, "2");
		}
	}

	public void teammatePicked(String teammateName, String targetName, int placeInList) {
		EnemyBot m_EnemyBot = null;
		Ally m_Ally = null;
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getName().equals(targetName)) {
				m_EnemyBot = enemies.get(i);

				break;
			}
		}
		for (int i = 0; i < MrRobotList.size(); i++) {
			if (MrRobotList.get(i).getName().equals(teammateName)) {
				m_Ally = MrRobotList.get(i);
				break;
			}
		}
		targetTracking.add(new AllyWithTarget(m_Ally, m_EnemyBot));
		if (placeInList + 1 == myPlaceInList) {
			
				radarTarget = newRadarTarget();
			} 
			mrRobot.sendMessage(4, "2");
		}
	

	public void gettingAttacked(String newRadarTarget) {
		// for (int i = 0; i < enemies.size(); i++) {
		// if (enemies.get(i).getName().equals(newRadarTarget)) {
		// enemies.add(radarTarget);
		// radarTarget = enemies.get(i);
		// break;
		// }
		// }
		// for (int i = 0; i < enemyTracker.getEnemies().size(); i++) {
		// if (enemyTracker.getEnemies().get(i).getName().equals(newRadarTarget)) {
		// radarTarget = enemies.get(i);
		// break;
		// }
		// }
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getName().equals(newRadarTarget)) {
				radarTarget = enemies.get(i);
				break;
			}
		}
	}

	public void teammateGettingAttacked(String teammateName, String shooter, String oldTarget) {
		if (shooter.equals(radarTarget.getName())) {
			mrRobot.sendMessage(6, "2");
			if (getIndexForName(teammateName) != -1 && getIndexForEnemy(oldTarget) != -1) {
				targetTracking.get(getIndexForName(teammateName))
						.updateTarget(enemies.get(getIndexForEnemy(oldTarget)));
			}
		}
	}

	public void teammateNewTarget(String teammateName, String newTarget) {
		EnemyBot m_EnemyBot = null;
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getName().equals(newTarget)) {
				m_EnemyBot = enemies.get(i);

				break;
			}
		}
		if (getIndexForName(teammateName) != -1) {
			targetTracking.get(getIndexForName(teammateName)).updateTarget(m_EnemyBot);
		}
	}

	public void robotDeath(RobotDeathEvent e) {
		if(e.getName().equals(radarTarget.getName()))
		{
			
			radarTarget = newRadarTarget();
			mrRobot.sendMessage(6, "2");
			
		}
		if(getIndexForName(e.getName()) != -1)
		{
			targetTracking.remove(getIndexForName(e.getName()));
		}
		
	}

	private int getIndexForEnemy(String enemyName) {

		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getName().equals(enemyName)) {
				return i;

			}
		}
		return -1;

	}

	private int getIndexForName(String teammateName) {
		for (int x = 0; x < targetTracking.size(); x++) {
			if (targetTracking.get(x).getAlly().getName().equals(teammateName)) {
				return x;
			}
		}
		return -1;
	}

	private EnemyBot newRadarTarget() {
		ArrayList<EnemyBot> temp = new ArrayList<>();
		temp.addAll(enemies);

		if (!enemies.isEmpty()) {
			for (int i = 0; i < enemies.size(); i++) {
				for (int x = 0; x < targetTracking.size(); x++) {
					if (enemies.get(i).getName().equals(targetTracking.get(x).getRadarTarget().getName())) {
						temp.remove(enemies.get(i));
					}
				}
			}
			if (!temp.isEmpty()) {
				return temp.get(0);
			}

		}
		return enemyTracker.getTarget();
	}

	public EnemyBot getRadarTarget() {
		return radarTarget;
	}
}

class AllyWithTarget {
	Ally ally;
	EnemyBot radarTarget;

	public AllyWithTarget(Ally ally, EnemyBot radarTarget) {
		this.ally = ally;
		this.radarTarget = radarTarget;
	}

	public void updateTarget(EnemyBot newRadarTarget) {
		radarTarget = newRadarTarget;
	}

	public Ally getAlly() {
		return ally;
	}

	public EnemyBot getRadarTarget() {
		return radarTarget;
	}
}
