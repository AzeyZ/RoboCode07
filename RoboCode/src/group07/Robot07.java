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
	private EnemyTracker enemyTracker = new EnemyTracker(this);
	private AllyTracker allyTracker = new AllyTracker(this);
	private RobotMovement robotMovement = new RobotMovement(this);
	private Radar radar = new Radar(this);
	private Gun gun = new Gun(this);

	private MessageHandler messageHandler = new MessageHandler(this);
	private Message msg = new Message();
	private MessageWriter messageWriter = new MessageWriter(this);

	public void run() {
		// Initialization of the robot should be put here
		setColors(Color.red, Color.blue, Color.red); // body,gun,radar

		// adding allies
		allyTracker.addAllAllies();

		// ser till att alla delar kan rotera individuellt
		setAdjustRadarForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustGunForRobotTurn(true);
		setTurnRadarRight(360);
		// Robot main loop
		while (true) {
			// flyttar roboten
			robotMovement.update(enemyTracker.getTarget());
			robotMovement.move();
			// scannar
			radar.update(enemyTracker.getTarget());
			radar.scan();
			// flyttar vapnet
			gun.update(enemyTracker.getTarget());
			gun.aim();
			gun.fire();
			// behövs för att alla set commands ska köra
			execute();
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Checks if Scanned is Team
		if (!(isTeammate(e.getName()))) {
			// Här är något fel (else kallades aldrig även 1v1)
			enemyTracker.update(e);
			// Update target
			enemyTracker.updateTarget();
		} else {
			allyTracker.update(e);
		}
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

		// Tar meddelandet till rec, skickar det till Message Handler
		//Message rec = (Message) e.getMessage();
		messageHandler.recieve(e, allyTracker);

		// WIP
		updateFromMessage(messageHandler);

		// Test om det funkar (Samma target så blir de svarta)
		if (enemyTracker.getTarget().getName().equals(messageHandler.getTargetName())) {
			setColors(Color.black, Color.black, Color.black);
		}
	}

	// WIP ska ta informationen från message handler
	public void updateFromMessage(MessageHandler mh) {

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

	public void onRobotDeath(RobotDeathEvent e) {
		enemyTracker.robotDeath(e);
		allyTracker.robotDeath(e);
	}

	public ArrayList<Ally> getAllies() {
		return allyTracker.getAllyList();
	}

	public Radar getRadar() {
		return radar;
	}

	public Robot07 getRobot() {
		return this;
	}
}
