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

public class MessageWriter {
	Robot07 MrRobot;

	public MessageWriter(Robot07 MrRobot) {
		this.MrRobot = MrRobot;
	}

	public String standardMessage(double myXPos, double myYPos, ArrayList<Ally> allyList, ArrayList<EnemyBot> enemyList,
			String targetEnemy, double tarXPos, double tarYPos) {
		String message = "myPos;" + myXPos + ";" + myYPos;
		if (!allyList.isEmpty()) {
			for (Ally k : allyList) {
				message = message + "\nfriendPos;" + k.getName() + ";"+ k.getX() + ";" + k.getY();
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
				message = message + "\nenemyDetails;" + k.getName() + ";" + x + ";" + y + ";" + k.getVelocity() + ";" + k.getEnergy() + ";" + k.getHeading() + ";0";

			}
		}
		message = message + "\ntargetEnemy;" + targetEnemy;
		message = message + "\ntargetPos;" + tarXPos + ";" + tarYPos;
		return message;
	}

	public String shotTowardsAlly(double myXPos, double myYPos, String targetEnemy, ArrayList<Shots> shot) {
		String message = "myPos;" + myXPos + ";" + myYPos + "\ntargetEnemy;" + targetEnemy;
		if (!shot.isEmpty()) {
			for (Shots k : shot) {
				message = message + "\nrShot;" + k.getX() + ";" + k.getY() + ";" + k.getTick();
			}
		}
		return message;

	}

	public String allyListUpdate(double myXPos, double myYPos, ArrayList<Ally> allyList) {
		String message = "myPos;" + myXPos + ";" + myYPos;
		if (!allyList.isEmpty()) {
			for (Ally k : allyList) {
				message = message + "\nrAlly;" + k.getName() + ";" + k.getX() + ";" + k.getY() + ";" + k.getTick();
			}
		}
		return message;
	}

	public String enemyListUpdate(double myXPos, double myYPos, ArrayList<EnemyBot> enemyList) {
		String message = "myPos;" + myXPos + ";" + myYPos;
		if (!enemyList.isEmpty()) {

			for (EnemyBot k : enemyList) {

				message = message + "\nrEnemy;" + k.getBearing() + ";" + k.getDistance() + ";" + k.getEnergy() + ";"
						+ k.getHeading() + ";" + k.getVelocity() + ";" + k.getTick() + ";" + k.getName();

			}
		}
		return message;
	}

}
