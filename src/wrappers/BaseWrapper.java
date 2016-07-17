package wrappers;

import com.intellij.ui.components.JBCheckBox;

/**
 * Created by Arsen on 11.07.2016.
 */
public abstract class BaseWrapper {

    JBCheckBox cbEnabled;

    public abstract void collectDataFromFields();
    public abstract void runGroovyScript();
    public abstract void updateComponentsState();

}
