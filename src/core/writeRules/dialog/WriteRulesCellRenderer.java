package core.writeRules.dialog;

import com.intellij.ui.ListCellRendererWrapper;
import core.writeRules.WriteRules;
import global.utils.i18n.Localizer;

import javax.swing.*;

/**
 * Created by Arsen on 04.02.2017.
 */
public class WriteRulesCellRenderer extends ListCellRendererWrapper<WriteRules> {

    @Override
    public void customize(JList list, WriteRules item, int index, boolean selected, boolean hasFocus) {
        if (item != null) {
            setText(Localizer.get(item.getNameLangKey()));
            setIcon(item.toIcon());
        }
    }

}
