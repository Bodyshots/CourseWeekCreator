import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Asker {

    private static final Scanner SCANNER = new Scanner(System.in); // Reads the user's inputs

    /**
     * Ask the user for a string input and, if an invalid string is inputted, 
     * keep asking them for a valid string
     * @param msg the message displayed to the user to pick an option
     * @param options the valid options the user can choose from
     * @return the user's valid string input
     */
    private static String decisionString(String msg, List<String> options) {
        Boolean chosen = false;
        String userInput = "";
        while (!chosen) {
            try {
                userInput = SCANNER.nextLine().toUpperCase();
                if (!options.contains(userInput)) {
                    throw new InvalidOptionException();
                }
                else chosen = true;
            }
            catch (InvalidOptionException e) {System.out.println(msg);}
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
    private static Integer decisionNumber(String msg, Integer min, Integer max) {
    	Boolean chosen = false;
    	int chosenAmount = -1;
    	while (!chosen) {
    		try {
    			chosenAmount = Integer.parseInt(SCANNER.nextLine());
    			if (chosenAmount > max || chosenAmount < min) {
    				throw new InvalidOptionException();
    			}
    			else chosen = true;
    		}		
    		catch(NumberFormatException| InvalidOptionException e) {System.out.println(msg);}	
    		}
        return chosenAmount;
    }

    /**
     * Ask the user a yes or no question and keep asking them for one of those options
     * @param msg the message displayed to the user to pick yes or no
     * @return a chosen yes or no from the user
     */
    private static final String yesNo(String msg) {
        List<String> options = Arrays.asList(Prompts.YES, Prompts.NO);
        return Asker.decisionString(msg, options);
    }

    /**
     * Ask the user for any string
     * @param msg the message displayed for the user to input a string
     * @return the user's string input
     */
    public static String askString(String msg) {
        System.out.println(msg);
        return SCANNER.nextLine();
    }

    /**
     * Ask the user for a valid integer
     * @param msg the message displayed for the user to input an integer
     * @param min the minimum valid integer the user can input
     * @param max the maximum valid integer the user can input
     * @return the user's inputted valid integer
     */
    public static Integer askValidInteger(String msg, Integer min, Integer max) {
        System.out.println(msg);
        return decisionNumber(msg, min, max);
    }

    /**
     * Ask the user for a valid file path and, if an invalid path is given, keep
     * asking the user for a valid one
     * @return the user's inputted valid file path
     */
    public static String askFilePath() {
        String startFilePathQ = "Enter the filepath for where your folders should be created:";
        Boolean chosen = false;
        String userInput = "";

        while (!chosen) {
            try {
                userInput = askString(startFilePathQ);
                if (!(new File(userInput).exists())) {
                    throw new InvalidOptionException();
                }
                else chosen = true;
            }
            catch (InvalidOptionException e) {
                System.out.println("Invalid file path. Please try again.");
            }
        }
        return userInput;
    }

    /**
     * Ask the user if they wish to continue or not
     * @param msg the message displayed that asks the user if they want to continue
     * @return a chosen yes or no from the user
     */
    public static final String askContinue(String msg) {
        String continueMsg = msg + "\n" + Prompts.continuePrompt();
        System.out.println(continueMsg);
        return yesNo(continueMsg);
    }

    /**
     * Ask the user to pick an option
     * @param msg the message displayed that asks the user to pick an option
     * @param options the possible options the user can choose from
     * @return a valid option the user has picked
     */
    public static final String askOption(String msg, List<String> options) {
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
        String yesNoMsg = String.format("Creating %d Week folders in %s.", weekTotal, filePath);
        return Asker.askContinue(yesNoMsg);
    }

    /**
     * Ask the user if they want to create a specific folder in a particular folder
     * @param newFolder the folder to be created
     * @param folder the folder where the new folder will be created
     * @return a yes or no from the user
     */
    public static final String askFolderConfirm(String newFolder, String folder) {
        String yesNoMsg = String.format("Creating \"%s\" in %s.", newFolder, folder);
        return Asker.askContinue(yesNoMsg);
    }
}