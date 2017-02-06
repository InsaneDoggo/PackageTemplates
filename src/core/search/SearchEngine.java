package core.search;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import global.utils.Logger;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Arsen on 01.02.2017.
 */
public class SearchEngine {

    public static File find(File startDir, List<SearchAction> actions) {
        if (startDir == null || actions == null || actions.isEmpty()) {
            Logger.log("find fail: INVALID args");
            return null;
        }

        File curDir = startDir;

        for (SearchAction action : actions) {
            if (curDir == null) {
                return null;
            }

            if (!curDir.isDirectory()) {
                curDir = startDir.getParentFile();
            }

            switch (action.getActionType()) {
                case DIR_ABOVE:
                    curDir = getEngine().findDirAbove(curDir, action);
                    break;
                case DIR_BELOW:
                    curDir = getEngine().findDirBelow(curDir, action);
                    break;
                case FILE:
                    curDir = getEngine().findFile(curDir, action);
                    break;
            }
        }

        return curDir;
    }


    //=================================================================
    //  Search
    //=================================================================
    /**
     * Поиск директории среди родителей файла(recursive getParentFile).
     */
    @Nullable
    private File findDirAbove(@NotNull File dirFrom, SearchAction action) {
        File currentFile = dirFrom;
        int deepLimit = action.getDeepLimit();

        while (deepLimit > 0 || deepLimit == DEEP_LIMITLESS) {
            if (deepLimit != DEEP_LIMITLESS) {
                deepLimit--;
            }

            // Next File
            currentFile = currentFile.getParentFile();
            if (currentFile == null) {
                return null;
            }

            //Check Name
            if (isNameEquals(action, currentFile.getName())) {
                return currentFile;
            }
        }

        return null;
    }

    /**
     * Поиск директории среди чилдов(обход дерева).
     */
    @Nullable
    private File findDirBelow(@NotNull File dirFrom, SearchAction action) {
        return findDirectoryRecursively(action, dirFrom, action.getDeepLimit());
    }

    /**
     * Поиск файла среди чилдов(обход дерева).
     */
    @Nullable
    private File findFile(@NotNull File dirFrom, SearchAction action) {
        return findFileRecursively(action, dirFrom, action.getDeepLimit());
    }


    //=================================================================
    //  Utils
    //=================================================================
    public static final int DEEP_LIMITLESS = -1;
    private static SearchEngine engine;

    private static SearchEngine getEngine() {
        if (engine == null) {
            engine = new SearchEngine();
        }

        return engine;
    }

    private boolean isNameEquals(SearchAction action, String fileName) {
        if (action.isRegexp()) {
            Pattern p = Pattern.compile(action.getName());
            Matcher m = p.matcher(fileName);
            return m.matches();
        } else {
            return fileName.equals(action.getName());
        }
    }

    @Nullable
    private File findDirectoryRecursively(SearchAction action, File dirFrom, int deepLimit) {
        // SubDirs
        File[] dirs = dirFrom.listFiles(File::isDirectory);
        if (dirs == null) {
            return null;
        }

        // Scan children
        for (File dir : dirs) {
            if (isNameEquals(action, dir.getName())) {
                return dir;
            }
        }

        if (deepLimit == 0) {
            return null;
        }

        //Scan subDirs
        for (File dir : dirs) {
            int curDeepLimit = DEEP_LIMITLESS;
            if (deepLimit != DEEP_LIMITLESS) {
                curDeepLimit = deepLimit - 1;
            }

            File result = findDirectoryRecursively(action, dir, curDeepLimit);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    @Nullable
    private File findFileRecursively(SearchAction action, File dirFrom, int deepLimit) {
        // Scan children
        File[] files = dirFrom.listFiles((file) -> !file.isDirectory());
        if (files != null) {
            for (File file : files) {
                if (isNameEquals(action, file.getName())) {
                    return file;
                }
            }
        }

        File[] dirs = dirFrom.listFiles(File::isDirectory);
        if (deepLimit == 0 || dirs == null) {
            return null;
        }

        //Scan subDir's children
        for (File dir : dirs) {
            int curDeepLimit = DEEP_LIMITLESS;
            if (deepLimit != DEEP_LIMITLESS) {
                curDeepLimit = deepLimit - 1;
            }

            File result = findFileRecursively(action, dir, curDeepLimit);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

}
