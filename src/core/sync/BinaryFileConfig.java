package core.sync;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import core.writeRules.WriteRules;
import global.utils.file.PathHelper;

public class BinaryFileConfig {

    @Expose @SerializedName("isEnabled") private boolean isEnabled;
    @Expose @SerializedName("writeRules") private WriteRules writeRules;
    @Expose @SerializedName("shouldClearCacheOnIdeStarts") private boolean shouldClearCacheOnIdeStarts;
    @Expose @SerializedName("pathToBinaryFilesCache") private String pathToBinaryFilesCache;

    private BinaryFileConfig() {
    }

    public static BinaryFileConfig newInstance() {
        BinaryFileConfig config = new BinaryFileConfig();

        config.isEnabled = false;
        config.writeRules = WriteRules.OVERWRITE;
        config.shouldClearCacheOnIdeStarts = false;
        config.setDefaultBinaryFilesCacheDir();

        return config;
    }


    //=================================================================
    //  Utils
    //=================================================================
    public void setDefaultBinaryFilesCacheDir(){
        pathToBinaryFilesCache = PathHelper.getBinaryFilesCacheDefaultDir();
    }


    //=================================================================
    //  G|S
    //=================================================================
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public WriteRules getWriteRules() {
        return writeRules;
    }

    public void setWriteRules(WriteRules writeRules) {
        this.writeRules = writeRules;
    }

    public boolean isShouldClearCacheOnIdeStarts() {
        return shouldClearCacheOnIdeStarts;
    }

    public void setShouldClearCacheOnIdeStarts(boolean shouldClearCacheOnIdeStarts) {
        this.shouldClearCacheOnIdeStarts = shouldClearCacheOnIdeStarts;
    }

    public String getPathToBinaryFilesCache() {
        return pathToBinaryFilesCache;
    }

    public void setPathToBinaryFilesCache(String pathToBinaryFilesCache) {
        this.pathToBinaryFilesCache = pathToBinaryFilesCache;
    }
}
