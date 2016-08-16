package state.export.models;

import com.google.gson.annotations.Expose;

/**
 * Created by CeH9 on 05.08.2016.
 */
public class ExpFileTemplate {

    @Expose
    private String name;
    @Expose
    private String extension;
    @Expose
    private String description;
    @Expose
    private String text;

    public ExpFileTemplate(String name, String extension, String description, String text) {
        this.name = name;
        this.extension = extension;
        this.description = description;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
