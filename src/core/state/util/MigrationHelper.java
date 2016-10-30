package core.state.util;

import core.state.models.StateModel;

/**
 * Created by CeH9 on 06.08.2016.
 */
public class MigrationHelper {

    public static final long CURRENT_MODEL_VERSION = 1;

    public static void checkVersion(StateModel stateModel) {
        if (stateModel.getModelVersion() == CURRENT_MODEL_VERSION) {
            return;
        }

//        if(stateModel.getModelVersion() == 1){
//            //todo migrate from 1 to 2.
//            stateModel.setModelVersion(2);
//        }

//        SaveUtil.getInstance().save();
    }

}
