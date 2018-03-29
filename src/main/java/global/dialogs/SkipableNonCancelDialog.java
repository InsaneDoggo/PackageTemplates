package global.dialogs;

import com.intellij.openapi.project.Project;
import global.utils.i18n.Localizer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by Arsen on 05.01.2017.
 */
public abstract class SkipableNonCancelDialog extends SimpleConfirmationDialog {

    public SkipableNonCancelDialog(Project project, String message, String title, String okActionName) {
        super(project, message, title, okActionName, "cancelActionName");
    }

    @Override
    protected void initDialog() {
        show();

        int exitCode = getExitCode();
        switch (exitCode) {
            case OK_EXIT_CODE:
                onOk();
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
        final Action okAction = getOKAction();
        assignMnemonic(getOkActionName(), okAction);

        return new Action[]{
                okAction,
                new DialogWrapperExitAction(Localizer.get("action.Skip"), SKIP_EXIT_CODE)
        };
    }
}
