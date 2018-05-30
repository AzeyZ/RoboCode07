package group07test;

import static org.junit.Assert.assertTrue;

import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.BattleMessageEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IRobotSnapshot;
import robocode.control.testing.RobotTestBed;

public class ST_F4_1_RambotAvoid extends RobotTestBed {

	private String ROBOT_UNDER_TEST = "group07.MrRobot*";
	private String ENEMY_ROBOTS = "sample.RamFire";
	private int NBR_ROUNDS = 1000;
	private boolean PRINT_DEBUG = false;

	boolean neverHit = true;
	int countHits = 0;

	
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
		assertTrue("The bot was hit by rambot: " + countHits + " times.", neverHit);
	}

	@Override
	public void onTurnEnded(TurnEndedEvent event) {
		IRobotSnapshot robot = event.getTurnSnapshot().getRobots()[0];
		
		if (robot.getState().isHitRobot()){
			neverHit = false;
			countHits++;
		}	
	}

	@Override
	public void onBattleMessage(BattleMessageEvent event) {
		event.getMessage();
	}
}
