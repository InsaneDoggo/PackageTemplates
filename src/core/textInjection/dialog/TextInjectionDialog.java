package core.textInjection.dialog;

import base.BaseDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.IconUtil;
import core.search.SearchAction;
import core.search.customPath.CustomPath;
import core.search.customPath.dialog.SearchActionWrapper;
import core.textInjection.TextInjection;
import global.listeners.ClickListener;
import global.utils.i18n.Localizer;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arsen on 19.07.2016.
 */
public abstract class TextInjectionDialog extends BaseDialog {

    private TextInjection textInjection;

    public TextInjectionDialog(Project project, TextInjection textInjection) {
        super(project);
        this.textInjection = textInjection;

        if (textInjection == null) {
            this.textInjection = new TextInjection();
        }
    }


    //=================================================================
    //  UI
    //=================================================================
    private JButton btnAdd;
    private JPanel actionsPanel;

    @Override
    public void preShow() {
        panel.setLayout(new MigLayout(new LC().gridGap("0", "8pt")));
    }


    //=================================================================
    //  Dialog specific stuff
    //=================================================================
    private static final int MIN_WIDTH = 720;
    private static final int MIN_HEIGHT = 240;

    public abstract void onSuccess(TextInjection textInjection);

    @Override
    public void onOKAction() {
        //todo collect
        onSuccess(textInjection);
    }

    @Override
    public void onCancelAction() {
    }

    @Override
    protected ValidationInfo doValidate() {
        return new ValidationInfo(Localizer.get("warning.CreateAtLeastOneAction"), actionsPanel);
    }

    @Override
    protected JComponent createCenterPanel() {
        panel = new JPanel();
        JBScrollPane scrollPane = new JBScrollPane(panel);
        scrollPane.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        return scrollPane;
    }

}
