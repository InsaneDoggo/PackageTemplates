package core.state.util;

import core.state.models.StateModel;
import global.models.PackageTemplate;

/**
 * Created by Arsen on 09.10.2016.
 */
public class StateEditor {

    private StateModel model() {
        return SaveUtil.getInstance().getStateModel();
    }

    public StateEditor save(){
        SaveUtil.getInstance().save();
        return this;
    }

    public StateEditor removeGroupName(String name){
        model().getUserSettings().getGroupNames().remove(name);
        return this;
    }

    public StateEditor removePackageTemplate(PackageTemplate pt) {
        model().getListPackageTemplate().remove(pt);
        return this;
    }

    public StateEditor addPackageTemplate(PackageTemplate pt) {
        model().getListPackageTemplate().add(pt);
        return this;
    }
}
