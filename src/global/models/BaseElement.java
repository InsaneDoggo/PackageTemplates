package global.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import core.search.CustomPath;

/**
 * Created by CeH9 on 06.07.2016.
 */
public abstract class BaseElement {

    @Expose @SerializedName("name") private String name;
    @Expose @SerializedName("isEnabled") private boolean isEnabled;
    @Expose @SerializedName("groovyCode") private String groovyCode;
    @Expose @SerializedName("customPath") private CustomPath customPath;
    @Expose @SerializedName("writeBehavior") private WriteBehavior writeBehavior;

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

    public String getGroovyCode() {
        return groovyCode;
    }

    public void setGroovyCode(String groovyCode) {
        this.groovyCode = groovyCode;
    }

    public CustomPath getCustomPath() {
        return customPath;
    }

    public void setCustomPath(CustomPath customPath) {
        this.customPath = customPath;
    }

    public WriteBehavior getWriteBehavior() {
        return writeBehavior;
    }

    public void setWriteBehavior(WriteBehavior writeBehavior) {
        this.writeBehavior = writeBehavior;
    }
}
