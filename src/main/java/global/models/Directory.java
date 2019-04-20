package global.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import core.writeRules.WriteRules;
import global.wrappers.PackageTemplateWrapper;

import java.util.ArrayList;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class Directory extends BaseElement {

    @Expose @SerializedName("listBaseElement") private ArrayList<BaseElement> listBaseElement;

    public static Directory newInstance() {
        Directory directory = new Directory();

        directory.setName("unnamed");
        directory.setWriteRules(WriteRules.FROM_PARENT);
        directory.setEnabled(true);
        directory.setScript("");
        directory.setListBaseElement(new ArrayList<>());

        return directory;
    }


    public ArrayList<BaseElement> getListBaseElement() {
        return listBaseElement;
    }

    public void setListBaseElement(ArrayList<BaseElement> listBaseElement) {
        this.listBaseElement = listBaseElement;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

}
