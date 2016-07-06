package reborn.models;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class File extends BaseElement {

    private String value;
    private String extension;

    public File(String name, boolean isEnabled, Directory parent, String groovyCode, String value, String extension) {
        super(name, isEnabled, parent, groovyCode);
        this.value = value;
        this.extension = extension;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
