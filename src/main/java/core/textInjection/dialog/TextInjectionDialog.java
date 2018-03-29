package core.textInjection.dialog;

import base.BaseDialog;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBScrollPane;
import core.regexp.RegexpHelperDialog;
import core.search.customPath.CustomPath;
import core.search.customPath.dialog.CustomPathDialog;
import core.textInjection.InjectDirection;
import core.textInjection.TextInjection;
import global.listeners.ClickListener;
import global.utils.UIHelper;
import global.utils.factories.GsonFactory;
import global.utils.i18n.Localizer;
import icons.PluginIcons;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.intellij.lang.regexp.RegExpFileType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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
    public ComboBox cmbDirection;
    public EditorTextField tfToSearch;
    public JBCheckBox cbRegexp;
    public EditorTextField tfToInject;

    @Override
    public void preShow() {
        setTitle(Localizer.get("title.TextInjection"));
        panel.setLayout(new MigLayout(new LC().gridGap("0", "8pt")));

        JLabel jlDescription = new JLabel(Localizer.get("Description"));
        tfDescription = new EditorTextField(textInjection.getDescription());

        if (textInjection.getCustomPath() == null) {
            btnCustomPath = new JButton(Localizer.get("AddCustomPath"), PluginIcons.CUSTOM_PATH_DISABLED);
        } else {
            btnCustomPath = new JButton(Localizer.get("EditCustomPath"), PluginIcons.CUSTOM_PATH);
        }
        btnCustomPath.addActionListener(e -> {
            CustomPath customPath = textInjection.getCustomPath() == null ? null : GsonFactory.cloneObject(textInjection.getCustomPath(), CustomPath.class);
            new CustomPathDialog(project, customPath, true) {
                @Override
                public void onSuccess(CustomPath customPath) {
                    textInjection.setCustomPath(customPath);
                    btnCustomPath.setIcon(PluginIcons.CUSTOM_PATH);
                }
            }.show();
        });

        //Type
        ArrayList<InjectDirection> directions = new ArrayList<>();
        directions.add(InjectDirection.BEFORE);
        directions.add(InjectDirection.AFTER);
        directions.add(InjectDirection.PREV_LINE);
        directions.add(InjectDirection.NEXT_LINE);
        directions.add(InjectDirection.REPLACE);
        directions.add(InjectDirection.START_OF_FILE);
        directions.add(InjectDirection.SOF_END_OF_LINE);
        directions.add(InjectDirection.END_OF_FILE);
        directions.add(InjectDirection.EOF_START_OF_LINE);

        JLabel jlInjectDirection = new JLabel(Localizer.get("label.InjectDirection"));
        cmbDirection = new ComboBox(directions.toArray());
        cmbDirection.setRenderer(new InjectDirectionCellRenderer());
        cmbDirection.setSelectedItem(textInjection.getInjectDirection());

        JLabel jlToSearch = new JLabel(Localizer.get("label.TextToSearch"));
        cbRegexp = new JBCheckBox(Localizer.get("label.IsRegExp"), textInjection.isRegexp());
        cbRegexp.addActionListener(e -> {
            AbstractButton abstractButton = (AbstractButton) e.getSource();
            if (abstractButton.getModel().isSelected()) {
                tfToSearch.setFileType(RegExpFileType.INSTANCE);
            } else {
                tfToSearch.setFileType(PlainTextFileType.INSTANCE);
            }
        });
        tfToSearch = new EditorTextField(textInjection.getTextToSearch(), project, PlainTextFileType.INSTANCE);

        JButton btnRegexpHelper = new JButton(Localizer.get("label.RegexpHelperDialog"));
        btnRegexpHelper.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegexpHelperDialog(project).show();
            }
        });

        JLabel jlToInject = new JLabel(Localizer.get("label.TextToInject"));
        tfToInject = UIHelper.getEditorTextField(textInjection.getTextToInject(), project, false);

        String topGapY = "10pt";

        panel.add(jlDescription, new CC().wrap());
        panel.add(tfDescription, new CC().pushX().growX().wrap());
        panel.add(btnCustomPath, new CC().wrap().gapTop(topGapY));

        panel.add(jlInjectDirection, new CC().wrap().gapTop(topGapY));
        panel.add(cmbDirection, new CC().wrap());

        panel.add(jlToSearch, new CC().wrap().gapTop(topGapY));
        panel.add(cbRegexp, new CC().spanX().split(2));
        panel.add(tfToSearch, new CC().wrap().pushX().growX());
        panel.add(btnRegexpHelper, new CC().wrap());

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
        collectDataFromFields();
        onSuccess(textInjection);
    }

    private void collectDataFromFields() {
        textInjection.setDescription(tfDescription.getText());
        textInjection.setInjectDirection((InjectDirection) cmbDirection.getSelectedItem());
        textInjection.setRegexp(cbRegexp.isSelected());
        textInjection.setTextToSearch(tfToSearch.getText());
        textInjection.setTextToInject(tfToInject.getText());
    }

    @Override
    public void onCancelAction() {
    }

    @Override
    protected ValidationInfo doValidate() {
        // description
        String description = tfDescription.getText();
        if (description == null || description.isEmpty()) {
            return new ValidationInfo(Localizer.get("warning.FillEmptyFields"), tfDescription);
        }

        if (textInjection.getCustomPath() == null) {
            return new ValidationInfo(Localizer.get("warning.ShouldCreateCustomPath"), panel);
        }

        // textToSearch
        String textToSearch = tfToSearch.getText();
        if (textToSearch == null || textToSearch.isEmpty()) {
            return new ValidationInfo(Localizer.get("warning.FillEmptyFields"), tfToSearch);
        }

        // textToInject
        String textToInject = tfToInject.getText();
        if (textToInject == null) {
            tfToInject.setText("");
        }

        return null;
    }

    @Override
    protected JComponent createCenterPanel() {
        panel = new JPanel();
        JBScrollPane scrollPane = new JBScrollPane(panel);
        scrollPane.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        return scrollPane;
    }

}
