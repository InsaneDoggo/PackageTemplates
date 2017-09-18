package core.library;

import com.intellij.openapi.application.PathManager;
import core.library.models.Alias;
import core.library.models.collections.LibCollection;
import core.library.models.collections.LibCollectionBinaryFile;
import core.library.models.collections.LibCollectionScript;
import core.library.models.LibRequest;
import core.library.models.lib.BinaryFileLibModel;
import global.Const;
import global.utils.Logger;
import global.utils.factories.GsonFactory;
import global.utils.file.FileReaderUtil;
import global.utils.file.FileWriter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Paths;

public class LibraryManager {

    private static LibraryManager instance;

    public static LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }

        return instance;
    }

    public static void removeInstance() {
        instance = null;
    }


    //=================================================================
    //  API
    //=================================================================
    public LibRequest get(Alias alias) {
        LibRequest request = new LibRequest<>(alias);

        switch (request.getAlias().getAliasType()) {
            case BINARY_FILE:
                initBinaryFileCollection();
                collectionBinaryFile.get(request);
                break;
            case SCRIPT:
                initScriptCollection();
                collectionScript.get(request);
                break;
        }

        return request;
    }

    public LibraryManager put(LibRequest request) {
        switch (request.getAlias().getAliasType()) {
            case BINARY_FILE:
                initBinaryFileCollection();
                collectionBinaryFile.put(request);
                break;
            case SCRIPT:
                initScriptCollection();
                collectionScript.put(request);
                break;
        }

        return this;
    }

    public void saveAllCollections() {
        if (collectionBinaryFile != null) {
            saveLibCollection(BINARY_FILE, collectionBinaryFile, LibCollectionBinaryFile.class);
        }

        if (collectionScript != null) {
            saveLibCollection(SCRIPT, collectionScript, LibCollectionScript.class);
        }
    }


    //=================================================================
    //  Collections
    //=================================================================
    private LibCollectionBinaryFile collectionBinaryFile;
    private LibCollectionScript collectionScript;

    private final String BINARY_FILE = "library_binary_file.json";
    private final String SCRIPT = "library_script.json";

    private <T> T getLibCollection(String fullName, Class<T> type) {
        createRootDirIfNotExists();

        File collectionFile = new File(getLibrariesPath() + fullName);
        if (!collectionFile.exists()) {
            return null;
        }

        String json = FileReaderUtil.readFile(collectionFile);
        if (json == null || json.isEmpty()) {
            return null;
        }

        try {
            return GsonFactory.getInstance().fromJson(json, type);
        } catch (Exception e) {
            Logger.log(e.getMessage());
        }

        return null;
    }

    private <T> void saveLibCollection(String fullName, LibCollection collection, Class<T> type) {
        createRootDirIfNotExists();

        File collectionFile = new File(getLibrariesPath() + fullName);
        String json = GsonFactory.getInstance().toJson(collection, LibCollection.class);

        FileWriter.writeStringToFile(json, collectionFile);
    }

    private void initBinaryFileCollection() {
        if (collectionBinaryFile != null) {
            return;
        }

        collectionBinaryFile = getLibCollection(BINARY_FILE, LibCollectionBinaryFile.class);
        if (collectionBinaryFile == null) {
            collectionBinaryFile = new LibCollectionBinaryFile();
        }
    }

    private void initScriptCollection() {
        if (collectionScript != null) {
            return;
        }

        collectionScript = getLibCollection(BINARY_FILE, LibCollectionScript.class);
        if (collectionScript == null) {
            collectionScript = new LibCollectionScript();
        }
    }


    //=================================================================
    //  Util
    //=================================================================
    @NotNull
    private String getLibrariesPath() {
        return Paths.get(PathManager.getConfigPath()).toString() + File.separator + Const.LIBRARIES_DIR_NAME + File.separator;
    }

    private void createRootDirIfNotExists() {
        File rootDir = new File(getLibrariesPath());
        if (!rootDir.exists()) {
            FileWriter.createDirectories(rootDir.toPath());
        }
    }

}
