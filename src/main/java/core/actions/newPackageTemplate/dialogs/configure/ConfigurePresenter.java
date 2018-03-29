package core.actions.newPackageTemplate.dialogs.configure;

import com.intellij.openapi.ui.ValidationInfo;

/**
 * Created by Arsen on 16.09.2016.
 */
public interface ConfigurePresenter {
    void onPreShow();
    void onOKAction();
    void onFail();

    ValidationInfo doValidate();
}