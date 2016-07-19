package groovy;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.GridBag;
import custom.dialogs.BaseDialog;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.groovy.GroovyFileType;
import org.jetbrains.plugins.groovy.GroovyLanguage;
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile;
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementFactory;
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
        GridBag gridBag = GridBagFactory.getBagForGroovyDialog();

        String code = "\nstatic String getModifiedName(String name) {\n" +
                "   //A small Example:\n" +
                "   return name.toLowerCase();\n" +
                "}\n";

        PsiFile psiFile = PsiFileFactory.getInstance(project).createFileFromText("GroovyCode", GroovyLanguage.INSTANCE, code);
        GroovyFile groovyFile = GroovyPsiElementFactory.getInstance(project).createGroovyFile(code, true, psiFile);

        etfCode = new EditorTextField(PsiDocumentManager.getInstance(project).getDocument(groovyFile), project, GroovyFileType.GROOVY_FILE_TYPE);
        etfCode.setOneLineMode(false);

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
