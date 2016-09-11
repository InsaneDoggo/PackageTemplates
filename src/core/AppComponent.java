package core;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.ApplicationComponent;
import core.actions.generator.BaseAction;
import core.state.SaveUtil;
import global.Const;
import global.models.PackageTemplate;
import global.utils.StringTools;
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
        ArrayList<PackageTemplate> listPackageTemplate = SaveUtil.getInstance().getStateModel().getListPackageTemplate();

        for(PackageTemplate pt : listPackageTemplate){
            if(!pt.isShouldRegisterAction()){
                continue;
            }
            BaseAction action = new BaseAction(StringTools.formatActionName(pt.getName()), pt);
            am.registerAction(action.getName() + Const.ACTION_PREFIX, action);
//            group.addSeparator();
//            group.add(action);
        }
    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
//        return Localizer.get("plugin.name");
        return "PackageTemplatesComponent";
    }
}
