package global.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class GlobalVariable{

    @Expose @SerializedName("name") private String name;
    @Expose @SerializedName("isEnabled") private boolean isEnabled;
    @Expose @SerializedName("groovyCode") private String groovyCode;
    @Expose @SerializedName("value") private String value;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
