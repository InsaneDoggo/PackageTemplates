package wrappers;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.ui.CreateFromTemplatePanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.GridBag;
import groovy.GroovyDialog;
import groovy.GroovyExecutor;
import icons.JetgroovyIcons;
import models.BaseElement;
import models.File;
import org.jetbrains.annotations.NotNull;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class FileWrapper extends ElementWrapper {

    private File file;
    private CreateFromTemplatePanel panelVariables;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

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

        if (getPackageTemplateWrapper().getMode() == PackageTemplateWrapper.ViewMode.USAGE) {
            FileTemplate fileTemplate = AttributesHelper.getTemplate(getFile().getTemplateName());
            if (fileTemplate != null) {
                String[] unsetAttributes = AttributesHelper.getUnsetAttrs(fileTemplate, getPackageTemplateWrapper().getProject());
                if (unsetAttributes != null && unsetAttributes.length > 0) {
                    panelVariables = new CreateFromTemplatePanel(unsetAttributes, false, null);

                    JComponent component = panelVariables.getComponent();
                    UIHelper.setLeftPadding(component, UIHelper.PADDING);
                    container.add(component, bag.nextLine().next().coverLine());
                }
            }
        }
    }

    @NotNull
    private JPanel createOptionsBlock() {
        JPanel optionsPanel = new JPanel(new GridBagLayout());
        GridBag optionsBag = GridBagFactory.getOptionsPanelGridBag();

        cbEnabled = new JBCheckBox();
        cbEnabled.setSelected(file.isEnabled());
        cbEnabled.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                setEnabled(cbEnabled.isSelected());
            }
        });

        jlGroovy = new JBLabel();
        if (file.getGroovyCode() != null && !file.getGroovyCode().isEmpty()) {
            jlGroovy.setIcon(CustomIconLoader.Groovy);
        } else {
            jlGroovy.setIcon(CustomIconLoader.GroovyDisabled);
        }
        jlGroovy.setToolTipText("Colored when item has GroovyScript");
        cbEnabled.setToolTipText("If checked, element will be created;");

        optionsPanel.add(cbEnabled, optionsBag.nextLine().next());
        optionsPanel.add(jlGroovy, optionsBag.next().insets(0, 0, 0, 12));
        optionsPanel.add(jlName, optionsBag.next());
        return optionsPanel;
    }

    @Override
    public void addElement(ElementWrapper element) {
        getParent().addElement(element);
    }

    @Override
    public BaseElement getElement() {
        return file;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public void replaceNameVariable(HashMap<String, String> mapVariables) {
        getFile().setName(StringTools.replaceGlobalVariables(getFile().getName(), mapVariables));
    }

    @Override
    public ValidationInfo isNameValid(List<String> listAllTemplates) {
        if (!listAllTemplates.contains(getFile().getTemplateName())) {
            return new ValidationInfo("Template \"" + getFile().getTemplateName() + "\" doesn't exist!");
        }
        return null;
    }

    @Override
    public ValidationInfo validateFields() {
        return TemplateValidator.validateText(etfName, etfName.getText(), TemplateValidator.FieldType.CLASS_NAME);
    }

    @Override
    public void writeFile(PsiDirectory currentDir, Project project) {
        if (!file.isEnabled()) {
            return;
        }

        PsiElement psiElement = FileWriter.writeFile(currentDir, this);
        if (psiElement == null) {
            // TODO: 20.06.2016 error write file
        }
    }

    @Override
    public void updateParents(DirectoryWrapper dwParent) {

    }

    @Override
    public void initNonSerializableFields() {

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
    public void collectDataFromFields() {
        file.setTemplateName(jlName.getText());
        file.setName(etfName.getText());

        if (getPackageTemplateWrapper().getMode() == PackageTemplateWrapper.ViewMode.USAGE) {
            getFile().setMapProperties(new HashMap<>());
            if (panelVariables != null) {
                Properties properties = new Properties();
                properties = panelVariables.getProperties(properties);

                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    getFile().getMapProperties().put((String) entry.getKey(), (String) entry.getValue());
                }
            }
            //add GLOBALS
            getFile().getMapProperties().putAll(getPackageTemplateWrapper().getPackageTemplate().getMapGlobalVars());
        }

    }

    @Override
    public void runGroovyScript() {
        if (file.getGroovyCode() != null && !file.getGroovyCode().isEmpty()) {
            file.setName(GroovyExecutor.runGroovy(file.getGroovyCode(), file.getName()));
        }
    }

    @Override
    public void updateComponentsState() {
        if (file.getGroovyCode() != null && !file.getGroovyCode().isEmpty()) {
            jlGroovy.setIcon(CustomIconLoader.Groovy);
        } else {
            jlGroovy.setIcon(CustomIconLoader.GroovyDisabled);
        }
        jlName.setEnabled(file.isEnabled());
        etfName.setEnabled(file.isEnabled());
    }

}
