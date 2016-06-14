package utils;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CeH9 on 14.06.2016.
 */
public class TemplatesValidator {

    public static final String PACKAGE_TEMPLATE_NAME = "${PACKAGE_TEMPLATE_NAME}";

    public static boolean isTemplatesValid(ArrayList<String> listNames){
        FileTemplateManager fileTemplateManager = FileTemplateManager.getDefaultInstance();
        List<String> listAllTemplates = new ArrayList<>();
        for( FileTemplate template : fileTemplateManager.getAllTemplates() ){
            //if( template.isDefault() ){
                listAllTemplates.add(template.getName());
            //}
        }

        for( String name : listNames ){
            if( !listAllTemplates.contains(name) ){
                Logger.log("Template " + name + " doesn't exist!");
                return false;
            }
        }

        return true;
    }

}
