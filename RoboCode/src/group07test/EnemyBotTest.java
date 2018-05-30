package group07test;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import group07.EnemyBot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import group07.Ally;

public class EnemyBotTest {

	private EnemyBot enemy;
	private MockBot mock;
	
	private double bearing, distance, energy, heading, velocity;
	private long tick;
	private int  type;
	private String name;
	
	@Before
	public void setUp() {
		bearing = 10;
		distance = 10;
		energy = 100;
		heading = 10;
		velocity = 10;
		tick = 1000;
		type = 2;
	}
	
	@After
	public void tearDown() {
		mock = null;
		enemy = null;
	}
	
//	@Test
//	public void none() {
//		mock = new MockBot("MrRobot", 100, 10, 10, 10);
//		enemy = new EnemyBot(mock);
//
//		enemy.update(bearing, distance, energy, heading, velocity, tick, "");
//		assertEquals("Fienden heter n�got trots att den inte borde det!", true, enemy.none());
//		enemy.update(bearing, distance, energy, heading, velocity, tick, "Enemy");
//		assertEquals("Fienden heter ingenting.", false, enemy.none());
//	}
	
	@Test
	public void testUpdate () {
		// Types of robots
		// type < 105 = normal
		// type < 130 = droid
		// type > 130 = leader
		int type = 90;
		
		for (int i = 0; i < 3; i++) {
			EnemyBot temp = new EnemyBot(mock);
			temp.update(bearing, distance, type, heading, velocity, tick, "TempBot");
			type += 30;
		}
	}
	
	@Test
	public void getters () {
		int newEnergy = 100;
		
		mock = new MockBot("MrRobot", 100, 10, 10, 10);
		enemy = new EnemyBot(mock);
		
		enemy.update(bearing, distance, energy, heading, velocity, tick, name);
		
		assertEquals("Fiendens bearing (grader) �r fel", bearing, enemy.getBearing(), 0.1d);
		assertEquals("Fiendens bearing (radianer) �r fel", Math.toRadians(bearing), enemy.getBearingRadians(), 0.1d);
		assertEquals("Fiendens distance �r fel", distance, enemy.getDistance(), 0.1d);
		assertEquals("Fiendens energy �r fel", energy, enemy.getEnergy(), 0.1d);
		assertEquals("Fiendens heading �r fel", heading, enemy.getHeading(), 0.1d);
		assertEquals("Fiendens velocity �r fel", velocity, enemy.getVelocity(), 0.1d);
		assertEquals("Fiendens name �r fel", name, enemy.getName());
		assertEquals("Fiendens tick �r fel", tick, enemy.getTick());
		assertEquals("Fiendens type �r fel", type, enemy.getType(), 0.4d);
		
		// One setter
		//enemy.setEnergy(newEnergy);
		//assertEquals("Check the energy", newEnergy, enemy.getEnergy(), 0.1d);
	}
}
