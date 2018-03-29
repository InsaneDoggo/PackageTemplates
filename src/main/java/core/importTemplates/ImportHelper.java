package core.importTemplates;

import com.intellij.ide.fileTemplates.FileTemplatesScheme;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import core.actions.custom.CreateFileTemplateAction;
import core.actions.custom.base.SimpleAction;
import core.actions.custom.undoTransparent.TransparentCopyFileAction;
import core.actions.custom.undoTransparent.TransparentCreateFileAction;
import core.actions.custom.undoTransparent.TransparentCreateSimpleDirectoryAction;
import core.actions.custom.undoTransparent.TransparentDeleteFileAction;
import core.actions.executor.AccessPrivileges;
import core.actions.executor.ActionExecutor;
import core.actions.executor.request.ActionRequest;
import core.actions.executor.request.ActionRequestBuilder;
import core.exportTemplates.ExportHelper;
import core.writeRules.WriteRules;
import global.Const;
import global.dialogs.SkipableNonCancelDialog;
import global.models.PackageTemplate;
import global.utils.Logger;
import global.utils.factories.GsonFactory;
import global.utils.file.FileWriter;
import global.utils.templates.FileTemplateHelper;
import global.utils.templates.FileTemplateSource;
import global.utils.text.StringTools;
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
        WriteRules writeRules;

        ArrayList<String> templateNamesToDelete;
        ArrayList<File> selectedFilesToDelete;

        File packageTemplatesDir;
        FileTemplateSource newFileTemplateSource;

        Context(Project project, ArrayList<PackageTemplateWrapper> ptWrappers, HashSet<String> requiredTemplateNames, ArrayList<File> selectedFiles, WriteRules writeRules) {
            this.project = project;
            this.ptWrappers = ptWrappers;
            this.requiredTemplateNames = requiredTemplateNames;
            this.selectedFiles = selectedFiles;
            this.writeRules = writeRules;

            availableFileTemplates = new ArrayList<>();
            listSimpleAction = new ArrayList<>();
            templateNamesToDelete = new ArrayList<>();
            selectedFilesToDelete = new ArrayList<>();
        }
    }

    public static void importPackageTemplate(Project project, ArrayList<PackageTemplateWrapper> ptWrappers,
                                             HashSet<String> requiredTemplateNames, ArrayList<File> selectedFiles, WriteRules writeRules) {
        Context ctx = new Context(project, ptWrappers, requiredTemplateNames, selectedFiles, writeRules);

        //check all src
        if (!isResourcesAvailable(ctx)) {
            //todo show err msg
            Logger.log("import, isResourcesAvailable false");
            return;
        }

        // IMPORT
        // PackageTemplate Actions
//        for (File file : selectedFiles) {
        for (PackageTemplateWrapper ptWrapper: ptWrappers) {
//            File fileTo = new File( ctx.packageTemplatesDir + File.separator +  file.getName());

            ptWrapper.getPackageTemplate().setFileTemplateSource(ctx.newFileTemplateSource);
            String ptJson = GsonFactory.getInstance().toJson(ptWrapper.getPackageTemplate(), PackageTemplate.class);

            File ptFile = new File(String.format("%s%s%s.%s",
                    ctx.packageTemplatesDir,
                    File.separator,
                    ptWrapper.getPackageTemplate().getName(),
                    Const.PACKAGE_TEMPLATES_EXTENSION
            ));

            ctx.listSimpleAction.add(new TransparentCreateFileAction(ptFile, ptJson));
//            ctx.listSimpleAction.add(new TransparentCopyFileAction(file, fileTo));
        }

        // FileTemplate Actions
        for (String name : requiredTemplateNames) {
            for (File template : ctx.availableFileTemplates) {
                if (StringTools.getNameWithoutExtension(template.getName()).equals(name)) {
                    ctx.listSimpleAction.add(new CreateFileTemplateAction(project, template));
                    break;
                }
            }
        }

        ActionRequest actionRequest = new ActionRequestBuilder()
                .setProject(project)
                .setActions(ctx.listSimpleAction)
                .setActionLabel("Import PackageTemplates")
                .setAccessPrivileges(AccessPrivileges.WRITE)
                .setConfirmationPolicy(UndoConfirmationPolicy.REQUEST_CONFIRMATION)
                .build();

        ActionExecutor.runAsTransaction(actionRequest);
    }

    private static boolean isResourcesAvailable(Context ctx) {
        ArrayList<String> scannedFileTemplateDirs = new ArrayList<>();

        for (File file : ctx.selectedFiles) {
            // Verify FileTemp Dir
            if (!containsDirectoryByName(file, FileTemplatesScheme.TEMPLATES_DIR)) {
                Logger.log("no FileTemplates dir for " + file.getName());
                return false;
            }

            // collect FileTemplates
            File templatesDir = new File(file.getParentFile().getPath() + File.separator + FileTemplatesScheme.TEMPLATES_DIR);

            // Avoid already scanned dirs
            if (isAlreadyScanned(scannedFileTemplateDirs, templatesDir)) {
                continue;
            }
            scannedFileTemplateDirs.add(templatesDir.getPath());

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
        File packageTemplatesDir = getCurrentRootDir(ctx);
        if(packageTemplatesDir == null){
            Logger.log("packageTemplatesDir == null");
            return false;
        }
        ctx.packageTemplatesDir = packageTemplatesDir;

        for (File file : ctx.selectedFiles) {
            File templateFile = new File(packageTemplatesDir.getPath() + File.separator + file.getName());
            if (templateFile.exists() && !templateFile.isDirectory()) {
                if (shouldSkipFile(ctx, templateFile, Localizer.get("warning.import.PackageTemplateAlreadyExist"))) {
                    ctx.selectedFilesToDelete.add(file);
                }
            }
        }

        //Verify existence of FileTemplates in IDE
        File[] userFiles = new File(FileTemplateHelper.getFileTemplatesDirPath(ctx.project) + Const.DIR_USER).listFiles();
        File[] internalFiles = new File(FileTemplateHelper.getFileTemplatesDirPath(ctx.project) + Const.DIR_INTERNAL).listFiles();
        File[] j2eeFiles = new File(FileTemplateHelper.getFileTemplatesDirPath(ctx.project) + Const.DIR_J2EE).listFiles();

        for (String templateName : ctx.requiredTemplateNames) {
            File file = ExportHelper.findInArrays(templateName, userFiles, internalFiles, j2eeFiles);
            if (file != null) {
                if (shouldSkipFile(ctx, file, Localizer.get("warning.import.FileTemplateAlreadyExist"))) {
                    ctx.templateNamesToDelete.add(templateName);
                }
            }
        }

        // Delete conflicted
        for (String name : ctx.templateNamesToDelete) {
            ctx.requiredTemplateNames.remove(name);
        }

        for (File file : ctx.selectedFilesToDelete) {
            ctx.selectedFiles.remove(file);
        }

        return true;
    }

    private static boolean isAlreadyScanned(ArrayList<String> scannedFileTemplateDirs, File templatesDir) {
        String curPath = templatesDir.getPath();
        for (String path : scannedFileTemplateDirs) {
            if (path.equals(curPath)) {
                return true;
            }
        }

        return false;
    }

    private static boolean shouldSkipFile(Context ctx, final File file, String dialogTitle) {
        final boolean[] result = {false};

        switch (ctx.writeRules) {
            case ASK_ME:
                result[0] = true;
                new SkipableNonCancelDialog(
                        ctx.project,
                        String.format(Localizer.get("question.OverwriteArg"), file.getName()),
                        dialogTitle,
                        Localizer.get("action.Overwrite")
                ) {
                    @Override
                    public void onOk() {
                        result[0] = false;
                        ctx.listSimpleAction.add(new TransparentDeleteFileAction(file));
                    }
                };
                break;
            case OVERWRITE:
                result[0] = false;
                ctx.listSimpleAction.add(new TransparentDeleteFileAction(file));
                break;
            case USE_EXISTING:
                result[0] = true;
                break;
        }

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


    //=================================================================
    //  Utils
    //=================================================================
    private static File getCurrentRootDir(Context ctx) {
        if (FileTemplateHelper.isDefaultScheme(ctx.project)) {
            return PackageTemplateHelper.getRootDir();
        }

        int resultCode = Messages.showYesNoDialog(
                ctx.project,
                Localizer.get("title.ImportPackageTemplate"),
                Localizer.get("text.WhereToSave"),
                Localizer.get("action.ToDefaultDir"),
                Localizer.get("action.ToProjectDir"),
                Messages.getQuestionIcon()
        );
        //To Default
        if (resultCode == Messages.YES) {
            ctx.newFileTemplateSource = FileTemplateSource.DEFAULT_ONLY;
            return PackageTemplateHelper.getRootDir();
        } else {
            //To Project
            ctx.newFileTemplateSource = FileTemplateSource.PROJECT_ONLY;
            String rootDirPath = PackageTemplateHelper.getProjectRootDirPath(ctx.project);
            if (rootDirPath == null) {
                return null;
            }

            File file = new File(rootDirPath);
            FileWriter.createDirectories(file.toPath());
            return file;
        }
    }


}
