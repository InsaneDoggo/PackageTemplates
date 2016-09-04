package global.views;

import com.intellij.ui.components.JBLabel;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Created by Arsen on 16.08.2016.
 */
public class SpoilerPane extends JPanel {

    private JButton jbToggle;
    private JComponent content;
    private Icon iconExpand;
    private Icon iconCollapse;
    private JBLabel jlTitle;

    // CollapsiblePanel

    public SpoilerPane(JComponent content, Icon iconExpand, Icon iconCollapse, String title) {
        this.content = content;
        this.iconExpand = iconExpand;
        this.iconCollapse = iconCollapse;
        jbToggle = new JButton();
        jbToggle.setOpaque(false);
        jbToggle.setBorderPainted(false);
        jbToggle.setBorder(new EmptyBorder(0, 0, 0, 0));
        jlTitle = new JBLabel(title);

        init();
    }

    public void init() {
        setLayout(new MigLayout());
        this.setBackground(content.getBackground());

//        add(jbToggle, bag.nextLine().next().fillCellVertically());
        add(jbToggle, new CC().alignX("left"));
//        add(jlTitle, bag.next().fillCellVertically().anchor(GridBagConstraints.NORTHWEST));
        add(jlTitle, new CC().wrap().alignX("left"));
        setCollapsed(true);

        this.jbToggle.addActionListener(e -> setCollapsed(!isCollapsed));
    }

    private boolean isInitialized;
    private boolean isCollapsed;

    protected void setCollapsed(boolean collapse) {
        try {
            if (collapse) {
                if (isInitialized) {
                    remove(content);
                }
            } else {
//                add(content, bag.nextLine().next().coverLine().setLine(1));
                add(content, "cell 0 1, span");
            }

            isCollapsed = collapse;
            jbToggle.setIcon(getIcon());

            if (collapse) {
                setFocused();
                setSelected(true);
            } else {
                content.requestFocusInWindow();
            }

//            notifyListners();
            revalidate();
            repaint();
        } finally {
            isInitialized = true;
        }
    }

    public void setFocused() {
        jbToggle.requestFocusInWindow();
    }

    public void setSelected(boolean selected) {
        jbToggle.setSelected(selected);
    }

    private Icon getIcon() {
        return isCollapsed ? iconExpand : iconCollapse;
    }

}
