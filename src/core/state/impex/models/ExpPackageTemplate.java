package core.state.impex.models;

import com.google.gson.annotations.Expose;
import global.models.PackageTemplate;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Arsen on 16.08.2016.
 */
public class ExpPackageTemplate {

    @Expose
    private PackageTemplate packageTemplate;
    @Expose
    private Set<ExpFileTemplate> listExpFileTemplate;

    public ExpPackageTemplate(PackageTemplate packageTemplate, Set<ExpFileTemplate> listExpFileTemplate) {
        this.packageTemplate = packageTemplate;
        this.listExpFileTemplate = listExpFileTemplate;
    }

    public PackageTemplate getPackageTemplate() {
        return packageTemplate;
    }

    public void setPackageTemplate(PackageTemplate packageTemplate) {
        this.packageTemplate = packageTemplate;
    }

    public Set<ExpFileTemplate> getListExpFileTemplate() {
        return listExpFileTemplate;
    }

    public void setListExpFileTemplate(Set<ExpFileTemplate> listExpFileTemplate) {
        this.listExpFileTemplate = listExpFileTemplate;
    }
}
