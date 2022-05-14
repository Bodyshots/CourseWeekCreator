package src.FolderBehaviours.FoldInFoldOpts;

import java.util.List;

import src.Asker;
import src.FolderCreator;
import src.FolderChecker;
import src.Prompts;

public abstract class AbsOpt {
    static final int OFFSET = 65;
    static final int MAXOPTION = 25;

    private final void createMultiFolders(String filePath, String folderNme, 
                                          List<String> folders, boolean number) {
        if (number) {
            for (int i = 0; i < folders.size(); i++) {
                String currentFolderNme = folderNme + String.format(" %d", i + 1);
                FolderCreator.createFolder(currentFolderNme, filePath + String.format("\\%s", folders.get(i)));
            }
        }
        else
        {
            for (int i = 0; i < folders.size(); i++) {
                FolderCreator.createFolder(folderNme, filePath + String.format("\\%s", folders.get(i)));
            }
        }
    }

    final int checkIfPg(int foldIndex, int foldSize, 
                                boolean nextPg, boolean backPg) {
        if (!backPg && (foldIndex / (MAXOPTION + 1)) > 0) return 2; // back pg
        else if ((foldSize - foldIndex - 1) > 0) return 1; // next pg
        return 0;
    }

    final boolean nextPgSelect(boolean nextPg, boolean backPg, int foldSize,
                                       int foldIndex, String userInput, List<String> options) {
        return (nextPg && ((foldSize - foldIndex) > 0 && (backPg && userInput.equals(options.get(options.size() - 3))) ||
                userInput.equals(options.get(options.size() - 2))));
    }

    final boolean backPgSelect(boolean backPg, int foldSize, String userInput, List<String> options) {
        return backPg && foldSize > MAXOPTION && userInput.equals(options.get(options.size() - 2));
    }

    final int oldFoldIndex(int descriptsSize, int foldIndex, int foldSize) {
        if (descriptsSize == 25) foldIndex = foldIndex - MAXOPTION - descriptsSize + 1;
        else foldIndex = foldIndex - MAXOPTION - descriptsSize; // +1 for back button
        boolean nextPg = false, backPg = false;
        if (checkIfPg(foldIndex + MAXOPTION, foldSize, nextPg, backPg) == 2) {
            backPg = true;
            foldIndex ++; // +1 for back pg
        }
        if (checkIfPg(foldIndex + MAXOPTION, foldSize, nextPg, backPg) == 1) {
            nextPg = true;
            foldIndex ++; // +1 for next pg
        }
        foldIndex ++;
        return foldIndex;
    }

    final boolean createFolder(String filePath, String folderNme, String chosenFolder) {
        Integer folderNum = Asker.askFolderNum();
        String userInput = Asker.askFolderInConfirm(folderNme, chosenFolder).toUpperCase();
        if (userInput.equals(Prompts.YES)) {
            for (int i = 0; i < folderNum; i++) {
                String newFolder = folderNme + String.format(" %d", i + 1);
                FolderCreator.createFolder(newFolder, 
                                            filePath + String.format("\\%s", chosenFolder));
            }
            return true;
        }
        else return false;
    }

    final boolean createFolder(String filePath, String folderNme, List<String> chosenCat) {
        String cat = chosenCat.get(0).split(" ")[0];
        String userInput = Asker.askFolderInConfirm(folderNme, 
                                                    String.format("All %s folders", cat)).toUpperCase();
        if (userInput.equals(Prompts.YES)) {
            FolderChecker.sortFolders(chosenCat);
            userInput = Asker.askYesNo(Asker.NUMBERFOLDERQ).toUpperCase();
            if (userInput.equals(Prompts.YES)) createMultiFolders(filePath, folderNme, 
                                                                  chosenCat, true);
            else createMultiFolders(filePath, folderNme, chosenCat, false);
            return true;
        }
        else return false; // no on "continue"
    }
    
    public abstract boolean doOption(String folderNme, String filePath);
}
