package wrappers;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.ui.CreateFromTemplatePanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.util.ui.GridBag;
import models.BaseElement;
import models.File;
import utils.*;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by CeH9 on 06.07.2016.
 */
public class FileWrapper extends ElementWrapper {

    private File file;
    private CreateFromTemplatePanel panelVariables;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public void buildView(Project project, JPanel container, GridBag bag) {
        jlName = new JLabel(UIHelper.getIconByFileExtension(getFile().getExtension()), SwingConstants.LEFT);
        jlName.setText(getFile().getTemplateName());
        UIHelper.setRightPadding(jlName, UIHelper.PADDING_LABEL);

        etfName = UIHelper.getEditorTextField(getFile().getName(), project);

        container.add(jlName, bag.nextLine().next());
        container.add(etfName, bag.next().coverLine(2));

        addMouseListener();

        if (getPackageTemplateWrapper().getMode() == PackageTemplateWrapper.ViewMode.USAGE) {
            FileTemplate fileTemplate = AttributesHelper.getTemplate(getFile().getTemplateName());
            if (fileTemplate != null) {
                String[] unsetAttributes = AttributesHelper.getUnsetAttrs(fileTemplate, getPackageTemplateWrapper().getProject());
                if (unsetAttributes != null && unsetAttributes.length > 0) {
                    panelVariables = new CreateFromTemplatePanel(unsetAttributes, false, null);

                    JComponent component = panelVariables.getComponent();
                    UIHelper.setLeftPadding(component, UIHelper.PADDING);
                    container.add(component, bag.nextLine().next().coverLine());
                }
            }
        }
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

        if( getPackageTemplateWrapper().getMode() == PackageTemplateWrapper.ViewMode.USAGE ) {
            getFile().setMapProperties(new HashMap<>());
            if (panelVariables != null) {
                Properties properties = new Properties();
                properties = panelVariables.getProperties(properties);

                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    getFile().getMapProperties().put((String) entry.getKey(), (String) entry.getValue());
                }
            }
            //add GLOBALS
            getFile().getMapProperties().putAll(getPackageTemplateWrapper().getPackageTemplate().getMapGlobalVars());
        }

    }

    @Override
    public void runGroovyScript() {
        // TODO: 17.07.2016 runGroovyScript
    }

    @Override
    public void updateComponentsState() {
        // TODO: 17.07.2016 updateComponentsState
    }

}
