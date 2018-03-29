package core.state;

import core.state.models.StateModel;
import core.state.models.UserSettings;
import core.state.util.MigrationHelper;
import core.sync.AutoImport;
import core.sync.BinaryFileConfig;
import global.utils.i18n.Language;

import java.util.ArrayList;

/**
 * Created by Arsen on 28.10.2016.
 */
public class StateFactory {

    public static StateModel createStateModel() {
        StateModel stateModel = new StateModel();
        stateModel.setModelVersion(MigrationHelper.CURRENT_MODEL_VERSION);

        preventNPE(stateModel);
        return stateModel;
    }

    public static void preventNPE(StateModel stateModel) {
        if (stateModel.getUserSettings() == null) stateModel.setUserSettings(new UserSettings());
        if (stateModel.getListFavourite() == null) stateModel.setListFavourite(new ArrayList<>());
        if (stateModel.getAutoImport() == null) stateModel.setAutoImport(new AutoImport());
        if (stateModel.getBinaryFileConfig() == null) stateModel.setBinaryFileConfig(BinaryFileConfig.newInstance());

        // User Settings
        UserSettings userSettings = stateModel.getUserSettings();
        if (userSettings.getLanguage() == null) userSettings.setLanguage(Language.EN);
    }

}
