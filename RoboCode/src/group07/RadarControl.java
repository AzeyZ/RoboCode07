package group07;

import java.util.ArrayList;

public class RadarControl {
	EnemyBot radarTarget;
	Robot07 mrRobot;
	AllyTracker allyTracker;
	EnemyTracker enemyTracker;
	ArrayList<Ally> MrRobotList = new ArrayList<>();
	ArrayList<EnemyBot> enemies = new ArrayList<>();
	int myPlaceInList;

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
			if(!enemies.isEmpty())
			{
				radarTarget = enemies.get(0);
				enemies.remove(enemies.get(0));
			}
			else {
				radarTarget = enemyTracker.getTarget();
			}
			mrRobot.sendMessage(4, "2");
		}
	}
	public void teammatePicked(String targetName, int placeInList) {
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getName().equals(targetName)) {
				enemies.remove(enemies.get(i));
				break;
			}
		}
		if (placeInList + 1 == myPlaceInList) {
			if(!enemies.isEmpty())
			{
				radarTarget = enemies.get(0);
				enemies.remove(enemies.get(0));
			}
			else {
				radarTarget = enemyTracker.getTarget();
			}
			mrRobot.sendMessage(4, "2");
		}
	}

	public void gettingAttacked(String newRadarTarget) {
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getName().equals(newRadarTarget)) {
				enemies.add(radarTarget);
				radarTarget = enemies.get(i);
				break;
			}
		}
		for (int i = 0; i < enemyTracker.getEnemies().size(); i++) {
			if (enemyTracker.getEnemies().get(i).getName().equals(newRadarTarget)) {
				radarTarget = enemies.get(i);
				break;
			}
		}
	}
	public void teammateGettingAttacked(String shooter, String oldTarget) {
		if(shooter.equals(radarTarget.getName()))
		{
			
		}
	}

	public void robotDead() {

	}

	public EnemyBot getRadarTarget() {
		return radarTarget;
	}
}
