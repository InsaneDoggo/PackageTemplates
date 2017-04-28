package core.search.customPath.dialog;

import com.intellij.icons.AllIcons;
import com.intellij.ui.ListCellRendererWrapper;
import core.search.SearchActionType;
import global.utils.i18n.Localizer;

import javax.swing.*;

/**
 * Created by Arsen on 04.02.2017.
 */
public class SearchActionTypeCellRenderer extends ListCellRendererWrapper<SearchActionType> {

    @Override
    public void customize(JList list, SearchActionType actionType, int index, boolean selected, boolean hasFocus) {
        if (actionType != null) {
            setText(Localizer.get(actionType.getNameLangKey()));
            switch (actionType) {
                case DIR_ABOVE:
                case DIR_BELOW:
                case DIR_PARENT:
                    setIcon(AllIcons.Nodes.Package);
                    break;
                case FILE:
                    setIcon(AllIcons.FileTypes.Text);
                    break;
            }
        }
    }

}
