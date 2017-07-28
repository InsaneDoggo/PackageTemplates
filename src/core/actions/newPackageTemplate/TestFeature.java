package core.actions.newPackageTemplate;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.impl.UndoManagerImpl;
import com.intellij.openapi.command.undo.UndoManager;
import com.intellij.openapi.command.undo.UnexpectedUndoException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import core.actions.ideSupported.CopyFileSupAction;

/**
 * Created by Arsen on 28.07.2017.
 */
public class TestFeature {

    private Project project;
    private AnActionEvent event;
    private VirtualFile virtualFile;

    public TestFeature(Project project, AnActionEvent event, VirtualFile virtualFile) {
        this.project = project;
        this.event = event;
        this.virtualFile = virtualFile;

        runTest();
    }


    //=================================================================
    //  Feature
    //=================================================================
    private void runTest() {
        String commandId = "testId";
        CommandProcessor.getInstance().executeCommand(project, this::execute, commandId, commandId);
    }

    private void execute() {
        CopyFileSupAction action = new CopyFileSupAction(project);
        try {
            action.redo();
        } catch (UnexpectedUndoException e) {
            e.printStackTrace();
        }
        getUndoManager().undoableActionPerformed(action);
        ApplicationManager.getApplication().invokeLater(() -> getUndoManager().undo(null));
    }

    private UndoManagerImpl getUndoManager() {
        if (project != null) {
            return (UndoManagerImpl) UndoManager.getInstance(project);
        }
        return (UndoManagerImpl) UndoManager.getGlobalInstance();
    }

}
