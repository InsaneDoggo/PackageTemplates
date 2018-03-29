package global.models;

import com.intellij.ide.fileTemplates.FileTemplate;

/**
 * Created by CeH9 on 25.06.2016.
 */
public class TemplateForSearch {

    private FileTemplate template;

    public TemplateForSearch(FileTemplate template) {
        this.template = template;
    }

    @Override
    public String toString() {
        return template.getName();
    }

    public FileTemplate getTemplate() {
        return template;
    }

    public void setTemplate(FileTemplate template) {
        this.template = template;
    }
}
