package core.actions.newPackageTemplate.dialogs.select.packageTemplate;

import global.models.PackageTemplate;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

/**
 * Created by Arsen on 17.09.2016.
 */
public interface SelectPackageTemplateView {

    void setTitle(String title);

    void onSuccess(PackageTemplate packageTemplate);

    void onCancel();

    void setTemplatesList(List<PackageTemplate> list);

    void nodesWereRemoved(DefaultMutableTreeNode node, int[] childIndices, DefaultMutableTreeNode[] removedChildren);

    void nodesWereInserted(DefaultMutableTreeNode group, int[] index);

    DefaultMutableTreeNode addGroupToTree(String name);

    void nodeChanged(DefaultMutableTreeNode node);

    void selectNode(DefaultMutableTreeNode node);
}
