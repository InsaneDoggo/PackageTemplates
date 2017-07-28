package global.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import core.writeRules.WriteRules;

import java.util.HashMap;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class BinaryFile extends BaseElement {

    @Expose @SerializedName("description") private String description;
    @Expose @SerializedName("sourcePath") private SourcePath sourcePath;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SourcePath getSourcePath() {
        return sourcePath;
    }

    public BinaryFile setSourcePath(SourcePath sourcePath) {
        this.sourcePath = sourcePath;
        return this;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    public static BinaryFile newInstance(SourcePath sourcePath) {
        BinaryFile file = new BinaryFile();
        file.setWriteRules(WriteRules.FROM_PARENT);
        file.setSourcePath(sourcePath);
        file.setName("");
        file.setScript("");
        file.setDescription("");
        file.setEnabled(true);

        return file;
    }
}
