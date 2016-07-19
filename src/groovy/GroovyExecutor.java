package groovy;

import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.EditorTextField;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import utils.TemplateValidator;

/**
 * Created by CeH9 on 19.07.2016.
 */
public class GroovyExecutor {

    private static GroovyShell shell;

    public static GroovyShell getShell(){
        if( shell == null ) {
            shell = new GroovyShell(new Binding());
        }

        return shell;
    }

    public static String runGroovy(String code, String variable){
        try {
            return ((String) getShell().evaluate(String.format("%s\n return getModifiedName(\"%s\");", code, variable)));
        } catch (Exception e){
            return e.getMessage();
        }
    }

    public static ValidationInfo ValidateGroovyCode(EditorTextField etfCode, String variable){
        try {
            getShell().evaluate(String.format("%s\n return getModifiedName(\"%s\");", etfCode.getText(), variable));
            return TemplateValidator.validateText(etfCode, etfCode.getText(), TemplateValidator.FieldType.CLASS_NAME);
        } catch (Exception e){
            return new ValidationInfo(e.getMessage());
        }
    }

    public static void main(String[] args) {
        String name = runGroovy("name.toLowerCase();", "PiOs");

        System.out.println("isValid " + TemplateValidator.isValidClassName(name));
    }

}
