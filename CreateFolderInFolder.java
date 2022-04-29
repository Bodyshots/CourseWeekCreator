import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateFolderInFolder implements FolderBehaviour {

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
                else if (userInput.equals(Prompts.OPTION_B)) {
                    List<String> folders = FolderChecker.listFolders(filePath);
                    options = new ArrayList<>();
                    for (int i = 0; i < folders.size(); i++) {
                        options.add(Character.toString((Prompts.OPTION_A.charAt(0) + i)));
                    }

                    System.out.println(Asker.PICKFOLDERQ);
                    userInput = Asker.askOption(Prompts.infOptions(options, folders), options).toUpperCase();
                    if (userInput.equals(options.get(options.size() - 1))) return; // back

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
