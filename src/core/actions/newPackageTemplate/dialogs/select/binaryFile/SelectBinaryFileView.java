package core.actions.newPackageTemplate.dialogs.select.binaryFile;

import global.models.BinaryFile;

/**
 * Created by Arsen on 16.09.2016.
 */
public interface SelectBinaryFileView {
    void onSuccess(BinaryFile binaryFile);
    void setTitle(String title);
    void onCancel();
}
