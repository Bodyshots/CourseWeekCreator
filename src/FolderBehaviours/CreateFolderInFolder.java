package src.FolderBehaviours;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import src.FolderChecker;
import src.FolderCreator;
import src.Prompts;
import src.Asker;
import src.Main;

public class CreateFolderInFolder implements FolderBehaviour {
    static final int OFFSET = 65; 
    static final int MAXOPTION = 25;

    @Override
    public void doCreate(String filePath) {
        List<String> options = Arrays.asList(Prompts.OPTION_A,
                                             Prompts.OPTION_B,
                                             Prompts.OPTION_C,
                                             Prompts.OPTION_D);
        Boolean finished = false;

        while (!finished) {
            String userInput = Asker.askOption(Prompts.createFolderOptionsPrompt(), options).toUpperCase();
            if (!userInput.equals(Prompts.OPTION_D)) {
                String folderNme = Asker.askNotString(Asker.NEWFOLDERQ, Arrays.asList(""));

                boolean folderMade = false;
                if (userInput.equals(Prompts.OPTION_A)) folderMade = handleA(folderNme, filePath);
                else if (userInput.equals(Prompts.OPTION_B)) folderMade = handleB(folderNme, filePath); // "In a specific folder"
                else { // "Where I'm at now"
                    userInput = Asker.askFolderInConfirm(folderNme, filePath).toUpperCase();
                    if (userInput.equals(Prompts.YES)) {
                        FolderCreator.createFolder(folderNme, filePath); 
                        folderMade = true;
                    }
                    else return; // no on "continue"
                }
                if (folderMade) {
                    userInput = Asker.askContinue(DONE).toUpperCase();
                    if (userInput.equals(Prompts.NO)) Main.exitProgram();
                }
                return; // folders created
            }
            else finished = true;
        }
        return; // enters back (not "continue" back)
    }

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

    private final int checkIfPg(int foldIndex, int foldSize, 
                                boolean nextPg, boolean backPg) {
        if (!backPg && (foldIndex / (MAXOPTION + 1)) > 0) return 2; // back pg
        else if ((foldSize - foldIndex - 1) > 0) return 1; // next pg
        return 0;
    }

    private final List<String> getDescripts(int optionSize, int times, int offset, List<String> folders) {
        List<String> descripts = new ArrayList<>();
        for (int i = 0; i < optionSize; i++) {
            descripts.add(folders.get(i + times * MAXOPTION - offset));
        }
        return descripts;
    }

    private final List<String> getDescriptCats(int optionSize, int times, int offset, List<List<String>> folderCats) {
        List<String> descripts = new ArrayList<>();
        for (int i = 0; i < optionSize; i++) {
        String folderCat = folderCats.get(i).get(0).split(" ")[0];
        String descriptsStr = String.format("All \"%s\" folders", folderCat);
        if (new File(folderCats.get(i).get(0)).isHidden()) descriptsStr += " (Hidden)";
        descripts.add(descriptsStr);
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

    private final int handleLastOptsCat(List<String> descripts, List<String> options, List<List<String>> folderCats,
                                         int foldIndex, boolean nextPg, boolean backPg) {
        int option = 0;
        int foldSize = folderCats.size();
        boolean doneNext = false, doneBack = false;
        while (option < 2) {
            if (foldIndex < foldSize && (!nextPg && !backPg || (option == 0 && !(backPg && nextPg)))) {
                String cat = folderCats.get(foldIndex).get(0).split(" ")[0];
                descripts.add(String.format("All \"%s\" folders", cat));
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

    private final List<String> chooseFolderCat(List<String> options, List<List<String>> folderCats,
                                               String userInput, int times) {

        List<String> chosenCat;
        if (times == 0) chosenCat = folderCats.get(times * MAXOPTION + options.indexOf(userInput));
        else chosenCat = folderCats.get(times * MAXOPTION + options.indexOf(userInput) - 1);
        return chosenCat;
    }

    private final boolean createFolder(String filePath, String folderNme, String chosenFolder) {
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

    private final boolean createFolderCat(String filePath, String folderNme, List<String> chosenCat) {
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

    private final boolean nextPgSelect(boolean nextPg, boolean backPg, int foldSize,
                                       int foldIndex, String userInput, List<String> options) {
        return (nextPg && ((foldSize - foldIndex) > 0 && (backPg && userInput.equals(options.get(options.size() - 3))) ||
                userInput.equals(options.get(options.size() - 2))));
    }

    private final boolean backPgSelect(boolean backPg, int foldSize, String userInput, List<String> options) {
        return backPg && foldSize > MAXOPTION && userInput.equals(options.get(options.size() - 2));
    }

    private final int oldFoldIndex(int descriptsSize, int foldIndex, int foldSize) {
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

    private final boolean handleA(String folderNme, String filePath) {
        List<List<String>> folderCats = FolderChecker.listFolderCats(filePath);
        List<String> options = new ArrayList<>();
        int foldSize = folderCats.size();
        int times = 0, foldIndex = 0, offset = 0;
        String userInput = "";

        while (userInput == "") {
            if (foldIndex + 1 == foldSize || options.size() > MAXOPTION - 3 || 
                (foldIndex > MAXOPTION && foldIndex == foldSize)) { // for contents larger than A-Z
                boolean nextPg = false, backPg = false;
                List<String> descripts = getDescriptCats(options.size(), times, offset, folderCats);

                int result = checkIfPg(foldIndex, foldSize, nextPg, backPg);
                if (result == 2) {
                    backPg = true;
                    if (checkIfPg(foldIndex, foldSize, nextPg, backPg) == 1) nextPg = true;
                }
                else if (result == 1) nextPg = true;

                foldIndex = handleLastOptsCat(descripts, options, folderCats, foldIndex, nextPg, backPg);

                System.out.println(Asker.PICKFOLDERQ);
                userInput = Asker.askOption(Prompts.infOptions(options, descripts), options).toUpperCase();
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
        List<String> chosenFolderCat = chooseFolderCat(options, folderCats, userInput, times);
        return createFolderCat(filePath, folderNme, chosenFolderCat);
    }

    private final boolean handleB(String folderNme, String filePath) {
        List<String> folders = FolderChecker.listFolders(filePath);
        List<String> options = new ArrayList<>();
        int foldSize = folders.size();
        int times = 0, foldIndex = 0, offset = 0;
        String userInput = "";
        
        while (userInput == "") {
            if (foldIndex + 1 == foldSize || options.size() > MAXOPTION - 3 || 
                (foldIndex > MAXOPTION && foldIndex == foldSize)) { // for contents larger than A-Z
                boolean nextPg = false, backPg = false;
                List<String> descripts = getDescripts(options.size(), times, offset, folders);

                int result = checkIfPg(foldIndex, foldSize, nextPg, backPg);
                if (result == 2) {
                    backPg = true;
                    if (checkIfPg(foldIndex, foldSize, nextPg, backPg) == 1) nextPg = true;
                }
                else if (result == 1) nextPg = true;

                foldIndex = handleLastOpts(descripts, options, folders, foldIndex, nextPg, backPg);

                System.out.println(Asker.PICKFOLDERQ);
                userInput = Asker.askOption(Prompts.infOptions(options, descripts), options).toUpperCase();
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
