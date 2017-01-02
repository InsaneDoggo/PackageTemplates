package global.utils;

import global.wrappers.PackageTemplateWrapper;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Arsen on 15.06.2016.
 */
public class StringTools {

    public static final String EMPTY_NAME = "UNNAMED";
    public static final String UNKNOWN_GLOBAL = "UNKNOWN_GLOBAL";

    public static final String PATTERN_ATTRIBUTE = "\\$\\{([_a-zA-Z0-9]*)\\}";
    public static final String PATTERN_BASE_NAME = "\\$\\{(" + PackageTemplateWrapper.ATTRIBUTE_BASE_NAME + ")\\}";

    public static String replaceGlobalVariables(String text, Map<String, String> mapGlobals) {
        Pattern pattern = Pattern.compile(PATTERN_ATTRIBUTE);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String var = mapGlobals.get(matcher.group(1));
            if (var == null) {
                // TODO: 20.06.2016 print error, var isn't global
                var = UNKNOWN_GLOBAL;
            }
            text = text.replace(String.format("${%s}", matcher.group(1)), var);
            matcher = pattern.matcher(text);
        }
        if (text.isEmpty()) {
            return EMPTY_NAME;
        }
        return text;
    }

    public static boolean containsVariable(String text) {
        return text.matches(greedy(PATTERN_ATTRIBUTE));
    }

    private static String greedy(String text) {
        return ".*" +  text + ".*";
    }

}
