package utils;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import wrappers.DirectoryWrapper;
import wrappers.FileWrapper;

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

    public static PsiDirectory writeDirectory(PsiDirectory dir, DirectoryWrapper dirWrapper, Project project) {
        if (dir == null) {
            //todo print error
            return null;
        }

        final PsiDirectory[] directory = new PsiDirectory[1];

        CommandProcessor.getInstance().executeCommand(project, () -> ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                directory[0] = dir.createSubdirectory(dirWrapper.getDirectory().getName());
            } catch (Exception ex) {
                Logger.log(ex.getMessage());
            }
        }), null, null);

        if(directory[0] == null){
            //todo print error
        }

        return directory[0];
    }

    public static PsiElement writeFile(PsiDirectory dir, FileWrapper fileWrapper) {
        FileTemplate template = AttributesHelper.getTemplate(fileWrapper.getFile().getTemplateName());

        if (dir == null || template == null) {
            //todo print error
            return null;
        }

        Properties properties = new Properties();
        properties.putAll(fileWrapper.getFile().getMapProperties());

        PsiElement element;
        try {
            element = FileTemplateUtil.createFromTemplate(template, fileWrapper.getFile().getName(), properties, dir);
        } catch (Exception e) {
            //todo print error
            Logger.log(e.getMessage());
            return null;
        }

        return element;
    }

    public static boolean exportFile(String path, String fileName, String content) {
        try {
            FileUtil.writeToFile(new File(path + "/" + fileName), content);
        } catch (IOException e) {
            //todo print error
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

}
