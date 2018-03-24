package group07;

import java.io.Serializable;

import robocode.MessageEvent;
// https://stackoverflow.com/questions/3429921/what-does-serializable-mean
public class Message implements Serializable  {
	// behövs en private static final long seralVersionUID = ???;
	private String leadership, teamMode, myPos, enemyPos, targetEnemy, targetPos, moveTo;
	
	public Message(String leadership, String teamMode, String myPos, String enemyPos, String targetEnemy,
			String targetPos, String moveTo) {
		super();
		this.leadership = leadership;
		this.teamMode = teamMode;
		this.myPos = myPos;
		this.enemyPos = enemyPos;
		this.targetEnemy = targetEnemy;
		this.targetPos = targetPos;
		this.moveTo = moveTo;
	}

	@Override
	public String toString() {
		return "Message [leadership=" + leadership + ", teamMode=" + teamMode + ", myPos=" + myPos + ", enemyPos="
				+ enemyPos + ", targetEnemy=" + targetEnemy + ", targetPos=" + targetPos + ", moveTo=" + moveTo + "]";
	}

	public String getLeadership() {
		return leadership;
	}

	public void setLeadership(String leadership) {
		this.leadership = leadership;
	}

	public String getTeamMode() {
		return teamMode;
	}

	public void setTeamMode(String teamMode) {
		this.teamMode = teamMode;
	}

	public String getMyPos() {
		return myPos;
	}

	public void setMyPos(String myPos) {
		this.myPos = myPos;
	}

	public String getEnemyPos() {
		return enemyPos;
	}

	public void setEnemyPos(String enemyPos) {
		this.enemyPos = enemyPos;
	}

	public String getTargetEnemy() {
		return targetEnemy;
	}

	public void setTargetEnemy(String targetEnemy) {
		this.targetEnemy = targetEnemy;
	}

	public String getTargetPos() {
		return targetPos;
	}

	public void setTargetPos(String targetPos) {
		this.targetPos = targetPos;
	}

	public String getMoveTo() {
		return moveTo;
	}

	public void setMoveTo(String moveTo) {
		this.moveTo = moveTo;
	}



}
