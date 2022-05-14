package src.FolderBehaviours.FoldInFoldOpts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import src.Asker;
import src.FolderChecker;
import src.Prompts;

public class OptionA extends AbsOpt{

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

    private final List<String> chooseFolderCat(List<String> options, List<List<String>> folderCats,
                                            String userInput, int times) {

        List<String> chosenCat;
        if (times == 0) chosenCat = folderCats.get(times * MAXOPTION + options.indexOf(userInput));
        else chosenCat = folderCats.get(times * MAXOPTION + options.indexOf(userInput) - 1);
        return chosenCat;
    }

    @Override
    public boolean doOption(String folderNme, String filePath) {
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
        return createFolder(filePath, folderNme, chosenFolderCat);
    }
}
