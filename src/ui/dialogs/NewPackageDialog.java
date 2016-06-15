package ui.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.EditorTextField;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * Created by CeH9 on 14.06.2016.
 */
public abstract class NewPackageDialog extends DialogWrapper {

    JPanel panel;

    public NewPackageDialog(@Nullable Project project, String title, JPanel panel) {
        super(project);
        this.panel = panel;
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
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Hello world!");
        EditorTextField etfName = new EditorTextField();

        panel.add(label);
        panel.add(etfName);
        return panel;
    }


    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        return super.doValidate();
    }

    public abstract void onFinish(String result);
}
