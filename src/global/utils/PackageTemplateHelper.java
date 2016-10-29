package global.utils;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.Project;
import global.Const;
import global.models.PackageTemplate;
import global.visitors.CollectFileTemplatesVisitor;
import global.wrappers.PackageTemplateWrapper;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Arsen on 25.10.2016.
 */
public class PackageTemplateHelper {

    @NotNull
    public static String getRootDirPath() {
        return Paths.get(PathManager.getConfigPath()).toString() + File.separator + Const.PACKAGE_TEMPLATES_DIR_NAME + File.separator;
    }

    private static File rootDir() {
        File file = new File(getRootDirPath());
        file.mkdirs();
        return file;
    }

    public static ArrayList<PackageTemplate> getListPackageTemplate() {
        File root = rootDir();
        return new ArrayList<>();
    }

    public static PackageTemplate getPackageTemplateByRelativePath(String relativePath) {
        return getPackageTemplate(String.format("%s%s.%s", getRootDirPath(), relativePath, Const.PACKAGE_TEMPLATES_EXTENSION));
    }

    public static PackageTemplate getPackageTemplate(String path) {
        return getPackageTemplate(new File(path));
    }

    public static PackageTemplate getPackageTemplate(File file) {
        String json = FileReaderUtil.readFile(file);
        if (json != null && !json.isEmpty()) {
            try {
                PackageTemplate pt = GsonFactory.getInstance().fromJson(json, PackageTemplate.class);
                pt.setName(file.getName().replace("." + Const.PACKAGE_TEMPLATES_EXTENSION, ""));
                return pt;
            } catch (Exception e) {
                Logger.log(e.getMessage());
                return null;
            }
        }
        return null;
    }

    private static String getRelativePath(File file) {
        String path = Paths.get(file.getPath()).toString();
        return path.replace(PackageTemplateHelper.getRootDirPath(), "");
    }

    public static void savePackageTemplate(PackageTemplate packageTemplate, String path) {
        FileWriter.writeStringToFile(GsonFactory.getInstance().toJson(packageTemplate, PackageTemplate.class), path);
    }

    public static String createNameFromPath(File file) {
        String path = getRelativePath(file);
        path = path.replaceAll(Pattern.quote(File.separator), Matcher.quoteReplacement("."));
        return path.replaceAll(Pattern.quote("-"), Matcher.quoteReplacement("_"));
    }

    public static void exportPackageTemplate(Project project, PackageTemplate pt, String pathDir) {
        CollectFileTemplatesVisitor visitor = new CollectFileTemplatesVisitor();
        PackageTemplateWrapper ptWrapper = WrappersFactory.wrapPackageTemplate(project, pt, PackageTemplateWrapper.ViewMode.EDIT);
        visitor.visit(ptWrapper.getRootElement());


        System.out.println("jhj");
    }

}
