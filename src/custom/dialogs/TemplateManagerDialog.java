package custom.dialogs;

import com.intellij.ide.fileTemplates.impl.AllFileTemplatesConfigurable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import models.InputBlock;
import org.jetbrains.annotations.Nullable;
import utils.InputManager;

import javax.swing.*;

/**
 * Created by Arsen on 21.06.2016.
 */
public abstract class TemplateManagerDialog extends DialogWrapper {

    public abstract void onFinish(String result);

    JPanel panel;
    AnActionEvent event;

    public TemplateManagerDialog(@Nullable AnActionEvent event, String title) {
        super(event.getProject());
        this.event = event;
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

        return true;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        panel = new JPanel();

        AllFileTemplatesConfigurable configurable = new AllFileTemplatesConfigurable(event.getProject());
        configurable.reset();
        panel.add(configurable.createComponent());

        return panel;
    }

}