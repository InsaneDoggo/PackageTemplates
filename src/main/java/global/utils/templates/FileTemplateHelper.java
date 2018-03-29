package global.utils.templates;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplatesScheme;
import com.intellij.ide.fileTemplates.impl.FileTemplateManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import global.models.TemplateForSearch;
import global.utils.i18n.Localizer;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Arsen on 02.01.2017.
 */
public class FileTemplateHelper {

    public static boolean isDefaultScheme(Project project) {
        return getProjectManager(project).getCurrentScheme() == FileTemplatesScheme.DEFAULT;
    }

    private static FileTemplateManager getProjectManager(Project project) {
        return FileTemplateManagerImpl.getInstance(project);
    }

    private static FileTemplateManager getDefaultManager() {
        return FileTemplateManager.getDefaultInstance();
    }

    public static String getFileTemplatesDirPath(Project project) {
        return getProjectManager(project).getCurrentScheme().getTemplatesDir() + File.separator;
    }

    public static String getFileTemplatesDefaultDirPath() {
        return FileTemplatesScheme.DEFAULT.getTemplatesDir() + File.separator;
    }


    private static FileTemplateManager fileTemplateManager;

    public static FileTemplateManager getManagerInstance(Project project) {
        if (fileTemplateManager == null) {
            fileTemplateManager = FileTemplateManagerImpl.getInstance(project);
        }

        return fileTemplateManager;
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
        ArrayList<FileTemplate> fileTemplates = new ArrayList<>();

        switch (fileTemplateSource) {
            case DEFAULT_ONLY:
                addIfNameUnique(fileTemplates, defaultManager.getAllTemplates());
                break;
            case PROJECT_ONLY:
                addIfNameUnique(fileTemplates, projectManager.getAllTemplates());
                break;
            case PROJECT_PRIORITY:
                addIfNameUnique(fileTemplates, projectManager.getAllTemplates());
                addIfNameUnique(fileTemplates, defaultManager.getAllTemplates());
                break;
            case DEFAULT_PRIORITY:
                addIfNameUnique(fileTemplates, defaultManager.getAllTemplates());
                addIfNameUnique(fileTemplates, projectManager.getAllTemplates());
                break;
        }


        if (addInternal) {
            addIfNameUnique(fileTemplates, projectManager.getInternalTemplates());
        }
        if (addJ2EE) {
            addIfNameUnique(fileTemplates, projectManager.getTemplates(FileTemplateManager.J2EE_TEMPLATES_CATEGORY));
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


    //=================================================================
    //  Utils
    //=================================================================
    private static void addIfNameUnique(ArrayList<FileTemplate> to, FileTemplate[] from) {
        for (FileTemplate item : from) {
            if (!contains(to, item)) {
                to.add(item);
            }
        }
    }

    private static boolean contains(ArrayList<FileTemplate> list, FileTemplate template) {
        for (FileTemplate item : list) {
            if (item.getName().equals(template.getName())) {
                return true;
            }
        }

        return false;
    }

    public static boolean isCurrentSchemeValid(Project project, FileTemplateSource fileTemplateSource) {
        switch (fileTemplateSource) {
            case PROJECT_ONLY:
            case PROJECT_PRIORITY:
            case DEFAULT_PRIORITY:
                if (FileTemplateHelper.isDefaultScheme(project)) {
                    Messages.showWarningDialog(project, Localizer.get("warning.SwitchToProjectScheme"), "Warning Dialog");
                    return false;
                }
        }

        return true;
    }

}
