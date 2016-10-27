package core.state.util;

import com.intellij.openapi.components.ServiceManager;
import core.state.Config;
import core.state.StateFactory;
import core.state.models.StateModel;
import core.state.models.StateWrapper;
import global.utils.GsonFactory;

/**
 * Created by CeH9 on 26.06.2016.
 */
public class SaveUtil {

    private static SaveUtil instance;
    private StateEditor editor;
    private StateReader reader;

    private StateModel stateModel;
    private Config cfg;

    private static SaveUtil getInstance() {
        if (instance == null) {
            instance = new SaveUtil();
        }
        return instance;
    }

    public SaveUtil() {
        cfg = ServiceManager.getService(Config.class);
        instance.load();
        editor = new StateEditor(this, stateModel);
        reader = new StateReader(this, stateModel);
    }

    public static StateReader reader() {
        return getInstance().reader;
    }

    public static StateEditor editor() {
        return getInstance().editor;
    }

    private void load() {
        StateWrapper stateWrapper = cfg.getState();
        if (stateWrapper == null) {
            StateFactory.createStateModel();
            return;
        }

        stateModel = GsonFactory.getInstance().fromJson(stateWrapper.value, StateModel.class);
        if (stateModel == null) {
            StateFactory.createStateModel(stateWrapper);
            return;
        }

        MigrationHelper.checkVersion(stateModel);
        StateFactory.preventNPE(stateModel);
    }

    public void save() {
        StateWrapper state = cfg.getState();
        if (state != null) {
            state.value = GsonFactory.getInstance().toJson(stateModel, StateModel.class);
        }
    }

}
