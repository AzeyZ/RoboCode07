package group07test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.AccessibleObject;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import group07.Radar;
import group07.EnemyBot;
import group07test.MockBot;

public class RadarTest {
	private Radar radar;
	private MockBot mockBot;
	private EnemyBot enemyBot;
	Random rand = new Random();
	
	@Before
	public void setUp() {
		double x = 50;
		double y = 50;
		double fakeEnergy = 100;
		double fakeHeading = 2;
		
		mockBot = new MockBot("Robot07Ally",fakeEnergy ,fakeHeading,x , y);
		radar = new Radar (mockBot);
		enemyBot = new EnemyBot(mockBot);
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
	
	@Test
	public void scan () {
		enemyBot.update(10, 10, 10, 10, 10, 1000, "Enemy");
		radar.update(enemyBot);
		double prevHeading = mockBot.getHeading();
		radar.scan();
		assertEquals("Mr.Robot vrider radarn", mockBot.getHeading(), prevHeading, 0.1d);
	}
	
	@Test
	public void scanTarget () {
		
	}
	
	@Test
	public void gotFocus () {
		assertEquals("Mr.Robot har fokus", true, radar.gotFocus());
//		radar.scan();
//		radar.scan();
//		assertEquals("Mr.Robot har tappar fokus", false, radar.gotFocus());
	}
}
