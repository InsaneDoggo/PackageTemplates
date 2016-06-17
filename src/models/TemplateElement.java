package models;

import com.intellij.ide.fileTemplates.FileTemplateManager;
import utils.InputManager;
import utils.Logger;
import utils.StringTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Arsen on 15.06.2016.
 */

public class TemplateElement {

    private boolean isDirectory;
    private String name;
    private ArrayList<TemplateElement> listTemplateElement;

    private TemplateElement parent;
    private HashMap<String, String> mapProperties;

    public TemplateElement() {
    }

    public TemplateElement(boolean isDirectory, String name, ArrayList<TemplateElement> listTemplateElement, TemplateElement parent) {
        this.isDirectory = isDirectory;
        this.parent = parent;
        this.name = name;
        this.listTemplateElement = listTemplateElement;

        if (!isDirectory) {
            mapProperties = new HashMap<>();
        }
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


    public Map<String, HashMap<String, String>> getMapTemplates(FileTemplateManager fileTemplateManager) {
        if (isDirectory()) {
            if (getListTemplateElement() != null) {
                for (TemplateElement element : getListTemplateElement()) {

                }
            }
        } else {

        }
        return null;
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
