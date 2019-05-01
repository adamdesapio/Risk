package logic;
import java.util.Arrays;

public class BattleSimulator {
	private int simulationCount;
	private int attackArmy;
	private int defenseArmy;
	private int resetAttack;
	private int resetDefense;
	
	public BattleSimulator(int simulationCount, int attackArmy, int defenseArmy) {
		this.simulationCount = simulationCount;
		this.attackArmy = attackArmy;
		this.defenseArmy = defenseArmy;
		
		resetAttack = attackArmy;
		resetDefense = defenseArmy;
	}
	
	public void simulate() {
		int attackWins = 0;
		int defenseWins = 0;
		int attackRemaining = 0;
		int defenseRemaining = 0;
		
		for (int i = 1; i <= simulationCount; i++) {
			System.out.println("\n-----");
			System.out.println("Simulation " + i);
			System.out.println("-----");
			reportArmies();
			
			doBattle();
			
			if (attackArmy > 1) {
				attackWins++;
				attackRemaining += attackArmy;
				System.out.println("Attack won.");
			} else {
				defenseWins++;
				attackRemaining++;
				defenseRemaining += defenseArmy;
				System.out.println("Defense won.");
			}
			
			attackArmy = resetAttack;
			defenseArmy = resetDefense;
		}
		
		double averageAttackRemaining = attackRemaining / simulationCount;
		double averageDefenseRemaining = defenseRemaining / simulationCount;
		
		System.out.printf("%nAttack won %d %s with an average of %.1f %s left.%n", 
				attackWins, grammar(attackWins, "time", "times"), averageAttackRemaining, grammar(averageAttackRemaining, "troop", "troops"));
		
		System.out.printf("Defense won %d %s with an average of %.1f %s left.%n", 
				defenseWins, grammar(defenseWins, "time", "times"), averageDefenseRemaining, grammar(averageDefenseRemaining, "troop", "troops"));
	}
	
	public void doBattle() {
		int round = 1;
		do {
			System.out.println("-");
			System.out.println("Round " + round++);
			System.out.println("-");
			
			int[] attackDice = getDice(true, attackArmy);
			int[] defenseDice = getDice(false, defenseArmy);
			
			System.out.println("Attack rolled: " + Arrays.toString(attackDice));
			System.out.println("Defense rolled: " + Arrays.toString(defenseDice));
			
			compareDice(attackDice, defenseDice);
		} while (attackArmy > 1 && defenseArmy > 0);
	}
	
	public int[] getDice(boolean isAttack, int army) {
		int[] dice;
		int attackOffset = 0;
		int maxDice = 2;
		
		if (isAttack) {
			maxDice = 3;
			attackOffset = 1;
		}
		
		if (army > maxDice) {
			dice = new int[maxDice];
		} else
			dice = new int[army - attackOffset];
		
		for (int i = 0; i < dice.length; i++)
			dice[i] = (int)(Math.random() * 6 + 1);
		
		Arrays.sort(dice);
		
		return dice;
	}
	
	public void compareDice(int[] atkDice, int[] defDice) {
		int atkCasualties = 0;
		int defCasualties = 0;
		int reps = Math.min(atkDice.length, defDice.length);
		for (int i = 0; i < reps; i++) {
			int atkHigh = atkDice[atkDice.length - (1 + i)];
			int defHigh = defDice[defDice.length - (1 + i)];
			int difference = atkHigh - defHigh;
			if (difference > 0)
				defCasualties++;
			else
				atkCasualties++;
		}
		
		System.out.printf("Attack lost %d %s.%n", atkCasualties, grammar(atkCasualties, "troop", "troops"));
		System.out.printf("Defense lost %d %s.%n", defCasualties, grammar(defCasualties, "troop", " troops"));
		attackArmy -= atkCasualties;
		defenseArmy -= defCasualties;
		
		reportArmies();
	}
	
	public void reportArmies() {
		System.out.printf("Attack has %d %s.%n", attackArmy, grammar(attackArmy, "army", "armies"));
		System.out.printf("Defense has %d %s.%n", defenseArmy, grammar(defenseArmy, "army", "armies"));
	}
	
	public String grammar(int number, String singular, String plural) {
		return number == 1 ? singular : plural;
	}
	
	public String grammar(double number, String singular, String plural) {
		return number == 1.0 ? singular : plural;
	}
}
