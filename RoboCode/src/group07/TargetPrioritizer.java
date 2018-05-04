package group07;

import java.util.ArrayList;

public class TargetPrioritizer {
	public static final int LEADER_BOT = 0; // LEADER_BOT används istället för att skriva en 0:a för att göra det
											// tydligare.
	public static final int DROID = 1;
	public static final int NORMAL_BOT = 2;
	private int amountAlive;
	private int amountDroidAlive;
	private boolean leaderAlive;
	private int amountNormalBotAlive;

	public TargetPrioritizer() {

	}

	public ArrayList<EnemyBot> sortList(ArrayList<EnemyBot> EnemyList, long time) {
		amountDroidAlive = amountDroidAlive(EnemyList); // Count the amount of each type to be able to
		amountNormalBotAlive = amountNormalBotAlive(EnemyList); // determine how class should be sorted
		leaderAlive = leaderAlive(EnemyList);
		amountAlive = amountNormalBotAlive + amountDroidAlive; // Counts the amount of living enemybots. if is used to
																// count if leader is alive.
		if (leaderAlive) {
			amountAlive++;
		}

		if (leaderAlive && amountDroidAlive > amountNormalBotAlive) { // checks if leader is alive and if we should
																		// focus it.
			EnemyList = placeLeaderBotFirst(EnemyList);
		} else if (amountNormalBotAlive != 0) { // Checks if the got any NormalEnemyBots if we do we focus it.
			EnemyList = placeNormalBotFirst(EnemyList);
		} else {
			EnemyList = placeDroidFirst(EnemyList); // Place the droids first in case we just got droids left.
		}
		EnemyList = placeOldInfoBotsLast(EnemyList, time); //place all bots that havent get any new info in the 5 last rounds.
		EnemyList = placeDeadBotsLast(EnemyList); // Place all dead bots last so we dont try to focus at a dead bot.
		
//		for (EnemyBot k : EnemyList) {
//			System.out.println(k.getName());
//		}
//		System.out.println("-----------------------");
		return EnemyList;
	}

	public int amountDroidAlive(ArrayList<EnemyBot> EnemyList) {
		int m_amountDroids = 0;
		for (EnemyBot k : EnemyList) {
			if (k.getEnergy() != 0 && k.getType() == DROID) {
				m_amountDroids++;
			}
		}
		return m_amountDroids;
	}

	public int amountNormalBotAlive(ArrayList<EnemyBot> EnemyList) {
		int m_amountNormalBot = 0;
		for (EnemyBot k : EnemyList) {
			if (k.getEnergy() != 0 && k.getType() == NORMAL_BOT) {
				m_amountNormalBot++;
			}
		}
		return m_amountNormalBot;
	}

	public boolean leaderAlive(ArrayList<EnemyBot> EnemyList) {
		for (EnemyBot k : EnemyList) {
			if (k.getEnergy() != 0 && k.getType() == LEADER_BOT) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<EnemyBot> placeLeaderBotFirst(ArrayList<EnemyBot> EnemyList) {
		ArrayList<EnemyBot> m_temp = new ArrayList<>();
		m_temp.addAll(EnemyList);
		for (EnemyBot k : EnemyList) {
			if (k.getType() == LEADER_BOT) {
				m_temp.remove(k);
				m_temp.add(0, k);
			}
		}
		return m_temp;
	}
	public ArrayList<EnemyBot> placeOldInfoBotsLast(ArrayList<EnemyBot> EnemyList, long time){
		ArrayList<EnemyBot> m_temp = new ArrayList<>();
		m_temp.addAll(EnemyList);
		for (EnemyBot k : EnemyList) {
			if (k.getTick() + 4 < time) {
				m_temp.remove(k);
				m_temp.add(k);
			}
		}
		return m_temp;
	}

	public ArrayList<EnemyBot> placeDeadBotsLast(ArrayList<EnemyBot> EnemyList) {
		ArrayList<EnemyBot> m_temp = new ArrayList<>();
		m_temp.addAll(EnemyList);
		for (EnemyBot k : EnemyList) {
			if (k.getEnergy() == 0) {
				m_temp.remove(k);
				m_temp.add(k);
			}
		}
		return m_temp;
	}

	public ArrayList<EnemyBot> placeNormalBotFirst(ArrayList<EnemyBot> EnemyList) {
		ArrayList<EnemyBot> m_temp = new ArrayList<>();
		m_temp.addAll(EnemyList);
		for (EnemyBot k : EnemyList) {
			if (k.getType() == NORMAL_BOT && k.getEnergy() != 0) {
				if (m_temp.get(0).getType() == NORMAL_BOT && m_temp.get(0).getEnergy() < k.getEnergy()
						&& amountAlive > 1 && m_temp.get(0).getEnergy() != 0) {

					m_temp.remove(k);
					m_temp.add(1, k);
				} else {
					m_temp.remove(k);
					m_temp.add(0, k);
				}
			}
		}
		return m_temp;
	}

	public ArrayList<EnemyBot> placeDroidFirst(ArrayList<EnemyBot> EnemyList) {
		ArrayList<EnemyBot> m_temp = new ArrayList<>();
		m_temp.addAll(EnemyList);
		for (EnemyBot k : EnemyList) {
			if (k.getType() == DROID && k.getEnergy() != 0) {
				if (m_temp.get(0).getType() == DROID && m_temp.get(0).getEnergy() < k.getEnergy() && amountAlive > 1
						&& m_temp.get(0).getEnergy() != 0) {
					m_temp.remove(k);
					m_temp.add(1, k);
				} else {
					m_temp.remove(k);
					m_temp.add(0, k);
				}
			}
		}
		return m_temp;
	}

}
