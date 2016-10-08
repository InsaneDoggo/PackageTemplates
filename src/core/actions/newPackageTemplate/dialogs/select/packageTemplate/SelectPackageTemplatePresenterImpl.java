package core.actions.newPackageTemplate.dialogs.select.packageTemplate;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.*;
import com.intellij.ui.AnActionButton;
import com.intellij.util.containers.HashMap;
import core.actions.newPackageTemplate.dialogs.configure.ConfigureDialog;
import core.settings.SettingsDialog;
import core.state.SaveUtil;
import core.state.impex.dialogs.ImpexDialog;
import core.state.models.StateModel;
import global.models.PackageTemplate;
import global.utils.Localizer;
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
    private ArrayList<PackageTemplate> templateList;
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

        if(packageTemplate == null){
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
        StateModel stateModel = SaveUtil.getInstance().getStateModel();
        templateList = stateModel.getListPackageTemplate();
        view.setTemplatesList(templateList);
    }

    @Override
    public void onDeleteAction(DefaultMutableTreeNode selectedNode) {
        Object userObject = selectedNode.getUserObject();
        if (userObject == null) {
            return;
        }

        if(userObject instanceof  PackageTemplate){
            int confirmDialog = JOptionPane.showConfirmDialog(((SelectPackageTemplateDialog) view).getRootPane(), Localizer.get("DeleteTemplate"));
            if (confirmDialog == JOptionPane.OK_OPTION) {
                templateList.remove(userObject);
                SaveUtil.getInstance().save();
                selectedNode.removeFromParent();
                view.reloadTree();
            }
            return;
        }
        if (userObject instanceof  String){
            int confirmDialog = JOptionPane.showConfirmDialog(((SelectPackageTemplateDialog) view).getRootPane(), Localizer.get("DeleteGroup"));
            if (confirmDialog == JOptionPane.OK_OPTION) {
                deleteGroupChildren(selectedNode);
                SaveUtil.getInstance().save();
                view.removeGroupFromTree(selectedNode);
                view.reloadTree();
            }
            return;
        }
    }

    private void deleteGroupChildren(DefaultMutableTreeNode groupNode) {
        Enumeration children = groupNode.children();
        while (children.hasMoreElements()){
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) children.nextElement();
            Object userObject = node.getUserObject();
            if(userObject instanceof  PackageTemplate){
                System.out.println("Remove: " + ((PackageTemplate) userObject).getName());
                templateList.remove(userObject);
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

        popupMenu.show(anActionButton.getContextComponent(), 0,  0);
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
        view.addGroupToTree(name);
        view.reloadTree();
    }

    private boolean isGroupUnique(String name) {
        for(Map.Entry<String, DefaultMutableTreeNode> entry : groups.entrySet())
            if(entry.getKey().equals(name)){
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
                if( selectedNode.getUserObject() instanceof String ){
                    groupNode = (DefaultMutableTreeNode) selectedNode.getParent();
                } else {
                    groupNode = selectedNode;
                }

                packageTemplate.setGroupName(groupNode.getUserObject().toString());
                templateList.add(packageTemplate);
                SaveUtil.getInstance().save();
                view.setTemplatesList(templateList);
                view.notifyNodeChanged(groupNode);
            }

            @Override
            public void onFail() {
            }
        };
        dialog.show();
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
                loadTemplates();
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
                System.out.println("onSuccess");
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
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
