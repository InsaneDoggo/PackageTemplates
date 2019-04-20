package core.actions.newPackageTemplate.dialogs.configure.render;

import com.intellij.ui.ListCellRendererWrapper;
import core.writeRules.WriteRules;
import global.utils.i18n.Localizer;
import global.utils.templates.FileTemplateSource;

import javax.swing.*;

/**
 * Created by Arsen on 04.02.2017.
 */
public class FileTemplateSourceCellRenderer extends ListCellRendererWrapper<FileTemplateSource> {

    @Override
    public void customize(JList list, FileTemplateSource item, int index, boolean selected, boolean hasFocus) {
        if (item != null) {
            setText(Localizer.get(item.getNameLangKey()));
        }
    }

}
