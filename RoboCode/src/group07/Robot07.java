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
	private TargetEnemyBot target = new TargetEnemyBot();
	private RobotMovement robotMovement = new RobotMovement(this);
	private Radar radar = new Radar(this);
	private Gun gun = new Gun(this);
	
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
			// flyttar roboten
			robotMovement.update(target);
			robotMovement.move();
			// scannar 
			radar.update(target);
			radar.scan();
			// flyttar vapnet
			gun.update(target);
			gun.aim();
			gun.fire();
			// behövs för att alla set commands ska köra
			execute();
		}
	}

	public TargetEnemyBot getAdvancedEnemyBot() {
		return target;
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Checks if Scanned is Team
		if (!(isTeammate(e.getName()))) {
			
			// Här är något fel (else kallades aldrig även 1v1)
			EnemyBot m_team = isNewEnemy(e);
			if ((isNewEnemy(e) != null)) {
				m_team.update(e);
			} else {
				EnemyBot bot = new EnemyBot();
				bot.update(e);
				enemies.add(bot);
			}
			// Flyttade ur update från else
			// Update target
			target.update(e, this);

		}
	}

	// public EnemyBot getEnemyIndex(ScannedRobotEvent e) {
	// for (EnemyBot k: enemies) {
	// if (e.getName().equals(k.getName())) {
	// return k;
	// }
	// }
	// return null;
	// }

	public EnemyBot isNewEnemy(ScannedRobotEvent e) {
		for (EnemyBot k : enemies) {
			if (e.getName().equals(k.getName())) {
				return k;
			}
		}
		return null;
	}

	/**
	 * onMessageReceived: What to do when you receive a message
	 */
	public void onMessageReceived(MessageEvent e) {
		// Sends message of ScannedEnemy to team
		// [0-1] leadership;[followMe|leadMe]
		// [0-1] teamMode;[offensive|defensive]
		// [0-1] myPos;x;y
		// [0-*] enemyPos;x;y
		// [0-1] targetEnemy;name
		// [0-1] targetPos;x;y
		// [0-1] moveTo;x;y
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
		// should probably be removed
		// ArrayList<Serializable> msg = new ArrayList<Serializable>();
		// msg.add("2");
		// msg.add((Serializable) this); // Vet inte om detta funkar
		// try {
		// sendMessage("Robot07", msg);
		// } catch (IOException error) {
		// // TODO Auto-generated catch block
		// }
	}

	public void onRobotDeath(RobotDeathEvent e) {
		if (e.getName().equals(target.getName())) {
			target.reset();
		}
	}

	// Ger en vinkel mellan -180 och 180
	public double normalizeBearing(double angle) {
		while (angle > 180)
			angle -= 360;
		while (angle < -180)
			angle += 360;
		return angle;
	}

}
