package core.actions.custom;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import core.actions.custom.base.SimpleAction;
import core.actions.custom.element.CreateElementBaseAction;
import core.actions.custom.interfaces.IHasPsiDirectory;
import core.actions.custom.interfaces.IHasWriteRules;
import core.report.ReportHelper;
import core.report.enums.ExecutionState;
import core.report.models.FailedActionReport;
import core.report.models.SuccessActionReport;
import core.search.SearchAction;
import core.search.SearchEngine;
import core.writeRules.WriteRules;
import global.Const;
import global.dialogs.impl.NeverShowAskCheckBox;
import global.models.BaseElement;
import global.models.File;
import global.utils.Logger;
import global.utils.file.FileWriter;
import global.utils.file.VFSHelper;
import global.utils.i18n.Localizer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Arsen on 09.01.2017.
 */
public class CreateFileFromTemplateAction extends CreateElementBaseAction<File, PsiElement> {

    private Properties properties;
    private FileTemplate template;

    public CreateFileFromTemplateAction(Properties properties, FileTemplate template, File file, Project project) {
        super(project, file);
        this.properties = properties;
        this.template = template;
    }

    @Override
    protected void createElement(String path) {
        fileResult = FileWriter.createFileFromTemplate(this, project, template, element.getName(), properties, path);
    }

    @Override
    protected java.io.File getDuplicateFile(String path) {
        return new java.io.File(path + java.io.File.separator
                + element.getName() + Const.FILE_EXTENSION_SEPARATOR + element.getExtension());
    }

    @Override
    protected String elementToString() {
        return String.format("%s.%s",
                element.getName(),
                element.getExtension()
        );
    }

    @Override
    protected PsiElement findExistingResultFile(java.io.File fileDuplicate) {
        return VFSHelper.findPsiFileByPath(project, fileDuplicate.getPath());
    }

    @Override
    protected boolean removeExistingElement(java.io.File fileDuplicate) {
        PsiFile psiDuplicate = (PsiFile) findExistingResultFile(fileDuplicate);
        if (psiDuplicate == null) {
            return false;
        }

        try {
            psiDuplicate.delete();
            return true;
        } catch (IncorrectOperationException e) {
            Logger.logAndPrintStack("CreateFileFromTemplateAction " + e.getMessage(), e);
            ReportHelper.putReport(new FailedActionReport(this, e.getMessage()));
            return false;
        }
    }


    //=================================================================
    //  Utils
    //=================================================================
    @Override
    public String toString() {
        return String.format("Create from FileTemplate:    %s.%s",
                element.getName(),
                element.getExtension()
        );
    }

}
