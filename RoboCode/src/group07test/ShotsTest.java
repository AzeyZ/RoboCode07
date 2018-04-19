package group07test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import group07.Shots;

public class ShotsTest {
	private double x = 10;
	private double y = 10;
	private long tick = 1000;
	private Shots shots = new Shots(x, y, tick);

	
	@Before
	public void setUp() {
		
	}
	
	@After
	public void tearDown () {
		shots = null;
	}
	
	@Test
	public void getX () {
		assertEquals("Test if x is correct", x, shots.getX(), 0.01d);
	}
	
	@Test
	public void getY () {
		assertEquals("Test if x is correct", y, shots.getY(), 0.01d);
	}
	
	@Test
	public void getTick () {
		assertEquals("Test if tick is correct", tick, shots.getTick(), 0.01d);
	}
}
