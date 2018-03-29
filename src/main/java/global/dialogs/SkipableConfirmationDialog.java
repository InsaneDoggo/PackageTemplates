package global.dialogs;

import com.intellij.openapi.project.Project;
import global.utils.Logger;
import global.utils.i18n.Localizer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by Arsen on 05.01.2017.
 */
public abstract class SkipableConfirmationDialog extends SimpleConfirmationDialog {

    public SkipableConfirmationDialog(Project project, String message, String title, String okActionName, String cancelActionName) {
        super(project, message, title, okActionName, cancelActionName);
    }

    @Override
    protected void initDialog() {
        show();

        int exitCode = getExitCode();
        switch (exitCode) {
            case OK_EXIT_CODE:
                onOk();
                break;
            case CANCEL_EXIT_CODE:
                onCancel();
                break;
            case SKIP_EXIT_CODE:
                onSkip();
                break;
        }
    }


    //=================================================================
    //  Skip action
    //=================================================================
    private final int SKIP_EXIT_CODE = 100;

    public void onSkip() {
        //nothing
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        Action[] actions = super.createActions();
        return new Action[]{
                actions[0],
                new DialogWrapperExitAction(Localizer.get("action.Skip"), SKIP_EXIT_CODE),
                actions[1]
        };
    }
}
