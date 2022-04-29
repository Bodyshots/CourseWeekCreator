import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

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

    public static final List<String> listFolders(String filePath) {
        String [] files = FolderChecker.listFolderFiles(filePath);
        List<String> folders = new ArrayList<>();
        for (String file: files) {
            if (FolderChecker.isFolder(String.format("%s\\%s", filePath, file))) {
                folders.add(file);
            }
        }
        return folders;
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

    public static final List<List<String>> listFolderCats(String filePath) {
        String [] files = FolderChecker.listFolderFiles(filePath);
        List<List<String>> cats = new ArrayList<List<String>>();
        int fileIndex = 0;

        while (fileIndex < files.length) {
            if (!FolderChecker.isFolder(files[fileIndex])) {
                fileIndex ++;
            }
            else {
                List<String> cat = new ArrayList<>();
                String [] folder = files[fileIndex].split(" ");

                int existingCatIndex = -1;
                int catIndex = 0;
                while (existingCatIndex == -1 && catIndex < cats.size()) { // goes over existing cats
                    if (cats.get(catIndex).get(0).startsWith(folder[0])) {
                        existingCatIndex = catIndex;
                    }
                    catIndex ++;
                }
                if (existingCatIndex != -1) cat = cats.get(existingCatIndex); // .add(files[fileIndex]);
    
                while (fileIndex < files.length && files[fileIndex].startsWith(folder[0])) {
                    cat.add(files[fileIndex]); // add files to existing category
                    fileIndex ++; // stops on different category
                }
                if (existingCatIndex == -1) cats.add(cat); // adds cat, if hasn't existed yet
            }
        }
        return cats;
    }

    public static final void sortFolders(List<String> folders) {
        Comparator<String> compare = new FolderComparator();
        Collections.sort(folders, compare);
    }
}
