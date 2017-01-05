package core.export;

import com.intellij.icons.AllIcons;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplatesScheme;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.DialogWrapperDialog;
import com.intellij.openapi.vcs.VcsShowConfirmationOption;
import com.intellij.util.ui.ConfirmationDialog;
import com.sun.jna.platform.FileUtils;
import core.state.models.StateModel;
import global.Const;
import global.models.PackageTemplate;
import global.utils.Logger;
import global.utils.StringTools;
import global.utils.factories.GsonFactory;
import global.utils.file.FileWriter;
import global.utils.i18n.Localizer;
import global.wrappers.PackageTemplateWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Arsen on 30.10.2016.
 */
public class ExportHelper {

    private static final String DIR_USER = "";
    private static final String DIR_INTERNAL = "internal";
    private static final String DIR_J2EE = "j2ee";

    @NotNull
    public static String getFileTemplatesDirPath() {
        return FileTemplatesScheme.DEFAULT.getTemplatesDir() + File.separator;
    }

    public static File getTemplate(String fileTemplateName) {
        FileTemplateManager ftm = FileTemplateManager.getDefaultInstance();

        // Custom
        FileTemplate result = ftm.getTemplate(fileTemplateName);
        // Internal
        if (result == null) {
            result = ftm.getInternalTemplate(fileTemplateName);
        }
        // J2EE
        if (result == null) {
            result = ftm.getJ2eeTemplate(fileTemplateName);
        }

        //todo impl
        return null;
    }

    private static String getRelativePath(String templateType) {
        //todo impl
        return null;
    }

    @Nullable
    public static void exportPackageTemplate(Project project, String pathDir, PackageTemplateWrapper ptWrapper, HashSet<String> hsFileTemplateNames) {
        Logger.log("exportPackageTemplate ");

        // Make Container Dir
        File rootDir = new File(pathDir + File.separator + ptWrapper.getPackageTemplate().getName());
        if (rootDir.exists()) {
            boolean doOverwrite = new ConfirmationDialog(project,
                    Localizer.get("question.Overwrite"),
                    Localizer.get("warning.PackageTemplateAlreadyExist"),
                    AllIcons.General.QuestionDialog,
                    VcsShowConfirmationOption.STATIC_SHOW_CONFIRMATION,
                    Localizer.get("action.Overwrite"),
                    Localizer.get("action.Cancel")
            ).showAndGet();

            if (!doOverwrite) {
                return;
            }

            Logger.log("Removing Directory!");
            FileWriter.removeDirectoryExceptRoot(rootDir);
        }


        // Write PackageTemplate
        String ptJson = GsonFactory.getInstance().toJson(ptWrapper.getPackageTemplate(), PackageTemplate.class);
        boolean isWritePtSuccess = FileWriter.writeStringToFile(ptJson,
                String.format("%s%s%s.%s",
                        rootDir.getPath(),
                        File.separator,
                        ptWrapper.getPackageTemplate().getName(),
                        Const.PACKAGE_TEMPLATES_EXTENSION
                )
        );
        if (!isWritePtSuccess) {
            //todo Writing ptJson failed, revert changes?
            throw new RuntimeException("Writing ptJson failed");
        }

        // Write FileTemplates
        File[] userFiles = new File(getFileTemplatesDirPath() + DIR_USER).listFiles();
        File[] internalFiles = new File(getFileTemplatesDirPath() + DIR_INTERNAL).listFiles();
        File[] j2eeFiles = new File(getFileTemplatesDirPath() + DIR_J2EE).listFiles();

        ArrayList<File> filesToCopy = new ArrayList<>();
        for (String name : hsFileTemplateNames) {
            File file = findInArray(name, userFiles, internalFiles, j2eeFiles);
            if (file != null) {
                filesToCopy.add(file);
            } else {
                //todo FileTemplate not found
                Logger.log("FileTemplate not found: " + name);
            }
        }

        for (File file : filesToCopy) {
            boolean isSuccess = FileWriter.copyFile(file.toPath(), Paths.get(rootDir.getPath()
                    + File.separator + FileTemplatesScheme.TEMPLATES_DIR
                    + File.separator + file.getName()));
            if (!isSuccess) {
                //todo FileTemplate not written
                Logger.log("FileTemplate not written");
            }
        }

    }

    private static File findInArray(String name, File[]... arrays) {
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
