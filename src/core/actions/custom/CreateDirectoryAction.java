package core.actions.custom;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import global.utils.file.FileWriter;

import java.io.File;

/**
 * Created by Arsen on 09.01.2017.
 */
public class CreateDirectoryAction extends SimpleAction {

    private File fileDir;
    private Project project;

    //result
    private PsiDirectory psiResultDirectory;

    public CreateDirectoryAction(File fileDir, Project project) {
        this.fileDir = fileDir;
        this.project = project;
    }

    @Override
    public boolean run() {
        psiResultDirectory = FileWriter.createDirectory(project, fileDir);
        isDone = psiResultDirectory != null;
        return isDone;
    }

    @Override
    public boolean undo() {
        isDone = !FileWriter.removeDirectory(fileDir);
        return !isDone;
    }
}
