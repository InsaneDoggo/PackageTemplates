package reborn.wrappers;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.PsiDirectory;
import com.intellij.util.ui.GridBag;
import reborn.models.BaseElement;
import reborn.models.Directory;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class DirectoryWrapper extends ElementWrapper {

    private Directory directory;
    private JPanel panel;
    private GridBag gridBag;

    private ArrayList<ElementWrapper> listElementWrapper;

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
    }

    public ArrayList<ElementWrapper> getListElementWrapper() {
        return listElementWrapper;
    }

    public void setListElementWrapper(ArrayList<ElementWrapper> listElementWrapper) {
        this.listElementWrapper = listElementWrapper;
    }

    @Override
    public BaseElement getElement() {
        return directory;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public void replaceNameVariable(HashMap<String, String> mapVariables) {
        directory.setName(StringTools.replaceGlobalVariables(directory.getName(), mapVariables));

        for (ElementWrapper element : getListElementWrapper()) {
            element.replaceNameVariable(mapVariables);
        }
    }

    @Override
    public ValidationInfo isNameValid(List<String> listAllTemplates) {
        for (ElementWrapper element : getListElementWrapper()) {
            ValidationInfo validationInfo = element.isNameValid(listAllTemplates);
            if (validationInfo != null) {
                return validationInfo;
            }
        }
        return null;
    }

    @Override
    public ValidationInfo validateFields() {
        ValidationInfo result = TemplateValidator.validateTextField(etfName, TemplateValidator.FieldType.CLASS_NAME);
        if(result != null){
            return result;
        }
        for (ElementWrapper elementWrapper : listElementWrapper){
            result = elementWrapper.validateFields();
            if(result != null){
                return result;
            }
        }

        return null;
    }

    @Override
    public void writeFile(PsiDirectory currentDir, Project project) {
        PsiDirectory subDirectory = FileWriter.writeDirectory(currentDir, this, project);
        if (subDirectory != null) {
            for (ElementWrapper element : getListElementWrapper()) {
                element.writeFile(subDirectory, project);
            }
        }
    }

    @Override
    public void updateParents(DirectoryWrapper dwParent) {
        //deprecated
    }

    @Override
    public void initNonSerializableFields() {
        //deprecated
    }

    public void addElement(ElementWrapper element) {
        directory.getListBaseElement().add(element.getElement());
        listElementWrapper.add(element);
    }

    public void removeElement(ElementWrapper element) {
        directory.getListBaseElement().remove(element.getElement());
        listElementWrapper.remove(element);
    }

    @Override
    public void removeMyself() {
        if (getParent() == null) {
            return;
        }

        getParent().removeElement(this);
    }

    @Override
    public void buildView(Project project, JPanel container, GridBag bag) {
        if (panel == null) {
            panel = new JPanel(new GridBagLayout());
        } else {
            panel.removeAll();
        }

        createPackageView(project, container, bag);

        gridBag = GridBagFactory.getBagForDirectory();

        for (ElementWrapper elementWrapper : getListElementWrapper()) {
            elementWrapper.buildView(project, panel, gridBag);
        }

        UIMaker.setLeftPadding(panel, UIMaker.PADDING + UIMaker.DEFAULT_PADDING);
        container.add(panel, bag.nextLine().next().coverLine());
    }

    private void createPackageView(Project project, JPanel container, GridBag bag) {
        jlName = new JLabel(AllIcons.Nodes.Package, SwingConstants.LEFT);
        jlName.setText("Directory");
        UIMaker.setRightPadding(jlName, UIMaker.PADDING_LABEL);

        etfName = UIMaker.getEditorTextField(getDirectory().getName(), project);

        container.add(jlName, bag.nextLine().next());
        container.add(etfName, bag.next().coverLine(2));

        UIMaker.addMouseListener(this, project, getPackageTemplateWrapper().getMode());
    }

    @Override
    public void collectDataFromFields() {
        directory.setName(etfName.getText());
//        directory.setGroovyCode("");
//        directory.setEnabled(true);

        for (ElementWrapper elementWrapper : getListElementWrapper()) {
            elementWrapper.collectDataFromFields();
        }
    }

    @Override
    public void runGroovyScript() {

    }

}
