package group07test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import group07.Ally;
import group07.EnemyBot;
import group07.MessageWriter;
import group07.Shots;

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
		
		assertEquals("Test if standard message is correct", "myPos;200.0;200.0\nfriendPos;Friend;0.0;0.0\nenemyDetails;;30.0;40.0;0.0;0.0;0.0;0\ntargetEnemy;SomeName\ntargetPos;100.0;100.0", outputMsg);
	}

	@Test
	public void shotTowardsAlly() {
		ArrayList<Shots> shots = new ArrayList<Shots>();
		Shots shot = new Shots(50, 39, 40);
		shots.add(shot);
		
		String outputMsg; 
		outputMsg = msg.shotTowardsAlly(myXPos, myYPos, name, shots);
		assertEquals("Test if shotTowardsAlly message is correct", "myPos;200.0;200.0\ntargetEnemy;Robot07something\nrShot;50.0;39.0;40", outputMsg);

	}

	@Test
	public void allyListUpdate() {
		String outputMsg; 
		outputMsg = msg.allyListUpdate(myXPos, myYPos, allyList);
		assertEquals("Test if allyListUpdate message is correct", "myPos;200.0;200.0\nrAlly;Friend;0.0;0.0;0", outputMsg);
}

	@Test
	public void enemyListUpdate() {
		String outputMsg; 
		outputMsg = msg.enemyListUpdate(myXPos, myYPos, enemyList);
		assertEquals("Test if enemyListUpdate message is correct", "myPos;200.0;200.0\nrEnemy;0.0;0.0;0.0;0.0;0.0;0;", outputMsg);
}
}
