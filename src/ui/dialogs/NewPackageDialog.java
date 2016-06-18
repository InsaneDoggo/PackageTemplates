package ui.dialogs;

import com.intellij.ide.fileTemplates.actions.AttributesDefaults;
import com.intellij.ide.fileTemplates.ui.CreateFromTemplatePanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.panels.HorizontalBox;
import com.intellij.util.ui.GridBag;
import models.InputBlock;
import org.jetbrains.annotations.Nullable;
import utils.InputManager;

import javax.swing.*;
import java.awt.*;

import static utils.UIMaker.DEFAULT_PADDING;

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
                onFinish("OK_EXIT_CODE");
                break;
            case NewPackageDialog.CANCEL_EXIT_CODE:
                onFinish("CANCEL_EXIT_CODE");
                break;
        }
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
//        AttributesDefaults attributesDefaults = new AttributesDefaults();
//        attributesDefaults.add("TEST_BLA", "bla223");
//        attributesDefaults.add("TEST_PROST", "prost223");
//        CreateFromTemplatePanel myAttrPanel = new CreateFromTemplatePanel(new String[]{"TEST_BLA", "TEST_PROST"}, true, attributesDefaults);

//        panel.add(myAttrPanel.getComponent());

        return panel;
    }

}
