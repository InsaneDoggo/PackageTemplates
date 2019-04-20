package core.settings;

import core.state.util.SaveUtil;
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

    //=================================================================
    //  Save
    //=================================================================
    @Override
    public void saveLanguage(Language lang) {
        SaveUtil.editor()
                .setLanguage(lang)
                .save();
    }

    @Override
    public void saveAutoImport(List<String> list, WriteRules writeRules) {
        SaveUtil.editor()
                .clearAutoImportPaths()
                .addAutoImportPath(list)
                .setAutoImportWriteRules(writeRules)
                .save();
    }

}
