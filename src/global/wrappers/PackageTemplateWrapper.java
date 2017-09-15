package global.wrappers;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.ui.SeparatorComponent;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.IconUtil;
import core.actions.custom.CreateDirectoryAction;
import core.actions.custom.DummyDirectoryAction;
import core.actions.custom.InjectTextAction;
import core.actions.custom.base.SimpleAction;
import core.actions.newPackageTemplate.dialogs.configure.ConfigureDialog;
import core.actions.newPackageTemplate.dialogs.configure.render.FileTemplateSourceCellRenderer;
import core.actions.newPackageTemplate.models.ExecutionContext;
import core.report.ReportHelper;
import core.report.models.PendingActionReport;
import core.textInjection.TextInjection;
import core.textInjection.dialog.TextInjectionDialog;
import core.textInjection.dialog.TextInjectionWrapper;
import core.writeRules.WriteRules;
import core.writeRules.dialog.WriteRulesCellRenderer;
import global.Const;
import global.dialogs.PredefinedVariablesDialog;
import global.listeners.ClickListener;
import global.models.*;
import global.utils.factories.WrappersFactory;
import global.utils.file.FileWriter;
import global.utils.i18n.Localizer;
import global.utils.templates.FileTemplateHelper;
import global.utils.templates.FileTemplateSource;
import global.visitors.*;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * Created by Arsen on 07.07.2016.
 */
public class PackageTemplateWrapper {

    public static final String ATTRIBUTE_BASE_NAME = "BASE_NAME";
    public static final String PATTERN_BASE_NAME = "${" + ATTRIBUTE_BASE_NAME + "}";

    public enum ViewMode {EDIT, CREATE, USAGE}

    private Project project;
    private Properties defaultProperties;
    private PackageTemplate packageTemplate;
    private DirectoryWrapper rootElement;
    private ArrayList<GlobalVariableWrapper> listGlobalVariableWrapper;
    private ArrayList<TextInjectionWrapper> listTextInjectionWrapper;
    private ViewMode mode;
    private ExecutionContext executionContext;

    public PackageTemplateWrapper(Project project) {
        this.project = project;
        this.executionContext = new ExecutionContext();
    }


    //=================================================================
    //  UI
    //=================================================================
    private JPanel panel;
    public JTextField jtfName;
    public JTextArea jtaDescription;
    public JCheckBox cbShouldRegisterAction;
    public JCheckBox cbSkipDefiningNames;
    public JCheckBox cbSkipRootDirectory;
    public JCheckBox cbShowReportDialog;
    private ComboBox cboxFileTemplateSource;
    private JPanel panelProperties;
    private JPanel panelElements;
    private JPanel panelTextInjection;
    private JPanel panelGlobals;

    public JPanel buildView() {
        if (panel == null) {
            panel = new JPanel(new MigLayout(new LC().insets("0").fillX().gridGapY("1pt")));
        }

        // Properties
        panelProperties = new JPanel(new MigLayout(new LC().insets("0").gridGap("0", "0")));
        buildProperties();
        panel.add(panelProperties, new CC().spanX().wrap().pushX().growX());


        // Globals
        panel.add(new SeparatorComponent(6), new CC().pushX().growX().wrap().spanX());
        JLabel jlGlobals = new JLabel(Localizer.get("GlobalVariables"), JLabel.CENTER);
        panel.add(jlGlobals, new CC().wrap().growX().pushX().spanX().gapY("0", "4pt"));

        panelGlobals = new JPanel(new MigLayout(new LC().insets("0").gridGap("0", "0")));
        buildGlobals();
        panel.add(panelGlobals, new CC().spanX().pushX().growX().wrap());
        initAvailableVariablesButton();


        // Files and Directories | Elements
        panel.add(new SeparatorComponent(6), new CC().pushX().growX().wrap().spanX());
        JLabel jlFilesAndDirs = new JLabel(Localizer.get("FilesAndDirs"), JLabel.CENTER);
        panel.add(jlFilesAndDirs, new CC().wrap().growX().pushX().spanX().gapY("0", "4pt"));

        panelElements = new JPanel(new MigLayout(new LC().insets("0").gridGap("0", "2pt")));
        buildElements();
        panel.add(panelElements, new CC().spanX().pushX().growX().wrap());


        // Text Injection
        panel.add(new SeparatorComponent(6), new CC().pushX().growX().wrap().spanX());
        JLabel jlTextInjection = new JLabel(Localizer.get("TextInjection"), JLabel.CENTER);
        panel.add(jlTextInjection, new CC().wrap().growX().pushX().spanX());

        panelTextInjection = new JPanel(new MigLayout(new LC().insets("0").gridGap("0", "0")));
        buildTextInjections();
        panel.add(panelTextInjection, new CC().spanX().pushX().growX().wrap());

        initTextInjectionAddButton();

        return panel;
    }

    private void initTextInjectionAddButton() {
        if (getMode() == ViewMode.USAGE) {
            return;
        }

        JButton btnAdd = new JButton(Localizer.get("action.AddTextInjection"), IconUtil.getAddIcon());
        btnAdd.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                createTextInjection();
            }
        });
        panel.add(btnAdd, new CC().wrap());
    }

    private void initAvailableVariablesButton() {
        if (getMode() == ViewMode.USAGE) {
            return;
        }

        JButton btnPredefinedVariables = new JButton(Localizer.get("label.PredefinedVariables"));
        btnPredefinedVariables.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new PredefinedVariablesDialog(project, PackageTemplateWrapper.this).show();
            }
        });
        panel.add(btnPredefinedVariables, new CC().spanX().wrap());
    }

    private void buildProperties() {
        // Header
        JLabel jlName = new JLabel(Localizer.get("Name"));
        JLabel jlDescription = new JLabel(Localizer.get("Description"));

        jtfName = new JBTextField(packageTemplate.getName());
        jtaDescription = new JTextArea(packageTemplate.getDescription());

        panelProperties.add(jlName, new CC().wrap().spanX().pad(0, 0, 0, 8).gapY("0", "4pt"));
        panelProperties.add(jtfName, new CC().spanX().growX().pushX().wrap());
        panelProperties.add(jlDescription, new CC().wrap().spanX().pad(0, 0, 0, 8).gapY("4pt", "4pt"));
        panelProperties.add(jtaDescription, new CC().spanX().growX().pushX().wrap().gapY("0", "4pt"));

        //FileTemplate Source
        ArrayList<FileTemplateSource> actionTypes = new ArrayList<>();
        actionTypes.add(FileTemplateSource.DEFAULT_ONLY);
        actionTypes.add(FileTemplateSource.PROJECT_ONLY);
        actionTypes.add(FileTemplateSource.PROJECT_PRIORITY);
        actionTypes.add(FileTemplateSource.DEFAULT_PRIORITY);

        cboxFileTemplateSource = new ComboBox(actionTypes.toArray());
        cboxFileTemplateSource.setRenderer(new FileTemplateSourceCellRenderer());
        cboxFileTemplateSource.setSelectedItem(packageTemplate.getFileTemplateSource());
        cboxFileTemplateSource.addActionListener(e -> {
            packageTemplate.setFileTemplateSource((FileTemplateSource) cboxFileTemplateSource.getSelectedItem());
        });

        if (mode == ViewMode.USAGE) {
            jtfName.setEditable(false);
            jtaDescription.setEditable(false);
            cboxFileTemplateSource.setEnabled(false);
        } else {
            // Properties
            cbShouldRegisterAction = new JBCheckBox(Localizer.get("property.ShouldRegisterAction"), packageTemplate.isShouldRegisterAction());
            cbSkipDefiningNames = new JBCheckBox(Localizer.get("property.SkipPresettings"), packageTemplate.isSkipDefiningNames());
            panelProperties.add(cbShouldRegisterAction, new CC().wrap().spanX());
            panelProperties.add(cbSkipDefiningNames, new CC().wrap().spanX());
        }

        // Properties
        cbSkipRootDirectory = new JBCheckBox(Localizer.get("property.SkipRootDirectory"), packageTemplate.isSkipRootDirectory());
        cbSkipRootDirectory.addItemListener(e -> {
            collectDataFromFields();
            reBuildElements();
        });
        cbShowReportDialog = new JBCheckBox(Localizer.get("property.ShowReportDialog"), packageTemplate.shouldShowReport());

        panelProperties.add(cbSkipRootDirectory, new CC().spanX().wrap());
        panelProperties.add(cbShowReportDialog, new CC().spanX().wrap());
        panelProperties.add(new JLabel(Localizer.get("FileTemplateSource")), new CC().spanX().split(2));
        panelProperties.add(cboxFileTemplateSource, new CC().pushX().growX().wrap());

    }

    private void buildElements() {
        rootElement.buildView(project, panelElements);
    }

    private void buildGlobals() {
        for (GlobalVariableWrapper variableWrapper : getListGlobalVariableWrapper()) {
            variableWrapper.buildView(this, panelGlobals);
        }
    }

    private void buildTextInjections() {
        for (TextInjectionWrapper wrapper : getListTextInjectionWrapper()) {
            wrapper.buildView(this, panelTextInjection);
        }
    }


    //=================================================================
    //  UI Actions
    //=================================================================
    private void createTextInjection() {
        new TextInjectionDialog(project, null) {
            @Override
            public void onSuccess(TextInjection textInjection) {
                addTextInjection(WrappersFactory.wrapTextInjection(textInjection));
                reBuildTextInjections();
            }
        }.show();
    }

    public void addGlobalVariable(GlobalVariableWrapper gvWrapper) {
        listGlobalVariableWrapper.add(gvWrapper);
        packageTemplate.getListGlobalVariable().add(gvWrapper.getGlobalVariable());
    }

    public void removeGlobalVariable(GlobalVariableWrapper gvWrapper) {
        packageTemplate.getListGlobalVariable().remove(gvWrapper.getGlobalVariable());
        listGlobalVariableWrapper.remove(gvWrapper);
    }

    public void addTextInjection(TextInjectionWrapper wrapper) {
        listTextInjectionWrapper.add(wrapper);
        packageTemplate.getListTextInjection().add(wrapper.getTextInjection());
    }

    public void removeTextInjection(TextInjectionWrapper wrapper) {
        packageTemplate.getListTextInjection().remove(wrapper.getTextInjection());
        listTextInjectionWrapper.remove(wrapper);
    }


    //=================================================================
    //  UI ReBuild
    //=================================================================
    ConfigureDialog.UpdateUICallback updateUICallback;

    public void setUpdateUICallback(ConfigureDialog.UpdateUICallback updateUICallback) {
        this.updateUICallback = updateUICallback;
    }

    public void fullReBuild() {
        reBuildProperties();
        reBuildGlobals();
        reBuildElements();
        reBuildTextInjections();
    }

    public void reBuildProperties() {
        panelProperties.removeAll();
        buildProperties();
    }

    public void reBuildGlobals() {
        panelGlobals.removeAll();
        buildGlobals();
        packParentContainer();
    }

    public void reBuildElements() {
        panelElements.removeAll();
        buildElements();
        panel.repaint();
        packParentContainer();
    }

    public void reBuildTextInjections() {
        panelTextInjection.removeAll();
        buildTextInjections();
        packParentContainer();
    }

    private void packParentContainer() {
        if (updateUICallback != null) {
            updateUICallback.pack();
        }
    }


    //=================================================================
    //  Utils
    //=================================================================
    public void replaceNameVariable() {
        ReplaceNameVariableVisitor visitor = new ReplaceNameVariableVisitor(getAllProperties());

        for (TextInjection textInjection : getPackageTemplate().getListTextInjection()) {
            visitor.visitCustomPath(textInjection.getCustomPath());
            visitor.visitTextToSearch(textInjection);
        }

        rootElement.accept(visitor);
    }

    public void collectDataFromFields() {
        if (getMode() != ViewMode.USAGE) {
            packageTemplate.setName(jtfName.getText());
            packageTemplate.setDescription(jtaDescription.getText());
            packageTemplate.setShouldRegisterAction(cbShouldRegisterAction.isSelected());
            packageTemplate.setSkipDefiningNames(cbSkipDefiningNames.isSelected());
            packageTemplate.setFileTemplateSource((FileTemplateSource) cboxFileTemplateSource.getSelectedItem());
        }
        packageTemplate.setSkipRootDirectory(cbSkipRootDirectory.isSelected());
        packageTemplate.setShouldShowReport(cbShowReportDialog.isSelected());

        for (GlobalVariableWrapper variableWrapper : listGlobalVariableWrapper) {
            variableWrapper.collectDataFromFields();
        }

        for (TextInjectionWrapper variableWrapper : listTextInjectionWrapper) {
            variableWrapper.collectDataFromFields();
        }


        rootElement.accept(new СollectDataFromFieldsVisitor());
    }

    /**
     * Replace BASE_NAME and Run SCRIPT
     */
    public void prepareGlobals() {
        packageTemplate.setMapGlobalVars(new HashMap<>());
        // Context Vars
        packageTemplate.getMapGlobalVars().put(Const.Key.CTX_FULL_PATH, executionContext.ctxFullPath);
        packageTemplate.getMapGlobalVars().put(Const.Key.CTX_DIR_PATH, executionContext.ctxDirPath);

        // Init temp map
        HashMap<String, String> mapAllProperties = getAllProperties();

        for (GlobalVariableWrapper variableWrapper : listGlobalVariableWrapper) {
            if (getMode() == ViewMode.USAGE) {
                variableWrapper.evaluteVelocity(mapAllProperties);
                // SCRIPT
                variableWrapper.runScript();
            }
            packageTemplate.getMapGlobalVars().put(variableWrapper.getGlobalVariable().getName(), variableWrapper.getGlobalVariable().getValue());
            mapAllProperties.put(variableWrapper.getGlobalVariable().getName(), variableWrapper.getGlobalVariable().getValue());
        }
    }

    @NotNull
    private HashMap<String, String> getAllProperties() {
        HashMap<String, String> mapAllProperties = new HashMap<>();
        //Globals + Context
        mapAllProperties.putAll(packageTemplate.getMapGlobalVars());
        // Default
        for (Map.Entry<Object, Object> entry : getDefaultProperties().entrySet()) {
            mapAllProperties.put((String) entry.getKey(), (String) entry.getValue());
        }
        return mapAllProperties;
    }

    /**
     * Properties with fake value for Helper Dialog
     */
    @NotNull
    public Map<String, String> getFakeProperties() {
        TreeMap<String, String> mapAllProperties = new TreeMap<>();
        //mapAllProperties.putAll(packageTemplate.getMapGlobalVars());

        // Context
        mapAllProperties.put(Const.Key.CTX_FULL_PATH, "C:/foo/bar/src/com/company/Main.java");
        mapAllProperties.put(Const.Key.CTX_DIR_PATH, "C:/foo/bar/src/com/company");

        // Default
        for (Map.Entry<Object, Object> entry : getDefaultProperties().entrySet()) {
            mapAllProperties.put((String) entry.getKey(), (String) entry.getValue());
        }

        return mapAllProperties;
    }


    public void addGlobalVariablesToFileTemplates() {
        rootElement.accept(new AddGlobalVariablesVisitor());
    }

    public void initCollections() {
        listGlobalVariableWrapper = new ArrayList<>();
        for (GlobalVariable item : packageTemplate.getListGlobalVariable()) {
            listGlobalVariableWrapper.add(new GlobalVariableWrapper(item));
        }

        listTextInjectionWrapper = new ArrayList<>();
        for (TextInjection item : packageTemplate.getListTextInjection()) {
            listTextInjectionWrapper.add(new TextInjectionWrapper(item));
        }
    }

    public void runElementsScript() {
        rootElement.accept(new RunScriptVisitor());
    }

    public void collectSimpleActions(Project project, VirtualFile virtualFile, List<SimpleAction> listSimpleAction) {
        ApplicationManager.getApplication().runReadAction(() -> {
            PsiDirectory currentDir = FileWriter.findCurrentDirectory(project, virtualFile);
            if (currentDir == null) {
                return;
            }

            // Disabled
            if (!getRootElement().getDirectory().isEnabled()) {
                return;
            }

            SimpleAction rootAction = getRootAction(project, listSimpleAction, currentDir);

            CollectSimpleActionVisitor visitor = new CollectSimpleActionVisitor(rootAction, project);

            for (ElementWrapper elementWrapper : rootElement.getListElementWrapper()) {
                elementWrapper.accept(visitor);
            }
        });
    }

    @NotNull
    private SimpleAction getRootAction(Project project, List<SimpleAction> listSimpleAction, PsiDirectory currentDir) {
        SimpleAction action;
        if (packageTemplate.isSkipRootDirectory()) {
            // Without root
            action = new DummyDirectoryAction(project, currentDir);
            listSimpleAction.add(action);
        } else {
            // With root
            action = new CreateDirectoryAction(packageTemplate.getDirectory(), project);
            action.setId(ReportHelper.getGenerateId());
            ReportHelper.putReport(new PendingActionReport(action));
            listSimpleAction.add(wrapInDummyDirAction(action, currentDir));
        }
        return action;
    }

    public void collectInjectionActions(Project project, List<SimpleAction> listSimpleAction) {
        HashMap<String, String> map = getAllProperties();

        for (TextInjectionWrapper wrapper : listTextInjectionWrapper) {
            if (!wrapper.getTextInjection().isEnabled()) {
                continue;
            }

            listSimpleAction.add(new InjectTextAction(project, wrapper.getTextInjection(), map));
        }
    }

    private SimpleAction wrapInDummyDirAction(SimpleAction simpleAction, PsiDirectory currentDir) {
        DummyDirectoryAction dummyAction = new DummyDirectoryAction(project, currentDir);
        dummyAction.addAction(simpleAction);
        return dummyAction;
    }

    public void initDefaultProperties() {
        defaultProperties = FileTemplateHelper.getDefaultProperties(project);
    }


    //=================================================================
    //  Getter | Setter
    //=================================================================
    public ExecutionContext getExecutionContext() {
        return executionContext;
    }

    public void setExecutionContext(ExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public PackageTemplate getPackageTemplate() {
        return packageTemplate;
    }

    public void setPackageTemplate(PackageTemplate packageTemplate) {
        this.packageTemplate = packageTemplate;
    }

    public DirectoryWrapper getRootElement() {
        return rootElement;
    }

    public void setRootElement(DirectoryWrapper rootElement) {
        this.rootElement = rootElement;
    }

    public ArrayList<GlobalVariableWrapper> getListGlobalVariableWrapper() {
        return listGlobalVariableWrapper;
    }

    public void setListGlobalVariableWrapper(ArrayList<GlobalVariableWrapper> listGlobalVariableWrapper) {
        this.listGlobalVariableWrapper = listGlobalVariableWrapper;
    }

    public ViewMode getMode() {
        return mode;
    }

    public void setMode(ViewMode mode) {
        this.mode = mode;
    }

    public Properties getDefaultProperties() {
        if (defaultProperties == null) {
            initDefaultProperties();
        }

        return defaultProperties;
    }

    public ArrayList<TextInjectionWrapper> getListTextInjectionWrapper() {
        return listTextInjectionWrapper;
    }

    public void setListTextInjectionWrapper(ArrayList<TextInjectionWrapper> listTextInjectionWrapper) {
        this.listTextInjectionWrapper = listTextInjectionWrapper;
    }
}