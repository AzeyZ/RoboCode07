package group07test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ST_F1_WallsAvoided.class, 
	ST_Q_1vs1SpinBot.class, ST_Q_1vs1Crazy.class, ST_Q_1vs1Rut.class})
public class Robot07SystemTestSuite {

}
