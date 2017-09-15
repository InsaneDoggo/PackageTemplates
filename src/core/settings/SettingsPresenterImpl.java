package core.settings;

import core.state.util.SaveUtil;
import core.sync.BinaryFileConfig;
import core.writeRules.WriteRules;
import global.utils.i18n.Language;
import global.utils.i18n.Localizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Arsen on 16.09.2016.
 */
public class SettingsPresenterImpl implements SettingsPresenter {

    private SettingsView view;

    public SettingsPresenterImpl(SettingsView view) {
        this.view = view;
    }

    @Override
    public void onPreShow() {
        view.setTitle(Localizer.get("title.Settings"));
        view.buildView();
    }


    //=================================================================
    //  Load
    //=================================================================
    @Override
    public void loadAutoImport() {
        HashSet<String> hashSet = SaveUtil.reader()
                .getAutoImport()
                .getPaths();

        view.buildAutoImport(new ArrayList<>(hashSet));
        view.setAutoImportWriteRules(SaveUtil.reader().getAutoImport().getWriteRules());
    }

    @Override
    public void addImportPath(List<String> oldPaths) {
        List<String> paths = new ArrayList<>();
        paths.addAll(oldPaths);
        paths.add("");
        view.buildAutoImport(paths);
    }

    @Override
    public void removeImportPath(List<String> oldPaths, int pos) {
        oldPaths.remove(pos);

        List<String> paths = new ArrayList<>();
        paths.addAll(oldPaths);
        view.buildAutoImport(paths);
    }

    @Override
    public void loadBinaryFileConfig() {
        view.buildBinaryFilesBlock(SaveUtil.reader().getBinaryFileConfig());
    }


    //=================================================================
    //  Save
    //=================================================================
    @Override
    public void setLanguage(Language lang) {
        SaveUtil.editor()
                .setLanguage(lang);
    }

    @Override
    public void setAutoImport(List<String> list, WriteRules writeRules) {
        SaveUtil.editor()
                .clearAutoImportPaths()
                .addAutoImportPath(list)
                .setAutoImportWriteRules(writeRules);
    }

    @Override
    public void setBinaryFilesConfig(BinaryFileConfig config) {
        SaveUtil.editor()
                .setBinaryFileCacheDirPath(config.getPathToBinaryFilesCache())
                .setBinaryFileEnabled(config.isEnabled())
                .setBinaryFileShouldClearCacheOnIdeStarts(config.isShouldClearCacheOnIdeStarts())
                .setBinaryFileWriteRules(config.getWriteRules());
    }

    @Override
    public void save() {
        SaveUtil.editor().save();
    }

}
