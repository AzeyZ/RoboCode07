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
	private MessageWriter messageWriter = new MessageWriter(this);
	private MovementModeSwitcher mode = new MovementModeSwitcher(this);
	private SurfMovement surfing = new SurfMovement(mode, robotMovement,enemyTracker );

	public void run() {
		// Init robot
		initialize();

		// adding allies
		allyTracker.addAllAllies();

		// Robot main loop
		while (true) {
			sendMessage(1, "1");
			sendMessage(2, "2");
			sendMessage(3, "2");
			// counting turns
			mode.NewTurn();
			// flyttar roboten
			if (mode.getCurrentMode() == 0) {
				//robotMovement.update(enemyTracker.getTarget());
				//robotMovement.move();
				//robotMovement.antiGravMove(enemyTracker);
			}

			// scannar
			radar.update(enemyTracker.getTarget());
			radar.scan();
			// flyttar vapnet


			// gun.aim();
			// gun.fire();
			// starts Wave calculations
			gun.Wave(enemyTracker);
			// behövs för att alla set commands ska köra
			execute();
			
		}
	}

	// Settings when starting robot
	public void initialize() {
		// Initialization of the robot should be put here
		setColors(Color.magenta, Color.black, Color.black, Color.green, Color.magenta); // body,gun,radar, bullet, scan

		// ser till att alla delar kan rotera individuellt
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setTurnRadarRight(360);
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		surfing.updateSurf(this, e);
		// Checks if Scanned is Team
		if (!(isTeammate(e.getName()))) {
			enemyTracker.update(e.getBearing(), e.getDistance(), e.getEnergy(), e.getHeading(), e.getVelocity(),
					e.getTime(), e.getName());
			// Update target
			enemyTracker.updateTarget();
		} else {
			allyTracker.update(e);
		}

	}
	// Standard message == 0
	// rShot == 1
	// rAlly == 2
	// rEnemy == 3

	// Skickar iväg ett meddelande
	// receiver == 1 skicka till alla.
	// receiver == 2 skicka till alla mrRobot.
	// receiver != 1 || 2 skicka till receiver.
	public void sendMessage(int messageType, String receiver) {
		String message = "";
		switch (messageType) {
		case 0: {
			message = messageWriter.standardMessage(this.getX(), this.getY(), allyTracker.getAllyList(),
					enemyTracker.getEnemies(), enemyTracker.getTarget().getName(), enemyTracker.getTarget().getX(),
					enemyTracker.getTarget().getY());
			break;
		}
		case 1: {
			break;
		}
		case 2:{
			message = messageWriter.allyListUpdate(this.getX(), this.getY(), allyTracker.getAllyList());
			break;
		}
		case 3:{
			message = messageWriter.enemyListUpdate(this.getX(), this.getY(), enemyTracker.getEnemies());
			break;
		}
		
		}
		messageHandler.send(message, receiver);
	}

	/**
	 * onMessageReceived: What to do when you receive a message
	 */
	public void onMessageReceived(MessageEvent e) {
		messageHandler.recieve(e, allyTracker, enemyTracker);

	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */

	public void onHitByBullet(HitByBulletEvent e) {

		// TODO:switch target to the one that hit us

		surfing.onHitByBulletSurf(e);
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
