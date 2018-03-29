package base;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBScrollPane;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * Created by CeH9 on 08.07.2016.
 */
public abstract class SimpleBaseDialog extends DialogWrapper {

    protected Project project;
    protected JPanel panel;

    public SimpleBaseDialog(@Nullable Project project) {
        super(project);
        this.project = project;
        init();
    }

    // Abstraction
    protected abstract void buildView();
    protected abstract MigLayout createLayout();
    protected abstract void collectDataFromUI();
    protected abstract boolean isDataValid();
    protected abstract void onPositiveButtonClick();


    //=================================================================
    //  UI
    //=================================================================
    protected Dimension getMinSizeDimension() {
        return new Dimension(600, 520);
    }

    @Override
    protected JComponent createCenterPanel() {
        panel = new JPanel(createLayout());
        buildView();

        JBScrollPane scrollPane = new JBScrollPane(panel);
        scrollPane.setMinimumSize(getMinSizeDimension());
        return scrollPane;
    }


    //=================================================================
    //  Validation
    //=================================================================
    @Override
    protected void doOKAction() {
        collectDataFromUI();

        if (!isDataValid()) {
            return;
        }

        onPositiveButtonClick();
        super.doOKAction();
    }

}
