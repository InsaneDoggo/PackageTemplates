package custom.view;

import com.intellij.ui.components.JBLabel;

import javax.swing.*;

/**
 * Created by Arsen on 16.08.2016.
 */
public class SpoilerPane extends JPanel {
    private JButton jbToggle;
    private JComponent container;
    private Icon iconExpand;
    private Icon iconCollapse;
    private JBLabel jlTitle;

    public SpoilerPane(JComponent content, Icon iconExpand, Icon iconCollapse) {
        this.container = content;
        this.iconExpand = iconExpand;
        this.iconCollapse = iconCollapse;

        this.setBackground(content.getBackground());

//        CollapsiblePanel
    }
}
