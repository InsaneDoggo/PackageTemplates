package reborn.models;

import java.util.ArrayList;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class Directory extends BaseElement {

    private ArrayList<File> listFile;

    public Directory(String name, boolean isEnabled, Directory parent, String groovyCode, ArrayList<File> listFile) {
        super(name, isEnabled, parent, groovyCode);
        this.listFile = listFile;
    }

    public ArrayList<File> getListFile() {
        return listFile;
    }

    public void setListFile(ArrayList<File> listFile) {
        this.listFile = listFile;
    }
}
