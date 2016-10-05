package core.actions.newPackageTemplate.dialogs.select.packageTemplate.controls;

import com.intellij.util.ui.GridBag;

import javax.swing.*;
import java.util.List;

/**
 * Created by Arsen on 06.10.2016.
 */
public class ControlsAdapter {

    private JPanel container;
    private List<Control> list;
    private GridBag bag;

    public ControlsAdapter(JPanel container, List<Control> list, GridBag bag) {
        this.container = container;
        this.list = list;
        this.bag = bag;
    }

    public void buildView() {
        boolean isFirst = true;

        for (Control control : list) {
            if (isFirst) {
                isFirst = false;
                container.add(getView(control), bag.nextLine().next());
            } else {
                container.add(getView(control), bag.next());
            }
        }
    }

    private JComponent getView(Control control) {
        JButton button = new JButton(control.getIcon());
        button.addMouseListener(control.getReleaseListener());
        button.setBorder(BorderFactory.createEmptyBorder());

        return button;
    }


}
