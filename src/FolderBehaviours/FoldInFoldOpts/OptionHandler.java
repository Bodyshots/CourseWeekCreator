package src.FolderBehaviours.FoldInFoldOpts;

public class OptionHandler {
    private AbsOpt optBeh = new OptionOther();

    public void setOptBeh(AbsOpt newBehaviour) {
        this.optBeh = newBehaviour;
    }

    public AbsOpt getOptBeh() {
        return this.optBeh;
    }

    public boolean handle(String folderNme, String filePath) {
        return this.optBeh.doOption(folderNme, filePath);
    }
}
