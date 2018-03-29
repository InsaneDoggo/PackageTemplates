package core.actions.newPackageTemplate.dialogs.select.packageTemplate;

import com.intellij.openapi.ui.ValidationInfo;
import global.models.PackageTemplate;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by Arsen on 17.09.2016.
 */
public interface SelectPackageTemplatePresenter {

    ValidationInfo doValidate(String path, @Nullable JComponent component);

    void onSuccess(PackageTemplate selectedValue);

    void onCancel();

    void onAddAction();

    void onEditAction(String packageTemplate);

    void onExportAction(String selectedPath);

    void onImportAction();

    void onSettingsAction();

    void addFavourite(String path);

    void removeFavourite(String path);

    void onAutoImportAction();
}
