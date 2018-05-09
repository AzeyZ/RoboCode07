package group07;

import java.util.ArrayList;

import robocode.RobotDeathEvent;

/**
 * EnemyTracker: Tracking data about Enemies.
 */
public class EnemyTracker {
	private ArrayList<EnemyBot> enemies = new ArrayList<EnemyBot>();
	private EnemyBot target;
	private EnemyBot updateTarget;
	private TargetPrioritizer targetPrio = new TargetPrioritizer();
	private MrRobot robot;
	private AllyTracker ally;
	private boolean allEnemiesScanned;
	private boolean robotDead;
	private boolean gotUpdateTarget;
	private boolean sentMessage;
	private boolean oneTurn;

	/**
	 * 
	 * @param robot
	 *            Instance of our main class.
	 * @param ally
	 *            Instance of our allyTracker.
	 */
	public EnemyTracker(MrRobot robot, AllyTracker ally) {
		this.robot = robot;
		this.ally = ally;
		target = new EnemyBot(robot);
		allEnemiesScanned = false;
		updateTarget = null;
		robotDead = false;
		gotUpdateTarget = false;
		sentMessage = false;
		oneTurn = false;
	}

	/**
	 * update: Updating the information about a specific enemy.
	 * 
	 * @param bearing
	 *            Bering to the enemy.
	 * @param distance
	 *            Distance to the enemy.
	 * @param energy
	 *            The energy of the enemy.
	 * @param heading
	 *            Enemy heading.
	 * @param velocity
	 *            Enemy velocity.
	 * @param time
	 *            Turn the information was last updated.
	 * @param name
	 *            The name of the robot the information belongs to.
	 */
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

	/**
	 * msgUpdate: Updating the information about a specific enemy from a message.
	 * 
	 * @param enemyX
	 *            Enemy X value.
	 * @param enemyY
	 *            Enemy Y value.
	 * @param energy
	 *            The energy of the enemy.
	 * @param heading
	 *            Enemy heading.
	 * @param velocity
	 *            Enemy velocity.
	 * @param time
	 *            Turn the information was last updated.
	 * @param name
	 *            The name of the robot the information belongs to.
	 */
	public void msgUpdate(double enemyX, double enemyY, double energy, double heading, double velocity, long time,
			String name) {
		double bearing = calBearing(enemyX, enemyY);
		double distance = MathUtils.distance(robot.getX(), robot.getY(), enemyX, enemyY);
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

	/**
	 * msgUpdateTarget: Get a new target from a message.
	 * 
	 * @param targetName
	 *            The name of the new target.
	 */

	public void msgUpdateTarget(String targetName) {
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getName().equals(targetName)) {
				updateTarget = enemies.get(i);
				gotUpdateTarget = true;
			}
		}

	}

	/**
	 * addEnemy: Add a new enemy to the enemyList.
	 * 
	 * @param bearing
	 *            Bearing to the enemy.
	 * @param distance
	 *            Distance to the enemy.
	 * @param energy
	 *            The energy of the enemy.
	 * @param heading
	 *            Enemy heading.
	 * @param velocity
	 *            Enemy velocity.
	 * @param time
	 *            Turn the information was last updated.
	 * @param name
	 *            The name of the robot the information belongs to.
	 */
	public void addEnemy(double bearing, double distance, double energy, double heading, double velocity, long time,
			String name) {
		EnemyBot bot = new EnemyBot(robot);
		bot.update(bearing, distance, energy, heading, velocity, time, name);
		enemies.add(bot);
	}

	// Check if exist (if exist return enemy)
	/**
	 * isNewEnemy: Checks if the enemy is in enemyList.
	 * 
	 * @param name
	 *            Name of the enemy that is getting checked.
	 * @return Null if enemy is new. The object of the enemy if not new.
	 */
	public EnemyBot isNewEnemy(String name) {
		for (EnemyBot k : enemies) {
			if (name.equals(k.getName())) {
				return k;
			}
		}
		return null;
	}

	/**
	 * robotDeath: Removing the dead robot from the enemyList.
	 * 
	 * @param e
	 *            RobotDeathEvent
	 */
	public void robotDeath(RobotDeathEvent e) {

		for (int k = 0; k < enemies.size(); k++) {
			if (e.getName().equals(enemies.get(k).getName())) {
				enemies.remove(enemies.get(k));
				robotDead = true;
			}
		}
		enemies = targetPrio.sortList(enemies, robot.getTime());
		updateTarget();

	}

	/**
	 * updateTarget: Updates the targeted enemy.
	 */
	public void updateTarget() {
		if (!enemies.isEmpty()) {
			enemies = targetPrio.sortList(enemies, robot.getTime());
			if (allEnemiesScanned() && robot.getCloseEnemies().isEmpty() && (robotDead || !gotUpdateTarget)) {
				target = enemies.get(0);
				if (ally.getPlaceInList() == 0 && !sentMessage && oneTurn) {
					sentMessage = true;
					robot.sendMessage(7, "2");
					msgUpdateTarget(target.getName());
				}
			} else if (!robot.getCloseEnemies().isEmpty()) {
				target = robot.getCloseEnemies().get(0);
				robot.enemyNearby();
			} else if (gotUpdateTarget) {
				target = updateTarget;
			}
			if (allEnemiesScanned()) {
				oneTurn = true;
			}
		}
	}

	/**
	 * allEnemiesScanned: checks if all enemies is in enemyList.
	 * 
	 * @return True if all enemies are in enemyList. False if enemies are missing.
	 */
	public boolean allEnemiesScanned() {
		if (enemies.size() > robot.getOthers() - robot.getAllies().size()) {
			allEnemiesScanned = true;
		}
		return allEnemiesScanned;
	}

	/**
	 * 
	 * @return enemyList
	 */
	public ArrayList<EnemyBot> getEnemyList() {
		return enemies;
	}

	/**
	 * calBearing: Calculate bearing of an Enemy.
	 * 
	 * @param enemyX
	 *            X value.
	 * @param enemyY
	 *            Y value.
	 * @return bearing in degrees.
	 */
	private double calBearing(double enemyX, double enemyY) {
		double angle = Math.toDegrees(Math.atan2(enemyX - robot.getX(), enemyY - robot.getY()));
		double heading = robot.getHeading();
		if (heading >= 180) {
			heading -= 360;
		}
		double bearing = angle - heading;
		if (bearing >= 180) {
			bearing -= 360;
		}
		return bearing;
	}

	/**
	 * 
	 * @return gunTarget.
	 */
	public EnemyBot getTarget() {
		return target;
	}
}
