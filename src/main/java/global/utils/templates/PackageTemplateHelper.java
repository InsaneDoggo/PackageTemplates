package global.utils.templates;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.FilePathsHelper;
import com.intellij.openapi.vfs.VirtualFile;
import core.exportTemplates.ExportHelper;
import core.importTemplates.ImportHelper;
import core.state.util.SaveUtil;
import core.writeRules.WriteRules;
import global.Const;
import global.models.Favourite;
import global.models.PackageTemplate;
import global.utils.Logger;
import global.utils.factories.GsonFactory;
import global.utils.factories.WrappersFactory;
import global.utils.file.FileReaderUtil;
import global.utils.file.FileWriter;
import global.visitors.CollectFileTemplatesVisitor;
import global.wrappers.PackageTemplateWrapper;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Arsen on 25.10.2016.
 */
public class PackageTemplateHelper {

    public static void savePackageTemplate(PackageTemplate packageTemplate, String path) {
        FileWriter.writeStringToFile(GsonFactory.getInstance().toJson(packageTemplate, PackageTemplate.class), path);
    }

    public static void exportPackageTemplate(Project project, PackageTemplate pt, String pathDir) {
        CollectFileTemplatesVisitor visitor = new CollectFileTemplatesVisitor();
        PackageTemplateWrapper ptWrapper = WrappersFactory.wrapPackageTemplate(project, pt, PackageTemplateWrapper.ViewMode.EDIT);
        visitor.visit(ptWrapper.getRootElement());

        ExportHelper.exportPackageTemplate(project, pathDir, ptWrapper, visitor.getHsFileTemplateNames());
    }

    public static void importPackageTemplate(Project project, ArrayList<PackageTemplateWrapper> ptWrappers, HashSet<String> hsFileTemplateNames, ArrayList<File> selectedFiles, WriteRules writeRules) {
        ImportHelper.importPackageTemplate(project, ptWrappers, hsFileTemplateNames, selectedFiles, writeRules);
    }


    //=================================================================
    //  Getters
    //=================================================================

    /**
     * @return коллекцию шаблонов из Favourites
     */
    public static ArrayList<PackageTemplate> getListPackageTemplate() {
        ArrayList<Favourite> favourites = SaveUtil.reader().getListFavourite();
        ArrayList<Favourite> favouritesToDelete = new ArrayList<>();
        ArrayList<PackageTemplate> templates = new ArrayList<>();

        for (Favourite item : favourites) {
            PackageTemplate pt = PackageTemplateHelper.getPackageTemplate(item.getPath());
            if (pt != null) {
                templates.add(pt);
            } else {
                favouritesToDelete.add(item);
            }
        }

        for (Favourite item : favouritesToDelete) {
            //todo notify about removeFavourite
            SaveUtil.editor().removeFavourite(item);
        }
        SaveUtil.editor().save();

        return templates;
    }

    public static PackageTemplate getPackageTemplateByRelativePath(String relativePath) {
        return getPackageTemplate(String.format("%s%s.%s", getRootDirPath(), relativePath, Const.PACKAGE_TEMPLATES_EXTENSION));
    }

    public static PackageTemplate getPackageTemplate(String path) {
        return getPackageTemplate(new File(path));
    }

    public static PackageTemplate getPackageTemplate(File file) {
        String json = FileReaderUtil.readFile(file);
        if (json == null || json.isEmpty()) {
            return null;
        }

        try {
            PackageTemplate pt = GsonFactory.getInstance().fromJson(json, PackageTemplate.class);
            pt.setName(file.getName().replace("." + Const.PACKAGE_TEMPLATES_EXTENSION, ""));
            preventNPE(pt);
            return pt;
        } catch (Exception e) {
            Logger.log(e.getMessage());
//            Logger.printStack(e);
        }

        return null;
    }

    private static void preventNPE(PackageTemplate template) {
        // 0.3.0+
        if (template.getListTextInjection() == null) template.setListTextInjection(new ArrayList<>());

        // 0.4.0+
        if (template.getFileTemplateSource() == null)
            template.setFileTemplateSource(FileTemplateSource.getDefault());
    }


    //=================================================================
    //  Utils
    //=================================================================


    //=================================================================
    //  Paths
    //=================================================================
    @NotNull
    public static String getRootDirPath() {
        return Paths.get(PathManager.getConfigPath()).toString() + File.separator + Const.PACKAGE_TEMPLATES_DIR_NAME + File.separator;
    }

    public static String getProjectRootDirPath(Project project) {
        String templatesDirPath = FileTemplateHelper.getFileTemplatesDirPath(project);
        File file = new File(templatesDirPath);
        if (!file.exists()) {
            return null;
        }

        return file.getParent() + File.separator + Const.PACKAGE_TEMPLATES_DIR_NAME + File.separator;
    }

    public static File getRootDir() {
        File file = new File(getRootDirPath());
        FileWriter.createDirectories(file.toPath());
        return file;
    }

    private static String getRelativePath(File file) {
        String path = Paths.get(file.getPath()).toString();
        return path.replace(PackageTemplateHelper.getRootDirPath(), "");
    }

    public static String createNameFromPath(File file) {
        String path = getRelativePath(file);
        path = path.replaceAll(Pattern.quote(File.separator), Matcher.quoteReplacement("."));
        return path.replaceAll(Pattern.quote("-"), Matcher.quoteReplacement("_"));
    }

}
