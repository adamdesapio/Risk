package logic;
import java.util.Arrays;
import java.util.Scanner;

public class BattleSimulator {
	private int numberOfSimulations;
	private int attackArmyCount;
	private int defenseArmyCount;
	private int resetAArmy;
	private int resetDArmy;
	
	public void run() {
		getinfo();
		
		int aWin = 0;
		int dWin = 0;
		int atkLeftovers = 0;
		int defLeftovers = 0;
		
		for (int i = 0; i < numberOfSimulations; i++) {
			System.out.println("");
			System.out.println("-----");
			System.out.println("Simulation " + (i + 1));
			System.out.println("-----");
			reportArmies();
			
			doBattle();
			
			if (attackArmyCount > 1) {
				aWin++;
				atkLeftovers += attackArmyCount;
				System.out.println("Attack won");
			} else {
				dWin++;
				atkLeftovers++;
				defLeftovers += defenseArmyCount;
				System.out.println("Defense won");
			}
			
			attackArmyCount = resetAArmy;
			defenseArmyCount = resetDArmy;
		}
		
		double avgLeftA = atkLeftovers / numberOfSimulations;
		double avgLeftD = defLeftovers / numberOfSimulations;
		
		System.out.println("");
		
		System.out.printf("Attack won %d %s with an average of %.1f %s left.%n", 
				aWin, grammar(aWin, "time", "times"), avgLeftA, grammar(avgLeftA, "troop", "troops"));
		
		System.out.printf("Defense won %d %s with an average of %.1f %s left.%n", 
				dWin, grammar(dWin, "time", "times"), avgLeftD, grammar(avgLeftD, "troop", "troops"));
	}
	
	public void getinfo() {
		try (Scanner reader = new Scanner(System.in)) {
			System.out.println("How many simulations should be run?");
			numberOfSimulations = Integer.parseInt(reader.next());
			
			System.out.println("How many attacking armies?");
			attackArmyCount = Integer.parseInt(reader.next());
			
			System.out.println("How many defending armies?");
			defenseArmyCount = Integer.parseInt(reader.next());
			
			if (numberOfSimulations < 1 || attackArmyCount < 2 || defenseArmyCount < 1) {
				System.out.println("Number of simulations must be greater than 0.");
				System.out.println("Number of attacking armies must be greater than 1.");
				System.out.println("Number of defending armies must be greater than 0.");
				System.exit(0);
			}
		} catch (NumberFormatException e) {
			System.err.println("Oh, fuck");
			System.exit(1);
		}
		
		resetAArmy = attackArmyCount;
		resetDArmy = defenseArmyCount;
	}
	
	public void doBattle() {
		int round = 0;
		do {
			System.out.println("-");
			System.out.println("Round " + (round++ + 1));
			System.out.println("-");
			
			int[] attackDice = getAtkDice();
			int[] defenseDice = getDefDice();
			rollDice(attackDice);
			rollDice(defenseDice);
			Arrays.sort(attackDice);
			Arrays.sort(defenseDice);
			
			System.out.println("Attack rolled: " + Arrays.toString(attackDice));
			System.out.println("Defense rolled: " + Arrays.toString(defenseDice));
			
			compareDice(attackDice, defenseDice);
		} while (attackArmyCount > 1 && defenseArmyCount > 0);
	}
	
	public int[] getAtkDice() {
		int numberOfDice;
		if (attackArmyCount > 3)
			numberOfDice = 3;
		else if (attackArmyCount == 2)
			numberOfDice = 1;
		else
			numberOfDice = attackArmyCount - 1;
		
		return new int[numberOfDice];
	}
	
	public int[] getDefDice() {
		int numberOfDice;
		if (defenseArmyCount >= 2)
			numberOfDice = 2;
		else
			numberOfDice = defenseArmyCount;
		
		return new int[numberOfDice];
	}
	
	public void rollDice(int[] dice) {
		for (int i = 0; i < dice.length; i++) {
			dice[i] = (int)(Math.random() * 6 + 1);
		}
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
		attackArmyCount -= atkCasualties;
		defenseArmyCount -= defCasualties;
		
		reportArmies();
	}
	
	public void reportArmies() {
		System.out.printf("Attack has %d %s.%n", attackArmyCount, grammar(attackArmyCount, "army", "armies"));
		System.out.printf("Defense has %d %s.%n", defenseArmyCount, grammar(defenseArmyCount, "army", "armies"));
	}
	
	public String grammar(int number, String singular, String plural) {
		return number == 1 ? singular : plural;
	}
	
	public String grammar(double number, String singular, String plural) {
		return number == 1.0 ? singular : plural;
	}
}
