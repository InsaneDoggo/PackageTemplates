package core.groovy;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.GridBag;
import base.BaseDialog;
import global.listeners.ClickListener;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.groovy.GroovyFileType;
import org.jetbrains.plugins.groovy.GroovyLanguage;
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile;
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementFactory;
import global.utils.GridBagFactory;
import global.utils.i18n.Localizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by Arsen on 19.07.2016.
 */
public abstract class GroovyDialog extends BaseDialog {

    public abstract void onSuccess(String code);

    public void onCancel(){

    }

    private String code = "\nstatic String getModifiedName(String name) {\n" +
            "   //A small Example:\n" +
            "   return name.toLowerCase();\n" +
            "}\n";

    public GroovyDialog(@Nullable Project project, String code) {
        super(project);
        if (code != null) {
            this.code = code;
        }
    }

    public GroovyDialog(@Nullable Project project) {
        super(project);
    }

    private EditorTextField etfCode;
    private EditorTextField etfName;


    @Override
    public void preShow() {
        panel.setLayout(new GridBagLayout());
        GridBag gridBag = GridBagFactory.getBagForGroovyDialog();

        createEditorField();

        etfName = new EditorTextField(Localizer.get("ExampleName"));
        JButton btnTry = new JButton(Localizer.get("TryIt"));
        JBLabel jlResult = new JBLabel(Localizer.get("ResultWillBeHere"));

        etfName.setAlignmentX(Component.CENTER_ALIGNMENT);
        etfName.setAlignmentY(Component.CENTER_ALIGNMENT);
        btnTry.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTry.setAlignmentY(Component.CENTER_ALIGNMENT);
        jlResult.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlResult.setAlignmentY(Component.CENTER_ALIGNMENT);

        btnTry.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jlResult.setText(GroovyExecutor.runGroovy(etfCode.getText(), etfName.getText()));
            }
        });

        panel.add(etfCode, gridBag.nextLine().next().weighty(1).fillCell());
        panel.add(etfName, gridBag.nextLine().next());
        panel.add(btnTry, gridBag.nextLine().next().insets(4, 0, 4, 0));
        panel.add(jlResult, gridBag.nextLine().next());
    }


    private void createEditorField() {
        PsiFile psiFile = PsiFileFactory.getInstance(project).createFileFromText("GroovyCode", GroovyLanguage.INSTANCE, code);
        GroovyFile groovyFile = GroovyPsiElementFactory.getInstance(project).createGroovyFile(code, true, psiFile);

        etfCode = new EditorTextField(PsiDocumentManager.getInstance(project).getDocument(groovyFile), project, GroovyFileType.GROOVY_FILE_TYPE);
        etfCode.setOneLineMode(false);
    }

    @Override
    public void onOKAction() {
        onSuccess(etfCode.getText());
    }

    @Override
    public void onCancelAction() {
        onCancel();
    }

    @Override
    protected ValidationInfo doValidate() {
        return GroovyExecutor.ValidateGroovyCode(etfCode, etfName.getText());
    }
}
