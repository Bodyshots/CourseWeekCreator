import java.io.File;

public class FolderCreator {
    public static final Integer MAXFOLDERS = 52; // Number of weeks in a year
    public static final Integer MINFOLDERS = 0;

    private String getFolderLoc() {
        String year, courseNme, filePath = "";      
        
        filePath = Prompts.askForFilePath() + "/";

        String yearQ = "What year? Eg. \'1st year\', \'2nd year\', etc.";
        year = Prompts.askForString(yearQ);

        String courseQ = "What course? Eg. \'CSC207\', \'MAT102\', etc.";
        courseNme = Prompts.askForString(courseQ);

        return new File(filePath + String.format("%s/%s", year, courseNme)).toString();
    }

    private Integer getWeeks() {
        String weekQ = "How many weeks? Max is 52.";
        return Prompts.askForInteger(weekQ, MINFOLDERS, MAXFOLDERS);
    }

    public void createWeekFolders() {
        String folderLoc = this.getFolderLoc();
        Integer weekNum = this.getWeeks();
        if (!Handler.handleFolderConf(folderLoc, weekNum)) {
            System.out.println("Exiting...");
            System.exit(0);
            }
        for (int i = 1; i < weekNum + 1; i++) {
            File folder = new File(folderLoc + "/" + String.format("Week %d", i));
            Boolean folderSuccess = folder.mkdirs();
            if (folderSuccess) System.out.println(String.format("Created folder \"Week %d\"", i));
            else System.out.println(String.format("Failed to create folder \"Week %d\". Folder already exists.", i));
        }
        Prompts.askForString("Finished. Enter any character to exit.");
        return;
    }

    public static void main(String[] args) {
        new FolderCreator().createWeekFolders();
    }

}
