public class Prompts {
    static final String YES = "Y";
    static final String NO = "N";
    static final String OPTION_A = "A";
    static final String OPTION_B = "B";
    static final String OPTION_C = "C";
    static final String OPTION_D = "D";
    static final String OPTION_E = "E";

    public static final String continuePrompt() {
        return String.format("Continue?\n" +
                             "[%s] - Yes\n" +
                             "[%s] - No", YES, NO);
    }

    public static final String folderOptionsPrompt() {
        return String.format("What do you want to do?\n" +
                             "[%s] - Create a new folder\n" +
                             "[%s] - Create \"Week\" folders\n" +
                             "[%s] - Reconfigure the main file path\n" +
                             "[%s] - Configure your year and course paths" +
                             "[%s] - Exit", OPTION_A, OPTION_B, OPTION_C, OPTION_D,
                                            OPTION_E);
    }

    public static final String createFolderOptionsPrompt() {
        return String.format("Where do you want to create a new folder?\n" +
                             "[%s] - In each week folder\n" +
                             "[%s] - In a specific week folder\n" +
                             "[%s] - In the course folder\n" +
                             "[%s] - Back", OPTION_A, OPTION_B, OPTION_C, OPTION_D);
    }
}
