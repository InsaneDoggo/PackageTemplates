package core.actions.custom.undoable;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.command.undo.UnexpectedUndoException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Clock;
import core.actions.custom.base.SimpleUndoableAction;
import global.utils.Logger;
import global.utils.file.FileWriter;
import global.utils.file.PsiHelper;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Paths;

/**
 * Created by Arsen on 09.01.2017.
 */
public class TestDeleteFileAction extends SimpleUndoableAction {

    private File fileToDelete;
    private String cacheKey;

    public TestDeleteFileAction(Project project, File fileToDelete) {
        super(project);
        this.fileToDelete = fileToDelete;
    }

    @Override
    public void undo() throws UnexpectedUndoException {
        File cacheDir = getCacheSystemDir();
        String cacheFileName = cacheDir.getPath() + File.separator + getCacheFileName();

        if (!FileWriter.copyFile(Paths.get(cacheFileName), fileToDelete.toPath())) {
            Logger.log("restore from cache fail");
        } else {
            refreshDir();
        }

        if (!FileWriter.removeFile(new File(cacheFileName))) {
            Logger.log("Clear cache fail");
        } else {
            clearCahceKey();
        }
    }

    @Override
    public void redo() throws UnexpectedUndoException {
        if (!fileToDelete.exists()) {
            return;
        }

        File cacheDir = getCacheSystemDir();
        String cacheFileName = cacheDir.getPath() + File.separator + getCacheFileName();
        if (!FileWriter.copyFile(fileToDelete.toPath(), Paths.get(cacheFileName))) {
            Logger.log("cache fail");
        }

        if (!FileWriter.removeFile(fileToDelete)) {
            Logger.log("redo fail");
        } else {
            refreshDir();
        }
    }

    private void refreshDir() {
        PsiHelper.refreshVirtualFile(fileToDelete.getParentFile().getPath());
        ProjectView.getInstance(project).refresh();
    }

    //=================================================================
    //  Utils
    //=================================================================
    @NotNull
    private File getCacheSystemDir() {
        File cacheDir = new File(PathManager.getSystemPath() + File.separator + "PackageTemplatesCache");
        if (!cacheDir.exists()) {
            FileWriter.createDirectories(cacheDir.toPath());
        }

        return cacheDir;
    }

    @NotNull
    private String getCacheFileName() {
        if (cacheKey == null) {
            cacheKey = fileToDelete.getName() + "_cached_in_" + Clock.getTime();
        }

        return cacheKey;
    }

    private void clearCahceKey(){
        cacheKey = null;
    }
}
