package core.search.customPath.dialog;

import base.BaseDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.IconUtil;
import core.actions.newPackageTemplate.dialogs.select.packageTemplate.SelectPackageTemplateDialog;
import core.regexp.RegexpHelperDialog;
import core.search.SearchAction;
import core.search.SearchActionType;
import core.search.customPath.CustomPath;
import global.listeners.ClickListener;
import global.utils.Logger;
import global.utils.i18n.Localizer;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arsen on 19.07.2016.
 */
public abstract class CustomPathDialog extends BaseDialog {

    private CustomPath customPath;
    private boolean returnFile = false;

    public CustomPathDialog(Project project, CustomPath customPath) {
        this(project, customPath, false);
    }

    public CustomPathDialog(Project project, CustomPath customPath, boolean returnFile) {
        super(project);
        this.returnFile = returnFile;
        this.customPath = customPath;

        if (customPath == null) {
            this.customPath = new CustomPath(new ArrayList<>());
        }

        if (this.customPath.getListSearchAction().isEmpty()) {
            this.customPath.getListSearchAction().add(new SearchAction());
        }
    }


    //=================================================================
    //  UI
    //=================================================================
    private JButton btnAdd;
    private JButton btnRegexpHelper;
    private List<SearchActionWrapper> wrappers;
    private JPanel actionsPanel;

    @Override
    public void preShow() {
        panel.setLayout(new MigLayout(new LC().gridGap("0", "8pt")));

        actionsPanel = new JPanel(new MigLayout(new LC().fillX().insetsAll("0").gridGap("0", "0")));
        wrappers = new ArrayList<>();

        for (SearchAction searchAction : customPath.getListSearchAction()) {
            createSearchAction(searchAction);
        }

        btnAdd = getAddButton();
        btnRegexpHelper = getRegexpButton();

        panel.add(actionsPanel, new CC().pushX().grow().spanX().wrap());
        panel.add(btnAdd, new CC().spanX().split(2));
        panel.add(btnRegexpHelper, new CC().wrap());
    }

    private JButton getRegexpButton() {
        JButton btnRegexpHelper = new JButton(Localizer.get("label.RegexpHelperDialog"));
        btnRegexpHelper.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegexpHelperDialog(project).show();
            }
        });
        return btnRegexpHelper;
    }

    private JButton getAddButton() {
        JButton btnAdd = new JButton(Localizer.get("action.Add"), IconUtil.getAddIcon());
        btnAdd.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                createSearchAction(null);
            }
        });
        return btnAdd;
    }

    private void createSearchAction(SearchAction searchAction) {
        SearchActionWrapper wrapper = new SearchActionWrapper(project, searchAction);

        wrappers.add(wrapper);
        // check is from "add" button
        if (searchAction == null) {
            customPath.getListSearchAction().add(wrapper.getAction());
        }

        JPanel wrapperView = wrapper.buildView();
        JButton btnDelete = getDeleteButton(wrapperView, wrapper);

        actionsPanel.add(btnDelete, new CC());
        actionsPanel.add(wrapperView, new CC().pushX().grow().spanX().wrap());
    }

    @NotNull
    private JButton getDeleteButton(final JPanel view, SearchActionWrapper wrapper) {
        JButton btnDelete = new JButton(IconUtil.getRemoveIcon());

        btnDelete.addMouseListener(new ClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                actionsPanel.remove(btnDelete);
                actionsPanel.remove(view);
                customPath.getListSearchAction().remove(wrapper.getAction());
                wrappers.remove(wrapper);
                actionsPanel.revalidate();
            }
        });
        return btnDelete;
    }


    //=================================================================
    //  Dialog specific stuff
    //=================================================================
    private static final int MIN_WIDTH = 720;
    private static final int MIN_HEIGHT = 240;

    public abstract void onSuccess(CustomPath customPath);

    @Override
    public void onOKAction() {
        for (SearchActionWrapper wrapper : wrappers) {
            wrapper.collectData();
        }
        onSuccess(customPath);
    }

    @Override
    public void onCancelAction() {
    }

    @Override
    protected ValidationInfo doValidate() {
        if (wrappers.isEmpty()) {
            return new ValidationInfo(Localizer.get("warning.CreateAtLeastOneAction"), actionsPanel);
        }

        for (SearchActionWrapper wrapper : wrappers) {
            ValidationInfo validationInfo = wrapper.doValidate();
            if (validationInfo != null) {
                return validationInfo;
            }
        }

        SearchActionType lastActionType = wrappers.get(wrappers.size() - 1).getAction().getActionType();
        if (returnFile) {
            switch (lastActionType) {
                case DIR_ABOVE:
                case DIR_BELOW:
                    return new ValidationInfo(Localizer.get("warning.LastActionShouldSearchFile"), actionsPanel);
            }
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
