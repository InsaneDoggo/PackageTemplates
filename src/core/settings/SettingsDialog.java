package core.settings;

import base.BaseDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.GridBag;
import core.actions.newPackageTemplate.dialogs.configure.ConfigurePresenter;
import core.actions.newPackageTemplate.dialogs.configure.ConfigurePresenterImpl;
import core.actions.newPackageTemplate.dialogs.configure.ConfigureView;
import global.models.PackageTemplate;
import global.utils.GridBagFactory;
import global.utils.Localizer;
import global.wrappers.PackageTemplateWrapper;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * Created by CeH9 on 22.06.2016.
 */

public class SettingsDialog extends BaseDialog implements SettingsView {

    private SettingsPresenter presenter;

    public SettingsDialog(Project project) {
        super(project);
        presenter = new SettingsPresenterImpl(this);
    }


    @Override
    public void preShow() {
        presenter.onPreShow();
    }

    @Override
    public void buildView() {
        panel.setLayout(new MigLayout());

        JBLabel label = new JBLabel("SETTINGS YEAH!!!");

        panel.add(label, new CC().wrap().spanX());
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        Action cancelAction = getCancelAction();
        cancelAction.putValue(Action.NAME, Localizer.get("action.Done"));
        return new Action[]{cancelAction};
    }
}
