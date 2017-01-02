package global.dialogs;

import base.BaseDialog;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.SeparatorComponent;
import com.intellij.ui.components.JBLabel;
import global.Const;
import global.listeners.ClickListener;
import global.utils.UIHelper;
import global.utils.i18n.Localizer;
import global.wrappers.ElementWrapper;
import global.wrappers.FileWrapper;
import global.wrappers.PackageTemplateWrapper;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * Created by CeH9 on 14.06.2016.
 */
public abstract class FailedFilesDialog extends BaseDialog {

    private PackageTemplateWrapper ptWrapper;

    public FailedFilesDialog(Project project, String title, PackageTemplateWrapper ptWrapper) {
        super(project);
        this.ptWrapper = ptWrapper;
        init();
        setTitle(title);
    }

    @Override
    public void preShow() {
        panel.setLayout(new MigLayout());
        panel.add(new JBLabel(
                Localizer.get("notification.ElementsListedBelowHaventBeenCreated")),
                new CC().spanX().wrap()
        );

        addSeparator();
        createErrorViews();
        addSeparator();

        panel.add(new JBLabel(
                Localizer.get("notification.NotePressCancelToDeleteCreatedFiles")),
                new CC().spanX().wrap()
        );
    }

    private void createErrorViews() {
        for (ElementWrapper eWrapper : ptWrapper.getFailedElements()) {
            JBLabel label;
            if (eWrapper.isDirectory()) {
                // Directory
                label = new JBLabel(
                        eWrapper.getElement().getName(),
                        AllIcons.Nodes.Package,
                        SwingConstants.LEFT
                );
            } else {
                // File
                label = new JBLabel(
                        eWrapper.getElement().getName(),
                        UIHelper.getIconByFileExtension(((FileWrapper) eWrapper).getFile().getExtension()),
                        SwingConstants.LEFT
                );
            }
            panel.add(label, new CC().spanX().wrap());
            if (eWrapper.getWriteException() != null && eWrapper.getWriteException().getMessage() != null) {
                addErrorRowView(eWrapper);
            }
        }
    }

    private void addErrorRowView(final ElementWrapper eWrapper) {
        JBLabel labelException = new JBLabel("Error message: " + getCompactMessage(eWrapper.getWriteException().getMessage()));
        JButton btnDetails = new JButton(Localizer.get("action.ShowDetails"), AllIcons.General.InspectionsEye);

        btnDetails.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Messages.showMessageDialog(
                        eWrapper.getWriteException().getMessage(),
                        eWrapper.getElement().getName(),
                        Messages.getInformationIcon()
                );
            }
        });

        panel.add(labelException, new CC().pushX().growX().pad(0,32,0,0));
        panel.add(btnDetails, new CC().wrap());
    }


    //=================================================================
    //  Utils
    //=================================================================
    private String getCompactMessage(String text) {
        if (text.length() > Const.MESSAGE_MAX_LENGTH) {
            text = text.substring(0, Const.MESSAGE_MAX_LENGTH) + "..";
        }
        return text;
    }

    private void addSeparator() {
        panel.add(new SeparatorComponent(), new CC().growX().spanX().wrap());
    }

    //=================================================================
    //  Dialog specific stuff
    //=================================================================
    public abstract void onSuccess();

    public abstract void onCancel();

    @Override
    public void onOKAction() {
        onSuccess();
    }

    @Override
    public void onCancelAction() {
        onCancel();
    }
}
