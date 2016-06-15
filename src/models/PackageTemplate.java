package models;

import java.util.ArrayList;

/**
 * Created by Arsen on 15.06.2016.
 */
public class PackageTemplate {

    private String name;
    private String templateVariableName;
    private String description;
    private ArrayList<TemplateElement> listTemplateElement;

    public PackageTemplate() {
    }

    public PackageTemplate(String name, String templateVariableName, String description, ArrayList<TemplateElement> listTemplateElement) {
        this.name = name;
        this.templateVariableName = templateVariableName;
        this.description = description;
        this.listTemplateElement = listTemplateElement;
    }

    public String getTemplateVariableName() {
        return templateVariableName;
    }

    public void setTemplateVariableName(String templateVariableName) {
        this.templateVariableName = templateVariableName;
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
        return listTemplateElement;
    }

    public void setListTemplateElement(ArrayList<TemplateElement> listTemplateElement) {
        this.listTemplateElement = listTemplateElement;
    }
}
