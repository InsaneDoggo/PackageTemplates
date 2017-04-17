package core.actions.newPackageTemplate.dialogs.select.packageTemplate;

import com.intellij.ide.fileTemplates.FileTemplatesScheme;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import core.actions.newPackageTemplate.dialogs.configure.ConfigureDialog;
import core.settings.SettingsDialog;
import core.state.util.SaveUtil;
import core.sync.AutoImport;
import core.writeRules.WriteRules;
import global.Const;
import global.models.Favourite;
import global.models.PackageTemplate;
import global.utils.Logger;
import global.utils.NotificationHelper;
import global.utils.factories.WrappersFactory;
import global.utils.file.FileReaderUtil;
import global.utils.file.FileValidator;
import global.utils.templates.PackageTemplateHelper;
import global.utils.i18n.Localizer;
import global.utils.text.StringTools;
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
                    String path = String.format("%s/%s.%s", files[0].getPath(), packageTemplate.getName(), Const.PACKAGE_TEMPLATES_EXTENSION);
                    PackageTemplateHelper.savePackageTemplate(packageTemplate, path);

                    view.setPathBtnText(path);
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
                Favourite favourite = getFavouriteByPath(path);

                String newPath = path.replace(new File(path).getName(), packageTemplate.getName() + "." + Const.PACKAGE_TEMPLATES_EXTENSION);
                PackageTemplateHelper.savePackageTemplate(packageTemplate, newPath);

                if (favourite != null) {
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

    @Override
    public void addFavourite(String path) {
        if (!FileValidator.isUnderConfigDir(path)) {
            Messages.showErrorDialog(project, String.format(Localizer.get("warning.select.fromConfigDir"), PackageTemplateHelper.getRootDirPath()), Localizer.get("title.SystemMessage"));
            return;
        }

        SaveUtil.editor()
                .addFavourite(new Favourite(path, SaveUtil.reader().getListFavourite().size()))
                .save();

        view.updateFavouritesUI();
    }

    @Override
    public void onExportAction(String path) {
        VirtualFile[] files = FileChooser.chooseFiles(FileReaderUtil.getDirectoryDescriptor(), project,
                null);
        //todo remove
//                LocalFileSystem.getInstance().findFileByIoFile(new File("E:" + File.separator + "Downloads")));

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
                null);
        //todo remove
//                LocalFileSystem.getInstance().findFileByIoFile(new File("E:" + File.separator + "Downloads")));

        if (files.length <= 0) {
            return;
        }

        importFromFiles(files, WriteRules.ASK_ME);
    }

    @Override
    public void onSettingsAction() {
        SettingsDialog dialog = new SettingsDialog(project);
        dialog.show();
    }

    @Override
    public void removeFavourite(String path) {
        Favourite favourite = getFavouriteByPath(path);

        SaveUtil.editor().removeFavourite(favourite).save();
        view.updateFavouritesUI();
    }

    @Override
    public void onAutoImportAction() {
        ApplicationManager.getApplication().runReadAction(() -> {
            AutoImport autoImport = SaveUtil.reader().getAutoImport();
            ArrayList<VirtualFile> listVirtualFile = new ArrayList<>();

            if (autoImport.getPaths().isEmpty()) {
//                autoImport.getPaths().add("E:\\pluginTemp\\Idea\\plugins_sandbox\\config\\packageTemplates\\backups");
                Messages.showWarningDialog("Add at Least One Path in Settings!", "Auto Import");
                return;
            }

            for (String dirPath : autoImport.getPaths()) {
                File dir = new File(dirPath);
                if (!dir.isDirectory()) {
                    continue;
                }

                parseDir(listVirtualFile, dir);
            }

            importFromFiles(listVirtualFile.toArray(new VirtualFile[listVirtualFile.size()]), autoImport.getWriteRules());
        });
    }

    private void parseDir(ArrayList<VirtualFile> listVirtualFile, File dir) {
        File[] listFiles = getPackageTemplateFiles(dir);

        for (File file : listFiles) {
            if (file.isDirectory()) {
                parseDir(listVirtualFile, file);
            } else {
                parseFile(listVirtualFile, file);
            }
        }
    }

    private void parseFile(ArrayList<VirtualFile> listVirtualFile, File file) {
        VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(file.getPath());
        if (virtualFile == null) {
            Logger.log(" onAutoImportAction virtualFile is NULL\n" + file.getPath());
        } else {
            listVirtualFile.add(virtualFile);
        }
    }

    private File[] getPackageTemplateFiles(File dir) {
        return dir.listFiles(file -> {
            // Dir
            if (file.isDirectory()) {
                if (file.getName().equals(FileTemplatesScheme.TEMPLATES_DIR)) {
                    return false;
                }
                return true;
            }

            //file
            return StringTools.getExtensionFromName(file.getName()).equals(Const.PACKAGE_TEMPLATES_EXTENSION);
        });
    }


    //=================================================================
    //  Utils
    //=================================================================
    private Favourite getFavouriteByPath(String path) {
        ArrayList<Favourite> listFavourite = SaveUtil.reader().getListFavourite();
        for (Favourite item : listFavourite) {
            if (item.getPath().equals(path)) {
                return item;
            }
        }

        return null;
    }

    private void importFromFiles(VirtualFile[] files, WriteRules writeRules) {
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


        PackageTemplateHelper.importPackageTemplate(project, ptWrappers, visitor.getHsFileTemplateNames(), selectedFiles, writeRules);
    }

}
