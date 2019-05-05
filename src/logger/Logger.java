package logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

public class Logger {
	private PrintStream out;
	private boolean isTextFileOutput;
	
	public Logger(boolean isTextFileOutput) {
		this.isTextFileOutput = isTextFileOutput;
		
		if (isTextFileOutput) {
			try {
				out = new PrintStream(new FileOutputStream("risk_output.txt", false));
				return;
			} catch (FileNotFoundException e) {
				System.err.println("Problem writing to file, writing to console instead.");
			}
		}
		
		out = System.out;
	}
	
	public void simHeader(int simNumber) {
		out.println("-----");
		out.println("Simulation " + simNumber);
		out.println("-----");
	}
	
	public void reportArmies(int attackArmy, int defenseArmy) {
		out.printf("Attack has %d %s.%n", attackArmy, grammar(attackArmy, "army", "armies"));
		out.printf("Defense has %d %s.%n", defenseArmy, grammar(defenseArmy, "army", "armies"));
	}
	
	public void roundHeader(int roundNumber) {
		out.println("-");
		out.println("Round " + roundNumber);
		out.println("-");
	}

	public void reportRolls(int[] attackDice, int[] defenseDice) {
		out.println("Attack rolled: " + Arrays.toString(attackDice));
		out.println("Defense rolled: " + Arrays.toString(defenseDice));
	}
	
	public void reportLosses(int atkCasualties, int defCasualties) {
		out.printf("Attack lost %d %s.%n", atkCasualties, grammar(atkCasualties, "troop", "troops"));
		out.printf("Defense lost %d %s.%n", defCasualties, grammar(defCasualties, "troop", " troops"));
	}
	
	public void reportBattleOutcome(String winner) {
		out.println(winner + " won.\n");
	}
	
	public void reportSimulationOutcome(int attackWins, double averageAttackRemaining, int defenseWins, double averageDefenseRemaining) {
		out.printf("%nAttack won %d %s with an average of %.1f %s left.%n", 
				attackWins, grammar(attackWins, "time", "times"), averageAttackRemaining, grammar(averageAttackRemaining, "troop", "troops"));
		
		out.printf("Defense won %d %s with an average of %.1f %s left.%n", 
				defenseWins, grammar(defenseWins, "time", "times"), averageDefenseRemaining, grammar(averageDefenseRemaining, "troop", "troops"));
	}
	
	public String grammar(int number, String singular, String plural) {
		return number == 1 ? singular : plural;
	}
	
	public String grammar(double number, String singular, String plural) {
		return number == 1.0 ? singular : plural;
	}
	
	public void close() {
		if (isTextFileOutput)
			out.close();
	}
}
