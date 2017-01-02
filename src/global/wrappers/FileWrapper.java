package global.wrappers;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.ui.CreateFromTemplatePanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.GridBag;
import global.models.BaseElement;
import global.models.File;
import global.utils.factories.GridBagFactory;
import global.utils.i18n.Localizer;
import global.utils.templates.FileTemplateHelper;
import global.utils.validation.FieldType;
import global.utils.validation.TemplateValidator;
import icons.PluginIcons;
import base.ElementVisitor;
import org.jetbrains.annotations.NotNull;
import global.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class FileWrapper extends ElementWrapper {

    private File file;

    //=================================================================
    //  UI
    //=================================================================
    public CreateFromTemplatePanel panelVariables;

    @Override
    public void buildView(Project project, JPanel container, GridBag bag) {
        jlName = new JLabel(UIHelper.getIconByFileExtension(getFile().getExtension()), SwingConstants.LEFT);
        jlName.setDisabledIcon(jlName.getIcon());
        jlName.setText(getFile().getTemplateName());
        UIHelper.setRightPadding(jlName, UIHelper.PADDING_LABEL);

        etfName = UIHelper.getEditorTextField(getFile().getName(), project);

        container.add(createOptionsBlock(), bag.nextLine().next());
        container.add(etfName, bag.next().coverLine(2));
        updateComponentsState();

        addMouseListener();

        createUssageUI(container, bag);
    }

    private void createUssageUI(JPanel container, GridBag bag) {
        if (getPackageTemplateWrapper().getMode() != PackageTemplateWrapper.ViewMode.USAGE) {
            return;
        }
        FileTemplate fileTemplate = FileTemplateHelper.getTemplate(getFile().getTemplateName());
        if (fileTemplate == null) {
            return;
        }

        String[] unsetAttributes = AttributesHelper.getUnsetAttrs(fileTemplate, getPackageTemplateWrapper().getProject());
        if (unsetAttributes == null || unsetAttributes.length <= 0) {
            return;
        }

        panelVariables = new CreateFromTemplatePanel(AttributesHelper.getNonGlobalAttributes(
                unsetAttributes,
                getPackageTemplateWrapper().getPackageTemplate().getListGlobalVariable()),
                false,
                null
        );

        JComponent component = panelVariables.getComponent();
        UIHelper.setLeftPadding(component, UIHelper.PADDING);
        container.add(component, bag.nextLine().next().coverLine());
    }

    @NotNull
    private JPanel createOptionsBlock() {
        JPanel optionsPanel = new JPanel(new GridBagLayout());
        GridBag optionsBag = GridBagFactory.getOptionsPanelGridBag();

        cbEnabled = new JBCheckBox();
        cbEnabled.setSelected(file.isEnabled());
        cbEnabled.addItemListener(e -> setEnabled(cbEnabled.isSelected()));

        jlGroovy = new JBLabel();
        if (file.getGroovyCode() != null && !file.getGroovyCode().isEmpty()) {
            jlGroovy.setIcon(PluginIcons.GROOVY);
        } else {
            jlGroovy.setIcon(PluginIcons.GROOVY_DISABLED);
        }
        jlGroovy.setToolTipText(Localizer.get("ColoredWhenItemHasGroovyScript"));
        cbEnabled.setToolTipText(Localizer.get("IfCheckedElementWillBeCreated"));

        optionsPanel.add(cbEnabled, optionsBag.nextLine().next());
        optionsPanel.add(jlGroovy, optionsBag.next().insets(0, 0, 0, 12));
        optionsPanel.add(jlName, optionsBag.next());
        return optionsPanel;
    }


    //=================================================================
    //  Utils
    //=================================================================
    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void addElement(ElementWrapper element) {
        getParent().addElement(element);
    }

    @Override
    public ValidationInfo isNameValid(List<String> listAllTemplates) {
        if (!listAllTemplates.contains(getFile().getTemplateName())) {
            return new ValidationInfo(String.format(Localizer.get("warning.TemplateSDoesntExist"), getFile().getTemplateName()));
        }
        return null;
    }

    @Override
    public ValidationInfo validateFields() {
        return TemplateValidator.validateText(etfName, etfName.getText(), FieldType.CLASS_NAME);
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        cbEnabled.setSelected(isEnabled);
        file.setEnabled(isEnabled);
        updateComponentsState();
    }

    @Override
    public void removeMyself() {
        getParent().removeElement(this);
    }

    @Override
    public void updateComponentsState() {
        if (file.getGroovyCode() != null && !file.getGroovyCode().isEmpty()) {
            jlGroovy.setIcon(PluginIcons.GROOVY);
        } else {
            jlGroovy.setIcon(PluginIcons.GROOVY_DISABLED);
        }
        jlName.setEnabled(file.isEnabled());
        etfName.setEnabled(file.isEnabled());
    }


    //=================================================================
    //  Getter | setter
    //=================================================================
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public BaseElement getElement() {
        return file;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

}
