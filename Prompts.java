import java.util.List;

public class Prompts {
    static final String YES = "Y";
    static final String NO = "N";
    static final String OPTION_A = "A";
    static final String OPTION_B = "B";
    static final String OPTION_C = "C";
    static final String OPTION_D = "D";
    static final String OPTION_E = "E";

    public static final String yesNoPrompt() {
        return String.format("[%s] - Yes\n" +
                             "[%s] - No", YES, NO);
    }

    public static final String folderOptionsPrompt(String filePath) {
        return String.format("What do you want to do?\n" +
                             "Current location: %s\n" +
                             "[%s] - Mass create folders\n" +
                             "[%s] - Create \"Week\" folders\n" +
                             "[%s] - Reconfigure the main file path\n" +
                             "[%s] - Create a new folder\n" +
                             "[%s] - Exit", filePath, OPTION_A, OPTION_B, OPTION_C, 
                                            OPTION_D, OPTION_E);
        // alt version:
        // List<String> options = Arrays.asList(OPTION_A, OPTION_B, OPTION_C, OPTION_D, OPTION_E);
        // List<String> descripts = Arrays.asList("Mass create folders",
        //                                         "Create \"Week\" folders",
        //                                         "Reconfigure the main file path",
        //                                         "Create a new folder",
        //                                         "Exit");
        // String msg = String.format("What do you want to do?\nCurrent location: %s\n", filePath);
        // return msg + infOptions(options, descripts);
    }

    public static final String createFolderOptionsPrompt() { // should add ls option here
        return String.format("Where do you want to create a new folder?\n" +
                             "[%s] - In a series of folders\n" +
                             "[%s] - In a specific folder\n" +
                             "[%s] - Where I\'m at now\n" +
                             "[%s] - Back", OPTION_A, OPTION_B, OPTION_C, OPTION_D);
    }

    public static final String weekFolderExplain() {
        return String.format("Creating \"Week\" folders is specifically " + 
                             "designed for students.\n" + 
                             "The program will ask you for:\n-Your current year\n-A course code\n" +
                             "-An x number of \"Week\" folders you want created\n\n" +
                             "Thereafter, your specified year will be created as a folder " +
                             "at your set file path.\nInside this folder will be another folder " +
                             "with your specified course code.\nFinally, this course code folder will " +
                             "contain 1 to x empty \"Week\" folders (i.e. Week 1, Week 2,...).\n");
    }

    public static final String continueFilePath(String filePath) {
        return String.format("Your folders will be created at:\n%s\nKeep this file path?", filePath);
    }

    /**
     * Prereq: <options> and <descripts> have to be parallel arrays
     * 
     * @param options Valid buttons that the user can enter
     * @param descripts Descriptions of each button
     * @return A prompt listing a series of buttons with their respective descriptions
     */
    public static final String infOptions(List<String> options, List<String> descripts) {
        String outputStr = "";
        int optionsSize = options.size();
        for (int i = 0; i < optionsSize; i++) {
            outputStr += String.format("[%s] - %s\n", options.get(i), descripts.get(i));
            // if (i != optionsSize - 1) outputStr += "\n";
        }
        options.add(Character.toString(options.get(options.size() - 1).charAt(0) + 1));
        outputStr += String.format("[%s] - Back", options.get(options.size() - 1)); 
        return outputStr;
    }
}
