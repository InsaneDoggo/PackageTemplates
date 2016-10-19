package core.actions.newPackageTemplate.dialogs.select.packageTemplate;

import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.AnActionButton;
import com.intellij.util.containers.HashMap;
import global.models.PackageTemplate;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by Arsen on 17.09.2016.
 */
public interface SelectPackageTemplatePresenter {

    ValidationInfo doValidate(PackageTemplate packageTemplate, @Nullable JComponent component);

    void onSuccess(PackageTemplate selectedValue);

    void onCancel();

    void loadTemplates();

    void onDeleteAction(DefaultMutableTreeNode selectedNode);

    void onAddAction(AnActionButton anActionButton, DefaultMutableTreeNode selectedNode);

    void onEditAction(PackageTemplate packageTemplate, DefaultMutableTreeNode selectedNode);

    void onExportAction();

    void onSettingsAction();

    void newGroup();

    void newTemplate(DefaultMutableTreeNode selectedNode);

    void setTreeRootNode(DefaultMutableTreeNode rootNode);

    void setGroups(HashMap<String, DefaultMutableTreeNode> groups);

    void onCopyAction(DefaultMutableTreeNode selectedNode);
}
