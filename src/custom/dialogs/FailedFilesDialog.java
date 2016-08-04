package custom.dialogs;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.GridBag;
import models.PackageTemplate;
import org.jetbrains.annotations.NotNull;
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


    public FailedFilesDialog(AnActionEvent event, String title, PackageTemplateWrapper ptWrapper) {
        super(event.getProject());
        this.ptWrapper = ptWrapper;
        init();
        setTitle(title);
    }

    @Override
    public void preShow() {
        panel.setLayout(new GridBagLayout());
        GridBag gridBag = GridBagFactory.getBagForFailedFilesDialog();

        panel.add(new JBLabel(Localizer.get("NextElementsHaventBeenCreated")), gridBag.nextLine().next());

        for (ElementWrapper eWrapper : ptWrapper.getFailedElements()){
            JBLabel label;
            if( eWrapper.isDirectory() ){
                label = new JBLabel(eWrapper.getElement().getName(), AllIcons.Nodes.Package, SwingConstants.LEFT );
            } else {
                label = new JBLabel(eWrapper.getElement().getName(), UIHelper.getIconByFileExtension(((FileWrapper) eWrapper).getFile().getExtension()), SwingConstants.LEFT);
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
