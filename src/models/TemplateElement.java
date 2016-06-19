package models;

import utils.InputManager;
import utils.Logger;
import utils.StringTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Arsen on 15.06.2016.
 */

public class TemplateElement {

    private boolean isDirectory;
    private String name;
    private String templateName;
    private ArrayList<TemplateElement> listTemplateElement;

    private TemplateElement parent;
    private HashMap<String, String> mapProperties;

    public TemplateElement(boolean isDirectory, String name, String templateName, ArrayList<TemplateElement> listTemplateElement, TemplateElement parent) {
        this.isDirectory = isDirectory;
        this.parent = parent;
        this.name = name;
        this.templateName = templateName;
        this.listTemplateElement = listTemplateElement;

        if (!isDirectory) {
            mapProperties = new HashMap<>();
        }
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public TemplateElement getParent() {
        return parent;
    }

    public void setParent(TemplateElement parent) {
        this.parent = parent;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
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

    public HashMap<String, String> getMapProperties() {
        return mapProperties;
    }

    public void setMapProperties(HashMap<String, String> mapProperties) {
        this.mapProperties = mapProperties;
    }

    public void setListTemplateElement(ArrayList<TemplateElement> listTemplateElement) {
        this.listTemplateElement = listTemplateElement;
    }

    public boolean isNameValid(List<String> listAllTemplates) {
        if (isDirectory()) {
            if (getListTemplateElement() != null) {
                for (TemplateElement element : getListTemplateElement()) {
                    if (!element.isNameValid(listAllTemplates)) {
                        return false;
                    }
                }
            }
        } else {
            if (!listAllTemplates.contains(getName())) {
                Logger.log("Template " + getName() + " doesn't exist!");
                return false;
            }
        }
        return true;
    }

    public void replaceNameVariable(String packageTemplateName) {
        setName(StringTools.replaceName(packageTemplateName, getName()));
        if (getListTemplateElement() != null) {
            for (TemplateElement element : getListTemplateElement()) {
                element.replaceNameVariable(packageTemplateName);
            }
        }
    }

    public void makeInputBlock(InputManager inputManager) {
        inputManager.addElement(this);

        if (isDirectory()) {
            if (getListTemplateElement() != null) {
                for (TemplateElement element : getListTemplateElement()) {
                    element.makeInputBlock(inputManager);
                }
            }

            // TODO: 17.06.2016 skip empty packages
            inputManager.onPackageEnds();
        }
    }
}
