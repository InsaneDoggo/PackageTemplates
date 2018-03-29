package global.wrappers;

import base.ElementVisitor;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBCheckBox;
import global.models.BaseElement;
import global.models.Directory;
import global.utils.UIHelper;
import global.utils.i18n.Localizer;
import global.utils.validation.FieldType;
import global.utils.validation.TemplateValidator;
import global.views.IconLabel;
import global.views.IconLabelCustom;
import icons.PluginIcons;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
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
    public ValidationInfo validateFields() {
        ValidationInfo result = TemplateValidator.validateText(etfDescription, etfDescription.getText(), FieldType.CLASS_NAME);
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
    public void buildView(Project project, JPanel container) {
        if (panel == null) {
            panel = new JPanel(new MigLayout(new LC().insets("0").gridGapY("2pt").fillX()));
        } else {
            panel.removeAll();
        }

        jlName = new JLabel(AllIcons.Nodes.Package, SwingConstants.LEFT);
        jlName.setDisabledIcon(jlName.getIcon());
        jlName.setText(Localizer.get("Directory"));

        etfDescription = UIHelper.getEditorTextField(getDirectory().getName(), project);
        addMouseListener();

        container.add(getOptionsPanel(), new CC().spanX().split(3));
        container.add(jlName, new CC().pad(0, 0, 0, UIHelper.PADDING_LABEL));
        container.add(etfDescription, new CC().growX().pushX().wrap());

        updateComponentsState();

        //Children
        if (!getListElementWrapper().isEmpty()) {
            for (ElementWrapper elementWrapper : getListElementWrapper()) {
                elementWrapper.buildView(project, panel);
            }

            UIHelper.setLeftPadding(panel, UIHelper.PADDING + UIHelper.DEFAULT_PADDING);
            container.add(panel, new CC().spanX().growX().pushX().wrap());
        }
    }

    @NotNull
    private JPanel getOptionsPanel() {
        //  isEnabled
        cbEnabled = new JBCheckBox();
        cbEnabled.setSelected(directory.isEnabled());
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
        jlWriteRules = new IconLabelCustom<Directory>(Localizer.get("tooltip.WriteRules"), directory) {
            @Override
            public void onUpdateIcon(Directory item) {
                setIcon(item.getWriteRules().toIcon());
            }
        };

        updateOptionIcons();

        JPanel optionsPanel = new JPanel(new MigLayout(new LC().insets("0").gridGap("2pt","0")));
        optionsPanel.add(cbEnabled, new CC());
        optionsPanel.add(jlScript, new CC());
        optionsPanel.add(jlCustomPath, new CC());
        optionsPanel.add(jlWriteRules, new CC());
        return optionsPanel;
    }

    @Override
    public void updateComponentsState() {
        if (getParent() == null) {
            if (getPackageTemplateWrapper().getPackageTemplate().isSkipRootDirectory()) {
                Map attributes = jlName.getFont().getAttributes();
                attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                jlName.setFont(new Font(attributes));
            }
        }

        updateOptionIcons();

        jlName.setEnabled(directory.isEnabled());
        etfDescription.setEnabled(directory.isEnabled());
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
