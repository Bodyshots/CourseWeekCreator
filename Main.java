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
        folderCreator.setFilePath();
        List<String> options = Arrays.asList(Prompts.OPTION_A,
                                             Prompts.OPTION_B,
                                             Prompts.OPTION_C,
                                             Prompts.OPTION_D,
                                             Prompts.OPTION_E);
        Boolean exitProgram = false;
        while (!exitProgram) {
            String userInput = Asker.askOption(Prompts.folderOptionsPrompt(folderCreator.getFilePath()), options).toUpperCase();
            if (userInput.equals(Prompts.OPTION_A)) folderCreator.setFBehaviour(new CreateMass());
            else if (userInput.equals(Prompts.OPTION_B)) folderCreator.setFBehaviour(new CreateWeeks());
            else if (userInput.equals(Prompts.OPTION_C)) {
                folderCreator.setFBehaviour(new CreateNull());
                folderCreator.setFilePath();
            }
            else if (userInput.equals(Prompts.OPTION_D)) folderCreator.setFBehaviour(new CreateFolderInFolder());
            else exitProgram = true;
            if (!exitProgram) folderCreator.create();
        }
        Main.exitProgram();
    }

    public static void main(String[] args) {
        Main.run();
    }
}
