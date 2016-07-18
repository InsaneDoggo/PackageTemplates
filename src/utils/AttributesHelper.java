package utils;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.project.Project;
import com.intellij.util.ArrayUtil;
import org.apache.velocity.runtime.parser.ParseException;
import org.jetbrains.annotations.Nullable;
import wrappers.PackageTemplateWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by CeH9 on 15.07.2016.
 */
public class AttributesHelper {

    public static final ArrayList<String> listAttributesToRemove = new ArrayList<String>() {{
        add(FileTemplate.ATTRIBUTE_NAME);
        add(FileTemplate.ATTRIBUTE_PACKAGE_NAME);
        add(PackageTemplateWrapper.ATTRIBUTE_BASE_NAME);
    }};

    public static String[] getUnsetAttrs(FileTemplate fileTemplate, Project project) {
        try {
            return getWithoutDefaultAttributes(fileTemplate.getUnsetAttributes(new Properties(), project));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String[] getWithoutDefaultAttributes(String[] unsetAttributes) {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(unsetAttributes));
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (listAttributesToRemove.contains(iterator.next())) {
                iterator.remove();
            }
        }
        return list.toArray(new String[list.size()]);
    }

    @Nullable
    public static FileTemplate getTemplate(String name) {
        FileTemplateManager ftm = FileTemplateManager.getDefaultInstance();

        FileTemplate result = ftm.getTemplate(name);
        if( result == null ) result = ftm.getInternalTemplate(name);
        if( result == null ) result = ftm.getJ2eeTemplate(name);

        return result;
    }

}
