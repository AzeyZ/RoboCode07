package group07test;

import static org.junit.Assert.assertTrue;

import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.BattleMessageEvent;
import robocode.control.testing.RobotTestBed;

public class ST_Q_1vs1Rut extends RobotTestBed{
	private String ROBOT_UNDER_TEST = "group07.MrRobot*";
	private String ENEMY_ROBOTS = "grupp1.RUT";
	private int NBR_ROUNDS = 1000;
	private double TRESHHOLD = 1;
	private boolean PRINT_DEBUG = false;

	int wins = 0;

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
		wins = event.getIndexedResults()[0].getFirsts();
		assertTrue("The Bot suck get more wins: " + wins + " wins. "+ event.getIndexedResults()[0].getTeamLeaderName(), wins / (double)NBR_ROUNDS >= TRESHHOLD);
		// Insert assert for countWallHits
	}

	@Override
	public void onBattleMessage(BattleMessageEvent event) {
		event.getMessage();
	}



}
