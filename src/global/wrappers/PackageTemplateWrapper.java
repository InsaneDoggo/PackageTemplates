package global.wrappers;

import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.SeparatorComponent;
import com.intellij.ui.components.JBCheckBox;
import core.actions.custom.CreateDirectoryAction;
import core.actions.custom.DummyDirectoryAction;
import core.actions.custom.base.SimpleAction;
import global.dialogs.FailedFilesDialog;
import global.models.*;
import global.utils.Logger;
import global.utils.UIHelper;
import global.utils.file.FileWriter;
import global.utils.i18n.Localizer;
import global.visitors.*;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

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
    private ViewMode mode;
    private ArrayList<ElementWrapper> failedElements;
    private ArrayList<PsiElement> writtenElements;

    public PackageTemplateWrapper(Project project) {
        this.project = project;
    }


    //=================================================================
    //  UI
    //=================================================================
    private JPanel panel;
    public EditorTextField etfName;
    public EditorTextField etfDescription;
    public JCheckBox cbShouldRegisterAction;
    public JCheckBox cbSkipDefiningNames;
    public JCheckBox cbSkipRootDirectory;

    public JPanel buildView() {
        if (panel == null) {
            panel = new JPanel(new MigLayout(new LC().fillX().gridGapY("0")));
        }

        // Properties
        JPanel jpProperties = new JPanel(new MigLayout(new LC()));

        if (mode != ViewMode.USAGE) {
            // Header
            JLabel jlName = new JLabel(Localizer.get("Name"));
            JLabel jlDescription = new JLabel(Localizer.get("Description"));

            etfName = UIHelper.getEditorTextField(packageTemplate.getName(), project);
            etfDescription = UIHelper.getEditorTextField(packageTemplate.getDescription(), project);

            panel.add(jlName, new CC().wrap().spanX().pad(0, 0, 0, 8).gapY("0","8"));
            panel.add(etfName, new CC().spanX().growX().pushX().wrap());
            panel.add(jlDescription, new CC().wrap().spanX().pad(0, 0, 0, 8).gapY("8","8"));
            panel.add(etfDescription, new CC().spanX().growX().pushX().wrap());

            // Properties
            cbShouldRegisterAction = new JBCheckBox(Localizer.get("property.ShouldRegisterAction"), packageTemplate.isShouldRegisterAction());
            cbSkipDefiningNames = new JBCheckBox(Localizer.get("property.SkipPresettings"), packageTemplate.isSkipDefiningNames());
            jpProperties.add(cbShouldRegisterAction, new CC().wrap().spanX());
            jpProperties.add(cbSkipDefiningNames, new CC().wrap().spanX());
        }

        // Properties
        cbSkipRootDirectory = new JBCheckBox(Localizer.get("property.SkipRootDirectory"), packageTemplate.isSkipRootDirectory());
        cbSkipRootDirectory.addItemListener(e -> {
            collectDataFromFields();
            reBuildView();
        });
        jpProperties.add(cbSkipRootDirectory, new CC().spanX().wrap());
        panel.add(jpProperties, new CC().spanX().wrap());

        panel.add(new SeparatorComponent(10), new CC().pushX().growX().wrap().spanX());


        // Globals
        JLabel jlGlobals = new JLabel(Localizer.get("GlobalVariables"), JLabel.CENTER);
        panel.add(jlGlobals, new CC().wrap().growX().pushX().spanX());

        for (GlobalVariableWrapper variableWrapper : getListGlobalVariableWrapper()) {
            variableWrapper.buildView(this, panel);
        }

        // Files and Directories
        panel.add(new SeparatorComponent(10), new CC().pushX().growX().wrap().spanX());
        rootElement.buildView(project, panel);

        return panel;
    }

    public void addGlobalVariable(GlobalVariableWrapper gvWrapper) {
        listGlobalVariableWrapper.add(gvWrapper);
        packageTemplate.getListGlobalVariable().add(gvWrapper.getGlobalVariable());
    }

    public void removeGlobalVariable(GlobalVariableWrapper gvWrapper) {
        packageTemplate.getListGlobalVariable().remove(gvWrapper.getGlobalVariable());
        listGlobalVariableWrapper.remove(gvWrapper);
    }

    public void reBuildView() {
        panel.removeAll();
        buildView();
    }


    //=================================================================
    //  Utils
    //=================================================================
    public void replaceNameVariable() {
        rootElement.accept(new ReplaceNameVariableVisitor(packageTemplate.getMapGlobalVars()));
    }

    public void collectDataFromFields() {
        if (getMode() != ViewMode.USAGE) {
            packageTemplate.setName(etfName.getText());
            packageTemplate.setDescription(etfDescription.getText());
            packageTemplate.setShouldRegisterAction(cbShouldRegisterAction.isSelected());
            packageTemplate.setSkipDefiningNames(cbSkipDefiningNames.isSelected());
        }
        packageTemplate.setSkipRootDirectory(cbSkipRootDirectory.isSelected());

        for (GlobalVariableWrapper variableWrapper : listGlobalVariableWrapper) {
            variableWrapper.collectDataFromFields();
        }

        rootElement.accept(new СollectDataFromFieldsVisitor());
    }

    /**
     * Replace BASE_NAME and Run SCRIPT
     */
    public void prepareGlobals() {
        packageTemplate.setMapGlobalVars(new HashMap<>());

        for (GlobalVariableWrapper variableWrapper : listGlobalVariableWrapper) {
            if (getMode() == ViewMode.USAGE) {
                // Replace ${BASE_NAME}
                if (!variableWrapper.getGlobalVariable().getName().equals(ATTRIBUTE_BASE_NAME)) {
                    variableWrapper.replaceBaseName(packageTemplate.getMapGlobalVars().get(ATTRIBUTE_BASE_NAME));
                }
                // SCRIPT
                variableWrapper.runGroovyScript();
            }
            packageTemplate.getMapGlobalVars().put(variableWrapper.getGlobalVariable().getName(), variableWrapper.getGlobalVariable().getValue());
        }
    }

    public void addGlobalVariablesToFileTemplates() {
        rootElement.accept(new AddGlobalVariablesVisitor());
    }

    public DirectoryWrapper wrapDirectory(Directory directory, DirectoryWrapper parent) {
        DirectoryWrapper result = new DirectoryWrapper();
        result.setDirectory(directory);
        result.setParent(parent);
        result.setPackageTemplateWrapper(PackageTemplateWrapper.this);

        ArrayList<ElementWrapper> list = new ArrayList<>();

        for (BaseElement baseElement : directory.getListBaseElement()) {
            if (baseElement.isDirectory()) {
                list.add(wrapDirectory(((Directory) baseElement), result));
            } else {
                list.add(wrapFile(((File) baseElement), result));
            }
        }

        result.setListElementWrapper(list);
        return result;
    }

    private FileWrapper wrapFile(File file, DirectoryWrapper parent) {
        FileWrapper result = new FileWrapper();
        result.setPackageTemplateWrapper(PackageTemplateWrapper.this);
        result.setParent(parent);
        result.setFile(file);

        return result;
    }

    public void wrapGlobals() {
        listGlobalVariableWrapper = new ArrayList<>();
        for (GlobalVariable globalVariable : packageTemplate.getListGlobalVariable()) {
            listGlobalVariableWrapper.add(new GlobalVariableWrapper(globalVariable));
        }
    }

    public void runElementsGroovyScript() {
        rootElement.accept(new RunGroovyScriptVisitor());
    }

    public void collectSimpleActions(Project project, VirtualFile virtualFile, List<SimpleAction> listSimpleAction) {
        ApplicationManager.getApplication().runReadAction(() -> {
            PsiDirectory currentDir = FileWriter.findCurrentDirectory(project, virtualFile);
            if (currentDir != null) {
                failedElements = new ArrayList<>();
                writtenElements = new ArrayList<>();
                initDefaultProperties();

                SimpleAction rootAction;
                if (packageTemplate.isSkipRootDirectory()) {
                    // Without root
                    rootAction = new DummyDirectoryAction(project, currentDir);
                    listSimpleAction.add(rootAction);
                } else {
                    // With root
                    rootAction = new CreateDirectoryAction(packageTemplate.getDirectory(), project);
                    listSimpleAction.add(wrapInDummyDirAction(rootAction, currentDir));
                }

                CollectSimpleActionVisitor visitor = new CollectSimpleActionVisitor(rootAction, project);

                for (ElementWrapper elementWrapper : rootElement.getListElementWrapper()) {
                    elementWrapper.accept(visitor);
                }
            }
        });
        //ApplicationManager.getApplication().invokeLater(() -> checkWrittenElements(project));
    }

    private SimpleAction wrapInDummyDirAction(SimpleAction simpleAction, PsiDirectory currentDir) {
        DummyDirectoryAction dummyAction = new DummyDirectoryAction(project, currentDir);
        dummyAction.addAction(simpleAction);
        return dummyAction;
    }

    private void checkWrittenElements(final Project project) {
        if (!failedElements.isEmpty()) {
            //show dialog
            new FailedFilesDialog(project, Localizer.get("ErrorDialog"), this) {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onCancel() {
                    ApplicationManager.getApplication().runWriteAction(() -> {
                        try {
                            for (int pos = writtenElements.size() - 1; pos >= 0; pos--) {
                                PsiElement item = writtenElements.get(pos);
                                item.delete();
                            }
                        } catch (Exception ex) {
                            Logger.log(ex.getMessage());
                            Logger.printStack(ex);
                        }
                    });

                }
            }.show();
        }
    }

    public void initDefaultProperties() {
        defaultProperties = FileTemplateManager.getInstance(getProject()).getDefaultProperties();
    }


    //=================================================================
    //  Getter | Setter
    //=================================================================
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ArrayList<PsiElement> getWrittenElements() {
        return writtenElements;
    }

    public void setWrittenElements(ArrayList<PsiElement> writtenElements) {
        this.writtenElements = writtenElements;
    }

    public ArrayList<ElementWrapper> getFailedElements() {
        return failedElements;
    }

    public void setFailedElements(ArrayList<ElementWrapper> failedElements) {
        this.failedElements = failedElements;
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
        return defaultProperties;
    }

}