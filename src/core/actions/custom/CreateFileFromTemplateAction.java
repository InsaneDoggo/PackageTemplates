package core.actions.custom;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import global.utils.Logger;
import global.utils.file.FileWriter;

import java.io.File;
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
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(new File(parentPath));
        PsiDirectory psiDirectory = PsiManager.getInstance(project).findDirectory(virtualFile);
        psiResult = null;

        try {
            psiResult = FileTemplateUtil.createFromTemplate(template, fileName, properties, psiDirectory);
            isDone = true;
        } catch (Exception e) {
            Logger.log("createFromTemplate ex: " + e.getMessage());
            isDone = false;
        }

        return isDone;
    }

    @Override
    public boolean undo() {
        isDone = !FileWriter.removeFile(psiResult);
        return !isDone;
    }
}
