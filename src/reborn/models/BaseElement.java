package reborn.models;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class BaseElement {

    private String name;
    private boolean isEnabled;
    private String groovyCode;

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
}
