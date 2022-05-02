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
                    
                    while (userInput == "") {
                        if (options.size() > maxOption - 1 || foldIndex > maxOption && foldIndex == foldSize) { // for contents larger than A-Z
                            int leftoff = 0;
                            boolean nextPg = false, backPg = false;
                            for (int i = 0; i < options.size() + offset; i++) {
                                if ((foldSize - foldIndex - 1) > 0 && i > options.size() - 2) {
                                    descripts.add("Next Page");
                                    nextPg = true;
                                    if (leftoff == 0) leftoff = i + (foldIndex / maxOption - 1) * maxOption;
                                }
                                if ((foldIndex / (maxOption + 1)) > 0 && ((i > options.size() - 2 && nextPg) || i == options.size() - 1))  {
                                    descripts.add("Back Page");
                                    backPg = true;
                                    if (leftoff == 0) leftoff = i + (foldIndex / maxOption - 1) * maxOption;
                                }
                                if (leftoff == 0) descripts.add(folders.get(i + times * maxOption));
                            }
                            while (offset != 0) {
                                options.add(Character.toString(options.get(options.size() - 1).charAt(0) + 1));
                                offset --;
                            }

                            System.out.println(Asker.PICKFOLDERQ);
                            userInput = Asker.askOption(Prompts.infOptions(options, descripts), options).toUpperCase();
                            times ++;

                            if (foldSize > maxOption && backPg) // back pg option selected
                            {
                                // third-last opt if w/ nextPg, second-last opt if not 
                                if (userInput.equals(options.get(options.size() - 2))) {
                                        foldIndex = foldIndex - maxOption - descripts.size() + 2;
                                        userInput = "";
                                        options = new ArrayList<>();
                                        descripts = new ArrayList<>();
                                        times -= 2;
                                }
                            }
                            else if ((foldSize - foldIndex) > 0 && nextPg) { // next pg selected
                                if ((backPg && userInput.equals(options.get(options.size() - 3))) ||
                                     userInput.equals(options.get(options.size() - 2))) {
                                        userInput = "";
                                        options = new ArrayList<>();
                                        descripts = new ArrayList<>();
                                }
                            }
                            else if (userInput.equals(options.get(options.size() - 1))) return; // back button (not back pg)
                            while (leftoff != foldIndex && Math.abs(foldIndex - leftoff) < 3) {
                                descripts.add(folders.get(leftoff));
                                if (foldIndex - leftoff == 2) options.add(Prompts.OPTION_B);
                                else options.add(Prompts.OPTION_A);
                                leftoff ++;
                                offset ++;
                            }
                        }
                        else {
                            options.add(Character.toString(((foldIndex + offset) % maxOption) + OFFSET));
                            foldIndex ++;
                        }
                    }

                    String chosenFolder = folders.get(options.indexOf(userInput));

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
}
