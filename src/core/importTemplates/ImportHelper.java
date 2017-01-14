package core.importTemplates;

import com.intellij.ide.fileTemplates.FileTemplatesScheme;
import com.intellij.openapi.project.Project;
import core.actions.executor.AccessPrivileges;
import core.actions.executor.ActionExecutor;
import core.actions.custom.SimpleAction;
import core.export.ExportHelper;
import core.importTemplates.models.CopyFileAction;
import global.utils.Logger;
import global.utils.StringTools;
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

    public static void importPackageTemplate(Project project, ArrayList<PackageTemplateWrapper> ptWrappers, HashSet<String> requiredTemplateNames, ArrayList<File> selectedFiles) {
        ArrayList<File> availableFileTemplates = new ArrayList<>();
        //check all src
        if (!isResourcesAvailable(requiredTemplateNames, selectedFiles, availableFileTemplates)) {
            //todo show err msg
            Logger.log("import, isResourcesAvailable false");
            return;
        }

        // Import
        ArrayList<SimpleAction> listSimpleAction = new ArrayList<>();
        File templatesDir = new File(ExportHelper.getFileTemplatesDirPath());

        //PackageTemplate Actions
        for (File file : selectedFiles) {
            listSimpleAction.add(new CopyFileAction(
                    file,
                    new File(PackageTemplateHelper.getRootDirPath() + file.getName())
            ));
        }

        // FileTemplate Actions
        for (String name : requiredTemplateNames) {
            for (File template : availableFileTemplates) {
                if (StringTools.getNameWithoutExtension(template.getName()).equals(name)) {
                    listSimpleAction.add(new CopyFileAction(
                            template,
                            new File(templatesDir.getPath() + File.separator + template.getName())
                    ));
                    break;
                }
            }
        }

        if (ActionExecutor.runAsTransaction(project, listSimpleAction, "Import PackageTemplates", AccessPrivileges.WRITE)) {
            Logger.log("importPackageTemplate  Done!");
        } else {
            //todo revert?
            Logger.log("importPackageTemplate  Fail!");
        }
    }

    /**
     * Проверка шаблонов на валидность.
     *
     * @param availableFileTemplates пустой массив, который будет наполнен всеми доступными FileTemplates.
     */
    private static boolean isResourcesAvailable(HashSet<String> requiredTemplateNames, ArrayList<File> selectedFiles, ArrayList<File> availableFileTemplates) {
        for (File file : selectedFiles) {
            // Verify FileTemp Dir
            if (!containsDirectoryByName(file, FileTemplatesScheme.TEMPLATES_DIR)) {
                Logger.log("no FileTemplates dir for " + file.getName());
                return false;
            }

            // collect FileTemplates
            File templatesDir = new File(file.getParentFile().getPath() + File.separator + FileTemplatesScheme.TEMPLATES_DIR);
            File[] templates = templatesDir.listFiles();

            if (templates != null) {
                Collections.addAll(availableFileTemplates, templates);
            }
        }

        for (String name : requiredTemplateNames) {
            boolean contains = false;
            for (File template : availableFileTemplates) {
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

        //Verify existence of PackageTemplate in IDE
//        File ptDir = PackageTemplateHelper.getRootDir();
//        for (File file : selectedFiles) {
//            File templateFile = new File(ptDir.getPath() + File.separator + file.getName());
//            if (templateFile.exists() && !templateFile.isDirectory()) {
//                //todo dlg ask to replace | remove | cancel
//                Logger.log("isResourcesAvailable PackageTemplate Already exists in IDE!");
//                if(!templateFile.delete()){
//                    Logger.log("can't delete " + templateFile.getName());
//                    return false;
//                }
//            }
//        }

        return true;
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
