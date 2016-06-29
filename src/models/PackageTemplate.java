package models;

import com.google.gson.annotations.Expose;
import utils.InputManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Arsen on 15.06.2016.
 */
public class PackageTemplate {

    public static final java.lang.String ATTRIBUTE_BASE_NAME = "BASE_NAME";

    @Expose private String name;
    @Expose private String description;
    @Expose private TemplateElement templateElement;
    @Expose private HashMap<String, String> mapGlobalVars;

    public PackageTemplate(String name, String description, ArrayList<TemplateElement> listTemplateElement) {
        this.name = name;
        this.description = description;

        templateElement = new TemplateElement("", listTemplateElement, null);
    }
    public PackageTemplate(String name, String description, TemplateElement templateElement) {
        this.name = name;
        this.description = description;
        this.templateElement = templateElement;
    }

    public void replaceNameVariable(InputManager inputManager) {
        templateElement.replaceNameVariable(inputManager.getMapGlobalProperties());
    }

    public HashMap<String, String> getMapGlobalVars() {
        return mapGlobalVars;
    }

    public void setMapGlobalVars(HashMap<String, String> mapGlobalVars) {
        this.mapGlobalVars = mapGlobalVars;
    }

    public void setTemplateElement(TemplateElement root) {
        this.templateElement = root;
    }

    public void setTemplateVariableName(String templateVariableName) {
        templateElement.setName(templateVariableName);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<TemplateElement> getListTemplateElement() {
        return templateElement.getListTemplateElement();
    }

    public TemplateElement getTemplateElement() {
        return templateElement;
    }
}
