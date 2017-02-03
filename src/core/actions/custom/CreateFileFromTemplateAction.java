package core.actions.custom;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import core.actions.custom.base.SimpleAction;
import core.actions.custom.interfaces.IHasPsiDirectory;
import core.search.SearchAction;
import core.search.SearchEngine;
import global.models.BaseElement;
import global.models.File;
import global.utils.Logger;
import global.utils.file.FileWriter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Arsen on 09.01.2017.
 */
public class CreateFileFromTemplateAction extends SimpleAction {

    private Properties properties;
    private FileTemplate template;
    private File file;
    private Project project;

    //result
    private PsiElement psiElementCreated;

    public CreateFileFromTemplateAction(Properties properties, FileTemplate template, File file, Project project) {
        this.properties = properties;
        this.template = template;
        this.file = file;
        this.project = project;
    }

    @Override
    public boolean run(SimpleAction parentAction) {
        psiElementCreated = null;

        if (parentAction instanceof IHasPsiDirectory) {
            PsiDirectory psiParent = ((IHasPsiDirectory) parentAction).getPsiDirectory();
            String path = psiParent.getVirtualFile().getPath();

            // Custom Path
            if (file.getCustomPath() != null) {
                path = getCustomPath(file, path);

                if (path == null) {
                    isDone = false;
                    return false;
                }
            }

            psiElementCreated = FileWriter.createFileFromTemplate(project, template, file.getName(), properties, path);
        }

        if (psiElementCreated == null) {
            isDone = false;
            return false;
        }

        return super.run(this);
    }

    @Override
    public boolean undo(SimpleAction parentAction) {
        if (!super.undo(this)) {
            return false;
        }

        isDone = !FileWriter.removeFile(psiElementCreated);
        return !isDone;
    }


    //=================================================================
    //  Utils
    //=================================================================
    @Nullable
    private String getCustomPath(BaseElement element, String pathFrom) {
        ArrayList<SearchAction> actions = element.getCustomPath().getListSearchAction();

        java.io.File searchResultFile = SearchEngine.find(new java.io.File(pathFrom), actions);

        if (searchResultFile == null) {
            //print name of last action
            Logger.log("getCustomPath File Not Found: " + (actions.isEmpty() ? "" : actions.get(actions.size() - 1).getName()));
            return null;
        }

        return searchResultFile.getPath();
    }

}
