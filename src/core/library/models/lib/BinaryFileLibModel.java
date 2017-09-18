package core.library.models.lib;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BinaryFileLibModel {

    @Expose @SerializedName("path")  private String path;

    public BinaryFileLibModel(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
