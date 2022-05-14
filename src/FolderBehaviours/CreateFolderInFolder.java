package src.FolderBehaviours;
import java.util.Arrays;
import java.util.List;

import src.Prompts;
import src.Asker;
import src.Main;
import src.FolderBehaviours.FoldInFoldOpts.OptionFactory;
import src.FolderBehaviours.FoldInFoldOpts.OptionHandler;

public class CreateFolderInFolder implements FolderBehaviour {

    @Override
    public void doCreate(String filePath) {
        List<String> options = Arrays.asList(Prompts.OPTION_A,
                                             Prompts.OPTION_B,
                                             Prompts.OPTION_C,
                                             Prompts.OPTION_D);
        Boolean back = false;

        while (!back) {
            OptionHandler optHandler = new OptionHandler();
            OptionFactory optFact = new OptionFactory();
            boolean folderMade = false;

            String userInput = Asker.askOption(Prompts.createFolderOptionsPrompt(), options).toUpperCase();
            String folderNme = Asker.askNotString(Asker.NEWFOLDERQ, Arrays.asList(""));

            optHandler.setOptBeh(optFact.createBehaviour(userInput));
            folderMade = optHandler.handle(folderNme, filePath);

            if (folderMade) {
                userInput = Asker.askContinue(DONE).toUpperCase();
                if (userInput.equals(Prompts.NO)) Main.exitProgram();
            }
            back = true; // back button
        }
        return; // enters back (not "continue" back)
    }
}
