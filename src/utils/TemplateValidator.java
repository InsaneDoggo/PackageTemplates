package utils;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import models.PackageTemplate;
import models.TemplateElement;

import java.util.ArrayList;
import java.util.List;

import static com.intellij.ide.fileTemplates.FileTemplateManager.DEFAULT_TEMPLATES_CATEGORY;

/**
 * Created by CeH9 on 14.06.2016.
 */
public class TemplateValidator {

    /**
     * Check existence FileTemplates used in PackageTemplate
     */
    public static boolean isTemplateValid(PackageTemplate packageTemplate){
        if (packageTemplate.getListTemplateElement() == null){
            // when template is empty folder(useless, but valid)
            return true;
        }


        List<String> listAllTemplates = new ArrayList<>();

        for( FileTemplate template : FileTemplateManager.getDefaultInstance().getTemplates(DEFAULT_TEMPLATES_CATEGORY) ){
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
