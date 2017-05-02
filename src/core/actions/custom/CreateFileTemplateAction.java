package core.actions.custom;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.util.ArrayUtil;
import core.actions.custom.base.SimpleAction;
import core.report.ReportHelper;
import core.report.enums.ExecutionState;
import core.report.models.FailedActionReport;
import core.report.models.SuccessActionReport;
import global.utils.file.FileReaderUtil;
import global.utils.i18n.Localizer;
import global.utils.templates.FileTemplateHelper;
import global.utils.text.StringTools;

import java.io.File;
import java.util.Arrays;

/**
 * Created by Arsen on 09.01.2017.
 */
public class CreateFileTemplateAction extends SimpleAction {

    private File fileFrom;
    private Project project;
    private FileTemplate backupTemplate = null;

    public CreateFileTemplateAction(Project project, File fileFrom) {
        this.fileFrom = fileFrom;
        this.project = project;
    }

    @Override
    public void doRun() {
        ApplicationManager.getApplication().invokeAndWait(() -> {
            CommandProcessor.getInstance().executeCommand(project, () -> {
                ApplicationManager.getApplication().runWriteAction(() -> {
                    doRunInWrite();
                });
            }, getClass().getSimpleName(), getClass().getSimpleName());
        }, ModalityState.defaultModalityState());
    }

    private void doRunInWrite() {
        String text = FileReaderUtil.readFile(fileFrom);

        if (text == null) {
            ReportHelper.setState(ExecutionState.FAILED);
            ReportHelper.putReport(new FailedActionReport(this, Localizer.get("error.ReadFileContent"), "FileReaderUtil.readFile return null"));
            return;
        }


        FileTemplateManager ftm = FileTemplateHelper.getManagerInstance(project);
        FileTemplate oldTemplate = ftm.getTemplate(StringTools.getNameWithoutExtension(fileFrom.getName()));

        if (oldTemplate != null) {
            ftm.removeTemplate(oldTemplate);
//            ftm.saveAllTemplates();
        }

        FileTemplate[] templates = ftm.getAllTemplates();

        FileTemplate template = FileTemplateUtil.createTemplate(
                StringTools.getNameWithoutExtension(fileFrom.getName()),
                StringTools.getExtensionFromName(fileFrom.getName()),
                text, templates
        );
        ftm.setTemplates(FileTemplateManager.DEFAULT_TEMPLATES_CATEGORY, Arrays.asList(ArrayUtil.append(templates, template)));


        ReportHelper.putReport(new SuccessActionReport(this, toString()));
    }


    //=================================================================
    //  Utils
    //=================================================================
    private void restoreTemplate(FileTemplate newTemplate, FileTemplateManager ftm) {
        // Restore Old
        if (backupTemplate != null) {
            newTemplate.setText(backupTemplate.getText());
            newTemplate.setExtension(backupTemplate.getExtension());
            newTemplate.setLiveTemplateEnabled(backupTemplate.isLiveTemplateEnabled());
            newTemplate.setReformatCode(backupTemplate.isReformatCode());
        } else {
            ftm.removeTemplate(newTemplate);
        }
    }

    @Override
    public String toString() {
        return super.toString() + " " + fileFrom.getName();
    }
}
