package models;

import com.google.gson.annotations.Expose;
import utils.InputManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Arsen on 15.06.2016.
 */
public class PackageTemplate {

    public static final java.lang.String ATTRIBUTE_PACKAGE_TEMPLATE_NAME = "PACKAGE_TEMPLATE_NAME";

    @Expose private String name;
    @Expose private String description;
    @Expose private TemplateElement root;
    @Expose private HashMap<String, String> mapGlobalVars;

    public PackageTemplate(String name, String description, ArrayList<TemplateElement> listTemplateElement) {
        this.name = name;
        this.description = description;

        root = new TemplateElement("", listTemplateElement, null);
    }
    public PackageTemplate(String name, String description, TemplateElement root) {
        this.name = name;
        this.description = description;
        this.root = root;
    }

    public void replaceNameVariable(InputManager inputManager) {
        if (getListTemplateElement() == null) {
            return;
        }
        for (TemplateElement element : getListTemplateElement()) {
            element.replaceNameVariable(inputManager.getMapGlobalProperties());
        }
    }

    public void setTemplateElement(TemplateElement root) {
        this.root = root;
    }

    public void setTemplateVariableName(String templateVariableName) {
        root.setName(templateVariableName);
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
        return root.getListTemplateElement();
    }

    public TemplateElement getTemplateElement() {
        return root;
    }
}
