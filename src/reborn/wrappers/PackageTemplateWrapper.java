package reborn.wrappers;

import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.GridBag;
import models.PackageTemplate;
import org.omg.CORBA.PUBLIC_MEMBER;
import utils.InputManager;
import utils.UIMaker;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Arsen on 07.07.2016.
 */
public class PackageTemplateWrapper {

    public enum ViewMode { EDIT, CREATE, USAGE }

    public JPanel panel;
    public EditorTextField etfName;
    public EditorTextField etfDescription;
    public GridBag gridBag;

    private PackageTemplate packageTemplate;
    private DirectoryWrapper rootElement;
    private ArrayList<GlobalVariableWrapper> listGlobalVariableWrapper;
    private ViewMode mode;

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

    public JPanel buildView(Project project){
        JLabel jlName = new JLabel("Template Name");
        JLabel jlDescription = new JLabel("Description..");
        UIMaker.setRightPadding(jlName, UIMaker.PADDING_LABEL);
        UIMaker.setRightPadding(jlDescription, UIMaker.PADDING_LABEL);

        etfName = UIMaker.getEditorTextField(packageTemplate.getName(), project);
        etfDescription = UIMaker.getEditorTextField(packageTemplate.getDescription(), project);

        panel.add(jlName, gridBag.nextLine().next());
        panel.add(etfName, gridBag.next());
        panel.add(jlDescription, gridBag.nextLine().next());
        panel.add(etfDescription, gridBag.next());

        //todo exec build on children

        return panel;
    }

    public void reBuild(Project project) {
        // TODO: 07.07.2016 rebuild
        rootElement.buildView(project, panel, gridBag, mode);
    }

    public void replaceNameVariable() {
        rootElement.replaceNameVariable(packageTemplate.getMapGlobalVars());
    }
}
