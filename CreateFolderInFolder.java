import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateFolderInFolder implements FolderBehaviour {
    static final int OFFSET = 65; 

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

                if (userInput.equals(Prompts.OPTION_A)) {
                    List<List<String>> folderCats = FolderChecker.listFolderCats(filePath);
                    options = new ArrayList<>(); // valid choices
                    List<String> descripts = new ArrayList<>(); // descriptions of each option
                    for (int i = 0; i < folderCats.size(); i++) {
                        // List<String> cat: folderCats) { // later might need a "next" option to handle more than Z options
                        String folderCat = folderCats.get(i).get(0).split(" ")[0];
                        String descriptsStr = String.format("All \"%s\" folders", folderCat);
                        if (new File(folderCats.get(i).get(0)).isHidden()) descriptsStr += " (Hidden)";
                        descripts.add(descriptsStr);
                        options.add(Character.toString((Prompts.OPTION_A.charAt(0) + i)));
                    }
                    
                    System.out.println(Asker.PICKFOLDERQ);
                    userInput = Asker.askOption(Prompts.infOptions(options, descripts), options).toUpperCase();
                    if (userInput.equals(options.get(options.size() - 1))) return; // back

                    List<String> chosenCat = folderCats.get(options.indexOf(userInput));
                    String cat = chosenCat.get(0).split(" ")[0];
                    userInput = Asker.askFolderInConfirm(folderNme, 
                                                         String.format("All %s folders", cat)).toUpperCase();
                    if (userInput.equals(Prompts.YES)) {
                        FolderChecker.sortFolders(chosenCat);
                        userInput = Asker.askYesNo(Asker.NUMBERFOLDERQ).toUpperCase();
                        if (userInput.equals(Prompts.YES)) createMultiFolders(filePath, folderNme, 
                                                                                        chosenCat, true);
                        else createMultiFolders(filePath, folderNme, chosenCat, false);
                    }
                    else return; // no on "continue"
                }
                else if (userInput.equals(Prompts.OPTION_B)) { // Z => Back, "In a specific folder"
                    List<String> folders = FolderChecker.listFolders(filePath);
                    options = new ArrayList<>();
                    int maxOption = Prompts.OPTION_Z.charAt(0) - Prompts.OPTION_A.charAt(0);
                    
                    int foldIndex = 0;
                    int foldSize = folders.size();
                    userInput = "";
                    int times = 0;
                    List<String> descripts = new ArrayList<>();
                    int offset = 0;
                    
                    while (userInput == "") { // adds options
                        // adds descripts
                        if (foldIndex == foldSize || options.size() > maxOption - 3 || 
                           (foldIndex > maxOption && foldIndex == foldSize)) { // for contents larger than A-Z
                            boolean nextPg = false, backPg = false;
                            for (int i = 0; i < options.size(); i++) descripts.add(folders.get(i + times * maxOption - offset));

                            int result = checkIfPg(foldIndex, foldSize, maxOption, options, nextPg, backPg);
                            if (result == 2) {
                                backPg = true;
                                result = checkIfPg(foldIndex, foldSize, maxOption, options, nextPg, backPg);
                                if (result == 1) nextPg = true;
                            }
                            else if (result == 1) nextPg = true;

                            int option = 0;
                            boolean doneNext = false, doneBack = false;
                            while (option < 2) {
                                if (foldIndex < foldSize && (!nextPg && !backPg || (option == 0 && !(backPg && nextPg)))) {
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

                                // int result = checkIfPg(foldIndex, foldSize, maxOption, i, options, nextPg, backPg);

                                
                                // if (result == 1) {
                                //     descripts.add("Next Page");
                                //     nextPg = true;
                                //     if (leftoff == 0) leftoff = i + (foldIndex / maxOption - 1) * maxOption;
                                // }
                                // else if (result == 2) {
                                //     descripts.add("Back Page");
                                //     backPg = true;
                                //     if (leftoff == 0) leftoff = i + (foldIndex / maxOption - 1) * maxOption;
                                // }
                                // else {
                                //     if (leftoff == 0) descripts.add(folders.get(i + times * maxOption));
                                //     else {
                                //         foldIndex ++;
                                //         options.add(Character.toString(options.get(options.size() - 1).charAt(0) + 1));
                                //     }
                                // }
                            // while (offset != 0) {
                            //     options.add(Character.toString(options.get(options.size() - 1).charAt(0) + 1));
                            //     offset --;
                            // }

                            System.out.println(Asker.PICKFOLDERQ);
                            userInput = Asker.askOption(Prompts.infOptions(options, descripts), options).toUpperCase();
                            times ++;

                            if (foldSize > maxOption && backPg && 
                                userInput.equals(options.get(options.size() - 2))) // back pg option selected
                            {
                                // third-last opt if w/ nextPg, second-last opt if not 
                                foldIndex = foldIndex - maxOption - descripts.size() + 3;
                                userInput = "";
                                options = new ArrayList<>();
                                descripts = new ArrayList<>();
                                times -= 2;
                                nextPg = false; backPg = false;
                                result = checkIfPg(foldIndex + times * maxOption, 
                                                   foldSize, maxOption, options, nextPg, backPg);
                                if (result == 2) {
                                    backPg = true;
                                    offset --;
                                    result = checkIfPg(foldIndex, foldSize, maxOption, options, nextPg, backPg);
                                    if (result == 1) {
                                        nextPg = true;
                                        offset --;
                                    }
                                }
                                else if (result == 1) {
                                    nextPg = true;
                                    offset --;
                                }
                                if (offset < 0) offset = 0;
                            }
                            else if (nextPg && ((foldSize - foldIndex) > 0 && (backPg && userInput.equals(options.get(options.size() - 3))) ||
                                      userInput.equals(options.get(options.size() - 2)))) { // next pg selected
                                userInput = "";
                                options = new ArrayList<>();
                                descripts = new ArrayList<>();
                                if (doneBack) offset ++;
                                if (doneNext) offset ++;
                            }
                            else if (userInput.equals(options.get(options.size() - 1))) return; // back button (not back pg)
                        }
                        else {
                            options.add(Character.toString(((foldIndex + offset) % maxOption) + OFFSET));
                            foldIndex ++;
                        }
                    }
                    
                    if (times != 0) times --;
                    String chosenFolder;
                    if (times == 0) chosenFolder = folders.get(times * maxOption + options.indexOf(userInput));
                    else chosenFolder = folders.get(times * maxOption + options.indexOf(userInput) - 1);

                    Integer folderNum = Asker.askFolderNum();
                    userInput = Asker.askFolderInConfirm(folderNme, chosenFolder).toUpperCase();
                    if (userInput.equals(Prompts.YES)) {
                        for (int i = 0; i < folderNum; i++) {
                            String newFolder = folderNme + String.format(" %d", i + 1);
                            FolderCreator.createFolder(newFolder, 
                                                       filePath + String.format("\\%s", chosenFolder));
                        }
                    }
                    else return;
                }
                else { // "Where I'm at now"
                    userInput = Asker.askFolderInConfirm(folderNme, filePath).toUpperCase();
                    if (userInput.equals(Prompts.YES)) FolderCreator.createFolder(folderNme, filePath);
                    else return; // no on "continue"
                }
                userInput = Asker.askContinue(DONE).toUpperCase();
                if (userInput.equals(Prompts.NO)) Main.exitProgram();
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
            for (int i = 1; i < folders.size() + 1; i++) {
                FolderCreator.createFolder(folderNme, filePath + folders.get(i));
            }
        }
    }

    private final int checkIfPg(int foldIndex, int foldSize, int maxOption, 
                                List<String> options, boolean nextPg, boolean backPg) {
        if (!backPg && (foldIndex / (maxOption + 1)) > 0) { // back pg
            return 2;
        }
        else if ((foldSize - foldIndex - 1) > 0) {
            return 1;
        }
        return 0;
    }
}
