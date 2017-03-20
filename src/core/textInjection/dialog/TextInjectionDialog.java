package core.textInjection.dialog;

import base.BaseDialog;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx;
import com.intellij.openapi.fileTypes.impl.FileTypeRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.IconUtil;
import core.search.SearchAction;
import core.search.SearchActionType;
import core.search.customPath.CustomPath;
import core.search.customPath.dialog.SearchActionTypeCellRenderer;
import core.search.customPath.dialog.SearchActionWrapper;
import core.textInjection.InjectDirection;
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
    public EditorTextField tfDescription;
    public JButton btnCustomPath;
    public ComboBox<InjectDirection> cmbDirection;
    public EditorTextField tfToSearch;
    public JBCheckBox cbRegexp;
    public EditorTextField tfToInject;

    @Override
    public void preShow() {
        panel.setLayout(new MigLayout(new LC().gridGap("0", "8pt")));

        JLabel jlDescription = new JLabel(Localizer.get("Description"));
        tfDescription = new EditorTextField(textInjection.getDescription());

        if (textInjection.getCustomPath() == null) {
            btnCustomPath = new JButton(Localizer.get("AddCustomPath"));
        } else {
            btnCustomPath = new JButton(Localizer.get("EditCustomPath"));
        }

        //Type
        ArrayList<InjectDirection> directions = new ArrayList<>();
        directions.add(InjectDirection.BEFORE);
        directions.add(InjectDirection.AFTER);
        directions.add(InjectDirection.PREV_LINE);
        directions.add(InjectDirection.NEXT_LINE);
        directions.add(InjectDirection.REPLACE);
        directions.add(InjectDirection.START_OF_FILE);
        directions.add(InjectDirection.END_OF_FILE);

        JLabel jlInjectDirection = new JLabel(Localizer.get("label.InjectDirection"));
        cmbDirection = new ComboBox(directions.toArray());
        cmbDirection.setRenderer(new InjectDirectionCellRenderer());
        cmbDirection.setSelectedItem(textInjection.getInjectDirection());

        JLabel jlToSearch = new JLabel(Localizer.get("label.TextToSearch"));
        cbRegexp = new JBCheckBox(Localizer.get("label.IsRegExp"), textInjection.isRegexp());
        tfToSearch = new EditorTextField(textInjection.getTextToSearch());

        JLabel jlToInject = new JLabel(Localizer.get("label.TextToInject"));
        tfToInject = new EditorTextField(textInjection.getTextToInject());
        tfToInject.setOneLineMode(false);
        tfToInject.setFileType(PlainTextFileType.INSTANCE);

        String topGapY = "10pt";

        panel.add(jlDescription, new CC().wrap());
        panel.add(tfDescription, new CC().pushX().growX().wrap());
        panel.add(btnCustomPath, new CC().wrap().gapTop(topGapY));

        panel.add(jlInjectDirection, new CC().wrap().gapTop(topGapY));
        panel.add(cmbDirection, new CC().wrap());

        panel.add(jlToSearch, new CC().wrap().gapTop(topGapY));
        panel.add(cbRegexp, new CC().spanX().split(2));
        panel.add(tfToSearch, new CC().wrap().pushX().growX());

        panel.add(jlToInject, new CC().wrap().gapTop(topGapY));
        panel.add(tfToInject, new CC().wrap().push().grow());
    }


    //=================================================================
    //  Dialog specific stuff
    //=================================================================
    private static final int MIN_WIDTH = 720;
    private static final int MIN_HEIGHT = 480;

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
        return new ValidationInfo(Localizer.get("warning.CreateAtLeastOneAction"), panel);
    }

    @Override
    protected JComponent createCenterPanel() {
        panel = new JPanel();
        JBScrollPane scrollPane = new JBScrollPane(panel);
        scrollPane.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        return scrollPane;
    }

}
