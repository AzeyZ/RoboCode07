package group07;

import java.util.ArrayList;

// Kanske borde lägga till mer på hur mycket hp som EnemyBot har kvar.
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

	public ArrayList<EnemyBot> sortList(ArrayList<EnemyBot> EnemyList) {
		amountDroidAlive = amountDroidAlive(EnemyList);
		amountNormalBotAlive = amountNormalBotAlive(EnemyList);
		leaderAlive = leaderAlive(EnemyList);
		amountAlive = amountNormalBotAlive + amountDroidAlive;
		if(leaderAlive)
		{
			amountAlive ++;
			System.out.println("Ledaren lever");
		}
		System.out.println("Antal levande motståndare: " + amountAlive);
		System.out.println("Antal levande motståndar droids: " + amountDroidAlive);
		System.out.println("Antal levande motståndar NormalBots: " + amountNormalBotAlive);
		
		if (leaderAlive && amountDroidAlive > amountNormalBotAlive) {
			EnemyList = placeLeaderBotFirst(EnemyList);
		} else if (amountNormalBotAlive != 0) {
			EnemyList = placeNormalBotFirst(EnemyList);
		} else {
			EnemyList = placeDroidFirst(EnemyList);
		}

		EnemyList = placeDeadBotsLast(EnemyList);
		System.out.println("Lista över alla robotar i ordning");
		for(EnemyBot k : EnemyList)
		{
			System.out.println(k.getName());
		}
		System.out.println("-----------------------");
		return EnemyList;
	}

	private int amountDroidAlive(ArrayList<EnemyBot> EnemyList) {
		int m_amountDroids = 0;
		for (EnemyBot k : EnemyList) {
			if (k.getEnergy() != 0 && k.getType() == DROID) {
				m_amountDroids++;
			}
		}
		return m_amountDroids;
	}

	private int amountNormalBotAlive(ArrayList<EnemyBot> EnemyList) {
		int m_amountNormalBot = 0;
		for (EnemyBot k : EnemyList) {
			if (k.getEnergy() != 0 && k.getType() == NORMAL_BOT) {
				m_amountNormalBot++;
			}
		}
		return m_amountNormalBot;
	}

	private boolean leaderAlive(ArrayList<EnemyBot> EnemyList) {
		for (EnemyBot k : EnemyList) {
			if (k.getEnergy() != 0 && k.getType() == LEADER_BOT) {
				return true;
			}
		}
		return false;
	}

	private ArrayList<EnemyBot> placeLeaderBotFirst(ArrayList<EnemyBot> EnemyList) {
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
	private ArrayList<EnemyBot> placeDeadBotsLast(ArrayList<EnemyBot> EnemyList)
	{
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

	private ArrayList<EnemyBot> placeNormalBotFirst(ArrayList<EnemyBot> EnemyList) {
		ArrayList<EnemyBot> m_temp = new ArrayList<>();
		m_temp.addAll(EnemyList);
		for (EnemyBot k : EnemyList) {
			if (k.getType() == NORMAL_BOT) {
				if (m_temp.get(0).getType() == NORMAL_BOT && m_temp.get(0).getEnergy() > k.getEnergy() && amountAlive > 1) {
					
					m_temp.remove(k);
					m_temp.add(1, k);
				}
				else
				{
					m_temp.remove(k);
					m_temp.add(0, k);
				}
			}
		}
		return m_temp;
	}

	private ArrayList<EnemyBot> placeDroidFirst(ArrayList<EnemyBot> EnemyList) {
		ArrayList<EnemyBot> m_temp = new ArrayList<>();
		m_temp.addAll(EnemyList);
		for (EnemyBot k : EnemyList) {
			if (k.getType() == DROID) {
				m_temp.remove(k);
				m_temp.add(0, k);
			}
		}
		return m_temp;
	}

}
