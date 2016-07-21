package utils;

import javax.swing.*;

/**
 * Created by CeH9 on 21.07.2016.
 */
public class CustomIconLoader {

    private static CustomIconLoader instance;

    private static CustomIconLoader getInstance() {
        if (instance == null) {
            instance = new CustomIconLoader();
        }
        return instance;
    }

    public static Icon getIcon(String path) {
        return new ImageIcon(getInstance().getClass().getResource("/icons/" + path));
    }

    public static final Icon Groovy = getIcon("groovy_16x16.png");
    public static final Icon GroovyDisabled = getIcon("groovy_disabled_16x16.png");

}
