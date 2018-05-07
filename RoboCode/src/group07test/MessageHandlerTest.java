package group07test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import robocode.MessageEvent;
import group07.Ally;
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
		mock = null;
		
		allyTracker = null;
		enemyTracker = null;
		
		msgHandler = null;
	}
	
	@Test
	public void testSend_Broadcast () {
		// For sending messages
		String receiver = "1";
		String message = "Han är sprängd";
		
		msgHandler.send(message, receiver);
		String msg = mock.getBroadcast();
		
		assertEquals("The message failed to broadcast", message, msg); 
	}
	
	@Test
	public void testSend_mrRobots() {
		Ally[] allies = {new Ally("MrRobot"), new Ally("yes"), new Ally("MrRobot (2)")};
		for (Ally ally: allies){
			mock.addFakeAlly(ally);
		}
		
		// For sending messages
		String receiver = "2";
		String message = "Exclusive message";
		
		msgHandler.send(message, receiver);
		for (String name: mock.getRecievers()){
			assertTrue("The message was sent to others than MrRobot", !name.contains("MrRobot"));
			
		}
		
		for (String rec: mock.getRecievers()){
			assertTrue("The wrong message was sent", rec.equals(message));
		}
	}
}
