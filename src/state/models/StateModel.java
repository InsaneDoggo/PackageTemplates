package state.models;

import models.PackageTemplate;

import java.util.ArrayList;

/**
 * Created by CeH9 on 05.08.2016.
 */
public class StateModel {

    private long modelVersion;
    private ArrayList<PackageTemplate> listPackageTemplate;

    public long getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(long modelVersion) {
        this.modelVersion = modelVersion;
    }

    public ArrayList<PackageTemplate> getListPackageTemplate() {
        return listPackageTemplate;
    }

    public void setListPackageTemplate(ArrayList<PackageTemplate> listPackageTemplate) {
        this.listPackageTemplate = listPackageTemplate;
    }
}
