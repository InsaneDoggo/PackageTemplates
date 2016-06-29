package custom.dialogs;

import com.intellij.ide.fileTemplates.ui.ConfigureTemplatesDialog;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.SeparatorComponent;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.GridBag;
import custom.components.TemplateView;
import custom.components.VariableView;
import models.PackageTemplate;
import models.TemplateContainer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

import static utils.UIMaker.*;

/**
 * Created by CeH9 on 22.06.2016.
 */
public abstract class ConfigurePackageTemplatesDialog extends ConfigureTemplatesDialog {

    private TemplateContainer templateContainer;
    private PackageTemplate packageTemplate;
    private JBSplitter panel;

    public abstract void onSuccess(PackageTemplate packageTemplate);

    public abstract void onCancel();

    public ConfigurePackageTemplatesDialog(Project project) {
        super(project);
    }

    public ConfigurePackageTemplatesDialog(Project project, PackageTemplate packageTemplate) {
        super(project);
        this.packageTemplate = packageTemplate;
    }

    @Override
    public void show() {
        preShow();
        super.show();

        switch (getExitCode()) {
            case ConfigurePackageTemplatesDialog.OK_EXIT_CODE:
                onOKAction();
                break;
            case ConfigurePackageTemplatesDialog.CANCEL_EXIT_CODE:
                onCancel();
                break;
        }
    }

    private void preShow() {
        initContainer();
        panel.setFirstComponent(getPackageBuilderComponent());
    }

    private void onOKAction() {
        templateContainer.collectDataFromFields();

        if (packageTemplate == null) {
            onSuccess(getResultAsPackageTemplate());
        } else {
            updatePackageTemplate();
            onSuccess(packageTemplate);
        }
    }

    private void updatePackageTemplate() {
        packageTemplate.setName(templateContainer.getName());
        packageTemplate.setTemplateVariableName(templateContainer.getTemplateView().getTemplateName());
        packageTemplate.setDescription(templateContainer.getDescription());
        packageTemplate.setTemplateElement(templateContainer.getTemplateView().toTemplateElement(null));
        packageTemplate.setMapGlobalVars(templateContainer.collectGlobalVarsAsMap());
    }

    private PackageTemplate getResultAsPackageTemplate() {
        PackageTemplate result = new PackageTemplate(
                templateContainer.getName(),
                templateContainer.getDescription(),
                templateContainer.getTemplateView().toTemplateElement(null)
        );

        result.setMapGlobalVars(templateContainer.collectGlobalVarsAsMap());

        return result;
    }

    @Override
    public JComponent getContentPanel() {
        return super.getContentPanel();
    }

    @Override
    protected JComponent createCenterPanel() {
        panel = new JBSplitter();

        // TODO: 28.06.2016 fix default template editor
//        panel.setSecondComponent(super.createCenterPanel());
        super.createCenterPanel();

        return panel;
    }

    private void initContainer() {
        if (packageTemplate == null) {
            // New
            templateContainer = new TemplateContainer(
                    "New Template",
                    "Description..",
                    new TemplateView(String.format("%s%s%s", "${", PackageTemplate.ATTRIBUTE_BASE_NAME, "}"), null)
            );
            templateContainer.addVariable(new VariableView(PackageTemplate.ATTRIBUTE_BASE_NAME, ""));
        } else {
            // Load (old)
            templateContainer = new TemplateContainer(
                    packageTemplate.getName(),
                    packageTemplate.getDescription(),
                    packageTemplate.getTemplateElement().toTemplateView(null)
            );

            if (packageTemplate.getMapGlobalVars() != null) {
                for (Map.Entry<String, String> entry : packageTemplate.getMapGlobalVars().entrySet()) {
                    templateContainer.getListVariableView().add(new VariableView(
                            entry.getKey(),
                            entry.getValue()
                    ));
                }
            }

        }
    }

    private JComponent getPackageBuilderComponent() {
        JPanel root = new JPanel();
        JBScrollPane scrollPane = new JBScrollPane(root);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setAlignmentY(JPanel.TOP_ALIGNMENT);

        root.add(getTemplateNameComponent());
        root.add(new SeparatorComponent(10));
        root.add(templateContainer.buildView());
        root.add(new SeparatorComponent(10));
        root.add(templateContainer.getTemplateView().buildView(getProject()));

        root.setBorder(new EmptyBorder(8, 8, 8, 8));
        scrollPane.setMinimumSize(new Dimension(300, 150));
        return scrollPane;
    }

    private Component getTemplateNameComponent() {
        JPanel container = new JPanel(new GridBagLayout());
        setLeftPadding(container, DEFAULT_PADDING);

        JLabel jLabel = new JLabel("Template Name");
        setRightPadding(jLabel, PADDING_LABEL);


        templateContainer.setEtfTemplateName(getEditorTextField(templateContainer.getName(), getProject()));

        GridBag bag = getDefaultGridBag();
        container.add(jLabel, bag.nextLine().next());
        container.add(templateContainer.getEtfTemplateName(), bag.next());

        return container;
    }

}
