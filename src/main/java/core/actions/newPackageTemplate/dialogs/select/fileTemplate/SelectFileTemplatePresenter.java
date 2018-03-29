package core.actions.newPackageTemplate.dialogs.select.fileTemplate;

import com.intellij.ide.fileTemplates.FileTemplate;
import global.models.TemplateForSearch;

import java.util.ArrayList;

/**
 * Created by Arsen on 16.09.2016.
 */
public interface SelectFileTemplatePresenter {

    void onSuccess(FileTemplate template);

    void onCancel();

}
