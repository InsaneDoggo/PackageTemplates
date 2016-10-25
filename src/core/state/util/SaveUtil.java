package core.state.util;

import com.google.gson.Gson;
import com.intellij.openapi.components.ServiceManager;
import core.state.Config;
import core.state.impex.Exporter;
import core.state.impex.models.ExportBundle;
import core.state.models.StateModel;
import core.state.models.StateWrapper;
import core.state.models.UserSettings;
import global.utils.GsonFactory;
import global.utils.i18n.Language;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by CeH9 on 26.06.2016.
 */
public class SaveUtil {

    private static SaveUtil instance;
    private static StateEditor editor;
    private StateModel stateModel;
    private Gson gson;
    private Config cfg;

    public static SaveUtil getInstance() {
        if (instance == null) {
            instance = new SaveUtil();
            editor = new StateEditor();
            instance.load();
        }

        return instance;
    }

    public SaveUtil() {
        gson = GsonFactory.getInstance();
        cfg = ServiceManager.getService(Config.class);
    }

    public StateEditor editor() {
        return editor;
    }

    public void load() {
        StateWrapper state = cfg.getState();
        if (state != null) {
            stateModel = gson.fromJson(state.value, StateModel.class);
            if (stateModel != null) {
                MigrationHelper.checkVersion(stateModel);
                preventNPE();
            } else {
                initNewState(state);
            }
        } else {
            initNewState();
        }
    }

    public void save() {
        StateWrapper state = cfg.getState();
        if (state != null) {
            state.value = gson.toJson(stateModel, StateModel.class);
        }
    }

    private void initNewState() {
        initNewState(new StateWrapper());
    }

    private void initNewState(StateWrapper state) {
        stateModel = new StateModel();
        stateModel.setModelVersion(MigrationHelper.CURRENT_MODEL_VERSION);

        preventNPE();
        state.value = gson.toJson(stateModel, StateModel.class);
        cfg.setMyState(state);
    }

    public StateModel getStateModel() {
        return stateModel;
    }

    public String getTemplatesForExport() {
        return gson.toJson(Exporter.stateModelToExpString(stateModel), ExportBundle.class);
    }

    private void preventNPE() {
//        if (stateModel.getListPackageTemplate() == null) stateModel.setListPackageTemplate(new ArrayList<>());
        if (stateModel.getUserSettings() == null) stateModel.setUserSettings(new UserSettings());

        // User Settings
        UserSettings userSettings = stateModel.getUserSettings();
        if (userSettings.getLanguage() == null) userSettings.setLanguage(Language.EN);
        if (userSettings.getGroupNames() == null) userSettings.setGroupNames(new HashSet<>());
    }


}
