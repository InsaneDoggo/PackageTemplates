package state;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.components.ServiceManager;
import models.PackageTemplate;

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

    private TemplateList templateList;
    private Gson gson;
    private Config cfg;

    public SaveUtil() {
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        cfg = ServiceManager.getService(Config.class);
    }

    public void load() {
        PackageTemplateState state = cfg.getState();
        if( state != null ){
            templateList = gson.fromJson(state.value, TemplateList.class);
        }
        if (templateList == null) {
            templateList = new TemplateList();
        }
        initNonSerializableFields();
    }

    public void save() {
        PackageTemplateState state = cfg.getState();
        if( state != null ) {
            state.value = gson.toJson(templateList, TemplateList.class);
        } else {
            state = new PackageTemplateState();
            state.value = gson.toJson(templateList, TemplateList.class);
            cfg.loadState(state);
        }
    }

    private void initNonSerializableFields() {
        for (PackageTemplate packageTemplate : templateList) {
            packageTemplate.getTemplateElement().updateParents(null);
            packageTemplate.getTemplateElement().initNonSerializableFields();
        }
    }

    public TemplateList getTemplateList() {
        return templateList;
    }

    public void setTemplateList(TemplateList templateList) {
        this.templateList = templateList;
    }
}
