package global.utils.file;

import global.Const;
import global.utils.templates.PackageTemplateHelper;

import java.io.File;

/**
 * Created by Arsen on 28.10.2016.
 */
public class FileValidator {

    public static boolean isTemplateFileValid(File file) {
        return !file.isDirectory() && file.getPath().endsWith(Const.PACKAGE_TEMPLATES_EXTENSION);
    }

    public static boolean isValidTemplatePath(String path) {
        return path != null
                && !path.isEmpty()
                && isTemplateFileValid(new File(path));
    }

    public static boolean isUnderConfigDir(String path) {
        return path.startsWith(PackageTemplateHelper.getRootDirPath());
    }
}
