package core.actions.custom;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import core.actions.custom.base.SimpleAction;
import core.actions.custom.interfaces.IHasPsiDirectory;
import global.models.Directory;
import global.utils.file.FileWriter;

/**
 * Created by Arsen on 09.01.2017.
 */
public class CreateDirectoryAction extends SimpleAction implements IHasPsiDirectory {

    private Directory directory;
    private Project project;

    //result
    private PsiDirectory psiDirectoryCreated;

    //other
    private boolean shouldSkipUndo = false;

    public CreateDirectoryAction(Directory directory, Project project) {
        this.directory = directory;
        this.project = project;
    }

    @Override
    public boolean run(SimpleAction parentAction) {
        psiDirectoryCreated = null;

        if(parentAction instanceof IHasPsiDirectory){
            PsiDirectory psiParent = ((IHasPsiDirectory) parentAction).getPsiDirectory();

            //todo check custom path

            // check existence
            psiDirectoryCreated = psiParent.findSubdirectory(directory.getName());
            if(psiDirectoryCreated == null){
                // create new one
                psiDirectoryCreated = psiParent.createSubdirectory(directory.getName());
            } else {
                shouldSkipUndo = true;
            }
        }

        if(psiDirectoryCreated == null){
            isDone = false;
            return false;
        }

        return super.run(this);
    }

    @Override
    public boolean undo(SimpleAction parentAction) {
        if(!super.undo(this)){ return false; }

        if(shouldSkipUndo){
            isDone = false;
            return true;
        }

        isDone = !FileWriter.removeDirectory(psiDirectoryCreated);
        return !isDone;
    }

    @Override
    public PsiDirectory getPsiDirectory() {
        return psiDirectoryCreated;
    }
}
