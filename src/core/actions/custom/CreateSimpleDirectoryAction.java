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
public class CreateSimpleDirectoryAction extends SimpleAction {

    private Project project;
    private File directory;

    PsiDirectory psiDirectoryCreated;

    public CreateSimpleDirectoryAction(Project project, File directory) {
        this.project = project;
        this.directory = directory;
    }

    @Override
    public void doRun() {
        PsiDirectory psiParent = VFSHelper.findPsiDirByPath(project, directory.getParentFile().getPath());
        if (psiParent == null) {
            ReportHelper.setState(ExecutionState.FAILED);
            return;
        }

        try {
            // check existence
            psiDirectoryCreated = psiParent.findSubdirectory(directory.getName());
            if (psiDirectoryCreated == null) {
                // create new one
                psiDirectoryCreated = psiParent.createSubdirectory(directory.getName());
            }
        } catch (IncorrectOperationException ex) {
            Logger.log("CreateSimpleDirectoryAction " + ex.getMessage());
            Logger.printStack(ex);
            ReportHelper.setState(ExecutionState.FAILED);
            return;
        }
    }

}
