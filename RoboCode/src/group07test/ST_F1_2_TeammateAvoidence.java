package group07test;

import static org.junit.Assert.assertTrue;

import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.BattleMessageEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IRobotSnapshot;
import robocode.control.testing.RobotTestBed;

public class ST_F1_2_TeammateAvoidence extends RobotTestBed {

	private String ROBOT_UNDER_TEST = "group07.MrRobot*";
	private String ENEMY_ROBOTS = "group07.MrRobot*,group07.MrRobot*,group07.MrRobot*,group07.MrRobot*,sample.Crazy";
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
		assertTrue("The bot crashed into other players " + countAllyHits + " times.", 0.02 > (double)countAllyHits / (double)NBR_ROUNDS);
		//assertTrue("The bot crashed into other players " + countAllyHits + " times.", neverCrashedIntoAlly);
	}

	@Override
	public void onTurnEnded(TurnEndedEvent event) {
		IRobotSnapshot robot = event.getTurnSnapshot().getRobots()[0];
		
		if (robot.getState().isHitRobot()){
			neverCrashedIntoAlly = false;
			countAllyHits++;
		}	
	}

	@Override
	public void onBattleMessage(BattleMessageEvent event) {
		event.getMessage();
	}
}
