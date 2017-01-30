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
import com.intellij.util.ui.GridBag;
import core.actions.custom.SimpleAction;
import global.dialogs.FailedFilesDialog;
import global.models.*;
import global.utils.Logger;
import global.utils.UIHelper;
import global.utils.factories.GridBagFactory;
import global.utils.file.FileWriter;
import global.utils.i18n.Localizer;
import global.visitors.*;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
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
    private GridBag gridBag;

    public JPanel buildView() {
        if (panel == null) {
            panel = new JPanel(new GridBagLayout());
            gridBag = GridBagFactory.getBagForPackageTemplate();
        }

        // Properties
        JPanel jpProperties = new JPanel(new MigLayout());

        if (mode != ViewMode.USAGE) {
            // Header
            JLabel jlName = new JLabel(Localizer.get("Name"));
            JLabel jlDescription = new JLabel(Localizer.get("Description"));
            UIHelper.setRightPadding(jlName, UIHelper.PADDING_LABEL);
            UIHelper.setRightPadding(jlDescription, UIHelper.PADDING_LABEL);

            etfName = UIHelper.getEditorTextField(packageTemplate.getName(), project);
            etfDescription = UIHelper.getEditorTextField(packageTemplate.getDescription(), project);

            panel.add(jlName, gridBag.nextLine().next());
            panel.add(etfName, gridBag.next().coverLine(2));
            panel.add(jlDescription, gridBag.nextLine().next());
            panel.add(etfDescription, gridBag.next().coverLine(2));

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
        jpProperties.add(cbSkipRootDirectory, new CC().wrap().spanX());
        panel.add(jpProperties, gridBag.nextLine().next().coverLine());

        panel.add(new SeparatorComponent(10), gridBag.nextLine().next().coverLine());


        // Globals
        JLabel jlGlobals = new JLabel(Localizer.get("GlobalVariables"), JLabel.CENTER);
        panel.add(jlGlobals, gridBag.nextLine().next().fillCellHorizontally().coverLine());

        for (GlobalVariableWrapper variableWrapper : getListGlobalVariableWrapper()) {
            variableWrapper.buildView(this, panel, gridBag);
        }

        // Files and Directories
        panel.add(new SeparatorComponent(10), gridBag.nextLine().next().coverLine());
        rootElement.buildView(project, panel, gridBag);

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
        gridBag = GridBagFactory.getBagForPackageTemplate();
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
     * Replace BASE_NAME and Run GROOVY
     */
    public void prepareGlobals() {
        packageTemplate.setMapGlobalVars(new HashMap<>());

        for (GlobalVariableWrapper variableWrapper : listGlobalVariableWrapper) {
            if (getMode() == ViewMode.USAGE) {
                // Replace ${BASE_NAME}
                if (!variableWrapper.getGlobalVariable().getName().equals(ATTRIBUTE_BASE_NAME)) {
                    variableWrapper.replaceBaseName(packageTemplate.getMapGlobalVars().get(ATTRIBUTE_BASE_NAME));
                }
                // GROOVY
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
                rootElement.accept(new CollectSimpleActionVisitor(currentDir, project, listSimpleAction));
            }
        });
        //ApplicationManager.getApplication().invokeLater(() -> checkWrittenElements(project));
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
                            for (int pos = writtenElements.size()-1; pos >= 0; pos--) {
                                PsiElement item = writtenElements.get(pos);
                                item.delete();
                            }
                        } catch (Exception ex) {
                            Logger.log(ex.getMessage());
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