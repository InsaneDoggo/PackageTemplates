package core.actions.generator;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import global.utils.Localizer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by Arsen on 11.09.2016.
 */
public class AppComponent implements ApplicationComponent {

    @Override
    public void initComponent() {
        ActionManager am = ActionManager.getInstance();
        DefaultActionGroup group = (DefaultActionGroup) am.getAction("WindowMenu");

        ArrayList<BaseAction> listBaseAction = new ArrayList<>();
        listBaseAction.add(new BaseAction("AcOne"));
        listBaseAction.add(new BaseAction("AcTwo"));
        listBaseAction.add(new BaseAction("AcThree"));
        listBaseAction.add(new BaseAction("AcFour"));

        for( BaseAction action : listBaseAction){
            am.registerAction(action.getName() + "Action", action);
            group.addSeparator();
            group.add(action);
        }
    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
//        return Localizer.get("plugin.name");
        return "MegaAction";
    }
}
