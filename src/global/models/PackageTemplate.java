package global.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import core.textInjection.TextInjection;
import global.utils.templates.FileTemplateSource;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Arsen on 15.06.2016.
 */
public class PackageTemplate {

    @Expose @SerializedName("name") private String name;
    @Expose @SerializedName("description") private String description;
    @Expose @SerializedName("directory") private Directory directory;
    @Expose @SerializedName("mapGlobalVars") private HashMap<String, String> mapGlobalVars;
    @Expose @SerializedName("listGlobalVariable") private ArrayList<GlobalVariable> listGlobalVariable;
    @Expose @SerializedName("listTextInjection") private ArrayList<TextInjection> listTextInjection;
    @Expose @SerializedName("postGeneratedResults") private PostGeneratedResults postGeneratedResults;
    @Expose @SerializedName("skipRootDirectory") private boolean skipRootDirectory;
    @Expose @SerializedName("skipDefiningNames") private boolean skipDefiningNames;
    @Expose @SerializedName("shouldRegisterAction") private boolean shouldRegisterAction;
    @Expose @SerializedName("shouldShowReport") private boolean shouldShowReport;
    @Expose @SerializedName("fileTemplateSource") private FileTemplateSource fileTemplateSource;

    public static PackageTemplate newInstance() {
        PackageTemplate pt = new PackageTemplate();

        pt.setMapGlobalVars(new HashMap<>());
        pt.setListGlobalVariable(new ArrayList<>());
        pt.setListTextInjection(new ArrayList<>());
        pt.setName("NewPackageTemplate");
        pt.setDescription("Manual:\n1. Do foo\n2. Do bar");
        pt.setSkipRootDirectory(false);
        pt.setFileTemplateSource(FileTemplateSource.getDefault());

        return pt;
    }


    public boolean shouldShowReport() {
        return shouldShowReport;
    }

    public void setShouldShowReport(boolean shouldShowReport) {
        this.shouldShowReport = shouldShowReport;
    }

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

    public ArrayList<TextInjection> getListTextInjection() {
        return listTextInjection;
    }

    public void setListTextInjection(ArrayList<TextInjection> listTextInjection) {
        this.listTextInjection = listTextInjection;
    }

    public boolean isShouldShowReport() {
        return shouldShowReport;
    }

    public FileTemplateSource getFileTemplateSource() {
        return fileTemplateSource;
    }

    public void setFileTemplateSource(FileTemplateSource fileTemplateSource) {
        this.fileTemplateSource = fileTemplateSource;
    }

}
