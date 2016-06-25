package custom.components;

import com.intellij.openapi.project.Project;
import com.intellij.util.ui.GridBag;
import utils.UIMaker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static com.intellij.testFramework.LightPlatformTestCase.getProject;
import static com.intellij.ui.content.ContentManagerEvent.ContentOperation.add;
import static javafx.scene.input.KeyCode.T;
import static utils.UIMaker.getDefaultGridBag;

/**
 * Created by CeH9 on 23.06.2016.
 */
public class TemplateView extends JPanel {

    private String defaultName;
    private String extension;
    private String templateName;
    private boolean isDirectory;
    private ArrayList<TemplateView> listTemplateView;

    private GridBag bag;
    private TemplateView templateParent;

    public TemplateView(String defaultName, String templateName, String extension, TemplateView templateParent) {
        this.defaultName = defaultName;
        this.templateParent = templateParent;
        this.templateName = templateName;
        this.extension = extension;
        this.isDirectory = false;

        setLayout(new GridBagLayout());
    }

    public TemplateView(String defaultName, TemplateView templateParent) {
        this.defaultName = defaultName;
        this.templateParent = templateParent;
        this.isDirectory = true;
        this.listTemplateView = new ArrayList<>();

        setLayout(new GridBagLayout());
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

    public TemplateView getTemplateParent() {
        return templateParent;
    }

    public void setTemplateParent(TemplateView parent) {
        this.templateParent = parent;
    }

    public GridBag getBag() {
        return bag;
    }

    public void setBag(GridBag bag) {
        this.bag = bag;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public ArrayList<TemplateView> getListTemplateView() {
        return listTemplateView;
    }

    public void setListTemplateView(ArrayList<TemplateView> listTemplateView) {
        this.listTemplateView = listTemplateView;
    }

    public void reBuild() {
        if (getTemplateParent() == null) {
            removeAll();
            buildView(getProject());
        } else {
            getTemplateParent().reBuild();
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
        if(getTemplateParent() == null){
            return;
        } else {
            getTemplateParent().getListTemplateView().remove(this);
        }
    }
}
