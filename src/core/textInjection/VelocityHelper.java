package core.textInjection;

import global.utils.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jetbrains.java.generate.velocity.VelocityFactory;

import java.io.StringWriter;
import java.util.Map;

/**
 * Created by Arsen on 15.03.2017.
 */
public class VelocityHelper {

    public static String fromTemplate(String velocityTemplate, Map<String, String> mapVariables) {
        StringWriter stringWriter = new StringWriter();
        try {
            VelocityEngine velocityEngine = VelocityFactory.getVelocityEngine();

            VelocityContext velocityContext = new VelocityContext();
            for (Map.Entry<String, String> entry : mapVariables.entrySet()) {
                velocityContext.put(entry.getKey(), entry.getValue());
            }

            velocityEngine.evaluate(velocityContext, stringWriter, "PackageTemplatesVelocity", velocityTemplate);
        } catch (Exception e) {
            Logger.log("fromTemplate Error in Velocity code generator");
            return null;
        }
        return stringWriter.getBuffer().toString();
    }

}
