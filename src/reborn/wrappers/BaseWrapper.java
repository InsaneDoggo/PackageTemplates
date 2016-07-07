package reborn.wrappers;

import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.GridBag;
import reborn.models.BaseElement;

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

    public abstract void buildView(Project project, JPanel container, GridBag bag);
    public abstract void reBuild(Project project);
    public abstract void collectDataFromFields();
    public abstract void removeMyself();
    public abstract void addElement(BaseWrapper element);
    public abstract BaseElement getElement();

    public DirectoryWrapper getParent() {
        return parent;
    }

    public void setParent(DirectoryWrapper parent) {
        this.parent = parent;
    }


}
