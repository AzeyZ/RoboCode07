package group07;

import java.util.ArrayList;

public class TargetPrioritizer {
	
	
	private int amountEnemyBot = 0;
	private int leader;
	private int droid;
	private int normRobot;

	public TargetPrioritizer()
	{
		
	}
	public ArrayList<EnemyBot> sort(ArrayList<EnemyBot> enemyBot)
	{
		leader = 0;
		droid = 0;
		normRobot = 0;
		if(amountEnemyBot < enemyBot.size())
		{
			amountEnemyBot = enemyBot.size();
		}
		for(EnemyBot k: enemyBot)
		{
			switch(k.getType())
			{
			case 0:
			{
				leader++;
				break;
			}
			case 1:
			{
				droid++;
				break;
			}
			case 2:
			{
				normRobot++;
				break;
			}
			default:
			{
				break;
			}
			}
		}
		switch (amountEnemyBot)
		{
		case 1:
		{
			break;
		}
		case 2:
		{
			
		}
		}
		
		return enemyBot;
	}
	
}
