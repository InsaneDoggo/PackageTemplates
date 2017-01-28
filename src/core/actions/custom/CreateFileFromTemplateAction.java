package core.actions.custom;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import global.utils.file.FileWriter;

import java.util.Properties;

/**
 * Created by Arsen on 09.01.2017.
 */
public class CreateFileFromTemplateAction extends SimpleAction {

    private Properties properties;
    private FileTemplate template;
    private String fileName;
    private String parentPath;
    private Project project;

    //result
    private PsiElement psiResult;

    public CreateFileFromTemplateAction(Properties properties, FileTemplate template, String fileName, String parentPath, Project project) {
        this.properties = properties;
        this.template = template;
        this.fileName = fileName;
        this.parentPath = parentPath;
        this.project = project;
    }

    @Override
    public boolean run() {
        psiResult = null;
        isDone = false;

        psiResult = FileWriter.createFileFromTemplate(project, template, fileName, properties, parentPath);
        isDone = psiResult != null;

        return isDone;
    }

    @Override
    public boolean undo() {
        isDone = !FileWriter.removeFile(psiResult);
        return !isDone;
    }

}
