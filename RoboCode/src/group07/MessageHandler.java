package group07;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import robocode.MessageEvent;

public class MessageHandler {
	Robot07 robot;

	public MessageHandler(Robot07 robot) {
		this.robot = robot;
	}

	// Skickar iväg ett meddelande
	// receiver == 1 skicka till alla.
	// receiver == 2 skicka till alla mrRobot.
	// receiver != 1 || 2 skicka till receiver.
	public void send(String message, String receiver) {

		if (receiver.contains("1")) {

			try {
				robot.broadcastMessage(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (receiver.contains("2")) {

			// Kolla vilka robotar i laget som �r Mr.robots
			// Skicka till dessa robotar

			ArrayList<Ally> mrrobots = robot.getAllies();

			// Loopar listan och skickar till de robotar som �r Mrrobots.

			for (int i = 0; i < robot.getAllies().size(); i++) {
				if (mrrobots.get(i).isMrRobot()) {
					try {
						robot.sendMessage(mrrobots.get(i).getName(), message);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			try {
				robot.sendMessage(receiver, message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// Tar emot ett Message och uppdaterar alla variablar här
	public void recieve(MessageEvent e, AllyTracker allyTracker, EnemyTracker enemyTracker) {
		ArrayList<String> rowsInMessage = new ArrayList<>();
		rowsInMessage.addAll(Arrays.asList(e.getMessage().toString().split("\n")));

		for (String k : rowsInMessage) {
			ArrayList<String> infoInRow = new ArrayList<>();
			infoInRow.addAll(Arrays.asList(k.split(";")));

			if (k.contains("leadership")) {
				// I nuläget vill vi inte göra något med denna infon
			} else if (k.contains("teamMode")) {
				// I nuläget vill vi inte göra något med denna infon
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
				// I nuläget vill vi inte göra något med denna infon
			} else if (k.contains("enemyPos")) {
				// I nuläget vill vi inte göra något med denna infon
			} else if (k.contains("targetEnemy")) {
				// I nuläget vill vi inte göra något med denna infon
			} else if (k.contains("targetPos")) {
				// I nuläget vill vi inte göra något med denna infon
			} else if (k.contains("moveTo")) {
				// I nuläget vill vi inte göra något med denna infon
			} else if (k.contains("rShot")) {

				// göra saker med hur roboten rör sig
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
			} else if (k.contains("rEnemy") && infoInRow.size() == 8) {
				// Uppdatera listan om infon e nyare
				enemyTracker.update(Double.parseDouble(infoInRow.get(1)), Double.parseDouble(infoInRow.get(2)),
						Double.parseDouble(infoInRow.get(3)), Double.parseDouble(infoInRow.get(4)),
						Double.parseDouble(infoInRow.get(5)), Long.parseLong(infoInRow.get(6)), infoInRow.get(7));

			}
		}
	}
}
