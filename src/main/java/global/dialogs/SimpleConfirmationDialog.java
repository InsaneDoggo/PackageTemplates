package global.dialogs;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.VcsShowConfirmationOption;
import com.intellij.util.ui.ConfirmationDialog;
import global.utils.Logger;

/**
 * Created by Arsen on 05.01.2017.
 */
public abstract class SimpleConfirmationDialog extends ConfirmationDialog {

    public SimpleConfirmationDialog(Project project, String message, String title, String okActionName, String cancelActionName) {
        super(project, message, title, AllIcons.General.QuestionDialog, VcsShowConfirmationOption.STATIC_SHOW_CONFIRMATION, okActionName, cancelActionName);
        initDialog();
    }

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
        }
    }

    public abstract void onOk();

    public void onCancel() {
        //nothing
    }

}
