package global.utils.file;

import global.Const;
import global.utils.templates.PackageTemplateHelper;

import java.io.File;
import java.nio.file.Paths;

/**
 * Created by Arsen on 28.10.2016.
 */
public class FileValidator {

    public static boolean isTemplateFileValid(File file) {
        return file.exists() && !file.isDirectory() && file.getPath().endsWith(Const.PACKAGE_TEMPLATES_EXTENSION);
    }

    public static boolean isValidTemplatePath(String path) {
        return path != null
                && !path.isEmpty()
                && isTemplateFileValid(new File(path));
    }

    public static boolean isUnderConfigDir(String path) {
        String pathRoot = Paths.get(PackageTemplateHelper.getRootDirPath()).toAbsolutePath().toString();
        String pathToCheck = Paths.get(path).toAbsolutePath().toString();

        return pathToCheck.startsWith(pathRoot);
    }
}
