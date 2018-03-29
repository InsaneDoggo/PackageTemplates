package global.utils;

import com.intellij.openapi.application.PathManager;
import core.library.LibraryManager;
import core.library.models.Alias;
import core.library.models.AliasType;
import core.library.models.LibRequest;
import core.library.models.lib.BinaryFileLibModel;
import global.Const;
import global.utils.file.FileWriter;

import java.io.File;
import java.nio.file.Paths;

public class BinaryFileHelper {

    public static String getBinaryFileDirPath() {
        return Paths.get(PathManager.getConfigPath()).toString() + File.separator + Const.BINARY_FILES_DIR_NAME + File.separator;
    }

    public static File getOrCreateRootDir() {
        File dir = new File(getBinaryFileDirPath());
        if (!dir.exists()) {
            FileWriter.createDirectories(dir.toPath());
        }

        return dir;
    }

    public static File getBinaryFile(Alias alias) {
        File binaryFile = getBinaryFile(LibraryManager.getInstance(), alias);
        LibraryManager.removeInstance();

        return binaryFile;
    }

    public static File getBinaryFile(LibraryManager libraryManager, Alias alias) {
        if (alias == null) {
            Logger.log("getBinaryFile alias is NULL");
            return null;
        }

        if (alias.getAliasType() != AliasType.BINARY_FILE) {
            Logger.log("getBinaryFile AliasType is not BINARY_FILE");
            return null;
        }

        LibRequest<BinaryFileLibModel> request = libraryManager.get(alias);

        if (request.getValue() == null) {
            Logger.log("getBinaryFile request.getValue() is NULL");
            return null;
        }

        return new File(request.getValue().getPath());
    }

}
