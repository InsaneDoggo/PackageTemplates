package global.utils.highligt;

import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.codeInsight.template.impl.TemplateColors;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import global.models.TextRange;
import global.utils.text.StringTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Arsen on 04.10.2016.
 */
public class HighlightHelper {

    private static final String VAR_PREFIX = "\\$\\{";
    private static final String VAR_POSTFIX = "\\}";
    private static final int VAR_PREFIX_LENGTH = 2;
    private static final int VAR_POSTFIX_LENGTH = 1;

    public static void addHighlightListener(Project project, EditorTextField etfName, EditorEx editor, String regExpPattern) {
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
                applyHighlightRange(findResults(event.getDocument().getText(), regExpPattern), project, editor);
            }
        });
    }

    public static void applyHighlightRange(ArrayList<TextRange> ranges, Project project, Editor editor) {
        if (project == null) {
            return;
        }

        EditorColorsScheme scheme = editor.getColorsScheme();
        TextAttributes attributes = scheme.getAttributes(TemplateColors.TEMPLATE_VARIABLE_ATTRIBUTES);

        for (TextRange textRange : ranges) {
            HighlightManager.getInstance(project).addRangeHighlight(
                    editor, textRange.getBegin(), textRange.getEnd(), attributes, true, null);
        }
    }

    public static ArrayList<TextRange> findResults(String text, String regExpPattern) {
        Pattern pattern = Pattern.compile(regExpPattern, Pattern.DOTALL);
        ArrayList<TextRange> result = new ArrayList<>();

        boolean doWork = true;
        while (doWork) {
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                result.add(new TextRange(matcher.start(1) - VAR_PREFIX_LENGTH, matcher.end(1) + VAR_POSTFIX_LENGTH));
                text = text.replaceFirst(
                        String.format("%s%s%s", VAR_PREFIX, matcher.group(1), VAR_POSTFIX),
                        getDummyString(matcher.group(1).length() + VAR_PREFIX_LENGTH + VAR_POSTFIX_LENGTH)
                );
            } else {
                doWork = false;
            }
        }

        return result;
    }

    private static String getDummyString(int length) {
        char[] chars = new char[length];
        Arrays.fill(chars, '1');
        return new String(chars);
    }


}
