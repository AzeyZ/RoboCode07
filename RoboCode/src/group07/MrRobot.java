package group07;

import robocode.*;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class MrRobot extends robocode.TeamRobot {
	/**
	 * run: Robot's default behavior
	 */
	private EnemyTracker enemyTracker = new EnemyTracker(this);
	private AllyTracker allyTracker = new AllyTracker(this);
	private Radar radar = new Radar(this);
	private Gun gun = new Gun(this);
	private MessageHandler messageHandler = new MessageHandler(this);
	private MessageWriter messageWriter = new MessageWriter(this);
	private MovementModeSwitcher mode = new MovementModeSwitcher(this);
	private RobotMovement robotMovement = new RobotMovement(this, mode, enemyTracker);
	private SurfMovement surfing = new SurfMovement(mode, robotMovement, enemyTracker, this, allyTracker);
	private RadarControl radarControl = new RadarControl(allyTracker, enemyTracker, this);
	private HitByBulletEvent lastHitEvent;

	public void run() {
		// Init robot
		initialize();

		// adding allies
		allyTracker.addAllAllies();
		radarControl.givePlaceInList();
		// Robot main loop
		while (true) {
			sendMessage(0, "1");
			sendMessage(2, "2");
			sendMessage(3, "2");
			// counting turns

			mode.NewTurn();
			// flyttar roboten
			if (mode.getCurrentMode() == 0) {
//				System.out.println("ANTI GRAVING");
				robotMovement.antiGravMove(enemyTracker);
			}

			if (mode.getCurrentMode() == 2) {
				// System.out.println("BasicMove engaged");
				robotMovement.update(enemyTracker.getTarget());
				robotMovement.move();
			}

			radarControl.startOfGame();
			if (radarControl.gotTarget) {
				System.out.println(getTime());
				System.out.println(radarControl.getRadarTarget().getName());
			}
			// scannar
			radar.update(radarControl.getRadarTarget());

			radar.scan();

			// behövs för att alla set command ska köra
			gun.Wave(enemyTracker);
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
		surfing.updateSurf(e);
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
	// rGettningRammed == 1
	// rAlly == 2
	// rEnemy == 3
	// rPickRadarTarget == 4
	// rGettingAttacked == 5
	// rNewTarget == 6

	// Skickar ivÃ¤g ett meddelande
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
			message = messageWriter.gettingRammed(this.getX(), this.getY(), radarControl.getNewRadarTarget().getName(),
					radarControl.getRadarTarget().getName());
			break;
			
		}
		case 2: {
			message = messageWriter.allyListUpdate(this.getX(), this.getY(), allyTracker.getAllyList());
			break;
		}
		case 3: {
			message = messageWriter.enemyListUpdate(this.getX(), this.getY(), enemyTracker.getEnemies());
			break;
		}
		case 4: {
			message = messageWriter.pickRadarTarget(this.getX(), this.getY(), radarControl.getRadarTarget().getName(),
					allyTracker.getPlaceInList());
			break;
		}
		case 5: {
			message = messageWriter.gettingAttacked(this.getX(), this.getY(), lastHitEvent.getName(),
					radarControl.getRadarTarget().getName());
			break;
		}
		case 6: {
			message = messageWriter.newRadarTarget(this.getX(), this.getY(), radarControl.getRadarTarget().getName());
			break;
		}
		}
		messageHandler.send(message, receiver);
	}

	/**
	 * onMessageReceived: What to do when you receive a message
	 */
	public void onMessageReceived(MessageEvent e) {

		if (e.getMessage() instanceof RobotColors) {
			RobotColors c = (RobotColors) e.getMessage();
			setBodyColor(c.bodyColor);
			setGunColor(c.gunColor);
			setRadarColor(c.radarColor);
			setScanColor(c.scanColor);
			setBulletColor(c.bulletColor);
		}

		else {
			messageHandler.recieve(e, allyTracker, enemyTracker, radarControl);
		}

	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */

	public void onHitByBullet(HitByBulletEvent e) {

		// // TODO:switch target to the one that hit us
		lastHitEvent = e;
		radarControl.gettingAttacked(e);
		surfing.onHitByBulletSurf(e);
	}

	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {

	}

	public void onRobotDeath(RobotDeathEvent e) {
		enemyTracker.robotDeath(e);
		enemyTracker.updateTarget();
		allyTracker.robotDeath(e);
		radarControl.robotDeath(e);
	}

	public ArrayList<EnemyBot> getCloseEnemies() {
		ArrayList<EnemyBot> rammers = new ArrayList<>();
		for (int i = 0; i < enemyTracker.getLivingEnemies().size(); i++) {
			if (enemyTracker.getLivingEnemies().get(i).getDistance() < 150) {
				rammers.add(enemyTracker.getLivingEnemies().get(i));
			}
		}

		for (int i = 1; i < rammers.size(); i++) {
			if (rammers.get(i - 1).getDistance() > (rammers.get(i).getDistance())) {
				EnemyBot higer = rammers.get(i - 1);
				EnemyBot lower = rammers.get(i);
				rammers.set(i - 1, lower);
				rammers.set(i, higer);
				i = 0;
			}
		}
		return rammers;
	}

	public ArrayList<Ally> getAllies() {
		return allyTracker.getAllyList();
	}

	public Radar getRadar() {
		return radar;
	}

	public MrRobot getRobot() {
		return this;
	}
	public void enemyNearby() {
		radarControl.gettingRammed(getCloseEnemies().get(0));
	}
}
