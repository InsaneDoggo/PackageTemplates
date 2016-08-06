package state;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.components.ServiceManager;
import models.BaseElement;
import models.serialization.BaseElementConverter;
import state.export.Exporter;
import state.export.models.ExportBundle;
import state.models.StateModel;
import state.models.StateWrapper;

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
        gson = new GsonBuilder()
                .registerTypeAdapter(BaseElement.class, new BaseElementConverter())
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();

        cfg = ServiceManager.getService(Config.class);
    }

    public void load() {
        StateWrapper state = cfg.getState();
        if( state != null ){
            stateModel = gson.fromJson(state.value, StateModel.class);
            MigrationHelper.checkVersion(stateModel);
        }
        preventNPE();
    }

    public void save() {
        StateWrapper state = cfg.getState();
        if( state != null ) {
            state.value = gson.toJson(stateModel, StateModel.class);
        } else {
            state = new StateWrapper();
            state.value = gson.toJson(stateModel, StateModel.class);
            cfg.loadState(state);
        }
    }

    public StateModel getStateModel() {
        return stateModel;
    }

    public String getTemplatesForExport() {
        return gson.toJson(Exporter.StateModelToExpString(stateModel), ExportBundle.class);
    }

    private void preventNPE() {
        if (stateModel.getListPackageTemplate() == null) {
            stateModel.setListPackageTemplate(new ArrayList<>());
        }
    }


}
