package utils;

import models.TextRange;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.awt.SystemColor.text;

/**
 * Created by Arsen on 15.06.2016.
 */
public class StringTools {

    public static final String PATTERN_ATTRIBUTE = ".*\\$\\{([_a-zA-Z0-9]*)\\}.*";
    public static final String EMPTY_NAME = "EMPTY_NAME";

    public static String replaceGlobalVariables(String text, Map<String, String> mapGlobals) {
        Pattern pattern = Pattern.compile(PATTERN_ATTRIBUTE);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String var = mapGlobals.get(matcher.group(1));
            if (var == null) {
                // TODO: 20.06.2016 print error, var isn't global
                var = "";
            }
            text = text.replace(String.format("${%s}", matcher.group(1)), var);
            matcher = pattern.matcher(text);
        }
        if (text.isEmpty()) {
            return EMPTY_NAME;
        }
        return text;
    }

    public static final String VAR_PREFIX = "${";
    public static final String VAR_POSTFIX = "}";

    public static ArrayList<TextRange> findVariable(String text) {
        Pattern pattern = Pattern.compile(PATTERN_ATTRIBUTE);
        Matcher matcher = pattern.matcher(text);
        ArrayList<TextRange> result = new ArrayList<>();

        while (matcher.find()) {
            result.add(new TextRange(matcher.start(1)- VAR_PREFIX.length(), matcher.end(1)+ VAR_POSTFIX.length()));
            text = text.replace(String.format("%s%s%s", VAR_PREFIX, matcher.group(1), VAR_POSTFIX), "a");
            matcher = pattern.matcher(text);
        }

        return result;
    }

    public static String formatConst(String text) {
        return text.replace("_", " ");
    }

    public static boolean containsVariable(String text) {
        return text.matches(PATTERN_ATTRIBUTE);
    }

//    \d    A digit: [0-9]
//    \D    A non-digit: [^0-9]
//    \h    A horizontal whitespace character: [ \t\xA0\u1680\u180e\u2000-\u200a\u202f\u205f\u3000]
//    \H    A non-horizontal whitespace character: [^\h]
//    \s    A whitespace character: [ \t\n\x0B\f\r]
//    \S    A non-whitespace character: [^\s]
//    \v    A vertical whitespace character: [\n\x0B\f\r\x85\u2028\u2029]
//    \V    A non-vertical whitespace character: [^\v]
//    \w    A word character: [a-zA-Z_0-9]
//    \W    A non-word character: [^\w]
    public static boolean isNameValid(String text) {
        if( text.matches(".*[^0-9a-zA-Z_}{\\$].*")){
            // TODO: 21.06.2016 print illegal symbols
            return false;
        }
        if( text.substring(0,1).matches("\\d")){
            // TODO: 21.06.2016 print starts with digit
            return false;
        }
        return true;
    }
}
