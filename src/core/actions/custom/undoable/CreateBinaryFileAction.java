package core.actions.custom.undoable;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import core.actions.custom.element.CreateElementBaseAction;
import core.report.ReportHelper;
import core.report.models.FailedActionReport;
import global.Const;
import global.models.BinaryFile;
import global.utils.Logger;
import global.utils.file.VFSHelper;

public class CreateBinaryFileAction extends CreateElementBaseAction<BinaryFile, java.io.File> {

    public CreateBinaryFileAction(BinaryFile file, Project project) {
        super(project, file);
    }

    @Override
    protected void createElement(String path) {
        //fileResult =
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
    protected java.io.File findExistingResultFile(java.io.File fileDuplicate) {
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
        return String.format("Create BinaryFile:    %s",
                element.getName()
        );
    }

}
