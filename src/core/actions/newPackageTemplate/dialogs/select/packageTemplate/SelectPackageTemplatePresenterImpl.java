package core.actions.newPackageTemplate.dialogs.select.packageTemplate;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.*;
import com.intellij.ui.AnActionButton;
import com.intellij.util.containers.HashMap;
import core.actions.newPackageTemplate.dialogs.configure.ConfigureDialog;
import core.settings.SettingsDialog;
import core.state.util.SaveUtil;
import core.state.impex.dialogs.ImpexDialog;
import core.state.models.StateModel;
import core.state.util.StateEditor;
import global.models.PackageTemplate;
import global.utils.GsonFactory;
import global.utils.Logger;
import global.utils.PackageTemplateHelper;
import global.utils.TemplateValidator;
import global.utils.i18n.Localizer;
import icons.PluginIcons;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by Arsen on 17.09.2016.
 */
public class SelectPackageTemplatePresenterImpl implements SelectPackageTemplatePresenter {

    private SelectPackageTemplateView view;
    private Project project;
    private DefaultMutableTreeNode rootNode;
    private HashMap<String, DefaultMutableTreeNode> groups;

    public SelectPackageTemplatePresenterImpl(SelectPackageTemplateView view, Project project) {
        this.view = view;
        this.project = project;
        view.setTitle(Localizer.get("SelectPackageTemplate"));
    }


    @Override
    public ValidationInfo doValidate(PackageTemplate packageTemplate, @Nullable JComponent component) {
//        ValidationInfo validationInfo = TemplateValidator.isTemplateValid((PackageTemplate) jbList.getSelectedValue());
//        if (validationInfo != null) {
//            return new ValidationInfo(validationInfo.message, jbList);
//        }

        if (packageTemplate == null) {
            return new ValidationInfo(Localizer.get("warning.SelectTemplate"), component);
        }

        return null;
    }

    @Override
    public void onSuccess(PackageTemplate packageTemplate) {
        view.onSuccess(packageTemplate);
    }

    @Override
    public void onCancel() {
        view.onCancel();
    }

    @Override
    public void loadTemplates() {
        view.setTemplatesList(PackageTemplateHelper.getListPackageTemplate());
    }

    @Override
    public void onDeleteAction(DefaultMutableTreeNode selectedNode) {
        if (selectedNode == null) return;
        Object userObject = selectedNode.getUserObject();
        if (userObject == null) return;

        if (userObject instanceof PackageTemplate) {
            int confirmDialog = JOptionPane.showConfirmDialog(((SelectPackageTemplateDialog) view).getRootPane(), Localizer.get("DeleteTemplate"));
            if (confirmDialog == JOptionPane.OK_OPTION) {
                SaveUtil.getInstance().editor()
                        .removePackageTemplate((PackageTemplate) userObject)
                        .save();
                removeNode(selectedNode);
                view.selectNode(null);
            }
            return;
        }
        if (userObject instanceof String) {
            int confirmDialog = JOptionPane.showConfirmDialog(((SelectPackageTemplateDialog) view).getRootPane(), Localizer.get("DeleteGroup"));
            if (confirmDialog == JOptionPane.OK_OPTION) {
                deleteGroupChildren(selectedNode);
                SaveUtil.getInstance().editor()
                        .removeGroupName(selectedNode.getUserObject().toString())
                        .save();
                groups.remove(selectedNode.getUserObject().toString());
                removeNode(selectedNode);
            }
            return;
        }
    }

    private void removeNode(DefaultMutableTreeNode selectedNode) {
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();
        int index = parent.getIndex(selectedNode);
        selectedNode.removeFromParent();
        view.nodesWereRemoved(parent, new int[]{index}, new DefaultMutableTreeNode[]{selectedNode});
    }

    private void deleteGroupChildren(DefaultMutableTreeNode groupNode) {
        Enumeration children = groupNode.children();
        while (children.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) children.nextElement();
            Object userObject = node.getUserObject();
            if (userObject instanceof PackageTemplate) {
                SaveUtil.getInstance().editor()
                        .removePackageTemplate((PackageTemplate) userObject);
            }
        }
    }

    @Override
    public void onCopyAction(DefaultMutableTreeNode selectedNode) {
        if (selectedNode == null) return;
        Object userObject = selectedNode.getUserObject();
        if (userObject == null) return;

        if (userObject instanceof PackageTemplate) {
            PackageTemplate ptCopy = GsonFactory.cloneObject((PackageTemplate) userObject, PackageTemplate.class);

            String name = Messages.showInputDialog(project, Localizer.get("message.EnterName"),
                    Localizer.get("title.CopyOf" )+ " " + ptCopy.getName(), Messages.getQuestionIcon(), "", new InputValidator() {
                        @Override
                        public boolean checkInput(String inputString) {
                            return !inputString.isEmpty() && TemplateValidator.isPackageTemplateNameUnique(inputString);
                        }

                        @Override
                        public boolean canClose(String inputString) {
                            return checkInput(inputString);
                        }
                    });
            if (name != null && !name.isEmpty()) {
                ptCopy.setName(name);
                addPackageTemplate(ptCopy, (DefaultMutableTreeNode) selectedNode.getParent());
            }
        }

    }

    @Override
    public void onAddAction(AnActionButton anActionButton, DefaultMutableTreeNode selectedNode) {
        JPopupMenu popupMenu = new JBPopupMenu();

        JMenuItem itemNewTemplate = new JBMenuItem(Localizer.get("popup.item.NewPackageTemplate"), PluginIcons.PACKAGE_TEMPLATES);
        JMenuItem itemNewGroup = new JBMenuItem(Localizer.get("popup.item.NewGroup"), AllIcons.Nodes.Package);

        itemNewTemplate.addActionListener(e -> newTemplate(selectedNode));
        itemNewGroup.addActionListener(e -> newGroup());

        popupMenu.add(itemNewTemplate);
        popupMenu.add(itemNewGroup);

        popupMenu.show(anActionButton.getContextComponent(), 0, 0);
    }

    @Override
    public void newGroup() {
        String name = Messages.showInputDialog(project, Localizer.get("message.EnterName"),
                Localizer.get("title.NewGroup"), Messages.getQuestionIcon(), "", new InputValidator() {
                    @Override
                    public boolean checkInput(String inputString) {
                        return !inputString.isEmpty() && isGroupUnique(inputString);
                    }

                    @Override
                    public boolean canClose(String inputString) {
                        return checkInput(inputString);
                    }
                });
        if (name != null && !name.isEmpty()) {
            DefaultMutableTreeNode groupNode = view.addGroupToTree(name);
            view.nodesWereInserted(rootNode, new int[]{rootNode.getIndex(groupNode)});
            view.selectNode(groupNode);
        }
    }

    private boolean isGroupUnique(String name) {
        for (Map.Entry<String, DefaultMutableTreeNode> entry : groups.entrySet())
            if (entry.getKey().equals(name)) {
                return false;
            }
        return true;
    }

    @Override
    public void setGroups(HashMap<String, DefaultMutableTreeNode> groups) {
        this.groups = groups;
    }

    @Override
    public void newTemplate(DefaultMutableTreeNode selectedNode) {
        ConfigureDialog dialog = new ConfigureDialog(project) {
            @Override
            public void onSuccess(PackageTemplate packageTemplate) {
                DefaultMutableTreeNode groupNode;
                if (selectedNode.getUserObject() instanceof String) {
                    groupNode = selectedNode;
                } else {
                    groupNode = (DefaultMutableTreeNode) selectedNode.getParent();
                }

                addPackageTemplate(packageTemplate, groupNode);
            }

            @Override
            public void onFail() {
            }
        };
        dialog.show();
    }

    private void addPackageTemplate(PackageTemplate packageTemplate, DefaultMutableTreeNode groupNode) {
        packageTemplate.setGroupName(groupNode.getUserObject().toString());
        SaveUtil.getInstance().editor()
                .addPackageTemplate(packageTemplate)
                .save();
        DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(packageTemplate);
        groupNode.add(newChild);
        view.nodesWereInserted(groupNode, new int[]{groupNode.getIndex(newChild)});
        view.selectNode(newChild);
    }

    @Override
    public void setTreeRootNode(DefaultMutableTreeNode rootNode) {
        this.rootNode = rootNode;
    }

    @Override
    public void onEditAction(PackageTemplate packageTemplate) {
        if (packageTemplate == null) {
            return;
        }

        ConfigureDialog dialog = new ConfigureDialog(project, packageTemplate) {
            @Override
            public void onSuccess(PackageTemplate packageTemplate) {
                SaveUtil.getInstance().save();
//                view.nodeChanged(selectedNode);
            }

            @Override
            public void onFail() {
            }
        };
        dialog.show();
    }

    @Override
    public void onExportAction() {
        ImpexDialog dialog = new ImpexDialog(project, "Export Templates") {
            @Override
            public void onSuccess() {
                Logger.log("onSuccess");
            }

            @Override
            public void onCancel() {
                Logger.log("onCancel");
            }
        };
        dialog.show();
    }

    @Override
    public void onSettingsAction() {
        SettingsDialog dialog = new SettingsDialog(project);
        dialog.show();
    }
}
