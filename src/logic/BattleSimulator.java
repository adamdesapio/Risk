package logic;
import java.util.Arrays;
import logger.Logger;

public class BattleSimulator {
	private int simulationCount;
	private int attackArmy;
	private int defenseArmy;
	private int resetAttack;
	private int resetDefense;
	private Logger log;
	
	public BattleSimulator(int simulationCount, int attackArmy, int defenseArmy, boolean isTextFileOutput) {
		this.simulationCount = simulationCount;
		this.attackArmy = attackArmy;
		this.defenseArmy = defenseArmy;
		this.log = new Logger(isTextFileOutput);
		
		resetAttack = attackArmy;
		resetDefense = defenseArmy;
	}
	
	public void simulate() {
		int attackWins = 0;
		int defenseWins = 0;
		int attackRemaining = 0;
		int defenseRemaining = 0;
		
		//Loop for number of sims
		for (int i = 1; i <= simulationCount; i++) {
			log.simHeader(i);
			
			log.reportArmies(attackArmy, defenseArmy);
			
			//Loop for number of rounds
			int round = 1;
			do {
				log.roundHeader(round);
				round++;
				
				int[] attackDice = getDice(true, attackArmy);
				int[] defenseDice = getDice(false, defenseArmy);
				
				log.reportRolls(attackDice, defenseDice);
				
				int atkCasualties = 0;
				int defCasualties = 0;
				
				//Loop for comparing dice
				int reps = Math.min(attackDice.length, defenseDice.length);
				for (int j = 0; j < reps; j++) {
					int atkHigh = attackDice[attackDice.length - (1 + j)];
					int defHigh = defenseDice[defenseDice.length - (1 + j)];
					int difference = atkHigh - defHigh;
					if (difference > 0)
						defCasualties++;
					else
						atkCasualties++;
				}
				
				log.reportLosses(atkCasualties, defCasualties);
				
				attackArmy -= atkCasualties;
				defenseArmy -= defCasualties;
				
				log.reportArmies(attackArmy, defenseArmy);
				
			} while (attackArmy > 1 && defenseArmy > 0);
			
			String winner;
			if (attackArmy > 1) {
				attackWins++;
				attackRemaining += attackArmy;
				winner = "Attack";
			} else {
				defenseWins++;
				attackRemaining++;
				defenseRemaining += defenseArmy;
				winner = "Defense";
			}
			
			log.reportBattleOutcome(winner);
			
			attackArmy = resetAttack;
			defenseArmy = resetDefense;
		}
		
		double averageAttackRemaining = attackRemaining / simulationCount;
		double averageDefenseRemaining = defenseRemaining / simulationCount;
		
		log.reportSimulationOutcome(attackWins, averageAttackRemaining, defenseWins, averageDefenseRemaining);
		log.close();
		
		System.out.printf("Attack won %d %s with an average of %.1f %s left.%n", 
				attackWins, log.grammar(attackWins, "time", "times"), averageAttackRemaining, log.grammar(averageAttackRemaining, "troop", "troops"));
		
		System.out.printf("Defense won %d %s with an average of %.1f %s left.%n", 
				defenseWins, log.grammar(defenseWins, "time", "times"), averageDefenseRemaining, log.grammar(averageDefenseRemaining, "troop", "troops"));
	}
	
	public int[] getDice(boolean isAttack, int army) {
		int[] dice;
		int attackOffset = 0;
		int maxDice = 2;
		
		if (isAttack) {
			maxDice = 3;
			attackOffset = 1;
		}
		
		if (army > maxDice)
			dice = new int[maxDice];
		else
			dice = new int[army - attackOffset];
		
		for (int i = 0; i < dice.length; i++)
			dice[i] = (int)(Math.random() * 6 + 1);
		
		Arrays.sort(dice);
		
		return dice;
	}
}