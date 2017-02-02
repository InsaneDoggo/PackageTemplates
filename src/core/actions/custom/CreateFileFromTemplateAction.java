package core.actions.custom;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import core.actions.custom.base.SimpleAction;
import core.actions.custom.interfaces.IHasPsiDirectory;
import global.utils.file.FileWriter;

import java.util.Properties;

/**
 * Created by Arsen on 09.01.2017.
 */
public class CreateFileFromTemplateAction extends SimpleAction {

    private Properties properties;
    private FileTemplate template;
    private String fileName;
    private Project project;

    //result
    private PsiElement psiElementCreated;

    public CreateFileFromTemplateAction(Properties properties, FileTemplate template, String fileName, Project project) {
        this.properties = properties;
        this.template = template;
        this.fileName = fileName;
        this.project = project;
    }

    @Override
    public boolean run(SimpleAction parentAction) {
        psiElementCreated = null;

        if(parentAction instanceof IHasPsiDirectory){
            PsiDirectory psiParent = ((IHasPsiDirectory) parentAction).getPsiDirectory();

            //todo check custom path
            psiElementCreated = FileWriter.createFileFromTemplate(project, template, fileName, properties, psiParent.getVirtualFile().getPath());
        }

        if(psiElementCreated == null){
            isDone = false;
            return false;
        }

        return super.run(this);
    }

    @Override
    public boolean undo(SimpleAction parentAction) {
        if(!super.undo(this)){ return false; }

        isDone = !FileWriter.removeFile(psiElementCreated);
        return !isDone;
    }

}
