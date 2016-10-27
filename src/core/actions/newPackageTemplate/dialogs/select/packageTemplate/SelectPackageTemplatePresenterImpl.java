package core.actions.newPackageTemplate.dialogs.select.packageTemplate;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import core.actions.newPackageTemplate.dialogs.configure.ConfigureDialog;
import core.settings.SettingsDialog;
import core.state.impex.dialogs.ImpexDialog;
import core.state.util.SaveUtil;
import global.Const;
import global.models.PackageTemplate;
import global.utils.FileReaderUtil;
import global.utils.Logger;
import global.utils.PackageTemplateHelper;
import global.utils.i18n.Localizer;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

/**
 * Created by Arsen on 17.09.2016.
 */
public class SelectPackageTemplatePresenterImpl implements SelectPackageTemplatePresenter {

    private SelectPackageTemplateView view;
    private Project project;

    public SelectPackageTemplatePresenterImpl(SelectPackageTemplateView view, Project project) {
        this.view = view;
        this.project = project;
        view.setTitle(Localizer.get("SelectPackageTemplate"));
    }


    @Override
    public ValidationInfo doValidate(String path, @Nullable JComponent component) {
//        ValidationInfo validationInfo = TemplateValidator.isTemplateValid((PackageTemplate) jbList.getSelectedValue());
//        if (validationInfo != null) {
//            return new ValidationInfo(validationInfo.message, jbList);
//        }

        File file = new File(path);
        if(file.isDirectory()){
            return new ValidationInfo(Localizer.get("warning.select.packageTemplate"), component);
        }

        PackageTemplate pt = PackageTemplateHelper.getPackageTemplate(file.getPath());

        if (pt == null) {
            return new ValidationInfo(Localizer.get("warning.select.packageTemplate"), component);
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
    public void onAddAction() {
        ConfigureDialog dialog = new ConfigureDialog(project) {
            @Override
            public void onSuccess(PackageTemplate packageTemplate) {
                VirtualFile[] files = FileChooser.chooseFiles(FileReaderUtil.getDirectoryDescriptor(), project,
                        LocalFileSystem.getInstance().findFileByIoFile(new File(PackageTemplateHelper.getRootDirPath())));
                if (files.length > 0) {
                    PackageTemplateHelper.savePackageTemplate(packageTemplate,
                            String.format("%s/%s.%s", files[0].getPath(), packageTemplate.getName(), Const.PACKAGE_TEMPLATES_EXTENSION));
                }
            }

            @Override
            public void onFail() {
            }
        };
        dialog.show();
    }

    @Override
    public void onEditAction(PackageTemplate packageTemplate) {
        if (packageTemplate == null) {
            return;
        }

        ConfigureDialog dialog = new ConfigureDialog(project, packageTemplate) {
            @Override
            public void onSuccess(PackageTemplate packageTemplate) {
                SaveUtil.editor().save();
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
