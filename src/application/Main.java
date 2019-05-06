package application;

import java.util.Scanner;
import logic.BattleSimulator;

public class Main {
	public static void main(String[] args) {

		//Defaults
		int simulationCount = 1;
		int attackArmy = 2;
		int defenseArmy = 1;
		boolean isTextFileOutput = false;
		
		//process command line options
		if (args.length > 0) {
			//-help
			if (args[0].equals("-help")) {
				System.out.printf("Usage: java -jar riskv#.jar [-options] [num 1] ... [num n]\n"
								+ "Where options include '-' followed by any of the following:\n"
								+ "\tn\t\t the number that follows is the amount of simulations to be run\n"
								+ "\ta\t\t the number that follows is the amount of attacking armies\n"
								+ "\td\t\t the number that follows is the amount of defending armies\n"
								+ "\to\t\t output battle log to a risk_output.txt file"
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
					
					switch (c) {
						case '-': 
							break;
						case 'n':
							simulationCount = Integer.parseInt(args[numberPosition]);
							numberPosition++;
							break;
						case 'a':
							attackArmy = Integer.parseInt(args[numberPosition]);
							numberPosition++;
							break;
						case 'd':
							defenseArmy = Integer.parseInt(args[numberPosition]);
							numberPosition++;
							break;
						case 'o':
							isTextFileOutput = true;
							break;
						default:
							System.err.println("Unrecognized option");
							return;
					}
				}
				
				//If multiple args need numbers, and not provided such as -na 5
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println("Not enough numbers for options specified, using defaults.");
				
				//if options are followed by something other than numbers
			} catch (NumberFormatException e) {
				System.err.println("After options, only supply numbers for options n, a, d.");
				return;
			} 
		} 
		
		//Text UI - no command line options
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
					
					System.out.println("Do you want the log in a text file? (enter 'y' for yes, anything else for no)");
					String textFile = reader.next();
					if (textFile.length() == 1) {
						if (textFile.charAt(0) == 'y') {
							isTextFileOutput = true;
						}
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
