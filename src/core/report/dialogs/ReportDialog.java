package core.report.dialogs;

import base.BaseDialog;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.SeparatorComponent;
import com.intellij.ui.components.JBLabel;
import core.actions.custom.CreateFileFromTemplateAction;
import core.report.ReportHelper;
import core.report.enums.ReportType;
import core.report.models.BaseReport;
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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by CeH9 on 14.06.2016.
 */
public abstract class ReportDialog extends BaseDialog {

    public ReportDialog(Project project) {
        super(project);
        init();
        setTitle(Localizer.get("title.ReportDialog"));
    }

    @Override
    public void preShow() {
        panel.setLayout(new MigLayout());
        panel.add(new JBLabel(
                        Localizer.get("label.Actions")),
                new CC().spanX().wrap()
        );

        addSeparator();
    }

    private void createViews() {
        HashMap<Integer, BaseReport> reports = ReportHelper.getReports();

        // SUCCESS
        for (BaseReport report : reports.values()) {
            if (report.getType() == ReportType.SUCCESS_ACTION) {
                JBLabel label;
                Icon icon;

                if (report.getAction() instanceof CreateFileFromTemplateAction) {
                    icon = AllIcons.FileTypes.Any_type;
                } else {
                    icon = AllIcons.Nodes.Package;
                }

                label = new JBLabel(report.getAction().toString(), icon, SwingConstants.LEFT);
                panel.add(label, new CC().wrap());
            }
        }

        // FAILED
        for (BaseReport report : reports.values()) {
            JBLabel label;
            if (report.getType() == ReportType.FAILED_ACTION) {

            }
        }

        // PENDING
        for (BaseReport report : reports.values()) {
            JBLabel label;
            if (report.getType() == ReportType.PENDING_ACTION) {

            }
        }

//        JButton btnDetails = new JButton(Localizer.get("action.ShowDetails"), AllIcons.General.InspectionsEye);
//        btnDetails.addMouseListener(new ClickListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                Messages.showMessageDialog(
//                        eWrapper.getWriteException().getMessage(),
//                        eWrapper.getElement().getName(),
//                        Messages.getInformationIcon()
//                );
//            }
//        });
//        panel.add(btnDetails, new CC().wrap());
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
