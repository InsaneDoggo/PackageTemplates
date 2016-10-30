package global.utils;

import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import global.utils.highligt.HighlightHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created by CeH9 on 16.06.2016.
 */

public class UIHelper {

    public static final int DEFAULT_PADDING = 0;
    public static final int PADDING = 28;
    public static final int PADDING_LABEL = 8;

    public static EditorTextField getEditorTextField(String defValue, Project project) {
        EditorTextField etfName = new EditorTextField();
        etfName.setAlignmentX(Component.LEFT_ALIGNMENT);

        etfName.addSettingsProvider(editor -> {
            HighlightHelper.addHighlightListener(project, etfName, editor, StringTools.PATTERN_ATTRIBUTE);
            HighlightHelper.applyHighlightRange(HighlightHelper.findResults(etfName.getText(), StringTools.PATTERN_ATTRIBUTE), project, editor);
        });

        etfName.setText(defValue);
        return etfName;
    }

    public static void setLeftPadding(JComponent component, int padding) {
        component.setBorder(BorderFactory.createEmptyBorder(
                DEFAULT_PADDING,
                padding,
                DEFAULT_PADDING,
                DEFAULT_PADDING
        ));
    }

    public static void setRightPadding(JComponent component, int padding) {
        component.setBorder(BorderFactory.createEmptyBorder(
                DEFAULT_PADDING,
                DEFAULT_PADDING,
                DEFAULT_PADDING,
                padding
        ));
    }

    public static Icon getIconByFileExtension(String extension) {
        return FileTypeManager.getInstance().getFileTypeByExtension(extension).getIcon();
    }


}
