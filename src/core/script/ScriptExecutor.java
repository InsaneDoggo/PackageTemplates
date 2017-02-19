package core.script;

import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.EditorTextField;
import global.utils.Logger;
import global.utils.i18n.Localizer;
import global.utils.validation.FieldType;
import global.utils.validation.TemplateValidator;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


/**
 * Created by CeH9 on 19.07.2016.
 */
public class ScriptExecutor {

    private static final String FUNCTION_NAME = "getModifiedName";
    public static String defaultCode =
            "\nfunction  " + ScriptExecutor.FUNCTION_NAME + "(name) {\n" +
                    "   //A small Example:\n" +
                    "   return name.toLowerCase();\n" +
                    "}\n";

    /**
     * @return результат вызова функции из переданного кода.
     */
    public static String runScript(String code, String variable) {
        try {
            // evaluate script
            getEngine().eval(code);

            return (String) ((Invocable) getEngine()).invokeFunction(FUNCTION_NAME, variable);
        } catch (Exception e) {
            Logger.log("Run script ex: " + e.getMessage());
            Logger.printStack(e);
            return Localizer.get("ScriptError");
        }
    }

    public static ValidationInfo ValidateScript(EditorTextField etfCode, String variable) {
        try {
            return TemplateValidator.validateText(etfCode, runScript(etfCode.getText(), variable), FieldType.SCRIPT);
        } catch (Exception e) {
            return new ValidationInfo(e.getMessage());
        }
    }


    //=================================================================
    //  Engine
    //=================================================================
    private static ScriptEngine engine;
    private static final String ENGINE_NAME = "JavaScript";

    public static ScriptEngine getEngine() {
        if (engine == null) {
            engine = new ScriptEngineManager().getEngineByName(ENGINE_NAME);
        }
        return engine;
    }

}
