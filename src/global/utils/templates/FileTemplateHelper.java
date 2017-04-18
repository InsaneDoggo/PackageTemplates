package global.utils.templates;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplatesScheme;
import com.intellij.openapi.options.SchemeState;
import com.intellij.openapi.project.Project;
import com.intellij.util.ArrayUtil;
import global.models.TemplateForSearch;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Arsen on 02.01.2017.
 */
public class FileTemplateHelper {

    private static FileTemplateManager getProjectManager(Project project) {
        return FileTemplateManager.getInstance(project);
    }

    private static FileTemplateManager getDefaultManager() {
        return FileTemplateManager.getDefaultInstance();
    }


    //=================================================================
    //  Read
    //=================================================================
    public static Properties getDefaultProperties(Project project) {
        return getProjectManager(project).getDefaultProperties();
    }

    @Nullable
    public static FileTemplate getTemplate(String name, Project project, FileTemplateSource fileTemplateSource) {
        FileTemplate result;
        FileTemplateManager projectManager = getProjectManager(project);
        FileTemplateManager defaultManager = getDefaultManager();

        switch (fileTemplateSource){
            case DEFAULT_ONLY:
                break;
            case PROJECT_ONLY:
                break;
            case PROJECT_PRIORITY:
                break;
            case DEFAULT_PRIORITY:
                break;
        }


        result = defaultManager.getTemplate(name);
        if (result != null) {
            return result;
        }

        // Custom (DEFAULT or PROJECT)
        result = projectManager.getTemplate(name);
        if (result != null) {
            return result;
        }

        // Internal
        result = projectManager.getInternalTemplate(name);
        if (result != null) {
            return result;
        }

        // J2EE
        result = projectManager.getJ2eeTemplate(name);
        if (result != null) {
            return result;
        }

        return null;
    }

    public ArrayList<TemplateForSearch> getTemplates(Project project, boolean addInternal, boolean addJ2EE) {
        FileTemplateManager ftm = FileTemplateManager.getDefaultInstance();
        FileTemplate[] fileTemplates = ftm.getAllTemplates();

        if (addInternal)
            fileTemplates = ArrayUtil.mergeArrays(fileTemplates, ftm.getInternalTemplates());
        if (addJ2EE)
            fileTemplates = ArrayUtil.mergeArrays(fileTemplates, ftm.getTemplates(FileTemplateManager.J2EE_TEMPLATES_CATEGORY));


        ArrayList<TemplateForSearch> listTemplateForSearch = new ArrayList(fileTemplates.length);
        for (FileTemplate template : fileTemplates) {
            listTemplateForSearch.add(new TemplateForSearch(template));
        }
        return listTemplateForSearch;
    }


    //=================================================================
    //  Edit
    //=================================================================

}
