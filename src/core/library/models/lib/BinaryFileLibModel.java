package core.library.models.lib;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BinaryFileLibModel {

    @Expose @SerializedName("relativePath")  private String relativePath;

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
}
