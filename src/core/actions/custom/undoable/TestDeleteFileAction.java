package core.actions.custom.undoable;

import com.intellij.openapi.command.undo.UnexpectedUndoException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import core.actions.custom.base.SimpleUndoableAction;
import core.actions.custom.interfaces.IHasWriteRules;
import core.search.SearchAction;
import core.search.SearchEngine;
import core.writeRules.WriteRules;
import global.models.BaseElement;
import global.models.BinaryFile;
import global.utils.Logger;
import global.utils.file.FileWriter;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Arsen on 09.01.2017.
 */
public class TestDeleteFileAction extends SimpleUndoableAction {

    private File fileToDelete;

    public TestDeleteFileAction(Project project, File fileToDelete) {
        super(project);
        this.fileToDelete = fileToDelete;
    }

    @Override
    public void undo() throws UnexpectedUndoException {

    }

    @Override
    public void redo() throws UnexpectedUndoException {
        if (!fileToDelete.exists()) {
            return;
        }

        VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(fileToDelete.getPath());
        if (virtualFile == null) {
            Logger.log("redo virtualFile is NULL");
            return;
        }

        if (!FileWriter.removeFile(fileToDelete)) {
            Logger.log("redo fail");
        }
    }

}
