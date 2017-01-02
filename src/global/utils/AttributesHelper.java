package global.utils;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.project.Project;
import global.models.GlobalVariable;
import global.wrappers.PackageTemplateWrapper;
import org.apache.velocity.runtime.parser.ParseException;

import java.util.*;

/**
 * Created by CeH9 on 15.07.2016.
 */
public class AttributesHelper {

    private static final ArrayList<String> listAttributesToRemove = new ArrayList<String>() {{
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

        list.removeIf(listAttributesToRemove::contains);

        return list.toArray(new String[list.size()]);
    }

    public static String[] getNonGlobalAttributes(String[] unsetAttributes, ArrayList<GlobalVariable> listGlobalVariable) {
        List<String> listUnset = new ArrayList<>(Arrays.asList(unsetAttributes));
        List<String> listGlobals = new ArrayList<>();

        for (GlobalVariable variable : listGlobalVariable) {
            listGlobals.add(variable.getName());
        }

        Iterator<String> iterator = listUnset.iterator();

        while (iterator.hasNext()) {
            String next = iterator.next();
            if (listGlobals.contains(next)) {
                iterator.remove();
            }
        }

        return listUnset.toArray(new String[listUnset.size()]);
    }
}
