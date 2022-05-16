package src;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import src.Exceptions.InvalidOptionException;

public class Asker {


    private static final Integer MINFOLDERS = 1;
    private static final Scanner SCANNER = new Scanner(System.in);
    public static final String YEARQ = "What year? Eg. \'1st year\', \'2nd year\', etc.";
    public static final String COURSEQ = "What course? Eg. \'CSC207\', \'MAT102\', etc.";
    public static final String FILEPATHQ = "Enter the filepath for where your folders should be created:";
    public static final String NEWFOLDERQ = "What is this new folder\'s name?";
    public static final String NUMBERFOLDERQ = "Number these folders? (eg. Lab 1, Lab 2, etc.)";
    public static final String PICKFOLDERQ = "Which folder do you want to create your folders in?";
    public static final String FOLDERTOTALQ = "How many folders? The amount must be at least 1.\n"
                                              + "Alternatively, enter nothing (\"\") to go back to the main menu.";
    public static final String DONE = "Finished.";

    /*
    Credit to:
    https://www.delftstack.com/howto/java/
    java-clear-console/#:~:text=To%20clear%20the%20console%20in,
    command%20to%20clean%20the%20console.
    */
    public static final void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static final String decisionString(String msg, List<String> options) {
        Boolean chosen = false;
        String userInput = "";
        while (!chosen) {
            try {
                userInput = SCANNER.nextLine();
                if (!options.contains(userInput.toUpperCase())) {
                    throw new InvalidOptionException();
                }
                else chosen = true;
            }
            catch (InvalidOptionException e) {
                clearScreen();
                System.out.println(String.format("Invalid choice.\n%s", msg));
            }
        }
        return userInput;
    }

    private static final String decisionNotString(String msg, List<String> options) {
        Boolean chosen = false;
        String userInput = "";
        while (!chosen) {
            try {
                userInput = SCANNER.nextLine();
                if (options.contains(userInput.toUpperCase())) {
                    throw new InvalidOptionException();
                }
                else chosen = true;
            }
            catch (InvalidOptionException e) {
                clearScreen();
                System.out.println(String.format("Invalid choice.\n%s", msg));
            }
        }
        return userInput;
    }

    /**
     * Ask the user for an integer input and, if an invalid integer is inputted,
     * keep asking them for a valid one
     * @param msg the message displayed to the user to pick an option
     * @param min the minimum valid number the user can input
     * @param max the maximum valid number the user can input
     * @return the user's valid integer input
     */
    private static final Integer decisionNumber(String msg, Integer min, Integer max) {
    	Boolean chosen = false;
    	int chosenAmount = -1;
    	while (!chosen) {
    		try {
                String userEntry = SCANNER.nextLine();
                if (userEntry == "") return 0;
    			chosenAmount = Integer.parseInt(userEntry);
    			if (chosenAmount > max || chosenAmount < min) {
    				throw new InvalidOptionException();
    			}
    			else chosen = true;
    		}		
    		catch (NumberFormatException| InvalidOptionException e) {
                clearScreen();
                System.out.println(String.format("Invalid choice.\n%s", msg));
            }	
    	}
        return chosenAmount;
    }

    private static final Integer askInteger(String msg, Integer min, Integer max) {
        System.out.println(msg);
        return decisionNumber(msg, min, max);
    }

    private static final String askYesNo(String msg) {
        List<String> options = Arrays.asList(Prompts.YES, Prompts.NO);
        String displayMsg = msg + "\n" + Prompts.yesNoPrompt();
        System.out.println(displayMsg);
        return Asker.decisionString(displayMsg, options);
    }

    /**
     * Ask the user for any string
     * @param msg the message displayed for the user to input a string
     * @return the user's string input
     */
    private static String askString(String msg) {
        clearScreen();
        System.out.println(msg);
        return SCANNER.nextLine();
    }

    public static String askNotString(String msg, List<String> notOptions) {
        clearScreen();
        System.out.println(msg);
        return decisionNotString(msg, notOptions);
    }

    public static String askFilePath(String msg) {
        Boolean chosen = false;
        String userInput = "";

        while (!chosen) {
            try {
                userInput = askString(msg);
                if (userInput == "") {
                    userInput = PathFinder.getDefault();
                    chosen = true;
                }
                else if (!FolderChecker.isValidPath(userInput)) throw new InvalidOptionException();
                else chosen = true;
            }
            catch (InvalidOptionException e) {
                clearScreen();
                System.out.println("Invalid file path. Please try again.");
            }
        }
        return userInput;
    }

    public static String askFilePath(String msg, String filePath) {
        Boolean chosen = false;
        String userInput = "";

        while (!chosen) {
            try {
                userInput = askString(msg);
                if (!FolderChecker.isValidPath(filePath + "\\" + userInput)) {
                    throw new InvalidOptionException();
                }
                else chosen = true;
            }
            catch (InvalidOptionException e) {
                clearScreen();
                System.out.println("Invalid file path. Please try again.");
            }
        }
        return filePath + "\\" + userInput;
    }

    private static final String askContinue(String msg) {
        return askYesNo(msg + "\n" + "Continue?");
    }

    public static final String askOption(String msg, List<String> options) {
        clearScreen();
        System.out.println(msg);
        return decisionString(msg, options);
    }

    /**
     * Ask the user if they want to create a specific number of week folders at a file path
     * @param filePath the path where the week folders will be created
     * @param weekTotal the number of week folders that will be created
     * @return a yes or no from the user
     */
    public static final String askWeekFolderConfirm(String filePath, Integer weekTotal) {
        String yesNoMsg = String.format("Creating %d folders at:\n%s", weekTotal, filePath);
        clearScreen();
        String userInput = Asker.askContinue(yesNoMsg);
        clearScreen();                                                              
        return userInput;
    }

    public static final String askFolderInConfirm(String newFolder, String folder) {
        String yesNoMsg = String.format("Creating \"%s\" in:\n%s", newFolder, folder);
        clearScreen();
        String userInput = Asker.askContinue(yesNoMsg);
        clearScreen();                                                              
        return userInput;
    }

    public static final String askFoldersConfirm(String folderNme, String filePath, Integer folderTotal) {
        String yesNoMsg = String.format("Creating %d \"%s\" folders at:\n%s", folderTotal, folderNme,
                                                                                      filePath);
        clearScreen();
        String userInput = Asker.askContinue(yesNoMsg);
        clearScreen();                                                              
        return userInput;
    }

    public static final Integer askFolderNum() {
        clearScreen();
        return Asker.askInteger(Asker.FOLDERTOTALQ, MINFOLDERS, Integer.MAX_VALUE);
    }

    public static final String askFilePathCont(String filePath) {
        clearScreen();
        return Asker.askYesNo(Prompts.continueFilePath(filePath)).toUpperCase();
    }

    public static final String askNumFoldQ() {
        clearScreen();
        String userInput = Asker.askYesNo(Asker.NUMBERFOLDERQ).toUpperCase();
        clearScreen();
        return userInput;
    }

    public static final String askDoneContinue() {
        return Asker.askContinue(DONE).toUpperCase();
    }

    public static final String weekFoldersExplain() {
        clearScreen();
        return Asker.askContinue(Prompts.weekFolderExplain()).toUpperCase();
    }

}