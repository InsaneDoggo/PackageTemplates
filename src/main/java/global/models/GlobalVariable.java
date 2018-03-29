package global.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import global.wrappers.PackageTemplateWrapper;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class GlobalVariable {

    @Expose @SerializedName("name") private String name;
    @Expose @SerializedName("isEnabled") private boolean isEnabled;
    @Expose @SerializedName("script") private String script;
    @Expose @SerializedName("value") private String value;

    public static GlobalVariable newInstance() {
        GlobalVariable globalVariable = new GlobalVariable();

        globalVariable.setName(PackageTemplateWrapper.ATTRIBUTE_BASE_NAME);
        globalVariable.setValue("Example");
        globalVariable.setEnabled(true);
        globalVariable.setScript("");

        return globalVariable;
    }


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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
