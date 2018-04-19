package group07;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import robocode.MessageEvent;

public class MessageHandler {
	Robot07 robot;

	// H�r sparas alla v�rden
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

	// Skickar iv�g ett meddelande
	public void send() {
		try {
			robot.broadcastMessage("test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Tar emot ett Message och uppdaterar alla variablar h�r
	public void recieve(MessageEvent e, AllyTracker allyTracker) {
		ArrayList<String> rowsInMessage = new ArrayList<>();
		rowsInMessage.addAll(Arrays.asList(e.getMessage().toString().split("\n")));
		for (String k : rowsInMessage) {
			ArrayList<String> infoInRow = new ArrayList<>();
			infoInRow.addAll(Arrays.asList(k.split(";")));
			if (k.contains("leadership")) {
				// I nul�get vill vi inte g�ra n�got med denna infon
			} else if (k.contains("teamMode")) {
				// I nul�get vill vi inte g�ra n�got med denna infon
			} else if (k.contains("myPos")) {
				// Uppdatera ally listan
				if (infoInRow.size() == 3) {
					String m_SenderName = e.getSender();
					for (Ally x : allyTracker.getAllyList()) {
						if (x.getName().equalsIgnoreCase(m_SenderName)) {
							x.update(Double.parseDouble(infoInRow.get(1)), Double.parseDouble(infoInRow.get(2)),
									robot.getTime());

						}
					}
				}
			} else if (k.contains("friendPos")) {
				// I nul�get vill vi inte g�ra n�got med denna infon
			} else if (k.contains("enemyPos")) {
				// I nul�get vill vi inte g�ra n�got med denna infon
			} else if (k.contains("targetEnemy")) {
				// I nul�get vill vi inte g�ra n�got med denna infon
			} else if (k.contains("targetPos")) {
				// I nul�get vill vi inte g�ra n�got med denna infon
			} else if (k.contains("moveTo")) {
				// I nul�get vill vi inte g�ra n�got med denna infon
			} else if (k.contains("rShot")) {
				// g�ra saker med hur roboten r�r sig
			} else if (k.contains("rAlly")) {
				// Uppdatera listan om infon e nyare
				if (infoInRow.size() == 5) {
					String m_allyName = infoInRow.get(1);
					for (Ally x : allyTracker.getAllyList()) {
						if (x.getName().equalsIgnoreCase(m_allyName)
								&& x.getTick() < Long.parseLong(infoInRow.get(4))) {
							x.update(Double.parseDouble(infoInRow.get(2)), Double.parseDouble(infoInRow.get(3)),
									Long.parseLong(infoInRow.get(4)));

						}
					}
				}
			} else if (k.contains("rEnemy")) {
				// Uppdatera listan om infon e nyare

			}
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

//// Leadership
// leadership = e.getLeadership();
// // Team Mode
// teamMode = e.getTeamMode();
// // My X Y
// String pos = e.getMyPos();
// String[] parts;
// String x;
// String y;
// if(pos != "") {
// parts = pos.split(";");
// x = parts[0];
// y = parts[1];
// myX = Double.parseDouble(x);
// myY = Double.parseDouble(y);
// }
//
//
// // Enemy X Y array
// pos = e.getEnemyPos();
// if(pos != "") {
// parts = pos.split(";");
// x = parts[0];
// y = parts[1];
// enemyX = Double.parseDouble(x);
// enemyY = Double.parseDouble(y);
// }
// // Target Name
// targetName = e.getTargetEnemy();
// // Target X Y
// pos = e.getTargetPos();
// if(pos != "") {
// parts = pos.split(";");
// x = parts[0];
// y = parts[1];
// targetX = Double.parseDouble(x);
// targetY = Double.parseDouble(y);
// }
//
//
// // Move To X Y
// pos = e.getMoveTo();
// if(pos != "") {
// parts = pos.split(";");
// x = parts[0];
// y = parts[1];
// moveToX = Double.parseDouble(x);
// moveToY = Double.parseDouble(y);
// }
