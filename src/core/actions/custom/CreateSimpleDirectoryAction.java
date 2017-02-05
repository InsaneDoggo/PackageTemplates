package core.actions.custom;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.util.IncorrectOperationException;
import core.actions.custom.base.SimpleAction;
import global.utils.Logger;
import global.utils.file.PsiHelper;

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
    public boolean run(SimpleAction parentAction) {
        PsiDirectory psiParent = PsiHelper.getPsiDirByPath(project, directory.getParentFile().getPath());
        if (psiParent == null) {
            isDone = false;
            return false;
        }

        try {
            // check existence
            psiDirectoryCreated = psiParent.findSubdirectory(directory.getName());
            if(psiDirectoryCreated == null){
                // create new one
                psiDirectoryCreated = psiParent.createSubdirectory(directory.getName());
            }
        } catch (IncorrectOperationException ex) {
            Logger.log("CreateSimpleDirectoryAction " + ex.getMessage());
            isDone = false;
            return false;
        }

        return super.run(this);
    }

}
