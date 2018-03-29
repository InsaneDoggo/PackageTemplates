package global.utils.text;

import com.intellij.openapi.util.Pair;
import core.regexp.NonRegexResult;
import global.Const;
import global.wrappers.PackageTemplateWrapper;

import java.util.Map;
import java.util.regex.MatchResult;
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
        return "(?s).*" + text + ".*";
    }

    public static String getNameWithoutExtension(String name) {
        Pair<String, String> nameExt = decodeFileName(name);
        String templateName = nameExt.first;
        String extension = nameExt.second;

        return templateName;
        //return name.replaceFirst("[.][^.]+$", "");
    }

    public static String getExtensionFromName(String name) {
        Pair<String, String> nameExt = decodeFileName(name);
        String templateName = nameExt.first;
        String extension = nameExt.second;

        return extension;
        //return name.replaceFirst("^.*[.]", "");
    }


    //=================================================================
    //  Extension
    //=================================================================
    private static String encodeFileName(String templateName, String extension) {
        String nameExtDelimiter = extension.contains(".") ? Const.ENCODED_NAME_EXT_DELIMITER : ".";
        return templateName + nameExtDelimiter + extension;
    }

    private static Pair<String, String> decodeFileName(String fileName) {
        String name = fileName;
        String ext = "";
        String nameExtDelimiter = fileName.contains(Const.ENCODED_NAME_EXT_DELIMITER) ? Const.ENCODED_NAME_EXT_DELIMITER : ".";
        int extIndex = fileName.lastIndexOf(nameExtDelimiter);
        if (extIndex >= 0) {
            name = fileName.substring(0, extIndex);
            ext = fileName.substring(extIndex + nameExtDelimiter.length());
        }
        return Pair.create(name, ext);
    }


    //=================================================================
    //  Search
    //=================================================================
    public static int matchCount(String text, String textToSearch) {
        int indexFrom = 0;
        int count = 0;

        while (indexFrom != -1) {
            indexFrom = text.indexOf(textToSearch, indexFrom);

            if (indexFrom != -1) {
                count++;
                indexFrom += textToSearch.length();
            }
        }

        return count;
    }

    public static MatchResult match(String text, String token) {
        int start = text.indexOf(token);
        if (start == -1) {
            return null;
        }

        NonRegexResult result = new NonRegexResult();
        result.setStart(start);
        result.setEnd(start + token.length());
        result.setGroup(token);

        return result;
    }

}
