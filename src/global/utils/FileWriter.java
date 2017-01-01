package global.utils;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import global.wrappers.DirectoryWrapper;
import global.wrappers.FileWrapper;
import groovy.json.internal.Charsets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
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

        RunnableFuture<PsiDirectory> runnableFuture = new FutureTask<>(() ->
                ApplicationManager.getApplication().runWriteAction(new Computable<PsiDirectory>() {
                    @Override
                    public PsiDirectory compute() {
                        return writeDirectoryAction(dir, dirWrapper, project);
                    }
                }));

        ApplicationManager.getApplication().invokeLater(runnableFuture);

        try {
            return runnableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            Logger.log("runnableFuture  " + e.getMessage());
        }

        return null;
    }

    @Nullable
    private static PsiDirectory writeDirectoryAction(PsiDirectory dir, DirectoryWrapper dirWrapper, Project project) {
        FutureTask<PsiDirectory> psiDirectoryFutureTask = new FutureTask<>(() -> {

//            Properties properties = new Properties();
//            properties.putAll(dirWrapper.getPackageTemplateWrapper().getDefaultProperties());
//            FileTemplate fileTemplate = AttributesHelper.getTemplate("Test");
//            FileTemplateUtil.createFromTemplate(fileTemplate, "TestFile", properties, dir);

            return dir.createSubdirectory(dirWrapper.getDirectory().getName());
        });
        CommandProcessor.getInstance().executeCommand(project, psiDirectoryFutureTask, "Create '" + dirWrapper.getDirectory().getName() + "' Directory",  "testGroupId");

        try {
            return psiDirectoryFutureTask.get();
        } catch (Exception ex) {
            Logger.log(ex.getMessage());
            dirWrapper.setWriteException(ex);
            dirWrapper.getPackageTemplateWrapper().getFailedElements().add(dirWrapper);
            return null;
        }
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
            Logger.log("runnableFuture  " + e.getMessage());
        }

        fileWrapper.getPackageTemplateWrapper().getWrittenElements().add(element);
        return element;
    }

    @NotNull
    private static File createFile(String path, String fileName) {
        File file = new File(path + "/" + fileName);

        while (file.exists() && !file.isDirectory()) {
            Logger.log("Exist");
            //todo overwrite or change name dialog
            break;
        }

        return file;
    }

    public static void writeStringToFile(String text, String path) {
        writeStringToFile(text, new File(path));
    }

    public static void writeStringToFile(String text, File file) {
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8));

            out.write(text);

            out.flush();
            out.close();
        } catch (Exception e) {
            Logger.log(e.getMessage());
        }
    }

}
