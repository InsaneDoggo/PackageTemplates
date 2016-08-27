package global.models;


import com.google.gson.annotations.Expose;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class GlobalVariable extends BaseElement{

    @Expose
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }
}
