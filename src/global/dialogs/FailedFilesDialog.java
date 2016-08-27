package global.dialogs;

import base.BaseDialog;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.GridBag;
import global.utils.*;
import global.wrappers.ElementWrapper;
import global.wrappers.FileWrapper;
import global.wrappers.PackageTemplateWrapper;

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

        panel.add(new JBLabel(Localizer.get("NextElementsHaventBeenCreated")), gridBag.nextLine().next().insets(4,0,16,0));

        for (ElementWrapper eWrapper : ptWrapper.getFailedElements()){
            JBLabel label;
            if( eWrapper.isDirectory() ){
                label = new JBLabel(eWrapper.getElement().getName(), AllIcons.Nodes.Package, SwingConstants.LEFT );
            } else {
                label = new JBLabel(eWrapper.getElement().getName(), UIHelper.getIconByFileExtension(((FileWrapper) eWrapper).getFile().getExtension()), SwingConstants.LEFT);
            }
            panel.add(label, gridBag.nextLine().next());
        }

        panel.add(new JBLabel(Localizer.get("NotePressCancelToDeleteCreatedFiles")), gridBag.nextLine().next().insets(16,0,4,0));
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
