package core.actions.newPackageTemplate;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.undo.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Arsen on 25.10.2016.
 */
public class Tester {


    public Tester() {
        this.fooBar = new FooBar();
    }

    FooBar fooBar;
    Project project;

    public void runMockAction(Project project) {
        this.project = project;

        String oldProperty = fooBar.someProperty;
        fooBar.someProperty = "new value 123"; // action, which must support Undo

        // Create UndoAction (revert changes above)
        UndoableAction action = new UndoableAction() {
            @Override
            public void undo() throws UnexpectedUndoException {
                fooBar.someProperty = oldProperty;
            }

            @Override
            public void redo() throws UnexpectedUndoException {

            }

            @Nullable
            @Override
            public DocumentReference[] getAffectedDocuments() {
                return null;
            }

            @Override
            public boolean isGlobal() {
                return false;
            }
        };

        CommandProcessor.getInstance().executeCommand(project, () -> {
            UndoManager.getInstance(project).undoableActionPerformed(action);
        }, "Custom Undoable Action", null);

    }

//    FooBar fooBar; // My custom data to change
//
//    void blabla() {
//        String oldProperty = fooBar.someProperty;
//        fooBar.someProperty = "new value 123"; // action, which must support Undo
//
//        // Create UndoAction (revert changes above)
//        UndoableAction action = new BasicUndoableAction() {
//            @Override
//            public void undo() throws UnexpectedUndoException {
//                fooBar.someProperty = oldProperty;
//            }
//
//            @Override public void redo() throws UnexpectedUndoException {}
//        };
//
//        CommandProcessor.getInstance().executeCommand(project, () -> {
//            UndoManager.getInstance(project).undoableActionPerformed(action);
//        }, "Custom Undoable Action", null);
//    }

}
