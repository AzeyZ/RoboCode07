package group07test;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import group07.Ally;

public class AllyTest {
	private Ally ally;
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
		ally = new Ally("IAmYourFriendRobot07546wasd_k l");
		assertEquals("Test if name is correct", true, ally.isSame());
	}
	@Test
	public void testXnY() {
		ally = new Ally("IAmYourFriend");
		int x = rand.nextInt(401);
		int y = rand.nextInt(401);
		ally.setX(x);
		ally.setY(y);
		assertEquals("Test if x-cord is correct", x, ally.getX());
		assertEquals("Test if y-cord is correct", y, ally.getY());
	}
	
}
