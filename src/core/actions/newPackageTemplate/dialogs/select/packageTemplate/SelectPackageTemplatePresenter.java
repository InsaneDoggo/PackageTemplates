package core.actions.newPackageTemplate.dialogs.select.packageTemplate;

import com.intellij.openapi.ui.ValidationInfo;
import global.models.PackageTemplate;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by Arsen on 17.09.2016.
 */
public interface SelectPackageTemplatePresenter {
    ValidationInfo doValidate(PackageTemplate packageTemplate, @Nullable JComponent component);

    void onSuccess(PackageTemplate selectedValue);

    void onCancel();

    void loadTemplates();

    void onDeleteAction(JButton jbDelete, PackageTemplate selectedValue);

    void onAddAction();

    void onEditAction(PackageTemplate packageTemplate);

    void onExportAction();
}
