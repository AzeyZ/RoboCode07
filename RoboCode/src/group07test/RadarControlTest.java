package group07test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import group07.RadarControl;
import group07.EnemyTracker;
import group07.EnemyBot;
import group07.Ally;
import group07.AllyTracker;

public class RadarControlTest {
	// For creating objects 
	private MockBot mock;
	private RadarControl rControl;
	private EnemyTracker eTrack;
	private EnemyBot eBot;
	private Ally ally;
	private AllyTracker aTrack;
	
	// Standard variables for creating enemies
	private double sDistance = 100;
	private double sBearing =  40;
	private double sHeading = 90;
	private double sEnergy = 100;
	private double sVelocity = 10;
	private long sTime = 1000;
	
	@Before
	public void setUp () {
		mock = new MockBot ("Mock", 100, 90, 9, 9);
		eTrack = new EnemyTracker(mock);
	}
	
	@After
	public void tearDown () {
		mock = null;
	}
	
	// Not sure if this function can be used after all
	// Creates a list with a total of 5 mockbots.
	private ArrayList<Ally> createAllyList () {
		MockBot allyMock;	 // Allies
		ArrayList<Ally> temp = new ArrayList<Ally>();
		
		for (int i = 0; i < 4; i++) {
			allyMock = new MockBot("Mock" + i, 100, 90, 10 + i, 10 + i);
			temp.add(new Ally(allyMock.getName()));
		}
		
		temp.add(new Ally(mock.getName()));
		
		return temp;
	}
	
	//Creates a list with a total of 5 enemies
	private ArrayList<EnemyBot> createEnemyList () {
		ArrayList<EnemyBot> temp =  new ArrayList<EnemyBot>();
		
		for (int i = 0; i < 5; i++) {
			eTrack.addEnemy(sBearing, sDistance, sEnergy, sHeading, sVelocity, sTime, "Enemy" + i);
		}
		
		temp = eTrack.getEnemies();
		
		return temp;
	}
	
	@Test
	public void testAllyPicked () {
		
	}
	
	@Test
	public void testGetRadarTarget () {
	
	}
}
