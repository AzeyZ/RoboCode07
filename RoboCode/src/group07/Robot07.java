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
	private SurfMovement surfing = new SurfMovement(mode);
	

	public void run() {
		// Init robot
		initialize();
		
		// adding allies
		allyTracker.addAllAllies();

		// Robot main loop
		while (true) {
			// counting turns
			mode.NewTurn();
			// flyttar roboten
			if(mode.getCurrentMode() == 0) {
				robotMovement.update(enemyTracker.getTarget());
				robotMovement.move();
			}
			
			// scannar
			radar.update(enemyTracker.getTarget());
			radar.scan();
			// flyttar vapnet
			gun.update(enemyTracker.getTarget());

			//gun.aim();
			//gun.fire();
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
			enemyTracker.update(e.getBearing(), e.getDistance(), e.getEnergy(), e.getHeading(), e.getVelocity(), e.getTime(), e.getName());
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

		//		// Check if message from Mr. Robot and is of type MessageScannedEvent
		//		if(e.getSender().contains("Robot07")) {
		//			try {
		//				MessageScannedEvent msg = (ScannedRobotEvent)e.getMessage();
		//			} catch (Exception error) {
		//				// TODO: handle exception
		//			}
		//		}

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
		//		messageHandler.recieve(e, allyTracker, enemyTracker);
		//
		//		// WIP
		//		updateFromMessage(messageHandler);
		//
		//		// Test om det funkar (Samma target så blir de svarta)
		//		if (enemyTracker.getTarget().getName().equals(messageHandler.getTargetName())) {
		//			setColors(Color.black, Color.black, Color.black);
		//		}
	}

	// WIP ska ta informationen från message handler
	public void updateFromMessage(MessageHandler mh) {

	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	
	
	public void onHitByBullet(HitByBulletEvent e) {
		
		//TODO:switch target to the one that hit us
		
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
