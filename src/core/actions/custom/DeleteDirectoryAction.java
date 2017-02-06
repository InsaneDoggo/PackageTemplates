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
public class DeleteDirectoryAction extends SimpleAction {

    private File fileDirToDelete;
    private Project project;

    public
    DeleteDirectoryAction(File fileDirToDelete, Project project) {
        this.fileDirToDelete = fileDirToDelete;
        this.project = project;
    }

    @Override
    public boolean run() {
        PsiDirectory psiDirectory = PsiHelper.findPsiDirByPath(project, fileDirToDelete.getPath());
        if(psiDirectory==null){
            isDone = false;
            return false;
        }

        try {
            psiDirectory.delete();
        } catch (IncorrectOperationException ex){
            Logger.log("DeleteDirectoryAction " + ex.getMessage());
            Logger.printStack(ex);
            isDone = false;
            return false;
        }

        return super.run();
    }

}
