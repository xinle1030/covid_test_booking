package fit3077.lab02team14.assignment2.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringFormatter {
    static Pattern WORD_FINDER = Pattern.compile("(([A-Z]?[a-z]+)|([A-Z]))");
    

    public static List<String> findWordsInMixedCase(String text) {
    Matcher matcher = WORD_FINDER.matcher(text);
    List<String> words = new ArrayList<>();
    while (matcher.find()) {
        words.add(matcher.group(0));
    }
    return words;
}

private static String capitalizeFirst(String word) {
    return word.substring(0, 1).toUpperCase()
      + word.substring(1).toLowerCase();
}

public static String sentenceCase(List<String> words) {
    List<String> capitalized = new ArrayList<>();
    for (int i = 0; i < words.size(); i++) {
        String currentWord = words.get(i);
        capitalized.add(capitalizeFirst(currentWord));
    }
    return String.join(" ", capitalized);
}

public static String formatString(String lowerCaseStr){
    List<String> words = findWordsInMixedCase(lowerCaseStr);
    return sentenceCase(words);
}

}

