package group07;
import robocode.*;
import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Robot07 extends robocode.TeamRobot {
	/**
	 * run: Robot's default behavior
	 */
	private ArrayList<EnemyBot> enemies = new ArrayList<EnemyBot>();
	private ArrayList<Ally> allies = new ArrayList<Ally>();
	private AdvancedEnemyBot target = new AdvancedEnemyBot();
	private RobotMovement robotMovement = new RobotMovement();
	double angleTurret;
	// går upp varje efter varje tick då scan ej fokuserad
	private int lastScan = 0;
	// bytas till -1 om man vill åka baklänges
	private int moveDirection = 1;

	public void run() {
		// Initialization of the robot should be put here
		setColors(Color.red, Color.blue, Color.red); // body,gun,radar
		// adding allies
		String[] teamm8 = getTeammates();
		if (teamm8 != null) {
			for (int i = 0; i < teamm8.length; i++) {
				allies.add(new Ally(teamm8[i]));
			}
		}

		// ser till att alla delar kan rotera individuellt
		setAdjustRadarForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustGunForRobotTurn(true);

		setTurnRadarRight(360);
		// Robot main loop
		while (true) {
			// flyttar vapnet
			doMoveGun();
			// flyttar roboten
			if (!(robotMovement.isTargetNull(target))) {
				setTurnRight(robotMovement.doMoveRobot(moveDirection, getVelocity(), getTime()));
				if (robotMovement.hasStopped()) {
					setAhead(robotMovement.setAhead());
				}
			}
			doMoveRobot();
			// har koll på scannern
			doScan();
			// behövs för att alla set commands ska köra
			execute();
		}
	}

	public AdvancedEnemyBot getAdvancedEnemyBot() {
		return target;
	}

	// Flyttar vapnet om man har en target
	private void doMoveGun() {
		if (target != null) {

			setTurnGunRight(getHeading() + target.getBearing() - getGunHeading());
			doShootGun();
		}
	}

	// Skjuter med en viss kraft
	private void doShootGun() {
		if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 5 && target.getDistance() < 500) {
			setFire(Math.min(400 / target.getDistance(), 3));
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Checks if Scanned is Team
		if (!(isTeammate(e.getName()))) {

			if (!isNewEnemy(e)) {
				enemies.get(getEnemyIndex(e)).update(e);
			} else {
				enemies.add(new EnemyBot());
				enemies.get(enemies.size()-1).update(e);

				// Update target
				target.update(e, this);
				radarFollowTarget();

			}
			// Sends message of ScannedEnemy to team
			// [0-1] leadership;[followMe|leadMe]
			// [0-1] teamMode;[offensive|defensive]
			// [0-1] myPos;x;y
			// [0-*] enemyPos;x;y
			// [0-1] targetEnemy;name
			// [0-1] targetPos;x;y
			// [0-1] moveTo;x;y
		}
	}

	public int getEnemyIndex(ScannedRobotEvent e) {
		for (int i = 0; i < enemies.size(); i++) {
			if (e.getName().equals(enemies.get(i).getName())) {
				return i;
			}
		}
		return -1;
	}

	public boolean isNewEnemy(ScannedRobotEvent e) {
		for (int i = 0; i < enemies.size(); i++) {
			if (e.getName().equals(enemies.get(i).getName())) {
				return false;
			}
		}
		return true;
	}

	public void doScan() {
		// Kollar om scannern har tappat fokus
		lastScan++;
		if (lastScan % 5 == 0 && target != null) {
			target.reset();
			setTurnRadarRight(360);
		}
	}

	// Följer radarn på target
	public void radarFollowTarget() {
		double d = getHeading() - getRadarHeading() + target.getBearing();
		setTurnRadarRight(d);
		lastScan = 0;
	}

	/**
	 * onMessageReceived: What to do when you receive a message
	 */
	public void onMessageReceived(MessageEvent e) {
		// removed
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
	}

	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {

	}

	public void onDeath(RobotDeathEvent event) {
		//should probably be removed
//		ArrayList<Serializable> msg = new ArrayList<Serializable>();
//		msg.add("2");
//		msg.add((Serializable) this); // Vet inte om detta funkar
//		try {
//			sendMessage("Robot07", msg);
//		} catch (IOException error) {
//			// TODO Auto-generated catch block
//		}
	}

	public void onRobotDeath(RobotDeathEvent e) {
		if (e.getName().equals(target.getName())) {
			target.reset();
		}
	}

	private void doMoveRobot() {
		if (target == null) {
			return;
		}
		double degreeCloser = 0;
		if (target.getDistance() > 200) {
			degreeCloser = 10;
		} else {
			degreeCloser = 0;
		}
		setTurnRight(target.getBearing() + 90 - (degreeCloser * moveDirection));

		// bytar riktning om vi stannat eller det gått 20 ticks
		if (getVelocity() == 0 || getTime() % 20 == 0) {
			moveDirection *= -1;
			setAhead(100 * moveDirection);
		}
	}

}
