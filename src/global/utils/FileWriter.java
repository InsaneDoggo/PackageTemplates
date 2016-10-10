package global.utils;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import core.state.impex.models.ExpFileTemplate;
import global.wrappers.DirectoryWrapper;
import global.wrappers.FileWrapper;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 * Created by CeH9 on 19.06.2016.
 */
public class FileWriter {

    public static PsiDirectory findCurrentDirectory(Project project, VirtualFile file) {
        if (file != null && project != null) {
            if (file.isDirectory()) {
                return PsiManager.getInstance(project).findDirectory(file);
            } else {
                return PsiManager.getInstance(project).findDirectory(file.getParent());
            }
        }

        return null;
    }

    public static PsiDirectory writeDirectory(PsiDirectory dir, DirectoryWrapper dirWrapper, Project project) {
        if (dir == null) {
            //todo print error
            return null;
        }


        //        ApplicationManager.getApplication().invokeLater(
//                () -> CommandProcessor.getInstance().executeCommand(project,
//                        () -> ApplicationManager.getApplication().runWriteAction(() -> {
//                            try {
//                                directory[0] = dir.createSubdirectory(dirWrapper.getDirectory().getName());
//                            } catch (Exception ex) {
//                                Logger.log(ex.getMessage());
//                                dirWrapper.setWriteException(ex);
//                                dirWrapper.getPackageTemplateWrapper().getFailedElements().add(dirWrapper);
//                            }
//                        }), null, null));

        RunnableFuture<PsiDirectory> runnableFuture = new FutureTask<>(() ->
                ApplicationManager.getApplication().runWriteAction(new Computable<PsiDirectory>() {
                    @Override
                    public PsiDirectory compute() {
                        try {
                            return dir.createSubdirectory(dirWrapper.getDirectory().getName());
                        } catch (Exception ex) {
                            Logger.log(ex.getMessage());
                            dirWrapper.setWriteException(ex);
                            dirWrapper.getPackageTemplateWrapper().getFailedElements().add(dirWrapper);
                            return null;
                        }
                    }
                }));

        ApplicationManager.getApplication().invokeLater(runnableFuture);
        try {
            return runnableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("runnableFuture  " + e.getMessage());
        }

        return null;
    }

    public static PsiElement writeFile(PsiDirectory dir, FileWrapper fileWrapper) {
        FileTemplate template = AttributesHelper.getTemplate(fileWrapper.getFile().getTemplateName());

        if (dir == null || template == null) {
            //todo print error
            return null;
        }

        Properties properties = new Properties();
        properties.putAll(fileWrapper.getPackageTemplateWrapper().getDefaultProperties());
        properties.putAll(fileWrapper.getFile().getMapProperties());

        PsiElement element = null;
        RunnableFuture<PsiElement> runnableFuture = new FutureTask<>(() ->
                ApplicationManager.getApplication().runWriteAction((Computable<PsiElement>) () -> {
                    try {
                        return FileTemplateUtil.createFromTemplate(template, fileWrapper.getFile().getName(), properties, dir);
                    } catch (Exception e) {
                        Logger.log(e.getMessage());
                        fileWrapper.setWriteException(e);
                        fileWrapper.getPackageTemplateWrapper().getFailedElements().add(fileWrapper);
                        return null;
                    }
                }));

        ApplicationManager.getApplication().invokeLater(runnableFuture);
        try {
            element = runnableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("runnableFuture  " + e.getMessage());
        }

        fileWrapper.getPackageTemplateWrapper().getWrittenElements().add(element);
        return element;
    }

    public static boolean exportFile(String path, String fileName, String content) {
        try {
            File file = createFile(path, fileName);

            FileUtil.writeToFile(file, content);
        } catch (IOException e) {
            //todo print error
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @NotNull
    private static File createFile(String path, String fileName) {
        File file = new File(path + "/" + fileName);

        while (file.exists() && !file.isDirectory()) {
            System.out.println("Exist");
            //todo overwrite or change name dialog
            break;
        }

        return file;
    }

    public static FileTemplate createFileTemplate(ExpFileTemplate expTemplate) {
        FileTemplateManager ftm = FileTemplateManager.getDefaultInstance();
        if (isFileTemplateExist(expTemplate.getName())) {
            //todo overwrite dialog
            System.out.println("file template already exist");
        }

        FileTemplate fileTemplate = ftm.addTemplate(expTemplate.getName(), expTemplate.getExtension());
        fileTemplate.setText(expTemplate.getText());

        //todo description
        return fileTemplate;
    }

    private static boolean isFileTemplateExist(String name) {
        return AttributesHelper.getTemplate(name) == null;
    }

}
