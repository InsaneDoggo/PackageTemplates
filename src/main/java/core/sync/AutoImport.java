package core.sync;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import core.writeRules.WriteRules;

import java.util.HashSet;

/**
 * Created by Arsen on 16.04.2017.
 */
public class AutoImport {

    @Expose @SerializedName("writeRules") private WriteRules writeRules;
    @Expose @SerializedName("paths") private HashSet<String> paths;

    public AutoImport() {
        writeRules = WriteRules.OVERWRITE;
        paths  = new HashSet<>();
    }

    public WriteRules getWriteRules() {
        return writeRules;
    }

    public void setWriteRules(WriteRules writeRules) {
        this.writeRules = writeRules;
    }

    public HashSet<String> getPaths() {
        return paths;
    }

    public void setPaths(HashSet<String> paths) {
        this.paths = paths;
    }
}
