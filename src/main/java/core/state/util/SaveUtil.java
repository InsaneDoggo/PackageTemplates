package core.state.util;

import com.intellij.openapi.components.ServiceManager;
import core.state.Config;
import core.state.StateFactory;
import core.state.models.StateModel;
import core.state.models.StateWrapper;
import global.utils.factories.GsonFactory;

/**
 * Created by CeH9 on 26.06.2016.
 */
public class SaveUtil {

    private static SaveUtil instance;
    private StateModel stateModel;
    private Config cfg;

    private static SaveUtil getInstance() {
        if (instance == null) {
            instance = new SaveUtil();
            instance.init();
        }
        return instance;
    }

    private void init() {
        instance.load();
        initEditor();
        initReader();
    }

    public SaveUtil() {
        cfg = ServiceManager.getService(Config.class);
    }

    private void load() {
        StateWrapper stateWrapper = cfg.getState();
        if (stateWrapper == null) {
            stateModel = StateFactory.createStateModel();
            stateWrapper = new StateWrapper();
            cfg.setMyState(stateWrapper);
            return;
        }

        stateModel = GsonFactory.getInstance().fromJson(stateWrapper.value, StateModel.class);
        if (stateModel == null) {
            stateModel = StateFactory.createStateModel();
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


    //=================================================================
    //  Editor
    //=================================================================
    private StateEditor editor;

    private void initEditor() {
        editor = new StateEditor(this, stateModel);
    }

    public static StateEditor editor() {
        return getInstance().editor;
    }


    //=================================================================
    //  Reader
    //=================================================================
    private StateReader reader;

    private void initReader() {
        reader = new StateReader(this, stateModel);
    }

    public static StateReader reader() {
        return getInstance().reader;
    }

}
