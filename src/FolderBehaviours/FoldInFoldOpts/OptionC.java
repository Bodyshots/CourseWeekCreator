package src.FolderBehaviours.FoldInFoldOpts;

import src.Asker;
import src.FolderCreator;
import src.Prompts;

public class OptionC extends AbsOpt {

    @Override
    public boolean doOption(String folderNme, String filePath) {
        String userInput = Asker.askFolderInConfirm(folderNme, filePath).toUpperCase();
        if (userInput.equals(Prompts.YES)) {
            FolderCreator.createFolder(folderNme, filePath);
            return true;
        }
        return false;
    }
    
}
