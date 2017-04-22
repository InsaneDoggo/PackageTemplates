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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;

/**
 * Created by Arsen on 02.01.2017.
 */
public class FileTemplateHelper {

    public static boolean isDefaultScheme(Project project) {
        return getProjectManager(project).getCurrentScheme() == FileTemplatesScheme.DEFAULT;
    }

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
        FileTemplate result = null;
        FileTemplateManager projectManager = getProjectManager(project);
        FileTemplateManager defaultManager = getDefaultManager();

        switch (fileTemplateSource) {
            case DEFAULT_ONLY:
                result = getDefaultTemplateOnly(name, projectManager, defaultManager);
                break;
            case PROJECT_ONLY:
                result = getProjectTemplateOnly(name, projectManager, defaultManager);
                break;
            case PROJECT_PRIORITY:
                result = getProjectTemplatePriority(name, projectManager, defaultManager);
                break;
            case DEFAULT_PRIORITY:
                result = getDefaultTemplatePriority(name, projectManager, defaultManager);
                break;
        }
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

        return result;
    }

    private static FileTemplate getDefaultTemplateOnly(String name, FileTemplateManager projectManager, FileTemplateManager defaultManager) {
        FileTemplate result;

        result = defaultManager.getTemplate(name);
        if (result != null) {
            return result;
        }

        return null;
    }

    private static FileTemplate getProjectTemplateOnly(String name, FileTemplateManager projectManager, FileTemplateManager defaultManager) {
        FileTemplate result;

        result = projectManager.getTemplate(name);
        if (result != null) {
            return result;
        }

        return null;
    }

    private static FileTemplate getProjectTemplatePriority(String name, FileTemplateManager projectManager, FileTemplateManager defaultManager) {
        FileTemplate result;

        // Project
        result = getProjectTemplateOnly(name, projectManager, defaultManager);
        if (result != null) {
            return result;
        }

        // Default
        result = getDefaultTemplateOnly(name, projectManager, defaultManager);
        if (result != null) {
            return result;
        }

        return null;
    }

    private static FileTemplate getDefaultTemplatePriority(String name, FileTemplateManager projectManager, FileTemplateManager defaultManager) {
        FileTemplate result;

        // Default
        result = getDefaultTemplateOnly(name, projectManager, defaultManager);
        if (result != null) {
            return result;
        }

        // Project
        result = getProjectTemplateOnly(name, projectManager, defaultManager);
        if (result != null) {
            return result;
        }

        return null;
    }

    public static ArrayList<TemplateForSearch> getTemplates(Project project, boolean addInternal, boolean addJ2EE, FileTemplateSource fileTemplateSource) {
        FileTemplateManager projectManager = getProjectManager(project);
        FileTemplateManager defaultManager = getDefaultManager();
        HashSet<FileTemplate> fileTemplates = new HashSet<>();

        switch (fileTemplateSource) {
            case DEFAULT_ONLY:
                fileTemplates.addAll(Arrays.asList(defaultManager.getAllTemplates()));
                break;
            case PROJECT_ONLY:
                fileTemplates.addAll(Arrays.asList(projectManager.getAllTemplates()));
                break;
            case PROJECT_PRIORITY:
                fileTemplates.addAll(Arrays.asList(projectManager.getAllTemplates()));
                fileTemplates.addAll(Arrays.asList(defaultManager.getAllTemplates()));
                break;
            case DEFAULT_PRIORITY:
                fileTemplates.addAll(Arrays.asList(defaultManager.getAllTemplates()));
                fileTemplates.addAll(Arrays.asList(projectManager.getAllTemplates()));
                break;
        }


        if (addInternal) {
            fileTemplates.addAll(Arrays.asList(projectManager.getInternalTemplates()));
        }
        if (addJ2EE) {
            fileTemplates.addAll(Arrays.asList(projectManager.getTemplates(FileTemplateManager.J2EE_TEMPLATES_CATEGORY)));
        }


        ArrayList<TemplateForSearch> listTemplateForSearch = new ArrayList(fileTemplates.size());
        for (FileTemplate template : fileTemplates) {
            listTemplateForSearch.add(new TemplateForSearch(template));
        }
        return listTemplateForSearch;
    }


    //=================================================================
    //  Edit
    //=================================================================

}
