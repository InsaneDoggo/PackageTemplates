package global.wrappers;

import com.intellij.ui.components.JBCheckBox;
import global.views.IconLabel;

import javax.swing.*;

/**
 * Created by Arsen on 11.07.2016.
 */
public abstract class BaseWrapper {

    protected JBCheckBox cbEnabled;
    protected IconLabel jlScript;

    public abstract void updateComponentsState();

}
