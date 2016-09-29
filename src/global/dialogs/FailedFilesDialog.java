package global.dialogs;

import base.BaseDialog;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.ui.SeparatorComponent;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.GridBag;
import global.utils.*;
import global.utils.Localizer;
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


    public FailedFilesDialog(Project project, String title, PackageTemplateWrapper ptWrapper) {
        super(project);
        this.ptWrapper = ptWrapper;
        init();
        setTitle(title);
    }

    @Override
    public void preShow() {
        panel.setLayout(new GridBagLayout());
        GridBag gridBag = GridBagFactory.getBagForFailedFilesDialog();

        panel.add(new JBLabel(Localizer.get("notification.NextElementsHaventBeenCreated")), gridBag.nextLine().next());
        panel.add(new SeparatorComponent(), gridBag.nextLine().next());

        for (ElementWrapper eWrapper : ptWrapper.getFailedElements()){
            JBLabel label;
            if( eWrapper.isDirectory() ){
                label = new JBLabel(eWrapper.getElement().getName(), AllIcons.Nodes.Package, SwingConstants.LEFT );
            } else {
                label = new JBLabel(eWrapper.getElement().getName(), UIHelper.getIconByFileExtension(((FileWrapper) eWrapper).getFile().getExtension()), SwingConstants.LEFT);
            }
            panel.add(label, gridBag.nextLine().next());
            if(eWrapper.getWriteException() != null && eWrapper.getWriteException().getMessage() != null){
                JBLabel labelException = new JBLabel("Error message: " + eWrapper.getWriteException().getMessage());
                panel.add(labelException, gridBag.nextLine().next().insets(0,32,0,0));
            }
        }

        panel.add(new SeparatorComponent(), gridBag.nextLine().next());
        panel.add(new JBLabel(Localizer.get("notification.NotePressCancelToDeleteCreatedFiles")), gridBag.nextLine().next());
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
