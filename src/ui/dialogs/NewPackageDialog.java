package ui.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by CeH9 on 14.06.2016.
 */
public abstract class NewPackageDialog extends DialogWrapper {

    public NewPackageDialog(@Nullable Project project, String title) {
        super(project);
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
        JLabel label = new JLabel("Hello world!");
        //todo input fields
        return label;
    }


    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        return super.doValidate();
    }

    public abstract void onFinish(String result);
}
