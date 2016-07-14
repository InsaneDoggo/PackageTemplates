package utils;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.EditorTextField;
import models.PackageTemplate;
import models.TemplateElement;
import reborn.models.BaseElement;
import reborn.wrappers.ElementWrapper;
import reborn.wrappers.PackageTemplateWrapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.intellij.ide.fileTemplates.FileTemplateManager.DEFAULT_TEMPLATES_CATEGORY;

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

        List<String> listAllTemplates = new ArrayList<>();

        for (FileTemplate template : FileTemplateManager.getDefaultInstance().getTemplates(DEFAULT_TEMPLATES_CATEGORY)) {
            //if( template.isDefault() ){
            listAllTemplates.add(template.getName());
            //}
        }

        for (ElementWrapper element : ptWrapper.getRootElement().getListElementWrapper()) {
            ValidationInfo validationInfo = element.isNameValid(listAllTemplates);
            if (validationInfo != null) {
                return validationInfo;
            }
        }
        return null;
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

    private static final String PATTERN_CLASS_NAME_VALIDATION = ".*[^0-9a-zA-Z_].*";
    private static final String PATTERN_PLAIN_TEXT_VALIDATION = ".*[^0-9a-zA-Z_=\\-+.)(].*";
    private static final String PATTERN_GLOBAL_VARIABLE_VALIDATION = ".*[^0-9a-zA-Z_}{\\$].*";

    private static final String ILLEGAL_SYMBOLS = "Field contains illegal symbols";
    private static final String STARTS_WITH_DIGIT = "Name can't starts with digit";
    private static final String EMPTY_FIELDS = "Fill empty fields";

    public enum FieldType {
        GLOBAL_VARIABLE,
        CLASS_NAME,
        DIRECTORY_NAME,
        PACKAGE_TEMPLATE_NAME,
        PLAIN_TEXT
    }

    public static ValidationInfo validateTextField(EditorTextField etField, FieldType fieldType) {
        if (etField.getText().trim().isEmpty()) {
            return new ValidationInfo(EMPTY_FIELDS, etField);
        }

        switch (fieldType) {
            case CLASS_NAME:
            case DIRECTORY_NAME:
            case PACKAGE_TEMPLATE_NAME:
                if (!isNameValid(etField.getText(), PATTERN_CLASS_NAME_VALIDATION)) {
                    return new ValidationInfo(ILLEGAL_SYMBOLS, etField);
                }
                // Starts with digit
                if (startsWithDigit(etField.getText())) {
                    return new ValidationInfo(STARTS_WITH_DIGIT, etField);
                }
                break;
            case GLOBAL_VARIABLE:
                if (!isNameValid(etField.getText(), PATTERN_GLOBAL_VARIABLE_VALIDATION)) {
                    return new ValidationInfo(ILLEGAL_SYMBOLS, etField);
                }
                // Starts with digit
                if (startsWithDigit(etField.getText())) {
                    return new ValidationInfo(STARTS_WITH_DIGIT, etField);
                }
                break;
            case PLAIN_TEXT:
                if (!isNameValid(etField.getText(), PATTERN_PLAIN_TEXT_VALIDATION)) {
                    return new ValidationInfo(ILLEGAL_SYMBOLS, etField);
                }
                break;
        }

        return null;
    }


    private static boolean isNameValid(String text, String pattern) {
        if (text.matches(pattern)) {
            return false;
        }
        return true;
    }

    private static boolean startsWithDigit(String text) {
        if (text.substring(0, 1).matches("\\d")) {
            return true;
        }
        return false;
    }

}
