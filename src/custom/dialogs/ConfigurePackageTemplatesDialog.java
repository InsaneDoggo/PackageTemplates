package custom.dialogs;

import com.intellij.ide.fileTemplates.ui.ConfigureTemplatesDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.JBSplitter;
import custom.components.TemplateView;
import models.TemplateContainer;
import utils.UIMaker;

import javax.swing.*;
import java.awt.*;

/**
 * Created by CeH9 on 22.06.2016.
 */
public class ConfigurePackageTemplatesDialog extends ConfigureTemplatesDialog {

    private TemplateContainer templateContainer;

    public ConfigurePackageTemplatesDialog(AnActionEvent event) {
        super(event.getProject());

        TemplateView main = new TemplateView("IvanClass", null);
        templateContainer = new TemplateContainer("", "", main);

        main.getListTemplateView().add(new TemplateView("IvanClass", "Prost", "java", main));
        main.getListTemplateView().add(new TemplateView("IvanClass", "Slojn", "java", main));
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


        root.add(templateContainer.buildView());
        root.add(templateContainer.getTemplateView().buildView(getProject()));


        return root;
    }

}
