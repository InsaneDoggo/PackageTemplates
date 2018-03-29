package core.state.util;

import core.state.models.StateModel;
import core.state.models.UserSettings;
import core.sync.AutoImport;
import core.sync.BinaryFileConfig;
import global.models.Favourite;
import global.utils.i18n.Language;

import java.util.ArrayList;

/**
 * Хелпер для взаимодествия с конфигом
 * Created by Arsen on 28.10.2016.
 */
public class StateReader {

    private SaveUtil saveUtil;
    private StateModel model;

    public StateReader(SaveUtil saveUtil, StateModel model) {
        this.saveUtil = saveUtil;
        this.model = model;
    }

    private UserSettings userSettings() {
        return model.getUserSettings();
    }

    public Language getLanguage() {
        return userSettings().getLanguage();
    }

    public ArrayList<Favourite> getListFavourite() {
        return model.getListFavourite();
    }

    public AutoImport getAutoImport() {
        return model.getAutoImport();
    }

    public BinaryFileConfig getBinaryFileConfig() {
        return model.getBinaryFileConfig();
    }



}
