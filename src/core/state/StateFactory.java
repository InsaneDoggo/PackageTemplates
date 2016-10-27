package core.state;

import core.state.models.StateModel;
import core.state.models.StateWrapper;
import core.state.models.UserSettings;
import core.state.util.MigrationHelper;
import global.utils.i18n.Language;

/**
 * Created by Arsen on 28.10.2016.
 */
public class StateFactory {

    public static void createStateModel() {
        createStateModel(new StateWrapper());
    }

    public static StateModel createStateModel(StateWrapper state) {
        StateModel stateModel = new StateModel();
        stateModel.setModelVersion(MigrationHelper.CURRENT_MODEL_VERSION);

        preventNPE(stateModel);
        return stateModel;
    }

    public static void preventNPE(StateModel stateModel) {
        if (stateModel.getUserSettings() == null) stateModel.setUserSettings(new UserSettings());

        // User Settings
        UserSettings userSettings = stateModel.getUserSettings();
        if (userSettings.getLanguage() == null) userSettings.setLanguage(Language.EN);
    }

}
