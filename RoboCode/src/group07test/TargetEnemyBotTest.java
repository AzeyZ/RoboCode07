package group07test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import group07.TargetEnemyBot;
import robocode.ScannedRobotEvent;


public class TargetEnemyBotTest {
	private TargetEnemyBot target;
	private MockBot mockBot;
	
	private String name = "Robot07something";
	private double fakeEnergy = 100;
	private double fakeHeading = 2;
	private double fakePosX = 30;
	private double fakePosY = 40;
	
	@Before
	public void setUp() {
		mockBot = new MockBot(name, fakeEnergy, fakeHeading, fakePosX, fakePosY); 
		target = new TargetEnemyBot(mockBot);
	}
	
	@After
	public void tearDown() {
		mockBot = null;
		target = null;
	}
	
	@Test
	public void getX() {
		double fakeBearing = 90;
		double fakeDistance = 40;
		double fakeVelocity = 3;
		MockScannedRobotEvent e = new MockScannedRobotEvent(name, fakeEnergy, fakeBearing, fakeDistance, fakeHeading, fakeVelocity, false, false);
		target.update(fakeBearing, fakeDistance, fakeEnergy, fakeHeading, fakeVelocity, e.getTime(), e.getName());
		assertEquals("Test if x is correct", 70, target.getX(), 0.1d);
	}
	
	@Test
	public void getY() {
		double fakeBearing = 90;
		double fakeDistance = 40;
		double fakeVelocity = 3;
		MockScannedRobotEvent e = new MockScannedRobotEvent(name, fakeEnergy, fakeBearing, fakeDistance, fakeHeading, fakeVelocity, false, false);
		target.update(fakeBearing, fakeDistance, fakeEnergy, fakeHeading, fakeVelocity, e.getTime(), e.getName());
		assertEquals("Test if y is correct", 39, target.getY(), 0.5d);
	}
	
	@Test
	public void getFutureX() {
		
		//assertEquals("Test if name is correct", "IAmYourFriend", );
	}
	
	@Test
	public void getFutureY() {
		
	}
	
	@Test
	public void getFutureDistance() {
		
	}
	
	
}
