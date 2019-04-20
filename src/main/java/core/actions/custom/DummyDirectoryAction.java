package core.actions.custom;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import core.actions.custom.base.SimpleAction;
import core.actions.custom.interfaces.IHasPsiDirectory;

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
    public void doRun() {
        //nothing
    }

    @Override
    public PsiDirectory getPsiDirectory() {
        return psiDirectory;
    }
}
