import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FolderCreator {
    private static final Integer MAXFOLDERS = 52; // Number of weeks in a year
    private static final Integer MINFOLDERS = 1;

    private String year;
    private String filePath;
    private String courseNme;
    private String weekFilePath;
    private Integer weekTotal;

    public String getFilePath() {
        return this.filePath;
    }

    public String getYear() {
        return this.year;
    }

    public String getCourse() {
        return this.courseNme;
    }

    public Integer getWeekTotal() {
        return this.weekTotal;
    }

    public String getWeekFilePath() {    
        return this.weekFilePath;
    }

    private void setUserFilePath() {
        this.filePath = Asker.askFilePath();
        this.year = null;
        this.courseNme = null;
        this.weekTotal = null;
        this.weekFilePath = null;
    }

    private void setUserYear() {
        this.year = Asker.askString("What year? Eg. \'1st year\', \'2nd year\', etc.");
    }
    
    private void setUserCourse() {
        this.courseNme = Asker.askString("What course? Eg. \'CSC207\', \'MAT102\', etc.");
    }

    private void setUserWeekFilePath() {
        if (filePath == null) this.setUserFilePath();
        if (year == null) this.setUserYear();
        if (courseNme == null) this.setUserCourse();

        this.weekFilePath = new File(filePath + "\\" + String.format("%s\\%s", year, courseNme)).toString();
    }

    private void setUserWeekTotal() {
        this.weekTotal = Asker.askInteger("How many weeks? Max is 52.", MINFOLDERS, MAXFOLDERS);
    }

    private void createWeekFolders() {
        this.setUserYear();
        this.setUserCourse();
        this.setUserWeekFilePath();
        this.setUserWeekTotal();

        String userInput = Asker.askWeekFolderConfirm(this.getWeekFilePath(), this.getWeekTotal());
        if (userInput.equals(Prompts.NO)) {
            this.year = null;
            this.courseNme = null;
            this.weekTotal = null;
            this.weekFilePath = null;
            return;
        }
        for (int i = 1; i < weekTotal + 1; i++) createFolder(String.format("Week %d", i), this.weekFilePath);

        userInput = Asker.askContinue("Finished.");
        if (userInput.equals(Prompts.NO)) this.exitProgram();
        return;
    }

    private final void createFolderInFolder() {
        if (this.weekFilePath == null) {
            Asker.askString("Create your week folders first!");
            return;
        }

        List<String> options = Arrays.asList(Prompts.OPTION_A,
                                             Prompts.OPTION_B,
                                             Prompts.OPTION_C,
                                             Prompts.OPTION_D);
        Boolean finished = false;
        while (!finished) {
            String userInput = Asker.askOption(Prompts.createFolderOptionsPrompt(), options);
            if (!userInput.equals(Prompts.OPTION_D)) {
                String folderNme = Asker.askString("What is this folder\'s name?");

                if (userInput.equals(Prompts.OPTION_A)) {
                    userInput = Asker.askFolderConfirm(folderNme, String.format("Weeks 1-%d", this.weekTotal));
                    if (userInput.equals(Prompts.YES)) {
                        for (int i = 1; i < this.weekTotal + 1; i++) {
                            createFolder(folderNme, this.getWeekFilePath() + String.format("\\Week %d", i));
                        }
                    }
                }
                else if (userInput.equals(Prompts.OPTION_B)) {
                    String weekNum = "Week ";
                    String weekNumQ = String.format("What week? Select from weeks %d-%d", MINFOLDERS, this.weekTotal);
                    weekNum += Integer.toString(Asker.askInteger(weekNumQ, MINFOLDERS, this.weekTotal));
                    userInput = Asker.askFolderConfirm(folderNme, weekNum);
                    if (userInput.equals(Prompts.YES)) {
                        createFolder(folderNme, this.getWeekFilePath() + String.format("\\%s", weekNum));
                    }
                }
                else {
                    userInput = Asker.askFolderConfirm(folderNme, String.format("%s", courseNme));
                    if (userInput.equals(Prompts.YES)) {
                        createFolder(folderNme, this.getFilePath() + String.format("\\%s", courseNme));
                    }
                }
                userInput = Asker.askContinue("Finished.");
                if (userInput.equals(Prompts.NO)) this.exitProgram();
                return;
            }
            else finished = true;
        }
        return;
    }

    private final void createFolder(String newFolder, String desiredPath) {
        if (new File(desiredPath + String.format("\\%s", newFolder)).mkdirs()) {            
            System.out.println(String.format("Created folder \"%s\" in %s.", newFolder, desiredPath));
        }
        else System.out.println(String.format("Failed to create folder \"%s\". Folder already exists.", newFolder));
    }

    public void run() {
        this.setUserFilePath();

        List<String> options = Arrays.asList(Prompts.OPTION_A,
                                             Prompts.OPTION_B,
                                             Prompts.OPTION_C,
                                             Prompts.OPTION_D);
        Boolean exitProgram = false;
        while (!exitProgram) {
            String userInput = Asker.askOption(Prompts.folderOptionsPrompt(), options);
            if (userInput.equals(Prompts.OPTION_A)) this.createFolderInFolder();
            else if (userInput.equals(Prompts.OPTION_B)) this.createWeekFolders();
            else if (userInput.equals(Prompts.OPTION_C)) this.setUserFilePath();
            else exitProgram = true;
        }
        this.exitProgram();
    }

    private void exitProgram() {
        System.out.println("Exiting...");
        System.exit(0);
    }

    public static void main(String[] args) {
        new FolderCreator().run();
    }

}
