package core.export;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplatesScheme;
import global.wrappers.PackageTemplateWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Created by Arsen on 30.10.2016.
 */
public class ExportHelper {

    private static final String DIR_USER = "";
    private static final String DIR_INTERNAL = "internal";
    private static final String DIR_J2EE = "j2ee";

    @NotNull
    public static String getFileTemplatesDirPath() {
        return FileTemplatesScheme.DEFAULT.getTemplatesDir() + File.separator;
    }

    @Nullable
    public static void exportPackageTemplate(PackageTemplateWrapper ptWrapper) {

    }

    public static File getTemplate(String fileTemplateName) {
        FileTemplateManager ftm = FileTemplateManager.getDefaultInstance();

        // Custom
        FileTemplate result = ftm.getTemplate(fileTemplateName);
        // Internal
        if (result == null) {
            result = ftm.getInternalTemplate(fileTemplateName);
        }
        // J2EE
        if (result == null) {
            result = ftm.getJ2eeTemplate(fileTemplateName);
        }

    }

    private static String getRelativePath(String templateType) {

    }

}
