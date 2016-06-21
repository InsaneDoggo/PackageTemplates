package custom.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import models.InputBlock;
import org.jetbrains.annotations.Nullable;
import utils.InputManager;

import javax.swing.*;

/**
 * Created by Arsen on 21.06.2016.
 */
public abstract class TamplateManagerDialog extends DialogWrapper {

    public abstract void onFinish(String result);

    JPanel panel;
    InputManager inputManager;

    public TamplateManagerDialog(@Nullable Project project, String title) {
        super(project);
        this.panel = inputManager.getPanel();
        init();
        setTitle(title);
    }

    @Override
    public void show() {
        super.show();

        switch (getExitCode()) {
            case NewPackageDialog.OK_EXIT_CODE:
                onFinish("OK_EXIT_CODE");
                break;
            case NewPackageDialog.CANCEL_EXIT_CODE:
                onFinish("CANCEL_EXIT_CODE");
                break;
        }
    }

    @Override
    protected void doOKAction() {
        if (isInputValid()) {
            super.doOKAction();
        } else {
            // TODO: 19.06.2016 print wrong input
        }
    }

    private boolean isInputValid() {
        // TODO: 19.06.2016 check input
        for (InputBlock block : inputManager.getListInputBlock()) {

        }
        return true;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return panel;
    }

}