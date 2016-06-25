package custom.dialogs;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.ui.ConfigureTemplatesDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ComboboxSpeedSearch;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.panels.VerticalBox;
import custom.components.TemplateView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static com.sun.tools.internal.xjc.reader.Ring.add;

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
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));

        root.setAlignmentY(Component.TOP_ALIGNMENT);

        //EditorTextField name
        //EditorTextField description

        // SeparatorComponent

        // Jlabel globals
        // EditorTextField TEMPLATE_PACKAGE_NAME

        // SeparatorComponent


        TemplateView main = new TemplateView("IvanClass", null);

        main.getListTemplateView().add(new TemplateView("IvanClass", "Prost", "java", main));
        main.getListTemplateView().add(new TemplateView("IvanClass", "Slojn", "java", main));


        root.add(main.buildView(getProject()));
        return root;
    }

}
