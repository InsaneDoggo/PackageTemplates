package global.wrappers;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.ui.EditorTextField;
import global.listeners.ClickListener;
import core.script.ScriptDialog;
import core.script.ScriptExecutor;
import global.utils.StringTools;
import global.utils.highligt.HighlightHelper;
import global.views.IconLabel;
import icons.PluginIcons;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import global.models.GlobalVariable;
import global.utils.i18n.Localizer;
import global.utils.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static global.utils.UIHelper.PADDING_LABEL;
import static global.wrappers.PackageTemplateWrapper.ATTRIBUTE_BASE_NAME;
import static global.wrappers.PackageTemplateWrapper.PATTERN_BASE_NAME;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class GlobalVariableWrapper extends BaseWrapper {

    private GlobalVariable globalVariable;

    //=================================================================
    //  UI
    //=================================================================
    private EditorTextField tfKey;
    private EditorTextField tfValue;
    private JLabel jlVariable;

    public void buildView(PackageTemplateWrapper ptWrapper, JPanel container) {
        jlVariable = new JLabel(AllIcons.Nodes.Variable, JLabel.LEFT);
        jlVariable.setDisabledIcon(jlVariable.getIcon());
        jlVariable.setText("variable");

        tfKey = new EditorTextField(globalVariable.getName());
        tfKey.setAlignmentX(Component.LEFT_ALIGNMENT);
        UIHelper.setRightPadding(tfKey, PADDING_LABEL);

        tfValue = new EditorTextField(globalVariable.getValue());
        tfValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (!globalVariable.getName().equals(ATTRIBUTE_BASE_NAME)) {
            tfValue.addSettingsProvider(editor -> {
                HighlightHelper.addHighlightListener(ptWrapper.getProject(), tfValue, editor, StringTools.PATTERN_BASE_NAME);
                HighlightHelper.applyHighlightRange(HighlightHelper.findResults(tfValue.getText(), StringTools.PATTERN_BASE_NAME), ptWrapper.getProject(), editor);
            });
        }

        if (ptWrapper.getMode() == PackageTemplateWrapper.ViewMode.USAGE) {
            tfKey.setEnabled(false);
        } else {
            jlVariable.addMouseListener(new ClickListener() {
                @Override
                public void mouseClicked(MouseEvent eventOuter) {
                    if (SwingUtilities.isRightMouseButton(eventOuter)) {
                        JPopupMenu popupMenu = getPopupMenu(ptWrapper);
                        popupMenu.show(jlVariable, eventOuter.getX(), eventOuter.getY());
                    }
                }
            });
        }

        // Lock modifying BASE_NAME
        if (getGlobalVariable().getName().equals(ATTRIBUTE_BASE_NAME)) {
            tfKey.setEnabled(false);
        }

        container.add(createOptionsBlock(), new CC().spanX().split(3));
        container.add(tfKey, new CC().width("0").pushX().growX());
        container.add(tfValue, new CC().width("0").pushX().growX().wrap());
    }

    @NotNull
    private JPanel createOptionsBlock() {
        JPanel optionsPanel = new JPanel(new MigLayout(new LC()));

        // Groovy
        jlGroovy = new IconLabel(
                Localizer.get("tooltip.ColoredWhenItemHasScript"),
                PluginIcons.SCRIPT,
                PluginIcons.SCRIPT_DISABLED
        );

        updateComponentsState();

        optionsPanel.add(jlGroovy, new CC().pad(0, 0, 0, 6));
        optionsPanel.add(jlVariable, new CC());
        return optionsPanel;
    }

    @NotNull
    private JPopupMenu getPopupMenu(final PackageTemplateWrapper ptWrapper) {
        JPopupMenu popupMenu = new JBPopupMenu();

        JMenuItem itemAddVariable = new JBMenuItem(Localizer.get("AddVariable"), AllIcons.Nodes.Variable);
        JMenuItem itemDelete = new JBMenuItem(Localizer.get("Delete"), AllIcons.Actions.Delete);

        itemAddVariable.addActionListener(e -> addVariable(ptWrapper));
        itemDelete.addActionListener(e -> deleteVariable(ptWrapper));

        popupMenu.add(itemAddVariable);
        addScriptMenuItems(popupMenu, ptWrapper.getProject());
        if (!getGlobalVariable().getName().equals(ATTRIBUTE_BASE_NAME)) {
            popupMenu.add(itemDelete);
        }
        return popupMenu;
    }

    private void addScriptMenuItems(JPopupMenu popupMenu, Project project) {
        if (globalVariable.getScript() != null && !globalVariable.getScript().isEmpty()) {
            JMenuItem itemEditGroovy = new JBMenuItem(Localizer.get("EditScript"), PluginIcons.SCRIPT);
            itemEditGroovy.addActionListener(e -> new ScriptDialog(project, globalVariable.getScript()) {
                @Override
                public void onSuccess(String code) {
                    globalVariable.setScript(code);
                    updateComponentsState();
                }
            }.show());
            popupMenu.add(itemEditGroovy);

            JMenuItem itemDeleteGroovy = new JBMenuItem(Localizer.get("DeleteScript"), AllIcons.Actions.Delete);
            itemDeleteGroovy.addActionListener(e -> {
                globalVariable.setScript("");
                updateComponentsState();
            });
            popupMenu.add(itemDeleteGroovy);
        } else {
            JMenuItem itemAddGroovy = new JBMenuItem(Localizer.get("AddScript"), PluginIcons.SCRIPT);
            itemAddGroovy.addActionListener(e -> new ScriptDialog(project) {
                @Override
                public void onSuccess(String code) {
                    globalVariable.setScript(code);
                    updateComponentsState();
                }
            }.show());
            popupMenu.add(itemAddGroovy);
        }
    }


    //=================================================================
    //  Utils
    //=================================================================
    private void deleteVariable(PackageTemplateWrapper ptWrapper) {
        ptWrapper.removeGlobalVariable(this);

        ptWrapper.collectDataFromFields();
        ptWrapper.reBuildView();
    }

    private void addVariable(PackageTemplateWrapper ptWrapper) {
        ptWrapper.collectDataFromFields();

        GlobalVariable gVariable = new GlobalVariable();
        gVariable.setName("UNNAMED_VARIABLE");
        gVariable.setValue("");
        gVariable.setEnabled(true);
        gVariable.setScript("");

        ptWrapper.addGlobalVariable(new GlobalVariableWrapper(gVariable));
        ptWrapper.reBuildView();
    }

    public void collectDataFromFields() {
        globalVariable.setName(tfKey.getText());
        globalVariable.setValue(tfValue.getText());
    }

    public void runGroovyScript() {
        if (globalVariable.getScript() != null && !globalVariable.getScript().isEmpty()) {
            globalVariable.setValue(ScriptExecutor.runScript(globalVariable.getScript(), globalVariable.getValue()));
        }
    }

    @Override
    public void updateComponentsState() {
        if (globalVariable.getScript() != null && !globalVariable.getScript().isEmpty()) {
            jlGroovy.enableIcon();
        } else {
            jlGroovy.disableIcon();
        }
    }

    public void replaceBaseName(String baseName) {
        globalVariable.setValue(globalVariable.getValue().replace(PATTERN_BASE_NAME, baseName));
    }


    //=================================================================
    //  Getter | Setter
    //=================================================================
    public GlobalVariableWrapper(GlobalVariable globalVariable) {
        this.globalVariable = globalVariable;
    }

    public GlobalVariable getGlobalVariable() {
        return globalVariable;
    }

    public void setGlobalVariable(GlobalVariable globalVariable) {
        this.globalVariable = globalVariable;
    }

    public EditorTextField getTfKey() {
        return tfKey;
    }

    public EditorTextField getTfValue() {
        return tfValue;
    }

}
