package utils;

import com.intellij.openapi.project.Project;
import models.PackageTemplate;
import reborn.models.Directory;
import reborn.models.GlobalVariable;
import reborn.wrappers.DirectoryWrapper;
import reborn.wrappers.GlobalVariableWrapper;
import reborn.wrappers.PackageTemplateWrapper;

import java.util.ArrayList;
import java.util.HashMap;

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

    public static PackageTemplateWrapper createAndWrapPackageTemplate(Project project, PackageTemplateWrapper.ViewMode mode) {
        Directory directory = new Directory();
        directory.setName("example");
        directory.setListBaseElement(new ArrayList<>());
        directory.setEnabled(true);
        directory.setGroovyCode("");

        PackageTemplate packageTemplate = new PackageTemplate();
        packageTemplate.setMapGlobalVars(new HashMap<>());
        packageTemplate.setListGlobalVariable(new ArrayList<>());
        packageTemplate.setName("New Package Template");
        packageTemplate.setDescription("");
        packageTemplate.setDirectory(directory);

        DirectoryWrapper dirWrapper = new DirectoryWrapper();
        dirWrapper.setListElementWrapper(new ArrayList<>());
//        dirWrapper.setPackageTemplateWrapper(ptWrapper);
        dirWrapper.setParent(null);
        dirWrapper.setDirectory(directory);

        GlobalVariable globalVariable = new GlobalVariable();
        globalVariable.setName(PackageTemplateWrapper.ATTRIBUTE_BASE_NAME);
        globalVariable.setValue("Example");
        globalVariable.setEnabled(true);
        globalVariable.setGroovyCode("");

        return wrapPackageTemplate(project, packageTemplate, mode);
    }

}
