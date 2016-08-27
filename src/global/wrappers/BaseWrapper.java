package global.wrappers;

import com.intellij.ui.components.JBCheckBox;

import javax.swing.*;

/**
 * Created by Arsen on 11.07.2016.
 */
public abstract class BaseWrapper {

    JBCheckBox cbEnabled;
    JLabel jlGroovy;

    public abstract void collectDataFromFields();
    public abstract void runGroovyScript();
    public abstract void updateComponentsState();

}
