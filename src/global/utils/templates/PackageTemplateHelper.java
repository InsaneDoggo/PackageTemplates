package global.utils.templates;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiDirectory;
import core.export.ExportHelper;
import global.Const;
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
import java.util.concurrent.FutureTask;
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

        //todo check existing dir

        FutureTask<Void> futureTask = new FutureTask<>(() ->
                ApplicationManager.getApplication().runWriteAction((Computable<Void>) () -> {
                    ExportHelper.exportPackageTemplate(project, pathDir, ptWrapper, visitor.getHsFileTemplateNames());
                    return null;
                })
        );
        CommandProcessor.getInstance().executeCommand(project, futureTask,
                "Export '" + pt.getName() + "' PackageTemplate", null);
        try {
            futureTask.get();
        } catch (Exception ex) {
            Logger.log(ex.getMessage());
//            dirWrapper.setWriteException(ex);
//            dirWrapper.getPackageTemplateWrapper().getFailedElements().add(dirWrapper);
        }

    }


    //=================================================================
    //  Getters
    //=================================================================
    public static ArrayList<PackageTemplate> getListPackageTemplate() {
        //todo getListPackageTemplate
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
        if (json == null || json.isEmpty()) {
            return null;
        }

        try {
            PackageTemplate pt = GsonFactory.getInstance().fromJson(json, PackageTemplate.class);
            pt.setName(file.getName().replace("." + Const.PACKAGE_TEMPLATES_EXTENSION, ""));
            return pt;
        } catch (Exception e) {
            Logger.log(e.getMessage());
        }

        return null;
    }


    //=================================================================
    //  Paths
    //=================================================================
    @NotNull
    public static String getRootDirPath() {
        return Paths.get(PathManager.getConfigPath()).toString() + File.separator + Const.PACKAGE_TEMPLATES_DIR_NAME + File.separator;
    }

    private static File rootDir() {
        File file = new File(getRootDirPath());
        file.mkdirs();
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
