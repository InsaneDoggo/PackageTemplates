package core.actions.newPackageTemplate.dialogs.select.binaryFile;

import com.intellij.ide.fileTemplates.FileTemplate;

/**
 * Created by Arsen on 16.09.2016.
 */
public interface SelectBinaryFileView {
    void onSuccess(FileTemplate fileTemplate);
    void setTitle(String title);
    void onCancel();
}
