package utils;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import models.PackageTemplate;
import models.TemplateElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CeH9 on 14.06.2016.
 */
public class TemplateValidator {

    /**
     * Check existence FileTemplates used in PackageTemplate
     */
    public static boolean isTemplatesValid(PackageTemplate packageTemplate){
        if (packageTemplate.getListTemplateElement() == null){
            // when template is empty folder(useless, but valid)
            return true;
        }

        FileTemplateManager fileTemplateManager = FileTemplateManager.getDefaultInstance();
        List<String> listAllTemplates = new ArrayList<>();

        for( FileTemplate template : fileTemplateManager.getAllTemplates() ){
            //if( template.isDefault() ){
                listAllTemplates.add(template.getName());
            //}
        }

        for( TemplateElement element : packageTemplate.getListTemplateElement() ){
            if( !element.isNameValid(listAllTemplates) ){
                return false;
            }
        }
        return true;
    }

}
