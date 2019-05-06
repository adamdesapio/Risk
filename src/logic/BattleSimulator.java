package logic;
import java.util.Arrays;
import java.util.Random;

import logger.Logger;

public class BattleSimulator {
	private int simulationCount;
	private int attackArmy;
	private int defenseArmy;
	private Logger log;
	
	public BattleSimulator(int simulationCount, int attackArmy, int defenseArmy, boolean isTextFileOutput) {
		this.simulationCount = simulationCount;
		this.attackArmy = attackArmy;
		this.defenseArmy = defenseArmy;
		this.log = new Logger(isTextFileOutput);
	}
	
	public void simulate() {
		int attackWins = 0;
		int defenseWins = 0;
		double attackRemaining = 0;
		double defenseRemaining = 0;
		
		//Loop for number of sims
		for (int i = 1; i <= simulationCount; i++) {
			log.simHeader(i);
			
			int currentAttack = attackArmy;
			int currentDefense = defenseArmy;
			
			//Loop for number of rounds
			int round = 1;
			do {
				log.roundHeader(round++);
				log.reportArmies(currentAttack, currentDefense);
				
				int[] attackDice = getDice(true, currentAttack);
				int[] defenseDice = getDice(false, currentDefense);
				
				log.reportRolls(attackDice, defenseDice);
				
				int attackCasualties = 0;
				int defenseCasualties = 0;
				
				//Loop for comparing dice
				int diceToCompare = Math.min(attackDice.length, defenseDice.length);
				for (int j = 0; j < diceToCompare; j++) {
					int attackHighRoll = attackDice[attackDice.length - (1 + j)];
					int defenseHighRoll = defenseDice[defenseDice.length - (1 + j)];
					if (attackHighRoll - defenseHighRoll > 0)
						defenseCasualties++;
					else
						attackCasualties++;
				}
				
				log.reportLosses(attackCasualties, defenseCasualties);
				
				currentAttack -= attackCasualties;
				currentDefense -= defenseCasualties;
				
			} while (currentAttack > 1 && currentDefense > 0);
			
			String winner;
			if (currentAttack > 1) {
				attackWins++;
				attackRemaining += currentAttack;
				winner = "Attack";
			} else {
				defenseWins++;
				attackRemaining++;
				defenseRemaining += currentDefense;
				winner = "Defense";
			}
			
			log.reportBattleOutcome(winner);
		}
		
		double averageAttackRemaining = attackRemaining / simulationCount;
		double averageDefenseRemaining = defenseRemaining / simulationCount;
		
		log.reportSimulationOutcomeConsole(attackWins, averageAttackRemaining, defenseWins, averageDefenseRemaining);
		
		if (log.isTextFileOutput()) {
			log.reportSimulationOutcomeFile(attackWins, averageAttackRemaining, defenseWins, averageDefenseRemaining);
			log.close();
		}
	}
	
	public int[] getDice(boolean isAttack, int army) {
		int[] dice;
		int attackOffset = 0;
		int maxDice = 2;
		Random rand = new Random();
		
		if (isAttack) {
			maxDice = 3;
			attackOffset = 1;
		}
		
		if (army > maxDice)
			dice = new int[maxDice];
		else
			dice = new int[army - attackOffset];
		
		for (int i = 0; i < dice.length; i++)
			dice[i] = rand.nextInt(6) + 1;
		
		Arrays.sort(dice);
		
		return dice;
	}
}