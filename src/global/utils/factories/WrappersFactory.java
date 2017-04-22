package global.utils.factories;

import com.intellij.openapi.project.Project;
import core.textInjection.TextInjection;
import core.textInjection.dialog.TextInjectionWrapper;
import core.writeRules.WriteRules;
import global.models.*;
import global.utils.templates.FileTemplateSource;
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

    public static TextInjectionWrapper wrapTextInjection(TextInjection textInjection) {
        return new TextInjectionWrapper(textInjection);
    }

    public static PackageTemplateWrapper wrapPackageTemplate(Project project, PackageTemplate packageTemplate, PackageTemplateWrapper.ViewMode mode) {
        PackageTemplateWrapper ptWrapper = new PackageTemplateWrapper(project);
        ptWrapper.setMode(mode);
        ptWrapper.setPackageTemplate(packageTemplate);
        ptWrapper.initCollections();
        ptWrapper.setRootElement(ptWrapper.wrapDirectory(packageTemplate.getDirectory(), null));
        ptWrapper.setProject(project);

        return ptWrapper;
    }

    public static PackageTemplateWrapper createAndWrapPackageTemplate(Project project, PackageTemplateWrapper.ViewMode mode) {
        Directory directory = Directory.newInstance();
        directory.setWriteRules(WriteRules.OVERWRITE);
        directory.setName(PackageTemplateWrapper.PATTERN_BASE_NAME);

        PackageTemplate packageTemplate = PackageTemplate.newInstance();
        packageTemplate.setDirectory(directory);

        GlobalVariable globalVariable = GlobalVariable.newInstance();

        packageTemplate.getListGlobalVariable().add(globalVariable);
        return wrapPackageTemplate(project, packageTemplate, mode);
    }

    @NotNull
    public static DirectoryWrapper createNewWrappedDirectory(DirectoryWrapper parent) {
        Directory directory = Directory.newInstance();

        DirectoryWrapper dirWrapper = new DirectoryWrapper();
        dirWrapper.setParent(parent);
        dirWrapper.setDirectory(directory);
        dirWrapper.setListElementWrapper(new ArrayList<>());
        dirWrapper.setPackageTemplateWrapper(parent.getPackageTemplateWrapper());

        return dirWrapper;
    }

    @NotNull
    public static FileWrapper createNewWrappedFile(DirectoryWrapper parent, String templateName, String extension) {
        FileWrapper fileWrapper = new FileWrapper();

        File file = File.newInstance(templateName, extension);

        fileWrapper.setParent(parent);
        fileWrapper.setFile(file);
        fileWrapper.setPackageTemplateWrapper(parent.getPackageTemplateWrapper());

        return fileWrapper;
    }

}
