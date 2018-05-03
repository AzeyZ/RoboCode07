package group07test;

import static org.junit.Assert.assertTrue;

import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.BattleMessageEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IRobotSnapshot;
import robocode.control.testing.RobotTestBed;

public class ST_F1_2_TeammateAvoidence extends RobotTestBed {

	private String ROBOT_UNDER_TEST = "group07.Robot07*";
	private String ENEMY_ROBOTS = "group07.Robot07*";
	private int NBR_ROUNDS = 1000;
	private boolean PRINT_DEBUG = false;
	private int TEAM_SIZE = 5;

	boolean neverCrashedIntoAlly = true;
	int countAllyHits = 0;

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
		assertTrue("The bot crashed into the wall " + countAllyHits + " times.", neverCrashedIntoAlly);
	}

	@Override
	public void onTurnEnded(TurnEndedEvent event) {
		IRobotSnapshot[] robots = event.getTurnSnapshot().getRobots();
		
		for(IRobotSnapshot robot: robots){
			if (robot.getTeamName().contains(ROBOT_UNDER_TEST) ){
				
			}
		}
		//
		if (true) {//TD Fix
			neverCrashedIntoAlly = false;
			countAllyHits++;
		}
	}

	@Override
	public void onBattleMessage(BattleMessageEvent event) {
		event.getMessage();
	}
}
