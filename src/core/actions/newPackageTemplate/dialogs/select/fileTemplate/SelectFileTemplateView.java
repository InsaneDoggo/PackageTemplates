package core.actions.newPackageTemplate.dialogs.select.fileTemplate;

import com.intellij.ide.fileTemplates.FileTemplate;
import global.wrappers.PackageTemplateWrapper;

/**
 * Created by Arsen on 16.09.2016.
 */
public interface SelectFileTemplateView {
    void onSuccess(FileTemplate fileTemplate);
    void setTitle(String title);
    void onCancel();
}
