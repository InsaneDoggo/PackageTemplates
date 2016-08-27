package core.state;

import com.google.gson.Gson;
import com.intellij.openapi.components.ServiceManager;
import core.state.export.Exporter;
import core.state.export.models.ExportBundle;
import core.state.models.StateModel;
import core.state.models.StateWrapper;
import global.utils.GsonFactory;

import java.util.ArrayList;

/**
 * Created by CeH9 on 26.06.2016.
 */
public class SaveUtil {

    private static SaveUtil instance;

    public static SaveUtil getInstance() {
        if (instance == null) {
            instance = new SaveUtil();
            instance.load();
        }

        return instance;
    }

    private StateModel stateModel;
    private Gson gson;
    private Config cfg;

    public SaveUtil() {
        gson = GsonFactory.getInstance();

        cfg = ServiceManager.getService(Config.class);
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
        if (stateModel.getListPackageTemplate() == null) {
            stateModel.setListPackageTemplate(new ArrayList<>());
        }
    }


}
