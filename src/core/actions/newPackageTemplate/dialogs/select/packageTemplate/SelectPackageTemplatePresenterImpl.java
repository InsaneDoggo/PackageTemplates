package core.actions.newPackageTemplate.dialogs.select.packageTemplate;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import core.actions.newPackageTemplate.dialogs.configure.ConfigureDialog;
import core.settings.SettingsDialog;
import core.state.util.SaveUtil;
import global.Const;
import global.models.Favourite;
import global.models.PackageTemplate;
import global.utils.CollectionHelper;
import global.utils.FileReaderUtil;
import global.utils.FileValidator;
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
        if (file.isDirectory()) {
            return new ValidationInfo(Localizer.get("warning.select.packageTemplate"), component);
        }

        PackageTemplate pt = PackageTemplateHelper.getPackageTemplate(file);

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


    //=================================================================
    //  Toolbar actions
    //=================================================================
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
    public void onEditAction(String path) {
        PackageTemplate packageTemplate = PackageTemplateHelper.getPackageTemplate(path);

        if (packageTemplate == null) {
            return;
        }

        ConfigureDialog dialog = new ConfigureDialog(project, packageTemplate) {
            @Override
            public void onSuccess(PackageTemplate packageTemplate) {
                // Replace Name
                String newPath = path.replace(new File(path).getName(), packageTemplate.getName() + "."+Const.PACKAGE_TEMPLATES_EXTENSION);
                PackageTemplateHelper.savePackageTemplate(packageTemplate, newPath);
//                view.nodeChanged(selectedNode);
            }

            @Override
            public void onFail() {
            }
        };
        dialog.show();
    }

    @Override
    public void onAddToFavourites(String path) {
        if (!FileValidator.isUnderConfigDir(path)) {
            Messages.showErrorDialog(project, String.format(Localizer.get("warning.select.fromConfigDir"), PackageTemplateHelper.getRootDirPath()), Localizer.get("title.SystemMessage"));
            return;
        }

        Favourite storedFavourite = CollectionHelper.getFavouriteByPath(SaveUtil.reader().getListFavourite(), path);

        if (storedFavourite != null) {
            SaveUtil.editor()
                    .removeFavourite(storedFavourite)
                    .reorderFavourites()
                    .save();
        } else {
            SaveUtil.editor()
                    .addFavourite(new Favourite(path, SaveUtil.reader().getListFavourite().size()))
                    .save();
        }
    }

    @Override
    public void onExportAction(String path) {
        VirtualFile[] files = FileChooser.chooseFiles(FileReaderUtil.getDirectoryDescriptor(), project, null);

        if (files.length > 0) {
            PackageTemplate pt = PackageTemplateHelper.getPackageTemplate(path);
            PackageTemplateHelper.exportPackageTemplate(project, pt, files[0].getPath());
        }
    }

    @Override
    public void onSettingsAction() {
        SettingsDialog dialog = new SettingsDialog(project);
        dialog.show();
    }

}
