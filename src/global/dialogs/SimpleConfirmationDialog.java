package global.dialogs;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.VcsShowConfirmationOption;
import com.intellij.util.ui.ConfirmationDialog;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Arsen on 05.01.2017.
 */
public abstract class SimpleConfirmationDialog extends ConfirmationDialog {

    public SimpleConfirmationDialog(Project project, String message, String title) {
        super(project, message, title, AllIcons.General.QuestionDialog, VcsShowConfirmationOption.STATIC_SHOW_CONFIRMATION);
        initDialog();
    }

    public SimpleConfirmationDialog(Project project, String message, String title, @Nullable String okActionName, @Nullable String cancelActionName) {
        super(project, message, title, AllIcons.General.QuestionDialog, VcsShowConfirmationOption.STATIC_SHOW_CONFIRMATION, okActionName, cancelActionName);
        initDialog();
    }

    public abstract void onOk();

    public void onCancel() {
        //nothing
    }

    private void initDialog() {
        boolean isSuccess = showAndGet();
        if (isSuccess) {
            onOk();
        } else {
            onCancel();
        }
    }

}
