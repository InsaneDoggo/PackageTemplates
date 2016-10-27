package core.state.util;

import core.state.models.StateModel;
import core.state.models.UserSettings;
import global.utils.i18n.Language;

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

}
