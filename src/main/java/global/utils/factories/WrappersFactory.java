package global.utils.factories;

import com.intellij.openapi.project.Project;
import core.textInjection.TextInjection;
import core.textInjection.dialog.TextInjectionWrapper;
import core.writeRules.WriteRules;
import global.models.*;
import global.wrappers.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by CeH9 on 14.07.2016.
 */
public class WrappersFactory {

    //=================================================================
    //  Create and Wrap
    //=================================================================
    @NotNull
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


    //=================================================================
    //  Wrap existing
    //=================================================================
    @NotNull
    public static TextInjectionWrapper wrapTextInjection(TextInjection textInjection) {
        return new TextInjectionWrapper(textInjection);
    }

    @NotNull
    public static PackageTemplateWrapper wrapPackageTemplate(Project project, PackageTemplate packageTemplate, PackageTemplateWrapper.ViewMode mode) {
        PackageTemplateWrapper ptWrapper = new PackageTemplateWrapper(project);
        ptWrapper.setMode(mode);
        ptWrapper.setPackageTemplate(packageTemplate);
        ptWrapper.initCollections();
        ptWrapper.setRootElement(wrapDirectory(packageTemplate.getDirectory(), null, ptWrapper));
        ptWrapper.setProject(project);

        return ptWrapper;
    }

    @NotNull
    private static DirectoryWrapper wrapDirectory(Directory directory, DirectoryWrapper parent, PackageTemplateWrapper ptWrapper) {
        DirectoryWrapper result = new DirectoryWrapper();
        result.setDirectory(directory);
        result.setParent(parent);
        result.setPackageTemplateWrapper(ptWrapper);

        ArrayList<ElementWrapper> list = new ArrayList<>();

        for (BaseElement baseElement : directory.getListBaseElement()) {
            if (baseElement.isDirectory()) {
                list.add(wrapDirectory(((Directory) baseElement), result, ptWrapper));
            } else {
                if (baseElement instanceof File) {
                     list.add(WrappersFactory.wrapFile(result, (File) baseElement));
                } else {
                     list.add(WrappersFactory.wrapBinaryFile(result, (BinaryFile) baseElement));
                }
            }
        }

        result.setListElementWrapper(list);
        return result;
    }

    @NotNull
    private static FileWrapper wrapFile(DirectoryWrapper parent, File file) {
        FileWrapper wrapper = new FileWrapper();

        wrapper.setParent(parent);
        wrapper.setFile(file);
        wrapper.setPackageTemplateWrapper(parent.getPackageTemplateWrapper());

        return wrapper;
    }

    @NotNull
    public static BinaryFileWrapper wrapBinaryFile(DirectoryWrapper parent, BinaryFile binaryFile) {
        BinaryFileWrapper wrapper = new BinaryFileWrapper();

        wrapper.setParent(parent);
        wrapper.setBinaryFile(binaryFile);
        wrapper.setPackageTemplateWrapper(parent.getPackageTemplateWrapper());

        return wrapper;
    }

}
