package group07test;

import static org.junit.Assert.assertTrue;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.BattleMessageEvent;
import robocode.control.events.RoundEndedEvent;
import robocode.control.events.RoundStartedEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IBulletSnapshot;
import robocode.control.snapshot.IRobotSnapshot;
import robocode.control.testing.RobotTestBed;

@RunWith(JUnit4.class)
public class ST_F2_1 extends RobotTestBed {

	private String ROBOT_UNDER_TEST = "group07.MrRobot*";
	private String ENEMY_ROBOTS = "sample.SpinBot";
	private int NBR_ROUNDS = 1000;
	private boolean PRINT_DEBUG = false;

	boolean neverShotTooFar = true;
	private int fired = 0;
	private int hits;

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
		assertTrue("The bot shot too far away from an enemy.", neverShotTooFar);
		assertTrue("Try dodging more. " + (double)hits/fired + " Hit rate", 0.0 >= (double) hits/fired);
	}

	@Override
	public void onTurnEnded(TurnEndedEvent event) {
		IRobotSnapshot[] robots = event.getTurnSnapshot().getRobots();
		robots[0].getName();
		for (IBulletSnapshot bullet : event.getTurnSnapshot().getBullets()) {
			if (bullet.getState().FIRED.isActive() == true && bullet.getOwnerIndex() == robots[0].getRobotIndex()
					&& 900 < Math.sqrt(Math.pow(robots[0].getX() - robots[1].getX(), 2) + Math.pow(robots[0].getY() - robots[1].getY(), 2))) {
				neverShotTooFar = false;
			}
			if (bullet.getState().FIRED.isActive() == true && bullet.getOwnerIndex() == robots[1].getRobotIndex()){
				fired ++;
			}
			if (bullet.getState().HIT_VICTIM.isActive() == true && bullet.getOwnerIndex() == robots[1].getRobotIndex()){
				hits ++;
			}
		}
	}

	@Override
	public void onBattleMessage(BattleMessageEvent event) {
		event.getMessage();
	}
}