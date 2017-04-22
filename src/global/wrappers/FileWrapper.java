package global.wrappers;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.ui.CreateFromTemplatePanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBCheckBox;
import core.writeRules.WriteRules;
import global.models.BaseElement;
import global.models.File;
import global.utils.i18n.Localizer;
import global.utils.templates.FileTemplateHelper;
import global.utils.validation.FieldType;
import global.utils.validation.TemplateValidator;
import global.views.IconLabel;
import global.views.IconLabelCustom;
import icons.PluginIcons;
import base.ElementVisitor;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import global.utils.*;

import javax.swing.*;
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
    public void buildView(Project project, JPanel container) {
        jlName = new JLabel(UIHelper.getIconByFileExtension(getFile().getExtension()), SwingConstants.LEFT);
        jlName.setDisabledIcon(jlName.getIcon());
        jlName.setText(getFile().getTemplateName());

        etfName = UIHelper.getEditorTextField(getFile().getName(), project);

        container.add(getOptionsPanel(), new CC().spanX().split(3));
        container.add(jlName, new CC().pad(0, 0, 0, UIHelper.PADDING_LABEL));
        container.add(etfName, new CC().wrap().pushX().growX());
        updateComponentsState();

        addMouseListener();

        createUsageUI(container, project);
    }

    private void createUsageUI(JPanel container, Project project) {
        if (getPackageTemplateWrapper().getMode() != PackageTemplateWrapper.ViewMode.USAGE) {
            return;
        }
        FileTemplate fileTemplate = FileTemplateHelper.getTemplate(
                getFile().getTemplateName(),
                project,
                getPackageTemplateWrapper().getPackageTemplate().getFileTemplateSource()
        );

        if (fileTemplate == null) {
            return;
        }

        panelVariables = new CreateFromTemplatePanel(
                AttributesHelper.getUnsetAttributes(fileTemplate, getPackageTemplateWrapper()),
                false,
                null
        );

        container.add(panelVariables.getComponent(), new CC().spanX().pad(0, UIHelper.PADDING, 0, 0).wrap());
    }

    @NotNull
    private JPanel getOptionsPanel() {
        JPanel optionsPanel = new JPanel(new MigLayout(new LC().insets("0").gridGap("2pt","0")));

        cbEnabled = new JBCheckBox();
        cbEnabled.setSelected(file.isEnabled());
        cbEnabled.addItemListener(e -> setEnabled(cbEnabled.isSelected()));
        cbEnabled.setToolTipText(Localizer.get("tooltip.IfCheckedElementWillBeCreated"));

        // Script
        jlScript = new IconLabel(
                Localizer.get("tooltip.ColoredWhenItemHasScript"),
                PluginIcons.SCRIPT,
                PluginIcons.SCRIPT_DISABLED
        );

        // CustomPath
        jlCustomPath = new IconLabel(
                Localizer.get("tooltip.ColoredWhenItemHasCustomPath"),
                PluginIcons.CUSTOM_PATH,
                PluginIcons.CUSTOM_PATH_DISABLED
        );

        // WriteRules
        jlWriteRules = new IconLabelCustom<File>(Localizer.get("tooltip.WriteRules"), file) {
            @Override
            public void onUpdateIcon(File item) {
                setIcon(item.getWriteRules().toIcon());
            }
        };

        updateOptionIcons();

        optionsPanel.add(cbEnabled, new CC());
        optionsPanel.add(jlScript, new CC());
        optionsPanel.add(jlCustomPath, new CC());
        optionsPanel.add(jlWriteRules, new CC());
        return optionsPanel;
    }

    @Override
    public void updateComponentsState() {
        updateOptionIcons();

        jlName.setEnabled(file.isEnabled());
        etfName.setEnabled(file.isEnabled());
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
