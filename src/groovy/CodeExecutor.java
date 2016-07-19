package groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import utils.TemplateValidator;

/**
 * Created by CeH9 on 19.07.2016.
 */
public class CodeExecutor {

    private static GroovyShell shell;

    public static GroovyShell getShell(){
        if( shell == null ) {
            shell = new GroovyShell(new Binding());
        }

        return shell;
    }

    public static String runGroovy(String code, String variable){
        String result = variable;
        // Write your code here
//        code = "result.toLowerCase();";

        Object value = getShell().evaluate(String.format("String result = \"%s\";\n%s", variable, code));

        return ((String) value);
    }

    public static void main(String[] args) {
        String name = runGroovy("result.toLowerCase();", "PiOs");

        System.out.println("isValid " + TemplateValidator.isValidClassName(name));
    }

}
