package global.utils.validation;

import com.intellij.openapi.ui.ValidationInfo;
import global.utils.i18n.Localizer;
import global.wrappers.GlobalVariableWrapper;
import global.wrappers.PackageTemplateWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by CeH9 on 14.06.2016.
 */
public class TemplateValidator {

    /**
     * Check existence FileTemplates used in PackageTemplate
     */
    public static ValidationInfo isTemplateValid(PackageTemplateWrapper ptWrapper) {
        if (ptWrapper.getRootElement().getListElementWrapper() == null) {
            // when template is empty folder(useless, but valid)
            return null;
        }

        //todo validate?

        return null;
    }


    //=================================================================
    //  Text
    //=================================================================
    private static final String PATTERN_CLASS_NAME_VALIDATION = ".*[^0-9a-zA-Z_].*";
    private static final String PATTERN_PLAIN_TEXT_VALIDATION = ".*[^0-9a-zA-Z_=\\-+.)(].*";
    private static final String PATTERN_GLOBAL_VARIABLE_VALIDATION = ".*[^0-9a-zA-Z_}{\\$].*";

    private static final String ILLEGAL_SYMBOLS = Localizer.get("warning.FieldContainsIllegalSymbols");
    private static final String STARTS_WITH_DIGIT = Localizer.get("warning.NameCantStartsWithDigit");
    private static final String EMPTY_FIELDS = Localizer.get("warning.FillEmptyFields");

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

    @Nullable
    public static ValidationInfo validateText(JComponent jComponent, String text, FieldType fieldType) {
        //todo uncomment

        switch (fieldType) {
            case PACKAGE_TEMPLATE_NAME:
                if (text.trim().isEmpty()) {
                    return new ValidationInfo(EMPTY_FIELDS, jComponent);
                }

                if (!isValidByPattern(text, PATTERN_CLASS_NAME_VALIDATION)) {
                    return new ValidationInfo(ILLEGAL_SYMBOLS, jComponent);
                }
                // Starts with digit
                if (startsWithDigit(text)) {
                    return new ValidationInfo(STARTS_WITH_DIGIT, jComponent);
                }

                break;
            case CLASS_NAME:
            case DIRECTORY_NAME:
            case SCRIPT:
            case GLOBAL_VARIABLE:
//                if (text.trim().isEmpty()) {
//                    return new ValidationInfo(EMPTY_FIELDS, jComponent);
//                }
//                if (!isValidByPattern(text, PATTERN_GLOBAL_VARIABLE_VALIDATION)) {
//                    return new ValidationInfo(ILLEGAL_SYMBOLS, jComponent);
//                }
//                // Starts with digit
//                if (startsWithDigit(text)) {
//                    return new ValidationInfo(STARTS_WITH_DIGIT, jComponent);
//                }
//                break;
            case PLAIN_TEXT:
//                if (text.trim().isEmpty()) {
//                    return new ValidationInfo(EMPTY_FIELDS, jComponent);
//                }
//                if (!isValidByPattern(text, PATTERN_PLAIN_TEXT_VALIDATION)) {
//                    return new ValidationInfo(ILLEGAL_SYMBOLS, jComponent);
//                }
                break;
        }

        return null;
    }

    private static boolean isValidByPattern(String text, String pattern) {
        return !text.matches(pattern);
    }

    private static boolean startsWithDigit(String text) {
        return text.substring(0, 1).matches("\\d");
    }

    public static boolean isValidClassName(String name) {
        return isValidByPattern(name, PATTERN_CLASS_NAME_VALIDATION) && !TemplateValidator.startsWithDigit(name);
    }


    //=================================================================
    //  PackageTemplate
    //=================================================================
    @Nullable
    public static ValidationInfo validateProperties(PackageTemplateWrapper ptWrapper) {
        ValidationInfo result;
        if (ptWrapper.getMode() != PackageTemplateWrapper.ViewMode.USAGE) {
            result = TemplateValidator.validateText(ptWrapper.jtfName, ptWrapper.jtfName.getText(), FieldType.PACKAGE_TEMPLATE_NAME);
            if (result != null) return result;
            result = TemplateValidator.validateText(ptWrapper.jtaDescription, ptWrapper.jtaDescription.getText(), FieldType.PLAIN_TEXT);
            if (result != null) return result;
        }
        return null;
    }

    @Nullable
    public static ValidationInfo validateGlobalVariables(PackageTemplateWrapper ptWrapper) {
        ValidationInfo result;
        for (GlobalVariableWrapper gvWrapper : ptWrapper.getListGlobalVariableWrapper()) {
            result = TemplateValidator.validateText(gvWrapper.getTfValue(), gvWrapper.getTfValue().getText(), FieldType.GLOBAL_VARIABLE);
            if (result != null) return result;
        }
        return null;
    }

}
