package group07;

public class GunControl {
	private Robot07 MrRobot;
	private TargetEnemyBot target;

	public GunControl() {

	}

	public boolean takeShot(Robot07 MrRobot, TargetEnemyBot target) {
		this.MrRobot = MrRobot;
		this.target = target;
		return (distance() && target());
	}

	private boolean distance() {
		return MrRobot.getGunHeat() == 0 && Math.abs(MrRobot.getGunTurnRemaining()) < 2 && target.getDistance() < 500;
	}

	private boolean target() {
		return MrRobot.getRadar().gotFocus();

	}

}
