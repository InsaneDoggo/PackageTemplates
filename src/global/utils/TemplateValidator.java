package global.utils;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import global.wrappers.ElementWrapper;
import global.wrappers.GlobalVariableWrapper;
import global.wrappers.PackageTemplateWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

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

        for (FileTemplate template : FileTemplateManager.getDefaultInstance().getTemplates(FileTemplateManager.DEFAULT_TEMPLATES_CATEGORY)) {
            listAllTemplates.add(template.getName());
        }
        for (FileTemplate template : FileTemplateManager.getDefaultInstance().getTemplates(FileTemplateManager.J2EE_TEMPLATES_CATEGORY)) {
            listAllTemplates.add(template.getName());
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

    private static final String ILLEGAL_SYMBOLS = Localizer.get("FieldContainsIllegalSymbols");
    private static final String STARTS_WITH_DIGIT = Localizer.get("NameCantStartsWithDigit");
    private static final String EMPTY_FIELDS = Localizer.get("FillEmptyFields");

    public enum FieldType {
        GLOBAL_VARIABLE,
        CLASS_NAME,
        DIRECTORY_NAME,
        PACKAGE_TEMPLATE_NAME,
        PLAIN_TEXT
    }

    @Nullable
    public static ValidationInfo validateText(JComponent jComponent, String text, FieldType fieldType) {
        //todo uncomment

//        if (text.trim().isEmpty()) {
//            return new ValidationInfo(EMPTY_FIELDS, jComponent);
//        }
//
//        switch (fieldType) {
//            case PACKAGE_TEMPLATE_NAME:
//                if (!isValidByPattern(text, PATTERN_CLASS_NAME_VALIDATION)) {
//                    return new ValidationInfo(ILLEGAL_SYMBOLS, jComponent);
//                }
//                // Starts with digit
//                if (startsWithDigit(text)) {
//                    return new ValidationInfo(STARTS_WITH_DIGIT, jComponent);
//                }
//                break;
//            case CLASS_NAME:
//            case DIRECTORY_NAME:
//            case GLOBAL_VARIABLE:
//                if (!isValidByPattern(text, PATTERN_GLOBAL_VARIABLE_VALIDATION)) {
//                    return new ValidationInfo(ILLEGAL_SYMBOLS, jComponent);
//                }
//                // Starts with digit
//                if (startsWithDigit(text)) {
//                    return new ValidationInfo(STARTS_WITH_DIGIT, jComponent);
//                }
//                break;
//            case PLAIN_TEXT:
//                if (!isValidByPattern(text, PATTERN_PLAIN_TEXT_VALIDATION)) {
//                    return new ValidationInfo(ILLEGAL_SYMBOLS, jComponent);
//                }
//                break;
//        }

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

    @Nullable
    public static ValidationInfo validateProperties(PackageTemplateWrapper ptWrapper) {
        ValidationInfo result;
        if (ptWrapper.getMode() != PackageTemplateWrapper.ViewMode.USAGE) {
            result = TemplateValidator.validateText(ptWrapper.etfName, ptWrapper.etfName.getText(), TemplateValidator.FieldType.PACKAGE_TEMPLATE_NAME);
            if(result != null) return result;
            result = TemplateValidator.validateText(ptWrapper.etfDescription, ptWrapper.etfDescription.getText(), TemplateValidator.FieldType.PLAIN_TEXT);
            if(result != null) return result;
        }
        return null;
    }

    @Nullable
    public static ValidationInfo validateGlobalVariables(PackageTemplateWrapper ptWrapper) {
        ValidationInfo result;
        for (GlobalVariableWrapper gvWrapper : ptWrapper.getListGlobalVariableWrapper()) {
            result = TemplateValidator.validateText(gvWrapper.getTfValue(), gvWrapper.getTfValue().getText(), TemplateValidator.FieldType.GLOBAL_VARIABLE);
            if(result != null) return result;
        }
        return null;
    }

    public static ValidationInfo checkExisting(PackageTemplateWrapper ptWrapper, VirtualFile virtualFile, Project project) {
        PsiDirectory currentDir = FileWriter.findCurrentDirectory(project, virtualFile);
        if (currentDir != null) {
            VirtualFile existingFile = currentDir.getVirtualFile().findChild(ptWrapper.getRootElement().getDirectory().getName());
            if (existingFile != null) {
                return new ValidationInfo(String.format(
                        Localizer.get("DirectoryAlreadyExist"),
                        ptWrapper.getRootElement().getDirectory().getName()
                ));
            }
        }
        return null;
    }

}
