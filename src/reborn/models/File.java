package reborn.models;

import com.google.gson.annotations.Expose;

import java.util.HashMap;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class File extends BaseElement {

    private String templateName;
    private String extension;
    @Expose
    private HashMap<String, String> mapProperties;

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
}
