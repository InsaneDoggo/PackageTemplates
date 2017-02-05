package global.views;

import com.intellij.ui.components.JBLabel;

import javax.swing.*;

/**
 * Created by Arsen on 04.02.2017.
 */
public class IconLabel extends JBLabel {

    private Icon iconOn;
    private Icon iconOff;

    public IconLabel(String tooltipText, Icon iconOn, Icon iconOff) {
        this.iconOn = iconOn;
        this.iconOff = iconOff;
        setToolTipText(tooltipText);
    }


    //=================================================================
    //  Public API
    //=================================================================
    /**
     * Отобразить On иконку
     */
    public void enableIcon(){
        setIcon(iconOn);
    }

    /**
     * Отобразить Off иконку
     */
    public void disableIcon(){
        setIcon(iconOff);
    }

}
