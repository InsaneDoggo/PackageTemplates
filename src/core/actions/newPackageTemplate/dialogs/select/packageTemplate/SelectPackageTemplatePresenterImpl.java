package core.actions.newPackageTemplate.dialogs.select.packageTemplate;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import core.actions.newPackageTemplate.dialogs.configure.ConfigureDialog;
import core.settings.SettingsDialog;
import core.state.SaveUtil;
import core.state.impex.dialogs.ImpexDialog;
import core.state.models.StateModel;
import global.models.PackageTemplate;
import global.models.TemplateListModel;
import global.utils.Localizer;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Arsen on 17.09.2016.
 */
public class SelectPackageTemplatePresenterImpl implements SelectPackageTemplatePresenter {

    private SelectPackageTemplateView view;
    private Project project;
    private ArrayList<PackageTemplate> templateList;

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
        view.setTemplatesList(new TemplateListModel<>(templateList));
    }

    @Override
    public void onDeleteAction(PackageTemplate selectedValue) {
        int confirmDialog = JOptionPane.showConfirmDialog(((SelectPackageTemplateDialog) view).panel, Localizer.get("DeleteTemplate"));
        if (confirmDialog == JOptionPane.OK_OPTION) {
            templateList.remove(selectedValue);
            SaveUtil.getInstance().save();
            loadTemplates();
        }
    }

    @Override
    public void onAddAction() {
        ConfigureDialog dialog = new ConfigureDialog(project) {
            @Override
            public void onSuccess(PackageTemplate packageTemplate) {
                templateList.add(packageTemplate);
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
    public void onEditAction(PackageTemplate packageTemplate) {
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
