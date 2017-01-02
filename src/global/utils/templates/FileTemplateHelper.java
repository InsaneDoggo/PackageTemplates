package global.utils.templates;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Arsen on 02.01.2017.
 */
public class FileTemplateHelper {

    @Nullable
    public static FileTemplate getTemplate(String name) {
        FileTemplateManager ftm = FileTemplateManager.getDefaultInstance();

        // Custom
        FileTemplate result = ftm.getTemplate(name);
        // Internal
        if (result == null) {
            result = ftm.getInternalTemplate(name);
        }
        // J2EE
        if (result == null) {
            result = ftm.getJ2eeTemplate(name);
        }

        return result;
    }

}
