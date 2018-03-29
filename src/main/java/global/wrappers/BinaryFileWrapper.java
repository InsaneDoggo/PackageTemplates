package global.wrappers;

import base.ElementVisitor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBCheckBox;
import global.models.BaseElement;
import global.models.BinaryFile;
import global.utils.UIHelper;
import global.utils.i18n.Localizer;
import global.views.IconLabel;
import global.views.IconLabelCustom;
import icons.PluginIcons;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class BinaryFileWrapper extends ElementWrapper {

    private BinaryFile binaryFile;

    //=================================================================
    //  UI
    //=================================================================
    @Override
    public void buildView(Project project, JPanel container) {
        File file = new File(getBinaryFile().getSourcePath());
        jlName = new JLabel("Binary File:      " + file.getName());
//        jlName.setDisabledIcon(jlName.getIcon());

        container.add(getOptionsPanel(), new CC().spanX().split(3));
        container.add(jlName, new CC().pushX().growX().wrap());
        updateComponentsState();

        addMouseListener();
    }

    @NotNull
    private JPanel getOptionsPanel() {
        JPanel optionsPanel = new JPanel(new MigLayout(new LC().insets("0").gridGap("2pt", "0")));

        cbEnabled = new JBCheckBox();
        cbEnabled.setSelected(binaryFile.isEnabled());
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
        jlWriteRules = new IconLabelCustom<BinaryFile>(Localizer.get("tooltip.WriteRules"), binaryFile) {
            @Override
            public void onUpdateIcon(BinaryFile item) {
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

        jlName.setEnabled(binaryFile.isEnabled());
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
    public ValidationInfo validateFields() {
        return null;
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        cbEnabled.setSelected(isEnabled);
        binaryFile.setEnabled(isEnabled);
        updateComponentsState();
    }

    @Override
    public void removeMyself() {
        getParent().removeElement(this);
    }


    //=================================================================
    //  Getter | setter
    //=================================================================
    public BinaryFile getBinaryFile() {
        return binaryFile;
    }

    public void setBinaryFile(BinaryFile binaryFile) {
        this.binaryFile = binaryFile;
    }

    @Override
    public BaseElement getElement() {
        return binaryFile;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

}
