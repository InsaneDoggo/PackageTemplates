package reborn.wrappers;

import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.GridBag;
import models.PackageTemplate;
import utils.GridBagFactory;
import utils.UIMaker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Arsen on 07.07.2016.
 */
public class PackageTemplateWrapper {

    public enum ViewMode {EDIT, CREATE, USAGE}

    private Project project;

    public JPanel panel;
    public EditorTextField etfName;
    public EditorTextField etfDescription;
    public GridBag gridBag;

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

        JLabel jlName = new JLabel("Template Name");
        JLabel jlDescription = new JLabel("Description");
        UIMaker.setRightPadding(jlName, UIMaker.PADDING_LABEL);
        UIMaker.setRightPadding(jlDescription, UIMaker.PADDING_LABEL);

        etfName = UIMaker.getEditorTextField(packageTemplate.getName(), project);
        etfDescription = UIMaker.getEditorTextField(packageTemplate.getDescription(), project);

        panel.add(jlName, gridBag.nextLine().next());
        panel.add(etfName, gridBag.next());
        panel.add(jlDescription, gridBag.nextLine().next());
        panel.add(etfDescription, gridBag.next());

        //todo exec build on children

        JLabel label = new JLabel("Global Variables", JLabel.CENTER);
        panel.add(label, gridBag.nextLine().next().fillCellHorizontally().coverLine(3));

        for (GlobalVariableWrapper variableWrapper : getListGlobalVariableWrapper()) {
            variableWrapper.buildView(this, panel, gridBag);
        }

        rootElement.buildView(project, panel, gridBag, mode);

        return panel;
    }

    public void reBuildView() {
        panel.removeAll();
        buildView();
    }

    public void replaceNameVariable() {
        rootElement.replaceNameVariable(packageTemplate.getMapGlobalVars());
    }

    public void collectDataFromFields() {
        getPackageTemplate().setName(etfName.getText());
        getPackageTemplate().setDescription(etfDescription.getText());

        for (GlobalVariableWrapper variableWrapper : getListGlobalVariableWrapper()) {
            variableWrapper.collectDataFromFields();
        }

        rootElement.collectDataFromFields();
    }

}
