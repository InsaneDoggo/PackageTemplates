package wrappers;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.PsiDirectory;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.GridBag;
import groovy.GroovyExecutor;
import models.BaseElement;
import models.Directory;
import org.jetbrains.annotations.NotNull;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
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
        ValidationInfo result = TemplateValidator.validateText(etfName, etfName.getText(), TemplateValidator.FieldType.CLASS_NAME);
        if (result != null) {
            return result;
        }
        for (ElementWrapper elementWrapper : listElementWrapper) {
            result = elementWrapper.validateFields();
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    @Override
    public void writeFile(PsiDirectory currentDir, Project project) {
        if (!directory.isEnabled()) {
            return;
        }
        if (getParent() == null && getPackageTemplateWrapper().getPackageTemplate().isSkipRootDirectory()) {
            // Only files without Directory
            for (ElementWrapper element : getListElementWrapper()) {
                element.writeFile(currentDir, project);
            }
        } else {
            PsiDirectory subDirectory = FileWriter.writeDirectory(currentDir, this, project);
            if (subDirectory != null) {
                for (ElementWrapper element : getListElementWrapper()) {
                    element.writeFile(subDirectory, project);
                }
            } else {
                getPackageTemplateWrapper().getFailedElements().add(this);
            }
        }
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
        updateComponentsState();

        gridBag = GridBagFactory.getBagForDirectory();

        for (ElementWrapper elementWrapper : getListElementWrapper()) {
            elementWrapper.buildView(project, panel, gridBag);
        }

        UIHelper.setLeftPadding(panel, UIHelper.PADDING + UIHelper.DEFAULT_PADDING);
        container.add(panel, bag.nextLine().next().coverLine());
    }

    private void createPackageView(Project project, JPanel container, GridBag bag) {
        jlName = new JLabel(AllIcons.Nodes.Package, SwingConstants.LEFT);
        jlName.setDisabledIcon(jlName.getIcon());
        jlName.setText(Localizer.get("Directory"));
        UIHelper.setRightPadding(jlName, UIHelper.PADDING_LABEL);

        etfName = UIHelper.getEditorTextField(getDirectory().getName(), project);

        container.add(createOptionsBlock(), bag.nextLine().next());
        container.add(etfName, bag.next().coverLine(2));

        addMouseListener();
    }

    @NotNull
    private JPanel createOptionsBlock() {
        cbEnabled = new JBCheckBox();
        cbEnabled.setSelected(directory.isEnabled());
        cbEnabled.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                setEnabled(cbEnabled.isSelected());
            }
        });

        jlGroovy = new JBLabel();
        if (directory.getGroovyCode() != null && !directory.getGroovyCode().isEmpty()) {
            jlGroovy.setIcon(CustomIconLoader.Groovy);
        } else {
            jlGroovy.setIcon(CustomIconLoader.GroovyDisabled);
        }
        jlGroovy.setToolTipText(Localizer.get("ColoredWhenItemHasGroovyScript"));
        cbEnabled.setToolTipText(Localizer.get("IfCheckedElementWillBeCreated"));

        JPanel optionsPanel = new JPanel(new GridBagLayout());
        GridBag optionsBag = GridBagFactory.getOptionsPanelGridBag();
        optionsPanel.add(cbEnabled, optionsBag.nextLine().next());
        optionsPanel.add(jlGroovy, optionsBag.next().insets(0, 0, 0, 12));
        optionsPanel.add(jlName, optionsBag.next());
        return optionsPanel;
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        directory.setEnabled(isEnabled);
        updateComponentsState();

        for (ElementWrapper elementWrapper : listElementWrapper) {
            elementWrapper.cbEnabled.setSelected(isEnabled);
        }
    }

    @Override
    public void updateComponentsState() {
        if (directory.getGroovyCode() != null && !directory.getGroovyCode().isEmpty()) {
            jlGroovy.setIcon(CustomIconLoader.Groovy);
        } else {
            jlGroovy.setIcon(CustomIconLoader.GroovyDisabled);
        }
        jlName.setEnabled(directory.isEnabled());
        etfName.setEnabled(directory.isEnabled());
    }

    @Override
    public void collectDataFromFields() {
        directory.setName(etfName.getText());

        for (ElementWrapper elementWrapper : getListElementWrapper()) {
            elementWrapper.collectDataFromFields();
        }
    }

    @Override
    public void runGroovyScript() {
        if (directory.getGroovyCode() != null && !directory.getGroovyCode().isEmpty()) {
            directory.setName(GroovyExecutor.runGroovy(directory.getGroovyCode(), directory.getName()));
        }

        for (ElementWrapper elementWrapper : listElementWrapper) {
            elementWrapper.runGroovyScript();
        }
    }

}
