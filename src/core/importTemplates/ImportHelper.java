package core.importTemplates;

import com.intellij.ide.fileTemplates.FileTemplatesScheme;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import global.models.PackageTemplate;
import global.utils.factories.WrappersFactory;
import global.utils.templates.PackageTemplateHelper;
import global.visitors.CollectFileTemplatesVisitor;
import global.wrappers.PackageTemplateWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Arsen on 05.01.2017.
 */
public class ImportHelper {

    public static void importPackageTemplate(Project project, ArrayList<PackageTemplateWrapper> ptWrappers, HashSet<String> hsFileTemplateNames, ArrayList<File> selectedFiles) {
//        check all src
//        if (!isResourcesAvailable()) {
//            //todo show err msg
//            return;
//        }


    }

    private static boolean isResourcesAvailable(PackageTemplate pt, File file) {
        if (!containsDirectoryByName(file, FileTemplatesScheme.TEMPLATES_DIR)) {
            return false;
        }

        File templatesDir = new File(file.getParentFile().getPath() + File.separator + FileTemplatesScheme.TEMPLATES_DIR);
        File[] templates = templatesDir.listFiles();

        return true;
    }

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
