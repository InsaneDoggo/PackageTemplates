package core.report.dialogs;

import base.BaseDialog;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.SeparatorComponent;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import core.report.ReportHelper;
import core.report.enums.ReportType;
import core.report.models.BaseReport;
import global.listeners.ClickListener;
import global.utils.i18n.Localizer;
import icons.PluginIcons;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
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
        panel.setLayout(new MigLayout(new LC().fillX()));
        panel.add(new JBLabel(
                        Localizer.get("label.Actions")),
                new CC().spanX().wrap()
        );

        addSeparator();
        createViews();

        pack();
    }


    //=================================================================
    //  UI
    //=================================================================
    private void createViews() {
        HashMap<Integer, BaseReport> reports = ReportHelper.getReports();
        boolean hasErrors = false;

        // SUCCESS
        for (BaseReport report : reports.values()) {
            if (report.getType() != ReportType.SUCCESS_ACTION) {
                continue;
            }

            addSuccess(report);
        }

        // FAILED
        for (BaseReport report : reports.values()) {
            if (report.getType() != ReportType.FAILED_ACTION) {
                continue;
            }
            addFailed(report);
            hasErrors = true;
        }

        // PENDING
        for (BaseReport report : reports.values()) {
            if (report.getType() != ReportType.PENDING_ACTION) {
                continue;
            }

            addPending(report);
        }

        if (hasErrors) {
            addSeparator();
            addUndoTip();
        }
    }

    private void addSuccess(BaseReport report) {
        // Icon
        JBLabel iconLabel = new JBLabel(PluginIcons.REPORT_SUCCESS);
        iconLabel.setToolTipText(Localizer.get("tooltip.ReportSuccess"));

        // Label
        JBLabel label = new JBLabel(report.getAction().toString(), report.toIcon(), SwingConstants.LEFT);

        panel.add(iconLabel, new CC().spanX().split(2));
        panel.add(label, new CC().wrap());
    }

    private void addFailed(final BaseReport report) {
        //Icon
        JBLabel iconLabel = new JBLabel(PluginIcons.REPORT_FAIL);
        iconLabel.setToolTipText(Localizer.get("tooltip.ReportFailed"));

        //Label
        JBLabel label = new JBLabel(report.getAction().toString(), report.toIcon(), SwingConstants.LEFT);

        // Button details
        JButton btnDetails = new JButton(Localizer.get("action.ShowDetails"), AllIcons.General.InspectionsEye);
        btnDetails.repaint();
        btnDetails.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Messages.showMessageDialog(
                        report.getMessage(),
                        Localizer.get("title.ErrorDetails"),
                        Messages.getInformationIcon()
                );
            }
        });

        panel.add(iconLabel, new CC().spanX().split(3));
        panel.add(label, new CC());
        panel.add(btnDetails, new CC().wrap().pad(0, 0, 0, 0));
    }

    private void addPending(BaseReport report) {
        //Icon
        JBLabel iconLabel = new JBLabel(PluginIcons.REPORT_NEUTRAL);
        iconLabel.setToolTipText(Localizer.get("tooltip.ReportPending"));

        //Label
        JBLabel label = new JBLabel(report.getAction().toString(), report.toIcon(), SwingConstants.LEFT);

        panel.add(iconLabel, new CC().spanX().split(2));
        panel.add(label, new CC().wrap());
    }

    private void addUndoTip() {
        JBLabel label = new JBLabel(Localizer.get("text.UndoTip"));
        panel.add(label, new CC().wrap());
    }


    //=================================================================
    //  Utils
    //=================================================================
    private void addSeparator() {
        panel.add(new SeparatorComponent(), new CC().growX().spanX().wrap());
    }


    //=================================================================
    //  Dialog specific stuff
    //=================================================================
    public abstract void onSuccess();

    @Override
    public void onOKAction() {
        onSuccess();
    }


    @NotNull
    @Override
    protected Action[] createActions() {
        Action okAction = getOKAction();
        okAction.putValue(Action.NAME, Localizer.get("action.Close"));
        return new Action[]{okAction};
    }

    @Override
    protected JComponent createCenterPanel() {
        panel = new JPanel();
        JBScrollPane scrollPane = new JBScrollPane(panel);
        panel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
//        scrollPane.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        return scrollPane;
    }

}
