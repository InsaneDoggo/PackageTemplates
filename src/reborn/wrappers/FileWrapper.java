package reborn.wrappers;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.util.ui.GridBag;
import reborn.models.BaseElement;
import reborn.models.File;
import utils.FileWriter;
import utils.StringTools;
import utils.TemplateValidator;
import utils.UIMaker;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class FileWrapper extends ElementWrapper {

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
        container.add(etfName, bag.next().coverLine(2));

        UIMaker.addMouseListener(this, project, PackageTemplateWrapper.ViewMode.EDIT);
    }

    @Override
    public void addElement(ElementWrapper element) {
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
    public void replaceNameVariable(HashMap<String, String> mapVariables) {
        getFile().setName(StringTools.replaceGlobalVariables(getFile().getName(), mapVariables));
    }

    @Override
    public ValidationInfo isNameValid(List<String> listAllTemplates) {
        if (!listAllTemplates.contains(getFile().getTemplateName())) {
            return new ValidationInfo("Template \"" + getFile().getTemplateName() + "\" doesn't exist!");
        }
        return null;
    }

    @Override
    public ValidationInfo validateFields() {
        return TemplateValidator.validateTextField(etfName, TemplateValidator.FieldType.CLASS_NAME);
    }

    @Override
    public void writeFile(PsiDirectory currentDir, Project project) {
        PsiElement psiElement = FileWriter.writeFile(currentDir, this);
        if (psiElement == null) {
            // TODO: 20.06.2016 error write file
        }
    }

    @Override
    public void updateParents(DirectoryWrapper dwParent) {

    }

    @Override
    public void initNonSerializableFields() {

    }

    @Override
    public void removeMyself() {
        getParent().removeElement(this);
    }

    @Override
    public void collectDataFromFields() {
        file.setTemplateName(jlName.getText());
        file.setName(etfName.getText());
    }

    @Override
    public void runGroovyScript() {

    }

}
