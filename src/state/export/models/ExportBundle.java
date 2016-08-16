package state.export.models;

import com.google.gson.annotations.Expose;
import models.PackageTemplate;

import java.util.ArrayList;

/**
 * Created by CeH9 on 05.08.2016.
 */
public class ExportBundle {

    @Expose
    private long modelVersion;
    @Expose
    private ArrayList<ExpPackageTemplate> listPackageTemplate;

    public long getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(long modelVersion) {
        this.modelVersion = modelVersion;
    }

    public ArrayList<ExpPackageTemplate> getListPackageTemplate() {
        return listPackageTemplate;
    }

    public void setListPackageTemplate(ArrayList<ExpPackageTemplate> listPackageTemplate) {
        this.listPackageTemplate = listPackageTemplate;
    }
}
