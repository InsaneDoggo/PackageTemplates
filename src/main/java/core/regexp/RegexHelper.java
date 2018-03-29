package core.regexp;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHelper {

    public static int matchCount(String text, String pattern) {
        Matcher matcher = Pattern.compile(pattern).matcher(text);

        int count = 0;
        while (matcher.find()) {
            count++;
        }

        return count;
    }

    public static MatchResult match(String text, String pattern) {
        Matcher matcher = Pattern.compile(pattern).matcher(text);

        if (matcher.find()) {
            return matcher.toMatchResult();
        }
        return null;
    }

}
