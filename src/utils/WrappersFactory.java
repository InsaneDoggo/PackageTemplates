package utils;

import com.intellij.openapi.project.Project;
import models.PackageTemplate;
import reborn.wrappers.PackageTemplateWrapper;

/**
 * Created by CeH9 on 14.07.2016.
 */
public class WrappersFactory {

    public static PackageTemplateWrapper wrapPackageTemplate(Project project, PackageTemplate packageTemplate, PackageTemplateWrapper.ViewMode mode){
        PackageTemplateWrapper ptWrapper = new PackageTemplateWrapper(project);
        ptWrapper.setMode(mode);
        ptWrapper.setPackageTemplate(packageTemplate);
        ptWrapper.wrapGlobals();
        ptWrapper.setRootElement(ptWrapper.wrapDirectory(packageTemplate.getDirectory(), null));
        ptWrapper.setProject(project);

        return ptWrapper;
    }

}
