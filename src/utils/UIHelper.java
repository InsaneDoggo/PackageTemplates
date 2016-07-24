package utils;

import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.codeInsight.template.impl.TemplateColors;
import com.intellij.icons.AllIcons;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.ui.EditorSettingsProvider;
import com.intellij.ui.EditorTextField;
import custom.dialogs.SelectFileTemplateDialog;
import custom.impl.ClickListener;
import models.Directory;
import models.File;
import models.TextRange;
import org.jetbrains.annotations.NotNull;
import wrappers.DirectoryWrapper;
import wrappers.ElementWrapper;
import wrappers.FileWrapper;
import wrappers.PackageTemplateWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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

        etfName.addSettingsProvider(new EditorSettingsProvider() {
            @Override
            public void customizeSettings(EditorEx editor) {
                addHighlightListener(project, etfName, editor);
                applyHighlightRange(etfName.getText(), project, editor);
            }
        });

        etfName.setText(defValue);
        return etfName;
    }

    private static void addHighlightListener(final Project project, final EditorTextField etfName, EditorEx editor) {
        etfName.addDocumentListener(new DocumentListener() {
            @Override
            public void beforeDocumentChange(DocumentEvent event) {
            }

            @Override
            public void documentChanged(DocumentEvent event) {
                if (editor == null || !StringTools.containsVariable(event.getDocument().getText())) {
                    return;
                }
                //highlight text
                applyHighlightRange(event.getDocument().getText(), project, editor);
            }
        });
    }

    private static void applyHighlightRange(String text, Project project, Editor editor) {
        if (project == null) {
            return;
        }

        EditorColorsScheme scheme = editor.getColorsScheme();
        TextAttributes attributes = scheme.getAttributes(TemplateColors.TEMPLATE_VARIABLE_ATTRIBUTES);

        for (TextRange textRange : StringTools.findVariable(text)) {
            HighlightManager.getInstance(project).addRangeHighlight(
                    editor, textRange.getBegin(), textRange.getEnd(), attributes, true, null);
        }
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

    public static void setHorizontalPadding(JComponent component, int padding) {
        component.setBorder(BorderFactory.createEmptyBorder(
                DEFAULT_PADDING,
                padding,
                DEFAULT_PADDING,
                padding
        ));
    }

    public static Icon getIconByFileExtension(String extension) {
        return FileTypeManager.getInstance().getFileTypeByExtension(extension).getIcon();
    }

}
