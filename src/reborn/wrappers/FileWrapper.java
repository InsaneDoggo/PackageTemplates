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
    public void buildView(Project project, JPanel container, GridBag bag) {
        jlName = new JLabel(UIMaker.getIconByFileExtension(file.getExtension()), SwingConstants.LEFT);

        jlName.setText(file.getTemplateName());
        UIMaker.setRightPadding(jlName, UIMaker.PADDING_LABEL);

        etfName = UIMaker.getEditorTextField(file.getName(), project);

        bag.weightx(1);
        container.add(jlName, bag.nextLine().next());
        container.add(etfName, bag.next());

        UIMaker.addMouseListener(this, project);
    }

    @Override
    public void reBuild(Project project) {
        getParent().reBuild(project);
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
    public void removeMyself() {
        getParent().removeElement(this);
    }

    @Override
    public void collectDataFromFields() {
        file.setName(jlName.getText());
        file.setTemplateName(etfName.getText());
    }

}
