package global.utils;

import com.intellij.openapi.project.Project;
import global.models.*;
import org.jetbrains.annotations.NotNull;
import global.wrappers.DirectoryWrapper;
import global.wrappers.FileWrapper;
import global.wrappers.PackageTemplateWrapper;

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
        directory.setWriteBehavior(WriteBehavior.CREATE_IF_NOT_EXIST);
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
        packageTemplate.setSkipRootDirectory(false);

        GlobalVariable globalVariable = new GlobalVariable();
        globalVariable.setName(PackageTemplateWrapper.ATTRIBUTE_BASE_NAME);
        globalVariable.setValue("Example");
        globalVariable.setEnabled(true);
        globalVariable.setGroovyCode("");

        packageTemplate.getListGlobalVariable().add(globalVariable);

        return wrapPackageTemplate(project, packageTemplate, mode);
    }

    @NotNull
    public static DirectoryWrapper createNewWrappedDirectory(DirectoryWrapper parent) {
        Directory dir = new Directory();
        dir.setWriteBehavior(WriteBehavior.FROM_PARENT);
        dir.setName("unnamed");
        dir.setEnabled(true);
        dir.setGroovyCode("");
        dir.setListBaseElement(new ArrayList<>());

        DirectoryWrapper dirWrapper = new DirectoryWrapper();
        dirWrapper.setParent(parent);
        dirWrapper.setDirectory(dir);
        dirWrapper.setListElementWrapper(new ArrayList<>());
        dirWrapper.setPackageTemplateWrapper(parent.getPackageTemplateWrapper());

        return dirWrapper;
    }

    @NotNull
    public static FileWrapper createNewWrappedFile(DirectoryWrapper parent, String templateName, String extension) {
        FileWrapper fileWrapper = new FileWrapper();

        File file = new File();
        file.setWriteBehavior(WriteBehavior.FROM_PARENT);
        file.setName("Unnamed");
        file.setTemplateName(templateName);
        file.setExtension(extension);
        file.setEnabled(true);
        file.setGroovyCode("");
        file.setMapProperties(new HashMap<>());

        fileWrapper.setParent(parent);
        fileWrapper.setFile(file);
        fileWrapper.setPackageTemplateWrapper(parent.getPackageTemplateWrapper());

        return fileWrapper;
    }

}
