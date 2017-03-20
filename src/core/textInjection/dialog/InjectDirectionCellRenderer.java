package core.textInjection.dialog;

import com.intellij.ui.ListCellRendererWrapper;
import core.textInjection.InjectDirection;
import global.utils.i18n.Localizer;

import javax.swing.*;

/**
 * Created by Arsen on 04.02.2017.
 */
public class InjectDirectionCellRenderer extends ListCellRendererWrapper<InjectDirection> {

    @Override
    public void customize(JList list, InjectDirection direction, int index, boolean selected, boolean hasFocus) {
        if (direction != null) {
            setText(Localizer.get(direction.getNameLangKey()));
        }
    }

}
