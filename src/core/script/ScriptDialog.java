package core.script;

import base.BaseDialog;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.GridBag;
import global.listeners.ClickListener;
import global.utils.factories.GridBagFactory;
import global.utils.i18n.Localizer;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by Arsen on 19.07.2016.
 */
public abstract class ScriptDialog extends BaseDialog {

    private String code = ScriptExecutor.defaultCode;

    public ScriptDialog(@Nullable Project project, String code) {
        super(project);
        if (code != null) {
            this.code = code;
        }
    }

    public ScriptDialog(@Nullable Project project) {
        super(project);
    }

    private EditorTextField etfCode;
    private EditorTextField etfName;
    private JButton btnTry;
    private JBLabel jlResult;

    @Override
    public void preShow() {
        panel.setLayout(new GridBagLayout());
        GridBag gridBag = GridBagFactory.getBagForScriptDialog();

        createEditorField();
        createViews();

        panel.add(etfCode, gridBag.nextLine().next().weighty(1).fillCell());
        panel.add(etfName, gridBag.nextLine().next());
        panel.add(btnTry, gridBag.nextLine().next().insets(4, 0, 4, 0));
        panel.add(jlResult, gridBag.nextLine().next());
    }

    private void createEditorField() {
        PsiFile psiFile = PsiFileFactory.getInstance(project).createFileFromText("Script", PlainTextLanguage.INSTANCE, code);

        etfCode = new EditorTextField(PsiDocumentManager.getInstance(project).getDocument(psiFile), project, PlainTextFileType.INSTANCE);
        etfCode.setOneLineMode(false);
    }

    private void createViews() {
        etfName = new EditorTextField(Localizer.get("ExampleName"));
        btnTry = new JButton(Localizer.get("TryIt"));
        jlResult = new JBLabel(Localizer.get("ResultWillBeHere"));

        etfName.setAlignmentX(Component.CENTER_ALIGNMENT);
        etfName.setAlignmentY(Component.CENTER_ALIGNMENT);
        btnTry.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTry.setAlignmentY(Component.CENTER_ALIGNMENT);
        jlResult.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlResult.setAlignmentY(Component.CENTER_ALIGNMENT);

        btnTry.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jlResult.setText(ScriptExecutor.runScript(etfCode.getText(), etfName.getText()));
            }
        });
    }


    //=================================================================
    //  Dialog specific stuff
    //=================================================================
    public abstract void onSuccess(String code);

    @Override
    public void onOKAction() {
        onSuccess(etfCode.getText());
    }

    @Override
    public void onCancelAction() {}

    @Override
    protected ValidationInfo doValidate() {
        return ScriptExecutor.ValidateScript(etfCode, etfName.getText());
    }

}
