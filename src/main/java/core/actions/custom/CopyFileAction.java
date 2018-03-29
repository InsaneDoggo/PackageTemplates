package core.actions.custom;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import core.actions.custom.base.SimpleAction;
import core.report.ReportHelper;
import core.report.enums.ExecutionState;
import global.utils.Logger;
import global.utils.file.PsiHelper;
import global.utils.i18n.Localizer;

import java.io.File;

/**
 * Created by Arsen on 09.01.2017.
 */
public class CopyFileAction extends SimpleAction {

    private File fileFrom;
    private File fileTo;
    private Project project;

    public CopyFileAction(Project project, File fileFrom, File fileTo) {
        this.fileFrom = fileFrom;
        this.fileTo = fileTo;
        this.project = project;
    }

    @Override
    public void doRun() {
        PsiDirectory psiParent = PsiHelper.findPsiDirByPath(project, fileTo.getParentFile().getPath());
        PsiFile psiFrom = PsiHelper.findPsiFileByPath(project, fileFrom.getPath());
        if (psiParent == null || psiFrom == null) {
            ReportHelper.setState(ExecutionState.FAILED);
            return;
        }

        PsiFile psiDuplicate = psiParent.findFile(fileTo.getName());
        if (psiDuplicate != null) {
            Logger.log("Delete psiDuplicate " + psiDuplicate.getName());
            psiDuplicate.delete();
        }

        try {
            psiParent.copyFileFrom(fileTo.getName(), psiFrom);
        } catch (Exception ex) {
            Logger.log("CopyFileAction " + ex.getMessage());
            Logger.printStack(ex);
            ReportHelper.setState(ExecutionState.FAILED);
            return;
        }
    }


    //=================================================================
    //  Utils
    //=================================================================
    @Override
    public String toString() {
        return String.format("%s: from %s to %s",
                getClass().getSimpleName(),
                fileFrom.getName(),
                fileTo.getName()
        );
    }

}
