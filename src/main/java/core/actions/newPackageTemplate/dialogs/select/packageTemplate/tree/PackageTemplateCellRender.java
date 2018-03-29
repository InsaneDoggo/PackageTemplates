package core.actions.newPackageTemplate.dialogs.select.packageTemplate.tree;

import com.intellij.ui.ColoredTreeCellRenderer;
import global.models.PackageTemplate;
import icons.PluginIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by Arsen on 07.10.2016.
 */
public class PackageTemplateCellRender extends ColoredTreeCellRenderer {

    //todo delete?

    @Override
    public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
        if (userObject instanceof PackageTemplate) {
            PackageTemplate pt = (PackageTemplate) userObject;
            setIcon(PluginIcons.PACKAGE_TEMPLATES);
            append(pt.getName());
            return;
        }
        if (userObject instanceof String) {
            append(userObject.toString());
        }
    }

}
