package ui.dialogs;

import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.JBColor;
import com.intellij.ui.SeparatorComponent;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static utils.InputManager.DEFAULT_PADDING;

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
//        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel("Var1");
        EditorTextField etfName = new EditorTextField();
        JLabel label2 = new JLabel("Var 2");
        EditorTextField etfName2 = new EditorTextField();

        JLabel lPackage = new JLabel("Package Ivan");

        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        etfName.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        etfName.setAlignmentX(Component.LEFT_ALIGNMENT);

        etfName.setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getPreferredSize().height));

        label2.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        etfName2.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));
        label2.setAlignmentX(Component.LEFT_ALIGNMENT);
        etfName2.setAlignmentX(Component.LEFT_ALIGNMENT);

        SeparatorComponent separator = new SeparatorComponent(10);
        lPackage.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        lPackage.setAlignmentX(Component.LEFT_ALIGNMENT);


        panel.add(label);
        panel.add(etfName);

        panel.add(separator);
        panel.add(lPackage);

        panel.add(label2);
        panel.add(etfName2);
        return panel;
    }


    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        return super.doValidate();
    }

    public abstract void onFinish(String result);
}
