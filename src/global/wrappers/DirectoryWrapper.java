package global.wrappers;

import base.ElementVisitor;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.GridBag;
import global.models.BaseElement;
import global.models.Directory;
import global.utils.UIHelper;
import global.utils.factories.GridBagFactory;
import global.utils.i18n.Localizer;
import global.utils.validation.FieldType;
import global.utils.validation.TemplateValidator;
import icons.PluginIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class DirectoryWrapper extends ElementWrapper {

    private Directory directory;
    private ArrayList<ElementWrapper> listElementWrapper;

    //=================================================================
    //  Utils
    //=================================================================
    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
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
        ValidationInfo result = TemplateValidator.validateText(etfName, etfName.getText(), FieldType.CLASS_NAME);
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
    public void setEnabled(boolean isEnabled) {
        directory.setEnabled(isEnabled);
        updateComponentsState();

        for (ElementWrapper elementWrapper : listElementWrapper) {
            elementWrapper.cbEnabled.setSelected(isEnabled);
        }
    }


    //=================================================================
    //  UI
    //=================================================================
    private JPanel panel;

    @Override
    public void buildView(Project project, JPanel container, GridBag bag) {
        if (panel == null) {
            panel = new JPanel(new GridBagLayout());
        } else {
            panel.removeAll();
        }

        createPackageView(project, container, bag);
        updateComponentsState();

        GridBag gridBag = GridBagFactory.getBagForDirectory();

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
        cbEnabled.addItemListener(e -> setEnabled(cbEnabled.isSelected()));

        jlGroovy = new JBLabel();
        if (directory.getGroovyCode() != null && !directory.getGroovyCode().isEmpty()) {
            jlGroovy.setIcon(PluginIcons.GROOVY);
        } else {
            jlGroovy.setIcon(PluginIcons.GROOVY_DISABLED);
        }
        jlGroovy.setToolTipText(Localizer.get("ColoredWhenItemHasGroovyScript"));
        cbEnabled.setToolTipText(Localizer.get("IfCheckedElementWillBeCreated"));

        GridBag optionsBag = GridBagFactory.getOptionsPanelGridBag();
        JPanel optionsPanel = new JPanel(new GridBagLayout());
        optionsPanel.add(cbEnabled, optionsBag.nextLine().next());
        optionsPanel.add(jlGroovy, optionsBag.next().insets(0, 0, 0, 12));
        optionsPanel.add(jlName, optionsBag.next());
        return optionsPanel;
    }

    @Override
    public void updateComponentsState() {
        if(getParent() == null){
            if(getPackageTemplateWrapper().getPackageTemplate().isSkipRootDirectory()){
                Map attributes = jlName.getFont().getAttributes();
                attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                jlName.setFont(new Font(attributes));
            }
        }

        if (directory.getGroovyCode() != null && !directory.getGroovyCode().isEmpty()) {
            jlGroovy.setIcon(PluginIcons.GROOVY);
        } else {
            jlGroovy.setIcon(PluginIcons.GROOVY_DISABLED);
        }
        jlName.setEnabled(directory.isEnabled());
        etfName.setEnabled(directory.isEnabled());
    }


    //=================================================================
    //  Getters | Setters
    //=================================================================
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

}
