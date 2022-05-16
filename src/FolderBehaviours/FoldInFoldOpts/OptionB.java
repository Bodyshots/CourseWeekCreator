package src.FolderBehaviours.FoldInFoldOpts;

import java.util.ArrayList;
import java.util.List;

import src.Asker;
import src.FolderChecker;

public class OptionB extends AbsOpt {

    private final List<String> getDescripts(int optionSize, int times, int offset, List<String> folders) {
        List<String> descripts = new ArrayList<>();
        for (int i = 0; i < optionSize; i++) {
            descripts.add(folders.get(i + times * MAXOPTION - offset));
        }
        return descripts;
    }

    private final int handleLastOpts(List<String> descripts, List<String> options, List<String> folders,
                                      int foldIndex, boolean nextPg, boolean backPg) {
        int option = 0;
        int foldSize = folders.size();
        boolean doneNext = false, doneBack = false;
        while (option < 2) {
            if (foldIndex < foldSize && ((!nextPg && !backPg) || (option == 0 && !(backPg && nextPg)))) {
                descripts.add(folders.get(foldIndex));
                foldIndex ++;
            }
            else if (!doneNext && nextPg) {
                descripts.add("Next Page");
                doneNext = true;
            }
            else if (!doneBack && backPg) {
                descripts.add("Back Page");
                doneBack = true;
            }
            else break;
            options.add(Character.toString(options.get(options.size() - 1).charAt(0) + 1));
            option ++;
        }
        return foldIndex;
    }

    private final String chooseFolder(List<String> options, List<String> folders, 
                                      String userInput, int times) {
        String chosenFolder;
        if (times == 0) chosenFolder = folders.get(times * MAXOPTION + options.indexOf(userInput));
        else chosenFolder = folders.get(times * MAXOPTION + options.indexOf(userInput) - 1);
        return chosenFolder;
    }

    @Override
    public boolean doOption(String folderNme, String filePath) {
        List<String> folders = FolderChecker.listFolders(filePath);
        List<String> options = new ArrayList<>();
        int foldSize = folders.size();
        int times = 0, foldIndex = 0, offset = 0;
        String userInput = "";
        
        while (userInput == "") {
            if (foldSize == 0 || foldIndex + 1 == foldSize || options.size() > MAXOPTION - 3 || 
                (foldIndex > MAXOPTION && foldIndex == foldSize)) { // for contents larger than A-Z
                boolean nextPg = false, backPg = false;
                List<String> descripts;
                if (foldSize != 0) descripts = getDescripts(options.size(), times, offset, folders);
                else descripts = new ArrayList<>();

                int result = checkIfPg(foldIndex, foldSize, nextPg, backPg);
                if (result == 2) {
                    backPg = true;
                    if (checkIfPg(foldIndex, foldSize, nextPg, backPg) == 1) nextPg = true;
                }
                else if (result == 1) nextPg = true;

                foldIndex = handleLastOpts(descripts, options, folders, foldIndex, nextPg, backPg);

                userInput = Asker.askFolderChoice(options, descripts).toUpperCase();
                times ++;

                if (!(userInput.equals(options.get(options.size() - 1)))) { // if not back button
                    if (nextPgSelect(nextPg, backPg, foldSize, foldIndex, userInput, options) || 
                        backPgSelect(backPg, foldSize, userInput, options)) {
                            if (backPgSelect(backPg, foldSize, userInput, options)) {
                                foldIndex = oldFoldIndex(descripts.size(), foldIndex, foldSize);
                                times -= 2;
                            }
                            userInput = "";
                            options = new ArrayList<>();
                            descripts = new ArrayList<>();
                            if (foldIndex != 0) offset = (MAXOPTION * times) % foldIndex;
                            else offset = 0;
                        }
                }
                else return false; // back button (not back pg)
            }
            else {
                options.add(Character.toString(((foldIndex + offset) % MAXOPTION) + OFFSET));
                foldIndex ++;
            }
        }
        
        if (times != 0) times --;
        String chosenFolder = chooseFolder(options, folders, userInput, times);
        return createFolder(filePath, folderNme, chosenFolder);
    }

}
