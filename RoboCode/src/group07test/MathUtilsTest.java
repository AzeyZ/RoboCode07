package group07test;

import static org.junit.Assert.assertEquals;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
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
	public void testAbsoluteBearing () {
		// These will be thrown around to adjust other values
		double x1 = 1;
		double x2 = 2;
		double y1 = 1;
		double y2 = 2;
		
		// These will be test values
		double xo = x2 - x1;
		double yo = y2 - y1;
		double hyp = Math.sqrt(Math.pow(xo, 2) + Math.pow(yo, 2));
		double arcSin = Math.toDegrees(Math.asin(xo/hyp));
		
		// bearing will be adjusted
		double bearing = arcSin;
		assertEquals("Fails to calc bearing (xo > 0 && yo > 0)", bearing, MathUtils.absoluteBearing(x1, y1, x2, y2), 0.01d);
		
		// Next branch, update variables
		xo = x1 - x2;	// To get negative x
		hyp = Math.sqrt(Math.pow(xo, 2) + Math.pow(yo, 2));
		arcSin = Math.toDegrees(Math.asin(xo/hyp));
		bearing = 360 + arcSin;
		assertEquals("Failt to calc bearing (xo < 0 && yo > 0)", bearing, MathUtils.absoluteBearing(x2, y1, x1, y2), 0.01d);
		
		// Next branch, update variables
		xo = x2 - x1;
		yo = y1 - y2;
		hyp = Math.sqrt(Math.pow(xo, 2) + Math.pow(yo, 2));
		arcSin = Math.toDegrees(Math.asin(xo/hyp));
		bearing = 180 - arcSin;
		assertEquals("Failt to calc bearing (xo > 0 && yo < 0)", bearing, MathUtils.absoluteBearing(x1, y2, x2, y1), 0.01d);
		
		// Next branch, update variables
		xo = x1 - x2;
		yo = y1 - y2;
		hyp = Math.sqrt(Math.pow(xo, 2) + Math.pow(yo, 2));
		arcSin = Math.toDegrees(Math.asin(xo/hyp));
		bearing = 180 - arcSin;
		assertEquals("Failt to calc bearing (xo < 0 && yo < 0)", bearing, MathUtils.absoluteBearing(x2, y2, x1, y1), 0.01d);
	}
	
	@Test
	public void testBulletVelocity () {
		double power = 2;	// To calculate velocity
		double bVelocity = 20.0 - 3.0 * power;	// bulletVelocity
		
		assertEquals("Fails to calc bullet velocity", bVelocity, MathUtils.bulletVelocity(power), 0.1d);
	}
	
	@Test
	public void testDistance () {
		double x1 = 1;
		double y1 = 1;
		double x2 = 2;
		double y2 = 2;
		
		double hyp = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2- y1), 2));
		assertEquals("Fails to calculate distance", hyp, MathUtils.distance(x1, y1, x2, y2), 0.1d);
	}
	
	@Test
	public void testSign () {
		double v = -1;	// To be adjusted oncce to reach all branches
		assertEquals("Fails to give correct sign", -1, MathUtils.sign(v), 0.0d);
		
		v = 1;
		assertEquals("Fails to give correct sign", 1, MathUtils.sign(v), 0.0d);
	}
	
	@Test
	public void testMinMax () {
		int v = 1;		// For comparison with the other variables
		int min = 2;	// Bigger than v, v will not på used (?)
		int max = 3;	// Bigger than v, max will not be used (?)
		
		assertEquals("Fails to do minMax", min, MathUtils.minMax(v, min, max), 0.1d);
	}
	
	@Test
	public void testTrig () {	// distance and absoluteBearing
		Point2D source = new Point2D.Double(10, 10);
		Point2D target = new Point2D.Double(20, 20);
		
		double absBearing = Math.atan2(target.getX() - source.getX(), target.getY() - source.getY());
		assertEquals("Fails to calculate absolute bearing", absBearing, MathUtils.absoluteBearing(source, target), 0.1d);
		
		double distance = Math.hypot(target.getX() - source.getX(), target.getY() - source.getY());
		assertEquals("Fails to calculate distance", distance, MathUtils.distance(source, target), 0.1d);
	}
}
