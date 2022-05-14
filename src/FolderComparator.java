package src;
import java.util.Comparator;

public class FolderComparator implements Comparator<String>{

    @Override
    public int compare(String o1, String o2) {
        String [] o1Split = o1.split(" ");
        String [] o2Split = o2.split(" ");
        String o1Last = o1Split[o1Split.length - 1];
        String o2Last = o2Split[o2Split.length - 1];
        String intRegex = "^-?[0-9]+$"; 
        // ? => (-) optional, [0-9]+ => checks # of digits >=1
        // ^ => start of str, $ => end of str
        
        // compares last substring only. if they're both valid #s:
        if (o1Last.matches(intRegex) && o2Last.matches(intRegex)) {
            try {
                Integer o1Num = Integer.parseInt(o1Last);
                Integer o2Num = Integer.parseInt(o2Last);
                if (o1Num > o2Num) return 1; // swap o1 and o2
                return -1; // don't swap o1 and o2
            }
            catch (NumberFormatException e) {
                return -1;
            }
        }
        // if one of them are not valid #s:
        // doesn't account for strs like -44fndkl32 vs. 49123anf
        if (o1Last.length() > o2Last.length()) return 1;
        if (o1Last.length() < o2Last.length()) return -1;
        for (int i = 0; i < o1Last.length(); i++) {
            if (o1Last.charAt(i) > o2Last.charAt(i)) return 1;
            if (o1Last.charAt(i) < o2Last.charAt(i)) return -1;
        }
        return -1; // if they're the exact same
    }
    
}
