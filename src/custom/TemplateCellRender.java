package custom;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.psi.codeStyle.extractor.ui.ExtractedSettingsDialog;

import javax.swing.*;
import java.awt.*;

/**
 * Created by CeH9 on 24.06.2016.
 */
public class TemplateCellRender extends JLabel implements ListCellRenderer {

    public TemplateCellRender() {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        FileTemplate item = ((FileTemplate) value);

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setText(String.format("%s.%s", item.getName(), item.getExtension()));

        return this;
    }
}