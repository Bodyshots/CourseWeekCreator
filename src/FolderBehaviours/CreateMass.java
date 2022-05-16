package src.FolderBehaviours;
import java.util.Arrays;

import src.FolderCreator;
import src.Asker;
import src.Prompts;
import src.Main;

public class CreateMass implements FolderBehaviour {

    @Override
    public void doCreate(String filePath) {
        int folderTotal = Asker.askFolderNum();
        if (folderTotal == 0) return;

        String folderNme = Asker.askNotString(Asker.NEWFOLDERQ, Arrays.asList(""));
        
        String userInput = Asker.askFoldersConfirm(folderNme, filePath, folderTotal).toUpperCase();
        if (userInput.equals(Prompts.NO)) return;

        for (int i = 1; i < folderTotal + 1; i++) {
            FolderCreator.createFolder(String.format("%s %d", folderNme, i), filePath);
        }

        userInput = Asker.askDoneContinue();
        if (userInput.equals(Prompts.NO)) Main.exitProgram();
        
    }
    
}
