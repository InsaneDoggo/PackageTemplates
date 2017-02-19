package global.views;

import com.intellij.ui.components.JBLabel;

/**
 * Created by Arsen on 04.02.2017.
 */
public abstract class IconLabelCustom<Item> extends JBLabel {

    private Item item;

    protected abstract void onUpdateIcon(Item item);

    public IconLabelCustom(String tooltipText, Item item) {
        this.item = item;
        setToolTipText(tooltipText);
    }


    //=================================================================
    //  Public API
    //=================================================================
    /**
     * Обновить иконку
     */
    public  void updateIcon(){
        onUpdateIcon(item);
    }

}
