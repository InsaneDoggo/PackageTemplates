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
        String sb = "String getModifiedName(String name) {\n" +
                "   //TODO return modified 'name', Example:\n" +
                "   return name.toLowerCase();\n" +
                "}\n";

        //sb.append("%s\n return getModifiedName(\"%s\");");

        return ((String) getShell().evaluate(String.format(sb, variable, code)));
    }

    public static void main(String[] args) {
        String name = runGroovy("name.toLowerCase();", "PiOs");

        System.out.println("isValid " + TemplateValidator.isValidClassName(name));
    }

}
