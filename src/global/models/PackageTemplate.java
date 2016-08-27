package global.models;

import com.google.gson.annotations.Expose;

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
    @Expose
    private boolean skipRootDirectory;

    public boolean isSkipRootDirectory() {
        return skipRootDirectory;
    }

    public void setSkipRootDirectory(boolean skipRootDirectory) {
        this.skipRootDirectory = skipRootDirectory;
    }

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
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

    public ArrayList<GlobalVariable> getListGlobalVariable() {
        return listGlobalVariable;
    }

    public void setListGlobalVariable(ArrayList<GlobalVariable> listGlobalVariable) {
        this.listGlobalVariable = listGlobalVariable;
    }
}
