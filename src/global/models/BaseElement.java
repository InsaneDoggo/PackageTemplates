package global.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import core.library.models.Alias;
import core.search.customPath.CustomPath;
import core.writeRules.WriteRules;

/**
 * Created by CeH9 on 06.07.2016.
 */
public abstract class BaseElement {

    @Expose @SerializedName("name") private String name;
    @Expose @SerializedName("isEnabled") private boolean isEnabled;
    @Expose @SerializedName("script") private String script;
    @Expose @SerializedName("customPath") private CustomPath customPath;
    @Expose @SerializedName("writeRules") private WriteRules writeRules;
    @Expose @SerializedName("alias") private Alias alias;

    public abstract boolean isDirectory();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public CustomPath getCustomPath() {
        return customPath;
    }

    public void setCustomPath(CustomPath customPath) {
        this.customPath = customPath;
    }

    public WriteRules getWriteRules() {
        return writeRules;
    }

    public void setWriteRules(WriteRules writeRules) {
        this.writeRules = writeRules;
    }

    public Alias getAlias() {
        return alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

}
