package group07;

import java.util.ArrayList;
import robocode.*;

public class AllyTracker {
	private ArrayList<Ally> allies = new ArrayList<>();
	private Robot07 MrRobot;

	public AllyTracker(Robot07 MrRobot) {
		this.MrRobot = MrRobot;
	}

	public void addAllAllies() {
		String[] teamm8 = MrRobot.getTeammates();
		if (teamm8 != null) {
			for (int i = 0; i < teamm8.length; i++) {
				allies.add(new Ally(teamm8[i]));
			}
		}
	}
	public void update(ScannedRobotEvent e) {
		double absBearingDeg = (MrRobot.getHeading() + e.getBearing());
		if (absBearingDeg < 0) { absBearingDeg += 360; }
		for(Ally k : allies)
		{
			if(k.getName().equalsIgnoreCase(e.getName()))
			{
				k.update(MrRobot.getX() + Math.sin(Math.toRadians(absBearingDeg)) * e.getDistance(),
				MrRobot.getY() + Math.cos(Math.toRadians(absBearingDeg)) * e.getDistance());
			}
		}
	}
	public void robotDeath(RobotDeathEvent e) {
		for (Ally k : allies) {
			if (e.getName().equals(k.getName())) {
				allies.remove(k);
			}
		}
		
	}
	
	public ArrayList<Ally> getAllyList()
	{
		return allies;
	}
	
	
}
