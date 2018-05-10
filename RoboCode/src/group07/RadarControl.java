package group07;

import java.util.ArrayList;

import robocode.HitByBulletEvent;
import robocode.RobotDeathEvent;

/**
 * RadarControl: Makes sure we are focusing the right enemy with our radar.
 *
 */
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

	/**
	 * 
	 * @param allyTracker
	 *            Instance of AllyTracker.
	 * @param enemyTracker
	 *            Instance of EnemyTracker.
	 * 
	 * @param mrRobot
	 *            Instance of main class.
	 */
	public RadarControl(AllyTracker allyTracker, EnemyTracker enemyTracker, MrRobot mrRobot) {
		this.allyTracker = allyTracker;
		this.enemyTracker = enemyTracker;
		this.mrRobot = mrRobot;
		oneTime = true;
		gotTarget = false;
	}

	/**
	 * givePlaceInList: Saves the place MrRobot has in allyList.
	 */
	public void givePlaceInList() {
		myPlaceInList = allyTracker.getPlaceInList();
	}

	/**
	 * StartOfGame: Makes sure the ally with place 0 in allyList picks a target and
	 * sends a message telling the next MrRobot to pick a target.
	 * 
	 */
	public void startOfGame() {
		if (enemyTracker.allEnemiesScanned() && oneTime) {

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

	/**
	 * gettingRammed: Change radarTarget if Mr Robot is getting rammed.
	 * 
	 * @param ramBot
	 *            The EnemyBot that is ramming us.
	 */
	public void gettingRammed(EnemyBot ramBot) {
		if (gotTarget) {
			for (int i = 0; i < enemyTracker.getEnemyList().size(); i++) {
				if (enemyTracker.getEnemyList().get(i).getName().equals(ramBot.getName())) {
					if (!ramBot.getName().equals(radarTarget.getName())) {
						newRadarTarget = enemyTracker.getEnemyList().get(getIndexForEnemy(ramBot.getName()));
						mrRobot.sendMessage(1, "2");
						radarTarget = enemyTracker.getEnemyList().get(getIndexForEnemy(ramBot.getName()));
						targetTracking.get(getIndexForName(mrRobot.getName())).updateTarget(radarTarget);
					}
				}
			}
		}
	}

	/**
	 * gettingAttacked: Change radarTarget if Mr Robot is getting attacked.
	 * 
	 * @param e
	 *            HitByBulletEvent
	 */
	public void gettingAttacked(HitByBulletEvent e) {
		if (gotTarget) {
			for (int i = 0; i < enemyTracker.getEnemyList().size(); i++) {
				if (enemyTracker.getEnemyList().get(i).getName().equals(e.getName())) {
					if (!e.getName().equals(radarTarget.getName())) {
						mrRobot.sendMessage(5, "2");
						radarTarget = enemyTracker.getEnemyList().get(getIndexForEnemy(e.getName()));
						targetTracking.get(getIndexForName(mrRobot.getName())).updateTarget(radarTarget);
					}
				}
			}
		}
	}

	/**
	 * robotDeath: Change target if our old target died.
	 * 
	 * @param e
	 *            RobotDeathEvent
	 */
	public void robotDeath(RobotDeathEvent e) {

		if (gotTarget && e.getName().equals(radarTarget.getName())) {
			radarTarget = newRadarTarget();
			mrRobot.sendMessage(6, "2");

		}
		if (getIndexForName(e.getName()) != -1) {
			targetTracking.remove(getIndexForName(e.getName()));
		}

	}

	/**
	 * teammatePicked: The next Mr Robot with a number higher than last placeInList
	 * should now pick.
	 * 
	 * @param teammateName
	 *            Name of the teammate that picked target.
	 * @param targetName
	 *            Name of the target the teammate picked.
	 * @param placeInList
	 *            The place in allyList for the ally that picked.
	 */
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

	/**
	 * teammateGettingAttacked: Change target if a teammate is getting attacked by
	 * our old target.
	 * 
	 * @param teammateName
	 *            Name of the teammate.
	 * @param shooter
	 *            Name of the robot the teammate is getting attacked by.
	 * @param oldTarget
	 *            name of the teammates old radarTarget.
	 */
	public void teammateGettingAttacked(String teammateName, String shooter, String oldTarget) {
		if (gotTarget) {
			if (shooter.equals(radarTarget.getName()) && getIndexForEnemy(oldTarget) != -1) {

				radarTarget = enemyTracker.getEnemyList().get(getIndexForEnemy(oldTarget));
				targetTracking.get(getIndexForName(mrRobot.getName())).updateTarget(radarTarget);
				mrRobot.sendMessage(6, "2");

			}
		}
		if (getIndexForName(teammateName) != -1 && getIndexForEnemy(shooter) != -1) {
			targetTracking.get(getIndexForName(teammateName))
					.updateTarget(enemyTracker.getEnemyList().get(getIndexForEnemy(shooter)));

		}
	}

	/**
	 * teammateNewTarget: Update list a teammate changed target.
	 * 
	 * @param teammateName
	 *            Name of the teammate that changed target.
	 * @param newTarget
	 *            Name of the new target.
	 */
	public void teammateNewTarget(String teammateName, String newTarget) {
		if (getIndexForName(teammateName) != -1 && getIndexForEnemy(newTarget) != -1) {
			targetTracking.get(getIndexForName(teammateName))
					.updateTarget(enemyTracker.getEnemyList().get(getIndexForEnemy(newTarget)));
		}
	}

	/**
	 * getIndexForEnemy: Finds the index in enemyList for the enemyName.
	 * 
	 * @param enemyName
	 *            Name of the robot we are looking for.
	 * @return The index in enemyList. If we can not find the robot in the list
	 *         return -1.
	 */
	private int getIndexForEnemy(String enemyName) {

		for (int i = 0; i < enemyTracker.getEnemyList().size(); i++) {
			if (enemyTracker.getEnemyList().get(i).getName().equals(enemyName)) {
				return i;

			}
		}
		return -1;

	}

	/**
	 * getIndexForName: Finds the index in targetList for the teammateName.
	 * 
	 * @param teammateName
	 *            Name of the teammate we ar looking for.
	 * @return The index in targetList. if we can not find the teammate in the list
	 *         return -1.
	 */
	private int getIndexForName(String teammateName) {
		for (int x = 0; x < targetTracking.size(); x++) {
			if (targetTracking.get(x).getAlly().getName().equals(teammateName)) {
				return x;
			}
		}
		return -1;
	}

	/**
	 * newRadarTarget: Finds a free enemy bot to scan.
	 * 
	 * @return Object of the enemy we should scan.
	 */
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
	/**
	 * 
	 * @return
	 * radarTarget.
	 */
	public EnemyBot getRadarTarget() {
		if (gotTarget) {
			return radarTarget;
		} else {
			return enemyTracker.getTarget();
		}

	}
	/**
	 * 
	 * @return
	 * newRadarTarget.
	 */
	public EnemyBot getNewRadarTarget() {
		if (gotTarget) {
			return newRadarTarget;
		} else {
			return enemyTracker.getTarget();
		}
	}
}
/**
 * AllyWithTarget: Saves which target every ally has.
 *
 */
class AllyWithTarget {
	Ally ally;
	EnemyBot radarTarget;
	/**
	 * 
	 * @param ally
	 * The Ally.
	 * @param radarTarget
	 * The Enemy that Ally is scanning.
	 * 
	 */
	public AllyWithTarget(Ally ally, EnemyBot radarTarget) {
		this.ally = ally;
		this.radarTarget = radarTarget;
	}
/**
 * updateTarget: updating the radarTarget.
 * @param newRadarTarget
 * The newTarget.
 */
	public void updateTarget(EnemyBot newRadarTarget) {
		radarTarget = newRadarTarget;
	}
/**
 * 
 * @return
 * Ally.
 */
	public Ally getAlly() {
		return ally;
	}
/**
 * 
 * @return
 * EnemyBot.
 */
	public EnemyBot getRadarTarget() {
		return radarTarget;
	}

}
