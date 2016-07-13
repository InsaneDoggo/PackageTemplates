package models;

import com.google.gson.annotations.Expose;
import reborn.models.Directory;
import reborn.models.GlobalVariable;
import utils.InputManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Arsen on 15.06.2016.
 */
public class PackageTemplate {

    @Expose
    private String name;
    @Expose
    private String description;
    @Expose
    private Directory directory;
    @Expose
    private HashMap<String, String> mapGlobalVars;
    @Expose
    private ArrayList<GlobalVariable> listGlobalVariable;

    public PackageTemplate() {
    }

    public PackageTemplate(String name, String description, ArrayList<TemplateElement> listTemplateElement) {
        this.name = name;
        this.description = description;

//        templateElement = new TemplateElement("", listTemplateElement, null);
    }

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
    }

    public PackageTemplate(String name, String description, TemplateElement templateElement) {
        this.name = name;
        this.description = description;
//        this.templateElement = templateElement;
    }

    public void replaceNameVariable(InputManager inputManager) {
//        directory.replaceNameVariable(inputManager.getMapGlobalProperties());
    }

    public HashMap<String, String> getMapGlobalVars() {
        return mapGlobalVars;
    }

    public void setMapGlobalVars(HashMap<String, String> mapGlobalVars) {
        this.mapGlobalVars = mapGlobalVars;
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

    public TemplateElement getTemplateElement() {
        return null;
    }

    public ArrayList<GlobalVariable> getListGlobalVariable() {
        return listGlobalVariable;
    }

    public void setListGlobalVariable(ArrayList<GlobalVariable> listGlobalVariable) {
        this.listGlobalVariable = listGlobalVariable;
    }
}
