package utils;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.ui.ValidationInfo;
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
    public static ValidationInfo isTemplateValid(PackageTemplate packageTemplate){
        if (packageTemplate.getListTemplateElement() == null){
            // when template is empty folder(useless, but valid)
            return null;
        }

        List<String> listAllTemplates = new ArrayList<>();

        for( FileTemplate template : FileTemplateManager.getDefaultInstance().getTemplates(DEFAULT_TEMPLATES_CATEGORY) ){
            //if( template.isDefault() ){
                listAllTemplates.add(template.getName());
            //}
        }

        for( TemplateElement element : packageTemplate.getListTemplateElement() ){
            ValidationInfo validationInfo = element.isNameValid(listAllTemplates);
            if( validationInfo != null ){
                return validationInfo;
            }
        }
        return null;
    }

}
