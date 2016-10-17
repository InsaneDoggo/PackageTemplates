package core.state.impex.models;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;

/**
 * Created by Arsen on 16.08.2016.
 */
public class ExpFileTemplateWrapper {

    private ExpFileTemplate template;

    public JBCheckBox cbInclude;
    public JBLabel jlName;

    public ExpFileTemplateWrapper(boolean isSelected, ExpFileTemplate template) {
        this.template = template;

        cbInclude = new JBCheckBox("", isSelected);
        jlName = new JBLabel(template.getName());
    }

    public ExpFileTemplate getTemplate() {
        return template;
    }

    public void setTemplate(ExpFileTemplate template) {
        this.template = template;
    }
}
