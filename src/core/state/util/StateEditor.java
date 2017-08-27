package core.state.util;

import core.state.models.StateModel;
import core.state.models.UserSettings;
import core.writeRules.WriteRules;
import global.models.Favourite;
import global.utils.i18n.Language;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Хелпер для взаимодествия с конфигом
 * Created by Arsen on 09.10.2016.
 */
public class StateEditor {

    private SaveUtil saveUtil;
    private StateModel model;

    public StateEditor(SaveUtil saveUtil, StateModel model) {
        this.saveUtil = saveUtil;
        this.model = model;
    }

    public StateEditor save() {
        saveUtil.save();
        return this;
    }


    //=================================================================
    //  User Settings
    //=================================================================
    private UserSettings userSettings() {
        return model.getUserSettings();
    }

    public StateEditor setLanguage(Language lang) {
        userSettings().setLanguage(lang);
        return this;
    }


    //=================================================================
    //  Favourite
    //=================================================================
    public StateEditor addFavourite(Favourite favourite) {
        model.getListFavourite().add(favourite);
        return this;
    }

    public StateEditor addFavourite(List<Favourite> list) {
        model.getListFavourite().addAll(list);
        return this;
    }

    public StateEditor removeFavourite(Favourite favourite) {
        model.getListFavourite().remove(favourite);
        return this;
    }

    public StateEditor clearListFavourite() {
        model.getListFavourite().clear();
        return this;
    }

    public StateEditor reorderFavourites() {
        ArrayList<Favourite> listFavourite = model.getListFavourite();
        listFavourite.sort(Comparator.comparingInt(Favourite::getOrder));

        for (int i = 0; i < listFavourite.size(); i++) {
            listFavourite.get(i).setOrder(i);
        }
        return this;
    }


    //=================================================================
    // AutoImport
    //=================================================================
    public StateEditor addAutoImportPath(String item) {
        model.getAutoImport().getPaths().add(item);
        return this;
    }

    public StateEditor addAutoImportPath(List<String> list) {
        model.getAutoImport().getPaths().addAll(list);
        return this;
    }

    public StateEditor removeAutoImportPath(String item) {
        model.getAutoImport().getPaths().remove(item);
        return this;
    }

    public StateEditor clearAutoImportPaths() {
        model.getAutoImport().getPaths().clear();
        return this;
    }

    public StateEditor setAutoImportWriteRules(WriteRules writeRules) {
        model.getAutoImport().setWriteRules(writeRules);
        return this;
    }


    //=================================================================
    //  BinaryFiles
    //=================================================================
    public StateEditor setBinaryFileWriteRules(WriteRules writeRules) {
        model.getBinaryFileConfig().setWriteRules(writeRules);
        return this;
    }

    public StateEditor setBinaryFileCacheDirPath(String path) {
        model.getBinaryFileConfig().setPathToBinaryFilesCache(path);
        return this;
    }

    public StateEditor resetBinaryFileCacheDirPath() {
        model.getBinaryFileConfig().setDefaultBinaryFilesCacheDir();
        return this;
    }

    public StateEditor setBinaryFileShouldClearCacheOnIdeStarts(boolean shouldClear) {
        model.getBinaryFileConfig().setShouldClearCacheOnIdeStarts(shouldClear);
        return this;
    }

    public StateEditor setBinaryFileEnabled(boolean enabled) {
        model.getBinaryFileConfig().setEnabled(enabled);
        return this;
    }

}
