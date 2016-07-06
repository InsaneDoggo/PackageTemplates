package reborn.wrappers;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.GridBag;
import custom.components.VariableView;
import custom.impl.ClickListener;
import models.PackageTemplate;
import models.TemplateContainer;
import reborn.models.GlobalVariable;
import utils.Logger;
import utils.UIMaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import static utils.UIMaker.PADDING_LABEL;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class GlobalVariableWrapper {

    private GlobalVariable globalVariable;
    private EditorTextField tfKey;
    private EditorTextField tfValue;

    public GlobalVariableWrapper(GlobalVariable globalVariable, EditorTextField tfKey, EditorTextField tfValue) {
        this.globalVariable = globalVariable;
        this.tfKey = tfKey;
        this.tfValue = tfValue;
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



    public void buildView(TemplateContainer templateContainer, JPanel container, GridBag bag) {
        JLabel label = new JLabel(AllIcons.Nodes.Variable, JLabel.LEFT);

        tfKey = new EditorTextField(getGlobalVariable().getName());
        tfKey.setAlignmentX(Component.LEFT_ALIGNMENT);
        UIMaker.setHorizontalPadding(tfKey, PADDING_LABEL);


        tfValue = new EditorTextField(getGlobalVariable().getValue());
        tfValue.setAlignmentX(Component.LEFT_ALIGNMENT);

        label.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent eventOuter) {
                if (SwingUtilities.isRightMouseButton(eventOuter)) {
                    JPopupMenu popupMenu = new JBPopupMenu();

                    JMenuItem itemAddVariable = new JBMenuItem("Add Variable", AllIcons.Nodes.Variable);
                    JMenuItem itemDelete = new JBMenuItem("Delete", AllIcons.Actions.Delete);

                    itemAddVariable.addMouseListener(new ClickListener() {
                        @Override
                        public void mouseClicked(MouseEvent event) {
                            if (UIMaker.isLeftClick(event)) {
                                addVariable(templateContainer);
                                System.out.println("AddVariable");
                            }
                        }
                    });
                    itemDelete.addMouseListener(new ClickListener() {
                        @Override
                        public void mouseClicked(MouseEvent event) {
                            if (UIMaker.isLeftClick(event)) {
                                deleteVariable(templateContainer);
                                System.out.println("Delete");
                            }
                        }
                    });

                    popupMenu.add(itemAddVariable);
                    popupMenu.add(itemDelete);
                    popupMenu.show(label, eventOuter.getX(), eventOuter.getY());
                }
            }
        });

        tfKey.setPreferredWidth(0);
        tfValue.setPreferredWidth(0);

        container.add(label, bag.nextLine().next());
        container.add(tfKey, bag.next());
        container.add(tfValue, bag.next());
    }

    private void deleteVariable(TemplateContainer templateContainer) {
        if (getGlobalVariable().getName().equals(PackageTemplate.ATTRIBUTE_BASE_NAME)) {
            // TODO: 25.06.2016  error can't delete NAME var
            Logger.log("can't delete NAME var");

            return;
        }
        templateContainer.getListVariableView().remove(this);
        templateContainer.collectDataFromFields();
        templateContainer.rebuildView();
    }

    private void addVariable(TemplateContainer templateContainer) {
        templateContainer.collectDataFromFields();

        templateContainer.addVariable(new VariableView("UNNAMED_VARIABLE", ""));
        templateContainer.rebuildView();
    }

    public void collectDataFromFields() {
        getGlobalVariable().setName(tfKey.getText());
        getGlobalVariable().setValue(tfValue.getText());
    }

}
