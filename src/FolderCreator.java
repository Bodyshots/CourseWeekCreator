package src;
import java.io.File;

import src.FolderBehaviours.CreateNull;
import src.FolderBehaviours.FolderBehaviour;

public class FolderCreator {
    private static final Integer MINFOLDERS = 1;
    private FolderBehaviour fBehaviour = new CreateNull();

    private String filePath;

    public String getFilePath() {
        return this.filePath;
    }

    public void setFBehaviour(FolderBehaviour newBehaviour) {
        this.fBehaviour = newBehaviour;
    }

    public FolderBehaviour getFBehaviour() {
        return this.fBehaviour;
    }

    public void setFilePath() {
        String userInput = "";
        boolean confirmPath = false;
        String tempFilePath = "";
        while (this.filePath == null || !confirmPath) {
            if (tempFilePath == null) {
                tempFilePath = Asker.askFilePath(Asker.FILEPATHQ);
                if (tempFilePath == "") tempFilePath = PathFinder.getDefault();
            }
            else {
                if (tempFilePath != null && !tempFilePath.equals("") 
                     && this.filePath == null) {
                    this.filePath = tempFilePath;
                }
                else if (this.filePath == null) this.filePath = PathFinder.getDefault();

                tempFilePath = this.filePath;
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

    Integer setFolderTotal() {
        return Asker.askInteger(Asker.FOLDERTOTALQ, MINFOLDERS, Integer.MAX_VALUE);
    }

    public void create() {
        fBehaviour.doCreate(this.filePath);
    }

    /**
     * Create a new folder. Also, say when this folder has been successfully created or not.
     * @param newFolder the folder to be created
     * @param desiredPath where the folder is created
     */
    public final static void createFolder(String newFolder, String desiredPath) {
        if (new File(desiredPath + String.format("\\%s", newFolder)).mkdirs()) {            
            System.out.println(String.format("Created folder \"%s\" in %s.", newFolder, desiredPath));
        }
        else System.out.println(String.format("Failed to create folder \"%s\". Folder already exists.", newFolder));
    }

}
