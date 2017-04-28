package core.search.customPath.dialog;

import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.JBCheckBox;
import core.search.SearchAction;
import core.search.SearchActionType;
import core.search.SearchEngine;
import global.utils.Logger;
import global.utils.UIHelper;
import global.utils.i18n.Localizer;
import global.views.spinner.IntSpinner;
import com.intellij.openapi.ui.ComboBox;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.intellij.lang.regexp.RegExpFileType;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created by Arsen on 04.02.2017.
 */
public class SearchActionWrapper {

    private SearchAction action;
    private Project project;

    public SearchActionWrapper(Project project, SearchAction action) {
        this.action = action;
        this.project = project;

        if (action == null) {
            this.action = new SearchAction();
        }
    }


    //=================================================================
    //  Public API
    //=================================================================

    /**
     * Собирает инфу из полей
     */
    public void collectData() {
        action.setActionType((SearchActionType) cmbActionTypes.getSelectedItem());
        action.setName(tfName.getText());
        action.setRegexp(cbIsRegex.isSelected());

        //Deep Limit
        if (cbDeepLimit.isSelected()) {
            try {
                action.setDeepLimit((Integer) spnDeepLimit.getValue());
            } catch (Exception e) {
                Logger.log("collectData spnDeepLimit.getValue: " + e.getMessage());
                Logger.printStack(e);
                action.setDeepLimit(SearchEngine.DEEP_LIMITLESS);
            }
        } else {
            action.setDeepLimit(SearchEngine.DEEP_LIMITLESS);
        }
    }

    public ValidationInfo doValidate() {
        if (tfName.getText().trim().isEmpty()) {
            return new ValidationInfo(Localizer.get("warning.FillEmptyFields"), tfName);
        }

        if (cbDeepLimit.isSelected()) {
            try {
                int testValue = (Integer) spnDeepLimit.getValue();
            } catch (Exception e) {
                Logger.log("collectData spnDeepLimit.getValue: " + e.getMessage());
                Logger.printStack(e);
                return new ValidationInfo(Localizer.get("warning.InvalidNumber"), tfName);
            }
        }

        return null;
    }


    //=================================================================
    //  UI
    //=================================================================
    public JPanel panel;
    public ComboBox cmbActionTypes;
    public EditorTextField tfName;
    public JBCheckBox cbDeepLimit;
    public IntSpinner spnDeepLimit;
    public JBCheckBox cbIsRegex;

    private final int DEFAULT_DEEP_LIMIT = 1;
    private final int MIN_DEEP_LIMIT = 0;
    private final int MAX_DEEP_LIMIT = Integer.MAX_VALUE;

    public JPanel buildView() {
        panel = new JPanel(new MigLayout(new LC().fillX()));

        //Type
        ArrayList<SearchActionType> actionTypes = new ArrayList<>();
        actionTypes.add(SearchActionType.DIR_ABOVE);
        actionTypes.add(SearchActionType.DIR_BELOW);
        actionTypes.add(SearchActionType.DIR_PARENT);
        actionTypes.add(SearchActionType.FILE);

        cmbActionTypes = new ComboBox(actionTypes.toArray());
        cmbActionTypes.setRenderer(new SearchActionTypeCellRenderer());
        cmbActionTypes.setSelectedItem(action.getActionType());
        cmbActionTypes.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                action.setActionType((SearchActionType) cmbActionTypes.getSelectedItem());
            }
        });

        // Name
        PsiFile psiFile = PsiFileFactory.getInstance(project).createFileFromText("Name", PlainTextLanguage.INSTANCE, action.getName());
        tfName = new EditorTextField(PsiDocumentManager.getInstance(project).getDocument(psiFile), project, PlainTextFileType.INSTANCE);
        UIHelper.addHighlightListener(project, tfName);


        //DeepLimit
        cbDeepLimit = new JBCheckBox(Localizer.get("label.DeepLimit"), action.getDeepLimit() != SearchEngine.DEEP_LIMITLESS);
        spnDeepLimit = new IntSpinner(DEFAULT_DEEP_LIMIT, MIN_DEEP_LIMIT, MAX_DEEP_LIMIT);
        spnDeepLimit.setEnabled(cbDeepLimit.isSelected());
        spnDeepLimit.setValue(action.getDeepLimit() >= 0 ? action.getDeepLimit() : 0);

        cbDeepLimit.addActionListener(e -> {
            AbstractButton abstractButton = (AbstractButton) e.getSource();
            if (abstractButton.getModel().isSelected()) {
                spnDeepLimit.setEnabled(true);
            } else {
                spnDeepLimit.setEnabled(false);
            }
        });

        //RegExp
        cbIsRegex = new JBCheckBox(Localizer.get("label.IsRegExp"), action.isRegexp());
        cbIsRegex.addActionListener(e -> {
            AbstractButton abstractButton = (AbstractButton) e.getSource();
            if (abstractButton.getModel().isSelected()) {
                tfName.setFileType(RegExpFileType.INSTANCE);
            } else {
                tfName.setFileType(PlainTextFileType.INSTANCE);
            }
        });

        panel.add(cmbActionTypes, new CC());
        panel.add(tfName, new CC().pushX().growX());
        panel.add(cbDeepLimit, new CC());
        panel.add(spnDeepLimit, new CC());
        panel.add(cbIsRegex, new CC());

        return panel;
    }


    //=================================================================
    //  Getter | Setter
    //=================================================================
    public SearchAction getAction() {
        return action;
    }

}
