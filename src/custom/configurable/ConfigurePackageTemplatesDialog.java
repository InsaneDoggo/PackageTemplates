package custom.configurable;

import com.intellij.ide.fileTemplates.impl.AllFileTemplatesConfigurable;
import com.intellij.ide.fileTemplates.ui.ConfigureTemplatesDialog;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBSplitter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by CeH9 on 22.06.2016.
 */
public class ConfigurePackageTemplatesDialog extends ConfigureTemplatesDialog {
    public ConfigurePackageTemplatesDialog(Project project) {
        super(project);
    }

    public ConfigurePackageTemplatesDialog(Project project, AllFileTemplatesConfigurable fileTemplatesConfigurable) {
        super(project, fileTemplatesConfigurable);
    }

    @Override
    public JComponent getContentPanel() {
        return super.getContentPanel();
    }

    @Override
    protected JComponent createCenterPanel() {
        JBSplitter panel = new JBSplitter();
        // TODO: 22.06.2016  add left panel
        JLabel label = new JLabel("Hello!!");
        label.setPreferredSize(new Dimension(300,500));
        panel.setFirstComponent(label);
        panel.setSecondComponent(super.createCenterPanel());
        return panel;
    }
}
