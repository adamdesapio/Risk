package application;

import java.util.Scanner;
import logic.BattleSimulator;

public class Main {
	public static void main(String[] args) {
		
		int attackArmy = 0;
		int defenseArmy = 0;
		int simulationCount = 0;
		
		try (Scanner reader = new Scanner(System.in)) {
			do {
				System.out.println("How many simulations should be run?");
				simulationCount = Integer.parseInt(reader.next());
				if (simulationCount < 1) { 
					System.out.println("Number of simulations must be greater than 0.");
					continue;
				}
				
				System.out.println("How many attacking armies?");
				attackArmy = Integer.parseInt(reader.next());
				if (attackArmy < 2) {
					System.out.println("Number of attacking armies must be greater than 1.");
					continue;
				}
				
				System.out.println("How many defending armies?");
				defenseArmy = Integer.parseInt(reader.next());
				if (defenseArmy < 1) { 
					System.out.println("Number of defending armies must be greater than 0.");
					continue;
				}
			} while (simulationCount < 1 || attackArmy < 2 || defenseArmy < 1);
		} catch (NumberFormatException e) {
			System.err.println("Incorrect input type");
			return;
		}
		
		BattleSimulator sim = new BattleSimulator(simulationCount, attackArmy, defenseArmy);
		sim.simulate();
	}
}
