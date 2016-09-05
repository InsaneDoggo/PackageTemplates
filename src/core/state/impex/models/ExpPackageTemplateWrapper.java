package core.state.impex.models;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import global.models.BaseElement;
import global.models.Directory;
import global.models.File;
import global.models.PackageTemplate;
import global.utils.AttributesHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Arsen on 16.08.2016.
 */
public class ExpPackageTemplateWrapper {

    private boolean isSelected;
    private ExpPackageTemplate epTemplate;
    private ArrayList<ExpFileTemplateWrapper> listExpFileTemplateWrapper;
    public JBCheckBox cbInclude;
    public JBLabel jlName;

    public ExpPackageTemplate getEpTemplate() {
        return epTemplate;
    }

    public ExpPackageTemplateWrapper(boolean isSelected, PackageTemplate pt) {
        this.isSelected = isSelected;
        epTemplate = new ExpPackageTemplate(pt, getListFileTemplates(pt));
        listExpFileTemplateWrapper = new ArrayList<>();
        for (ExpFileTemplate efTemplate : epTemplate.getListExpFileTemplate()){
            listExpFileTemplateWrapper.add(new ExpFileTemplateWrapper(true, efTemplate));
        }

        cbInclude = new JBCheckBox("", isSelected);
        jlName = new JBLabel(pt.getName());
    }

    public ExpPackageTemplateWrapper(boolean isSelected, ExpPackageTemplate ept) {
        this.isSelected = isSelected;
        epTemplate = ept;
        listExpFileTemplateWrapper = new ArrayList<>();
        for (ExpFileTemplate efTemplate : epTemplate.getListExpFileTemplate()){
            listExpFileTemplateWrapper.add(new ExpFileTemplateWrapper(true, efTemplate));
        }

        cbInclude = new JBCheckBox("", isSelected);
        jlName = new JBLabel(ept.getPackageTemplate().getName());
    }

    public boolean isSelected() {
        return isSelected;
    }

    public ArrayList<ExpFileTemplateWrapper> getListExpFileTemplateWrapper() {
        return listExpFileTemplateWrapper;
    }

    private Set<ExpFileTemplate> getListFileTemplates(PackageTemplate pt) {
        Set<ExpFileTemplate> result = new HashSet<>();

        Set<String> names = new HashSet<>();
        initFileTemplateNames(names, pt.getDirectory());

        for (String name : names) {
            FileTemplate fileTemplate = AttributesHelper.getTemplate(name);
            if (fileTemplate == null) {
                //System.out.println("Skip null :" + name);
                continue;
            }
            if (fileTemplate.isDefault()) {
                //System.out.println("Skip Default :" + name);
                continue;
            }
            result.add(new ExpFileTemplate(
                    name,
                    fileTemplate.getExtension(),
                    fileTemplate.getDescription(),
                    fileTemplate.getText()
            ));
        }

        return result;
    }

    private void initFileTemplateNames(Set<String> names, Directory directory) {
        for (BaseElement element : directory.getListBaseElement()) {
            if (element.isDirectory()) {
                initFileTemplateNames(names, ((Directory) element));
            } else {
                names.add(((File) element).getTemplateName());
            }
        }
    }

    public String getName() {
        return epTemplate.getPackageTemplate().getName();
    }

    public ExpPackageTemplate getTemplateForExport() {
        for(ExpFileTemplateWrapper item : listExpFileTemplateWrapper ){
            if(item.cbInclude.isSelected()){
                epTemplate.getListExpFileTemplate().add(item.getTemplate());
            }
        }

        return epTemplate;
    }
}
