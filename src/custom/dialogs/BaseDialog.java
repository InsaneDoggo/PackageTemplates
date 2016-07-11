package custom.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by CeH9 on 08.07.2016.
 */
public abstract class BaseDialog extends DialogWrapper {

    Project project;
    JPanel panel;

    public BaseDialog(@Nullable Project project) {
        super(project);
        this.project = project;
        init();
    }

    abstract void preShow();
    abstract void onOKAction();
    abstract void onCancelAction();

    @Override
    public void show() {
        preShow();
        super.show();

        switch (getExitCode()) {
            case ConfigurePackageTemplatesDialog.OK_EXIT_CODE:
                onOKAction();
                break;
            case ConfigurePackageTemplatesDialog.CANCEL_EXIT_CODE:
                onCancelAction();
                break;
        }
    }

    @Override
    protected JComponent createCenterPanel() {
        panel = new JPanel();
        return panel;
    }

}
