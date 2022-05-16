package src.FolderBehaviours;
import java.util.Arrays;

import src.FolderCreator;
import src.Prompts;
import src.Asker;
import src.Main;

public class CreateWeeks implements FolderBehaviour{

    private static final String FOLDERNME = "Week";

    private String getWeekPath(String filePath) {
        return String.format("%s\\", filePath) + this.getYear() +
               String.format("\\%s", this.getCourse());
    }

    private String getYear() {
        return Asker.askNotString(Asker.YEARQ, Arrays.asList(""));
    }
    
    private String getCourse() {
        return Asker.askNotString(Asker.COURSEQ, Arrays.asList(""));
    }

    @Override
    public void doCreate(String filePath) {
        String userInput = Asker.weekFoldersExplain();
        if (userInput.equals(Prompts.NO)) return; // Don't create "week" folders

        int folderTotal = Asker.askFolderNum();
        if (folderTotal == 0) return;

        String weekPath = this.getWeekPath(filePath);

        userInput = Asker.askFoldersConfirm(FOLDERNME, weekPath, folderTotal).toUpperCase();;
        if (userInput.equals(Prompts.NO)) return;

        for (int i = 1; i < folderTotal + 1; i++) {
            FolderCreator.createFolder(String.format("%s %d", FOLDERNME, i), weekPath);
        }

        userInput = Asker.askDoneContinue();
        if (userInput.equals(Prompts.NO)) Main.exitProgram();
        return;
    }

}
