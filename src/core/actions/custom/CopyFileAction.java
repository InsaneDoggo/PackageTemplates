package core.actions.custom;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import core.actions.custom.base.SimpleAction;
import global.utils.Logger;
import global.utils.file.PsiHelper;

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
    public boolean run(SimpleAction parentAction) {
        PsiDirectory psiParent = PsiHelper.getPsiDirByPath(project, fileTo.getParentFile().getPath());
        PsiFile psiFrom = PsiHelper.getPsiFileByPath(project, fileFrom.getPath());
        if (psiParent == null || psiFrom == null) {
            isDone = false;
            return false;
        }

        try {
            psiParent.copyFileFrom(fileTo.getName(), psiFrom);
        } catch (IncorrectOperationException ex) {
            Logger.log("CopyFileAction " + ex.getMessage());
            isDone = false;
            return false;
        }

//        if(!FileWriter.copyFile(fileFrom.toPath(), fileTo.toPath())){
//            isDone = false;
//            return false;
//        }

        return super.run(this);
    }

}
