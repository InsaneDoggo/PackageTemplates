package custom.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import models.InputBlock;
import org.jetbrains.annotations.Nullable;
import utils.InputManager;

import javax.swing.*;
import java.util.Map;
import java.util.Properties;

/**
 * Created by CeH9 on 14.06.2016.
 */
public abstract class NewPackageDialog extends DialogWrapper {

    public abstract void onFinish(String result);

    JPanel panel;
    InputManager inputManager;

    public NewPackageDialog(@Nullable Project project, String title, InputManager inputManager) {
        super(project);
        this.inputManager = inputManager;
        this.panel = inputManager.getPanel();
        init();
        setTitle(title);
        show();

        switch (getExitCode()) {
            case NewPackageDialog.OK_EXIT_CODE:
                inputManager.initGlobalProperties();
                saveVariablesFromDialog();
                inputManager.getPackageTemplate().replaceNameVariable(inputManager);
                onFinish("OK_EXIT_CODE");
                break;
            case NewPackageDialog.CANCEL_EXIT_CODE:
                onFinish("CANCEL_EXIT_CODE");
                break;
        }
    }

    private void saveVariablesFromDialog() {
        for (InputBlock block : inputManager.getListInputBlock()) {
            block.getElement().setName(block.getTfName().getText());

            if (!block.getElement().isDirectory()) {
                if (block.getPanelVariables() != null) {
                    Properties properties = new Properties();
                    properties = block.getPanelVariables().getProperties(properties);

                    for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                        block.getElement().getMapProperties().put((String) entry.getKey(), (String) entry.getValue());

                        //add GLOBALS
                        block.getElement().getMapProperties().putAll(inputManager.getMapGlobalProperties());
                    }
                }
            }
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
        return panel;
    }

}
