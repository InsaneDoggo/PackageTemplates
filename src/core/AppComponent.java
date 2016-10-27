package core;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.components.ApplicationComponent;
import core.actions.generated.RunTemplateAction;
import core.state.util.SaveUtil;
import global.Const;
import global.models.PackageTemplate;
import global.utils.PackageTemplateHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by Arsen on 11.09.2016.
 */
public class AppComponent implements ApplicationComponent {

    @Override
    public void initComponent() {
        ActionManager am = ActionManager.getInstance();
        ArrayList<PackageTemplate> listPackageTemplate = PackageTemplateHelper.getListPackageTemplate();

        for (PackageTemplate pt : listPackageTemplate) {
            if (!pt.isShouldRegisterAction()) {
                continue;
            }
            RunTemplateAction action = new RunTemplateAction(pt.getName(), pt);
            am.registerAction(Const.ACTION_PREFIX + action.getName(), action);
        }
    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "PackageTemplatesComponent";
    }
}
