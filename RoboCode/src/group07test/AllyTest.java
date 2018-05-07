package group07test;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import group07.Ally;

public class AllyTest {
	private Ally ally;
	private Ally otherAlly;
	Random rand = new Random();
	@Before
	public void setUp() {
		
	}
	
	@After
	public void tearDown() {
		ally = null;
	}
	
	@Test
	public void getName() {
		ally = new Ally("IAmYourFriend");
		assertEquals("Test if name is correct", "IAmYourFriend", ally.getName());
	}
	@Test
	public void isSameType() {
		ally = new Ally("IAmYourFriendRobot07546wasd_k lMrRobot");

		otherAlly = new Ally("IAmYourFriendR546wasd_k l");
		assertEquals("Test if name is correct", true, ally.isMrRobot());
		assertEquals(false, otherAlly.isMrRobot());
	}
	@Test
	public void testXnY() {
		ally = new Ally("IAmYourFriend");
		int x = rand.nextInt(401);
		int y = rand.nextInt(401);
		ally.setX(x);
		ally.setY(y);
		assertEquals("Test if x-cord is correct", x, ally.getX(), 0.01d);
		assertEquals("Test if y-cord is correct", y, ally.getY(), 0.01d);
	}
	
	@Test
	public void testUpdate () {
		double x = 10;
		double y = 10;
		long tick = 100; // Startvärde
		long newTick = 1000; // Kallas vid uppdatering
		ally = new Ally("IAmYourFriend");
		ally.update(x, y, tick);
		ally.setTick(newTick);
		
		assertEquals("Test if x from update is correct", x, ally.getX(), 0.1d);
		assertEquals("Test if y from update is correct", y, ally.getY(), 0.1d);
		assertEquals("Test if tick is correct", newTick, ally.getTick(), 0.1d);
	}
}
