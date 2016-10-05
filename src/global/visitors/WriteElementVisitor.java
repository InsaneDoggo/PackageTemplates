package global.visitors;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import global.models.Directory;
import global.models.File;
import global.utils.FileWriter;
import global.wrappers.DirectoryWrapper;
import global.wrappers.ElementWrapper;
import global.wrappers.FileWrapper;

/**
 * Записывает элемент на файловую систему.
 */
public class WriteElementVisitor implements ElementVisitor {

    private PsiDirectory parentDir;
    private Project project;

    public WriteElementVisitor(PsiDirectory currentDir, Project project) {
        this.parentDir = currentDir;
        this.project = project;
    }

    @Override
    public void visit(DirectoryWrapper wrapper) {
        Directory directory = wrapper.getDirectory();

        if (!directory.isEnabled()) {
            return;
        }
        PsiDirectory currentDir = parentDir;

        if (wrapper.getParent() == null && wrapper.getPackageTemplateWrapper().getPackageTemplate().isSkipRootDirectory()) {
            // Only files without Directory
            for (ElementWrapper element : wrapper.getListElementWrapper()) {
                parentDir = currentDir;
                element.accept(this);
            }
        } else {
            PsiDirectory subDirectory = FileWriter.writeDirectory(currentDir, wrapper, project);
            if (subDirectory != null) {
                wrapper.getPackageTemplateWrapper().getWrittenElements().add(subDirectory);
                for (ElementWrapper element : wrapper.getListElementWrapper()) {
                    parentDir = subDirectory;
                    element.accept(this);
                }
            }
        }
    }

    @Override
    public void visit(FileWrapper wrapper) {
        File file = wrapper.getFile();

        if (!file.isEnabled()) {
            return;
        }
        FileWriter.writeFile(parentDir, wrapper);
    }

}
