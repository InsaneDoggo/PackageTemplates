package io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.ide.util.PropertiesComponent;
import models.PackageTemplate;

import java.util.HashMap;

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

    public static final String KEY_PREFIX = "package_template_key_prefix";

    private TemplateList templateList;
    private Gson gson;
    private PropertiesComponent propertiesComponent;

    public SaveUtil() {
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        propertiesComponent = PropertiesComponent.getInstance();
    }

    public void load() {
        templateList = gson.fromJson(propertiesComponent.getValue(SaveUtil.KEY_PREFIX), TemplateList.class);
        if (templateList == null) {
            templateList = new TemplateList();
        }
        initNonSerializableFields();
    }

    public void save() {
        propertiesComponent.setValue(SaveUtil.KEY_PREFIX, gson.toJson(templateList, TemplateList.class));
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
