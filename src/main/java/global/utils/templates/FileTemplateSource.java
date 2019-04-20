package global.utils.templates;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arsen on 19.04.2017.
 */
public enum FileTemplateSource {
    
    @SerializedName("DEFAULT_ONLY")DEFAULT_ONLY("FileTemplateSource.DefaultOnly"),
    @SerializedName("PROJECT_ONLY")PROJECT_ONLY("FileTemplateSource.ProjectOnly"),
    @SerializedName("PROJECT_PRIORITY")PROJECT_PRIORITY("FileTemplateSource.ProjectPriority"),
    @SerializedName("DEFAULT_PRIORITY")DEFAULT_PRIORITY("FileTemplateSource.DefaultPriority");

    private String nameLangKey;

    FileTemplateSource(String nameLangKey) {
        this.nameLangKey = nameLangKey;
    }

    public String getNameLangKey() {
        return nameLangKey;
    }

    public static FileTemplateSource getDefault() {
        return DEFAULT_ONLY;
    }

}
