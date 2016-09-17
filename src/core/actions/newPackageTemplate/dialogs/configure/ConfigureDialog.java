package core.actions.newPackageTemplate.dialogs.configure;

import base.BaseDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.util.ui.GridBag;
import global.models.PackageTemplate;
import global.utils.GridBagFactory;
import global.wrappers.PackageTemplateWrapper;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Created by CeH9 on 22.06.2016.
 */

public abstract class ConfigureDialog extends BaseDialog implements ConfigureView {

    public abstract void onSuccess(PackageTemplate packageTemplate);
    public abstract void onFail();

    private ConfigurePresenter presenter;

    public ConfigureDialog(Project project) {
        super(project);
        presenter = new ConfigurePresenterImpl(project, this);
    }

    public ConfigureDialog(Project project, PackageTemplate packageTemplate) {
        super(project);
        presenter = new ConfigurePresenterImpl(project, packageTemplate, this);
    }

    @Override
    public void preShow() {
        presenter.onPreShow();
    }

    @Override
    public void buildView(PackageTemplateWrapper ptWrapper) {
        panel.setLayout(new GridBagLayout());
        GridBag gridBag = GridBagFactory.getBagForConfigureDialog();
        panel.add(ptWrapper.buildView(), gridBag.nextLine().next());
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        return presenter.doValidate();
    }

    @Override
    public void onOKAction() {
        presenter.onOKAction();
    }

    @Override
    public void onCancelAction() {
        presenter.onFail();
    }
}
