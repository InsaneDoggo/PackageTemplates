package core.actions.ideSupported;

import com.intellij.openapi.command.undo.DocumentReference;
import com.intellij.openapi.command.undo.UndoableAction;
import com.intellij.openapi.command.undo.UnexpectedUndoException;
import global.utils.NotificationHelper;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Arsen on 28.07.2017.
 */
public class CopyFileSupAction implements UndoableAction {

    @Override
    public void undo() throws UnexpectedUndoException {
        NotificationHelper.info("CopyFileSupAction", "undo!");
    }

    @Override
    public void redo() throws UnexpectedUndoException {
        NotificationHelper.info("CopyFileSupAction", "redo!");
    }

    @Nullable
    @Override
    public DocumentReference[] getAffectedDocuments() {
        return null;
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
