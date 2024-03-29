package group07test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AllyTest.class, MessageWriterTest.class, 
	EnemyBotTest.class, MathUtilsTest.class, SurfMovementTest.class, 
	AllyTrackerTest.class, TargetEnemyPrioritizerTest.class, 
	EnemyTrackerTest.class, MovementTest.class, EnemyTrackerTest.class, 
	MessageHandlerTest.class })

public class TestAllClasses {

}
