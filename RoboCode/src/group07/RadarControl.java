package group07;

import java.util.ArrayList;

import robocode.HitByBulletEvent;
import robocode.RobotDeathEvent;

public class RadarControl {
	EnemyBot radarTarget;
	MrRobot mrRobot;
	AllyTracker allyTracker;
	EnemyTracker enemyTracker;
	EnemyBot newRadarTarget;
	boolean oneTime;
	boolean gotTarget;
	int myPlaceInList;
	ArrayList<AllyWithTarget> targetTracking = new ArrayList<>();

	public RadarControl(AllyTracker allyTracker, EnemyTracker enemyTracker, MrRobot mrRobot) {
		this.allyTracker = allyTracker;
		this.enemyTracker = enemyTracker;
		this.mrRobot = mrRobot;
		oneTime = true;
		gotTarget = false;
	}
	public void givePlaceInList() {
		myPlaceInList = allyTracker.getPlaceInList();
	}
	public void startOfGame() {
		if (enemyTracker.allEnemiesScanned() && oneTime) {
			
		//	System.out.println(myPlaceInList);
			if (myPlaceInList == 0) {
				if (!enemyTracker.getEnemyList().isEmpty()) {
					radarTarget = enemyTracker.getEnemyList().get(0);

					targetTracking.add(new AllyWithTarget(allyTracker.getMrRobots().get(myPlaceInList), radarTarget));
				} else {
					radarTarget = enemyTracker.getTarget();
					targetTracking.add(new AllyWithTarget(allyTracker.getMrRobots().get(myPlaceInList), radarTarget));
				}
				gotTarget = true;
				mrRobot.sendMessage(4, "2");

			}
			oneTime = false;
		}
	}
	public void gettingRammed(EnemyBot ramBot) {
		if (gotTarget) {
			for (int i = 0; i < enemyTracker.getEnemyList().size(); i++) {
				if (enemyTracker.getEnemyList().get(i).getName().equals(ramBot.getName())) {
					if (!ramBot.getName().equals(radarTarget.getName())) {
						newRadarTarget = enemyTracker.getEnemyList().get(getIndexForEnemy(ramBot.getName()));
						mrRobot.sendMessage(1, "2");
//						System.out.println(e.getName() + "-------");
						radarTarget = enemyTracker.getEnemyList().get(getIndexForEnemy(ramBot.getName()));
						targetTracking.get(getIndexForName(mrRobot.getName())).updateTarget(radarTarget);
					}
				}
			}
		}
	}

	public void gettingAttacked(HitByBulletEvent e) {
		if (gotTarget) {
			for (int i = 0; i < enemyTracker.getEnemyList().size(); i++) {
				if (enemyTracker.getEnemyList().get(i).getName().equals(e.getName())) {
					if (!e.getName().equals(radarTarget.getName())) {
						mrRobot.sendMessage(5, "2");
//						System.out.println(e.getName() + "-------");
						radarTarget = enemyTracker.getEnemyList().get(getIndexForEnemy(e.getName()));
						targetTracking.get(getIndexForName(mrRobot.getName())).updateTarget(radarTarget);
					}
				}
			}
		}
	}

	public void robotDeath(RobotDeathEvent e) {
//		System.out.println(e.getName());
//		System.out.println(radarTarget.getName() + "-------------------------------------!");
		if (gotTarget && e.getName().equals(radarTarget.getName())) {
			System.out.println("test");
			radarTarget = newRadarTarget();
			mrRobot.sendMessage(6, "2");

		}
		if (getIndexForName(e.getName()) != -1) {
			targetTracking.remove(getIndexForName(e.getName()));
		}

	}

	public void teammatePicked(String teammateName, String targetName, int placeInList) {
		
		EnemyBot m_EnemyBot = null;
		Ally m_Ally = null;
		for (int i = 0; i < enemyTracker.getEnemyList().size(); i++) {
			if (enemyTracker.getEnemyList().get(i).getName().equals(targetName)) {
				m_EnemyBot = enemyTracker.getEnemyList().get(i);

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
			targetTracking.add(new AllyWithTarget(allyTracker.getMrRobots().get(myPlaceInList), radarTarget));
			mrRobot.sendMessage(4, "2");

		}

	}

	public void teammateGettingAttacked(String teammateName, String shooter, String oldTarget) {
//		System.out.println("test");
		if(gotTarget) {
		if (shooter.equals(radarTarget.getName()) && getIndexForEnemy(oldTarget) != -1) {

			radarTarget = enemyTracker.getEnemyList().get(getIndexForEnemy(oldTarget));
			targetTracking.get(getIndexForName(mrRobot.getName())).updateTarget(radarTarget);
			mrRobot.sendMessage(6, "2");

		}}
		if (getIndexForName(teammateName) != -1 && getIndexForEnemy(shooter) != -1) {
			targetTracking.get(getIndexForName(teammateName))
					.updateTarget(enemyTracker.getEnemyList().get(getIndexForEnemy(shooter)));

		}
	}

	public void teammateNewTarget(String teammateName, String newTarget) {
		if (getIndexForName(teammateName) != -1 && getIndexForEnemy(newTarget) != -1) {
			targetTracking.get(getIndexForName(teammateName))
					.updateTarget(enemyTracker.getEnemyList().get(getIndexForEnemy(newTarget)));
		}
	}

	private int getIndexForEnemy(String enemyName) {

		for (int i = 0; i < enemyTracker.getEnemyList().size(); i++) {
			if (enemyTracker.getEnemyList().get(i).getName().equals(enemyName)) {
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
		temp.addAll(enemyTracker.getEnemyList());

		if (!enemyTracker.getEnemyList().isEmpty() && !targetTracking.isEmpty()) {
			for (int i = 0; i < enemyTracker.getEnemyList().size(); i++) {
				for (int x = 0; x < targetTracking.size(); x++) {
					if (enemyTracker.getEnemyList().get(i).getName()
							.equals(targetTracking.get(x).getRadarTarget().getName())) {
						temp.remove(enemyTracker.getEnemyList().get(i));
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
		if (gotTarget) {
			return radarTarget;
		} else {
			return enemyTracker.getTarget();
		}

	}
	public EnemyBot getNewRadarTarget() {
		if (gotTarget) {
			return newRadarTarget;
		} else {
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
