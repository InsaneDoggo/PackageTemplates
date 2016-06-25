package custom.components;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.MouseEventHandler;
import models.TemplateContainer;
import utils.StringTools;
import utils.UIMaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import static utils.UIMaker.PADDING_LABEL;
import static utils.UIMaker.setRightPadding;

/**
 * Created by CeH9 on 25.06.2016.
 */
public class VariableView extends JPanel {
    private String key;
    private String value;

    public VariableView(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void buildView(TemplateContainer templateContainer, JPanel container, GridBag bag){
        JLabel label = new JLabel(AllIcons.Nodes.Variable, SwingConstants.LEFT);
        label.setText(key);
        setRightPadding(label, PADDING_LABEL);

        EditorTextField textField = new EditorTextField(value);
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);

        label.addMouseListener(new MouseEventHandler() {
            @Override
            protected void handle(MouseEvent event) {
                if (event.getID() == MouseEvent.MOUSE_RELEASED && SwingUtilities.isRightMouseButton(event)) {
                    JPopupMenu popupMenu = new JBPopupMenu();

                    JMenuItem itemAddVariable = new JBMenuItem("Add Variable", AllIcons.Nodes.Package);
                    JMenuItem itemDelete = new JBMenuItem("Delete", AllIcons.Actions.Delete);

                    itemAddVariable.addMouseListener(new MouseEventHandler() {
                        @Override
                        protected void handle(MouseEvent event) {
                            if (UIMaker.isLeftClick(event)) {
                                addVariable(templateContainer);
                                System.out.println("AddVariable");
                            }
                        }
                    });
                    itemDelete.addMouseListener(new MouseEventHandler() {
                        @Override
                        protected void handle(MouseEvent event) {
                            if (UIMaker.isLeftClick(event)) {
                                deleteVariable(templateContainer);
                                System.out.println("Delete");
                            }
                        }
                    });

                    popupMenu.add(itemAddVariable);
                    popupMenu.add(itemDelete);
                    popupMenu.show(label, event.getX(), event.getY());
                }
            }
        });

        container.add(label, bag.nextLine().next());
        container.add(textField, bag.next());
    }

    private void deleteVariable(TemplateContainer templateContainer) {
        templateContainer.getListVariableView().remove(this);
        templateContainer.rebuildView();
    }

    private void addVariable(TemplateContainer templateContainer) {
        templateContainer.addVariable(new VariableView("UNNAMED_VARIABLE", ""));
        templateContainer.rebuildView();
    }
}
