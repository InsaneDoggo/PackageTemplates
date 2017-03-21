package core.writeRules.dialog;

import base.BaseDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBScrollPane;
import core.writeRules.WriteRules;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Arsen on 19.07.2016.
 */
public abstract class WriteRulesDialog extends BaseDialog {

    private WriteRules writeRules;
    // is Root element?
    private boolean hasParent;

    public WriteRulesDialog(Project project, WriteRules writeRules, boolean hasParent) {
        super(project);
        this.writeRules = writeRules;
        this.hasParent = hasParent;

        if (writeRules == null) {
            this.writeRules = WriteRules.getDefault();
        }
    }


    //=================================================================
    //  UI
    //=================================================================
    private ComboBox comboBoxRules;

    @Override
    public void preShow() {
        panel.setLayout(new MigLayout(new LC().gridGap("0", "8pt")));

        //Type
        ArrayList<WriteRules> actionTypes = new ArrayList<>();
        if(hasParent){
            actionTypes.add(WriteRules.FROM_PARENT);
        }
        actionTypes.add(WriteRules.USE_EXISTING);
        actionTypes.add(WriteRules.ASK_ME);
        actionTypes.add(WriteRules.OVERWRITE);

        comboBoxRules = new ComboBox(actionTypes.toArray());
        comboBoxRules.setRenderer(new WriteRulesCellRenderer());
        comboBoxRules.setSelectedItem(writeRules);

        panel.add(comboBoxRules, new CC().pushX().growX().spanX().gapY("10","10"));
    }


    //=================================================================
    //  Dialog specific stuff
    //=================================================================
    private static final int MIN_WIDTH = 200;
    private static final int MIN_HEIGHT = 20;

    public abstract void onSuccess(WriteRules writeRules);

    @Override
    public void onOKAction() {
        writeRules = (WriteRules) comboBoxRules.getSelectedItem();
        onSuccess(writeRules);
    }

    @Override
    public void onCancelAction() {
    }

    @Override
    protected ValidationInfo doValidate() {

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
