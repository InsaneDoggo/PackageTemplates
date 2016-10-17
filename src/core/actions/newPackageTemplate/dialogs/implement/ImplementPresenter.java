package core.actions.newPackageTemplate.dialogs.implement;

import com.intellij.openapi.ui.ValidationInfo;

/**
 * Created by Arsen on 11.09.2016.
 */
public interface ImplementPresenter {
    void onPreShow();
    void onOKAction();
    void onCancelAction();
    ValidationInfo doValidate();
}
