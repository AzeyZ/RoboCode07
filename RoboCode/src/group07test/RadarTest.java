package group07test;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.AccessibleObject;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import group07.Radar;
import group07test.MockBot;

public class RadarTest {
	private Radar radar;
	private MockBot mockBot;
	Random rand = new Random();
	
	@Before
	public void setUp() {
		double x = 50;
		double y = 50;
		double fakeEnergy = 100;
		double fakeHeading = 2;
		
		mockBot = new MockBot("Robot07Ally",fakeEnergy ,fakeHeading,x , y);
	}
	
	@After
	public void tearDown() {
		radar = null;
		mockBot = null;
	}
	
	@Test
	public void getName() {
		radar = new Radar(mockBot);
/*		Class c;
		try {
			c = Class.forName("Radar");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Field lastScanned = c.getDeclaredField("lastScan");
		lastScanned.setAccessible(true);
		lastScanned.set(radar,5);*/
	}

}
