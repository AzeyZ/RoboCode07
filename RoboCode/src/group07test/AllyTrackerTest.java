package group07test;

import group07.AllyTracker;
import group07.Ally;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AllyTrackerTest {
	
	private AllyTracker allyTracker;
	private MockBot mockBot;
	
	@Before
	public void setUp () {
		mockBot = new MockBot("Mock", 100, 90, 10, 10);
		allyTracker = new AllyTracker(mockBot);
	}
	
	@After
	public void tearDown () {
		mockBot = null;
		allyTracker = null;
	}
	
	@Test
	public void testAllies () {
		ArrayList<Ally> allies = allyTracker.getAllyList();
		assertEquals("Lagmedlemmar returnas fel", allies, allyTracker.getAllyList());
	}
}
