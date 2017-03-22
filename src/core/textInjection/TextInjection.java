package core.textInjection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import core.search.customPath.CustomPath;

/**
 * Created by Arsen on 15.03.2017.
 */
public class TextInjection {

    @Expose @SerializedName("isEnabled") private boolean isEnabled;
    @Expose @SerializedName("description") private String description;
    @Expose @SerializedName("customPath") private CustomPath customPath;
    @Expose @SerializedName("injectDirection") private InjectDirection injectDirection;
    @Expose @SerializedName("textToSearch") private String textToSearch;
    @Expose @SerializedName("isRegexp") private boolean isRegexp;
    @Expose @SerializedName("textToInject") private String textToInject;

    public TextInjection() {
        isEnabled = true;
        description = "";
        customPath = null;
        injectDirection = InjectDirection.NEXT_LINE;
        textToSearch = "";
        isRegexp = false;
        textToInject = "";
    }

    public void copyPropertiesFrom(TextInjection result) {
        setDescription(result.getDescription());
        setCustomPath(result.getCustomPath());
        setInjectDirection(result.getInjectDirection());
        setTextToSearch(result.getTextToSearch());
        setRegexp(result.isRegexp());
        setTextToInject(result.getTextToInject());
    }

    //=================================================================
    //  Get|Set
    //=================================================================
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CustomPath getCustomPath() {
        return customPath;
    }

    public void setCustomPath(CustomPath customPath) {
        this.customPath = customPath;
    }

    public InjectDirection getInjectDirection() {
        return injectDirection;
    }

    public void setInjectDirection(InjectDirection injectDirection) {
        this.injectDirection = injectDirection;
    }

    public String getTextToSearch() {
        return textToSearch;
    }

    public void setTextToSearch(String textToSearch) {
        this.textToSearch = textToSearch;
    }

    public boolean isRegexp() {
        return isRegexp;
    }

    public void setRegexp(boolean regexp) {
        isRegexp = regexp;
    }

    public String getTextToInject() {
        return textToInject;
    }

    public void setTextToInject(String textToInject) {
        this.textToInject = textToInject;
    }

}
