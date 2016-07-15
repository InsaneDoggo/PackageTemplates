package models;

import com.google.gson.annotations.Expose;

/**
 * Created by CeH9 on 06.07.2016.
 */
public abstract class BaseElement {

    @Expose
    private String name;
    @Expose
    private boolean isEnabled;
    @Expose
    private String groovyCode;

    public abstract boolean isDirectory();

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
