package reborn.wrappers;

import com.intellij.openapi.project.Project;
import com.intellij.util.ui.GridBag;
import reborn.models.BaseElement;
import reborn.models.File;
import utils.UIMaker;

import javax.swing.*;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class FileWrapper extends BaseWrapper {

    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public void buildView(Project project, JPanel container, GridBag bag, PackageTemplateWrapper.ViewMode mode) {
        jlName = new JLabel(UIMaker.getIconByFileExtension(getFile().getExtension()), SwingConstants.LEFT);
        jlName.setText(getFile().getTemplateName());
        UIMaker.setRightPadding(jlName, UIMaker.PADDING_LABEL);

        etfName = UIMaker.getEditorTextField(getFile().getName(), project);

        container.add(jlName, bag.nextLine().next());
        container.add(etfName, bag.next());

        UIMaker.addMouseListener(this, project, PackageTemplateWrapper.ViewMode.EDIT);
    }

    @Override
    public void addElement(BaseWrapper element) {
        getParent().addElement(element);
    }

    @Override
    public BaseElement getElement() {
        return file;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public void removeMyself() {
        getParent().removeElement(this);
    }

    @Override
    public void collectDataFromFields() {
        file.setName(jlName.getText());
        file.setTemplateName(etfName.getText());
    }

}
