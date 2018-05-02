package group07test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import group07.Ally;
import group07.EnemyBot;
import group07.MessageWriter;

public class MessageWriterTest {
	double myXPos = 200;
	double myYPos = 200;
	ArrayList<Ally> allyList = new ArrayList<Ally>();
	ArrayList<EnemyBot> enemyList = new ArrayList<EnemyBot>();
	String targetEnemy = "SomeName";
	double tarXPos = 100;
	double tarYPos = 100;

	private String name = "Robot07something";
	private double fakeEnergy = 100;
	private double fakeHeading = 2;
	private double fakePosX = 30;
	private double fakePosY = 40;

	MockBot mock = new MockBot(name, fakeEnergy, fakeHeading, fakePosX, fakePosY);
	MessageWriter msg = new MessageWriter(mock);
	
	Ally ally = new Ally("Friend");
	EnemyBot enemy = new EnemyBot(mock);

	@Before
	public void setUp() {
		allyList.add(ally);
		enemyList.add(enemy);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void standardMessage() {
		String outputMsg; 
		outputMsg = msg.standardMessage(myXPos, myYPos, allyList, enemyList, targetEnemy, tarXPos, tarYPos);
		
		assertEquals("Test if name is correct", "myPos;200.0;200.0\nfriendPos;Friend;0.0;0.0\nenemyDetails;;30.0;40.0;0.0;0.0;0.0;0\ntargetEnemy;SomeName\ntargetPos;100.0;100.0", outputMsg);
	}

	@Test
	public void shotTowardsAlly() {
		// assertEquals("Test if y-cord is correct", y, ally.getY(), 0.01d);
	}

	@Test
	public void allyListUpdate() {
		// assertEquals("Test if y-cord is correct", y, ally.getY(), 0.01d);
	}

	@Test
	public void enemyListUpdate() {
		// assertEquals("Test if y-cord is correct", y, ally.getY(), 0.01d);
	}
}
