package global.views;

import com.intellij.ui.components.JBRadioButton;

import javax.swing.*;

/**
 * Created by Arsen on 24.01.2017.
 */
public class FavouriteRadioButton extends JBRadioButton {

    private String favouritePath;


    //=================================================================
    //  Getter | Setter
    //=================================================================
    public String getFavouritePath() {
        return favouritePath;
    }

    public void setFavouritePath(String favouritePath) {
        this.favouritePath = favouritePath;
    }


    //=================================================================
    //  Constructors
    //=================================================================
    public FavouriteRadioButton() {
    }

    public FavouriteRadioButton(Icon icon) {
        super(icon);
    }

    public FavouriteRadioButton(Action a) {
        super(a);
    }

    public FavouriteRadioButton(Icon icon, boolean selected) {
        super(icon, selected);
    }

    public FavouriteRadioButton(String text) {
        super(text);
    }

    public FavouriteRadioButton(String text, boolean selected) {
        super(text, selected);
    }

    public FavouriteRadioButton(String text, Icon icon) {
        super(text, icon);
    }

    public FavouriteRadioButton(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
    }
}
