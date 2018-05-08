package group07test;

import static org.junit.Assert.assertTrue;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.BattleMessageEvent;
import robocode.control.events.RoundEndedEvent;
import robocode.control.events.RoundStartedEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IRobotSnapshot;
import robocode.control.testing.RobotTestBed;

@RunWith(JUnit4.class)
public class ST_F1_WallsAvoided extends RobotTestBed {
	
	
	private String ROBOT_UNDER_TEST = "group07.MrRobot*";
	private String ENEMY_ROBOTS = "group07.MrRobot*";
	private int NBR_ROUNDS = 1000;
	private boolean PRINT_DEBUG = false;
	
	boolean neverCrashedIntoAWall = true;
	int countWallHits = 0;

	@Override
	public String getRobotNames() {
		return ROBOT_UNDER_TEST + "," + ENEMY_ROBOTS;
	}

	@Override
	public int getNumRounds() {
		return NBR_ROUNDS;
	}

	@Override
	public String getInitialPositions() {
		return null;
	}

	@Override
	public boolean isDeterministic() {
		return true;
	}

	@Override
	protected int getExpectedErrors() {
		return 0;
	}


	@Override
	protected void runSetup() {
	}

	@Override
	protected void runTeardown() {
	}
	
	@Override
	public void onBattleCompleted(BattleCompletedEvent event) {
		assertTrue("The bot crashed into the wall " + countWallHits + " times.", neverCrashedIntoAWall);
		//Insert assert for countWallHits
	}
	
	@Override
	public void onTurnEnded(TurnEndedEvent event) {
	IRobotSnapshot robot = event.getTurnSnapshot().getRobots()[1];
	//
	if (robot.getState().isHitWall()) {
		neverCrashedIntoAWall = false;
		countWallHits++;
		}
	}
	
	@Override
	public void onBattleMessage(BattleMessageEvent event) {
		event.getMessage();
	}
}