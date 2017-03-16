package global.utils.file;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by Arsen on 16.03.2017.
 */
public class PathHelper {

    public static String toDirPath(VirtualFile file) {
        if(file.isDirectory()){
            return file.getPath();
        } else {
            return file.getParent().getPath();
        }
    }

}
