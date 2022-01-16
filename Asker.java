import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Asker {

    private static final Scanner SCANNER = new Scanner(System.in);

    private static <E> String decisionString(String msg, List<E> options) {
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

    public static final String askYesNo(String msg) {
        List<String> options = Arrays.asList(Prompts.YES, Prompts.NO);
        return Asker.decisionString(msg, options);
    }

    public static String askString(String msg) {
        System.out.println(msg);
        return SCANNER.nextLine();
    }

    public static Integer askInteger(String msg, Integer min, Integer max) {
        System.out.println(msg);
        return decisionNumber(msg, min, max);
    }

    public static String askFilePath(String msg) {
        Boolean chosen = false;
        String userInput = "";

        while (!chosen) {
            try {
                userInput = askString(msg);
                if (!FolderChecker.isValidPath(userInput)) throw new InvalidOptionException();
                else chosen = true;
            }
            catch (InvalidOptionException e) {
                System.out.println("Invalid file path. Please try again.");
            }
        }
        return userInput;
    }

    public static final String askContinue(String msg) {
        String continueMsg = msg + "\n" + Prompts.continuePrompt();
        System.out.println(continueMsg);
        return askYesNo(continueMsg);
    }

    public static final <E> String askOption(String msg, List<E> options) {
        System.out.println(msg);
        return decisionString(msg, options);
    }

    public static final String askWeekFolderConfirm(String filePath, Integer weekTotal) {
        String yesNoMsg = String.format("Creating %d folders at %s.", weekTotal, filePath);
        return Asker.askContinue(yesNoMsg);
    }

    public static final String askFolderConfirm(String newFolder, String folder) {
        String yesNoMsg = String.format("Creating \"%s\" in %s.", newFolder, folder);
        return Asker.askContinue(yesNoMsg);
    }
}