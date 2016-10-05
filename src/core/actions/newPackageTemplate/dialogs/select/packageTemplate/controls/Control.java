package core.actions.newPackageTemplate.dialogs.select.packageTemplate.controls;

import global.listeners.ReleaseListener;

import javax.swing.*;

/**
 * Created by Arsen on 06.10.2016.
 */
public class Control {

    private Icon icon;
    private ReleaseListener releaseListener;

    public Control(Icon icon, ReleaseListener releaseListener) {
        this.icon = icon;
        this.releaseListener = releaseListener;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public ReleaseListener getReleaseListener() {
        return releaseListener;
    }

    public void setReleaseListener(ReleaseListener releaseListener) {
        this.releaseListener = releaseListener;
    }
}
