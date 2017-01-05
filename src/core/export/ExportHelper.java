package core.export;

import com.intellij.ide.fileTemplates.FileTemplatesScheme;
import com.intellij.openapi.project.Project;
import global.Const;
import global.dialogs.SimpleConfirmationDialog;
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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Arsen on 30.10.2016.
 */
public class ExportHelper {

    @NotNull
    public static String getFileTemplatesDirPath() {
        return FileTemplatesScheme.DEFAULT.getTemplatesDir() + File.separator;
    }

    @Nullable
    public static void exportPackageTemplate(Project project, String pathDir, PackageTemplateWrapper ptWrapper, HashSet<String> hsFileTemplateNames) {
        Logger.log("exportPackageTemplate ");

        // Make Container Dir
        File rootDir = new File(pathDir + File.separator + ptWrapper.getPackageTemplate().getName());
        if (rootDir.exists()) {
            askUserToOverwriteTemplate(project, ptWrapper, hsFileTemplateNames, rootDir);
        } else {
            onExportConfirmed(ptWrapper, hsFileTemplateNames, rootDir);
        }
    }

    private static void askUserToOverwriteTemplate(final Project project, final PackageTemplateWrapper ptWrapper, final HashSet<String> hsFileTemplateNames, final File rootDir) {
        new SimpleConfirmationDialog(project, Localizer.get("question.Overwrite"), Localizer.get("warning.PackageTemplateAlreadyExist"),
                Localizer.get("action.Overwrite"), Localizer.get("action.Cancel")) {
            @Override
            public void onOk() {
                FileWriter.removeDirectoryExceptRoot(rootDir);
                onExportConfirmed(ptWrapper, hsFileTemplateNames, rootDir);
            }
        };
    }

    private static void onExportConfirmed(PackageTemplateWrapper ptWrapper, HashSet<String> hsFileTemplateNames, File rootDir) {
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
        if (!isWritePtSuccess)

        {
            //todo Writing ptJson failed, revert changes?
            throw new RuntimeException("Writing ptJson failed");
        }

        // Write FileTemplates
        File[] userFiles = new File(getFileTemplatesDirPath() + Const.DIR_USER).listFiles();
        File[] internalFiles = new File(getFileTemplatesDirPath() + Const.DIR_INTERNAL).listFiles();
        File[] j2eeFiles = new File(getFileTemplatesDirPath() + Const.DIR_J2EE).listFiles();

        ArrayList<File> filesToCopy = new ArrayList<>();
        for (
                String name : hsFileTemplateNames)

        {
            File file = findInArray(name, userFiles, internalFiles, j2eeFiles);
            if (file != null) {
                filesToCopy.add(file);
            } else {
                //todo FileTemplate not found
                Logger.log("FileTemplate not found: " + name);
            }
        }

        for (
                File file : filesToCopy)

        {
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
