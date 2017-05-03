package global.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import core.writeRules.WriteRules;
import global.utils.templates.FileTemplateSource;

import java.util.HashMap;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class File extends BaseElement {

    @Expose @SerializedName("templateName") private String templateName;
    @Expose @SerializedName("extension") private String extension;
    @Expose @SerializedName("mapProperties") private HashMap<String, String> mapProperties;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public HashMap<String, String> getMapProperties() {
        return mapProperties;
    }

    public void setMapProperties(HashMap<String, String> mapProperties) {
        this.mapProperties = mapProperties;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    public static File newInstance(String templateName, String extension) {
        File file = new File();
        file.setWriteRules(WriteRules.FROM_PARENT);
        file.setName("Unnamed");
        file.setTemplateName(templateName);
        file.setExtension(extension);
        file.setEnabled(true);
        file.setScript("");
        file.setMapProperties(new HashMap<>());

        return file;
    }
}
