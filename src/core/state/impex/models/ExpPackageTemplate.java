package core.state.impex.models;

import com.google.gson.annotations.Expose;
import global.models.PackageTemplate;

import java.util.ArrayList;

/**
 * Created by Arsen on 16.08.2016.
 */
public class ExpPackageTemplate {

    @Expose
    private PackageTemplate packageTemplate;
    @Expose
    private ArrayList<ExpFileTemplate> listExpFileTemplate;

    public ExpPackageTemplate(PackageTemplate packageTemplate, ArrayList<ExpFileTemplate> listExpFileTemplate) {
        this.packageTemplate = packageTemplate;
        this.listExpFileTemplate = listExpFileTemplate;
    }

    public PackageTemplate getPackageTemplate() {
        return packageTemplate;
    }

    public void setPackageTemplate(PackageTemplate packageTemplate) {
        this.packageTemplate = packageTemplate;
    }

    public ArrayList<ExpFileTemplate> getListExpFileTemplate() {
        return listExpFileTemplate;
    }

    public void setListExpFileTemplate(ArrayList<ExpFileTemplate> listExpFileTemplate) {
        this.listExpFileTemplate = listExpFileTemplate;
    }
}
