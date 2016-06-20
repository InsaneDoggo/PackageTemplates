package custom.annotator;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Created by CeH9 on 20.06.2016.
 */
public class PTAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof PsiLiteralExpression) {
            PsiLiteralExpression literalExpression = (PsiLiteralExpression) element;
            String value = literalExpression.getValue() instanceof String ? (String) literalExpression.getValue() : null;

            if (value != null && value.startsWith("simple" + ":")) {
                Project project = element.getProject();
                String key = value.substring(7);
//                List<SimpleProperty> properties = SimpleUtil.findProperties(project, key);
//                if (properties.size() == 1) {
                TextRange range = new TextRange(element.getTextRange().getStartOffset() + 7, element.getTextRange().getEndOffset()-1);
                Annotation annotation = holder.createInfoAnnotation(range, null);
                annotation.setTextAttributes(DefaultLanguageHighlighterColors.NUMBER);
//                }
//                else if (properties.size() == 0) {
//                    TextRange range = new TextRange(element.getTextRange().getStartOffset() + 8,
//                            element.getTextRange().getEndOffset());
//                    holder.createErrorAnnotation(range, "Unresolved property").
//                            registerFix(new CreatePropertyQuickFix(key));
//            }
            }
        }
    }
}