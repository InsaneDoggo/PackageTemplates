package global.visitors;

import base.ElementVisitor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import global.models.File;
import global.utils.file.FileWriter;
import global.wrappers.DirectoryWrapper;
import global.wrappers.ElementWrapper;
import global.wrappers.FileWrapper;

import java.util.ArrayList;

/**
 * Записывает элемент на файловую систему.
 */
public class WriteElementVisitor implements ElementVisitor {

    private Project project;

    public WriteElementVisitor(PsiDirectory currentDir, Project project) {
        this.project = project;
        initStack(currentDir);
    }


    //=================================================================
    //  Stack
    //=================================================================
    private ArrayList<PsiDirectory> stackDirs;

    private void initStack(PsiDirectory currentDir) {
        stackDirs = new ArrayList<>();
        pushDir(currentDir);
    }

    private PsiDirectory getLastDir() {
        return stackDirs.get(stackDirs.size() - 1);
    }

    private void popDir() {
        stackDirs.remove(stackDirs.size() - 1);
    }

    private void pushDir(PsiDirectory directory) {
        stackDirs.add(directory);
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
            PsiDirectory subDirectory = FileWriter.writeDirectory(getLastDir(), wrapper, project);
            if (subDirectory != null) {
                wrapper.getPackageTemplateWrapper().getWrittenElements().add(subDirectory);
                // Add to stack
                pushDir(subDirectory);
                for (ElementWrapper element : wrapper.getListElementWrapper()) {
                    element.accept(this);
                }
            }
        }

        popDir();
    }

    @Override
    public void visit(FileWrapper wrapper) {
        File file = wrapper.getFile();

        if (!file.isEnabled()) {
            return;
        }
        FileWriter.writeFile(getLastDir(), wrapper);
    }

}
