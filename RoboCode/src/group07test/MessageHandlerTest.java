package group07test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import robocode.MessageEvent;

import group07.AllyTracker;
import group07.EnemyTracker;
import group07.MessageHandler;
import group07.RadarControl;

public class MessageHandlerTest {
	
	private MockBot mock;
	private AllyTracker allyTracker;
	private EnemyTracker enemyTracker;
	private MessageEvent mEvent;
	private MessageHandler msgHandler;
	
	@Before
	public void setUp () {
		mock = new MockBot("Mock", 100, 90, 10, 10);
		
		allyTracker = new AllyTracker (mock);
		enemyTracker = new EnemyTracker(mock);
		
		msgHandler = new MessageHandler(mock);
	}
	
	@After
	public void tearDown () {
		
	}
	
	@Test
	public void testSend_Broadcast () {
		// For sending messages
		String receiver = "1";
		String message = " ";
		
		msgHandler.send(message, receiver);
		
		assertEquals("The message failed to broadcast", message, "Broadcast"); //change Broadcast to mock.getBroadcast
	}
}
