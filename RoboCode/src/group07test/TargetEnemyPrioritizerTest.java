package group07test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

import group07.EnemyBot;
import group07.TargetPrioritizer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TargetEnemyPrioritizerTest {

	private TargetPrioritizer prio = new TargetPrioritizer();

	// Standard variables for initialisation
	private int sBearing = 40;
	private int sHeading = 90;
	private int sX = 10;
	private int sY = 10;
	private int sDistance = 5;
	private int sTick = 1000;
	private int sVelocity = 10;

	private int normalEnergy = 100;
	private int droidEnergy = 120;
	private int leaderEnergy = 200;

	private ArrayList<EnemyBot> enemies = new ArrayList<EnemyBot>();

	private MockBot mock = new MockBot("MockBot", normalEnergy, sHeading, sX, sY);

	private EnemyBot normalBot = new EnemyBot(mock);
	private EnemyBot droidBot = new EnemyBot(mock);
	private EnemyBot leaderBot = new EnemyBot(mock);

	@Before
	public void setUp() {
		// Sets up three bots, one of each kind, to add them to a list
		normalBot.update(sBearing, sDistance, normalEnergy, sHeading, sVelocity, sTick, "Normal");
		droidBot.update(sBearing, sDistance, droidEnergy, sHeading, sVelocity, sTick, "Droid");
		leaderBot.update(sBearing, sDistance, leaderEnergy, sHeading, sVelocity, sTick, "Leader");

		// Add bots to list
		enemies.add(normalBot);
		enemies.add(droidBot);
		enemies.add(leaderBot);
	}

	@After
	public void tearDown() {
		mock = null;
		normalBot = null;
		droidBot = null;
		leaderBot = null;
	}
	
	@Test
	public void testSort () {
		boolean isLeaderList = false;
		boolean isNormalList = false;	// Booleans to determine wich bot is first
		boolean isDroidList = false;
		
		// Test case leaderAlive && amountDroid > amountNormal
		ArrayList<EnemyBot> sortedList;
		ArrayList<EnemyBot> leaderList = new ArrayList<EnemyBot>();
		leaderList.add(leaderBot);
		for (int i = 0; i < 4; i++) {
			leaderList.add(droidBot);
		}
		
		sortedList = prio.sortList(leaderList, sTick);
		
		if (sortedList.get(0).getType() == TargetPrioritizer.LEADER_BOT) {
			isLeaderList = true;
		}
		
		assertEquals("Fails to sort list", true, isLeaderList);
		
		// Test case amountNormal != 0
		// Adds leader first to make sure sorting happens
		ArrayList<EnemyBot> normalList = new ArrayList<EnemyBot>();
		normalList.add(leaderBot);
		for (int i = 0; i < 4; i++) {
			normalList.add(normalBot);
		}
		
		sortedList = prio.sortList(normalList, sTick);
		
		if (sortedList.get(0).getType() == TargetPrioritizer.NORMAL_BOT) {
			isNormalList = true;
		}
		
		assertEquals("Fails to sort list", true, isNormalList);
		
		// Test case only droids alive
		// Don't add leader because it will be focused before droids
		ArrayList<EnemyBot> droidList = new ArrayList<EnemyBot>();
		for (int i = 0; i < 4; i++) {
			droidList.add(droidBot);
		}
		
		sortedList = prio.sortList(droidList, sTick);
		
		if (sortedList.get(0).getType() == TargetPrioritizer.DROID) {
			isDroidList = true;
		}
		
		assertEquals("Fails to sort list", true, isDroidList);
	}

	// Tests the functions regarding what types are a live
	// and their quantity
	@Test
	public void botsAlive() {
		int normals;
		int droids;
		boolean leaderAlive;

		normals = prio.amountNormalBotAlive(enemies);
		droids = prio.amountDroidAlive(enemies);
		leaderAlive = prio.leaderAlive(enemies);

		// Checks the current list
		assertEquals("Number of normal bots is incorrect", 1, normals, 0.01d);
		assertEquals("Number of droids is incorrect", 1, droids, 0.01d);
		assertEquals("Fails to correctly check if leader is alive", true, leaderAlive);

		// Alter the list to reach all branches
		enemies.get(2).setEnergy(0);
		leaderAlive = prio.leaderAlive(enemies);
		assertEquals("Fails to correctly check if leader is dead", false, leaderAlive);
	}

	// Test the functions wich places each kind of robot
	// first in the enemy list.

	@Test
	public void testPlaceLeader() {
		// placeLeaderFirst - function
		boolean isLeaderFirst = false;
		ArrayList<EnemyBot> leaderFirst = prio.placeLeaderBotFirst(enemies);

		if (leaderFirst.get(0).getEnergy() == leaderEnergy) {
			isLeaderFirst = true;
		}

		assertEquals("Fails to place leader bot first", true, isLeaderFirst);
	}

	@Test
	public void testPlaceNormal() {
		// placeNormalBotFirst - function
		// Do more to check for more bots for more coverage
		EnemyBot extraBot = new EnemyBot(mock);
		// Update twice to make sure it gets type normal before losing energy
		extraBot.update(sBearing, sDistance, normalEnergy, sHeading, sVelocity, sTick, "Extra1");
		extraBot.update(sBearing, sDistance, normalEnergy - 50, sHeading, sVelocity, sTick, "Extra1");
		enemies.add(extraBot);
		
		boolean isNormalFirst = false;
		ArrayList<EnemyBot> normalFirst = prio.placeNormalBotFirst(enemies);

		if (normalFirst.get(0).getType() == TargetPrioritizer.NORMAL_BOT) {
			isNormalFirst = true;
		}

		assertEquals("Fails to place normal bot first", true, isNormalFirst);
	}

	@Test
	public void testPlaceDroid() {
		// placeDroidFirst - function
		// Do more to check for more bots for more coverage
		boolean isDroidFirst = false;
		ArrayList<EnemyBot> droidFirst = prio.placeDroidFirst(enemies);

		if (droidFirst.get(0).getEnergy() == droidEnergy) {
			isDroidFirst = true;
		}

		assertEquals("Fails to place doirds first", true, isDroidFirst);
	}
	
	@Test
	public void testPlaceDead () {
		// placeDeadBotLast - function
		EnemyBot deadBot = new EnemyBot(mock);
		deadBot.update(sBearing, sDistance, 0, sHeading, sVelocity, sTick, "DeadBot");
		enemies.add(deadBot);
		
		boolean isDeadLast = false;
		ArrayList<EnemyBot> deadLast = prio.placeDeadBotsLast(enemies);
		
		if (enemies.get(enemies.size() - 1).getEnergy() == 0) {
			isDeadLast = true;
		}
		
		assertEquals("Fails to place dead bots last", true, isDeadLast);
	}
}
