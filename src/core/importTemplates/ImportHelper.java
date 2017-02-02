package core.importTemplates;

import com.intellij.ide.fileTemplates.FileTemplatesScheme;
import com.intellij.openapi.project.Project;
import core.actions.custom.CopyFileAction;
import core.actions.custom.DeleteFileAction;
import core.actions.custom.base.SimpleAction;
import core.actions.executor.AccessPrivileges;
import core.actions.executor.ActionExecutor;
import core.export.ExportHelper;
import global.Const;
import global.dialogs.SkipableConfirmationDialog;
import global.utils.Logger;
import global.utils.StringTools;
import global.utils.i18n.Localizer;
import global.utils.templates.PackageTemplateHelper;
import global.wrappers.PackageTemplateWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by Arsen on 05.01.2017.
 */
public class ImportHelper {

    private static class Context {
        Project project;
        ArrayList<PackageTemplateWrapper> ptWrappers;
        HashSet<String> requiredTemplateNames;
        ArrayList<File> selectedFiles;

        ArrayList<File> availableFileTemplates;
        ArrayList<SimpleAction> listSimpleAction;

        Context(Project project, ArrayList<PackageTemplateWrapper> ptWrappers, HashSet<String> requiredTemplateNames, ArrayList<File> selectedFiles) {
            this.project = project;
            this.ptWrappers = ptWrappers;
            this.requiredTemplateNames = requiredTemplateNames;
            this.selectedFiles = selectedFiles;

            availableFileTemplates = new ArrayList<>();
            listSimpleAction = new ArrayList<>();
        }
    }

    public static void importPackageTemplate(Project project, ArrayList<PackageTemplateWrapper> ptWrappers, HashSet<String> requiredTemplateNames, ArrayList<File> selectedFiles) {
        Context ctx = new Context(project, ptWrappers, requiredTemplateNames, selectedFiles);

        //check all src
        if (!isResourcesAvailable(ctx)) {
            //todo show err msg
            Logger.log("import, isResourcesAvailable false");
            return;
        }

        // Import
        File templatesDir = new File(ExportHelper.getFileTemplatesDirPath());

        //PackageTemplate Actions
        for (File file : selectedFiles) {
            File fileTo = new File(PackageTemplateHelper.getRootDirPath() + file.getName());

            if (!inOrderToDelete(ctx, fileTo) && fileTo.exists() && !fileTo.isDirectory()) {
                Logger.log("Skip file: " + fileTo.getName());
                break;
            }

            ctx.listSimpleAction.add(new CopyFileAction(
                    file,
                    fileTo
            ));
        }

        // FileTemplate Actions
        for (String name : requiredTemplateNames) {
            for (File template : ctx.availableFileTemplates) {
                if (StringTools.getNameWithoutExtension(template.getName()).equals(name)) {
                    File fileTo = new File(templatesDir.getPath() + File.separator + template.getName());

                    if (!inOrderToDelete(ctx, fileTo) && fileTo.exists() && !fileTo.isDirectory()) {
                        Logger.log("Skip file: " + fileTo.getName());
                        break;
                    }

                    ctx.listSimpleAction.add(new CopyFileAction(
                            template,
                            fileTo
                    ));
                    break;
                }
            }
        }

        if (ActionExecutor.runAsTransaction(project, ctx.listSimpleAction, "Import PackageTemplates", AccessPrivileges.WRITE)) {
            Logger.log("importPackageTemplate  Done!");
        } else {
            //todo revert?
            Logger.log("importPackageTemplate  Fail!");
        }
    }

    private static boolean inOrderToDelete(Context ctx, File fileTo) {
        for (SimpleAction action : ctx.listSimpleAction){
            if(action instanceof DeleteFileAction){
                File fileToDelete = ((DeleteFileAction) action).getFileToDelete();
                if(fileToDelete.getPath().equals(fileTo.getPath())){
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isResourcesAvailable(Context ctx) {
        for (File file : ctx.selectedFiles) {
            // Verify FileTemp Dir
            if (!containsDirectoryByName(file, FileTemplatesScheme.TEMPLATES_DIR)) {
                Logger.log("no FileTemplates dir for " + file.getName());
                return false;
            }

            // collect FileTemplates
            File templatesDir = new File(file.getParentFile().getPath() + File.separator + FileTemplatesScheme.TEMPLATES_DIR);
            File[] templates = templatesDir.listFiles();

            if (templates != null) {
                Collections.addAll(ctx.availableFileTemplates, templates);
            }
        }

        for (String name : ctx.requiredTemplateNames) {
            boolean contains = false;
            for (File template : ctx.availableFileTemplates) {
                if (StringTools.getNameWithoutExtension(template.getName()).equals(name)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                Logger.log("don't contains " + name);
                return false;
            }
        }

        //Verify existence of PackageTemplates in IDE
        File packageTemplatesDir = PackageTemplateHelper.getRootDir();
        for (File file : ctx.selectedFiles) {
            File templateFile = new File(packageTemplatesDir.getPath() + File.separator + file.getName());
            if (templateFile.exists() && !templateFile.isDirectory()) {
                boolean doCancel = askAboutFileConflict(ctx, templateFile, Localizer.get("warning.import.PackageTemplateAlreadyExist"));
                if (doCancel) {
                    return false;
                }
            }
        }

        //Verify existence of FileTemplates in IDE
        File[] userFiles = new File(ExportHelper.getFileTemplatesDirPath() + Const.DIR_USER).listFiles();
        File[] internalFiles = new File(ExportHelper.getFileTemplatesDirPath() + Const.DIR_INTERNAL).listFiles();
        File[] j2eeFiles = new File(ExportHelper.getFileTemplatesDirPath() + Const.DIR_J2EE).listFiles();

        for (String templateName : ctx.requiredTemplateNames) {
            File file = ExportHelper.findInArrays(templateName, userFiles, internalFiles, j2eeFiles);
            if (file != null) {
                boolean doCancel = askAboutFileConflict(ctx, file, Localizer.get("warning.import.FileTemplateAlreadyExist"));
                if (doCancel) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * @return флаг отмены, true - doCancel.
     */
    private static boolean askAboutFileConflict(Context ctx, final File templateFile, String dialogTitle) {
        final boolean[] result = {false};
        new SkipableConfirmationDialog(
                ctx.project,
                Localizer.get("question.Overwrite"),
                dialogTitle,
                Localizer.get("action.Overwrite"),
                Localizer.get("action.CancelImport")
        ) {
            @Override
            public void onOk() {
                ctx.listSimpleAction.add(new DeleteFileAction(templateFile));
            }

            @Override
            public void onCancel() {
                result[0] = true;
            }
        };

        return result[0];
    }

    /**
     * Проверяет наличие директории рядом с file
     */
    private static boolean containsDirectoryByName(File file, String name) {
        File[] files = file.getParentFile().listFiles();
        if (files == null) {
            return false;
        }

        for (File item : files) {
            if (item.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

}
