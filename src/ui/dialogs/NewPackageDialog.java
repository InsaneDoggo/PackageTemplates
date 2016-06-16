package ui.dialogs;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.SeparatorComponent;
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
//        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel lClass = new JLabel("Var 2");
        EditorTextField etfClass = new EditorTextField();
        JLabel lPackage = new JLabel("Package Ivan");
        SeparatorComponent separator = new SeparatorComponent(10);



        lClass.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        lClass.setAlignmentX(Component.LEFT_ALIGNMENT);
        lClass.setIcon(AllIcons.Nodes.Class);

        etfClass.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));
        etfClass.setAlignmentX(Component.LEFT_ALIGNMENT);

        lPackage.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        lPackage.setAlignmentX(Component.LEFT_ALIGNMENT);
        lPackage.setIcon(AllIcons.Nodes.Folder);


        panel.add(lPackage);
        panel.add(lClass);
        panel.add(etfClass);
        panel.add(separator);
        return panel;
    }


    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        return super.doValidate();
    }

    public abstract void onFinish(String result);
}
