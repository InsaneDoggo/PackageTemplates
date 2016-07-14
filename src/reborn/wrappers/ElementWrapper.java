package reborn.wrappers;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.PsiDirectory;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.GridBag;
import reborn.models.BaseElement;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created by CeH9 on 06.07.2016.
 */
public abstract class ElementWrapper extends BaseWrapper {

    public JLabel jlName;
    public EditorTextField etfName;
    private DirectoryWrapper parent;

    private PackageTemplateWrapper packageTemplateWrapper;
    // check box
    //btn for groovy

    public abstract void buildView(Project project, JPanel container, GridBag bag, PackageTemplateWrapper.ViewMode mode);
    public abstract void removeMyself();
    public abstract void addElement(ElementWrapper element);
    public abstract BaseElement getElement();
    public abstract boolean isDirectory();
    public abstract void replaceNameVariable(HashMap<String, String> mapVariables);
    public abstract ValidationInfo isNameValid(List<String> listAllTemplates);
    public abstract ValidationInfo validateFields();
    public abstract void writeFile(PsiDirectory currentDir, Project project);
    public abstract void updateParents(DirectoryWrapper dwParent);
    public abstract void initNonSerializableFields();

    public DirectoryWrapper getParent() {
        return parent;
    }

    public void setParent(DirectoryWrapper parent) {
        this.parent = parent;
    }

    public PackageTemplateWrapper getPackageTemplateWrapper() {
        return packageTemplateWrapper;
    }

    public void setPackageTemplateWrapper(PackageTemplateWrapper packageTemplateWrapper) {
        this.packageTemplateWrapper = packageTemplateWrapper;
    }

    public void reBuild(Project project) {
        //todo delete project?
        packageTemplateWrapper.reBuildView();
    }

}
