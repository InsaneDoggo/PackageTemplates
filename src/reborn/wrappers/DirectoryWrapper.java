package reborn.wrappers;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.GridBag;
import reborn.models.BaseElement;
import reborn.models.Directory;
import utils.UIMaker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class DirectoryWrapper extends BaseWrapper {

    private Directory directory;
    private JPanel panel;
    private GridBag gridBag;

    private ArrayList<BaseWrapper> listBaseWrapper;

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
    }

    public ArrayList<BaseWrapper> getListBaseWrapper() {
        return listBaseWrapper;
    }

    public void setListBaseWrapper(ArrayList<BaseWrapper> listBaseWrapper) {
        this.listBaseWrapper = listBaseWrapper;
    }

    public void addElement(BaseWrapper element) {
        directory.getListBaseElement().add(element.getElement());
        listBaseWrapper.add(element);
    }

    @Override
    public BaseElement getElement() {
        return directory;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    public void removeElement(BaseWrapper element) {
        directory.getListBaseElement().remove(element.getElement());
        listBaseWrapper.remove(element);
    }

    @Override
    public void removeMyself() {
        if (getParent() == null) {
            return;
        }

        getParent().removeElement(this);
    }

    @Override
    public void buildView(Project project, JPanel container, GridBag bag, PackageTemplateWrapper.ViewMode mode) {
        if (panel == null) {
            panel = new JPanel(new GridBagLayout());
        } else {
            panel.removeAll();
        }

        gridBag = new GridBag()
                .setDefaultWeightX(1)
                .setDefaultInsets(new Insets(4, 0, 4, 0))
                .setDefaultFill(GridBagConstraints.HORIZONTAL);

        createPackageView( project, container, bag, mode);

        for (BaseWrapper baseWrapper : getListBaseWrapper()) {
            baseWrapper.buildView(project, panel, gridBag, PackageTemplateWrapper.ViewMode.EDIT);
        }

        UIMaker.setLeftPadding(panel, UIMaker.PADDING + UIMaker.DEFAULT_PADDING);
        container.add(panel, bag.nextLine().next());
    }

    private void createPackageView(Project project, JPanel container, GridBag bag, PackageTemplateWrapper.ViewMode mode) {
        jlName = new JLabel(AllIcons.Nodes.Package, SwingConstants.LEFT);
        jlName.setText("Directory");
        UIMaker.setRightPadding(jlName, UIMaker.PADDING_LABEL);

        etfName = UIMaker.getEditorTextField(getDirectory().getName(), project);

        container.add(jlName, bag.nextLine().next());
        container.add(etfName, bag.next());

        UIMaker.addMouseListener(this, project, mode);
    }

    @Override
    public void collectDataFromFields() {
        getDirectory().setName(etfName.getText());

        for (BaseWrapper baseWrapper : getListBaseWrapper()) {
            baseWrapper.collectDataFromFields();
        }
    }

}
