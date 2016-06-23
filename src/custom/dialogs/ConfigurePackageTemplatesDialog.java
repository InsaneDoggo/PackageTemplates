package custom.dialogs;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.ui.ConfigureTemplatesDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ComboboxSpeedSearch;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.panels.VerticalBox;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Created by CeH9 on 22.06.2016.
 */
public class ConfigurePackageTemplatesDialog extends ConfigureTemplatesDialog {

    public ConfigurePackageTemplatesDialog(AnActionEvent event) {
        super(event.getProject());
    }

    @Override
    public JComponent getContentPanel() {
        return super.getContentPanel();
    }

    @Override
    protected JComponent createCenterPanel() {
        JBSplitter panel = new JBSplitter();


        panel.setFirstComponent(getPackageBuilderComponent());
        panel.setSecondComponent(super.createCenterPanel());
        return panel;
    }

    private JComponent getPackageBuilderComponent() {
        VerticalBox root = new VerticalBox();

        ComboBox comboBox = getSelector();

        //EditorTextField name
        //EditorTextField description

        // SeparatorComponent

        // Jlabel globals
        // EditorTextField TEMPLATE_PACKAGE_NAME

        // SeparatorComponent

        //EditorTextField root package
            //rcm ->
                //add package
                //add file
                //remove

//        root.add(comboBox);
        return root;
    }

    @NotNull
    private ComboBox getSelector() {
        FileTemplateManager ftManager = FileTemplateManager.getDefaultInstance();
        FileTemplate[] fileTemplates = ftManager.getAllTemplates();
        ComboBox comboBox = new ComboBox(new String[]{"aaaaa", "bbbb", "cccc"});
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE , comboBox.getPreferredSize().height));
        ComboboxSpeedSearch speedSearch = new ComboboxSpeedSearch(comboBox);
        return comboBox;
    }
}
