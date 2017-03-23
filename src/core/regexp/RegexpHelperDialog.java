package core.regexp;

import base.BaseDialog;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBScrollPane;
import core.report.ReportHelper;
import core.report.enums.ExecutionState;
import core.report.models.FailedActionReport;
import global.listeners.ClickListener;
import global.utils.Logger;
import global.utils.i18n.Localizer;
import global.utils.text.RegexHelper;
import global.utils.text.StringTools;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.intellij.lang.regexp.RegExpFileType;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.regex.MatchResult;

/**
 * Created by Arsen on 19.07.2016.
 */
public class RegexpHelperDialog extends BaseDialog {

    public RegexpHelperDialog(@Nullable Project project) {
        super(project);
    }


    //=================================================================
    //  UI
    //=================================================================
    private EditorTextField tfPattern;
    private EditorTextField tfText;
    private JLabel jlResult;
    private JButton btnTry;

    @Override
    public void preShow() {
        panel.setLayout(new MigLayout(new LC().gridGap("0", "8pt")));

        JLabel jlPattern = new JLabel(Localizer.get("label.RegexpPattern"));

        tfPattern = new EditorTextField(".*My[\\w]+\\.html.*", project, RegExpFileType.INSTANCE);
        tfPattern.setOneLineMode(false);

        JLabel jlText = new JLabel(Localizer.get("label.Text"));
        tfText = new EditorTextField("Bla bla bla\nMyUniqueTextFragment.html\nBla bla bla");
        tfText.setOneLineMode(false);

        JPanel secondPanel = new JPanel(new MigLayout(new LC().insets("0").gridGap("0", "4pt")));
        secondPanel.add(jlText, new CC().wrap());
        secondPanel.add(tfText, new CC().wrap().push().grow());

        JBSplitter splitter = new JBSplitter(true, 0.35f);
        splitter.setFirstComponent(tfPattern);
        splitter.setSecondComponent(secondPanel);
        splitter.setShowDividerControls(true);
        splitter.setHonorComponentsMinimumSize(true);

        btnTry = new JButton(Localizer.get("TryIt"));
        btnTry.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                doMatch();
            }
        });
        jlResult = new JLabel(Localizer.get("ResultWillBeHere"));

        panel.add(jlPattern, new CC().wrap().spanX());
        panel.add(splitter, new CC().wrap().push().grow().spanX());
        panel.add(btnTry, new CC().spanX().split(2));
        panel.add(jlResult, new CC().wrap().pushX().growX());
    }

    private void doMatch() {
        String pattern = tfPattern.getText();
        String text = tfText.getText();

        int matchCount = RegexHelper.matchCount(text, pattern);
        // Too much Matches
        if (matchCount > 1) {
            jlResult.setText(Localizer.get("error.MoreThenOneMatch"));
            jlResult.setIcon(AllIcons.RunConfigurations.TestFailed);
            return;
        }
        // No Matches
        if (matchCount <= 0) {
            jlResult.setText(Localizer.get("error.NoMatches"));
            jlResult.setIcon(AllIcons.RunConfigurations.TestFailed);
            return;
        }

        MatchResult matchResult = RegexHelper.match(text, pattern);

        jlResult.setText(String.format(Localizer.get("regexp.MatchSuccess"), matchResult.group()));
        jlResult.setIcon(AllIcons.RunConfigurations.TestPassed);
    }


    //=================================================================
    //  Dialog specific stuff
    //=================================================================
    private static final int MIN_WIDTH = 400;
    private static final int MIN_HEIGHT = 280;

//    public abstract void onSuccess(TextInjection textInjection);

    @Override
    public void onCancelAction() {
    }

    @Override
    protected JComponent createCenterPanel() {
        setModal(false);
        panel = new JPanel();
        JBScrollPane scrollPane = new JBScrollPane(panel);
        scrollPane.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        return scrollPane;
    }

}
