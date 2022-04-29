import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FolderCreator {
    private static final Integer MINFOLDERS = 1;
    static final String DONE = "Finished";
    static final String EXIT = "Exiting...";

    private String filePath;

    private String getWeekPath() {
        return String.format("%s\\", this.filePath) + this.getYear() +
               String.format("\\%s", this.getCourse());
    }

    private String getYear() {
        return Asker.askNotString(Asker.YEARQ, Arrays.asList(""));
    }
    
    private String getCourse() {
        return Asker.askNotString(Asker.COURSEQ, Arrays.asList(""));
    }

    private void setFilePath() {
        String userInput = "";
        boolean confirmPath = false;
        String tempFilePath = "";
        while (this.filePath == null || !confirmPath) {
            if (tempFilePath == null) {
                tempFilePath = Asker.askFilePath(Asker.FILEPATHQ);
                if (tempFilePath == "") tempFilePath = PathFinder.getDefault();
            }
            else {
                if (this.filePath != null) tempFilePath = this.filePath;
                userInput = Asker.askYesNo(Prompts.continueFilePath(tempFilePath)).toUpperCase();
                if (userInput.equals(Prompts.YES)) {
                    confirmPath = true;
                    if (this.filePath == null) this.filePath = tempFilePath;
                }
                else {
                    this.filePath = null;
                    tempFilePath = null;
                }
            }
        }
    }

    private Integer setFolderTotal() {
        return Asker.askInteger(Asker.FOLDERTOTALQ, MINFOLDERS, Integer.MAX_VALUE);
    }

    private final void createMassFolders() {
        int folderTotal = this.setFolderTotal();
        this.setFilePath();
        String folderNme = Asker.askNotString(Asker.NEWFOLDERQ, Arrays.asList(""));
        
        String userInput = Asker.askFoldersConfirm(folderNme, filePath, folderTotal).toUpperCase();
        if (userInput.equals(Prompts.NO)) return;

        for (int i = 1; i < folderTotal + 1; i++) {
            createFolder(String.format("%s %d", folderNme, i), this.filePath);
        }

        userInput = Asker.askContinue(DONE).toUpperCase();
        if (userInput.equals(Prompts.NO)) this.exitProgram();
    }

    private final void createMassFolders (int folderTotal, String filePath, String folderNme) {
        String userInput = Asker.askFoldersConfirm(folderNme, filePath, folderTotal).toUpperCase();
        if (userInput.equals(Prompts.NO)) return;

        for (int i = 1; i < folderTotal + 1; i++) {
            createFolder(String.format("%s %d", folderNme, i), this.filePath);
        }

        userInput = Asker.askContinue(DONE).toUpperCase();
        if (userInput.equals(Prompts.NO)) this.exitProgram();
    }

    private void createWeekFolders() {

        String userInput = Asker.askContinue(Prompts.weekFolderExplain()).toUpperCase();
        if (userInput.equals(Prompts.NO)) return; // Don't create "week" folders

        this.setFilePath();
        int folderTotal = this.setFolderTotal();
        String weekPath = this.getWeekPath();

        userInput = Asker.askFoldersConfirm("Week", filePath, folderTotal).toUpperCase();;
        if (userInput.equals(Prompts.NO)) return;

        this.createMassFolders(folderTotal, weekPath, "Week");
        return;
    }

    private final void createFolderInFolder() {
        List<String> options = Arrays.asList(Prompts.OPTION_A,
                                             Prompts.OPTION_B,
                                             Prompts.OPTION_C,
                                             Prompts.OPTION_D);
        Boolean finished = false;
        this.setFilePath();

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
                    userInput = Asker.askFolderInConfirm(folderNme, String.format("All %s folders", cat)).toUpperCase();
                    if (userInput.equals(Prompts.YES)) {
                        FolderChecker.sortFolders(chosenCat);
                        userInput = Asker.askYesNo(Asker.NUMBERFOLDERQ).toUpperCase();
                        if (userInput.equals(Prompts.YES)) createMultiFolders(folderNme, chosenCat, true);
                        else createMultiFolders(folderNme, chosenCat, false);
                    }
                    else return; // no on "continue"
                }
                else if (userInput.equals(Prompts.OPTION_B)) {
                    List<String> folders = FolderChecker.listFolders(this.filePath);
                    options = new ArrayList<>();
                    for (int i = 0; i < folders.size(); i++) {
                        options.add(Character.toString((Prompts.OPTION_A.charAt(0) + i)));
                    }

                    System.out.println(Asker.PICKFOLDERQ);
                    userInput = Asker.askOption(Prompts.infOptions(options, folders), options).toUpperCase();
                    if (userInput.equals(options.get(options.size() - 1))) return; // back

                    String chosenFolder = folders.get(options.indexOf(userInput));

                    Integer folderNum = Asker.askInteger(Asker.FOLDERTOTALQ, MINFOLDERS, Integer.MAX_VALUE);
                    userInput = Asker.askFolderInConfirm(folderNme, chosenFolder).toUpperCase();
                    if (userInput.equals(Prompts.YES)) {
                        for (int i = 0; i < folderNum; i++) {
                            String newFolder = folderNme + String.format(" %d", i + 1);
                            createFolder(newFolder, this.filePath + String.format("\\%s", chosenFolder));
                        }
                    }
                    else return;
                }
                else { // "Where I'm at now"
                    userInput = Asker.askFolderInConfirm(folderNme, this.filePath).toUpperCase();
                    if (userInput.equals(Prompts.YES)) createFolder(folderNme, this.filePath);
                    else return; // no on "continue"
                }
                userInput = Asker.askContinue(DONE).toUpperCase();
                if (userInput.equals(Prompts.NO)) this.exitProgram();
                return; // folders created
            }
            else finished = true;
        }
        return; // enters back (not "continue" back)
    }

    private final void createMultiFolders(String folderNme, List<String> folders, boolean number) {
        if (number) {
            for (int i = 0; i < folders.size(); i++) {
                String currentFolderNme = folderNme + String.format(" %d", i + 1);
                createFolder(currentFolderNme, this.filePath + String.format("\\%s", folders.get(i)));
            }
        }
        else
        {
            for (int i = 1; i < folders.size() + 1; i++) {
                createFolder(folderNme, this.filePath + folders.get(i));
            }
        }
    }

    private final void createFolder(String newFolder, String desiredPath) {
        if (new File(desiredPath + String.format("\\%s", newFolder)).mkdirs()) {            
            System.out.println(String.format("Created folder \"%s\" in %s.", newFolder, desiredPath));
        }
        else System.out.println(String.format("Failed to create folder \"%s\". Folder already exists.", newFolder));
    }

    private void exitProgram() {
        System.out.println(EXIT);
        System.exit(0);
    }

    public void run() {
        this.filePath = PathFinder.getDefault();
        List<String> options = Arrays.asList(Prompts.OPTION_A,
                                             Prompts.OPTION_B,
                                             Prompts.OPTION_C,
                                             Prompts.OPTION_D,
                                             Prompts.OPTION_E);
        Boolean exitProgram = false;
        while (!exitProgram) {
            String userInput = Asker.askOption(Prompts.folderOptionsPrompt(this.filePath), options).toUpperCase();
            if (userInput.equals(Prompts.OPTION_A)) this.createMassFolders();
            else if (userInput.equals(Prompts.OPTION_B)) this.createWeekFolders();
            else if (userInput.equals(Prompts.OPTION_C)) this.setFilePath();
            else if (userInput.equals(Prompts.OPTION_D)) this.createFolderInFolder();
            else exitProgram = true;
        }
        this.exitProgram();
    }

    public static void main(String[] args) {
        new FolderCreator().run();
    }

}
