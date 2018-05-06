package group07test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import robocode.MessageEvent;

import group07.MessageHandler;
import group07.AllyTracker;
import group07.EnemyTracker;
import group07.RadarControl;

public class MessageHandlerTest {

	private MessageHandler mHandler;
	private MockBot mock;
	private AllyTracker allyTracker;
	private EnemyTracker enemyTracker;
	private MessageEvent mEvent;

	@Before
	public void setUp() {
		mock = new MockBot("Mock", 100, 90, 10, 10);
		mHandler = new MessageHandler(mock);
		allyTracker = new AllyTracker(mock);
		enemyTracker = new EnemyTracker(mock);
	}

	@After
	public void tearDown() {

	}

	@Test
	public void testSend() {
		// For sending messages
		String message = "Meddelande";
		String receiver = "1";
		mHandler.send(message, receiver); 	// broadCastMessage() kan inte kallas
											// innan run()

		receiver = "2";
		mHandler.send(message, receiver);

		receiver = "3";
		mHandler.send(message, receiver);
	}
}
