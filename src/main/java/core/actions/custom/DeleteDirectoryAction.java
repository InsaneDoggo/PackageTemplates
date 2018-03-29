package core.actions.custom;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.util.IncorrectOperationException;
import core.actions.custom.base.SimpleAction;
import core.report.ReportHelper;
import core.report.enums.ExecutionState;
import global.utils.Logger;
import global.utils.file.VFSHelper;

import java.io.File;

/**
 * Created by Arsen on 09.01.2017.
 */
public class DeleteDirectoryAction extends SimpleAction {

    private File fileDirToDelete;
    private Project project;

    public DeleteDirectoryAction(File fileDirToDelete, Project project) {
        this.fileDirToDelete = fileDirToDelete;
        this.project = project;
    }

    @Override
    public void doRun() {
        PsiDirectory psiDirectory = VFSHelper.findPsiDirByPath(project, fileDirToDelete.getPath());
        if (psiDirectory == null) {
            ReportHelper.setState(ExecutionState.FAILED);
            return;
        }

        try {
            psiDirectory.delete();
        } catch (IncorrectOperationException ex) {
            Logger.log("DeleteDirectoryAction " + ex.getMessage());
            Logger.printStack(ex);
            ReportHelper.setState(ExecutionState.FAILED);
            return;
        }
    }

}
