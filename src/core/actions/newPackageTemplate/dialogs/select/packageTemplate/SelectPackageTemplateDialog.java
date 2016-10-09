package core.actions.newPackageTemplate.dialogs.select.packageTemplate;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.PlatformIcons;
import com.intellij.util.containers.HashMap;
import core.actions.newPackageTemplate.dialogs.select.packageTemplate.tree.PackageTemplateCellRender;
import global.Const;
import global.models.PackageTemplate;
import global.utils.i18n.Localizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.util.List;

/**
 * Created by CeH9 on 24.06.2016.
 */
public abstract class SelectPackageTemplateDialog extends DialogWrapper implements SelectPackageTemplateView, TreeSelectionListener {

    private static final int MIN_WIDTH = 600;
    private static final int MIN_HEIGHT = 520;

    public abstract void onSuccess(PackageTemplate packageTemplate);

    public abstract void onCancel();

    private JPanel panel;
    private Tree tree;
    private Project project;
    private PackageTemplate selectedTemplate;
    private SelectPackageTemplatePresenter presenter;

    protected SelectPackageTemplateDialog(@Nullable Project project) {
        super(project);
        presenter = new SelectPackageTemplatePresenterImpl(this, project);
        this.project = project;
        groups = new HashMap<>();
        init();
    }

    @Override
    protected ValidationInfo doValidate() {
        return presenter.doValidate(selectedTemplate, tree);
    }

    @Override
    public void show() {
        super.show();

        switch (getExitCode()) {
            case DialogWrapper.OK_EXIT_CODE:
                presenter.onSuccess(selectedTemplate);
                break;
            case DialogWrapper.CANCEL_EXIT_CODE:
                presenter.onCancel();
                break;
        }
    }

    @NotNull
    @Override
    protected Action getOKAction() {
        Action action = super.getOKAction();
        action.putValue(Action.NAME, Localizer.get("action.Select"));
        return action;
    }

    @NotNull
    @Override
    protected Action getCancelAction() {
        Action action = super.getCancelAction();
        action.putValue(Action.NAME, Localizer.get("action.Cancel"));
        return action;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return tree;
    }

    @Override
    protected JComponent createCenterPanel() {
        createTree();
        ToolbarDecorator tbDecorator = ToolbarDecorator
                .createDecorator(tree)
                .setAddAction(action -> presenter.onAddAction(action, selectedNode))
                .setRemoveAction(anActionButton -> presenter.onDeleteAction(selectedNode))
                .setEditAction(anActionButton -> presenter.onEditAction(selectedTemplate, selectedNode))
                .addExtraAction(new AnActionButton("Export", PlatformIcons.EXPORT_ICON) {
                    @Override
                    public void actionPerformed(AnActionEvent e) {
                        presenter.onExportAction();
                    }
                })
                .addExtraAction(new AnActionButton("Setting", PlatformIcons.SHOW_SETTINGS_ICON) {
                    @Override
                    public void actionPerformed(AnActionEvent e) {
                        presenter.onSettingsAction();
                    }
                });

        panel = tbDecorator.createPanel();
        panel.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));

        return panel;
    }

    private DefaultMutableTreeNode rootNode;
    private HashMap<String, DefaultMutableTreeNode> groups;

    private Tree createTree() {
        rootNode = new DefaultMutableTreeNode("Package Templates");
        presenter.setTreeRootNode(rootNode);
        presenter.setGroups(groups);
        presenter.loadTemplates();

        tree = new Tree(rootNode);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setRootVisible(false);
        tree.setCellRenderer(new PackageTemplateCellRender());
        tree.addTreeSelectionListener(this);

        DefaultMutableTreeNode nodeToSelect = groups.get(Const.NODE_GROUP_DEFAULT);
        if (nodeToSelect.getChildCount() > 0) {
            nodeToSelect = (DefaultMutableTreeNode) nodeToSelect.getFirstChild();
            selectedTemplate = (PackageTemplate) nodeToSelect.getUserObject();
        }
        selectNode(nodeToSelect);

        return tree;
    }

    @Override
    public void setTemplatesList(List<PackageTemplate> list) {
        rootNode.removeAllChildren();
        groups.clear();
        addGroupToTree(Const.NODE_GROUP_DEFAULT);

        for (PackageTemplate pt : list) {
            DefaultMutableTreeNode group;
            if (pt.getGroupName() == null) {
                group = groups.get(Const.NODE_GROUP_DEFAULT);
            } else {
                group = groups.get(pt.getGroupName());
                if (group == null) {
                    group = addGroupToTree(pt.getGroupName());
                }
            }
            group.add(new DefaultMutableTreeNode(pt));
        }
    }

    @Override
    public void nodesWereInserted(DefaultMutableTreeNode group, int[] indexes) {
        ((DefaultTreeModel) tree.getModel()).nodesWereInserted(group, indexes);
    }

    @Override
    public void nodesWereRemoved(DefaultMutableTreeNode node, int[] childIndices, DefaultMutableTreeNode[] removedChildren) {
        ((DefaultTreeModel) tree.getModel()).nodesWereRemoved(node, childIndices, removedChildren);
    }
    @Override
    public void nodeChanged(DefaultMutableTreeNode node) {
        ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
    }

    @Override
    public void selectNode(DefaultMutableTreeNode node) {
        tree.setSelectionPath(new TreePath(((DefaultTreeModel) tree.getModel()).getPathToRoot(node)));
    }

    @Override
    public DefaultMutableTreeNode addGroupToTree(String name) {
        DefaultMutableTreeNode group = new DefaultMutableTreeNode(name);
        rootNode.add(group);
        groups.put(name, group);
        return group;
    }

    private DefaultMutableTreeNode selectedNode;

    @Override
    public void valueChanged(TreeSelectionEvent event) {
        //This method is useful only when the selection model allows a single selection.
        selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        if (selectedNode == null) {
            return;
        }

        Object userObject = selectedNode.getUserObject();
        if (userObject instanceof PackageTemplate) {
            selectedTemplate = (PackageTemplate) userObject;
        } else {
            selectedTemplate = null;
        }
    }
}
