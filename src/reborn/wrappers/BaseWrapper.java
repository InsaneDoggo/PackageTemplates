package reborn.wrappers;

import com.intellij.ui.EditorTextField;

import javax.swing.*;

/**
 * Created by CeH9 on 06.07.2016.
 */
public abstract class BaseWrapper {

    public JLabel jlName;
    public EditorTextField etfName;
    private DirectoryWrapper parent;
    // check box
    //btn for groovy

    public abstract void collectDataFromFields();

    public DirectoryWrapper getParent() {
        return parent;
    }

    public void setParent(DirectoryWrapper parent) {
        this.parent = parent;
    }


}
