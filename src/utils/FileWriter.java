package utils;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import models.TemplateElement;

import java.io.File;
import java.io.IOException;
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

    public static PsiDirectory writeDirectory(PsiDirectory dir, TemplateElement templateElement, Project project) {
        if (!templateElement.isDirectory()) {
            //todo print error
            return null;
        }

        if (dir == null) {
            //todo print error
            return null;
        }
        final PsiDirectory[] directory = new PsiDirectory[1];

        CommandProcessor.getInstance().executeCommand(project, () -> ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                directory[0] = dir.createSubdirectory(templateElement.getName());
            } catch (Exception ex) {}
        }), null, null);

        if(directory[0] == null){
            //todo print error
        }

        return directory[0];
    }

    public static PsiElement writeFile(PsiDirectory dir, TemplateElement templateElement) {
        if (dir == null) {
            //todo print error
            return null;
        }

        FileTemplate template = InputManager.getTemplate(templateElement.getTemplateName());

        Properties properties = new Properties();
        properties.putAll(templateElement.getMapProperties());

        PsiElement element;
        try {
            element = FileTemplateUtil.createFromTemplate(template, templateElement.getName(), properties, dir);
        } catch (Exception e) {
            //todo print error
            System.out.println(e.getMessage());
            return null;
        }

        return element;
    }

    public static boolean exportFile(String path, String fileName, String content){
        try {
            FileUtil.writeToFile(new File(path+"/"+fileName),content);
        } catch (IOException e) {
            //todo print error
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

}
