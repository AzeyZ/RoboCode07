package group07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import robocode.*;

public class AllyTracker {
	private ArrayList<Ally> allies = new ArrayList<>();
	private MrRobot MrRobot;

	public AllyTracker(MrRobot MrRobot) {
		this.MrRobot = MrRobot;
	}

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

	public void robotDeath(RobotDeathEvent e) {
		for (Ally k : allies) {
			if (e.getName().equals(k.getName())) {
				allies.remove(k);
				break;
			}
		}

	}

	private int amountMrRobot() {
		int amount = 0;
		for (int i = 0; i < allies.size(); i++) {
			if ((allies.get(i).isMrRobot())) {
				amount++;

			}
		}
		return amount;

	}

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

	public ArrayList<Ally> getAllyList() {
		return allies;
	}

	public int getPlaceInList() {
		for (int i = 0; i < allies.size(); i++) {
			if (allies.get(i).getName().equalsIgnoreCase(MrRobot.getName())) {

				return i;

			}
		}
		return amountMrRobot();
	}
}
