package group07test;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.AccessibleObject;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import group07.MathUtils;

public class MathUtilsTest {
	
	private double angle;
	private Random r;
	
	private double x, y; // Koordinater, linjäritet
	private double heading;
	private double time;
	private double velocity;
	
	private double x1, x2, y1, y2; // Koordinater, riktning
	
	private double degAngle;
	
	@Before
	public void setUp () {
		degAngle = 180; // Startvärde att göra om till radianer.
		heading = 90; // Pi/2 radianer -> sin(heading) = 1
		time = 100;
		x = 15;
		y = 15;
		velocity = 1;
	}
	
	@After
	public void tearDown () {
		angle = 0; 
	}
	
	@Test
	public void testBearing () {
		angle = -1080;
		assertEquals("nomralizeBearing ger fel värde", 0, MathUtils.normalizeBearing(angle), 0.1d);
		angle = 1080;
		assertEquals("nomralizeBearing ger fel värde", 0, MathUtils.normalizeBearing(angle), 0.1d);
		// Vi kollar ovan alla branches i normalizeBearing.
		
		
	}
	
	@Test
	public void toRadians () {
		assertEquals("Fel värde på vinkeln (radianer)", Math.PI, MathUtils.toRadians(degAngle), 0.1d);
	}
	
	@Test
	public void testBulletVelocity () {
		
	}
}
