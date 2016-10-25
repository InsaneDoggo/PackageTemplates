package core.state.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import global.models.PackageTemplate;

import java.util.ArrayList;

/**
 * Created by CeH9 on 05.08.2016.
 */
public class StateModel {

    @Expose @SerializedName("modelVersion") private long modelVersion;
    @Expose @SerializedName("userSettings") private UserSettings userSettings;
//    @Expose @SerializedName("listPackageTemplate") private ArrayList<PackageTemplate> listPackageTemplate;

    public long getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(long modelVersion) {
        this.modelVersion = modelVersion;
    }

//    public ArrayList<PackageTemplate> getListPackageTemplate() {
//        return listPackageTemplate;
//    }
//
//    public void setListPackageTemplate(ArrayList<PackageTemplate> listPackageTemplate) {
//        this.listPackageTemplate = listPackageTemplate;
//    }

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(UserSettings userSettings) {
        this.userSettings = userSettings;
    }
}
