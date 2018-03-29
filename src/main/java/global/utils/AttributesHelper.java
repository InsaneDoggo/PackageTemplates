package global.utils;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.project.Project;
import global.models.GlobalVariable;
import global.utils.templates.FileTemplateHelper;
import global.wrappers.PackageTemplateWrapper;
import org.apache.velocity.runtime.parser.ParseException;

import java.util.*;

/**
 * Created by CeH9 on 15.07.2016.
 */
public class AttributesHelper {

    private static HashSet<String> listDefaultAttributeNames;

    public static HashSet<String> getDefaultAttributeNames(Project project) {
        if (listDefaultAttributeNames == null) {
            listDefaultAttributeNames = new HashSet<>();

            listDefaultAttributeNames.add(PackageTemplateWrapper.ATTRIBUTE_BASE_NAME);
            listDefaultAttributeNames.add(FileTemplate.ATTRIBUTE_NAME);
            listDefaultAttributeNames.add(FileTemplate.ATTRIBUTE_PACKAGE_NAME);

            Properties defaultProperties = FileTemplateHelper.getDefaultProperties(project);
            for (Object Property : defaultProperties.keySet()) {
                listDefaultAttributeNames.add((String) Property);
            }
        }

        return listDefaultAttributeNames;
    }

    public static String[] getUnsetAttributes(FileTemplate fileTemplate, PackageTemplateWrapper ptWrapper) {
        HashSet<String> defaultAttributeNames = getDefaultAttributeNames(ptWrapper.getProject());

        // Add globals vars
        for (GlobalVariable variable : ptWrapper.getPackageTemplate().getListGlobalVariable()) {
            defaultAttributeNames.add(variable.getName());
        }

        Properties properties = new Properties();
        for (String item : defaultAttributeNames) {
            properties.put(item, "fakeValue");
        }

        //parse
        try {
            return fileTemplate.getUnsetAttributes(properties, ptWrapper.getProject());
        } catch (ParseException e) {
            Logger.log("getUnsetAttributes ex: " + e.getMessage());
            Logger.printStack(e);
            return new String[0];
        }
    }
}
