package state.export.models;

import models.PackageTemplate;

import java.util.ArrayList;

/**
 * Created by CeH9 on 05.08.2016.
 */
public class ExportBundle {

    private long modelVersion;
    private ArrayList<PackageTemplate> listPackageTemplate;
    private ArrayList<ExpFileTemplate> listExpFileTemplate;

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

    public ArrayList<ExpFileTemplate> getListExpFileTemplate() {
        return listExpFileTemplate;
    }

    public void setListExpFileTemplate(ArrayList<ExpFileTemplate> listExpFileTemplate) {
        this.listExpFileTemplate = listExpFileTemplate;
    }
}
