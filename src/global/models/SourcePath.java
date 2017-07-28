package global.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arsen on 28.07.2017.
 */
public class SourcePath {

    @Expose @SerializedName("path") private String path;
    @Expose @SerializedName("isCustom") private boolean isCustom;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean custom) {
        isCustom = custom;
    }
}
