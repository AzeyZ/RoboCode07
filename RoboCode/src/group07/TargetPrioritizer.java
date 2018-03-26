package group07;

import java.util.ArrayList;

// Kanske borde lägga till mer på hur mycket hp som EnemyBot har kvar.
public class TargetPrioritizer {
	public static final int LEADER_BOT = 0;
	public static final int DROID = 1;
	public static final int NORMAL_BOT = 2;
	private int amountEnemyBot = 0;
	private int amountLeader;
	private int amountDroid;
	private int amountNormalRobot;
	private ArrayList<EnemyBot> temp;

	public TargetPrioritizer() {

	}

	public ArrayList<EnemyBot> sort(ArrayList<EnemyBot> enemyBot) {
		amountLeader = 0;
		amountDroid = 0;
		amountNormalRobot = 0;
		temp = enemyBot;
		if (amountEnemyBot < enemyBot.size()) {
			amountEnemyBot = enemyBot.size();
		}
		for (EnemyBot k : enemyBot) {
			if (k.getEnergy() != 0) {
				switch (k.getType()) {
				case LEADER_BOT: {
					amountLeader++;
					break;
				}
				case DROID: {
					amountDroid++;
					break;
				}
				case NORMAL_BOT: {
					amountNormalRobot++;
					break;
				}
				default: {
					break;
				}
				}
			}
		}
		sortLivingBots(temp.size() - (placeDeadLast(enemyBot) + 1));
		placeDeadLast(enemyBot);
		return temp;
	}

	private void sortLivingBots(int amountAlive) {
		switch (amountAlive) {
		case 1: {
			break;
		}
		case 2: {
			for (EnemyBot k : temp) {
				if (amountDroid == 1) {
					if (k.getType() == LEADER_BOT && !(k.getEnergy() == 0)) {
						placeFirst(k);
						break;
					} else if (k.getType() == NORMAL_BOT) {
						placeFirst(k);
					}
				} else if (k.getType() == NORMAL_BOT) {
					placeFirst(k);
				}
			}
			break;
		}
		case 3: {
			switch (amountDroid) {

			case 0:
			case 1: {
				for (EnemyBot k : temp) {
					if (k.getType() == NORMAL_BOT) {
						placeFirst(k);
					}
				}
				break;
			}
			case 2: {
				for (EnemyBot k : temp) {
					if (k.getType() == LEADER_BOT && !(k.getEnergy() == 0)) {
						placeFirst(k);
						break;
					} else if (k.getType() == NORMAL_BOT) {
						placeFirst(k);
					}
				}
				break;
			}
			case 3: {
				for (EnemyBot k : temp) {
					if (k.getType() == DROID) {
						placeFirst(k);
					}
				}
				break;
			}
			default: {

			}

			}

			break;

		}
		case 4: {
			switch (amountDroid) {
			case 0:
			case 1: {
				for (EnemyBot k : temp) {
					if (k.getType() == NORMAL_BOT) {
						placeFirst(k);
					}
				}
				break;
			}
			case 2: {
				for (EnemyBot k : temp) {
					if (k.getType() == LEADER_BOT && !(k.getEnergy() == 0)) {
						placeFirst(k);
						break;
					} else if (k.getType() == NORMAL_BOT) {
						placeFirst(k);
					}
				}
				break;
			}
			case 3: {
				for (EnemyBot k : temp) {
					if (k.getType() == LEADER_BOT && !(k.getEnergy() == 0)) {
						placeFirst(k);
						break;
					} else if (k.getType() == NORMAL_BOT) {
						placeFirst(k);
					}
				}
				break;
			}
			case 4: {
				{
					for (EnemyBot k : temp) {
						if (k.getType() == DROID) {
							placeFirst(k);
						}
					}
					break;
				}

			}
			default: {

			}

			}
			break;
		}
		case 5: {
			switch (amountDroid) {
			case 0:
			case 1: {
				for (EnemyBot k : temp) {
					if (k.getType() == NORMAL_BOT) {
						placeFirst(k);
					}
				}
				break;
			}
			case 2:
			case 3:
			case 4: {
				for (EnemyBot k : temp) {
					if (k.getType() == LEADER_BOT) {
						placeFirst(k);
					}
				}
				break;
			}

			}

		}
		default: {

		}
		}
	}

	private int placeDeadLast(ArrayList<EnemyBot> enemyBot) {
		int deadBot = 0;
		for (EnemyBot k : enemyBot) {
			if (k.getEnergy() == 0) {
				placeLast(k);
				deadBot++;
			}

		}
		return deadBot;
	}

	private void placeLast(EnemyBot bot) {
		temp.remove(bot);
		temp.add(bot);
	}

	private void placeFirst(EnemyBot bot) {
		if (bot.getEnergy() == 0) {
			return;
		}
		if (bot.getType() == temp.get(0).getType()
				&& (temp.get(0).getEnergy() < bot.getEnergy() || temp.get(0).getEnergy() == 0)) {
			temp.remove(bot);
			temp.add(1, bot);
		} else {
			temp.remove(bot);
			temp.add(0, bot);
		}
	}

}
