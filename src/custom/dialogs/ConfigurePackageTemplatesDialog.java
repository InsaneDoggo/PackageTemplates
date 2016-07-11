package custom.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.ui.SeparatorComponent;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.GridBag;
import models.PackageTemplate;
import models.TemplateContainer;
import reborn.models.Directory;
import reborn.wrappers.DirectoryWrapper;
import reborn.wrappers.PackageTemplateWrapper;
import utils.GridBagFactory;
import utils.UIMaker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by CeH9 on 22.06.2016.
 */
//public abstract class ConfigurePackageTemplatesDialog extends ConfigureTemplatesDialog {
public abstract class ConfigurePackageTemplatesDialog extends BaseDialog {

    private TemplateContainer templateContainer;
    private PackageTemplateWrapper ptWrapper;
    private GridBag gridBag;

    public abstract void onSuccess(PackageTemplateWrapper packageTemplate);

    public abstract void onFail();

    public ConfigurePackageTemplatesDialog(Project project) {
        super(project);
        ptWrapper = new PackageTemplateWrapper(project);

        PackageTemplate packageTemplate = new PackageTemplate();
        packageTemplate.setMapGlobalVars(new HashMap<>());
        packageTemplate.setName("New Package Template");
        packageTemplate.setDescription("");

        Directory directory = new Directory();
        directory.setName("Example");
        directory.setListBaseElement(new ArrayList<>());
        directory.setEnabled(true);
        directory.setGroovyCode("");

        DirectoryWrapper dirWrapper = new DirectoryWrapper();
        dirWrapper.setListBaseWrapper(new ArrayList<>());
        dirWrapper.setPackageTemplateWrapper(ptWrapper);
        dirWrapper.setParent(null);
        dirWrapper.setDirectory(directory);

        ptWrapper.setPackageTemplate(packageTemplate);
        ptWrapper.setRootElement(dirWrapper);
        ptWrapper.setMode(PackageTemplateWrapper.ViewMode.CREATE);
        ptWrapper.setListGlobalVariableWrapper(new ArrayList<>());
    }

    public ConfigurePackageTemplatesDialog(Project project, PackageTemplate packageTemplate) {
        super(project);
        ptWrapper = new PackageTemplateWrapper(project);
        ptWrapper.setPackageTemplate(packageTemplate);
        ptWrapper.setMode(PackageTemplateWrapper.ViewMode.EDIT);
        ptWrapper.setListGlobalVariableWrapper(new ArrayList<>());

        DirectoryWrapper dirWrapper = new DirectoryWrapper();
//        dirWrapper.setDirectory(packageTemplate.get);
        // TODO: 09.07.2016 wrap pt
        ptWrapper.setRootElement(dirWrapper);
    }

    @Override
    void preShow() {
        switch (ptWrapper.getMode()){
            case CREATE: setTitle("New Package Template"); break;
            case EDIT: setTitle("Edit Package Template"); break;
        }

        panel.setLayout(new GridBagLayout());
        panel.setMinimumSize(new Dimension(420, 240));
        gridBag = GridBagFactory.getBagForConfigureDialog();


        initContainer();
        panel.add(ptWrapper.buildView(), gridBag.nextLine().next());
    }

    @Override
    void onOKAction() {
        ptWrapper.collectDataFromFields();
        updatePackageTemplate();
        onSuccess(ptWrapper);

    }

    @Override
    void onCancelAction() {
        onFail();
    }

    private void updatePackageTemplate() {
//        packageTemplate.setName(templateContainer.getName());
//        packageTemplate.setTemplateVariableName(templateContainer.getTemplateView().getTemplateName());
//        packageTemplate.setDescription(templateContainer.getDescription());
//        packageTemplate.setTemplateElement(templateContainer.getTemplateView().toTemplateElement(null));
//        packageTemplate.setMapGlobalVars(templateContainer.collectGlobalVarsAsMap());
    }
//
//    private PackageTemplate getResultAsPackageTemplate() {
//        PackageTemplate result = new PackageTemplate(
//                templateContainer.getName(),
//                templateContainer.getDescription(),
//                templateContainer.getTemplateView().toTemplateElement(null)
//        );
//
//        result.setMapGlobalVars(templateContainer.collectGlobalVarsAsMap());
//
//        return result;
//    }

    private void initContainer() {
//        if (packageTemplate == null) {
//            // New
//            templateContainer = new TemplateContainer(
//                    "ExampleTemplate",
//                    "Description..",
//                    new TemplateView("example", null)
//            );
//            templateContainer.addVariable(new VariableView(PackageTemplate.ATTRIBUTE_BASE_NAME, "Example"));
//        } else {
//            // Load (old)
//            templateContainer = new TemplateContainer(
//                    packageTemplate.getName(),
//                    packageTemplate.getDescription(),
//                    packageTemplate.getTemplateElement().toTemplateView(null)
//            );
//
//            if (packageTemplate.getMapGlobalVars() != null) {
//                for (Map.Entry<String, String> entry : packageTemplate.getMapGlobalVars().entrySet()) {
//                    templateContainer.getListVariableView().add(new VariableView(
//                            entry.getKey(),
//                            entry.getValue()
//                    ));
//                }
//            }
//
//        }
    }

    private JComponent getPackageBuilderComponent(Project project) {
        JPanel root = new JPanel();
        JBScrollPane scrollPane = new JBScrollPane(root);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setAlignmentY(JPanel.TOP_ALIGNMENT);

        root.add(getTemplateNameComponent(project));
        root.add(new SeparatorComponent(10));
        root.add(templateContainer.buildView());
        root.add(new SeparatorComponent(10));
        root.add(templateContainer.getTemplateView().buildView(project));

        root.setBorder(new EmptyBorder(8, 8, 8, 8));
        scrollPane.setMinimumSize(new Dimension(300, 150));
        return scrollPane;
    }

    private Component getTemplateNameComponent(Project project) {
        JPanel container = new JPanel(new GridBagLayout());
        UIMaker.setLeftPadding(container, UIMaker.DEFAULT_PADDING);

        JLabel jLabel = new JLabel("Template Name");
        UIMaker.setRightPadding(jLabel, UIMaker.PADDING_LABEL);


        templateContainer.setEtfTemplateName(UIMaker.getEditorTextField(templateContainer.getName(), project));

        GridBag bag = GridBagFactory.getDefaultGridBag();
        container.add(jLabel, bag.nextLine().next());
        container.add(templateContainer.getEtfTemplateName(), bag.next());

        return container;
    }

}
