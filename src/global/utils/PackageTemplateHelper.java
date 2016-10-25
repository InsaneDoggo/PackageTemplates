package global.utils;

import com.intellij.openapi.application.PathManager;
import global.Const;
import global.models.PackageTemplate;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Arsen on 25.10.2016.
 */
public class PackageTemplateHelper {

    @NotNull
    public static String getRootDirPath() {
        return PathManager.getConfigPath() + "/" + Const.PACKAGE_TEMPLATES_DIR_NAME + "/";
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
        String json = FileReaderUtil.readFile(path);
        if (json != null && !json.isEmpty()) {
            return GsonFactory.getInstance().fromJson(json, PackageTemplate.class);
        }
        return null;
    }

    public static void setListPackageTemplate(ArrayList<PackageTemplate> listPackageTemplate) {
//        this.listPackageTemplate = listPackageTemplate;
    }

}
