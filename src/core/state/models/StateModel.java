package core.state.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import global.models.Favourite;
import global.models.PackageTemplate;

import java.util.ArrayList;

/**
 * Created by CeH9 on 05.08.2016.
 */
public class StateModel {

    @Expose @SerializedName("modelVersion") private long modelVersion;
    @Expose @SerializedName("userSettings") private UserSettings userSettings;
    @Expose @SerializedName("listFavourite") private ArrayList<Favourite> listFavourite;

    public long getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(long modelVersion) {
        this.modelVersion = modelVersion;
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    public ArrayList<Favourite> getListFavourite() {
        return listFavourite;
    }

    public void setListFavourite(ArrayList<Favourite> listFavourite) {
        this.listFavourite = listFavourite;
    }
}
