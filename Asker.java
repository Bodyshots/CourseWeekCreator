import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Asker {

    private static final Scanner SCANNER = new Scanner(System.in);
    static final String YEARQ = "What year? Eg. \'1st year\', \'2nd year\', etc.";
    static final String COURSEQ = "What course? Eg. \'CSC207\', \'MAT102\', etc.";
    static final String FILEPATHQ = "Enter the filepath for where your folders should be created:";
    static final String NEWFOLDERQ = "What is this new folder\'s name?";
    static final String NUMBERFOLDERQ = "Number these folders? (eg. Lab 1, Lab 2, etc.)";
    static final String PICKFOLDERQ = "Which folder do you want to create your folders in?";
    static final String FOLDERTOTALQ = "How many folders? The amount must be at least 1.";

    private static String decisionString(String msg, List<String> options) {
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
            catch (InvalidOptionException e) {System.out.println(msg);}
        }
        return userInput;
    }

    private static String decisionNotString(String msg, List<String> options) {
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
        String displayMsg = msg + "\n" + Prompts.yesNoPrompt();
        System.out.println(displayMsg);
        return Asker.decisionString(displayMsg, options);
    }

    public static String askString(String msg) {
        System.out.println(msg);
        return SCANNER.nextLine();
    }

    public static String askNotString(String msg, List<String> notOptions) {
        System.out.println(msg);
        return decisionNotString(msg, notOptions);
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
                if (userInput == "") {
                    userInput = PathFinder.getDefault();
                    chosen = true;
                }
                else if (!FolderChecker.isValidPath(userInput)) throw new InvalidOptionException();
                else chosen = true;
            }
            catch (InvalidOptionException e) {
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
                System.out.println("Invalid file path. Please try again.");
            }
        }
        return filePath + "\\" + userInput;
    }

    public static final String askContinue(String msg) {
        return askYesNo(msg + "\n" + "Continue?");
    }

    public static final String askOption(String msg, List<String> options) {
        System.out.println(msg);
        return decisionString(msg, options);
    }

    public static final String askWeekFolderConfirm(String filePath, Integer weekTotal) {
        String yesNoMsg = String.format("Creating %d folders at:\n%s.", weekTotal, filePath);
        return Asker.askContinue(yesNoMsg);
    }

    public static final String askFolderInConfirm(String newFolder, String folder) {
        String yesNoMsg = String.format("Creating \"%s\" in:\n%s.", newFolder, folder);
        return Asker.askContinue(yesNoMsg);
    }

    public static final String askFoldersConfirm(String folderNme, String filePath, Integer folderTotal) {
        String yesNoMsg = String.format("Creating %d \"%s\" folders at:\n%s", folderTotal, folderNme,
                                                                                 filePath);
        return Asker.askContinue(yesNoMsg);
    }

}