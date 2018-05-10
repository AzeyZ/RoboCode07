package group07;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import robocode.MessageEvent;

/**
 * MessageHandler: Sends and receives messages.
 *
 */
public class MessageHandler {
	MrRobot robot;

	/**
	 * 
	 * @param robot
	 *            Instance of main class.
	 */
	public MessageHandler(MrRobot robot) {
		this.robot = robot;
	}

	/**
	 * send: Sending messages.
	 * 
	 * @param message
	 *            The string that should be sent.
	 * @param receiver
	 *            Receiver can contain 3 different types of Strings. The string can
	 *            contain "1" sending message to all teammates. The string can
	 *            contain "2" sending message to all MrRobots in the team. If the
	 *            string does not contain "1" or "2" send message to "receiver".
	 * 
	 */
	public void send(String message, String receiver) {

		if (receiver.equals("1")) {

			try {
				robot.broadcastMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (receiver.equals("2")) {

			ArrayList<Ally> mrrobots = robot.getAllies();

			for (int i = 0; i < mrrobots.size(); i++) {
				if (mrrobots.get(i).isMrRobot() && !mrrobots.get(i).getName().equals(robot.getName())) {
					try {
						robot.sendMessage(mrrobots.get(i).getName(), message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			try {
				robot.sendMessage(receiver, message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * receive: Handling received messages.
	 * 
	 * @param e
	 *            MessageEvent
	 * @param allyTracker
	 *            Instance of AllyTracker
	 * @param enemyTracker
	 *            Instance of EnemyTracker
	 * @param radarControl
	 *            Instance of RadarControl
	 */
	public void receive(MessageEvent e, AllyTracker allyTracker, EnemyTracker enemyTracker, RadarControl radarControl) {
		ArrayList<String> rowsInMessage = new ArrayList<>();
		rowsInMessage.addAll(Arrays.asList(e.getMessage().toString().split("\n")));

		for (String k : rowsInMessage) {
			ArrayList<String> infoInRow = new ArrayList<>();
			infoInRow.addAll(Arrays.asList(k.split(";")));
			if (k.contains("myPos")) {
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
<<<<<<< HEAD
			} else if (k.contains("friendPos")) {
				// I nuläget vill vi inte göra något med denna infon�
			} else if (k.contains("enemyDetails")) {
				// I nuläget vill vi inte göra något med denna infon

			} else if (k.contains("bullet")) {
				// I nuläget vill vi inte göra något med denna infon
			}

			else if (k.contains("enemyPos")) {
				// I nuläget vill vi inte göra något med denna infon
			} else if (k.contains("targetEnemy")) {
				// I nuläget vill vi inte göra något med denna infon
			} else if (k.contains("targetPos")) {
				// I nuläget vill vi inte göra något med denna infon
			} else if (k.contains("moveTo")) {
				// I nuläget vill vi inte göra något med denna infon
			} else if (k.contains("rShot")) {

				// göra saker med hur roboten rör sig
=======
>>>>>>> branch 'Beta.0.5.1' of https://github.com/AzeyZ/RoboCode07.git
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
				enemyTracker.msgUpdate(Double.parseDouble(infoInRow.get(1)), Double.parseDouble(infoInRow.get(2)),
						Double.parseDouble(infoInRow.get(3)), Double.parseDouble(infoInRow.get(4)),
						Double.parseDouble(infoInRow.get(5)), Long.parseLong(infoInRow.get(6)), infoInRow.get(7));
				enemyTracker.updateTarget();

			} else if (k.contains("rPickRadarTarget")) {
				radarControl.teammatePicked(e.getSender(), infoInRow.get(1), Integer.parseInt(infoInRow.get(2)));
			} else if (k.contains("rGettingAttacked")) {
				radarControl.teammateGettingAttacked(e.getSender(), infoInRow.get(1), infoInRow.get(2));
			} else if (k.contains("rNewRadarTarget")) {
				radarControl.teammateNewTarget(e.getSender(), infoInRow.get(1));
			} else if (k.contains("rSetGunTarget")) {
				enemyTracker.msgUpdateTarget(infoInRow.get(1));
			}

		}
	}
}
