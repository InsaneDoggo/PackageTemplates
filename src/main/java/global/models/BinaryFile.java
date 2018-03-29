package global.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import core.writeRules.WriteRules;
import org.jetbrains.annotations.NotNull;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class BinaryFile extends BaseElement {

    @Expose @SerializedName("description") private String description;
    @Expose @SerializedName("sourcePath") private String sourcePath;

    @Override
    public boolean isDirectory() {
        return false;
    }

    @NotNull
    public static BinaryFile newInstance() {
        BinaryFile file = new BinaryFile();
        file.setWriteRules(WriteRules.FROM_PARENT);
        file.setSourcePath(null);
        file.setName(null);
        file.setScript("");
        file.setDescription("");
        file.setEnabled(true);

        return file;
    }


    //=================================================================
    //  G|S
    //=================================================================
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }
}
