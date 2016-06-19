package utils;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import models.TemplateElement;

import java.util.Properties;

/**
 * Created by CeH9 on 19.06.2016.
 */
public class FileWriter {

    public static PsiDirectory findCurrentDirectory(AnActionEvent e) {
        VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);

        if (file != null && e.getProject() != null) {
            if (file.isDirectory()) {
                return PsiManager.getInstance(e.getProject()).findDirectory(file);
            } else {
                return PsiManager.getInstance(e.getProject()).findDirectory(file.getParent());
            }
        }

        return null;
    }

    public static PsiDirectory writeDirectory(PsiDirectory dir, TemplateElement templateElement) {
        if (!templateElement.isDirectory()) {
            //todo print error
            return null;
        }

        if (dir == null) {
            //todo print error
            return null;
        }

        return dir.createSubdirectory(templateElement.getName());
    }

    public static PsiElement writeFile(PsiDirectory dir, TemplateElement templateElement) {
        if (dir == null) {
            //todo print error
            return null;
        }

        FileTemplate template = FileTemplateManager.getDefaultInstance().getTemplate(templateElement.getTemplateName());

        Properties properties = new Properties();
        properties.putAll(templateElement.getMapProperties());

        PsiElement element;
        try {
            element = FileTemplateUtil.createFromTemplate(template, templateElement.getName(), properties, dir);
        } catch (Exception e) {
            //todo print error
            return null;
        }

        return element;
    }

}
