package src.FolderBehaviours;
import java.util.Arrays;
import java.util.List;

import src.Prompts;
import src.Asker;
import src.Main;
import src.FolderBehaviours.FoldInFoldOpts.OptionA;
import src.FolderBehaviours.FoldInFoldOpts.OptionB;
import src.FolderBehaviours.FoldInFoldOpts.OptionC;
import src.FolderBehaviours.FoldInFoldOpts.OptionHandler;
import src.FolderBehaviours.FoldInFoldOpts.OptionOther;

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
            boolean folderMade = false;

            String userInput = Asker.askOption(Prompts.createFolderOptionsPrompt(), options).toUpperCase();
            String folderNme = Asker.askNotString(Asker.NEWFOLDERQ, Arrays.asList(""));

            if (userInput.equals(Prompts.OPTION_A)) optHandler.setOptBeh(new OptionA());
            else if (userInput.equals(Prompts.OPTION_B)) optHandler.setOptBeh(new OptionB()); // "In a specific folder"
            else if (userInput.equals(Prompts.OPTION_C)) optHandler.setOptBeh(new OptionC());
            else optHandler.setOptBeh(new OptionOther());

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
