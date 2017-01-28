package global.visitors;

import base.ElementVisitor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import core.actions.custom.CreateDirectoryAction;
import core.actions.custom.CreateFileFromTemplateAction;
import core.actions.custom.CreateFileAction;
import core.actions.custom.SimpleAction;
import global.models.File;
import global.utils.templates.FileTemplateHelper;
import global.wrappers.DirectoryWrapper;
import global.wrappers.ElementWrapper;
import global.wrappers.FileWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Записывает элемент на файловую систему.
 */
public class CollectSimpleActionVisitor implements ElementVisitor {

    private Project project;
    private List<SimpleAction> listActions;

    public CollectSimpleActionVisitor(PsiDirectory currentDir, Project project, List<SimpleAction> listActions) {
        this.project = project;
        this.listActions = listActions;
        initStack(currentDir.getVirtualFile().getPath() + java.io.File.separator);
    }


    //=================================================================
    //  Stack
    //=================================================================
    private ArrayList<String> stackPath;

    private void initStack(String currentDir) {
        stackPath = new ArrayList<>();
        pushPath(currentDir);
    }

    private String getLastPath() {
        return stackPath.get(stackPath.size() - 1);
    }

    private void popPath() {
        stackPath.remove(stackPath.size() - 1);
    }

    private void pushPath(String directory) {
        stackPath.add(directory);
    }


    // ======================================================
    //  Write elements
    // ======================================================
    @Override
    public void visit(DirectoryWrapper wrapper) {
        if (!wrapper.getDirectory().isEnabled()) {
            return;
        }

        if (wrapper.getParent() == null && wrapper.getPackageTemplateWrapper().getPackageTemplate().isSkipRootDirectory()) {
            // Only files without Directory
            for (ElementWrapper element : wrapper.getListElementWrapper()) {
                element.accept(this);
            }
        } else {
//            PsiDirectory subDirectory = FileWriter.writeDirectory(getLastPath(), wrapper, project);
            String subDir = getLastPath() + wrapper.getElement().getName() + java.io.File.separator;

            java.io.File subDirectory = new java.io.File(subDir);
            if(!subDirectory.exists()){
                // Create if not exist
                listActions.add(new CreateDirectoryAction(subDirectory));
            }

            pushPath(subDir);
            for (ElementWrapper element : wrapper.getListElementWrapper()) {
                element.accept(this);
            }
        }

        popPath();
    }

    @Override
    public void visit(FileWrapper wrapper) {
        File file = wrapper.getFile();

        if (!file.isEnabled()) {
            return;
        }

        Properties properties = new Properties();
        properties.putAll(wrapper.getPackageTemplateWrapper().getDefaultProperties());
        properties.putAll(wrapper.getFile().getMapProperties());

        listActions.add(new CreateFileFromTemplateAction(
                properties,
                FileTemplateHelper.getTemplate(wrapper.getFile().getTemplateName()),
                file.getName(),
                getLastPath(),
                project
        ));
        //        FileWriter.writeFile(getLastPath(), wrapper);
    }

}
