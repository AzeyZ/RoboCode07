package group07;

import java.util.ArrayList;
import robocode.*;

/**
 * AllyTracker: Handles info about allies.
 * 
 *
 */
public class AllyTracker {
	private ArrayList<Ally> allies = new ArrayList<>();
	private MrRobot MrRobot;

	/**
	 * 
	 * @param MrRobot
	 */
	public AllyTracker(MrRobot MrRobot) {
		this.MrRobot = MrRobot;
	}

	/**
	 * addAllAllies: Adding all allies to the allyList.
	 * 
	 */
	public void addAllAllies() {
		String[] teamm8 = MrRobot.getTeammates();
		if (teamm8 != null) {
			for (int i = 0; i < teamm8.length; i++) {
				allies.add(new Ally(teamm8[i]));

			}

		}
		allies.add(0, new Ally(MrRobot.getName()));
		sortAlly();
	}

	/**
	 * update: Update the info about the scanned ally.
	 * 
	 * @param ScannedRobotEvent
	 */
	public void update(ScannedRobotEvent e) {
		double absBearingDeg = (MrRobot.getHeading() + e.getBearing());
		if (absBearingDeg < 0) {
			absBearingDeg += 360;
		}
		for (Ally k : allies) {
			if (k.getName().equalsIgnoreCase(e.getName())) {
				k.update(MrRobot.getX() + Math.sin(Math.toRadians(absBearingDeg)) * e.getDistance(),
						MrRobot.getY() + Math.cos(Math.toRadians(absBearingDeg)) * e.getDistance(), MrRobot.getTime());
			}
		}
	}

	/**
	 * robotDeath: Remove the robot that died from allyList.
	 * 
	 * @param RobotDeathEvent
	 */
	public void robotDeath(RobotDeathEvent e) {
		for (Ally k : allies) {
			if (e.getName().equals(k.getName())) {
				allies.remove(k);
				break;
			}
		}

	}

	/**
	 * amountMrRobot: Count the amount of MrRobots in allyList.
	 * 
	 * @return int
	 */
	private int amountMrRobot() {
		int amount = 0;
		for (int i = 0; i < allies.size(); i++) {
			if ((allies.get(i).isMrRobot())) {
				amount++;

			}
		}
		return amount;

	}

	/**
	 * sortAlly: Sort allyList and place the MrRobot in name order and all other
	 * bots after them.
	 */
	private void sortAlly() {

		for (int i = 1; i < allies.size(); i++) {
			if (allies.get(i - 1).getName().compareToIgnoreCase(allies.get(i).getName()) > 0) {
				Ally higher = allies.get(i - 1);
				Ally lower = allies.get(i);
				allies.set(i - 1, lower);
				allies.set(i, higher);
				i = 0;

			}
		}
		ArrayList<Ally> removedAllies = new ArrayList<>();
		for (int i = 0; i < allies.size(); i++) {
			if (!(allies.get(i).isMrRobot())) {
				Ally temp = allies.get(i);
				allies.remove(temp);
				removedAllies.add(temp);
				i -= 1;

			}
		}
		allies.addAll(removedAllies);
	}

	/**
	 * getMrRobots: Get a list with all MrRobots.
	 * 
	 * @return ArrayList<Ally>
	 */
	public ArrayList<Ally> getMrRobots() {
		ArrayList<Ally> temp = new ArrayList<>();
		temp.addAll(allies);
		for (int i = 0; i < temp.size(); i++) {
			if (!(temp.get(i).isMrRobot())) {
				temp.remove(temp.get(i));
				i -= 1;
			}
		}
		return temp;
	}

	/**
	 * getAllyListWithoutOurself: get a list without ourself.
	 * 
	 * @return ArrayList<Ally>
	 */
	public ArrayList<Ally> getAllyListWithoutOurself() {
		ArrayList<Ally> temp = new ArrayList<>();
		temp.addAll(allies);
		for (int i = 0; i < temp.size(); i++) {
			if (temp.get(i).getName().equalsIgnoreCase(MrRobot.getName())) {

				temp.remove(temp.get(i));
				break;

			}
		}
		return temp;
	}

	/**
	 * 
	 * @return ArrayList<Ally>
	 */
	public ArrayList<Ally> getAllyList() {
		return allies;
	}

	/**
	 * getPlaceInList: Return the place the ally has in allyList.
	 * 
	 * @return int
	 */
	public int getPlaceInList() {
		for (int i = 0; i < allies.size(); i++) {
			if (allies.get(i).getName().equalsIgnoreCase(MrRobot.getName())) {

				return i;

			}
		}
		return amountMrRobot();
	}
}
