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
import global.utils.Logger;
import global.utils.factories.WrappersFactory;
import global.utils.file.FileReaderUtil;
import global.utils.file.FileValidator;
import global.utils.templates.PackageTemplateHelper;
import global.utils.i18n.Localizer;
import global.visitors.CollectFileTemplatesVisitor;
import global.wrappers.PackageTemplateWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

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
                Favourite favourite = getFavourite(path);

                String newPath = path.replace(new File(path).getName(), packageTemplate.getName() + "." + Const.PACKAGE_TEMPLATES_EXTENSION);
                PackageTemplateHelper.savePackageTemplate(packageTemplate, newPath);

                if(favourite!=null){
                    favourite.setPath(newPath);
                    SaveUtil.editor().save();
                }

                view.updateFavouritesUI();
                view.selectFavourite(favourite);
            }

            @Override
            public void onFail() {
            }
        };
        dialog.show();
    }

    private Favourite getFavourite(String path) {
        ArrayList<Favourite> listFavourite = SaveUtil.reader().getListFavourite();
        for(Favourite item : listFavourite){
            if(item.getPath().equals(path)){
                return item;
            }
        }

        return null;
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
        VirtualFile[] files = FileChooser.chooseFiles(FileReaderUtil.getDirectoryDescriptor(), project,
//                null);
                //todo remove
                LocalFileSystem.getInstance().findFileByIoFile(new File("E:" + File.separator + "Downloads")));

        if (files.length <= 0) {
            return;
        }

        PackageTemplate pt = PackageTemplateHelper.getPackageTemplate(path);
        VirtualFile directoryToExport = files[0];

        PackageTemplateHelper.exportPackageTemplate(project, pt, directoryToExport.getPath());
    }

    @Override
    public void onImportAction() {
        VirtualFile[] files = FileChooser.chooseFiles(FileReaderUtil.getPackageTemplatesDescriptorForImport(), project,
//                null);
                //todo remove
                LocalFileSystem.getInstance().findFileByIoFile(new File("E:" + File.separator + "Downloads")));

        if (files.length <= 0) {
            return;
        }

        CollectFileTemplatesVisitor visitor = new CollectFileTemplatesVisitor();
        ArrayList<PackageTemplateWrapper> ptWrappers = new ArrayList<>();
        ArrayList<File> selectedFiles = new ArrayList<>();

        for (VirtualFile item : files) {
            File file = new File(item.getPath());
            selectedFiles.add(file);

            PackageTemplateWrapper ptWrapper = WrappersFactory.wrapPackageTemplate(project,
                    PackageTemplateHelper.getPackageTemplate(file),
                    PackageTemplateWrapper.ViewMode.EDIT
            );
            visitor.visit(ptWrapper.getRootElement());
            ptWrappers.add(ptWrapper);
        }


        PackageTemplateHelper.importPackageTemplate(project, ptWrappers, visitor.getHsFileTemplateNames(), selectedFiles);
    }

    @Override
    public void onSettingsAction() {
        SettingsDialog dialog = new SettingsDialog(project);
        dialog.show();
    }

}
