package global.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Arsen on 15.06.2016.
 */
public class PackageTemplate {

    @Expose @SerializedName("name") private String name;
    @Expose @SerializedName("description") private String description;
    @Expose @SerializedName("directory") private Directory directory;
    @Expose @SerializedName("groupName") private String groupName;
    @Expose @SerializedName("mapGlobalVars") private HashMap<String, String> mapGlobalVars;
    @Expose @SerializedName("listGlobalVariable") private ArrayList<GlobalVariable> listGlobalVariable;
    @Expose @SerializedName("postGeneratedResults") private PostGeneratedResults postGeneratedResults;
    @Expose @SerializedName("skipRootDirectory") private boolean skipRootDirectory;
    @Expose @SerializedName("skipDefiningNames") private boolean skipDefiningNames;
    @Expose @SerializedName("shouldRegisterAction") private boolean shouldRegisterAction;

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

    public boolean isSkipDefiningNames() {
        return skipDefiningNames;
    }

    public void setSkipDefiningNames(boolean skipDefiningNames) {
        this.skipDefiningNames = skipDefiningNames;
    }

    public boolean isShouldRegisterAction() {
        return shouldRegisterAction;
    }

    public void setShouldRegisterAction(boolean shouldRegisterAction) {
        this.shouldRegisterAction = shouldRegisterAction;
    }

    public PostGeneratedResults getPostGeneratedResults() {
        return postGeneratedResults;
    }

    public void setPostGeneratedResults(PostGeneratedResults postGeneratedResults) {
        this.postGeneratedResults = postGeneratedResults;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
