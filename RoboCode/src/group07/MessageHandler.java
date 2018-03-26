package group07;

import java.io.IOException;

public class MessageHandler {
	Robot07 robot;
	
	// Här sparas alla värden
	private String leadership;
	private String teamMode;
	private double myX;
	private double myY;
	private double enemyX;
	private double enemyY;
	private String targetName;
	private double targetX;
	private double targetY;
	private double moveToX;
	private double moveToY;
	
	
	public MessageHandler(Robot07 robot) {
		this.robot = robot;
	}
	
	// Skickar iväg ett meddelande
	public void send(Message message) {
		try {
			robot.broadcastMessage(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Tar emot ett Message och uppdaterar alla variablar här
	public void recieve(Message e) {
		// Leadership
		leadership = e.getLeadership();
		// Team Mode
		teamMode = e.getTeamMode();
		// My X Y
		String pos = e.getMyPos();
		String[] parts;
		String x;
		String y;
		if(pos != "") {
			parts = pos.split(";");
			x = parts[0];
			y = parts[1];
			myX = Double.parseDouble(x);
			myY = Double.parseDouble(y);
		}

		
		// Enemy X Y array
		pos = e.getEnemyPos();
		if(pos != "") {
			parts = pos.split(";");
			x = parts[0];
			y = parts[1];
			enemyX = Double.parseDouble(x);
			enemyY = Double.parseDouble(y);			
		}
		// Target Name
		targetName = e.getTargetEnemy();
		// Target X Y
		pos = e.getTargetPos();
		if(pos != "") {
			parts = pos.split(";");
			x = parts[0];
			y = parts[1];
			targetX = Double.parseDouble(x);
			targetY = Double.parseDouble(y);
		}

		
		// Move To X Y
		pos = e.getMoveTo();
		if(pos != "") {
			parts = pos.split(";");
			x = parts[0];
			y = parts[1];
			moveToX = Double.parseDouble(x);
			moveToY = Double.parseDouble(y);
		}

	}

	
	// Getters
	public double getEnemyX() {
		return enemyX;
	}
	
	public double getEnemyY() {
		return enemyY;
	}
	
	public String getLeadership() {
		return leadership;
	}

	public String getTeamMode() {
		return teamMode;
	}

	public double getMyX() {
		return myX;
	}

	public double getMyY() {
		return myY;
	}

	public String getTargetName() {
		return targetName;
	}

	public double getTargetX() {
		return targetX;
	}

	public double getTargetY() {
		return targetY;
	}

	public double getMoveToX() {
		return moveToX;
	}

	public double getMoveToY() {
		return moveToY;
	}
}


