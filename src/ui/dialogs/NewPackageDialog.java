package ui.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by CeH9 on 14.06.2016.
 */
public class NewPackageDialog extends DialogWrapper {

    public NewPackageDialog(@Nullable Project project, String title) {
        super(project);
        init();
        setTitle(title);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JLabel label = new JLabel("Hello world!");
        return label;
    }
}
