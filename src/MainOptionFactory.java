package src;

import src.FolderBehaviours.CreateFolderInFolder;
import src.FolderBehaviours.CreateMass;
import src.FolderBehaviours.CreateNull;
import src.FolderBehaviours.CreateWeeks;
import src.FolderBehaviours.FolderBehaviour;

public class MainOptionFactory {
    public FolderBehaviour createBehaviour(String userInput) {
        if (userInput.equals(Prompts.OPTION_A)) return new CreateMass();
        else if (userInput.equals(Prompts.OPTION_B)) return new CreateWeeks();
        else if (userInput.equals(Prompts.OPTION_D)) return new CreateFolderInFolder();
        else return new CreateNull();
    }
}
