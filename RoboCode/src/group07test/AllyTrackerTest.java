package group07test;

import group07.AllyTracker;
import group07.Ally;

import robocode.ScannedRobotEvent;
import robocode.RobotDeathEvent;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AllyTrackerTest {
	private int sEnergy = 100;
	private int sBearing = 40;
	private int sDistance = 10;
	private int sHeading = 90;
	private int sVelocity = 10;
	
	private AllyTracker allyTracker;
	private MockBot mockBot;
	
	private ArrayList<Ally> allies;
	private Ally normalBot = new Ally("Normal");
	private Ally droidBot = new Ally("Droid");
	private Ally leaderBot = new Ally("Leader");
	
	private ScannedRobotEvent scanned;
	
	@Before
	public void setUp () {
		mockBot = new MockBot("Mock", 100, 90, 10, 10);
		allyTracker = new AllyTracker(mockBot);
		allies = new ArrayList<Ally>();
		
		allies.add(normalBot);
		allies.add(droidBot);
		allies.add(leaderBot);
		
		scanned = new ScannedRobotEvent("Scanned", sEnergy, sBearing, sDistance, sHeading, sVelocity);
	}
	
	@After
	public void tearDown () {
		mockBot = null;
		allyTracker = null;
		allies = null;
	}
	
	@Test
	public void testUpdate () {
		double x = mockBot.getX();
		double y = mockBot.getY();
		boolean changed = false;
		
		allyTracker.addAllAllies();
		allyTracker.update(scanned);
	}
}
