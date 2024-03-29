package group07;

import robocode.*;
import se.lth.cs.etsa02.RobotColors;

import java.awt.Color;
import java.util.ArrayList;

/**
 * MrRobot: Our robot.
 */
public class MrRobot extends robocode.TeamRobot {

	private AllyTracker allyTracker = new AllyTracker(this);
	private EnemyTracker enemyTracker = new EnemyTracker(this, allyTracker);
	private Radar radar = new Radar(this);
	private Gun gun = new Gun(this, allyTracker);
	private MessageHandler messageHandler = new MessageHandler(this);
	private MessageWriter messageWriter = new MessageWriter(this);
	private MovementModeSwitcher mode = new MovementModeSwitcher(this);
	private RobotMovement robotMovement = new RobotMovement(this, mode, enemyTracker);
	private SurfMovement surfing = new SurfMovement(mode, robotMovement, enemyTracker, this, allyTracker);
	private RadarControl radarControl = new RadarControl(allyTracker, enemyTracker, this);
	private HitByBulletEvent lastHitEvent;

	/**
	 * run: Robot's default behavior
	 */
	@Override
	public void run() {
		// Getting the robot ready for battle.
		initialize();

		// Adding allies
		allyTracker.addAllAllies();
		// Giving the robot a int for its place in allyList
		radarControl.givePlaceInList();
		// Robot main loop
		while (true) {
			// Sending all messages that update Lists for Mr Robots and others.
			// One standard message and two special for Mr Robot.
			sendMessage(0, "1");
			sendMessage(2, "2");
			sendMessage(3, "3");

			// Telling MovementModeSwitcher that its a new turn.
			mode.newTurn();
			// Calling movement
			if (mode.getCurrentMode() == 0) {
				robotMovement.antiGravMove(enemyTracker);
			}

			if (mode.getCurrentMode() == 2) {
				robotMovement.update(enemyTracker.getTarget());
				robotMovement.move();
			}
			// Calling radarControl, will set up radarControl.
			radarControl.startOfGame();
			// Updating which target radar should focus.
			radar.update(radarControl.getRadarTarget());
			// Scanning chosen target.
			radar.scan();
			// Calling gun and fire.
			gun.Wave(enemyTracker);

			execute();

		}
	}

	/**
	 * initialize: Settings when starting robot
	 */
	public void initialize() {
		setColors(Color.magenta, Color.black, Color.black, Color.green, Color.magenta); // body,gun,radar, bullet, scan
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setTurnRadarRight(360);
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	@Override
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

	/**
	 * sendMessage: Sending messages to teammates.
	 * 
	 * @param messageType
	 *            We send 7 diffrent types of messages. messageType can have a value
	 *            from 0-6 and will send diffrent kinds of messages depending on the
	 *            value.
	 * @param receiver
	 *            We can have 4 types of strings as input. If we want to send the
	 *            message to all allied bots we set "1" as the input. If we want to
	 *            send the message to all MrRobots (not yourself) we set "2" as the
	 *            input. If we want to send to MrRobots and Hannibal we set "3" as the input.
	 *            If we want to send the message to a teammate we set the
	 *            name of that teammate as input.
	 */
	public void sendMessage(int messageType, String receiver) {
		String message = "";
		// Getting the right string after for the messageType we are trying to send.
		switch (messageType) {
		case 0: {
			message = messageWriter.standardMessage(this.getX(), this.getY(), allyTracker.getAllyList(),
					enemyTracker.getEnemyList(), enemyTracker.getTarget().getName(), enemyTracker.getTarget().getX(),
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
			message = messageWriter.enemyListUpdate(enemyTracker.getEnemyList());
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
		case 7: {
			message = messageWriter.setGunTarget(this.getX(), this.getY(), enemyTracker.getTarget().getName());
			break;
		}
		}
		// Sending the message to the right receiver.
		messageHandler.send(message, receiver);
	}

	/**
	 * onMessageReceived: What to do when you receive a message
	 */
	@Override
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
			messageHandler.receive(e, allyTracker, enemyTracker, radarControl);
		}

	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	@Override
	public void onHitByBullet(HitByBulletEvent e) {
		lastHitEvent = e;
		radarControl.gettingAttacked(e);
		surfing.onHitByBulletSurf(e);
	}

	/**
	 * onRobotDeath: What to do when another robot dies
	 */
	@Override
	public void onRobotDeath(RobotDeathEvent e) {
		enemyTracker.robotDeath(e);
		allyTracker.robotDeath(e);
		radarControl.robotDeath(e);
	}

	/**
	 * getCloseEnemies: Creating an ArrayList with all enemies that is closer than
	 * 150 pixels away and sort it in order starting with the closest.
	 * 
	 * @return ArrayList with EnemyBot
	 */
	public ArrayList<EnemyBot> getCloseEnemies() {
		ArrayList<EnemyBot> rammers = new ArrayList<>();
		for (int i = 0; i < enemyTracker.getEnemyList().size(); i++) {
			if (enemyTracker.getEnemyList().get(i).getDistance() < 150) {
				rammers.add(enemyTracker.getEnemyList().get(i));
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

	/**
	 * getAllies: getting allyList.
	 * 
	 * @return ArrayList<Ally>
	 */
	public ArrayList<Ally> getAllies() {
		return allyTracker.getAllyList();
	}

	/**
	 * getRadar: getting radar.
	 * 
	 * @return Radar
	 */
	public Radar getRadar() {
		return radar;
	}

	/**
	 * getRobot: getting this,
	 * 
	 * @return MrRobot
	 */
	public MrRobot getRobot() {
		return this;
	}

	/**
	 * enemyNearby: Checks if we have an EnemyBot nearby.
	 */
	public void enemyNearby() {
		radarControl.gettingRammed(getCloseEnemies().get(0));
	}
}
