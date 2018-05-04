package group07;

import java.util.ArrayList;

import robocode.RobotDeathEvent;

public class RadarControl {
	EnemyBot radarTarget;
	Robot07 mrRobot;
	AllyTracker allyTracker;
	EnemyTracker enemyTracker;
	boolean oneTime;
	boolean gotTarget;
	int myPlaceInList;
	ArrayList<AllyWithTarget> targetTracking = new ArrayList<>();
	
	public RadarControl(AllyTracker allyTracker, EnemyTracker enemyTracker, Robot07 mrRobot) {
		this.allyTracker = allyTracker;
		this.enemyTracker = enemyTracker;
		this.mrRobot = mrRobot;
		oneTime = true;
		gotTarget = false;

	}

	public void startOfGame() {
		if(enemyTracker.allEnemiesScanned() && oneTime) {
		myPlaceInList = allyTracker.getPlaceInList();
		if (myPlaceInList == 0) {
			if (!enemyTracker.getLivingEnemies().isEmpty()) {
				radarTarget = enemyTracker.getLivingEnemies().get(0);
				
				targetTracking.add(new AllyWithTarget(allyTracker.getMrRobots().get(0), radarTarget));
			} else {
				radarTarget = enemyTracker.getTarget();
				targetTracking.add(new AllyWithTarget(allyTracker.getMrRobots().get(0), radarTarget));
			}
			gotTarget = true;
			mrRobot.sendMessage(4, "2");
			
			
		}
		oneTime = false;
		}
	}

	public void teammatePicked(String teammateName, String targetName, int placeInList) {
		EnemyBot m_EnemyBot = null;
		Ally m_Ally = null;
		for (int i = 0; i < enemyTracker.getLivingEnemies().size(); i++) {
			if (enemyTracker.getLivingEnemies().get(i).getName().equals(targetName)) {
				m_EnemyBot = enemyTracker.getLivingEnemies().get(i);

				break;
			}
		}
		for (int i = 0; i < allyTracker.getMrRobots().size(); i++) {
			if (allyTracker.getMrRobots().get(i).getName().equals(teammateName)) {
				m_Ally = allyTracker.getMrRobots().get(i);
				break;
			}
		}
		targetTracking.add(new AllyWithTarget(m_Ally, m_EnemyBot));
		if (placeInList + 1 == myPlaceInList) {
			
				radarTarget = newRadarTarget();
				gotTarget = true;
				mrRobot.sendMessage(4, "2");
				
			} 
			
		}
	

	public void gettingAttacked(String newRadarTarget) {
		for (int i = 0; i < enemyTracker.getLivingEnemies().size(); i++) {
			if (enemyTracker.getLivingEnemies().get(i).getName().equals(newRadarTarget)) {
				radarTarget = enemyTracker.getLivingEnemies().get(i);
				break;
			}
		}
	}

	public void teammateGettingAttacked(String teammateName, String shooter, String oldTarget) {
		if (shooter.equals(radarTarget.getName()) && getIndexForEnemy(oldTarget) != -1) {
			
			radarTarget = enemyTracker.getLivingEnemies().get(getIndexForEnemy(oldTarget));
			mrRobot.sendMessage(6, "2");
			
		}
		if (getIndexForName(teammateName) != -1 && getIndexForEnemy(shooter) != -1) {
			targetTracking.get(getIndexForName(teammateName))
					.updateTarget(enemyTracker.getLivingEnemies().get(getIndexForEnemy(shooter)));
			
		}
	}

	public void teammateNewTarget(String teammateName, String newTarget) {
		if (getIndexForName(teammateName) != -1 && getIndexForEnemy(newTarget) != -1) {
			targetTracking.get(getIndexForName(teammateName)).updateTarget(enemyTracker.getLivingEnemies().get(getIndexForEnemy(newTarget)));
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

		for (int i = 0; i < enemyTracker.getLivingEnemies().size(); i++) {
			if (enemyTracker.getLivingEnemies().get(i).getName().equals(enemyName)) {
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
		temp.addAll(enemyTracker.getLivingEnemies());

		if (!enemyTracker.getLivingEnemies().isEmpty() && !targetTracking.isEmpty()) {
			for (int i = 0; i < enemyTracker.getLivingEnemies().size(); i++) {
				for (int x = 0; x < targetTracking.size(); x++) {
					if (enemyTracker.getLivingEnemies().get(i).getName().equals(targetTracking.get(x).getRadarTarget().getName())) {
						temp.remove(enemyTracker.getLivingEnemies().get(i));
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
		if(gotTarget) {
			return radarTarget;
		}
		else {
			return enemyTracker.getTarget();
		}
		
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
