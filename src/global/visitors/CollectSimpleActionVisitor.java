package global.visitors;

import base.ElementVisitor;
import com.intellij.openapi.project.Project;
import core.actions.custom.CreateDirectoryAction;
import core.actions.custom.CreateFileFromTemplateAction;
import core.actions.custom.base.SimpleAction;
import global.models.File;
import global.utils.templates.FileTemplateHelper;
import global.wrappers.DirectoryWrapper;
import global.wrappers.ElementWrapper;
import global.wrappers.FileWrapper;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Записывает элемент на файловую систему.
 */
public class CollectSimpleActionVisitor implements ElementVisitor {

    private Project project;

    public CollectSimpleActionVisitor(SimpleAction rootAction, Project project) {
        this.project = project;
        initStack(rootAction);
    }


    //=================================================================
    //  Stack
    //=================================================================
    private ArrayList<SimpleAction> stack;

    private void initStack(SimpleAction rootAction) {
        stack = new ArrayList<>();
        pushAction(rootAction);
    }

    private SimpleAction getLastAction() {
        return stack.get(stack.size() - 1);
    }

    private void popAction() {
        stack.remove(stack.size() - 1);
    }

    private void pushAction(SimpleAction simpleAction) {
        stack.add(simpleAction);
    }


    // ======================================================
    //  Write elements
    // ======================================================
    @Override
    public void visit(DirectoryWrapper wrapper) {
        if (!wrapper.getDirectory().isEnabled()) {
            return;
        }

        CreateDirectoryAction directoryAction = new CreateDirectoryAction(wrapper.getDirectory(), project);
        getLastAction().addAction(directoryAction);
        pushAction(directoryAction);

        for (ElementWrapper element : wrapper.getListElementWrapper()) {
            element.accept(this);
        }

        popAction();
    }

    @Override
    public void visit(FileWrapper wrapper) {
        File file = wrapper.getFile();

        if (!file.isEnabled()) {
            return;
        }

        Properties properties = new Properties();
        properties.putAll(wrapper.getPackageTemplateWrapper().getDefaultProperties());
        properties.putAll(wrapper.getFile().getMapProperties());

        getLastAction().addAction(new CreateFileFromTemplateAction(
                properties,
                FileTemplateHelper.getTemplate(wrapper.getFile().getTemplateName()),
                file.getName(),
                project
        ));
        //        FileWriter.writeFile(getLastAction(), wrapper);
    }

}
