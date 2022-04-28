import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FolderCreator {
    private static final Integer MINFOLDERS = 1;

    private String filePath;

    private String getWeekPath() {
        return String.format("%s\\", this.filePath) + this.getYear() +
               String.format("\\%s", this.getCourse());
    }

    private String getYear() {
        return Asker.askNotString("What year? Eg. \'1st year\', \'2nd year\', etc.", Arrays.asList(""));
    }
    
    private String getCourse() {
        return Asker.askNotString("What course? Eg. \'CSC207\', \'MAT102\', etc.", Arrays.asList(""));
    }

    private void setFilePath() {
        String userInput = "";
        boolean confirmPath = false;
        String tempFilePath = "";
        while (this.filePath == null || !confirmPath) {
            if (tempFilePath == null) {
                tempFilePath = Asker.askFilePath("Enter the filepath for where your folders should be created:");
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
        return Asker.askInteger("How many folders?", MINFOLDERS, Integer.MAX_VALUE);
    }

    private final void createMassFolders() {
        int folderTotal = this.setFolderTotal();
        this.setFilePath();
        String folderNme = Asker.askNotString("What is this new folder\'s name?", Arrays.asList(""));
        
        String userInput = Asker.askFoldersConfirm(folderNme, filePath, folderTotal).toUpperCase();
        if (userInput.equals(Prompts.NO)) return;

        for (int i = 1; i < folderTotal + 1; i++) {
            createFolder(String.format("%s %d", folderNme, i), this.filePath);
        }

        userInput = Asker.askContinue("Finished.").toUpperCase();
        if (userInput.equals(Prompts.NO)) this.exitProgram();
    }

    private final void createMassFolders (int folderTotal, String filePath, String folderNme) {
        String userInput = Asker.askFoldersConfirm(folderNme, filePath, folderTotal).toUpperCase();
        if (userInput.equals(Prompts.NO)) return;

        for (int i = 1; i < folderTotal + 1; i++) {
            createFolder(String.format("%s %d", folderNme, i), this.filePath);
        }

        userInput = Asker.askContinue("Finished.").toUpperCase();
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
                String folderNme = Asker.askNotString("What is this new folder\'s name?", Arrays.asList(""));

                if (userInput.equals(Prompts.OPTION_A)) { // need fix here, "In a series of folders"
                    List<List<String>> folderCats = FolderChecker.listFolderCats(filePath);
                    options = new ArrayList<>(); // valid choices
                    List<String> descripts = new ArrayList<>(); // descriptions of each option
                    for (int i = 0; i < folderCats.size(); i++) {
                        // List<String> cat: folderCats) { // later needs a "next" option to handle more than Z options
                        String folderCat = folderCats.get(i).get(0).split(" ")[0];
                        descripts.add(String.format("All \"%s\" folders", folderCat));
                        options.add(Character.toString((Prompts.OPTION_A.charAt(0) + i)));
                    }
                    
                    System.out.println("Which folder do you want to create your folders in?");
                    userInput = Asker.askOption(Prompts.infOptions(options, descripts), options).toUpperCase();
                    if (userInput.equals(options.get(options.size() - 1))) return; // back

                    List<String> chosenCat = folderCats.get(options.indexOf(userInput));
                    FolderChecker.sortFolders(chosenCat);
                    String cat = chosenCat.get(0).split(" ")[0];
                    userInput = Asker.askFolderInConfirm(folderNme, String.format("All %s folders", cat)).toUpperCase();
                    if (userInput.equals(Prompts.YES)) createMultiFolders(folderNme, chosenCat);
                }
                else if (userInput.equals(Prompts.OPTION_B)) { // "In a specific folder", needs fixing
                    String weekNum = "Week ";
                    String weekNumQ = String.format("How many?", MINFOLDERS, Integer.MAX_VALUE);
                    weekNum += Integer.toString(Asker.askInteger(weekNumQ, MINFOLDERS, Integer.MAX_VALUE));
                    userInput = Asker.askFolderInConfirm(folderNme, weekNum).toUpperCase();
                    if (userInput.equals(Prompts.YES)) {
                        createFolder(folderNme, this.filePath + String.format("\\%s", weekNum));
                    }
                }
                else { // "Where I'm at now"
                    userInput = Asker.askFolderInConfirm(folderNme, this.filePath).toUpperCase();
                    if (userInput.equals(Prompts.YES)) createFolder(folderNme, this.filePath);
                }
                userInput = Asker.askContinue("Finished.").toUpperCase();
                if (userInput.equals(Prompts.NO)) this.exitProgram();
                return;
            }
            else finished = true;
        }
        return;
    }

    private final void createMultiFolders(String folderNme, List<String> folders) {
        // String cat = folders.get(0).split(" ")[0];
        // String folderPath = this.filePath + String.format("\\%s", cat);
        String numberQ = "Number these folders? (eg. Lab 1, Lab 2, etc.)";

        String userInput = Asker.askYesNo(numberQ).toUpperCase();
        if (userInput.equals(Prompts.YES)) {
            for (int i = 0; i < folders.size(); i++) {
                String currentFolderNme = folderNme + String.format(" %d", i + 1);
                createFolder(currentFolderNme, this.filePath + String.format("\\%s", folders.get(i)));
            }
            // for (int i = 1; i < folders.size() + 1; i++) {
            // String currentFolderNme = folderNme + String.format(" %d", i);
            // createFolder(currentFolderNme, folderPath + String.format(" %d", i));
            // }
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
        System.out.println("Exiting...");
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
            if (userInput.equals(Prompts.OPTION_A)) this.createMassFolders(); // done
            else if (userInput.equals(Prompts.OPTION_B)) this.createWeekFolders(); // done
            else if (userInput.equals(Prompts.OPTION_C)) this.setFilePath(); // done
            else if (userInput.equals(Prompts.OPTION_D)) this.createFolderInFolder();
            else exitProgram = true;
        }
        this.exitProgram();
    }

    public static void main(String[] args) {
        new FolderCreator().run();
    }

}
