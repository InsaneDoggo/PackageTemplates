package wrappers;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.GridBag;
import custom.impl.ClickListener;
import groovy.GroovyDialog;
import groovy.GroovyExecutor;
import icons.JetgroovyIcons;
import org.jetbrains.annotations.NotNull;
import models.GlobalVariable;
import utils.CustomIconLoader;
import utils.GridBagFactory;
import utils.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static utils.UIHelper.PADDING_LABEL;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class GlobalVariableWrapper extends BaseWrapper {

    private GlobalVariable globalVariable;
    private EditorTextField tfKey;
    private EditorTextField tfValue;
    JLabel jlVariable;

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

    public void setTfKey(EditorTextField tfKey) {
        this.tfKey = tfKey;
    }

    public EditorTextField getTfValue() {
        return tfValue;
    }

    public void setTfValue(EditorTextField tfValue) {
        this.tfValue = tfValue;
    }

    public void buildView(PackageTemplateWrapper ptWrapper, JPanel container, GridBag bag) {
        jlVariable = new JLabel(AllIcons.Nodes.Variable, JLabel.LEFT);
        jlVariable.setDisabledIcon(jlVariable.getIcon());
        jlVariable.setText("variable");

        tfKey = new EditorTextField(globalVariable.getName());
        tfKey.setAlignmentX(Component.LEFT_ALIGNMENT);
        UIHelper.setRightPadding(tfKey, PADDING_LABEL);

        tfValue = new EditorTextField(globalVariable.getValue());
        tfValue.setAlignmentX(Component.LEFT_ALIGNMENT);

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

        tfKey.setPreferredWidth(0);
        tfValue.setPreferredWidth(0);

        container.add(createOptionsBlock(), bag.nextLine().next().weightx(0));
        container.add(tfKey, bag.next().weightx(1));
        container.add(tfValue, bag.next().weightx(1));

    }

    @NotNull
    private JPanel createOptionsBlock() {
        JPanel optionsPanel = new JPanel(new GridBagLayout());
        GridBag optionsBag = GridBagFactory.getOptionsPanelGridBag();

        jlGroovy = new JBLabel();
        if (globalVariable.getGroovyCode() != null && !globalVariable.getGroovyCode().isEmpty()) {
            jlGroovy.setIcon(CustomIconLoader.Groovy);
        } else {
            jlGroovy.setIcon(CustomIconLoader.GroovyDisabled);
        }
        jlGroovy.setToolTipText("Colored when item has GroovyScript");

        optionsPanel.add(jlGroovy, optionsBag.nextLine().next().insets(0, 0, 0, 6));
        optionsPanel.add(jlVariable, optionsBag.next());
        return optionsPanel;
    }

    @NotNull
    private JPopupMenu getPopupMenu(final PackageTemplateWrapper ptWrapper) {
        JPopupMenu popupMenu = new JBPopupMenu();

        JMenuItem itemAddVariable = new JBMenuItem("Add Variable", AllIcons.Nodes.Variable);
        JMenuItem itemDelete = new JBMenuItem("Delete", AllIcons.Actions.Delete);

        itemAddVariable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addVariable(ptWrapper);
                System.out.println("AddVariable");
            }
        });
        itemDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteVariable(ptWrapper);
                System.out.println("Delete");
            }
        });

        popupMenu.add(itemAddVariable);
        addGroovyMenuItems(popupMenu, ptWrapper.getProject());
        if (!getGlobalVariable().getName().equals(PackageTemplateWrapper.ATTRIBUTE_BASE_NAME)) {
            popupMenu.add(itemDelete);
        }
        return popupMenu;
    }

    private void addGroovyMenuItems(JPopupMenu popupMenu, Project project) {
        if (globalVariable.getGroovyCode() != null && !globalVariable.getGroovyCode().isEmpty()) {
            JMenuItem itemEditGroovy = new JBMenuItem("Edit GroovyScript", JetgroovyIcons.Groovy.Groovy_16x16);
            itemEditGroovy.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new GroovyDialog(project, globalVariable.getGroovyCode()) {
                        @Override
                        public void onSuccess(String code) {
                            globalVariable.setGroovyCode(code);
                            updateComponentsState();
                        }
                    }.show();
                }
            });
            popupMenu.add(itemEditGroovy);

            JMenuItem itemDeleteGroovy = new JBMenuItem("Delete GroovyScript", AllIcons.Actions.Delete);
            itemDeleteGroovy.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    globalVariable.setGroovyCode("");
                    updateComponentsState();
                }
            });
            popupMenu.add(itemDeleteGroovy);
        } else {
            JMenuItem itemAddGroovy = new JBMenuItem("Add GroovyScript", JetgroovyIcons.Groovy.Groovy_16x16);
            itemAddGroovy.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new GroovyDialog(project) {
                        @Override
                        public void onSuccess(String code) {
                            globalVariable.setGroovyCode(code);
                            updateComponentsState();
                        }
                    }.show();
                }
            });
            popupMenu.add(itemAddGroovy);
        }
    }

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
        gVariable.setGroovyCode("");

        ptWrapper.addGlobalVariable(new GlobalVariableWrapper(gVariable));
        ptWrapper.reBuildView();
    }

    @Override
    public void collectDataFromFields() {
        globalVariable.setName(tfKey.getText());
        globalVariable.setValue(tfValue.getText());
    }

    @Override
    public void runGroovyScript() {
        if (globalVariable.getGroovyCode() != null && !globalVariable.getGroovyCode().isEmpty()) {
            globalVariable.setValue(GroovyExecutor.runGroovy(globalVariable.getGroovyCode(), globalVariable.getValue()));
        }
    }

    @Override
    public void updateComponentsState() {
        if (globalVariable.getGroovyCode() != null && !globalVariable.getGroovyCode().isEmpty()) {
            jlGroovy.setIcon(CustomIconLoader.Groovy);
        } else {
            jlGroovy.setIcon(CustomIconLoader.GroovyDisabled);
        }
    }

}
