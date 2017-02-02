package core.actions.custom;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import core.actions.custom.base.SimpleAction;
import core.actions.custom.interfaces.IHasPsiDirectory;
import global.utils.file.FileWriter;

import java.io.File;

/**
 * Created by Arsen on 09.01.2017.
 */
public class DummyDirectoryAction extends SimpleAction implements IHasPsiDirectory {

    private Project project;
    private PsiDirectory psiDirectory;

    public DummyDirectoryAction(Project project, PsiDirectory psiDirectory) {
        this.project = project;
        this.psiDirectory = psiDirectory;
    }

    @Override
    public boolean run(SimpleAction parentAction) {
        isDone = true;
        isDone = super.run(this);
        return isDone;
    }

    @Override
    public boolean undo(SimpleAction parentAction) {
        isDone = false;
        if(!super.undo(this)){
            isDone =  true;
        }

        return !isDone;
    }

    @Override
    public PsiDirectory getPsiDirectory() {
        return psiDirectory;
    }
}
