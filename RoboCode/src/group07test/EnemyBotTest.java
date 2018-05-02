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
		mock = new MockBot("MrRobot", 100, 10, 10, 10);
		enemy = new EnemyBot(mock);
		
		bearing = 10;
		distance = 10;
		energy = 100;
		heading = 10;
		velocity = 10;
		tick = 1000;
		type = 0;
		
	}
	
	@After
	public void tearDown() {
		mock = null;
		enemy = null;
	}
	
	@Test
	public void none() {
		assertEquals("Fienden heter något trots att den inte borde det!", true, enemy.none());
		enemy.update(bearing, distance, energy, heading, velocity, tick, "Enemy");
		assertEquals("Fienden heter ingenting.", false, enemy.none());
	}
	
	@Test
	public void getters () {
		enemy.update(bearing, distance, energy, heading, velocity, tick, name);
		
		assertEquals("Fiendens bearing (grader) är fel", bearing, enemy.getBearing(), 0.1d);
		assertEquals("Fiendens bearing (radianer) är fel", Math.toRadians(bearing), enemy.getBearingRadians(), 0.1d);
		assertEquals("Fiendens distance är fel", distance, enemy.getDistance(), 0.1d);
		assertEquals("Fiendens energy är fel", energy, enemy.getEnergy(), 0.1d);
		assertEquals("Fiendens heading är fel", heading, enemy.getHeading(), 0.1d);
		assertEquals("Fiendens velocity är fel", velocity, enemy.getVelocity(), 0.1d);
		assertEquals("Fiendens name är fel", name, enemy.getName());
		assertEquals("Fiendens tick är fel", tick, enemy.getTick());
		assertEquals("Fiendens type är fel", type, enemy.getType(), 0.4d);
		
		// setEnergy??
	}
}
