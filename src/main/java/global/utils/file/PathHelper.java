package global.utils.file;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.vfs.VirtualFile;
import global.Const;

import java.io.File;

/**
 * Created by Arsen on 16.03.2017.
 */
public class PathHelper {

    public static String toDirPath(VirtualFile file) {
        if (file.isDirectory()) {
            return file.getPath();
        } else {
            return file.getParent().getPath();
        }
    }

    public static String getBinaryFilesCacheDefaultDir() {
        return PathManager.getSystemPath()
                + File.separator + Const.CACHE_DIR_NAME
                + File.separator + Const.BINARY_FILES_CACHE_DIR_NAME;
    }

}
