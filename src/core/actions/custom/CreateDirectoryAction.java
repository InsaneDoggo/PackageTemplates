package core.actions.custom;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import core.actions.custom.base.SimpleAction;
import core.actions.custom.interfaces.IHasPsiDirectory;
import core.search.SearchAction;
import core.search.SearchEngine;
import global.models.BaseElement;
import global.models.Directory;
import global.models.File;
import global.utils.Logger;
import global.utils.file.FileWriter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

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

            // Custom Path
            if (directory.getCustomPath() != null) {
                psiParent = getPsiDirectoryFromCustomPath(directory, psiParent.getVirtualFile().getPath());

                if (psiParent == null) {
                    isDone = false;
                    return false;
                }
            }

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


    //=================================================================
    //  Utils
    //=================================================================
    @Nullable
    private PsiDirectory getPsiDirectoryFromCustomPath(BaseElement element, String pathFrom) {
        ArrayList<SearchAction> actions = element.getCustomPath().getListSearchAction();

        java.io.File searchResultFile = SearchEngine.find(new java.io.File(pathFrom), actions);

        if (searchResultFile == null) {
            //print name of last action
            Logger.log("getCustomPath File Not Found: " + (actions.isEmpty() ? "" : actions.get(actions.size() - 1).getName()));
            return null;
        }

        VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(searchResultFile.getPath());
        if (virtualFile == null) {
            Logger.log("getCustomPath virtualFile is NULL");
            return null;
        }

        PsiDirectory psiParentDir = PsiManager.getInstance(project).findDirectory(virtualFile);
        if (psiParentDir == null) {
            Logger.log("getCustomPath psiDirectory is NULL");
            return null;
        }

        return psiParentDir;
    }

}
