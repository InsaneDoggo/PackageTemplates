package io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.ide.util.PropertiesComponent;
import models.PackageTemplate;

/**
 * Created by CeH9 on 26.06.2016.
 */
public class SaveUtil {

    public static final String KEY_PREFIX = "package_template_key_prefix";

    private TemplateList templateList;
    private Gson gson;
    private PropertiesComponent propertiesComponent;

    public SaveUtil() {
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        propertiesComponent = PropertiesComponent.getInstance();
        load();
    }

    public void load() {
        templateList = gson.fromJson(propertiesComponent.getValue(SaveUtil.KEY_PREFIX), TemplateList.class);
        if (templateList == null) {
            templateList = new TemplateList();
        }
        updateParents();
    }

    public void save() {
        propertiesComponent.setValue(SaveUtil.KEY_PREFIX, gson.toJson(templateList));
    }

    private void updateParents() {
        for (PackageTemplate packageTemplate : templateList) {
            packageTemplate.getTemplateElement().updateParents(null);
        }
    }

}
