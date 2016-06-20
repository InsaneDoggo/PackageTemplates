package utils;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.util.EmptyEditorHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.EditorSettingsProvider;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.GridBag;
import models.InputBlock;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Created by CeH9 on 16.06.2016.
 */

public class UIMaker {

    public static final int DEFAULT_PADDING = 0;
    public static final int PADDING = 16;
    public static final int DIALOG_MIN_WIDTH = 400;

    public static EditorTextField getEditorTextField(String defValue) {
        EditorTextField etfName = new EditorTextField("Test");
        etfName.setAlignmentX(Component.LEFT_ALIGNMENT);
        etfName.addSettingsProvider(new EditorSettingsProvider() {
            @Override
            public void customizeSettings(EditorEx editor) {
                editor.getHighlighter();
                TextAttributes attributes = new TextAttributes();
                attributes.setAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT);
                EmptyEditorHighlighter highlighter = new EmptyEditorHighlighter(attributes);
                editor.setHighlighter(highlighter);
            }
        });
        etfName.setText(defValue);
        return etfName;
    }

    public static JLabel getLabel(String text, int paddingScale) {
        JLabel label = new JLabel(text);
        setLeftPadding(label, PADDING * paddingScale);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    public static void setLeftPadding(JComponent component, int padding) {
        component.setBorder(BorderFactory.createEmptyBorder(
                DEFAULT_PADDING,
                padding,
                DEFAULT_PADDING,
                DEFAULT_PADDING
        ));
    }

    public static JPanel getClassPanel(InputBlock inputBlock, int paddingScale) {
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());

        GridBag bag = getDefaultGridBag();

        JLabel jLabel = new JLabel(AllIcons.Nodes.Class, SwingConstants.LEFT);
        setLeftPadding(container, DEFAULT_PADDING + PADDING * paddingScale);

        container.add(jLabel, bag.nextLine().next());
        container.add(inputBlock.getTfName(), bag.next());

        return container;
    }

    @NotNull
    public static GridBag getDefaultGridBag() {
        return new GridBag()
                .setDefaultWeightX(1, 1)
                .setDefaultInsets(new Insets(4,4,4,4))
                .setDefaultFill(GridBagConstraints.HORIZONTAL);
    }

    public static JPanel getDirectoryPanel(InputBlock inputBlock, int paddingScale) {
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());

        GridBag bag = getDefaultGridBag();

        JLabel jLabel = new JLabel(AllIcons.Nodes.Package, SwingConstants.LEFT);
        setLeftPadding(container, DEFAULT_PADDING + PADDING * paddingScale);

        container.add(jLabel, bag.nextLine().next());
        container.add(inputBlock.getTfName(), bag.next());

        return container;
    }
}
