package core.actions.custom;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiDirectory;
import core.actions.custom.element.CreateElementBaseAction;
import core.actions.custom.interfaces.IHasPsiDirectory;
import core.report.ReportHelper;
import core.report.models.FailedActionReport;
import global.models.Directory;
import global.utils.Logger;

import java.io.File;

/**
 * Created by Arsen on 09.01.2017.
 */
public class CreateDirectoryAction extends CreateElementBaseAction<Directory, PsiDirectory> implements IHasPsiDirectory {

    public CreateDirectoryAction(Directory directory, Project project) {
        super(project, directory);
    }

    @Override
    protected void createElement(String path) {
        ApplicationManager.getApplication().invokeAndWait(() -> {
            Runnable runnable = () ->
                    fileResult = ApplicationManager.getApplication().runWriteAction(
                            (Computable<PsiDirectory>) () -> psiParent.createSubdirectory(element.getName())
                    );
            CommandProcessor.getInstance().executeCommand(project, runnable, "testId", "testId");
        }, ModalityState.defaultModalityState());
    }

    @Override
    protected File getDuplicateFile(String path) {
        return new java.io.File(path + java.io.File.separator + element.getName());
    }

    @Override
    protected String elementToString() {
        return element.getClass().getSimpleName();
    }

    @Override
    protected PsiDirectory findExistingResultFile(File fileDuplicate) {
        return psiParent.findSubdirectory(element.getName());
    }

    @Override
    protected boolean removeExistingElement(File fileDuplicate) {
        PsiDirectory psiDuplicate = psiParent.findSubdirectory(element.getName());
        if (psiDuplicate == null) {
            return false;
        }

        try {
            psiDuplicate.delete();
            return true;
        } catch (Exception e) {
            Logger.logAndPrintStack("CreateDirectoryAction removeExistingElement: " + e.getMessage(), e);
            ReportHelper.putReport(new FailedActionReport(this, e.getMessage()));
            return false;
        }
    }


    //=================================================================
    //  Utils
    //=================================================================
    @Override
    public PsiDirectory getPsiDirectory() {
        return fileResult;
    }

    @Override
    public String toString() {
        return String.format("Create Directory:    %s", element.getName());
    }

}
