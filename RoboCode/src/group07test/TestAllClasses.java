package group07test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({AllyTest.class, MessageWriterTest.class, 
	EnemyBotTest.class, MathUtilsTest.class, 
	AllyTrackerTest.class, TargetEnemyPrioritizerTest.class, 
	EnemyTrackerTest.class, MovementTest.class})

public class TestAllClasses {

}
 