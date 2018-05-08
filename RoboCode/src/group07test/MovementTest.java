package group07test;

import java.util.ArrayList;

import group07.AllyTracker;
import group07.EnemyBot;
import group07.EnemyTracker;
import group07.RobotMovement;
import group07.MovementModeSwitcher;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MovementTest {
	private MockBot mock;
	private EnemyBot enemy;
	private EnemyTracker eTrack;
	private RobotMovement rMove;
	private MovementModeSwitcher modeSwitch;
	
	@Before
	public void setUp () {
		mock = new MockBot("Mock", 100, 90, 10, 10);
		AllyTracker allyTrack = new AllyTracker(mock);
		enemy = new EnemyBot (mock);
		eTrack = new EnemyTracker (mock, allyTrack);
		modeSwitch = new MovementModeSwitcher(mock);
		rMove = new RobotMovement(mock, modeSwitch, eTrack);
	}
	
	@After
	public void tearDown () {
		mock = null;
		enemy = null;
		eTrack = null;
	}
	
	@Test
	public void testMove () {
		EnemyBot target = null;	// Set to null to reach branch
		
		rMove.move();
		assertEquals("Fails to get target", true, rMove.isTargetNull());
		
		target = enemy;
		rMove.update(target);
		rMove.move();
		assertEquals("Fails to get target", false, rMove.isTargetNull());
		
		double turnRight = mock.getGunTurnRemaining();
		assertEquals ("Fails to turn correctly", 0, turnRight, 0.0d);
		
	}
}
