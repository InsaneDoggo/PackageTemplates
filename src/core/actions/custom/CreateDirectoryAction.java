package core.actions.custom;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import core.actions.custom.base.SimpleAction;
import core.actions.custom.interfaces.IHasPsiDirectory;
import core.search.SearchAction;
import core.search.SearchEngine;
import global.models.BaseElement;
import global.models.Directory;
import global.utils.Logger;
import global.utils.file.PsiHelper;
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
            }
        }

        if(psiDirectoryCreated == null){
            isDone = false;
            return false;
        }

        return super.run(this);
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

        return PsiHelper.getPsiDirByPath(project, searchResultFile.getPath());
    }

}
