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
	private String ENEMY_ROBOTS = "group07.MrRobot*";
	private int NBR_ROUNDS = 1000;
	private boolean PRINT_DEBUG = false;

	boolean neverShotTooFar = true;

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
	}

	@Override
	public void onTurnEnded(TurnEndedEvent event) {
		IRobotSnapshot[] robots = event.getTurnSnapshot().getRobots();
		robots[0].getName();
		for (IBulletSnapshot bullet : event.getTurnSnapshot().getBullets()) {
			if (bullet.getState().FIRED.isActive() == true && bullet.getOwnerIndex() == robots[0].getRobotIndex()
					&& 900 >= (robots[0].getX() - robots[1].getX())*(robots[0].getX() - robots[1].getX()) * (robots[0].getY() - robots[1].getY())*(robots[0].getY() - robots[1].getY())) {
				neverShotTooFar = false;
			}
		}

	}

	@Override
	public void onBattleMessage(BattleMessageEvent event) {
		event.getMessage();
	}
}