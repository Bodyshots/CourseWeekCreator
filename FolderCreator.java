import java.io.File;
import java.util.ArrayList;
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
        this.filePath = Asker.askFilePath("Enter the filepath for where your folders should be created:");
        this.year = null;
        this.courseNme = null;
        this.weekTotal = null;
        this.weekFilePath = null;
    }

    private void setUserYear() {
        this.year = Asker.askStringNonEmpty("What year? Eg. \'1st year\', \'2nd year\', etc.");
    }
    
    private void setUserCourse() {
        this.courseNme = Asker.askStringNonEmpty("What course? Eg. \'CSC207\', \'MAT102\', etc.");
    }

    private void setUserWeekFilePath() {
        if (filePath == null) this.setUserFilePath();
        if (year == null) this.setUserYear();
        if (courseNme == null) this.setUserCourse();

        this.weekFilePath = filePath + "\\" + String.format("%s\\%s", year, courseNme);
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
            Asker.askString("Configure your course and year folders first!");
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
                String folderNme = Asker.askStringNonEmpty("What is this new folder\'s name?");

                if (userInput.equals(Prompts.OPTION_A)) {
                    userInput = Asker.askFolderConfirm(folderNme, String.format("Weeks 1-%d", this.weekTotal));
                    if (userInput.equals(Prompts.YES)) createMultiFolders(folderNme);
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

    private final void createMultiFolders(String folderNme) {
        String weekFolder = this.getWeekFilePath() + "\\Week ";
        String numberQ = "Number these folders? (eg. Lab 1, Lab 2, etc.)";

        System.out.println(numberQ);
        String userInput = Asker.askYesNo("Number these folders? (eg. Lab 1, Lab 2, etc.)");
        if (userInput.equals(Prompts.YES)) {
            for (int i = 1; i < this.weekTotal + 1; i++) {
            String currentFolderNme = folderNme + String.format(" %d", i);
            createFolder(currentFolderNme, weekFolder + String.format("%d", i));
            }
        }
        else
        {
            for (int i = 1; i < this.weekTotal + 1; i++) {
                createFolder(folderNme, weekFolder + String.format("%d", i));
            }
        }
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
                                             Prompts.OPTION_D,
                                             Prompts.OPTION_E);
        Boolean exitProgram = false;
        while (!exitProgram) {
            String userInput = Asker.askOption(Prompts.folderOptionsPrompt(), options);
            if (userInput.equals(Prompts.OPTION_A)) this.createFolderInFolder();
            else if (userInput.equals(Prompts.OPTION_B)) this.createWeekFolders();
            else if (userInput.equals(Prompts.OPTION_C)) this.setUserFilePath();
            else if (userInput.equals(Prompts.OPTION_D)) this.configureYearAndCourse();
            else exitProgram = true;
        }
        this.exitProgram();
    }

    private final void configureYearAndCourse() {
        if (FolderChecker.folderEmpty(this.filePath)) {
            System.out.println("There are no possible year folders in your current main file path!");
            return;
        }

        boolean configured = false;
        String tempCourse = "";
        String tempYear = "";
        String proposedPath = "";
        Integer tempWeekTotal = 0;
        String yearQ = "Enter the name of your year folder here.\nNote that the year folder must be in your " +
        "configured main path.\nThe year and course paths are also configured automatically when " +
        "\"Week\" folders are created:";
        String courseQ = "Enter the name of your course folder.\nPlease ensure this folder " +
        "has \"Week\" folders that are contiguous and properly numbered\n(eg. has \"Week\" folders from 1-10):";

        String yearPath = Asker.askFilePath(yearQ, this.filePath);
        if (!FolderChecker.isPopFolder(yearPath)) return;

        String [] folderFiles = FolderChecker.listFolderFiles(yearPath);
        while (!configured) {
            tempCourse = Asker.askString(courseQ);
            proposedPath = yearPath + String.format("\\%s", tempCourse);

            if (!Arrays.asList(folderFiles).contains(tempCourse)) {
                System.out.println("This file doesn\'t exist!");
            }
            else if (!FolderChecker.isPopFolder(yearPath));
            else configured = true;
        }

        tempWeekTotal = configureWeekTotal(proposedPath);
        if (tempWeekTotal == -1) return;

        this.courseNme = tempCourse;
        this.year = tempYear;
        this.weekFilePath = proposedPath;
        this.weekTotal = tempWeekTotal;

    }

    private Integer configureWeekTotal(String weekPath) {
        String [] files = FolderChecker.listFolderFiles(weekPath);
        List<String> missingFolders = new ArrayList<>();
        Arrays.sort(files);
        Integer weeks = 0;
        for (String file: files) {
            if (file.startsWith("Week ")) {
                weeks += 1;
                String weekFolder = String.format("Week %d", weeks);
                if (!file.equals(weekFolder)) missingFolders.add(weekFolder);
            }
        }

        if (!missingFolders.isEmpty()) {
            for (String missingFolder: missingFolders) {
                System.out.println(String.format("Missing %s from Weeks 1-%d!", missingFolder, weeks));
            }
            System.out.println("Please ensure your week folders are contiguous and properly numbered!");
            return -1;
        }
        else if (weeks == 0) {
            System.out.println("There are no \"Week\" folders in this year folder!");
            return -1;
        }
        return weeks;
    }

    private void exitProgram() {
        System.out.println("Exiting...");
        System.exit(0);
    }

    public static void main(String[] args) {
        new FolderCreator().run();
    }

}
