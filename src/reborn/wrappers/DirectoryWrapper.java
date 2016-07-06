package reborn.wrappers;

import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import custom.components.TemplateView;
import reborn.models.Directory;
import utils.UIMaker;

import javax.swing.*;

import static utils.UIMaker.getDefaultGridBag;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class DirectoryWrapper extends BaseWrapper {

    private Directory directory;
    private Directory parent;

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
    }

    public Directory getParent() {
        return parent;
    }

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    public TemplateView buildView(Project project) {
        removeAll();

        setBag(getDefaultGridBag());
        getBag().setDefaultWeightX(1);

        if (isDirectory()) {
            add(UIMaker.getPackageView(this, project), getBag().nextLine().next());

            for (TemplateView templateView : getListTemplateView()) {
                TemplateView view = templateView.buildView(project);
                add(view, getBag().nextLine().next());
            }
        } else {
            add(UIMaker.getClassView(this, project), getBag().nextLine().next());
        }

        UIMaker.setLeftPadding(this, UIMaker.PADDING + UIMaker.DEFAULT_PADDING);
        return this;
    }

    public void reBuild(Project project) {
        if (getTemplateParent() == null) {
            removeAll();
            buildView(project);
        } else {
            getTemplateParent().reBuild(project);
        }
    }

    public void addTemplate(TemplateView tView) {
        if (isDirectory()) {
            getListTemplateView().add(tView);
        } else {
            getTemplateParent().getListTemplateView().add(tView);
        }
    }

    public void removeMyself() {
        if (getTemplateParent() == null) {
            return;
        } else {
            getTemplateParent().getListTemplateView().remove(this);
        }
    }

    public void collectDataFromFields() {
        if(isDirectory()){
            setPredefinedName(getEtfName().getText());

            for (TemplateView templateView : getListTemplateView()){
                templateView.collectDataFromFields();
            }
        } else {
            setPredefinedName(getEtfName().getText());
            setTemplateName(getJlName().getText());
        }
    }

}
