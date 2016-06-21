package utils;

import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.codeInsight.template.impl.TemplateColors;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorSettingsProvider;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.UIUtil;
import models.InputBlock;
import models.TextRange;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by CeH9 on 16.06.2016.
 */

public class UIMaker {

    public static final int DEFAULT_PADDING = 0;
    public static final int PADDING = 16;
    public static final int DIALOG_MIN_WIDTH = 400;
    public static final Color COLOR_VARIABLES = new Color(174, 138, 190);

    public static EditorTextField getEditorTextField(String defValue, Project project) {
        EditorTextField etfName = new EditorTextField("Test");
        etfName.setAlignmentX(Component.LEFT_ALIGNMENT);

        etfName.addSettingsProvider(new EditorSettingsProvider() {
            @Override
            public void customizeSettings(EditorEx editor) {
//                EditorHighlighter highlighter = EditorHighlighterFactory.getInstance().createEditorHighlighter(project, FileTypeManager.getInstance().getFileTypeByExtension("JAVA"));
//                editor.setHighlighter(highlighter);
                    addHighlightListener(project, etfName, editor);
                    etfName.setText(etfName.getText());

            }
        });
        etfName.setText(defValue);
        return etfName;
    }

    private static void addHighlightListener(final Project project, final EditorTextField etfName, EditorEx editor) {
        etfName.addDocumentListener(new DocumentListener() {
            @Override
            public void beforeDocumentChange(DocumentEvent event) {
                EditorColorsManager.getInstance().getGlobalScheme().getColor(TemplateColors.TEMPLATE_VARIABLE_ATTRIBUTES)
                UIUtil.get
                EditorColors.LIVE_TEMPLATE_ATTRIBUTES

            }

            @Override
            public void documentChanged(DocumentEvent event) {
                if(editor==null || !StringTools.containsVariable(event.getDocument().getText())){
                    return;
                }
                //highlight text
                TextAttributes attributes = new TextAttributes();
                attributes.setForegroundColor(COLOR_VARIABLES);
                ArrayList<TextRange> list = StringTools.findVariable(event.getDocument().getText());
                for( TextRange textRange:  list ) {
                    HighlightManager.getInstance(project).addRangeHighlight(
                            editor,
                            textRange.getBegin(),
                            textRange.getEnd(),
                            attributes,
                            true,
                            null
                    );
                }
            }
        });
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
