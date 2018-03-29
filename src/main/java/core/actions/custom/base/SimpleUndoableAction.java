package core.actions.custom.base;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.command.impl.UndoManagerImpl;
import com.intellij.openapi.command.undo.DocumentReference;
import com.intellij.openapi.command.undo.UndoManager;
import com.intellij.openapi.command.undo.UndoableAction;
import com.intellij.openapi.command.undo.UnexpectedUndoException;
import com.intellij.openapi.project.Project;
import core.report.ReportHelper;
import core.report.enums.ExecutionState;
import global.utils.Logger;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Arsen on 28.07.2017.
 */
public abstract class SimpleUndoableAction extends SimpleAction implements UndoableAction {

    protected Project project;

    public SimpleUndoableAction(Project project) {
        this.project = project;
    }

    private UndoManagerImpl getUndoManager() {
        if (project != null) {
            return (UndoManagerImpl) UndoManager.getInstance(project);
        }
        return (UndoManagerImpl) UndoManager.getGlobalInstance();
    }


    //=================================================================
    //  Impl stuff
    //=================================================================
    @Override
    protected void doRun() {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                redo();
                getUndoManager().undoableActionPerformed(this);
            } catch (UnexpectedUndoException e) {
                Logger.logAndPrintStack("SimpleUndoableAction " + e.getMessage(), e);
                ReportHelper.setState(ExecutionState.FAILED);
            }
        });

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
