package utils;

import models.PackageTemplate;
import models.TemplateElement;

/**
 * Created by Arsen on 15.06.2016.
 */
public class StringTools {

    public static final String PACKAGE_TEMPLATE_NAME = "${PACKAGE_TEMPLATE_NAME}";

    public static void replaceNameVariable(PackageTemplate packageTemplate, String packageTemplateName){

        if (packageTemplate.getListTemplateElement() == null){
            return;
        }

        for( TemplateElement element : packageTemplate.getListTemplateElement() ){
            element.replaceNameVariable(packageTemplateName);
        }
    }

    public static String replaceName(String packageTemplateName, String name){
        return name.replace(PACKAGE_TEMPLATE_NAME, packageTemplateName);
    }

    public static String formatConst(String text){
        return text.replace("_", " ");
    }

}
