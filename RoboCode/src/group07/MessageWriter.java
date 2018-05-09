package group07;

import java.util.ArrayList;
//Standard Protocol

//[0-1] leadership;[followMe|leadMe]
//[0-1] teamMode;[offensive|defensive]
//[0-1] myPos;x;y
//[0-1] friendPos;x;y
//[0-*] enemyPos;x;y
//[0-1] targetEnemy;name
//[0-1] targetPos;x;y
//[0-1] moveTo;x;y

//messageWriter.standardMessage(this.getX(), 
//this.getY(), allyTracker.getAllyList(), enemyTracker.getEnemies(), enemyTracker.getTarget().getName(), 
//enemyTracker.getTarget().getX(), enemyTracker.getTarget().getY());

//Shooting at an ally that has to move

//[0-1] myPos;x;y
//[0-1] targetEnemy;name
//[0-*] rShot;x,y,tick/time (many rows)

//AllyList update

//[0-1] myPos;x;y
//[0-*] rAlly;name;x;y;tick

//EnemyList update

//[0-1] myPos;x;y
//[0-*] rEnemy;name;type;x;y;tick

// The code to get the standard string to send to teammates.
/**
 * 
 * MessageWriter: returning a String that we are going to send.
 *
 */
public class MessageWriter {
	MrRobot MrRobot;

	/**
	 * 
	 * @param MrRobot
	 *            Instance of main class.
	 */
	public MessageWriter(MrRobot MrRobot) {
		this.MrRobot = MrRobot;
	}

	/**
	 * standardMessage: Message that follows RobotTalk 2018 v1.3
	 * 
	 * @param myXPos
	 *            This robot X value.
	 * @param myYPos
	 *            This robot Y value.
	 * @param allyList
	 *            List with allies.
	 * @param enemyList
	 *            List with enemies.
	 * @param targetEnemy
	 *            Name of the target.
	 * @param tarXPos
	 *            Target X value.
	 * @param tarYPos
	 *            Target Y value.
	 * @return Standard message.
	 */
	public String standardMessage(double myXPos, double myYPos, ArrayList<Ally> allyList, ArrayList<EnemyBot> enemyList,
			String targetEnemy, double tarXPos, double tarYPos) {
		String message = "myPos;" + myXPos + ";" + myYPos;
		if (!allyList.isEmpty()) {
			for (Ally k : allyList) {
				message = message + "\nfriendPos;" + k.getName() + ";" + k.getX() + ";" + k.getY();
			}
		}
		if (!enemyList.isEmpty()) {

			for (EnemyBot k : enemyList) {
				double absBearingDeg = (MrRobot.getHeading() + k.getBearing());
				if (absBearingDeg < 0) {
					absBearingDeg += 360;
				}

				double x = MrRobot.getX() + Math.sin(Math.toRadians(absBearingDeg)) * k.getDistance();
				double y = MrRobot.getY() + Math.cos(Math.toRadians(absBearingDeg)) * k.getDistance();
				message = message + "\nenemyDetails;" + k.getName() + ";" + x + ";" + y + ";" + k.getVelocity() + ";"
						+ k.getEnergy() + ";" + k.getHeading() + ";0";

			}
		}
		message = message + "\ntargetEnemy;" + targetEnemy;
		message = message + "\ntargetPos;" + tarXPos + ";" + tarYPos;
		return message;
	}

	/**
	 * allyListUpdate: Sending message to update allyList.
	 * 
	 * @param myXPos
	 *            This robot X value.
	 * @param myYPos
	 *            This robot Y value.
	 * @param allyList
	 *            List with allies.
	 * @return allyUpdate message.
	 */
	public String allyListUpdate(double myXPos, double myYPos, ArrayList<Ally> allyList) {
		String message = "myPos;" + myXPos + ";" + myYPos;
		if (!allyList.isEmpty()) {
			for (Ally k : allyList) {
				message = message + "\nrAlly;" + k.getName() + ";" + k.getX() + ";" + k.getY() + ";" + k.getTick();
			}
		}
		return message;
	}

	/**
	 * enemyListUpdate: Sending message to update enemyList.
	 * 
	 * @param myXPos
	 *            This robot X value.
	 * @param myYPos
	 *            This robot Y value.
	 * @param enemyList
	 *            List enemyList.
	 * @return enemyListUpdate message.
	 */
	public String enemyListUpdate(double myXPos, double myYPos, ArrayList<EnemyBot> enemyList) {
		String message = "myPos;" + myXPos + ";" + myYPos;
		if (!enemyList.isEmpty()) {

			for (EnemyBot k : enemyList) {

				message = message + "\nrEnemy;" + k.getX() + ";" + k.getY() + ";" + k.getEnergy() + ";" + k.getHeading()
						+ ";" + k.getVelocity() + ";" + k.getTick() + ";" + k.getName();
			}
		}
		return message;
	}

	/**
	 * pickRadarTarget: Picking a starting radarTarget.
	 * 
	 * @param myXPos
	 *            This robot X value.
	 * @param myYPos
	 *            This robot Y value.
	 * @param targetName
	 *            Name of the radarTarget.
	 * @param placeInList
	 *            Place in allyList.
	 * @return pickRadarTarget message.
	 */
	public String pickRadarTarget(double myXPos, double myYPos, String targetName, int placeInList) {

		return "myPos;" + myXPos + ";" + myYPos + "\nrPickRadarTarget;" + targetName + ";" + placeInList;
	}

	/**
	 * gettingAttacked: Change radarTarget getting attacked.
	 * 
	 * @param myXPos
	 *            This robot X value.
	 * @param myYPos
	 *            This robot Y value.
	 * @param shooterName
	 *            Name of the robot that hit MrRobot.
	 * @param oldTargetName
	 *            Name of the target MrRobot had before gettingAttacked.
	 * @return gettingAttacked message.
	 */
	public String gettingAttacked(double myXPos, double myYPos, String shooterName, String oldTargetName) {
		return "myPos;" + myXPos + ";" + myYPos + "\nrGettingAttacked;" + shooterName + ";" + oldTargetName;
	}

	/**
	 * newRadarTarget: updating radarTarget.
	 * 
	 * @param myXPos
	 *            This robot X value.
	 * @param myYPos
	 *            This robot Y value.
	 * @param targetName
	 *            Name of the target MrRobot change to.
	 * @return newRadarTarget message.
	 */
	public String newRadarTarget(double myXPos, double myYPos, String targetName) {
		return "myPos;" + myXPos + ";" + myYPos + "\nrNewRadarTarget;" + targetName;
	}

	/**
	 * gettingRammed: Change radarTarget getting rammed.
	 * 
	 * @param myXPos
	 *            This robot X value.
	 * @param myYPos
	 *            This robot Y value.
	 * @param rammerName
	 *            Name of the rammer that rammed MrRobot.
	 * @param oldTargetName
	 *            Name of the target MrRobot had before gettingRammed.
	 * 
	 * @return gettingRammed message.
	 */
	public String gettingRammed(double myXPos, double myYPos, String rammerName, String oldTargetName) {
		return "myPos;" + myXPos + ";" + myYPos + "\nrGettingAttacked;" + rammerName + ";" + oldTargetName;
	}

	/**
	 * setGunTarget: Which target MrRobots should focus.
	 * 
	 * @param myXPos
	 *            This robot X value.
	 * @param myYPos
	 *            This robot Y value.
	 * @param targetName
	 *            Name of the gunTarget.
	 * @return setGunTarget message.
	 */
	public String setGunTarget(double myXPos, double myYPos, String targetName) {
		return "myPos;" + myXPos + ";" + myYPos + "\nrSetGunTarget;" + targetName;
	}

}
