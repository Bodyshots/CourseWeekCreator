import java.util.Arrays;

public class CreateMass implements FolderBehaviour {

    @Override
    public void doCreate(String filePath) {
        int folderTotal = Asker.askFolderNum();
        String folderNme = Asker.askNotString(Asker.NEWFOLDERQ, Arrays.asList(""));
        
        String userInput = Asker.askFoldersConfirm(folderNme, filePath, folderTotal).toUpperCase();
        if (userInput.equals(Prompts.NO)) return;

        for (int i = 1; i < folderTotal + 1; i++) {
            FolderCreator.createFolder(String.format("%s %d", folderNme, i), filePath);
        }

        userInput = Asker.askContinue(DONE).toUpperCase();
        if (userInput.equals(Prompts.NO)) Main.exitProgram();
        
    }
    
}
