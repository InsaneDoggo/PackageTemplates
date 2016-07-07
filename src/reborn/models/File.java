package reborn.models;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class File extends BaseElement {

    private String templateName;
    private String extension;

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
}
