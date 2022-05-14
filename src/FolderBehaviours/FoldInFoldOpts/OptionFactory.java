package src.FolderBehaviours.FoldInFoldOpts;

import src.Prompts;

public class OptionFactory {
    public AbsOpt createBehaviour(String userInput) {
        if (userInput.equals(Prompts.OPTION_A)) return new OptionA();
        else if (userInput.equals(Prompts.OPTION_B)) return new OptionB(); // "In a specific folder"
        else if (userInput.equals(Prompts.OPTION_C)) return new OptionC();
        else return new OptionOther();
    }
}
