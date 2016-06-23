package models;

/**
 * Created by CeH9 on 23.06.2016.
 */
public class TemplateInfo {

    private String name;
    private String templateName;

    public TemplateInfo(String name, String templateName) {
        this.name = name;
        this.templateName = templateName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
