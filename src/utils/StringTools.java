package utils;

import models.PackageTemplate;
import models.TemplateElement;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.awt.SystemColor.text;

/**
 * Created by Arsen on 15.06.2016.
 */
public class StringTools {

    public static final String PACKAGE_TEMPLATE_NAME = "${PACKAGE_TEMPLATE_NAME}";
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
        if( text.isEmpty() ){
            return EMPTY_NAME;
        }
        return text;
    }

    public static String formatConst(String text) {
        return text.replace("_", " ");
    }

}
