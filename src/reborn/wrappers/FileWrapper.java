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

    public void buildView(Project project, JPanel container, GridBag bag) {
        jlName = new JLabel(UIMaker.getIconByFileExtension(file.getExtension()), SwingConstants.LEFT);

        jlName.setText(file.getName());
        UIMaker.setRightPadding(jlName, UIMaker.PADDING_LABEL);

        etfName = UIMaker.getEditorTextField(file.getValue(), project);

        bag.weightx(1);
        container.add(jlName, bag.nextLine().next());
        container.add(etfName, bag.next());

        UIMaker.addMouseListener(this, project);
    }

    public void addTemplate(BaseElement element) {
        // TODO: 07.07.2016 add method in Directory
//        getParent().getDirectory().getListBaseElement().add(element);
    }

    public void removeMyself() {
        // TODO: 07.07.2016 remove method in Directory
        //   getParent().getDirectory().getListBaseElement().remove(this);
    }

    public void collectDataFromFields() {
        file.setName(jlName.getText());
        file.setValue(etfName.getText());
    }

}
