package groovy;

import com.intellij.ide.dnd.Highlighters;
import com.intellij.lang.Language;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.lang.java.JavaSyntaxHighlighterFactory;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.textarea.TextAreaDocument;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.ClassFileViewProvider;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.ui.EditorSettingsProvider;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.GridBag;
import custom.dialogs.BaseDialog;
import org.jetbrains.annotations.Nullable;
import utils.GridBagFactory;

import java.awt.*;

/**
 * Created by Arsen on 19.07.2016.
 */
public abstract class GroovyDialog extends BaseDialog {

    public abstract void onSuccess(String name);

    public abstract void onCancel();

    public GroovyDialog(@Nullable Project project) {
        super(project);
    }

    EditorTextField etfCode;

    @Override
    public void preShow() {
        panel.setLayout(new GridBagLayout());
        GridBag gridBag = GridBagFactory.getBagForConfigureDialog();

        String code = "String getModifiedName(String name) {\n" +
                "   //TODO return modified \"name\"" +
                "   //A small Example:\n" +
                "   return name.toLowerCase();\n" +
                "}\n";

        etfCode = new EditorTextField(code, project, FileTypeManager.getInstance().getFileTypeByExtension("java"));
        etfCode.setOneLineMode(false);

        EditorFactory.getInstance().createDocument()

//        PsiFileFactory.getInstance(project).createFileFromText("GroovyCode",
//                FileTypeManager.getInstance().getFileTypeByExtension("java"), code);

//        etfCode.addSettingsProvider(new EditorSettingsProvider() {
//            @Override
//            public void customizeSettings(EditorEx editorEx) {
//                editorEx.setHighlighter(JavaSyntaxHighlighterFactory.getSyntaxHighlighter(
//                        new JavaLanguage(),
//
//                        ));
//            }
//        });
//        etfCode.setFileType(FileTypeManager.getInstance().getFileTypeByExtension("java"));

        panel.add(etfCode, gridBag.nextLine().next());
    }

    @Override
    public void onOKAction() {
        onSuccess("dfd");
    }

    @Override
    public void onCancelAction() {
        onCancel();
    }

    @Override
    protected ValidationInfo doValidate() {
        return super.doValidate();
    }
}
