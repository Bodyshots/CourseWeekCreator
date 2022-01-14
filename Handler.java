public class Handler {
    
    public static Boolean handleFolderConf(String folderLoc, Integer weekNum) {
        String yesNoMsg = String.format("Creating %d folders at %s.\nContinue? [Y/N]", weekNum, folderLoc);
        System.out.println(yesNoMsg);
        String userInput = Prompts.yesNo(yesNoMsg);
        if (userInput.equals(Prompts.YES)) return true;
        return false;
    }

}
