package custom.dialogs;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.GridBag;
import models.PackageTemplate;
import utils.*;
import wrappers.ElementWrapper;
import wrappers.FileWrapper;
import wrappers.GlobalVariableWrapper;
import wrappers.PackageTemplateWrapper;

import javax.swing.*;
import java.awt.*;

/**
 * Created by CeH9 on 14.06.2016.
 */
public abstract class FailedFilesDialog extends BaseDialog {

    public abstract void onSuccess();

    public abstract void onCancel();

    private PackageTemplateWrapper ptWrapper;
    AnActionEvent event;


    public FailedFilesDialog(AnActionEvent event, String title, PackageTemplateWrapper ptWrapper) {
        super(event.getProject());
        this.event = event;
        this.ptWrapper = ptWrapper;
        init();
        setTitle(title);
    }

    @Override
    protected ValidationInfo doValidate() {
        ValidationInfo result;

        return null;
    }


    @Override
    public void preShow() {
        panel.setLayout(new GridBagLayout());
        GridBag gridBag = GridBagFactory.getBagForConfigureDialog();

        for (ElementWrapper eWrapper : ptWrapper.getFailedElements()){
            JBLabel label;
            if( eWrapper.isDirectory() ){
                label = new JBLabel(eWrapper.getElement().getName(), AllIcons.Nodes.Package, SwingConstants.CENTER );
            } else {
                label = new JBLabel(eWrapper.getElement().getName(), UIHelper.getIconByFileExtension(((FileWrapper) eWrapper).getFile().getExtension()), SwingConstants.CENTER);
            }
            panel.add(label, gridBag.nextLine().next());
        }

    }

    @Override
    public void onOKAction() {
        onSuccess();
    }

    @Override
    public void onCancelAction() {
        onCancel();
    }
}
