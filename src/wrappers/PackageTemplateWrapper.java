package wrappers;

import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.SeparatorComponent;
import com.intellij.util.ui.GridBag;
import models.PackageTemplate;
import models.BaseElement;
import models.Directory;
import models.File;
import models.GlobalVariable;
import utils.GridBagFactory;
import utils.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Arsen on 07.07.2016.
 */
public class PackageTemplateWrapper {

    public static final java.lang.String ATTRIBUTE_BASE_NAME = "BASE_NAME";

    public enum ViewMode {EDIT, CREATE, USAGE}

    private Project project;

    private JPanel panel;
    public EditorTextField etfName;
    public EditorTextField etfDescription;
    private GridBag gridBag;

    private PackageTemplate packageTemplate;
    private DirectoryWrapper rootElement;
    private ArrayList<GlobalVariableWrapper> listGlobalVariableWrapper;
    private ViewMode mode;

    public PackageTemplateWrapper(Project project) {
        this.project = project;
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

    public JPanel buildView() {
        if (panel == null) {
            panel = new JPanel(new GridBagLayout());
            gridBag = GridBagFactory.getBagForPackageTemplate();
        }

        if (mode != ViewMode.USAGE) {
            // Header
            JLabel jlName = new JLabel("Name");
            JLabel jlDescription = new JLabel("Description");
            UIHelper.setRightPadding(jlName, UIHelper.PADDING_LABEL);
            UIHelper.setRightPadding(jlDescription, UIHelper.PADDING_LABEL);

            etfName = UIHelper.getEditorTextField(packageTemplate.getName(), project);
            etfDescription = UIHelper.getEditorTextField(packageTemplate.getDescription(), project);

            panel.add(jlName, gridBag.nextLine().next());
            panel.add(etfName, gridBag.next().coverLine(2));
            panel.add(jlDescription, gridBag.nextLine().next());
            panel.add(etfDescription, gridBag.next().coverLine(2));

            panel.add(new SeparatorComponent(10), gridBag.nextLine().next().coverLine());
        }

        // Globals
        JLabel jlGlobals = new JLabel("Global Variables", JLabel.CENTER);
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

    public void replaceNameVariable() {
        rootElement.replaceNameVariable(packageTemplate.getMapGlobalVars());
    }

    public void collectDataFromFields() {
        if( getMode() != ViewMode.USAGE ) {
            packageTemplate.setName(etfName.getText());
            packageTemplate.setDescription(etfDescription.getText());
        }

        packageTemplate.setMapGlobalVars(new HashMap<>());
        for (GlobalVariableWrapper variableWrapper : listGlobalVariableWrapper) {
            variableWrapper.collectDataFromFields();
            packageTemplate.getMapGlobalVars().put(variableWrapper.getGlobalVariable().getName(), variableWrapper.getGlobalVariable().getValue());
        }

        rootElement.collectDataFromFields();
    }

    public void wrapPackageTemplate(PackageTemplateWrapper.ViewMode mode) {
        setMode(PackageTemplateWrapper.ViewMode.EDIT);
        setPackageTemplate(packageTemplate);
        wrapGlobals();
        setRootElement(wrapDirectory(packageTemplate.getDirectory(), null));
        setProject(project);
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

}