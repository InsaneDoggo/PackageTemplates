package core.actions.custom.undoable;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.openapi.command.undo.UndoConstants;
import com.intellij.openapi.command.undo.UnexpectedUndoException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Clock;
import com.intellij.openapi.vfs.VirtualFile;
import core.actions.custom.base.SimpleUndoableAction;
import global.utils.Logger;
import global.utils.file.FileWriter;
import global.utils.file.PathHelper;
import global.utils.file.VFSHelper;
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
            restoreDataAfterPreventing();
        }

        if (!FileWriter.removeFile(new File(cacheFileName))) {
            Logger.log("Clear cache fail");
        } else {
            clearCacheKey();
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

        //Prevent Event
        preventAdditionalAction();

        if (!FileWriter.removeFile(fileToDelete)) {
            Logger.log("redo fail");
        } else {
            refreshDir();
        }
    }

    private void refreshDir() {
        VFSHelper.refreshVirtualFile(fileToDelete.getParentFile().getPath());
        ProjectView.getInstance(project).refresh();
    }


    //=================================================================
    //  ForceUndo Event
    //=================================================================
    private Boolean forceUndoHolder;

    // When non-binaryFile deleted IDE register additional non undoable action
    // which cause "Following files have changes that cannot be undone:".
    private void preventAdditionalAction() {
        VirtualFile virtualFile = VFSHelper.findVirtualFileByPath(fileToDelete.getPath());
        if (virtualFile != null) {
            forceUndoHolder = virtualFile.getUserData(UndoConstants.FORCE_RECORD_UNDO);
            virtualFile.putUserData(UndoConstants.FORCE_RECORD_UNDO, Boolean.TRUE);
        }
    }

    private void restoreDataAfterPreventing() {
        VirtualFile virtualFile = VFSHelper.findVirtualFileByPath(fileToDelete.getPath());
        if (virtualFile != null) {
            virtualFile.putUserData(UndoConstants.FORCE_RECORD_UNDO, forceUndoHolder);
        }
    }


    //=================================================================
    //  Utils
    //=================================================================
    @NotNull
    private File getCacheSystemDir() {
        File cacheDir = new File(PathHelper.getBinaryFilesCacheDefaultDir());
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

    private void clearCacheKey() {
        cacheKey = null;
    }
}
