import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Prompts {

    static final String YES = "Y";
    static final String NO = "N";
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

    public static String yesNo(String msg) {
        List<String> options = Arrays.asList(YES, NO);
        return Prompts.decisionString(msg, options);
    }

    public static String askForString(String msg) {
        System.out.println(msg);
        return SCANNER.nextLine();
    }

    public static Integer askForInteger(String msg, Integer min, Integer max) {
        System.out.println(msg);
        return decisionNumber(msg, min, max);
    }

    public static String askForFilePath() {
        String startFilePathQ = "Enter the filepath for where your folders should be created:";
        Boolean chosen = false;
        String userInput = "";

        System.out.println(startFilePathQ);
        while (!chosen) {
            try {
                userInput = SCANNER.nextLine();
                if (!(new File(userInput).exists())) {
                    throw new InvalidOptionException();
                }
                else chosen = true;
            }
            catch (InvalidOptionException e) {
                System.out.println("Invalid file path. Please try again:");
            }
        }
        return userInput;
    }
}