package models;

import java.util.ArrayList;

/**
 * Created by Arsen on 15.06.2016.
 */
public class PackageTemplate {

    public static final java.lang.String ATTRIBUTE_PACKAGE_TEMPLATE_NAME = "PACKAGE_TEMPLATE_NAME";

    private String name;
    private String description;
    private TemplateElement root;

    public PackageTemplate(String name, String templateVariableName, String description, ArrayList<TemplateElement> listTemplateElement) {
        this.name = name;
        this.description = description;

        root = new TemplateElement(true, "", templateVariableName, listTemplateElement, null);
    }

    public String getTemplateVariableName() {
        return root.getName();
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
