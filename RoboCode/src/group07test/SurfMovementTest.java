package group07test;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import group07.Ally;
import group07.AllyTracker;
import group07.EnemyBot;
import group07.EnemyTracker;
import group07.MovementModeSwitcher;
import group07.RobotMovement;
import group07.SurfMovement;
import robocode.TeamRobot;

public class SurfMovementTest {

	private MockBot mock;
	private EnemyBot enemy;
	private EnemyTracker eTrack;
	private RobotMovement rMove;
	private MovementModeSwitcher modeSwitch;
	private TeamRobot tR;
	private AllyTracker allyTrack;
	
	private SurfMovement surf;	
	@Before
	public void setUp () {
		mock = new MockBot("Mock", 100, 90, 10, 10);
		allyTrack = new AllyTracker(mock);
		enemy = new EnemyBot (mock);
		eTrack = new EnemyTracker (mock, allyTrack);
		modeSwitch = new MovementModeSwitcher(mock);
		rMove = new RobotMovement(mock, modeSwitch, eTrack);
		
		allyTrack = new AllyTracker(mock);
		surf = new SurfMovement(modeSwitch, rMove, eTrack, mock, allyTrack);

	}
	
	@After
	public void tearDown () {
		mock = null;
		enemy = null;
		eTrack = null;
		
		allyTrack = null;
		surf = null;
	}
	
	@Test
	public void testMove () {
		MockScannedRobotEvent e = new MockScannedRobotEvent("evilbot", 90, 30, 40, 50, 5, false, false);
		surf.updateSurf(e);
		
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
