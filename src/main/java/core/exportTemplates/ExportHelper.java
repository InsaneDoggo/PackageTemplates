package core.exportTemplates;

import com.intellij.ide.fileTemplates.FileTemplatesScheme;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import core.actions.custom.base.SimpleAction;
import core.actions.custom.undoTransparent.TransparentCopyFileAction;
import core.actions.custom.undoTransparent.TransparentCreateFileAction;
import core.actions.custom.undoTransparent.TransparentCreateSimpleDirectoryAction;
import core.actions.custom.undoTransparent.TransparentDeleteDirectoryAction;
import core.actions.executor.AccessPrivileges;
import core.actions.executor.ActionExecutor;
import core.actions.executor.request.ActionRequest;
import core.actions.executor.request.ActionRequestBuilder;
import global.Const;
import global.dialogs.SkipableNonCancelDialog;
import global.models.PackageTemplate;
import global.utils.Logger;
import global.utils.factories.GsonFactory;
import global.utils.i18n.Localizer;
import global.utils.templates.FileTemplateHelper;
import global.utils.text.StringTools;
import global.wrappers.PackageTemplateWrapper;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Arsen on 30.10.2016.
 */
public class ExportHelper {

    private static class Context {
        Project project;
        String pathDir;
        PackageTemplateWrapper ptWrapper;
        HashSet<String> hsFileTemplateNames;
        ArrayList<SimpleAction> listSimpleAction;

        public Context(Project project, String pathDir, PackageTemplateWrapper ptWrapper, HashSet<String> hsFileTemplateNames) {
            this.project = project;
            this.pathDir = pathDir;
            this.ptWrapper = ptWrapper;
            this.hsFileTemplateNames = hsFileTemplateNames;

            listSimpleAction = new ArrayList<>();
        }
    }

    @Nullable
    public static void exportPackageTemplate(Project project, String pathDir, PackageTemplateWrapper ptWrapper, HashSet<String> hsFileTemplateNames) {
        Context ctx = new Context(project, pathDir, ptWrapper, hsFileTemplateNames);

        if (!FileTemplateHelper.isCurrentSchemeValid(ctx.project, ptWrapper.getPackageTemplate().getFileTemplateSource())) {
            return;
        }

        File rootDir = new File(pathDir + File.separator + ptWrapper.getPackageTemplate().getName());
        if (rootDir.exists()) {
            askAboutFileConflict(ctx, rootDir);
        } else {
            onExportConfirmed(ctx, rootDir);
        }
    }

    private static void askAboutFileConflict(Context ctx, File rootDir) {
        new SkipableNonCancelDialog(ctx.project,
                String.format(Localizer.get("question.OverwriteArg"), rootDir.getName()),
                Localizer.get("warning.PackageTemplateAlreadyExist"),
                Localizer.get("action.Overwrite")
        ) {
            @Override
            public void onOk() {
                ctx.listSimpleAction.add(new TransparentDeleteDirectoryAction(rootDir));
                onExportConfirmed(ctx, rootDir);
            }
        };
    }

    private static void onExportConfirmed(Context ctx, File rootDir) {
        // Write PackageTemplate
        String ptJson = GsonFactory.getInstance().toJson(ctx.ptWrapper.getPackageTemplate(), PackageTemplate.class);

        ctx.listSimpleAction.add(new TransparentCreateSimpleDirectoryAction(rootDir));

        File ptFile = new File(String.format("%s%s%s.%s",
                rootDir.getPath(),
                File.separator,
                ctx.ptWrapper.getPackageTemplate().getName(),
                Const.PACKAGE_TEMPLATES_EXTENSION
        ));

        ctx.listSimpleAction.add(new TransparentCreateFileAction(ptFile, ptJson));

        //Create Dir for FileTemplates
        File fileTemplateParentDir = new File(rootDir.getPath() + File.separator + FileTemplatesScheme.TEMPLATES_DIR);
        ctx.listSimpleAction.add(new TransparentCreateSimpleDirectoryAction(fileTemplateParentDir));


        // Write FileTemplates
        File[] userProjectFiles = new File[0];
        if (!FileTemplateHelper.isDefaultScheme(ctx.project)) {
            userProjectFiles = new File(FileTemplateHelper.getFileTemplatesDirPath(ctx.project) + Const.DIR_USER).listFiles();
        }

        File[] userDefaultFiles = new File(FileTemplateHelper.getFileTemplatesDefaultDirPath() + Const.DIR_USER).listFiles();
        File[] internalFiles = new File(FileTemplateHelper.getFileTemplatesDirPath(ctx.project) + Const.DIR_INTERNAL).listFiles();
        File[] j2eeFiles = new File(FileTemplateHelper.getFileTemplatesDirPath(ctx.project) + Const.DIR_J2EE).listFiles();

        for (String name : ctx.hsFileTemplateNames) {
            File file = null;

            switch (ctx.ptWrapper.getPackageTemplate().getFileTemplateSource()) {
                case DEFAULT_ONLY:
                    file = findInArrays(name, userDefaultFiles, internalFiles, j2eeFiles);
                    break;
                case PROJECT_ONLY:
                    file = findInArrays(name, userProjectFiles, internalFiles, j2eeFiles);
                    break;
                case PROJECT_PRIORITY:
                    file = findInArrays(name, userProjectFiles, internalFiles, j2eeFiles, userDefaultFiles);
                    break;
                case DEFAULT_PRIORITY:
                    file = findInArrays(name, userDefaultFiles, internalFiles, j2eeFiles, userProjectFiles);
                    break;
            }

            if (file != null) {
                ctx.listSimpleAction.add(new TransparentCopyFileAction(file,
                        Paths.get(rootDir.getPath()
                                + File.separator
                                + FileTemplatesScheme.TEMPLATES_DIR
                                + File.separator
                                + file.getName()).toFile()
                ));
            } else {
                //todo FileTemplate not found
                Logger.log("FileTemplate not found: " + name);
            }
        }

        ActionRequest actionRequest = new ActionRequestBuilder()
                .setProject(ctx.project)
                .setActions(ctx.listSimpleAction)
                .setActionLabel("Export PackageTemplates")
                .setAccessPrivileges(AccessPrivileges.WRITE)
                .setConfirmationPolicy(UndoConfirmationPolicy.REQUEST_CONFIRMATION)
                .setUndoable(false)
                .build();

        ActionExecutor.runAsTransaction(actionRequest);
    }


    //=================================================================
    //  Utils
    //=================================================================
    public static File findInArrays(String name, File[]... arrays) {
        for (int arrayPos = 0; arrayPos < arrays.length; arrayPos++) {
            File[] files = arrays[arrayPos];

            for (int pos = 0; pos < files.length; pos++) {
                File file = files[pos];

                if (StringTools.getNameWithoutExtension(file.getName()).equals(name)) {
                    return file;
                }
            }
        }

        return null;
    }

}
