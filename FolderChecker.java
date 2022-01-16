import java.io.File;

public class FolderChecker {
    public static final boolean isValidPath(String file) {
        return (new File(file).exists());
    }

    public static final String [] listFolderFiles(String filePath) {
        return new File(filePath).list();
    }

    public static final boolean folderEmpty(String filePath) {
        return FolderChecker.listFolderFiles(filePath).length == 0;
    }

    public static final boolean isFolder(String filePath) {
        return new File(filePath).isDirectory();
    }

    public static final boolean isPopFolder(String filePath) {
        if (!FolderChecker.isFolder(filePath)) {
            System.out.println("This file isn\'t a folder!");
            return false;
        }
        else if (FolderChecker.folderEmpty(filePath)) {
            System.out.println("This folder is empty!");
            return false;
        }
        return true;
    }

}
