package src;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final String EXIT = "Exiting...";
    
    public static void exitProgram() {
        System.out.println(EXIT);
        System.exit(0);
    }

    public static void run() {
        FolderCreator folderCreator = new FolderCreator();
        MainOptionFactory optFact = new MainOptionFactory();
        List<String> options = Arrays.asList(Prompts.OPTION_A,
                                             Prompts.OPTION_B,
                                             Prompts.OPTION_C,
                                             Prompts.OPTION_D,
                                             Prompts.OPTION_E);
        Boolean exitProgram = false;

        folderCreator.setFilePath();
        while (!exitProgram) {
            String userInput = Asker.askOption(Prompts.folderOptionsPrompt(folderCreator.getFilePath()), options).toUpperCase();
            folderCreator.setFBehaviour(optFact.createBehaviour(userInput));

            if (userInput.equals(Prompts.OPTION_C)) folderCreator.setFilePath();
            if (userInput.equals(Prompts.OPTION_E)) exitProgram = true;

            if (!exitProgram) folderCreator.create();
        }
        Main.exitProgram();
    }

    public static void main(String[] args) {
        Main.run();
    }
}
