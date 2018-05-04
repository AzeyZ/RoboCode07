package group07test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import robocode.MessageEvent;

import group07.AllyTracker;
import group07.EnemyTracker;
import group07.RadarControl;

public class MessageHandlerTest {
	
	private MockBot mock;
	private AllyTracker allyTracker;
	private EnemyTracker enemyTracker;
	private MessageEvent mEvent;
	
	@Before
	public void setUp () {
		mock = new MockBot("Mock", 100, 90, 10, 10);
		allyTracker = new AllyTracker (mock);
		enemyTracker = new EnemyTracker(mock);
	}
	
	@After
	public void tearDown () {
		
	}
	
	@Test
	public void testSend () {
		// For sending messages
		String receiver;
	}
}
