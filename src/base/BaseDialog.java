package base;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by CeH9 on 08.07.2016.
 */
public abstract class BaseDialog extends DialogWrapper {

    protected Project project;
    protected JPanel panel;

    protected static final int PADDING = 10;
    protected final int MIN_WIDTH = 600;
    protected final int MIN_HEIGHT = 520;

    public BaseDialog(@Nullable Project project) {
        super(project);
        this.project = project;
        init();
    }

    public void preShow() {
    }

    public void onOKAction() {
    }

    public void onCancelAction() {
    }

    @Override
    public void show() {
        preShow();
        super.show();

        switch (getExitCode()) {
            case DialogWrapper.OK_EXIT_CODE:
                onOKAction();
                break;
            case DialogWrapper.CANCEL_EXIT_CODE:
                onCancelAction();
                break;
        }
    }

    @Override
    protected JComponent createCenterPanel() {
        panel = new JPanel();
        JBScrollPane scrollPane = new JBScrollPane(panel);
        panel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
        scrollPane.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        return scrollPane;
    }

}
