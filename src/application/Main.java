package application;

import java.util.Scanner;
import logic.BattleSimulator;
import logger.Logger;

public class Main {
	public static void main(String[] args) {

		//Defaults
		int simulationCount = 1;
		int attackArmy = 2;
		int defenseArmy = 1;
		boolean isTextFileOutput = false;
		
		//-help option
		if (args.length > 0) {
			if (args[0].equals("-help")) {
				//Change usage to [-options] [num1] ... [numN]
				System.out.printf("Usage: java -jar risk [-options] [num 1] ... [num n]\n"
								+ "Where options include '-' followed by any of the following:\n"
								+ "\tn\t\t the number that follows is the amount of simulations to be run\n"
								+ "\ta\t\t the number that follows is the amount of attacking armies\n"
								+ "\td\t\t the number that follows is the amount of defending armies\n"
								+ "\to\t\t output battle log to a .txt file"
								+ "\n"
								+ "Number includes:\n"
								+ "\t#\t\t an integer indicating:\n"
								+ "\t\t\t\t greater than 0 for number of simulations\n"
								+ "\t\t\t\t greater than 1 for attack\n"
								+ "\t\t\t\t greater than 0 for defense\n"
								+ "Default Behavior:\n"
								+ "\tOmmission of any options uses the following defaults:\n"
								+ "\t\t 1 simulation\n"
								+ "\t\t 2 armies for attack\n"
								+ "\t\t 1 army for defense\n"
								+ "\t\t output battle log to console window");
				return;
			}
			
			//Input from command line
			try {
				String options = args[0];
				
				int numberPosition = 1;
				for (int i = 0; i < options.length(); i++) {
					char c = options.charAt(i);
					if (c == '-') {
						continue;
					} 
					
					if (c == 'n') {
						simulationCount = Integer.parseInt(args[numberPosition]);
						numberPosition++;
						continue;
					} 
					
					if (c == 'a') {
						attackArmy = Integer.parseInt(args[numberPosition]);
						numberPosition++;
						continue;
					} 
					
					if (c == 'd') {
						defenseArmy = Integer.parseInt(args[numberPosition]);
						numberPosition++;
						continue;
					} 
					
					if (c == 'o') {
						isTextFileOutput = true;
						continue;
					} 
					
					System.err.println("Unrecognized option");
					return;
				}
				
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println("Not enough numbers for options specified, using defaults.");
				
			} catch (NumberFormatException e) {
				System.err.println("After options, only supply numbers for options n, a, d.");
				return;
				
			} catch (Exception e) {
				System.err.println(e);
				return;
			}
			
		} 
		
		//Text UI
		else {
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
		}
		
		BattleSimulator sim = new BattleSimulator(simulationCount, attackArmy, defenseArmy, isTextFileOutput);
		sim.simulate();
	}
}
