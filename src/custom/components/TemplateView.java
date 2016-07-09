package custom.components;

import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import com.intellij.util.ui.GridBag;
import models.TemplateElement;
import utils.UIMaker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static utils.UIMaker.getDefaultGridBag;

/**
 * Created by CeH9 on 23.06.2016.
 */
public class TemplateView extends JPanel {
    private String predefinedName;
    private String extension;
    private String templateName;
    private boolean isDirectory;
    private ArrayList<TemplateView> listTemplateView;

    private GridBag bag;
    private TemplateView templateParent;

    private EditorTextField etfName;
    private JLabel jlName;

    public TemplateView(String predefinedName, String templateName, String extension, TemplateView templateParent) {
        this.predefinedName = predefinedName;
        this.templateParent = templateParent;
        this.templateName = templateName;
        this.extension = extension;
        this.isDirectory = false;

        setLayout(new GridBagLayout());
    }

    public TemplateView(String predefinedName, TemplateView templateParent) {
        this.predefinedName = predefinedName;
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
//            add(UIMaker.createPackageView(this, project), getBag().nextLine().next());

            for (TemplateView templateView : getListTemplateView()) {
                TemplateView view = templateView.buildView(project);
                add(view, getBag().nextLine().next());
            }
        } else {
//            add(UIMaker.createClassView(this, project), getBag().nextLine().next());
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

    public String getPredefinedName() {
        return predefinedName;
    }

    public void setPredefinedName(String predefinedName) {
        this.predefinedName = predefinedName;
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

    public EditorTextField getEtfName() {
        return etfName;
    }

    public void setEtfName(EditorTextField etfName) {
        this.etfName = etfName;
    }

    public JLabel getJlName() {
        return jlName;
    }

    public void setJlName(JLabel jlName) {
        this.jlName = jlName;
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

    public TemplateElement toTemplateElement(TemplateElement parent) {
        TemplateElement result;
        if (isDirectory()) {
            result = new TemplateElement(getPredefinedName(), new ArrayList<>(), parent);

            for (TemplateView templateView : getListTemplateView()) {
                result.add(templateView.toTemplateElement(result));
            }

        } else {
            result = new TemplateElement(getPredefinedName(), getTemplateName(), getExtension(), parent);
        }

        return result;
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
